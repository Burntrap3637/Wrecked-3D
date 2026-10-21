using UnityEngine;

namespace Wrecked
{
    /// <summary>Manages all 150 resource slots: spawn and retrieval.</summary>
    public class ResourceManager : MonoBehaviour
    {
        [Header("Resource Prefabs (index = RES_* constant)")]
        public GameObject[] ResourcePrefabs;

        private ResourceBase[] _slots;

        private void Awake()
        {
            _slots = new ResourceBase[GameConstants.MAX_RESOURCES + 1];
            for (int i = 1; i <= GameConstants.MAX_RESOURCES; i++)
                _slots[i] = new ResourceBase { SlotIndex = i };
        }

        public ResourceBase Get(int slot) => _slots[slot];

        public void SpawnAll()
        {
            var gm  = GameManager.Instance;
            int isl = gm.CurrentIsland;

            for (int i = 1; i <= GameConstants.MAX_RESOURCES; i++)
            {
                var r = _slots[i];
                if (r.Island != isl) continue;
                if (r.ResType == 0) continue;
                SpawnAt(r);
            }
        }

        private void SpawnAt(ResourceBase r)
        {
            if (r.State == 0) return;
            int idx    = r.ResType < ResourcePrefabs.Length ? r.ResType : 0;
            var prefab = idx < ResourcePrefabs.Length ? ResourcePrefabs[idx] : null;
            if (prefab == null) return;
            r.SceneObject = Instantiate(prefab,
                r.Pos, Quaternion.Euler(0f, r.Angle, 0f));
            // Reset HP from stats
            if (r.HP == 0) r.HP = ResourceData.Get(r.ResType).Strength;
        }
    }
}
