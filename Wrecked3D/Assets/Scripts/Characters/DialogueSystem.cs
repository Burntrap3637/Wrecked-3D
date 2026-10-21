using UnityEngine;
using System.Collections.Generic;

namespace Wrecked
{
    /// <summary>
    /// NPC conversation lines keyed to character type, soul level, and current mission.
    /// Proximity trigger shows a subtitle via PromoManager.
    /// </summary>
    public class DialogueSystem : MonoBehaviour
    {
        public float TalkRadius   = 60f;
        public float CooldownSecs = 12f;

        private float _cooldown;

        // ── Line tables ───────────────────────────────────────────────────────

        private static readonly Dictionary<int, string[]> GreetsByType = new()
        {
            [GameConstants.CHAR_SOLDIER] = new[]
            {
                "Move along.",
                "This border is closed.",
                "I'm watching you.",
                "Don't test me.",
            },
            [GameConstants.CHAR_MYSTIC] = new[]
            {
                "The island breathes.",
                "Your soul speaks to me.",
                "What do you seek?",
                "The centre calls to all who are pure.",
            },
            [GameConstants.CHAR_NATIVE] = new[]
            {
                "Leave us be.",
                "You're not from here.",
                "The hunt was good today.",
                "Stay away from our fires.",
            },
        };

        private static readonly Dictionary<int, string[]> MysticMissionLines = new()
        {
            [MissionSystem.MISS_MYSTIC_VISIT] = new[]
            {
                "You found me at last. The artefact waits at the centre island.",
                "Cross the sea. Carry nothing but your courage.",
            },
            [MissionSystem.MISS_MYSTIC_GIFT] = new[]
            {
                "You have proven yourself. Take this gift.",
                "The path home is open now.",
            },
            [MissionSystem.MISS_REDEMPTION] = new[]
            {
                "Your sins weigh on you. Can you put them right?",
            },
        };

        // ── Per-frame check ───────────────────────────────────────────────────

        public void Tick()
        {
            _cooldown -= Time.deltaTime;
            if (_cooldown > 0) return;

            var gm     = GameManager.Instance;
            var player = gm.CharMgr?.Get(1);
            if (player == null) return;

            for (int c = 2; c <= GameConstants.MAX_PLAYERS; c++)
            {
                var npc = gm.CharMgr?.Get(c);
                if (npc == null || !npc.IsActive || npc.IsDead) continue;
                if (npc.Island != gm.CurrentIsland) continue;

                float dist = Vector3.Distance(player.Pos, npc.Pos);
                if (dist > TalkRadius) continue;

                string line = PickLine(npc, gm);
                if (!string.IsNullOrEmpty(line))
                {
                    gm.PromoMgr?.Show(line);
                    _cooldown = CooldownSecs;
                    return;
                }
            }
        }

        private string PickLine(CharacterBase npc, GameManager gm)
        {
            int mission = gm.GameMission[gm.SaveSlot];

            // Mystic gets mission-specific lines first
            if (npc.CharType == GameConstants.CHAR_MYSTIC &&
                MysticMissionLines.TryGetValue(mission, out var mlines))
            {
                return mlines[Random.Range(0, mlines.Length)];
            }

            // Generic type greet
            if (GreetsByType.TryGetValue(npc.CharType, out var glines))
                return glines[Random.Range(0, glines.Length)];

            return null;
        }
    }
}
