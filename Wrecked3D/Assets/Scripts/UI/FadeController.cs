using System.Collections;
using UnityEngine;
using UnityEngine.UI;

namespace Wrecked
{
    /// <summary>
    /// Full-screen fade panel used for scene transitions, game over, and cutscenes.
    /// </summary>
    public class FadeController : MonoBehaviour
    {
        public static FadeController Instance { get; private set; }

        public Image Panel;
        public float DefaultDuration = 1f;

        private Coroutine _fading;

        private void Awake()
        {
            Instance = this;
            if (Panel != null)
                Panel.color = Color.clear;
        }

        public void FadeIn(float duration = -1f, System.Action onComplete = null)
        {
            StartFade(Color.clear, Color.black, duration < 0 ? DefaultDuration : duration, onComplete);
        }

        public void FadeOut(float duration = -1f, System.Action onComplete = null)
        {
            StartFade(Color.black, Color.clear, duration < 0 ? DefaultDuration : duration, onComplete);
        }

        public void Flash(Color color, float duration = 0.2f)
        {
            if (_fading != null) StopCoroutine(_fading);
            _fading = StartCoroutine(FlashRoutine(color, duration));
        }

        private void StartFade(Color from, Color to, float duration, System.Action onComplete)
        {
            if (_fading != null) StopCoroutine(_fading);
            _fading = StartCoroutine(FadeRoutine(from, to, duration, onComplete));
        }

        private IEnumerator FadeRoutine(Color from, Color to, float duration, System.Action onComplete)
        {
            if (Panel == null) yield break;

            Panel.gameObject.SetActive(true);
            float t = 0f;
            while (t < duration)
            {
                t += Time.unscaledDeltaTime;
                Panel.color = Color.Lerp(from, to, t / duration);
                yield return null;
            }
            Panel.color = to;

            if (to.a <= 0f) Panel.gameObject.SetActive(false);
            onComplete?.Invoke();
        }

        private IEnumerator FlashRoutine(Color color, float duration)
        {
            if (Panel == null) yield break;

            Panel.gameObject.SetActive(true);
            Panel.color = color;
            yield return new WaitForSecondsRealtime(duration * 0.5f);
            yield return FadeRoutine(color, Color.clear, duration * 0.5f, null);
        }
    }
}
