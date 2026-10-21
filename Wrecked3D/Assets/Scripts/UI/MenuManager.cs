using UnityEngine;
using UnityEngine.UI;
using TMPro;
using System.Collections.Generic;

namespace Wrecked
{
    /// <summary>
    /// Manages all game menus: title, mode select, save slots, options, deathmatch setup.
    /// </summary>
    public class MenuManager : MonoBehaviour
    {
        public enum Screen
        {
            None        = 0,
            Title       = 1,
            ModeSelect  = 2,
            SaveSlots   = 3,
            Options     = 4,
            Deathmatch  = 5,
            Pause       = 6,
            GameOver    = 7,
            Victory     = 8,
        }

        [Header("Screen Panels")]
        public GameObject TitlePanel;
        public GameObject ModeSelectPanel;
        public GameObject SaveSlotsPanel;
        public GameObject OptionsPanel;
        public GameObject DeathmatchPanel;
        public GameObject PausePanel;
        public GameObject GameOverPanel;
        public GameObject VictoryPanel;

        [Header("Title")]
        public Button StoryButton;
        public Button DeathmatchButton;
        public Button OptionsButton;
        public Button QuitButton;

        [Header("Save Slots")]
        public Button[] SlotButtons;         // 3 slots
        public TextMeshProUGUI[] SlotLabels;

        [Header("Options")]
        public Slider MusicSlider;
        public Slider SFXSlider;
        public Toggle FullscreenToggle;
        public Button ResetProgressButton;

        [Header("Deathmatch")]
        public TextMeshProUGUI PlayerCountLabel;
        public Button PlayerCountUp;
        public Button PlayerCountDown;
        public Button StartDeathmatchButton;

        [Header("Game Over / Victory")]
        public TextMeshProUGUI OutcomeLabel;
        public Button ReturnButton;

        private Screen _current = Screen.None;
        private int    _deathmatchPlayers = 8;

        private readonly List<GameObject> _allPanels = new();

        // ─────────────────────────────────────────────────────────────────────

        private void Awake()
        {
            CollectPanels();
            WireButtons();
        }

        private void CollectPanels()
        {
            _allPanels.AddRange(new[]
            {
                TitlePanel, ModeSelectPanel, SaveSlotsPanel, OptionsPanel,
                DeathmatchPanel, PausePanel, GameOverPanel, VictoryPanel
            });
        }

        private void WireButtons()
        {
            StoryButton?.onClick.AddListener(() => Open(Screen.SaveSlots));
            DeathmatchButton?.onClick.AddListener(() => Open(Screen.Deathmatch));
            OptionsButton?.onClick.AddListener(() => Open(Screen.Options));
            QuitButton?.onClick.AddListener(Application.Quit);

            for (int i = 0; i < SlotButtons?.Length; i++)
            {
                int slot = i + 1;
                SlotButtons[i].onClick.AddListener(() => BeginStorySlot(slot));
            }

            PlayerCountUp?.onClick.AddListener(() => AdjustDeathmatchPlayers(1));
            PlayerCountDown?.onClick.AddListener(() => AdjustDeathmatchPlayers(-1));
            StartDeathmatchButton?.onClick.AddListener(BeginDeathmatch);

            ResetProgressButton?.onClick.AddListener(() =>
            {
                GameManager.Instance.ResetAllProgress();
                RefreshSlotLabels();
            });

            ReturnButton?.onClick.AddListener(() => Open(Screen.Title));

            MusicSlider?.onValueChanged.AddListener(v =>
                AudioListener.volume = v);
        }

        // ── Navigation ────────────────────────────────────────────────────────

        public void Open(Screen screen)
        {
            _current = screen;

            foreach (var p in _allPanels)
                if (p != null) p.SetActive(false);

            var panel = PanelFor(screen);
            if (panel != null) panel.SetActive(true);

            if (screen == Screen.SaveSlots) RefreshSlotLabels();
            if (screen == Screen.Deathmatch) RefreshDeathmatchUI();
        }

        public void Close() => Open(Screen.None);

        public void ShowGameOver(bool won)
        {
            Open(won ? Screen.Victory : Screen.GameOver);
            if (OutcomeLabel != null)
                OutcomeLabel.text = won ? "You escaped!" : "You didn't make it.";
        }

        public void TogglePause()
        {
            if (_current == Screen.Pause) Close();
            else Open(Screen.Pause);
        }

        // ── Save slot display ─────────────────────────────────────────────────

        private void RefreshSlotLabels()
        {
            if (SlotLabels == null) return;
            var gm = GameManager.Instance;

            for (int i = 0; i < SlotLabels.Length; i++)
            {
                if (SlotLabels[i] == null) continue;
                int slot    = i + 1;
                int mission = gm.GameMission != null && slot < gm.GameMission.Length
                              ? gm.GameMission[slot] : 0;
                int day     = gm.MissionDay  != null && slot < gm.MissionDay.Length
                              ? gm.MissionDay[slot]  : 0;

                SlotLabels[i].text = mission > 0
                    ? $"Slot {slot}  —  Mission {mission}  /  Day {day}"
                    : $"Slot {slot}  —  New Game";
            }
        }

        // ── Deathmatch ────────────────────────────────────────────────────────

        private void AdjustDeathmatchPlayers(int delta)
        {
            _deathmatchPlayers = Mathf.Clamp(_deathmatchPlayers + delta, 2, GameConstants.MAX_PLAYERS);
            RefreshDeathmatchUI();
        }

        private void RefreshDeathmatchUI()
        {
            if (PlayerCountLabel != null)
                PlayerCountLabel.text = $"Players: {_deathmatchPlayers}";
        }

        private void BeginStorySlot(int slot)
        {
            GameManager.Instance.SaveSlot  = slot;
            GameManager.Instance.GameMode  = GameConstants.MODE_STORY;
            GameManager.Instance.BeginIsland();
            Close();
        }

        private void BeginDeathmatch()
        {
            GameManager.Instance.GameMode = GameConstants.MODE_DEATHMATCH;
            GameManager.Instance.BeginIsland();
            Close();
        }

        // ── Helpers ───────────────────────────────────────────────────────────

        private GameObject PanelFor(Screen screen) => screen switch
        {
            Screen.Title       => TitlePanel,
            Screen.ModeSelect  => ModeSelectPanel,
            Screen.SaveSlots   => SaveSlotsPanel,
            Screen.Options     => OptionsPanel,
            Screen.Deathmatch  => DeathmatchPanel,
            Screen.Pause       => PausePanel,
            Screen.GameOver    => GameOverPanel,
            Screen.Victory     => VictoryPanel,
            _                  => null,
        };
    }
}
