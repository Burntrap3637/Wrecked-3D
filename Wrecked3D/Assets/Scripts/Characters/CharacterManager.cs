using System.Collections.Generic;
using UnityEngine;

namespace Wrecked
{
    /// <summary>
    /// Manages all 60 character slots: spawn, per-frame update,
    /// population counting, and island relocation.
    /// Translates LoadPlayers() / PlayerCycle() / RelocatePlayer() from Players.bb / Gameplay.bb.
    /// </summary>
    public class CharacterManager : MonoBehaviour
    {
        // ── Prefabs ────────────────────────────────────────────────────────────
        [Header("Character Prefabs")]
        public GameObject HumanPrefab;
        public GameObject SoldierPrefab;
        public GameObject MysticPrefab;
        public GameObject NativePrefab;
        public GameObject LeopardPrefab;
        public GameObject WolfPrefab;
        public GameObject DeerPrefab;
        public GameObject RabbitPrefab;
        public GameObject FishPrefab;

        // ── Subsystems ────────────────────────────────────────────────────────
        public CostumeSystem CostumeSys;
        public ScarSystem    ScarSys;

        // ── Slot pool ─────────────────────────────────────────────────────────
        private CharacterBase[] _slots;

        // ══════════════════════════════════════════════════════════════════════

        private void Awake()
        {
            _slots = new CharacterBase[GameConstants.MAX_PLAYERS + 1];
            for (int i = 1; i <= GameConstants.MAX_PLAYERS; i++)
                _slots[i] = new CharacterBase { SlotIndex = i };
        }

        /// <summary>Returns the character data for a given slot (1-60).</summary>
        public CharacterBase Get(int slot) => _slots[slot];

        // ── SpawnAll: called by GameManager.BeginIsland() ─────────────────────

        public void SpawnAll()
        {
            var gm  = GameManager.Instance;
            int isl = gm.CurrentIsland;

            for (int i = 1; i <= GameConstants.MAX_PLAYERS; i++)
            {
                var c = _slots[i];
                if (c.Island != isl) continue;
                SpawnCharacter(c, isl);
            }
        }

        private void SpawnCharacter(CharacterBase c, int island)
        {
            GameObject prefab = PrefabForType(c.CharType);
            if (prefab == null) return;

            // Random spawn if position is still at origin
            if (c.Pos == Vector3.zero)
            {
                var isl = GameManager.Instance.IslandMgr;
                c.Pos = new Vector3(
                    Random.Range(isl.WWest  + 100f, isl.WEast  - 100f),
                    110f,
                    Random.Range(isl.WSouth + 100f, isl.WNorth - 100f));
                c.Angle = Random.Range(0f, 360f);
            }

            // Mystic has a fixed spawn position
            if (c.CharType == GameConstants.TYPE_MYSTIC)
            {
                c.Pos = new Vector3(-386f, 101f, -614f);
                c.Angle = 330f;
            }

            c.SceneObject = Instantiate(prefab,
                c.Pos + Vector3.up * 3f,
                Quaternion.Euler(0f, c.Angle, 0f));
            c.Animator = c.SceneObject.GetComponentInChildren<Animator>();

            // Cache limb transforms
            BindLimbs(c);

            // Apply costume
            CostumeSys?.Apply(c);

            // Ensure default costume
            AssignDefaultCostume(c);
        }

        private void AssignDefaultCostume(CharacterBase c)
        {
            if (c.Costume > 0) return;
            c.Costume = c.CharType switch
            {
                GameConstants.TYPE_PLAYER  => 1,
                GameConstants.TYPE_SOLDIER => 2,
                GameConstants.TYPE_MYSTIC  => 3,
                GameConstants.TYPE_NATIVE  => Random.Range(4, 15),
                _ => 0
            };
        }

        // ── Per-frame update (called from GameManager.Update) ─────────────────

        public void Tick()
        {
            var gm  = GameManager.Instance;
            int isl = gm.CurrentIsland;

            for (int i = 1; i <= GameConstants.MAX_PLAYERS; i++)
            {
                var c = _slots[i];
                if (c.Island != isl || !c.IsActive) continue;
                UpdateCharacter(c);
            }
        }

        private void UpdateCharacter(CharacterBase c)
        {
            // Gravity
            ApplyGravity(c);

            // Hunger / thirst decay (every ~900 ticks = 30 seconds at 30 FPS)
            if (Time.frameCount % 900 == 0)
            {
                if (c.Hunger > 0)  c.Hunger--;
                if (c.Thirst > 0)  c.Thirst--;
            }

            // Health drain when starving/dehydrated
            if (c.IsHungry || c.IsThirsty)
                if (Time.frameCount % 300 == 0) c.Health--;

            // DT (damage immunity) timer
            if (c.DT > 0) c.DT--;

            // Sync scene object
            if (c.SceneObject != null)
            {
                c.SceneObject.transform.position = c.Pos;
                c.SceneObject.transform.rotation = Quaternion.Euler(0f, c.Angle, 0f);
            }

            // Update scars
            ScarSys?.UpdateScars(c);
        }

        // ── Gravity ───────────────────────────────────────────────────────────

        private void ApplyGravity(CharacterBase c)
        {
            if (c.Pos.y > GameConstants.SEA_LEVEL + 0.5f)
            {
                c.Gravity -= 0.5f * Time.deltaTime * 60f;
                c.Pos.y   += c.Gravity * Time.deltaTime;
                if (c.Pos.y <= GameConstants.SEA_LEVEL)
                {
                    c.Pos.y  = GameConstants.SEA_LEVEL;
                    c.Gravity = 0f;
                }
            }
            else
            {
                c.Gravity = 0f;
            }
        }

        // ── Population count ──────────────────────────────────────────────────

        public int CountPopulation(int island = -1)
        {
            int count = 0;
            for (int i = 1; i <= GameConstants.MAX_PLAYERS; i++)
            {
                var c = _slots[i];
                if (!c.IsActive) continue;
                if (island >= 0 && c.Island != island) continue;
                count++;
            }
            return count;
        }

        // ── Relocation ────────────────────────────────────────────────────────

        public void RelocatePlayer(int slot, int fromIsland, int toIsland)
        {
            var c   = _slots[slot];
            var isl = GameManager.Instance.IslandMgr;
            c.Pos   = isl.RelocateSpawn(fromIsland, toIsland);
            c.Pos.y = 110f;
            c.Island = toIsland;
        }

        public void RelocateEnemies()
        {
            var gm  = GameManager.Instance;
            int isl = gm.CurrentIsland;
            for (int i = 2; i <= GameConstants.MAX_PLAYERS; i++)
            {
                var c = _slots[i];
                if (c.Island == 0) continue;
                if (c.Island == isl) continue;
                // Small redistribution: keep NPCs on their existing islands
                c.Pos = Vector3.zero;   // trigger random-spawn on next BeginIsland
            }
        }

        // ── Limb binding ──────────────────────────────────────────────────────

        private static readonly (int limbIdx, string boneName)[] HumanLimbs =
        {
            (1,"Head"), (2,"Neck"), (3,"Body"), (4,"L_Bicep"), (5,"R_Bicep"),
            (6,"L_Arm"),(7,"R_Arm"),(8,"L_Hand"),(9,"R_Hand"),(10,"Hips"),
            (11,"L_Thigh"),(12,"R_Thigh"),(13,"L_Leg"),(14,"R_Leg"),
            (15,"L_Foot"),(16,"R_Foot")
        };

        private static readonly (int limbIdx, string boneName)[] AnimalLimbsBase =
        {
            (1,"Head"),(2,"Neck"),(3,"Torso"),(4,"L_Bicep"),(5,"R_Bicep"),
            (6,"L_Arm"),(7,"R_Arm"),(10,"Hips"),(11,"L_Thigh"),(12,"R_Thigh"),
            (13,"L_Leg"),(14,"R_Leg"),(15,"L_Foot"),(16,"R_Foot")
        };

        private void BindLimbs(CharacterBase c)
        {
            if (c.SceneObject == null) return;

            var pairs = c.IsAnimal ? AnimalLimbsBase : HumanLimbs;
            foreach (var (idx, name) in pairs)
            {
                var t = c.SceneObject.transform.FindDeep(name);
                c.SetLimb(idx, t);
            }

            // Type-specific extras
            switch (c.CharType)
            {
                case GameConstants.TYPE_LEOPARD:
                case GameConstants.TYPE_WOLF:
                    c.SetLimb(8,  c.SceneObject.transform.FindDeep("L_Paw"));
                    c.SetLimb(9,  c.SceneObject.transform.FindDeep("R_Paw"));
                    c.SetLimb(17, c.SceneObject.transform.FindDeep("Tail01"));
                    c.SetLimb(18, c.SceneObject.transform.FindDeep("Tail02"));
                    c.SetLimb(19, c.SceneObject.transform.FindDeep("Tail03"));
                    break;
                case GameConstants.TYPE_DEER:
                    c.SetLimb(17, c.SceneObject.transform.FindDeep("Tail"));
                    break;
                case GameConstants.TYPE_RABBIT:
                    c.SetLimb(8,  c.SceneObject.transform.FindDeep("L_Paw"));
                    c.SetLimb(9,  c.SceneObject.transform.FindDeep("R_Paw"));
                    c.SetLimb(17, c.SceneObject.transform.FindDeep("Tail"));
                    c.SetLimb(18, c.SceneObject.transform.FindDeep("L_Ear"));
                    c.SetLimb(19, c.SceneObject.transform.FindDeep("R_Ear"));
                    break;
                case GameConstants.TYPE_FISH:
                    c.SetLimb(17, c.SceneObject.transform.FindDeep("Tail"));
                    break;
            }
        }

        // ── Prefab selection ──────────────────────────────────────────────────

        private GameObject PrefabForType(int type) => type switch
        {
            GameConstants.TYPE_PLAYER  => HumanPrefab,
            GameConstants.TYPE_SOLDIER => SoldierPrefab,
            GameConstants.TYPE_MYSTIC  => MysticPrefab,
            GameConstants.TYPE_NATIVE  => NativePrefab,
            GameConstants.TYPE_LEOPARD => LeopardPrefab,
            GameConstants.TYPE_WOLF    => WolfPrefab,
            GameConstants.TYPE_DEER    => DeerPrefab,
            GameConstants.TYPE_RABBIT  => RabbitPrefab,
            GameConstants.TYPE_FISH    => FishPrefab,
            _ => null
        };
    }

    // ── Transform extension ───────────────────────────────────────────────────

    public static class TransformExtensions
    {
        public static Transform FindDeep(this Transform root, string name)
        {
            if (root.name == name) return root;
            foreach (Transform child in root)
            {
                var found = child.FindDeep(name);
                if (found != null) return found;
            }
            return null;
        }
    }
}
