using UnityEngine;

namespace Wrecked
{
    /// <summary>
    /// Music tracks and SFX pool.  Attach to GameManager GameObject.
    /// </summary>
    public class AudioManager : MonoBehaviour
    {
        public static AudioManager Instance { get; private set; }

        [Header("Music")]
        public AudioClip[] DayTracks;
        public AudioClip[] NightTracks;
        public AudioClip   MenuMusic;
        [Range(0f, 1f)] public float MusicVolume = 0.6f;

        [Header("SFX")]
        public AudioClip SfxHit;
        public AudioClip SfxPickup;
        public AudioClip SfxDrop;
        public AudioClip SfxCraft;
        public AudioClip SfxSplash;
        public AudioClip SfxFire;
        public AudioClip SfxDeath;
        [Range(0f, 1f)] public float SfxVolume = 1f;

        private AudioSource _musicSource;
        private AudioSource _sfxSource;
        private bool        _wasNight;

        private void Awake()
        {
            if (Instance != null && Instance != this) { Destroy(gameObject); return; }
            Instance = this;

            _musicSource = gameObject.AddComponent<AudioSource>();
            _musicSource.loop   = true;
            _musicSource.volume = MusicVolume;

            _sfxSource = gameObject.AddComponent<AudioSource>();
            _sfxSource.volume = SfxVolume;
        }

        // ── Music ─────────────────────────────────────────────────────────────

        public void PlayMenu()           => Play(MenuMusic);
        public void PlayAmbient(bool isNight)
        {
            if (isNight == _wasNight && _musicSource.isPlaying) return;
            _wasNight = isNight;

            var tracks = isNight ? NightTracks : DayTracks;
            if (tracks == null || tracks.Length == 0) return;

            Play(tracks[Random.Range(0, tracks.Length)]);
        }

        private void Play(AudioClip clip)
        {
            if (clip == null || _musicSource.clip == clip) return;
            _musicSource.clip = clip;
            _musicSource.Play();
        }

        public void StopMusic() => _musicSource.Stop();

        // ── SFX ───────────────────────────────────────────────────────────────

        public void Hit()     => PlaySfx(SfxHit);
        public void Pickup()  => PlaySfx(SfxPickup);
        public void Drop()    => PlaySfx(SfxDrop);
        public void Craft()   => PlaySfx(SfxCraft);
        public void Splash()  => PlaySfx(SfxSplash);
        public void FireCrackle() => PlaySfx(SfxFire);
        public void Death()   => PlaySfx(SfxDeath);

        public void PlaySfx(AudioClip clip)
        {
            if (clip == null) return;
            _sfxSource.PlayOneShot(clip, SfxVolume);
        }

        // ── Volume control ────────────────────────────────────────────────────

        public void SetMusicVolume(float v)
        {
            MusicVolume = v;
            _musicSource.volume = v;
        }

        public void SetSfxVolume(float v)
        {
            SfxVolume = v;
            _sfxSource.volume = v;
        }
    }
}
