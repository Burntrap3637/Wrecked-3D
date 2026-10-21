using UnityEngine;

namespace Wrecked
{
    /// <summary>Runtime state for one resource slot (1-150).</summary>
    public class ResourceBase
    {
        public int     SlotIndex;
        public int     ResType;    // RES_* constant
        public int     Island;
        public int     State;      // 0=removed/destroyed, 1=active
        public Vector3 Pos;
        public float   Angle;
        public int     HP;

        public GameObject SceneObject;

        public bool IsActive => State == 1 && Island > 0;

        // ── Strike (chopping / mining) ────────────────────────────────────────

        public bool Strike(int damage, out int harvestType, out int harvestAmount)
        {
            harvestType   = 0;
            harvestAmount = 0;

            HP -= damage;
            if (HP > 0) return false;

            // Harvest yield based on type stats
            var stats = ResourceData.Get(ResType);
            HP    = 0;
            State = 0;
            if (SceneObject != null) SceneObject.SetActive(false);

            // Determine random yield
            float roll = Random.value;
            if (stats.YieldsWood  > 0 && roll < 0.5f) { harvestType = GameConstants.ITEM_WOOD;  harvestAmount = Random.Range(1, stats.YieldsWood  + 1); }
            else if (stats.YieldsStone > 0)             { harvestType = GameConstants.ITEM_STONE; harvestAmount = Random.Range(1, stats.YieldsStone + 1); }
            else if (stats.YieldsVines > 0)             { harvestType = GameConstants.ITEM_VINES; harvestAmount = Random.Range(1, stats.YieldsVines + 1); }
            else if (stats.YieldsFruit > 0)             { harvestType = GameConstants.ITEM_FRUIT; harvestAmount = Random.Range(1, stats.YieldsFruit + 1); }

            return true;
        }
    }
}
