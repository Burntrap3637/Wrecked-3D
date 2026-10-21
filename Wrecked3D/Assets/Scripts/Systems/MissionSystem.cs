using UnityEngine;

namespace Wrecked
{
    /// <summary>
    /// Tracks the 16-mission story campaign.
    /// Missions gate NPC behaviour, dialogue triggers, and game endings.
    /// Translated from gamMission / gamCheckpoint usage across Gameplay.bb / AI.bb.
    /// </summary>
    public class MissionSystem : MonoBehaviour
    {
        public const int MISSION_COUNT = 16;

        // ── Mission ID constants ───────────────────────────────────────────────
        public const int MISS_ARRIVAL         = 1;
        public const int MISS_SOLDIER_CONTACT = 2;
        public const int MISS_THEFT           = 3;
        public const int MISS_MYSTIC_VISIT    = 4;
        public const int MISS_HUNTING         = 5;
        public const int MISS_CRAFTING        = 6;
        public const int MISS_FIRE            = 7;
        public const int MISS_SURVIVAL        = 8;
        public const int MISS_BOAT_TRAVEL     = 9;
        public const int MISS_CENTRE_ISLAND   = 10;
        public const int MISS_ARTEFACT        = 11;
        public const int MISS_MYSTIC_GIFT     = 12;
        public const int MISS_REDEMPTION      = 13;
        public const int MISS_FINAL_BATTLE    = 14;
        public const int MISS_ESCAPE          = 15;
        public const int MISS_ENDING          = 16;

        // ── Public API ────────────────────────────────────────────────────────

        public int CurrentMission
        {
            get => GameManager.Instance.GameMission[GameManager.Instance.SaveSlot];
            set => GameManager.Instance.GameMission[GameManager.Instance.SaveSlot] = value;
        }

        public int MissionDay
        {
            get => GameManager.Instance.MissionDay[GameManager.Instance.SaveSlot];
            set => GameManager.Instance.MissionDay[GameManager.Instance.SaveSlot] = value;
        }

        public bool GetResult(int mission)
        {
            var gm = GameManager.Instance;
            return gm.GameResult[gm.SaveSlot, mission] > 0;
        }

        public void SetResult(int mission, int val = 1)
        {
            var gm = GameManager.Instance;
            gm.GameResult[gm.SaveSlot, mission] = val;
        }

        // ── Checkpoint helpers ────────────────────────────────────────────────
        // CP meanings (from Values.bb dim gamCheckpoint(3,10) comment):
        //   1=soldier intro shown, 2=border arrival, 3=theft occurred,
        //   4=intro cutscene finished, 5=game-over triggered

        public int  GetCheckpoint(int cp)        => GameManager.Instance.GetCheckpoint(GameManager.Instance.SaveSlot, cp);
        public void SetCheckpoint(int cp, int v) => GameManager.Instance.GameCheckpoint(GameManager.Instance.SaveSlot, cp, v);

        // ── Per-frame check ────────────────────────────────────────────────────

        public void Tick()
        {
            if (GameManager.Instance.GameMode != GameConstants.MODE_STORY) return;

            // Advance to next mission if current completed
            CheckMissionProgress();
        }

        private void CheckMissionProgress()
        {
            int cur = CurrentMission;
            if (cur >= MISSION_COUNT) return;

            // Missions auto-advance if their result flag is set
            if (GetResult(cur + 1)) CurrentMission++;
        }

        // ── Advance helper ─────────────────────────────────────────────────────

        public void CompleteMission(int missionId)
        {
            SetResult(missionId);
            if (CurrentMission < missionId) CurrentMission = missionId;

            // Trigger ending when final mission done
            if (missionId >= MISS_ENDING)
                GameManager.Instance.TriggerGameOver(true);
        }
    }
}
