using UnityEngine;
using UnityEngine.SceneManagement;

namespace Wrecked
{
    /// <summary>
    /// Owns island boundary constants, mesh loading, and inter-island routing.
    /// Translated from LoadIsland() / LeaveIsland() / RelocatePlayer() in World.bb / Gameplay.bb.
    /// </summary>
    public class IslandManager : MonoBehaviour
    {
        // ── Loaded island ──────────────────────────────────────────────────────
        public int CurrentIsland { get; private set; }

        // ── World bounds (set per island, matches original float constants) ────
        public float WNorth { get; private set; }
        public float WSouth { get; private set; }
        public float WEast  { get; private set; }
        public float WWest  { get; private set; }

        // Water-edge sub-bounds (where the actual water surface starts)
        public float WaterNorth { get; private set; }
        public float WaterSouth { get; private set; }
        public float WaterEast  { get; private set; }
        public float WaterWest  { get; private set; }

        // Adjacency: which island is reached by crossing each edge (0 = ocean/exit)
        public int LeadNorth { get; private set; }
        public int LeadSouth { get; private set; }
        public int LeadEast  { get; private set; }
        public int LeadWest  { get; private set; }

        // ── Island prefabs ─────────────────────────────────────────────────────
        [Header("Island Prefabs")]
        public GameObject IslandNorthPrefab;
        public GameObject IslandEastPrefab;
        public GameObject IslandSouthPrefab;
        public GameObject IslandWestPrefab;
        public GameObject IslandCentrePrefab;

        [Tooltip("Optional procedural ground used when no island prefab is assigned.")]
        public IslandTerrain ProcTerrain;

        private GameObject _loadedIsland;

        // ══════════════════════════════════════════════════════════════════════

        public void LoadIsland(int island)
        {
            if (_loadedIsland != null) Destroy(_loadedIsland);
            CurrentIsland = island;
            ApplyIslandConfig(island);
            SpawnIslandMesh(island);
        }

        private void ApplyIslandConfig(int island)
        {
            switch (island)
            {
                // ── 1. North Coast ─────────────────────────────────────────────
                case GameConstants.ISLAND_NORTH:
                    WNorth = 600f;  WSouth = -700f;
                    WEast  = 620f;  WWest  = -750f;
                    WaterNorth = WNorth;  WaterSouth = 300f;
                    WaterEast  = WEast;   WaterWest  = WWest;
                    LeadNorth = 0; LeadSouth = 5; LeadEast = 2; LeadWest = 4;
                    break;

                // ── 2. East Coast ──────────────────────────────────────────────
                case GameConstants.ISLAND_EAST:
                    WNorth = 680f;  WSouth = -660f;
                    WEast  = 550f;  WWest  = -720f;
                    WaterNorth = WNorth;  WaterSouth = WSouth;
                    WaterEast  = WEast;   WaterWest  = 200f;
                    LeadNorth = 1; LeadSouth = 3; LeadEast = 0; LeadWest = 5;
                    break;

                // ── 3. South Coast ─────────────────────────────────────────────
                case GameConstants.ISLAND_SOUTH:
                    WNorth = 700f;  WSouth = -550f;
                    WEast  = 650f;  WWest  = -650f;
                    WaterNorth = -280f;   WaterSouth = WSouth;
                    WaterEast  = WEast;   WaterWest  = WWest;
                    LeadNorth = 5; LeadSouth = 0; LeadEast = 2; LeadWest = 4;
                    break;

                // ── 4. West Coast ──────────────────────────────────────────────
                case GameConstants.ISLAND_WEST:
                    WNorth = 700f;  WSouth = -650f;
                    WEast  = 650f;  WWest  = -600f;
                    WaterNorth = WNorth;  WaterSouth = WSouth;
                    WaterEast  = -420f;   WaterWest  = WWest;
                    LeadNorth = 1; LeadSouth = 3; LeadEast = 5; LeadWest = 0;
                    break;

                // ── 5. Centre ─────────────────────────────────────────────────
                case GameConstants.ISLAND_CENTRE:
                    WNorth = 550f;  WSouth = -540f;
                    WEast  = 680f;  WWest  = -680f;
                    WaterNorth = 0; WaterSouth = 0; WaterEast = 0; WaterWest = 0;
                    LeadNorth = 1; LeadSouth = 3; LeadEast = 2; LeadWest = 4;
                    break;
            }
        }

        private void SpawnIslandMesh(int island)
        {
            GameObject prefab = island switch
            {
                GameConstants.ISLAND_NORTH  => IslandNorthPrefab,
                GameConstants.ISLAND_EAST   => IslandEastPrefab,
                GameConstants.ISLAND_SOUTH  => IslandSouthPrefab,
                GameConstants.ISLAND_WEST   => IslandWestPrefab,
                GameConstants.ISLAND_CENTRE => IslandCentrePrefab,
                _ => null
            };
            if (prefab != null)
                _loadedIsland = Instantiate(prefab, Vector3.zero, Quaternion.identity);
            else if (ProcTerrain != null)
                ProcTerrain.BuildForIsland(island);
        }

        // ── Border check (called every frame by GameManager) ──────────────────

        /// <summary>Returns the destination island if the player has crossed a border, else 0.</summary>
        public int CheckBorder(Vector3 playerPos)
        {
            if (playerPos.z > WNorth && LeadNorth > 0) return LeadNorth;
            if (playerPos.z < WSouth && LeadSouth > 0) return LeadSouth;
            if (playerPos.x > WEast  && LeadEast  > 0) return LeadEast;
            if (playerPos.x < WWest  && LeadWest  > 0) return LeadWest;
            return 0;
        }

        // ── Player relocate spawn positions (from Gameplay.bb RelocatePlayer) ──

        /// <summary>
        /// Returns spawn position for a player arriving on <paramref name="toIsland"/>
        /// from <paramref name="fromIsland"/>.  Matches original BB coordinates exactly.
        /// </summary>
        public Vector3 RelocateSpawn(int fromIsland, int toIsland)
        {
            // Original format: pX(v)=?, pZ(v)=?  (Y is set by terrain snap)
            return (fromIsland, toIsland) switch
            {
                // ── arriving on NORTH ──────────────────────────────────────────
                (5, 1) => new Vector3( 300f, 0f,  400f),
                (4, 1) => new Vector3(-600f, 0f,    0f),
                (2, 1) => new Vector3( 500f, 0f,    0f),
                // ── arriving on EAST ──────────────────────────────────────────
                (1, 2) => new Vector3( 400f, 0f,  300f),
                (5, 2) => new Vector3( 400f, 0f, -200f),
                (3, 2) => new Vector3( 400f, 0f, -400f),
                // ── arriving on SOUTH ─────────────────────────────────────────
                (5, 3) => new Vector3( 300f, 0f, -300f),
                (4, 3) => new Vector3(-400f, 0f,    0f),
                (2, 3) => new Vector3( 400f, 0f,    0f),
                // ── arriving on WEST ──────────────────────────────────────────
                (1, 4) => new Vector3(-400f, 0f,  300f),
                (5, 4) => new Vector3(-400f, 0f,    0f),
                (3, 4) => new Vector3(-400f, 0f, -200f),
                // ── arriving on CENTRE ────────────────────────────────────────
                (1, 5) => new Vector3(   0f, 0f,  400f),
                (2, 5) => new Vector3( 400f, 0f,    0f),
                (3, 5) => new Vector3(   0f, 0f, -400f),
                (4, 5) => new Vector3(-400f, 0f,    0f),
                // fallback
                _      => new Vector3(   0f, 0f,    0f)
            };
        }

        // ── World-clamp helpers ────────────────────────────────────────────────

        public Vector3 ClampToIsland(Vector3 pos) => new Vector3(
            Mathf.Clamp(pos.x, WWest,  WEast),
            pos.y,
            Mathf.Clamp(pos.z, WSouth, WNorth));

        public bool IsInWater(Vector3 pos)
        {
            if (CurrentIsland == GameConstants.ISLAND_CENTRE) return false;
            return pos.z > WaterNorth || pos.z < WaterSouth ||
                   pos.x > WaterEast  || pos.x < WaterWest;
        }
    }
}
