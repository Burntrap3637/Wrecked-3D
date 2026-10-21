using UnityEngine;

namespace Wrecked
{
    /// <summary>
    /// Pooled particle effects: blood, impact, splash, smoke.
    /// Translated from particle handling in Players.bb / Values.bb (no_particles=400).
    /// </summary>
    public class ParticleManager : MonoBehaviour
    {
        [Header("Particle Prefabs")]
        public GameObject BloodPrefab;
        public GameObject ImpactPrefab;
        public GameObject SplashPrefab;
        public GameObject SmokePrefab;

        private GameObject[] _pool;
        private bool[]       _active;
        private int          _head;

        private void Awake()
        {
            _pool   = new GameObject[GameConstants.MAX_PARTICLES];
            _active = new bool[GameConstants.MAX_PARTICLES];
        }

        public void Initialize() { }

        // ── Spawn helpers ─────────────────────────────────────────────────────

        public void SpawnBlood(Vector3 pos)   => Spawn(BloodPrefab,  pos);
        public void SpawnImpact(Vector3 pos)  => Spawn(ImpactPrefab, pos);
        public void SpawnSplash(Vector3 pos)  => Spawn(SplashPrefab, pos);
        public void SpawnSmoke(Vector3 pos)   => Spawn(SmokePrefab,  pos);

        private void Spawn(GameObject prefab, Vector3 pos)
        {
            if (prefab == null) return;

            // Find or recycle a slot
            for (int i = 0; i < GameConstants.MAX_PARTICLES; i++)
            {
                int idx = (_head + i) % GameConstants.MAX_PARTICLES;
                if (_active[idx]) continue;

                _pool[idx]   = Instantiate(prefab, pos, Quaternion.identity);
                _active[idx] = true;
                _head        = (idx + 1) % GameConstants.MAX_PARTICLES;

                // Auto-destroy after particle duration
                Destroy(_pool[idx], 2f);
                return;
            }

            // Pool exhausted — just instantiate and let GC clean up
            var fx = Instantiate(prefab, pos, Quaternion.identity);
            Destroy(fx, 2f);
        }
    }
}
