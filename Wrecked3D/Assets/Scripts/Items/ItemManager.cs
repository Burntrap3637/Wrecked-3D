using UnityEngine;

namespace Wrecked
{
    /// <summary>
    /// Manages all 375 item slots: spawn, per-frame update (fire propagation),
    /// island-scoped queries.
    /// Translates iRange / item state handling from Gameplay.bb / Players.bb.
    /// </summary>
    public class ItemManager : MonoBehaviour
    {
        [Header("Item Prefabs (index = ITEM_* constant)")]
        public GameObject[] ItemPrefabs;   // 0-indexed by ITEM_* id

        private ItemBase[] _slots;

        // ── Item range per island (original iRange1/iRange2 = (island-1)*75+1 .. island*75) ─
        public static int RangeStart(int island) => (island - 1) * GameConstants.ITEMS_PER_ISLAND + 1;
        public static int RangeEnd(int island)   => island * GameConstants.ITEMS_PER_ISLAND;

        private void Awake()
        {
            _slots = new ItemBase[GameConstants.MAX_ITEMS + 1];
            for (int i = 1; i <= GameConstants.MAX_ITEMS; i++)
                _slots[i] = new ItemBase { SlotIndex = i };
        }

        public ItemBase Get(int slot) => _slots[slot];

        // ── SpawnAll ──────────────────────────────────────────────────────────

        public void SpawnAll()
        {
            var gm  = GameManager.Instance;
            int isl = gm.CurrentIsland;
            int r1  = RangeStart(isl);
            int r2  = RangeEnd(isl);

            for (int i = r1; i <= r2; i++)
            {
                var it = _slots[i];
                if (it.Island != isl) continue;
                if (it.ItemType == 0) continue;
                if (it.State == 0) SpawnRandom(it, isl);
                else               SpawnAt(it);
            }
        }

        private void SpawnRandom(ItemBase it, int island)
        {
            var isl = GameManager.Instance.IslandMgr;
            it.Pos = new Vector3(
                Random.Range(isl.WWest  + 50f, isl.WEast  - 50f),
                GameConstants.SEA_LEVEL,
                Random.Range(isl.WSouth + 50f, isl.WNorth - 50f));
            it.Island = island;
            it.State  = 1;
            SpawnAt(it);
        }

        private void SpawnAt(ItemBase it)
        {
            if (it.ItemType <= 0) return;
            int prefabIdx = it.ItemType < ItemPrefabs.Length ? it.ItemType : 0;
            var prefab    = prefabIdx < ItemPrefabs.Length ? ItemPrefabs[prefabIdx] : null;
            if (prefab == null) return;
            it.SceneObject = Instantiate(prefab, it.Pos, Quaternion.Euler(0, it.Angle, 0));
        }

        // ── Per-frame tick ────────────────────────────────────────────────────

        public void Tick()
        {
            var gm  = GameManager.Instance;
            int isl = gm.CurrentIsland;
            int r1  = RangeStart(isl);
            int r2  = RangeEnd(isl);

            for (int i = r1; i <= r2; i++)
            {
                var it = _slots[i];
                if (!it.IsActive) continue;

                // Sync carried-item position to carrier
                if (it.State == 2 && it.CarrierSlot > 0)
                {
                    var carrier = gm.CharMgr?.Get(it.CarrierSlot);
                    if (carrier != null && it.SceneObject != null)
                    {
                        // Attach to right hand limb
                        var hand = carrier.GetLimb(9);
                        if (hand != null) it.SceneObject.transform.position = hand.position;
                    }
                }
            }
        }

        // ── Proximity / range queries (used by AI) ────────────────────────────

        public bool InProximity(int itemSlot, Vector3 pos, float radius)
        {
            var it = _slots[itemSlot];
            return it.IsActive && Vector3.Distance(it.Pos, pos) <= radius;
        }

        public bool InRange(int itemSlot, Vector3 pos, float radius)
            => InProximity(itemSlot, pos, radius);
    }
}
