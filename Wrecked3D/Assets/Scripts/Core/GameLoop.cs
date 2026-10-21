using UnityEngine;

namespace Wrecked
{
    /// <summary>
    /// Central per-frame driver.  Add this to the GameManager GameObject.
    /// Calls every subsystem in dependency order each Update tick.
    /// </summary>
    public class GameLoop : MonoBehaviour
    {
        private GameManager _gm;

        private void Awake()
        {
            _gm = GameManager.Instance ?? GetComponent<GameManager>();
        }

        private void Update()
        {
            if (_gm == null || _gm.Paused) return;

            // Time & environment first so NPCs see current lighting/day state
            _gm.AtmosMgr?.Tick();
            _gm.WaterCtrl?.Tick();

            // Characters need fire/water state before moving
            _gm.FireSys?.Tick();
            _gm.CharMgr?.Tick();
            _gm.BoatSys?.Tick();
            _gm.SleepSys?.Tick();

            // Items after characters so carry positions update correctly
            _gm.ItemMgr?.Tick();

            // Dialogue proximity check
            _gm.DialogueMgr?.Tick();

            // Mission checks after all entity state is settled
            _gm.MissionMgr?.Tick();

            // Ambient music reacts to time of day
            AudioManager.Instance?.PlayAmbient(_gm.AtmosMgr?.IsNight() ?? false);

            // HUD last — reads the now-settled character state
            var player = _gm.CharMgr?.Get(1);
            _gm.HUDMgr?.Refresh(
                player,
                _gm.GameDay[_gm.SaveSlot],
                _gm.CurrentIsland,
                _gm.SpeedIndex);
        }
    }
}
