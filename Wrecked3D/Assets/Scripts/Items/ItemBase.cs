using UnityEngine;

namespace Wrecked
{
    /// <summary>
    /// Runtime state for one item slot (1-375).
    /// Plain data class managed by ItemManager.
    /// </summary>
    public class ItemBase
    {
        public int     SlotIndex;
        public int     ItemType;    // ITEM_* constant
        public int     Island;      // 0=inactive
        public int     State;       // 0=removed, 1=on ground, 2=carried
        public Vector3 Pos;
        public float   Angle;
        public bool    Flaming;
        public int     CarrierSlot; // which character carries this (0=none)

        public GameObject SceneObject;

        public bool IsActive => State > 0 && Island > 0;

        // ── Lifecycle ─────────────────────────────────────────────────────────

        public void PickUp(CharacterBase carrier)
        {
            State       = 2;
            CarrierSlot = carrier.SlotIndex;
            if (SceneObject != null) SceneObject.SetActive(false);
        }

        public void Drop(Vector3 position)
        {
            State       = 1;
            Pos         = position;
            CarrierSlot = 0;
            if (SceneObject != null)
            {
                SceneObject.SetActive(true);
                SceneObject.transform.position = position;
            }
        }

        public void Remove()
        {
            State  = 0;
            Island = 0;
            if (SceneObject != null) SceneObject.SetActive(false);
        }
    }
}
