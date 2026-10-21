using UnityEngine;

namespace Wrecked
{
    /// <summary>
    /// Player rest mechanic.  Holding the Sleep key at a safe location
    /// advances time to dawn and partially restores hunger/thirst.
    /// </summary>
    public class SleepSystem : MonoBehaviour
    {
        public KeyCode SleepKey         = KeyCode.Return;
        public int     TicksUntilDawn   = GameConstants.TIME_DAY;   // 3000
        public int     SleepHungerCost  = 100;
        public int     SleepThirstCost  = 80;

        private float  _holdTime;
        private const float HOLD_REQUIRED = 1.5f;   // hold 1.5s to sleep

        public void Tick()
        {
            var gm = GameManager.Instance;
            if (gm.GameMode != GameConstants.MODE_STORY) return;

            var player = gm.CharMgr?.Get(1);
            if (player == null || player.IsDead) return;
            if (!IsNightTime(gm)) return;
            if (!PlayerIsStill(player)) return;

            if (Input.GetKey(SleepKey))
            {
                _holdTime += Time.deltaTime;
                if (_holdTime >= HOLD_REQUIRED)
                    Sleep(player, gm);
            }
            else
            {
                _holdTime = 0f;
            }
        }

        private void Sleep(CharacterBase player, GameManager gm)
        {
            _holdTime = 0f;

            int slot = gm.SaveSlot;

            // Advance time to next dawn
            gm.GameTime[slot] = TicksUntilDawn;

            // Hunger and thirst worsen slightly overnight
            player.Hunger = Mathf.Min(player.Hunger + SleepHungerCost, 1000);
            player.Thirst = Mathf.Min(player.Thirst + SleepThirstCost, 1000);

            // Small health recovery if not starving
            if (player.Hunger < 800 && player.Thirst < 800)
                player.Health = Mathf.Min(player.Health + 10, player.MaxHP);

            // Advance day count
            gm.GameDay[slot]++;

            gm.PromoMgr?.Show($"Day {gm.GameDay[slot]}");
        }

        private bool IsNightTime(GameManager gm)
        {
            int t = gm.GameTime[gm.SaveSlot];
            return t >= GameConstants.TIME_DUSK;   // 9000
        }

        private bool PlayerIsStill(CharacterBase p)
        {
            // Sitting or idle anim
            return p.Anim is GameConstants.ANIM_IDLE or GameConstants.ANIM_SIT;
        }
    }
}
