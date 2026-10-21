using UnityEngine;
using UnityEngine.UI;
using TMPro;

namespace Wrecked
{
    /// <summary>
    /// Pop-up inventory panel showing the player's two hand slots, nearby items,
    /// and character stats.  Toggled with Tab.
    /// </summary>
    public class InventoryUI : MonoBehaviour
    {
        [Header("Panel")]
        public GameObject Panel;
        public KeyCode    ToggleKey = KeyCode.Tab;

        [Header("Hand Slots")]
        public Image         LeftHandIcon;
        public Image         RightHandIcon;
        public TextMeshProUGUI LeftHandLabel;
        public TextMeshProUGUI RightHandLabel;

        [Header("Nearby Items (up to 6 shown)")]
        public Image[]           NearbyIcons;
        public TextMeshProUGUI[] NearbyLabels;
        public float             NearbyRadius = 30f;

        [Header("Stats")]
        public TextMeshProUGUI HealthText;
        public TextMeshProUGUI HungerText;
        public TextMeshProUGUI ThirstText;
        public TextMeshProUGUI SoulText;
        public TextMeshProUGUI DayText;

        [Header("Crafting")]
        public Button          CraftButton;
        public TextMeshProUGUI CraftResultLabel;

        [Header("Item Sprites")]
        public Sprite[] ItemSprites;

        private bool _open;

        // ─────────────────────────────────────────────────────────────────────

        private void Awake()
        {
            CraftButton?.onClick.AddListener(OnCraftClick);
            if (Panel != null) Panel.SetActive(false);
        }

        private void Update()
        {
            if (Input.GetKeyDown(ToggleKey))
                Toggle();
        }

        public void Toggle()
        {
            _open = !_open;
            if (Panel != null) Panel.SetActive(_open);
            if (_open) Refresh();
        }

        // ── Refresh ───────────────────────────────────────────────────────────

        public void Refresh()
        {
            var gm     = GameManager.Instance;
            var player = gm.CharMgr?.Get(1);
            if (player == null) return;

            RefreshHandSlots(player, gm);
            RefreshNearby(player, gm);
            RefreshStats(player, gm);
            RefreshCraft(player, gm);
        }

        private void RefreshHandSlots(CharacterBase p, GameManager gm)
        {
            SetSlot(LeftHandIcon,  LeftHandLabel,  gm.ItemMgr?.Get(p.CarryA));
            SetSlot(RightHandIcon, RightHandLabel, gm.ItemMgr?.Get(p.CarryB));
        }

        private void SetSlot(Image icon, TextMeshProUGUI label, ItemBase item)
        {
            if (icon  != null) icon.enabled  = item != null;
            if (label != null) label.text     = item != null ? ItemData.Get(item.ItemType).TypeName : "Empty";

            if (icon == null || item == null) return;
            int t = item.ItemType;
            if (ItemSprites != null && t < ItemSprites.Length && ItemSprites[t] != null)
                icon.sprite = ItemSprites[t];
        }

        private void RefreshNearby(CharacterBase p, GameManager gm)
        {
            if (NearbyIcons == null) return;

            int shown = 0;
            for (int i = ItemManager.RangeStart(gm.CurrentIsland);
                     i <= ItemManager.RangeEnd(gm.CurrentIsland) && shown < NearbyIcons.Length; i++)
            {
                var item = gm.ItemMgr?.Get(i);
                if (item == null || item.State != 1) continue;
                if (Vector3.Distance(p.Pos, item.Pos) > NearbyRadius) continue;

                if (NearbyIcons[shown]  != null) NearbyIcons[shown].enabled  = true;
                if (NearbyLabels[shown] != null) NearbyLabels[shown].text    = ItemData.Get(item.ItemType).TypeName;

                int t = item.ItemType;
                if (ItemSprites != null && t < ItemSprites.Length && ItemSprites[t] != null)
                    NearbyIcons[shown].sprite = ItemSprites[t];

                shown++;
            }

            // Hide unused slots
            for (int i = shown; i < NearbyIcons.Length; i++)
            {
                if (NearbyIcons[i]  != null) NearbyIcons[i].enabled  = false;
                if (NearbyLabels[i] != null) NearbyLabels[i].text    = "";
            }
        }

        private void RefreshStats(CharacterBase p, GameManager gm)
        {
            int slot = gm.SaveSlot;
            if (HealthText != null) HealthText.text = $"Health  {p.Health} / {p.MaxHP}";
            if (HungerText != null) HungerText.text = $"Hunger  {p.Hunger} / 1000";
            if (ThirstText != null) ThirstText.text = $"Thirst  {p.Thirst} / 1000";
            if (DayText    != null) DayText.text    = $"Day {gm.GameDay[slot]}";

            if (SoulText != null)
            {
                string soulName = gm.GameSoul[slot] switch
                {
                    GameConstants.SOUL_EVIL   => "Evil",
                    GameConstants.SOUL_FLAWED => "Flawed",
                    GameConstants.SOUL_DIVINE => "Divine",
                    _                         => "Unknown"
                };
                SoulText.text = $"Soul  {soulName}";
            }
        }

        private void RefreshCraft(CharacterBase p, GameManager gm)
        {
            if (CraftButton == null) return;

            int result = CraftingSystem.TryCraft(p, gm.ItemMgr);
            CraftButton.interactable = result > 0;

            if (CraftResultLabel != null)
                CraftResultLabel.text = result > 0 ? $"Craft: {ItemData.Get(result).TypeName}" : "—";
        }

        // ── Craft button ──────────────────────────────────────────────────────

        private void OnCraftClick()
        {
            var gm     = GameManager.Instance;
            var player = gm.CharMgr?.Get(1);
            if (player == null) return;

            int result = CraftingSystem.TryCraft(player, gm.ItemMgr);
            if (result == 0) return;

            CraftingSystem.Craft(player, gm.ItemMgr, result);
            AudioManager.Instance?.Craft();
            Refresh();
        }
    }
}
