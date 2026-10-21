using UnityEngine;

namespace Wrecked
{
    /// <summary>
    /// Manages the 20-limb scarring system.
    /// Translated from ManageScars() / ScarLimb() / ScarBody() in Players.bb.
    /// Scar values are accumulated per limb; renderer material tint darkens accordingly.
    /// </summary>
    public class ScarSystem : MonoBehaviour
    {
        [Header("Scar Textures")]
        public Texture2D[] ScarTextures;   // 0=none, 1-4=progressive damage

        // Bone-name → limb index mapping (matches GetLimbs order)
        private static readonly (int limbIdx, string boneName)[] LimbMap =
        {
            (1,"Head"),    (2,"Neck"),    (3,"Body"),
            (4,"L_Bicep"), (5,"R_Bicep"), (6,"L_Arm"),  (7,"R_Arm"),
            (8,"L_Hand"),  (9,"R_Hand"),  (10,"Hips"),
            (11,"L_Thigh"),(12,"R_Thigh"),(13,"L_Leg"), (14,"R_Leg"),
            (15,"L_Foot"), (16,"R_Foot")
        };

        // ══════════════════════════════════════════════════════════════════════

        public void UpdateScars(CharacterBase c)
        {
            if (c.SceneObject == null) return;

            foreach (var (idx, boneName) in LimbMap)
            {
                int scar = c.GetScar(idx);
                if (scar <= 0) continue;
                ApplyScarToLimb(c, idx, boneName, scar);
            }
        }

        private void ApplyScarToLimb(CharacterBase c, int limb, string boneName, int scarValue)
        {
            var t = c.SceneObject.transform.FindDeep(boneName);
            if (t == null) return;
            var r = t.GetComponent<Renderer>();
            if (r == null) return;

            // Choose texture tier (0-4) based on scar value (0-255)
            int tier = Mathf.Clamp(scarValue / 64, 0, 4);

            if (ScarTextures != null && tier < ScarTextures.Length && ScarTextures[tier] != null)
                r.material.mainTexture = ScarTextures[tier];
            else
            {
                // Fallback: darken material colour
                float darkness = 1f - (scarValue / 255f) * 0.6f;
                r.material.color = new Color(darkness, darkness * 0.7f, darkness * 0.7f);
            }
        }

        // ── Public scar-infliction helpers ────────────────────────────────────

        public void ScarLimb(CharacterBase c, int limb, int amount = 1)
            => c.AddScar(limb, amount);

        public void ScarBody(CharacterBase c, int amount = 1)
        {
            // Distribute scar to a random body limb
            int limb = Random.Range(1, 17);
            c.AddScar(limb, amount);
        }
    }
}
