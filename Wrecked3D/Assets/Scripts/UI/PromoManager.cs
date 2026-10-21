using System.Collections;
using UnityEngine;
using UnityEngine.UI;
using TMPro;

namespace Wrecked
{
    /// <summary>
    /// Handles story subtitle lines and cutscene captions.
    /// Queue entries with Show(), they fade in and auto-expire.
    /// </summary>
    public class PromoManager : MonoBehaviour
    {
        [Header("UI")]
        public CanvasGroup    SubtitleGroup;
        public TextMeshProUGUI SubtitleText;
        public Image          SubtitleBackground;

        [Header("Timing")]
        public float FadeTime    = 0.4f;
        public float DisplayTime = 4f;    // seconds a line stays visible

        private Coroutine _current;

        // ── Public API ────────────────────────────────────────────────────────

        public void Show(string line)
        {
            if (_current != null) StopCoroutine(_current);
            _current = StartCoroutine(ShowRoutine(line));
        }

        public void ShowImmediate(string line)
        {
            StopAllCoroutines();
            if (SubtitleText  != null) SubtitleText.text = line;
            if (SubtitleGroup != null) SubtitleGroup.alpha = 1f;
            _current = StartCoroutine(HoldThenFade());
        }

        public void Clear()
        {
            if (_current != null) StopCoroutine(_current);
            StartCoroutine(FadeOut());
        }

        // ── Story line presets ────────────────────────────────────────────────

        public void ShowMissionCaption(int missionId)
        {
            string caption = missionId switch
            {
                MissionSystem.MISS_ARRIVAL          => "You wash ashore, alone.",
                MissionSystem.MISS_SOLDIER_CONTACT  => "Soldiers patrol the borders.",
                MissionSystem.MISS_THEFT            => "Someone has taken what isn't theirs.",
                MissionSystem.MISS_MYSTIC_VISIT     => "The mystic calls to you.",
                MissionSystem.MISS_HUNTING          => "The island must be fed.",
                MissionSystem.MISS_CRAFTING         => "Shape the world with your hands.",
                MissionSystem.MISS_FIRE             => "Fire spreads where you walk.",
                MissionSystem.MISS_SURVIVAL         => "Survive. Whatever it takes.",
                MissionSystem.MISS_BOAT_TRAVEL      => "The sea waits.",
                MissionSystem.MISS_CENTRE_ISLAND    => "Something powerful lies at the centre.",
                MissionSystem.MISS_ARTEFACT         => "The artefact hums in your hands.",
                MissionSystem.MISS_MYSTIC_GIFT      => "The mystic gifts you passage.",
                MissionSystem.MISS_REDEMPTION       => "Can you still be saved?",
                MissionSystem.MISS_FINAL_BATTLE     => "This ends now.",
                MissionSystem.MISS_ESCAPE           => "Run.",
                MissionSystem.MISS_ENDING           => "You are free.",
                _                                   => ""
            };

            if (!string.IsNullOrEmpty(caption))
                Show(caption);
        }

        // ── Coroutines ────────────────────────────────────────────────────────

        private IEnumerator ShowRoutine(string line)
        {
            if (SubtitleText  != null) SubtitleText.text = line;

            yield return FadeRoutine(0f, 1f);
            yield return new WaitForSeconds(DisplayTime);
            yield return FadeRoutine(1f, 0f);
        }

        private IEnumerator HoldThenFade()
        {
            yield return new WaitForSeconds(DisplayTime);
            yield return FadeRoutine(1f, 0f);
        }

        private IEnumerator FadeOut()
        {
            if (SubtitleGroup != null)
                yield return FadeRoutine(SubtitleGroup.alpha, 0f);
        }

        private IEnumerator FadeRoutine(float from, float to)
        {
            if (SubtitleGroup == null) yield break;

            float t = 0f;
            while (t < FadeTime)
            {
                t += Time.deltaTime;
                SubtitleGroup.alpha = Mathf.Lerp(from, to, t / FadeTime);
                yield return null;
            }
            SubtitleGroup.alpha = to;
        }
    }
}
