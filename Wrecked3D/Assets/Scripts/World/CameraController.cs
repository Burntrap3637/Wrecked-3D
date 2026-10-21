using UnityEngine;

namespace Wrecked
{
    /// <summary>
    /// All 15 camera modes from Camera() in World.bb, rebuilt as smooth-tracking
    /// Unity camera logic.  CamType 0-7 and 10-15 match the original enum exactly.
    /// </summary>
    public class CameraController : MonoBehaviour
    {
        // ── Public state (read/written by SaveSystem) ──────────────────────────
        public int     CamType     { get; set; } = 2;
        public Vector3 CamPosition { get; set; }
        public Vector3 CamTarget   { get; set; }

        // ── Scene refs ─────────────────────────────────────────────────────────
        [Header("Scene Refs")]
        public Camera MainCam;
        public Transform Dummy;          // pivot helper (replaces BB's dummy entity)
        public Transform CamPivot;       // look-at target pivot

        // ── Speed / focus ──────────────────────────────────────────────────────
        [Header("Tracking")]
        public int   FocusCharacter = 1;  // camFoc
        public float TrackSpeed     = 15f;
        public float FastTrackSpeed = 60f;

        // ── Fog / clipping ─────────────────────────────────────────────────────
        [Header("Optics")]
        public float FogNear    = 250f;
        public float FogFar     = 1100f;
        public float ClipNear   = 1f;
        public float ClipFar    = 2200f;

        // ── Camera preset keys (mapped in Update) ─────────────────────────────
        private int _keyTimer;
        private Vector3 _camPos;      // working position (smooth-interpolated)
        private Vector3 _targetPos;   // desired end position this frame

        // ══════════════════════════════════════════════════════════════════════

        public void Initialize()
        {
            if (MainCam == null) MainCam = Camera.main;
            MainCam.nearClipPlane = ClipNear;
            MainCam.farClipPlane  = ClipFar;
            _camPos = CamPosition;
        }

        private void Update()
        {
            if (GameManager.Instance.Paused) return;
            HandlePresetKeys();
            ComputeTarget();
            SmoothTrack();
            ApplyCamera();
            CamPosition = _camPos;
        }

        // ── Key presets ────────────────────────────────────────────────────────

        private void HandlePresetKeys()
        {
            if (_keyTimer > 0) { _keyTimer--; return; }
            for (int k = 2; k <= 8; k++)
                if (Input.GetKeyDown((KeyCode)(48 + k))) { CamType = k - 1; _keyTimer = 10; return; }
            if (Input.GetKeyDown(KeyCode.Alpha9))  { CamType = 11; _keyTimer = 10; }
            if (Input.GetKeyDown(KeyCode.Alpha0))  { CamType = 10; _keyTimer = 10; }
            if (Input.GetKeyDown(KeyCode.Minus))   { CamType =  0; _keyTimer = 10; }
        }

        // ── Target computation per camera type ─────────────────────────────────

        private void ComputeTarget()
        {
            var gm  = GameManager.Instance;
            var chr = gm.CharMgr?.Get(FocusCharacter);
            if (chr == null) return;

            Vector3 focus = chr.Pos;
            float   focY  = focus.y;
            float   yaw   = chr.Angle;

            switch (CamType)
            {
                case 0: // manual — WASD
                    ManualControl();
                    return;

                case 1: // first person
                    Vector3 head = chr.HeadPosition();
                    _targetPos   = head + Quaternion.Euler(0, yaw, 0) * new Vector3(0, 3, 3);
                    break;

                case 2: // close follow
                    _targetPos = OffsetBehind(focus, yaw, 35f, focY + 35f);
                    break;

                case 3: // raised follow
                    _targetPos = OffsetBehind(focus, yaw, 70f, focY + 50f);
                    break;

                case 4: // distant follow
                    _targetPos = OffsetBehind(focus, yaw, 140f, focY + 70f);
                    break;

                case 5: // high distant
                    _targetPos = OffsetBehind(focus, yaw, 150f, focY + 150f);
                    break;

                case 6: // bird's eye
                    _targetPos = OffsetBehind(focus, yaw, 1f,   focY + 150f);
                    break;

                case 7: // static overview
                    _targetPos = new Vector3(0f, 200f, 0f);
                    break;

                case 10: // promo headshot
                    _targetPos = OffsetFromYaw(focus, yaw, -10f, 25f, 25f);
                    break;

                case 11: // death observer
                    _targetPos = OffsetFromYaw(focus, yaw, 20f, 20f, -50f);
                    break;

                case 12: // heaven journey (rises forever)
                    _camPos.y  += 0.5f;
                    _targetPos  = _camPos;
                    break;

                case 13: // boat observer
                    _targetPos = new Vector3(-385f, 120f, -630f);
                    break;

                case 14: // ending headshot
                    _targetPos = OffsetFromYaw(focus, yaw, -30f, 20f, 40f);
                    break;

                case 15: // intro camera
                    _targetPos = OffsetFromYaw(focus, yaw, 20f, 20f, -120f);
                    break;
            }

            // East island pier ceiling clamp
            if (gm.IslandMgr != null && gm.IslandMgr.CurrentIsland == GameConstants.ISLAND_EAST)
            {
                if (focus.x > -300 && focus.x < 180 && focus.z > 60 && focus.z < 150 && focus.y < 20)
                    _targetPos.y = Mathf.Min(_targetPos.y, 20f);
            }

            // Look-at pivot
            if (CamPivot != null)
                CamPivot.position = chr.CharType == GameConstants.TYPE_MYSTIC
                    ? focus + Vector3.up * 15f
                    : focus + Vector3.up * 25f;
        }

        // ── Smooth tracking ────────────────────────────────────────────────────

        private void SmoothTrack()
        {
            if (CamType == 1) { _camPos = _targetPos; return; }

            var gm    = GameManager.Instance;
            float spd = (gm.GameMode == GameConstants.MODE_STORY) ? TrackSpeed : FastTrackSpeed;
            Vector3 diff = _targetPos - _camPos;
            if (diff.magnitude < 0.01f) { _camPos = _targetPos; return; }

            float step = diff.magnitude / spd;
            _camPos += diff.normalized * step;
        }

        // ── Apply to Unity camera ──────────────────────────────────────────────

        private void ApplyCamera()
        {
            if (MainCam == null) return;
            MainCam.transform.position = _camPos;
            if (CamPivot != null && CamType != 12)
                MainCam.transform.LookAt(CamPivot);

            // World-boundary clamp
            var isl = GameManager.Instance.IslandMgr;
            if (isl != null && CamType != 1 && CamType != 12)
            {
                Vector3 p = MainCam.transform.position;
                p.x = Mathf.Clamp(p.x, isl.WWest,  isl.WEast);
                p.z = Mathf.Clamp(p.z, isl.WSouth, isl.WNorth);
                MainCam.transform.position = p;
                _camPos = p;
            }
        }

        // ── Manual control ─────────────────────────────────────────────────────

        private void ManualControl()
        {
            float spd = 5f;
            if (Input.GetKey(KeyCode.W)) _camPos += MainCam.transform.forward * spd;
            if (Input.GetKey(KeyCode.S)) _camPos -= MainCam.transform.forward * spd;
            if (Input.GetKey(KeyCode.A)) _camPos -= MainCam.transform.right   * spd;
            if (Input.GetKey(KeyCode.D)) _camPos += MainCam.transform.right   * spd;
            if (Input.GetKey(KeyCode.Q)) _camPos += Vector3.up   * spd;
            if (Input.GetKey(KeyCode.E)) _camPos -= Vector3.up   * spd;
            _targetPos = _camPos;
        }

        // ── Helpers ────────────────────────────────────────────────────────────

        private static Vector3 OffsetBehind(Vector3 focus, float yaw, float back, float height)
        {
            Vector3 dir = Quaternion.Euler(0, yaw, 0) * Vector3.back;
            return new Vector3(focus.x + dir.x * back, height, focus.z + dir.z * back);
        }

        private static Vector3 OffsetFromYaw(Vector3 focus, float yaw, float side, float up, float forward)
        {
            Quaternion rot = Quaternion.Euler(0, yaw, 0);
            Vector3 offset = rot * new Vector3(side, 0, forward);
            return new Vector3(focus.x + offset.x, focus.y + up, focus.z + offset.z);
        }
    }
}
