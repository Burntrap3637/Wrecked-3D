using UnityEngine;

namespace Wrecked
{
    /// <summary>
    /// On startup, builds every placeholder model via ProceduralModelFactory and
    /// injects them into the managers that expect inspector-assigned prefabs.
    /// Runs before GameManager.Start (negative execution order) so the prefabs
    /// exist before the first spawn.  Any prefab field already assigned in the
    /// inspector (a real imported mesh) is left untouched.
    /// </summary>
    [DefaultExecutionOrder(-100)]
    public class ModelProvider : MonoBehaviour
    {
        [Tooltip("When true, overwrite even inspector-assigned prefabs with procedural ones.")]
        public bool ForceProcedural = false;

        private Transform _hidden;   // inactive holder so templates never render in-scene

        private void Awake()
        {
            var gm = GameManager.Instance ?? GetComponent<GameManager>();
            if (gm == null) return;

            _hidden = new GameObject("~ProceduralModels").transform;
            _hidden.gameObject.SetActive(false);
            _hidden.SetParent(transform, false);

            PopulateCharacters(gm.CharMgr);
            PopulateItems(gm.ItemMgr);
            PopulateResources(gm.ResMgr);
        }

        // ── Characters ──────────────────────────────────────────────────────────

        private void PopulateCharacters(CharacterManager cm)
        {
            if (cm == null) return;

            cm.HumanPrefab   = Pick(cm.HumanPrefab,   GameConstants.TYPE_PLAYER);
            cm.SoldierPrefab = Pick(cm.SoldierPrefab, GameConstants.TYPE_SOLDIER);
            cm.MysticPrefab  = Pick(cm.MysticPrefab,  GameConstants.TYPE_MYSTIC);
            cm.NativePrefab  = Pick(cm.NativePrefab,  GameConstants.TYPE_NATIVE);
            cm.LeopardPrefab = Pick(cm.LeopardPrefab, GameConstants.TYPE_LEOPARD);
            cm.WolfPrefab    = Pick(cm.WolfPrefab,    GameConstants.TYPE_WOLF);
            cm.DeerPrefab    = Pick(cm.DeerPrefab,    GameConstants.TYPE_DEER);
            cm.RabbitPrefab  = Pick(cm.RabbitPrefab,  GameConstants.TYPE_RABBIT);
            cm.FishPrefab    = Pick(cm.FishPrefab,    GameConstants.TYPE_FISH);
        }

        private GameObject Pick(GameObject existing, int charType)
        {
            if (existing != null && !ForceProcedural) return existing;
            var go = ProceduralModelFactory.BuildCharacter(charType);
            go.transform.SetParent(_hidden, false);
            return go;
        }

        // ── Items ─────────────────────────────────────────────────────────────

        private void PopulateItems(ItemManager im)
        {
            if (im == null) return;

            const int count = GameConstants.ITEM_OAR + 1;   // highest item id + 1
            if (im.ItemPrefabs == null || im.ItemPrefabs.Length < count)
            {
                var grown = new GameObject[count];
                if (im.ItemPrefabs != null)
                    System.Array.Copy(im.ItemPrefabs, grown, im.ItemPrefabs.Length);
                im.ItemPrefabs = grown;
            }

            for (int t = 1; t < count; t++)
            {
                if (im.ItemPrefabs[t] != null && !ForceProcedural) continue;
                var go = ProceduralModelFactory.BuildItem(t);
                go.transform.SetParent(_hidden, false);
                im.ItemPrefabs[t] = go;
            }
        }

        // ── Resources ───────────────────────────────────────────────────────────

        private void PopulateResources(ResourceManager rm)
        {
            if (rm == null) return;

            const int count = GameConstants.RES_ROCK + 1;   // highest res id + 1
            if (rm.ResourcePrefabs == null || rm.ResourcePrefabs.Length < count)
            {
                var grown = new GameObject[count];
                if (rm.ResourcePrefabs != null)
                    System.Array.Copy(rm.ResourcePrefabs, grown, rm.ResourcePrefabs.Length);
                rm.ResourcePrefabs = grown;
            }

            for (int t = 1; t < count; t++)
            {
                if (rm.ResourcePrefabs[t] != null && !ForceProcedural) continue;
                var go = ProceduralModelFactory.BuildResource(t);
                go.transform.SetParent(_hidden, false);
                rm.ResourcePrefabs[t] = go;
            }
        }
    }
}
