using UnityEngine;

namespace Wrecked
{
    /// <summary>
    /// Per-character CPU brain — 4-agenda decision tree.
    /// Translated from ArtificialIntelligence() in AI.bb.
    /// Operates on CharacterBase data; no MonoBehaviour — driven by CharacterManager.
    /// </summary>
    public static class AIController
    {
        private const int MAX_SEARCH_ITS = 50;

        // ── Main entry (called per-tick for CPU-controlled characters) ─────────

        public static void Think(CharacterBase c)
        {
            var gm = GameManager.Instance;

            // Random agenda switch (matches original Rnd(-2,1000) mechanic)
            int randy = Random.Range(-2, 1001);
            if (randy <= 0) { c.Agenda = GameConstants.AGENDA_EXPLORE; c.Target = 0; }
            if (randy == 1) { c.Agenda = GameConstants.AGENDA_ATTACK;  c.Target = 0; }
            if ((c.CarryA == 0 || c.CarryB == 0) && randy == 2)
                { c.Agenda = GameConstants.AGENDA_RESOURCE; c.Target = 0; }
            if ((c.CarryA == 0 || c.CarryB == 0) && randy == 3)
                { c.Agenda = GameConstants.AGENDA_GATHER; c.Target = 0; }

            // Type restrictions
            if (c.Agenda == GameConstants.AGENDA_ATTACK  && CharacterData.Get(c.CharType).Power == 0)
                c.Agenda = GameConstants.AGENDA_EXPLORE;
            if (c.Agenda == GameConstants.AGENDA_RESOURCE && c.CharType >= 10)
                c.Agenda = GameConstants.AGENDA_EXPLORE;
            if (c.CharType == GameConstants.TYPE_MYSTIC || c.CharType == GameConstants.TYPE_FISH)
                c.Agenda = GameConstants.AGENDA_EXPLORE;

            switch (c.Agenda)
            {
                case GameConstants.AGENDA_EXPLORE:  Explore(c);  break;
                case GameConstants.AGENDA_ATTACK:   Attack(c);   break;
                case GameConstants.AGENDA_RESOURCE: Resource(c); break;
                case GameConstants.AGENDA_GATHER:   Gather(c);   break;
            }
        }

        // ── Explore ───────────────────────────────────────────────────────────

        private static void Explore(CharacterBase c)
        {
            c.Target = 0;
            int randy = Random.Range(1, 801);
            if (randy == 1)
                c.TargetAngle = Random.Range(0f, 360f);
        }

        // ── Attack ────────────────────────────────────────────────────────────

        private static void Attack(CharacterBase c)
        {
            var gm   = GameManager.Instance;
            var mgr  = gm.CharMgr;
            int isl  = gm.CurrentIsland;

            if (c.Target == 0)
            {
                c.Target = FindVictim(c, mgr, isl);
                if (c.Target == 0) { c.Agenda = GameConstants.AGENDA_EXPLORE; return; }
            }

            var victim = mgr.Get(c.Target);
            if (!VictimViable(c, victim)) { c.Target = 0; c.Agenda = GameConstants.AGENDA_EXPLORE; return; }

            c.TargetPos = victim.Pos;
        }

        // ── Resource gathering ────────────────────────────────────────────────

        private static void Resource(CharacterBase c)
        {
            var gm   = GameManager.Instance;
            var resMgr = gm.ResMgr;
            int isl  = gm.CurrentIsland;

            if (c.Target == 0)
            {
                c.Target = FindResource(c, resMgr, isl);
                if (c.Target == 0) { c.Agenda = GameConstants.AGENDA_EXPLORE; return; }
            }

            var res = resMgr.Get(c.Target);
            if (res == null || res.State == 0 || res.Island != isl)
                { c.Target = 0; c.Agenda = GameConstants.AGENDA_EXPLORE; return; }

            c.TargetPos = new Vector3(res.Pos.x, c.Pos.y, res.Pos.z);
        }

        // ── Item gathering ────────────────────────────────────────────────────

        private static void Gather(CharacterBase c)
        {
            var gm  = GameManager.Instance;
            var itmMgr = gm.ItemMgr;
            int isl = gm.CurrentIsland;

            if (c.Target == 0)
            {
                c.Target = FindItem(c, itmMgr, isl);
                if (c.Target == 0) { c.Agenda = GameConstants.AGENDA_EXPLORE; return; }
            }

            var itm = itmMgr.Get(c.Target);
            if (itm == null || itm.State == 0 || itm.Island != isl)
                { c.Target = 0; c.Agenda = GameConstants.AGENDA_EXPLORE; return; }

            c.TargetPos = new Vector3(itm.Pos.x, c.Pos.y, itm.Pos.z);
        }

        // ── Viability checks ──────────────────────────────────────────────────

        private static bool VictimViable(CharacterBase attacker, CharacterBase victim)
        {
            if (victim == null || !victim.IsActive || victim.IsDead) return false;
            if (victim.SlotIndex == attacker.SlotIndex)  return false;
            if (victim.Island != attacker.Island)         return false;
            return true;
        }

        // ── Finders ───────────────────────────────────────────────────────────

        private static int FindVictim(CharacterBase c, CharacterManager mgr, int island)
        {
            for (int its = 0; its < MAX_SEARCH_ITS; its++)
            {
                int candidate = Random.Range(1, GameConstants.MAX_PLAYERS + 1);
                var v = mgr.Get(candidate);
                if (VictimViable(c, v)) return candidate;
            }
            return 0;
        }

        private static int FindResource(CharacterBase c, ResourceManager mgr, int island)
        {
            for (int its = 0; its < MAX_SEARCH_ITS; its++)
            {
                int candidate = Random.Range(1, GameConstants.MAX_RESOURCES + 1);
                var r = mgr.Get(candidate);
                if (r != null && r.IsActive && r.Island == island) return candidate;
            }
            return 0;
        }

        private static int FindItem(CharacterBase c, ItemManager mgr, int island)
        {
            for (int its = 0; its < MAX_SEARCH_ITS; its++)
            {
                int candidate = Random.Range(1, GameConstants.MAX_ITEMS + 1);
                var itm = mgr.Get(candidate);
                if (itm != null && itm.State == 1 && itm.Island == island) return candidate;
            }
            return 0;
        }

        // ── Movement direction helpers ─────────────────────────────────────────

        /// <summary>Returns whether character has reached its target destination.</summary>
        public static bool ReachedDest(CharacterBase c)
        {
            float dx = c.TargetPos.x - c.Pos.x;
            float dz = c.TargetPos.z - c.Pos.z;
            return (dx * dx + dz * dz) < 25f;   // within ~5 units
        }

        /// <summary>Returns steering direction to target: +1=left, -1=right, 0=straight.</summary>
        public static float SteerToTarget(CharacterBase c)
        {
            if (ReachedDest(c)) return 0f;
            float desiredYaw = Mathf.Atan2(c.TargetPos.x - c.Pos.x,
                                            c.TargetPos.z - c.Pos.z) * Mathf.Rad2Deg;
            float diff = Mathf.DeltaAngle(c.Angle, desiredYaw);
            return diff > 0f ? 1f : -1f;
        }

        /// <summary>Enemy proximity check — returns slot index of nearest threat within radius.</summary>
        public static int EnemyProximity(CharacterBase c, float radius)
        {
            var mgr = GameManager.Instance.CharMgr;
            int isl = GameManager.Instance.CurrentIsland;
            for (int i = 1; i <= GameConstants.MAX_PLAYERS; i++)
            {
                if (i == c.SlotIndex) continue;
                var e = mgr.Get(i);
                if (!e.IsActive || e.Island != isl) continue;
                float dist = Vector3.Distance(c.Pos, e.Pos);
                if (dist <= radius) return i;
            }
            return 0;
        }
    }
}
