using UnityEngine;

namespace Wrecked
{
    /// <summary>
    /// Manages up to 5 boats.  Boats sit at sea-level near island edges and
    /// trigger island travel when a character boards and stays on them.
    /// Translated from boatState / boatX / boatZ / boatA handling in Gameplay.bb.
    /// </summary>
    public class BoatSystem : MonoBehaviour
    {
        private const int MAX_BOATS = GameConstants.MAX_BOATS;

        private int[]     _state    = new int[MAX_BOATS + 1];
        private Vector3[] _position = new Vector3[MAX_BOATS + 1];
        private float[]   _angle    = new float[MAX_BOATS + 1];
        private int[]     _passenger= new int[MAX_BOATS + 1];   // character slot
        private int[]     _destination = new int[MAX_BOATS + 1];

        [Header("Boat Prefab")]
        public GameObject BoatPrefab;
        public float      BoardRadius = 15f;
        public float      BoatSpeed   = 60f;    // units/tick while departing
        public int        JourneyTicks = 300;   // ticks until island change

        private GameObject[] _boatObjects = new GameObject[MAX_BOATS + 1];
        private int[]        _journeyTim   = new int[MAX_BOATS + 1];

        // ══════════════════════════════════════════════════════════════════════

        public void Initialize()
        {
            PlaceBoatsAtDocks();
        }

        // ── SaveSystem accessors ───────────────────────────────────────────────

        public int     GetState(int slot)    => _state[slot];
        public Vector3 GetPosition(int slot) => _position[slot];
        public float   GetAngle(int slot)    => _angle[slot];

        public void Restore(int slot, int state, Vector3 pos, float angle)
        {
            _state[slot]    = state;
            _position[slot] = pos;
            _angle[slot]    = angle;
            if (state > 0) SpawnBoat(slot);
        }

        // ── Per-frame tick ────────────────────────────────────────────────────

        public void Tick()
        {
            var gm  = GameManager.Instance;
            int isl = gm.CurrentIsland;

            for (int b = 1; b <= MAX_BOATS; b++)
            {
                if (_state[b] == 0) continue;

                // Check for a passenger boarding
                if (_passenger[b] == 0)
                    CheckBoarding(b, gm, isl);

                // Journey in progress
                if (_passenger[b] > 0)
                {
                    _journeyTim[b]++;
                    if (_journeyTim[b] >= JourneyTicks)
                        CompleteJourney(b, gm);
                }

                // Sync scene object
                if (_boatObjects[b] != null)
                    _boatObjects[b].transform.position = _position[b];
            }
        }

        private void CheckBoarding(int slot, GameManager gm, int isl)
        {
            for (int c = 1; c <= GameConstants.MAX_PLAYERS; c++)
            {
                var chr = gm.CharMgr?.Get(c);
                if (chr == null || chr.Island != isl || !chr.IsActive) continue;
                if (Vector3.Distance(chr.Pos, _position[slot]) > BoardRadius) continue;

                // Board
                _passenger[slot]   = c;
                _journeyTim[slot]  = 0;
                _destination[slot] = DetermineDestination(slot, isl);
                chr.Anim = GameConstants.ANIM_BOAT;

                if (c == 1)
                {
                    gm.GameDestination = _destination[slot];
                    gm.GameJourney     = slot;
                }
                break;
            }
        }

        private void CompleteJourney(int slot, GameManager gm)
        {
            int passenger = _passenger[slot];
            _passenger[slot]   = 0;
            _journeyTim[slot]  = 0;
            _state[slot]       = 0;

            if (passenger == 1)
                gm.EndIsland();   // triggers island transition
        }

        private int DetermineDestination(int boatSlot, int currentIsland)
        {
            // Boats 1-4 map to compass leads; boat 5 to centre
            var isl = GameManager.Instance.IslandMgr;
            return boatSlot switch
            {
                1 => isl.LeadNorth,
                2 => isl.LeadEast,
                3 => isl.LeadSouth,
                4 => isl.LeadWest,
                5 => isl.LeadNorth,
                _ => 0
            };
        }

        // ── Dock placement ────────────────────────────────────────────────────

        private void PlaceBoatsAtDocks()
        {
            var isl = GameManager.Instance.IslandMgr;

            // One boat per navigable edge
            int b = 1;
            if (isl.LeadNorth > 0) PlaceBoat(b++, new Vector3(0f, GameConstants.SEA_LEVEL, isl.WNorth - 30f), 0f);
            if (isl.LeadEast  > 0) PlaceBoat(b++, new Vector3(isl.WEast  - 30f, GameConstants.SEA_LEVEL, 0f), 90f);
            if (isl.LeadSouth > 0) PlaceBoat(b++, new Vector3(0f, GameConstants.SEA_LEVEL, isl.WSouth + 30f), 180f);
            if (isl.LeadWest  > 0) PlaceBoat(b++, new Vector3(isl.WWest  + 30f, GameConstants.SEA_LEVEL, 0f), 270f);
        }

        private void PlaceBoat(int slot, Vector3 pos, float angle)
        {
            if (slot > MAX_BOATS) return;
            _state[slot]    = 1;
            _position[slot] = pos;
            _angle[slot]    = angle;
            SpawnBoat(slot);
        }

        private void SpawnBoat(int slot)
        {
            if (BoatPrefab == null) return;
            if (_boatObjects[slot] != null) Destroy(_boatObjects[slot]);
            _boatObjects[slot] = Instantiate(BoatPrefab,
                _position[slot],
                Quaternion.Euler(0f, _angle[slot], 0f));
        }
    }
}
