using UnityEngine;

namespace Wrecked
{
    public class WaterController : MonoBehaviour
    {
        [Header("Water Plane")]
        public Renderer WaterRenderer;
        public float ScrollSpeed = 0.005f;

        [Header("Wave Settings")]
        public float WaveHeight   = 0.3f;
        public float WaveFreq     = 0.8f;
        public float WaveSpeed    = 1.2f;

        private Material _mat;
        private float    _offsetX;
        private float    _offsetZ;
        private float    _baseY;

        private void Awake()
        {
            if (WaterRenderer != null)
            {
                _mat  = WaterRenderer.material;
                _baseY = WaterRenderer.transform.position.y;
            }
        }

        public void Tick()
        {
            ScrollTexture();
            AnimateWaves();
        }

        private void ScrollTexture()
        {
            if (_mat == null) return;

            _offsetX += ScrollSpeed;
            _offsetZ += ScrollSpeed * 0.6f;

            _offsetX %= 1f;
            _offsetZ %= 1f;

            _mat.SetTextureOffset("_MainTex", new Vector2(_offsetX, _offsetZ));
        }

        private void AnimateWaves()
        {
            if (WaterRenderer == null) return;

            float wave = Mathf.Sin(Time.time * WaveSpeed * WaveFreq) * WaveHeight;
            var pos    = WaterRenderer.transform.position;
            pos.y      = _baseY + wave;
            WaterRenderer.transform.position = pos;
        }

        public void SetWaterLevel(float y)
        {
            _baseY = y;
        }
    }
}
