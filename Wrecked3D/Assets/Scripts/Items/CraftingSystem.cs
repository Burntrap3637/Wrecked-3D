using UnityEngine;

namespace Wrecked
{
    /// <summary>
    /// Recipe-based item crafting: two carried items can combine to produce a third.
    /// Reflects the original game's crafting logic (e.g. Wood+Stone = Axe).
    /// </summary>
    public static class CraftingSystem
    {
        /// <summary>
        /// Recipe: (inputA type, inputB type) → output item type.
        /// Order of A and B does not matter.
        /// </summary>
        private static readonly (int a, int b, int result)[] Recipes =
        {
            (GameConstants.ITEM_WOOD,  GameConstants.ITEM_STONE, GameConstants.ITEM_HAMMER),
            (GameConstants.ITEM_WOOD,  GameConstants.ITEM_VINES, GameConstants.ITEM_SPEAR),
            (GameConstants.ITEM_STONE, GameConstants.ITEM_VINES, GameConstants.ITEM_WHIP),
            (GameConstants.ITEM_WOOD,  GameConstants.ITEM_MEAT,  GameConstants.ITEM_BONE),
            (GameConstants.ITEM_VINES, GameConstants.ITEM_STONE, GameConstants.ITEM_TRAP),
        };

        // ── Try to craft ──────────────────────────────────────────────────────

        /// <summary>
        /// Returns the output item type if a valid recipe exists for the two
        /// carried items, otherwise 0.
        /// </summary>
        public static int TryCraft(CharacterBase c, ItemManager mgr)
        {
            if (c.CarryA == 0 || c.CarryB == 0) return 0;
            var itmA = mgr.Get(c.CarryA);
            var itmB = mgr.Get(c.CarryB);
            if (itmA == null || itmB == null) return 0;

            int typeA = itmA.ItemType;
            int typeB = itmB.ItemType;

            foreach (var (a, b, result) in Recipes)
            {
                if ((typeA == a && typeB == b) || (typeA == b && typeB == a))
                    return result;
            }
            return 0;
        }

        /// <summary>
        /// Consumes both carried items and spawns a new crafted item at the character position.
        /// Returns slot index of the crafted item, or 0 on failure.
        /// </summary>
        public static int Craft(CharacterBase c, ItemManager mgr, int resultType)
        {
            if (resultType == 0) return 0;

            // Consume inputs
            mgr.Get(c.CarryA)?.Remove();
            mgr.Get(c.CarryB)?.Remove();
            c.CarryA = 0;
            c.CarryB = 0;

            // Find free slot and create result item
            for (int i = 1; i <= GameConstants.MAX_ITEMS; i++)
            {
                var it = mgr.Get(i);
                if (it.State != 0) continue;
                it.ItemType = resultType;
                it.Island   = GameManager.Instance.CurrentIsland;
                it.Pos      = c.Pos;
                it.State    = 1;
                return i;
            }
            return 0;
        }
    }
}
