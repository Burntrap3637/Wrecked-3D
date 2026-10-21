using UnityEngine;

namespace Wrecked
{
    /// <summary>
    /// Generates or configures the physical terrain for each island.
    /// In a shipped build the TerrainData assets would be hand-authored;
    /// this procedural fallback lets you iterate without art.
    /// </summary>
    [RequireComponent(typeof(Terrain))]
    public class IslandTerrain : MonoBehaviour
    {
        [Header("Terrain Generation")]
        public int   Resolution    = 257;
        public float MaxHeight     = 120f;
        public float NoiseScale    = 0.003f;
        public float IslandFalloff = 1.8f;   // steepness of coastal slope

        [Header("Terrain Size")]
        public float Width  = 1400f;
        public float Length = 1400f;

        [Header("Water")]
        public WaterController Water;
        public float WaterLevel = GameConstants.SEA_LEVEL;

        private Terrain    _terrain;
        private TerrainData _data;

        // ─────────────────────────────────────────────────────────────────────

        public void BuildForIsland(int islandId)
        {
            _terrain = GetComponent<Terrain>();
            _data    = new TerrainData();

            _data.heightmapResolution = Resolution;
            _data.size = new Vector3(Width, MaxHeight, Length);

            _data.SetHeights(0, 0, GenerateHeightmap(islandId));
            _terrain.terrainData = _data;

            // Centre the terrain so island origin is at (0,0,0)
            transform.position = new Vector3(-Width * 0.5f, 0f, -Length * 0.5f);

            Water?.SetWaterLevel(WaterLevel);
        }

        // ── Heightmap ─────────────────────────────────────────────────────────

        private float[,] GenerateHeightmap(int islandId)
        {
            int res = Resolution;
            var heights = new float[res, res];

            // Seed varies per island so they all look different
            float offsetX = islandId * 137.3f;
            float offsetZ = islandId * 289.1f;
            float cx = res * 0.5f;
            float cz = res * 0.5f;
            float maxDist = Mathf.Sqrt(cx * cx + cz * cz);

            for (int z = 0; z < res; z++)
            {
                for (int x = 0; x < res; x++)
                {
                    float nx = (x + offsetX) * NoiseScale;
                    float nz = (z + offsetZ) * NoiseScale;

                    float noise = Mathf.PerlinNoise(nx, nz)
                                + Mathf.PerlinNoise(nx * 2f, nz * 2f) * 0.5f
                                + Mathf.PerlinNoise(nx * 4f, nz * 4f) * 0.25f;
                    noise /= 1.75f;

                    // Radial falloff — island rises from the sea
                    float dx    = x - cx;
                    float dz    = z - cz;
                    float dist  = Mathf.Sqrt(dx * dx + dz * dz) / maxDist;
                    float shape = Mathf.Pow(1f - Mathf.Clamp01(dist), IslandFalloff);

                    heights[z, x] = Mathf.Clamp01(noise * shape);
                }
            }

            return heights;
        }

        // ── Spawn helpers ─────────────────────────────────────────────────────

        /// <summary>
        /// Sample terrain height at world-space XZ, clamped above water.
        /// </summary>
        public float HeightAt(float worldX, float worldZ)
        {
            if (_terrain == null) return WaterLevel;
            return Mathf.Max(_terrain.SampleHeight(new Vector3(worldX, 0f, worldZ)), WaterLevel);
        }

        /// <summary>
        /// Random land position at least minHeight above sea level.
        /// </summary>
        public Vector3 RandomLandPoint(float minHeight = 1f)
        {
            var isl = GameManager.Instance.IslandMgr;
            for (int attempt = 0; attempt < 50; attempt++)
            {
                float x = Random.Range(isl.WWest, isl.WEast);
                float z = Random.Range(isl.WSouth, isl.WNorth);
                float y = HeightAt(x, z);
                if (y >= WaterLevel + minHeight)
                    return new Vector3(x, y, z);
            }
            return new Vector3(0f, WaterLevel + minHeight, 0f);
        }
    }
}
