using UnityEngine;

namespace Wrecked
{
    /// <summary>
    /// Reads keyboard / gamepad input for the human player and translates it into
    /// movement commands on their CharacterBase.  Equivalent to GetInput() +
    /// TranslateInput() + ApplyMovement() in AI.bb / Players.bb.
    /// </summary>
    public class PlayerController : MonoBehaviour
    {
        // ── Key bindings (mirror originals; customisable) ──────────────────────
        [Header("Key Bindings")]
        public KeyCode KeyAttackA = KeyCode.Z;
        public KeyCode KeyAttackB = KeyCode.X;
        public KeyCode KeyPickA   = KeyCode.A;
        public KeyCode KeyPickB   = KeyCode.S;
        public KeyCode KeyJump    = KeyCode.Space;
        public KeyCode KeySleep   = KeyCode.Return;

        // ── Slot reference ─────────────────────────────────────────────────────
        public int PlayerSlot = 1;

        // ── Movement constants ─────────────────────────────────────────────────
        [Header("Movement")]
        public float WalkSpeed    = 3.0f;
        public float TurnSpeed    = 180f;
        public float JumpForce    = 8.0f;

        // ── Internal ───────────────────────────────────────────────────────────
        private bool _attackA, _attackB, _pickA, _pickB, _jump;
        private float _horizontal, _vertical;

        // ══════════════════════════════════════════════════════════════════════

        private void Update()
        {
            var gm = GameManager.Instance;
            if (gm.Paused) return;

            var c = gm.CharMgr?.Get(PlayerSlot);
            if (c == null || !c.IsActive || c.IsDead) return;

            ReadInput();
            ApplyInput(c);
        }

        private void ReadInput()
        {
            _vertical    =  (Input.GetKey(KeyCode.UpArrow)    ? 1f : 0f)
                          - (Input.GetKey(KeyCode.DownArrow)   ? 1f : 0f);
            _horizontal  =  (Input.GetKey(KeyCode.RightArrow)  ? 1f : 0f)
                          - (Input.GetKey(KeyCode.LeftArrow)   ? 1f : 0f);
            _attackA     = Input.GetKey(KeyAttackA);
            _attackB     = Input.GetKey(KeyAttackB);
            _pickA       = Input.GetKey(KeyPickA) || Input.GetKey(KeySleep);
            _pickB       = Input.GetKey(KeyPickB) || Input.GetKey(KeySleep);
            _jump        = Input.GetKeyDown(KeyJump);

            // Gamepad overlay
            _vertical   += Input.GetAxis("Vertical");
            _horizontal += Input.GetAxis("Horizontal");
        }

        private void ApplyInput(CharacterBase c)
        {
            // Rotation
            c.Angle += _horizontal * TurnSpeed * Time.deltaTime;

            // Movement
            if (_vertical > 0.1f)
            {
                Vector3 dir = Quaternion.Euler(0, c.Angle, 0) * Vector3.forward;
                var stats = CharacterData.Get(c.CharType);
                c.Pos += dir * (WalkSpeed * stats.Speed * c.SpeedMultiplier) * Time.deltaTime;
                c.Anim = GameConstants.ANIM_WALK;
            }
            else
            {
                c.Anim = GameConstants.ANIM_IDLE;
            }

            // Jump
            if (_jump && c.Gravity == 0f)
            {
                c.Gravity = JumpForce;
                c.Anim    = GameConstants.ANIM_JUMP;
            }

            // Enforce island bounds
            var isl = GameManager.Instance.IslandMgr;
            if (isl != null) c.Pos = isl.ClampToIsland(c.Pos);

            // Actions
            if (_attackA) TryAttack(c, isLeft: true);
            if (_attackB) TryAttack(c, isLeft: false);
            if (_pickA)   TryPick(c, isLeft: true);
            if (_pickB)   TryPick(c, isLeft: false);
        }

        // ── Actions ───────────────────────────────────────────────────────────

        private void TryAttack(CharacterBase c, bool isLeft)
        {
            if (c.AnimTimer > 0) return;

            int weapon = isLeft ? c.CarryA : c.CarryB;
            var stats  = CharacterData.Get(c.CharType);
            float range = stats.Range;

            if (weapon > 0)
            {
                var itm = GameManager.Instance.ItemMgr?.Get(weapon);
                if (itm != null) range = ItemData.Get(itm.ItemType).Range;
            }

            // High/low aim alternates
            c.Anim    = isLeft ? GameConstants.ANIM_ATK_HIGH_L : GameConstants.ANIM_ATK_HIGH_R;
            c.AnimTimer = 15;

            // Hit scan
            var mgr = GameManager.Instance.CharMgr;
            int isl = GameManager.Instance.CurrentIsland;
            for (int i = 1; i <= GameConstants.MAX_PLAYERS; i++)
            {
                var target = mgr.Get(i);
                if (!target.IsActive || target.Island != isl) continue;
                if (i == c.SlotIndex) continue;
                if (Vector3.Distance(c.Pos, target.Pos) > range) continue;

                int dmg = stats.Attack + (weapon > 0 ? ItemData.Get(GameManager.Instance.ItemMgr.Get(weapon).ItemType).Damage : 0);
                DealDamage(c, target, dmg, isLeft);
                break;
            }
        }

        private void DealDamage(CharacterBase attacker, CharacterBase victim, int damage, bool leftHand)
        {
            if (victim.DT > 0) return;
            victim.Health -= damage;
            victim.DT      = 30;

            // Random limb scar
            int limb = Random.Range(1, 21);
            victim.AddScar(limb, Random.Range(1, 4));

            if (victim.Health <= 0)
            {
                victim.Health = 0;
                victim.Anim   = GameConstants.ANIM_DEAD;
                if (attacker.SlotIndex == 1)
                    GameManager.Instance.AddCrime(GameConstants.TYPE_PLAYER < 10 ? 3 : 2);
            }
            else
            {
                victim.Anim    = GameConstants.ANIM_PAIN_HIGH;
                victim.AnimTimer = 10;
            }
        }

        private void TryPick(CharacterBase c, bool isLeft)
        {
            if (c.AnimTimer > 0) return;
            int hand = isLeft ? c.CarryA : c.CarryB;

            if (hand > 0)
            {
                // Drop
                var itm = GameManager.Instance.ItemMgr?.Get(hand);
                if (itm != null) { itm.Drop(c.Pos); }
                if (isLeft) c.CarryA = 0; else c.CarryB = 0;
                c.Anim    = isLeft ? GameConstants.ANIM_DROP_LEFT : GameConstants.ANIM_DROP_RIGHT;
                c.AnimTimer = 10;
                return;
            }

            // Pick up nearest item
            var itmMgr = GameManager.Instance.ItemMgr;
            if (itmMgr == null) return;
            int isl    = GameManager.Instance.CurrentIsland;
            int best   = 0;
            float bestDist = 12f;

            for (int i = 1; i <= GameConstants.MAX_ITEMS; i++)
            {
                var itm = itmMgr.Get(i);
                if (itm == null || itm.State != 1 || itm.Island != isl) continue;
                float d = Vector3.Distance(c.Pos, itm.Pos);
                if (d < bestDist) { bestDist = d; best = i; }
            }

            if (best > 0)
            {
                itmMgr.Get(best).PickUp(c);
                if (isLeft) c.CarryA = best; else c.CarryB = best;
                c.Anim    = isLeft ? GameConstants.ANIM_PICK_LEFT : GameConstants.ANIM_PICK_RIGHT;
                c.AnimTimer = 10;
            }
        }
    }
}
