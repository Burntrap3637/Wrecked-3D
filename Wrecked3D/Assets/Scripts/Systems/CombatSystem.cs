using UnityEngine;

namespace Wrecked
{
    /// <summary>
    /// Resolves all combat interactions: melee strikes, weapon damage, knockback,
    /// death handling, and deathmatch win conditions.
    /// </summary>
    public static class CombatSystem
    {
        // ── Strike ────────────────────────────────────────────────────────────

        /// <summary>
        /// Attempt a melee strike from attacker toward target.
        /// Returns true when the hit connects.
        /// </summary>
        public static bool TryStrike(CharacterBase attacker, CharacterBase target, ItemBase weapon)
        {
            if (attacker == null || target == null) return false;
            if (target.IsDead) return false;
            if (target.DT > 0) return false;   // still in damage-immunity window

            float range  = weapon != null ? ItemData.Get(weapon.ItemType).Range : 12f;
            float dist   = Vector3.Distance(attacker.Pos, target.Pos);
            if (dist > range) return false;

            int damage = weapon != null ? ItemData.Get(weapon.ItemType).Damage : 1;
            damage += CharacterData.Get(attacker.CharType).Attack;

            ApplyDamage(attacker, target, damage, weapon);
            return true;
        }

        // ── Damage application ────────────────────────────────────────────────

        public static void ApplyDamage(CharacterBase attacker, CharacterBase target, int amount, ItemBase weapon)
        {
            target.Health -= amount;
            target.DT      = 30;

            // Random limb scar
            int limb = Random.Range(1, 21);
            target.AddScar(limb, 64);

            // Particles
            var gm = GameManager.Instance;
            gm.PartMgr?.SpawnBlood(target.HeadPosition());
            AudioManager.Instance?.Hit();

            // Crime tracking — only when player 1 kills an innocent
            if (attacker.SlotIndex == 1 && target.CharType != GameConstants.CHAR_SOLDIER)
                gm.AddCrime(target.CharType);

            if (target.Health <= 0)
                HandleDeath(attacker, target, weapon);
        }

        // ── Death ─────────────────────────────────────────────────────────────

        private static void HandleDeath(CharacterBase killer, CharacterBase victim, ItemBase weapon)
        {
            victim.Health = 0;
            victim.Anim   = GameConstants.ANIM_DEAD;
            victim.State  = 2;   // dead

            var gm = GameManager.Instance;
            AudioManager.Instance?.Death();

            // Drop carried items at death position
            DropOnDeath(victim, gm);

            // Story: player death
            if (victim.SlotIndex == 1)
            {
                gm.PlayerDied();
                return;
            }

            // Deathmatch: check if only one remains
            if (gm.GameMode == GameConstants.MODE_DEATHMATCH)
                CheckDeathmatchWin(gm);

            // Story: mystic reward — gains meat from human kills
            if (killer != null && killer.CharType == GameConstants.CHAR_MYSTIC)
            {
                SpawnMeat(victim.Pos, gm);
            }
        }

        private static void DropOnDeath(CharacterBase c, GameManager gm)
        {
            if (c.CarryA != 0) { gm.ItemMgr?.Get(c.CarryA)?.Drop(c.Pos); c.CarryA = 0; }
            if (c.CarryB != 0) { gm.ItemMgr?.Get(c.CarryB)?.Drop(c.Pos); c.CarryB = 0; }

            // Always drop some meat from human/animal corpses
            var stats = CharacterData.Get(c.CharType);
            if (stats.MeatYield > 0)
                SpawnMeat(c.Pos, gm, stats.MeatYield);
        }

        private static void SpawnMeat(Vector3 pos, GameManager gm, int count = 1)
        {
            for (int n = 0; n < count; n++)
            {
                Vector3 scatter = pos + new Vector3(
                    Random.Range(-8f, 8f), 0f, Random.Range(-8f, 8f));

                for (int i = 1; i <= GameConstants.MAX_ITEMS; i++)
                {
                    var it = gm.ItemMgr?.Get(i);
                    if (it == null || it.State != 0) continue;
                    it.ItemType = GameConstants.ITEM_MEAT;
                    it.Island   = gm.CurrentIsland;
                    it.Pos      = scatter;
                    it.State    = 1;
                    if (it.SceneObject != null)
                    {
                        it.SceneObject.transform.position = scatter;
                        it.SceneObject.SetActive(true);
                    }
                    break;
                }
            }
        }

        // ── Deathmatch win check ──────────────────────────────────────────────

        private static void CheckDeathmatchWin(GameManager gm)
        {
            int alive  = 0;
            int winner = 0;

            for (int c = 1; c <= GameConstants.MAX_PLAYERS; c++)
            {
                var chr = gm.CharMgr?.Get(c);
                if (chr == null || !chr.IsActive) continue;
                if (chr.IsDead) continue;
                alive++;
                winner = c;
            }

            if (alive <= 1)
                gm.DeathMatchOver(winner);
        }

        // ── Area damage (fire, explosion) ─────────────────────────────────────

        public static void ApplyAreaDamage(Vector3 origin, float radius, int damage, GameManager gm)
        {
            for (int c = 1; c <= GameConstants.MAX_PLAYERS; c++)
            {
                var chr = gm.CharMgr?.Get(c);
                if (chr == null || !chr.IsActive || chr.IsDead) continue;
                if (Vector3.Distance(chr.Pos, origin) > radius) continue;
                if (chr.DT > 0) continue;

                ApplyDamage(null, chr, damage, null);
            }
        }
    }
}
