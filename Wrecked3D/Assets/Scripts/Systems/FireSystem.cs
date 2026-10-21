using UnityEngine;

namespace Wrecked
{
    /// <summary>
    /// Manages up to 5 simultaneous fires.  Translated from fire logic in
    /// World.bb / Players.bb (fireState, fireSource, fireTim, fire spreading).
    /// </summary>
    public class FireSystem : MonoBehaviour
    {
        private const int MAX_FIRES = GameConstants.MAX_FIRES;

        // ── Fire state ─────────────────────────────────────────────────────────
        private int[]     _state    = new int[MAX_FIRES + 1];    // 0=out, 1=burning
        private int[]     _source   = new int[MAX_FIRES + 1];    // character/item that started it
        private Vector3[] _position = new Vector3[MAX_FIRES + 1];
        private int[]     _timer    = new int[MAX_FIRES + 1];    // ticks remaining

        [Header("Fire Prefab")]
        public GameObject FirePrefab;
        public float      FireRadius = 25f;
        public float      SpreadRadius = 40f;
        public int        DefaultDuration = 3000;

        private GameObject[] _fireObjects = new GameObject[MAX_FIRES + 1];

        // ══════════════════════════════════════════════════════════════════════

        public void Initialize() { /* fires restored by SaveSystem */ }

        // ── Public accessors (used by SaveSystem) ──────────────────────────────

        public int     GetState(int slot)    => _state[slot];
        public int     GetSource(int slot)   => _source[slot];
        public Vector3 GetPosition(int slot) => _position[slot];
        public int     GetTimer(int slot)    => _timer[slot];

        public void Restore(int slot, int state, int source, Vector3 pos, int timer)
        {
            _state[slot]    = state;
            _source[slot]   = source;
            _position[slot] = pos;
            _timer[slot]    = timer;
            if (state > 0) SpawnFireFX(slot);
        }

        // ── Ignite ────────────────────────────────────────────────────────────

        public bool Ignite(Vector3 pos, int source = 0)
        {
            for (int i = 1; i <= MAX_FIRES; i++)
            {
                if (_state[i] != 0) continue;
                _state[i]    = 1;
                _source[i]   = source;
                _position[i] = pos;
                _timer[i]    = DefaultDuration;
                SpawnFireFX(i);
                return true;
            }
            return false;    // all slots full
        }

        // ── Per-frame tick ────────────────────────────────────────────────────

        public void Tick()
        {
            var gm = GameManager.Instance;

            for (int i = 1; i <= MAX_FIRES; i++)
            {
                if (_state[i] == 0) continue;

                _timer[i]--;
                if (_timer[i] <= 0) { Extinguish(i); continue; }

                // Damage nearby characters
                BurnNearby(i, gm);

                // Ignite nearby flammable items
                TrySpread(i, gm);
            }
        }

        // ── Proximity check (used by AI to avoid fire) ─────────────────────────

        public bool FireProximity(Vector3 pos, float radius)
        {
            for (int i = 1; i <= MAX_FIRES; i++)
            {
                if (_state[i] == 0) continue;
                if (Vector3.Distance(pos, _position[i]) <= radius) return true;
            }
            return false;
        }

        // ── Private ───────────────────────────────────────────────────────────

        private void BurnNearby(int slot, GameManager gm)
        {
            int isl = gm.CurrentIsland;
            for (int c = 1; c <= GameConstants.MAX_PLAYERS; c++)
            {
                var chr = gm.CharMgr?.Get(c);
                if (chr == null || chr.Island != isl || !chr.IsActive) continue;
                if (Vector3.Distance(chr.Pos, _position[slot]) > FireRadius) continue;
                if (chr.DT > 0) continue;
                chr.Health -= 2;
                chr.DT      = 30;
            }
        }

        private void TrySpread(int slot, GameManager gm)
        {
            int isl = gm.CurrentIsland;
            if (Random.Range(0, 300) != 0) return;

            // Attempt to ignite a nearby flammable item
            for (int i = ItemManager.RangeStart(isl); i <= ItemManager.RangeEnd(isl); i++)
            {
                var itm = gm.ItemMgr?.Get(i);
                if (itm == null || !itm.IsActive) continue;
                if (!ItemData.Get(itm.ItemType).Flammable) continue;
                if (Vector3.Distance(itm.Pos, _position[slot]) > SpreadRadius) continue;
                itm.Flaming = true;
                Ignite(itm.Pos, i);
                break;
            }
        }

        private void Extinguish(int slot)
        {
            _state[slot] = 0;
            _timer[slot] = 0;
            if (_fireObjects[slot] != null) { Destroy(_fireObjects[slot]); _fireObjects[slot] = null; }
        }

        private void SpawnFireFX(int slot)
        {
            if (FirePrefab == null) return;
            if (_fireObjects[slot] != null) Destroy(_fireObjects[slot]);
            _fireObjects[slot] = Instantiate(FirePrefab, _position[slot], Quaternion.identity);
        }
    }
}
