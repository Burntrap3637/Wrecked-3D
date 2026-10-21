using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

namespace Wrecked
{
    /// <summary>
    /// Central game controller — owns global state, drives the main loop, and
    /// coordinates all subsystems.  Equivalent to the Main() function and the
    /// global variables in Gameplay.bb / Values.bb.
    /// </summary>
    public class GameManager : MonoBehaviour
    {
        public static GameManager Instance { get; private set; }

        // ── Game mode ──────────────────────────────────────────────────────────
        [Header("Game Mode")]
        public int GameMode   = GameConstants.MODE_STORY;   // 0=deathmatch, 1=story, 2=ending
        public int SaveSlot   = 1;                          // 1-3
        public int CurrentIsland = GameConstants.ISLAND_NORTH;

        // ── Progress ───────────────────────────────────────────────────────────
        [HideInInspector] public int[]  GameTime      = new int[4];   // [slot]
        [HideInInspector] public int[]  GameDay       = new int[4];
        [HideInInspector] public int[]  GameSoul      = new int[4];   // 1=evil,2=flawed,3=divine
        [HideInInspector] public int[,] GameSins      = new int[4, 21];
        [HideInInspector] public int[]  GameCrime     = new int[4];
        [HideInInspector] public int[]  GameMission   = new int[4];
        [HideInInspector] public int[]  MissionDay    = new int[4];
        [HideInInspector] public int[,] GameResult    = new int[4, 21];
        [HideInInspector] public int[,] Checkpoint    = new int[4, 11];

        // ── Deathmatch options ─────────────────────────────────────────────────
        [Header("Deathmatch Options")]
        public int OptIsland  = 0;   // 0=random
        public int OptPlays   = 8;
        public int OptCast    = 0;   // 0=mixed, 1=humans, 2=animals
        public bool OptMulti  = false;

        // ── Travel ─────────────────────────────────────────────────────────────
        [HideInInspector] public int GameDestination;
        [HideInInspector] public int GameJourney;

        // ── Subsystem references ────────────────────────────────────────────────
        [Header("Subsystems")]
        public IslandManager     IslandMgr;
        public AtmosphereManager AtmosMgr;
        public CharacterManager  CharMgr;
        public ItemManager       ItemMgr;
        public ResourceManager   ResMgr;
        public FireSystem        FireSys;
        public BoatSystem        BoatSys;
        public ParticleManager   PartMgr;
        public MissionSystem     MissionSys;
        public HUDManager        HUDMgr;
        public CameraController  CamCtrl;
        public SaveSystem        SaveSys;

        // ── Pause / speed ──────────────────────────────────────────────────────
        [HideInInspector] public bool   Paused       = false;
        [HideInInspector] public float  GameSpeed    = 1f;   // F1-F4 speed multiplier
        [HideInInspector] public int    Survivor;            // deathmatch winner

        // ── Internal ───────────────────────────────────────────────────────────
        private bool _gameOver = false;
        private float _fadeAlpha;
        private float _fadeTarget;

        // ══════════════════════════════════════════════════════════════════════
        void Awake()
        {
            if (Instance != null && Instance != this) { Destroy(gameObject); return; }
            Instance = this;
            DontDestroyOnLoad(gameObject);
            ResetAllProgress();
        }

        void Start()
        {
            if (SaveSys == null) SaveSys = GetComponent<SaveSystem>();
            SaveSys.LoadProgressData();
            BeginIsland();
        }

        // ══════════════════════════════════════════════════════════════════════
        //  Public helpers
        // ══════════════════════════════════════════════════════════════════════

        public void BeginIsland()
        {
            _gameOver = false;
            GameDestination = 0;
            Checkpoint[SaveSlot, 2] = 0;
            Checkpoint[SaveSlot, 5] = 0;
            IslandMgr.LoadIsland(CurrentIsland);
            AtmosMgr.Initialize();
            CharMgr.SpawnAll();
            ResMgr.SpawnAll();
            ItemMgr.SpawnAll();
            BoatSys.Initialize();
            FireSys.Initialize();
            PartMgr.Initialize();
            HUDMgr.Initialize();
            CamCtrl.Initialize();
        }

        public void EndIsland()
        {
            SaveSys.SaveAll();
            if (GameDestination > 0)
            {
                CharMgr.RelocatePlayer(1, CurrentIsland, GameDestination);
                CharMgr.RelocateEnemies();
                CurrentIsland = GameDestination;
                SceneManager.LoadScene(SceneManager.GetActiveScene().name);
            }
            else
            {
                SceneManager.LoadScene("MainMenu");
            }
        }

        public void PlayerDied() => TriggerGameOver(false);
        public void DeathMatchOver(int winner) { Survivor = winner; TriggerGameOver(true); }

        public void TriggerGameOver(bool completed)
        {
            if (_gameOver) return;
            _gameOver = true;
            Checkpoint[SaveSlot, 5] = 1;
            if (completed)
                _fadeTarget = 1f;
            StartCoroutine(GameOverSequence(completed));
        }

        private IEnumerator GameOverSequence(bool completed)
        {
            yield return new WaitForSeconds(completed ? 5f : 3f);
            EndIsland();
        }

        // ── Progress management ────────────────────────────────────────────────

        public void ResetAllProgress()
        {
            for (int slot = 0; slot <= 3; slot++)
                ResetProgressData(slot);
        }

        public void ResetProgressData(int slot)
        {
            GameTime[slot]   = 3000;
            GameDay[slot]    = 1;
            GameSoul[slot]   = GameConstants.SOUL_DIVINE;
            GameCrime[slot]  = 0;
            GameMission[slot] = 0;
            MissionDay[slot] = 0;
            for (int i = 1; i <= 20; i++) { GameSins[slot, i] = 0; GameResult[slot, i] = 0; }
            for (int i = 1; i <= 10; i++) GameCheckpoint(slot, i, 0);
        }

        public void GameCheckpoint(int slot, int cp, int val) => Checkpoint[slot, cp] = val;
        public int  GetCheckpoint(int slot, int cp)           => Checkpoint[slot, cp];

        // ── Sins / crimes ──────────────────────────────────────────────────────

        public void AddCrime(int type)
        {
            if (type < 0) { ResolveSin(SaveSlot); return; }
            GameCrime[SaveSlot]++;
            if (GameCrime[SaveSlot] > 20) GameCrime[SaveSlot] = 20;
            GameSins[SaveSlot, GameCrime[SaveSlot]] = type;
            if (GameSoul[SaveSlot] > GameConstants.SOUL_EVIL)
                GameSoul[SaveSlot]--;
        }

        private void ResolveSin(int slot)
        {
            for (int i = 1; i <= 20; i++)
            {
                if (GameSins[slot, i] > 0) { GameSins[slot, i] = -1; break; }
            }
            int unresolved = 0;
            for (int i = 1; i <= 20; i++)
                if (GameSins[slot, i] > 0) unresolved++;
            if (unresolved == 0 && GameSoul[slot] < GameConstants.SOUL_DIVINE)
                GameSoul[slot]++;
        }

        public int CountCrimes()
        {
            int c = 0;
            for (int i = 1; i <= 20; i++)
                if (GameSins[SaveSlot, i] > 0) c++;
            return c;
        }

        // ── Fade ──────────────────────────────────────────────────────────────
        void Update()
        {
            if (Paused) return;
            HandleSpeedKeys();
            LeaveIslandCheck();
        }

        private void HandleSpeedKeys()
        {
            if (GameMode != GameConstants.MODE_STORY) return;
            if (Input.GetKeyDown(KeyCode.F1)) GameSpeed = 1f;
            if (Input.GetKeyDown(KeyCode.F2)) GameSpeed = 1.33f;
            if (Input.GetKeyDown(KeyCode.F3)) GameSpeed = 1.67f;
            if (Input.GetKeyDown(KeyCode.F4)) GameSpeed = 2f;
            Time.timeScale = GameSpeed;
        }

        private void LeaveIslandCheck()
        {
            if (GameDestination > 0 && !_gameOver)
            {
                _gameOver = true;
                EndIsland();
            }
        }

        public float FadeAlpha  => _fadeAlpha;
        public float FadeTarget => _fadeTarget;
    }
}
