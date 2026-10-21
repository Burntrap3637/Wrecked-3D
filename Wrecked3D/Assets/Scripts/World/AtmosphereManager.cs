using UnityEngine;

namespace Wrecked
{
    /// <summary>
    /// Drives the day/night cycle — ambient light, directional light, fog, and
    /// sky colour — all smoothly interpolated at 0.1 units/tick.
    /// Translated directly from ManageAtmosphere() / LoadAtmos() in World.bb.
    /// </summary>
    public class AtmosphereManager : MonoBehaviour
    {
        // ── Scene references ───────────────────────────────────────────────────
        [Header("Scene Refs")]
        public Light   SunLight;
        public Camera  MainCamera;
        public Renderer SkyRenderer;        // mesh child named "Sky"

        // ── Atmosphere options ─────────────────────────────────────────────────
        [Header("Options")]
        public bool FogEnabled  = true;
        public bool RainEnabled = false;

        // ── Public colour state (read by SaveSystem / LoadWorldData) ───────────
        public Color AmbientColor { get; set; } = new Color(170f/255f, 170f/255f, 170f/255f);
        public Color LightColor   { get; set; } = new Color(130f/255f, 130f/255f, 130f/255f);
        public Color FogColor     { get; set; } = new Color(170f/255f, 160f/255f, 160f/255f);
        public Color SkyColor     { get; set; } = new Color(210f/255f, 210f/255f, 210f/255f);

        // Current target colours (updated per time segment)
        private Color _ambTarget, _lightTarget, _fogTarget, _skyTarget;

        // Water/rain scroll
        private float _waterFlow;
        private float _rainFlow;

        private const float CHANGER = 0.1f / 255f;   // 0.1 per tick, normalised

        // ══════════════════════════════════════════════════════════════════════

        public void Initialize()
        {
            UpdateTargets();
            ApplyImmediate();
        }

        public void Tick()
        {
            var gm   = GameManager.Instance;
            int slot = gm.SaveSlot;

            // ── Advance time ──────────────────────────────────────────────────
            gm.GameTime[slot]++;
            int maxTime = (gm.GameMode == GameConstants.MODE_DEATHMATCH) ? 12000 : 18000;
            if (gm.GameTime[slot] > maxTime)
            {
                gm.GameTime[slot] = 0;
                gm.GameDay[slot]++;
            }

            UpdateTargets();
            SmoothStep();
            ApplyColors();
            ScrollWater();
        }

        // ── Target selection (matches original BB time ranges) ─────────────────

        private void UpdateTargets()
        {
            int t = GameManager.Instance.GameTime[GameManager.Instance.SaveSlot];

            if (t >= 0 && t <= 3000)
            {
                _ambTarget   = C(170, 170, 170);
                _lightTarget = C(130, 130, 130);
                _fogTarget   = C(170, 160, 160);
                _skyTarget   = C(210, 210, 210);
            }
            else if (t > 3000 && t <= 6000)
            {
                _ambTarget   = C(200, 200, 200);
                _lightTarget = C(200, 200, 200);
                _fogTarget   = C(200, 200, 240);
                _skyTarget   = C(255, 255, 255);
            }
            else if (t > 6000 && t <= 9000)
            {
                _ambTarget   = C(170, 170, 170);
                _lightTarget = C(120, 120, 120);
                _fogTarget   = C(160, 130, 100);
                _skyTarget   = C(200, 200, 200);
            }
            else if ((t > 9000 && t <= 12000) || (t > 15000 && t <= 18000))
            {
                _ambTarget   = C(130, 130, 130);
                _lightTarget = C( 60,  60,  60);
                _fogTarget   = C(100,  90, 120);
                _skyTarget   = C(155, 155, 155);
            }
            else if (t > 12000 && t <= 15000)
            {
                _ambTarget   = C( 80,  80,  80);
                _lightTarget = C( 40,  40,  40);
                _fogTarget   = C( 40,  40,  70);
                _skyTarget   = C(100, 100, 100);
            }

            // Bridge overrides (dusk→twilight seam)
            if (t >= 5900 && t <= 6600)
            {
                float r = _fogTarget.r; float g = 200f/255f; float b = _fogTarget.b;
                _fogTarget = new Color(r, g, b);
            }
            if (t >= 9000 && t <= 9300)
            {
                _fogTarget = new Color(_fogTarget.r, 130f/255f, 100f/255f);
            }
            if (t > 9300 && t <= 9600)
            {
                _fogTarget = new Color(_fogTarget.r, 100f/255f, 100f/255f);
            }
        }

        // ── Smooth step each channel independently (+=/-= 0.1/255) ────────────

        private void SmoothStep()
        {
            AmbientColor = Step(AmbientColor, _ambTarget);
            LightColor   = Step(LightColor,   _lightTarget);
            FogColor     = Step(FogColor,      _fogTarget);
            SkyColor     = Step(SkyColor,      _skyTarget);
        }

        private static Color Step(Color cur, Color tgt)
        {
            return new Color(
                StepChannel(cur.r, tgt.r),
                StepChannel(cur.g, tgt.g),
                StepChannel(cur.b, tgt.b),
                1f);
        }

        private static float StepChannel(float cur, float tgt)
        {
            if (cur < tgt) return Mathf.Min(cur + CHANGER, tgt);
            if (cur > tgt) return Mathf.Max(cur - CHANGER, tgt);
            return cur;
        }

        // ── Apply to Unity objects ─────────────────────────────────────────────

        private void ApplyColors()
        {
            RenderSettings.ambientLight = AmbientColor;
            if (SunLight) SunLight.color = LightColor;

            if (FogEnabled)
            {
                RenderSettings.fogColor = FogColor;
                RenderSettings.fog      = true;
            }

            if (SkyRenderer)
                SkyRenderer.material.color = SkyColor;
        }

        private void ApplyImmediate()
        {
            // Jump straight to target on first frame
            AmbientColor = _ambTarget;
            LightColor   = _lightTarget;
            FogColor     = _fogTarget;
            SkyColor     = _skyTarget;
            ApplyColors();
        }

        // ── Water / rain scroll ────────────────────────────────────────────────

        private void ScrollWater()
        {
            _waterFlow -= 0.0005f;
            // Individual WaterController drives the actual texture offset

            if (RainEnabled)
                _rainFlow += 0.1f;
        }

        public float WaterFlow => _waterFlow;
        public float RainFlow  => _rainFlow;

        // ── Time-of-day helpers ────────────────────────────────────────────────

        public bool IsNight()
        {
            int t = GameManager.Instance.GameTime[GameManager.Instance.SaveSlot];
            return t >= GameConstants.NIGHT_START && t <= GameConstants.NIGHT_END;
        }

        public bool IsSleepTime()
        {
            int t = GameManager.Instance.GameTime[GameManager.Instance.SaveSlot];
            return t >= GameConstants.SLEEP_TIME_START && t <= GameConstants.SLEEP_TIME_END;
        }

        // ── Helper ────────────────────────────────────────────────────────────
        private static Color C(int r, int g, int b) =>
            new Color(r / 255f, g / 255f, b / 255f);
    }
}
