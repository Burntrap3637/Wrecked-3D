using UnityEngine;
using UnityEngine.UI;
using TMPro;

namespace Wrecked
{
    /// <summary>
    /// Heads-up display: vital bars, day counter, island label, speed indicator.
    /// </summary>
    public class HUDManager : MonoBehaviour
    {
        [Header("Vital Bars")]
        public Slider HealthBar;
        public Slider HungerBar;
        public Slider ThirstBar;

        [Header("Labels")]
        public TextMeshProUGUI DayLabel;
        public TextMeshProUGUI IslandLabel;
        public TextMeshProUGUI SpeedLabel;
        public TextMeshProUGUI MissionLabel;

        [Header("Carry Icons")]
        public Image CarryAIcon;
        public Image CarryBIcon;

        [Header("Bar Colors")]
        public Color HealthColor  = new Color(0.8f, 0.1f, 0.1f);
        public Color HungerColor  = new Color(0.8f, 0.6f, 0.1f);
        public Color ThirstColor  = new Color(0.1f, 0.5f, 0.9f);
        public Color CriticalColor= new Color(1f,   0f,   0f);

        [Header("Item Sprites")]
        public Sprite[] ItemSprites;   // indexed by item type

        private static readonly string[] IslandNames =
            { "", "North Island", "East Island", "South Island", "West Island", "Centre Island" };

        private static readonly string[] SpeedNames = { "Normal", "Fast", "Faster", "Fastest" };

        // ─────────────────────────────────────────────────────────────────────

        public void Refresh(CharacterBase player, int day, int island, int speedIndex)
        {
            UpdateBars(player);
            UpdateLabels(player, day, island, speedIndex);
            UpdateCarryIcons(player);
        }

        private void UpdateBars(CharacterBase p)
        {
            if (p == null) return;

            SetBar(HealthBar, p.Health, p.MaxHP,   HealthColor);
            SetBar(HungerBar, p.Hunger, 1000,       HungerColor);
            SetBar(ThirstBar, p.Thirst, 1000,       ThirstColor);
        }

        private void SetBar(Slider bar, float value, float max, Color normalColor)
        {
            if (bar == null) return;

            bar.value = Mathf.Clamp01(value / max);

            var fill = bar.fillRect?.GetComponent<Image>();
            if (fill != null)
                fill.color = bar.value < 0.25f ? CriticalColor : normalColor;
        }

        private void UpdateLabels(CharacterBase p, int day, int island, int speedIndex)
        {
            if (DayLabel   != null) DayLabel.text    = $"Day {day}";
            if (IslandLabel!= null) IslandLabel.text = island > 0 && island < IslandNames.Length
                                                        ? IslandNames[island] : "";
            if (SpeedLabel != null) SpeedLabel.text  = speedIndex > 0
                                                        ? SpeedNames[Mathf.Clamp(speedIndex, 0, 3)]
                                                        : "";
            if (MissionLabel != null && p != null)
            {
                int mission = GameManager.Instance.MissionMgr?.CurrentMission ?? 0;
                MissionLabel.text = mission > 0 ? $"Mission {mission}" : "";
            }
        }

        private void UpdateCarryIcons(CharacterBase p)
        {
            if (p == null) return;

            SetCarryIcon(CarryAIcon, p.CarryA);
            SetCarryIcon(CarryBIcon, p.CarryB);
        }

        private void SetCarryIcon(Image icon, int itemSlot)
        {
            if (icon == null) return;

            if (itemSlot == 0)
            {
                icon.enabled = false;
                return;
            }

            var item = GameManager.Instance.ItemMgr?.Get(itemSlot);
            if (item == null) { icon.enabled = false; return; }

            int typeIdx = item.ItemType;
            if (ItemSprites != null && typeIdx < ItemSprites.Length && ItemSprites[typeIdx] != null)
            {
                icon.sprite  = ItemSprites[typeIdx];
                icon.enabled = true;
            }
            else
            {
                icon.enabled = false;
            }
        }

        // ── Visibility ────────────────────────────────────────────────────────

        public void Show() => gameObject.SetActive(true);
        public void Hide() => gameObject.SetActive(false);
    }
}
