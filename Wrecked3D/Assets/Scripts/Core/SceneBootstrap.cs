using UnityEngine;
using UnityEngine.UI;
using UnityEngine.EventSystems;

namespace Wrecked
{
    /// <summary>
    /// Zero-setup entry point.  Put this one component on an empty GameObject in
    /// an otherwise empty scene and press Play — it constructs the camera, light,
    /// terrain, water, UI canvas, and the full manager graph, wires every
    /// cross-reference, then activates everything so the game boots.
    ///
    /// Everything is built inactive and wired first, then switched on in one go,
    /// so no Awake fires against a half-wired graph.
    /// </summary>
    [DefaultExecutionOrder(-500)]
    public class SceneBootstrap : MonoBehaviour
    {
        [Header("Startup")]
        public int   StartIsland = GameConstants.ISLAND_NORTH;
        public int   GameMode    = GameConstants.MODE_DEATHMATCH;
        public int   SaveSlot    = 1;
        public bool  ProceduralModels = true;

        private void Awake()
        {
            BuildScene();
        }

        private void BuildScene()
        {
            // ── Lighting ────────────────────────────────────────────────────────
            var sun = new GameObject("Sun").AddComponent<Light>();
            sun.type = LightType.Directional;
            sun.transform.rotation = Quaternion.Euler(50f, -30f, 0f);
            sun.shadows = LightShadows.Soft;
            sun.intensity = 1.1f;

            RenderSettings.fog = true;
            RenderSettings.fogMode = FogMode.Linear;
            RenderSettings.fogStartDistance = 200f;
            RenderSettings.fogEndDistance   = 1600f;

            // ── Camera ──────────────────────────────────────────────────────────
            var camGo = new GameObject("Main Camera");
            var cam   = camGo.AddComponent<Camera>();
            camGo.tag = "MainCamera";
            cam.farClipPlane = 3000f;
            camGo.AddComponent<AudioListener>();

            var camPivot = new GameObject("CamPivot").transform;
            var camDummy = new GameObject("CamDummy").transform;

            // ── Terrain + water ───────────────────────────────────────────────────
            var terrainGo = new GameObject("Terrain");
            terrainGo.AddComponent<Terrain>();
            terrainGo.AddComponent<TerrainCollider>();
            var islandTerrain = terrainGo.AddComponent<IslandTerrain>();

            var waterGo = new GameObject("Water");
            waterGo.transform.localScale = new Vector3(300f, 1f, 300f);
            waterGo.transform.position   = new Vector3(0f, GameConstants.SEA_LEVEL, 0f);
            waterGo.AddComponent<MeshFilter>().sharedMesh = MeshBuilder.Get(MeshBuilder.Shape.Quad);
            var waterRenderer = waterGo.AddComponent<MeshRenderer>();
            waterRenderer.material = new Material(ShaderUtil.Lit);
            waterRenderer.material.color = new Color(0.15f, 0.35f, 0.55f, 1f);

            // ── UI canvas ─────────────────────────────────────────────────────────
            var canvasGo = new GameObject("Canvas");
            var canvas   = canvasGo.AddComponent<Canvas>();
            canvas.renderMode = RenderMode.ScreenSpaceOverlay;
            canvasGo.AddComponent<CanvasScaler>();
            canvasGo.AddComponent<GraphicRaycaster>();

            if (FindObjectOfType<EventSystem>() == null)
            {
                var es = new GameObject("EventSystem");
                es.AddComponent<EventSystem>();
                es.AddComponent<StandaloneInputModule>();
            }

            var fadePanel = NewUIImage("FadePanel", canvasGo.transform, Color.clear, stretch:true);

            // ── Manager graph (built inactive) ────────────────────────────────────
            var game = new GameObject("Game");
            game.SetActive(false);

            var gm       = game.AddComponent<GameManager>();
            var island   = game.AddComponent<IslandManager>();
            var atmos    = game.AddComponent<AtmosphereManager>();
            var water    = game.AddComponent<WaterController>();
            var chars    = game.AddComponent<CharacterManager>();
            var costume  = game.AddComponent<CostumeSystem>();
            var scars    = game.AddComponent<ScarSystem>();
            var items    = game.AddComponent<ItemManager>();
            var res      = game.AddComponent<ResourceManager>();
            var fire     = game.AddComponent<FireSystem>();
            var boats    = game.AddComponent<BoatSystem>();
            var particles= game.AddComponent<ParticleManager>();
            var missions = game.AddComponent<MissionSystem>();
            var dialogue = game.AddComponent<DialogueSystem>();
            var sleep    = game.AddComponent<SleepSystem>();
            var hud      = game.AddComponent<HUDManager>();
            var menu     = game.AddComponent<MenuManager>();
            var promo    = game.AddComponent<PromoManager>();
            var inv      = game.AddComponent<InventoryUI>();
            var fade     = game.AddComponent<FadeController>();
            var camCtrl  = game.AddComponent<CameraController>();
            var audio    = game.AddComponent<AudioManager>();
            var save     = game.AddComponent<SaveSystem>();
            var loop     = game.AddComponent<GameLoop>();
            var provider = game.AddComponent<ModelProvider>();

            // ── Wire GameManager ──────────────────────────────────────────────────
            gm.GameMode      = GameMode;
            gm.SaveSlot      = SaveSlot;
            gm.CurrentIsland = StartIsland;

            gm.IslandMgr   = island;
            gm.AtmosMgr    = atmos;
            gm.WaterCtrl   = water;
            gm.CharMgr     = chars;
            gm.ItemMgr     = items;
            gm.ResMgr      = res;
            gm.FireSys     = fire;
            gm.BoatSys     = boats;
            gm.PartMgr     = particles;
            gm.MissionMgr  = missions;
            gm.DialogueMgr = dialogue;
            gm.SleepSys    = sleep;
            gm.HUDMgr      = hud;
            gm.MenuMgr     = menu;
            gm.PromoMgr    = promo;
            gm.InvUI       = inv;
            gm.FadeCtrl    = fade;
            gm.CamCtrl     = camCtrl;
            gm.AudioMgr    = audio;
            gm.SaveSys     = save;

            // ── Wire sub-references ───────────────────────────────────────────────
            island.ProcTerrain = islandTerrain;
            islandTerrain.Water = water;

            water.WaterRenderer = waterRenderer;

            chars.CostumeSys = costume;
            chars.ScarSys    = scars;

            atmos.SunLight   = sun;
            atmos.MainCamera = cam;

            camCtrl.MainCam  = cam;
            camCtrl.CamPivot = camPivot;
            camCtrl.Dummy    = camDummy;

            fade.Panel = fadePanel;

            provider.ForceProcedural = ProceduralModels;

            // ── Boot ────────────────────────────────────────────────────────────
            game.SetActive(true);
        }

        // ── Helpers ───────────────────────────────────────────────────────────

        private static Image NewUIImage(string name, Transform parent, Color color, bool stretch)
        {
            var go  = new GameObject(name);
            go.transform.SetParent(parent, false);
            var img = go.AddComponent<Image>();
            img.color = color;
            img.raycastTarget = false;

            var rt = img.rectTransform;
            if (stretch)
            {
                rt.anchorMin = Vector2.zero;
                rt.anchorMax = Vector2.one;
                rt.offsetMin = Vector2.zero;
                rt.offsetMax = Vector2.zero;
            }
            return img;
        }
    }
}
