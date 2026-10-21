using UnityEngine;

namespace Wrecked
{
    /// <summary>
    /// Applies one of 21 costumes to a character by toggling hair mesh child
    /// visibility and swapping textures.  Translated from ApplyCostume() in Players.bb.
    /// </summary>
    public class CostumeSystem : MonoBehaviour
    {
        [Header("Hair Textures (index 1-8)")]
        public Texture2D[] HairTextures;   // 1-indexed; [0] unused

        [Header("Face Textures (index 1-7)")]
        public Texture2D[] FaceTextures;

        [Header("Body Textures (index 1-4)")]
        public Texture2D[] BodyTextures;

        [Header("Arms / Legs Textures (index 1-3)")]
        public Texture2D[] ArmsTextures;
        public Texture2D[] LegsTextures;

        private static readonly string[] AllHairMeshNames =
        {
            "Helmet","Hat","Hair_Short","Hair_Spike","Hair_Quiff","Hair_Mop",
            "Hair_Afro","Hair_Full","Hair_Punk","Hair_Bald","Hair_Thin","Hair_Thick","Hair_Pony"
        };

        // ══════════════════════════════════════════════════════════════════════

        public void Apply(CharacterBase c)
        {
            if (c.SceneObject == null) return;

            HideAllHair(c);

            switch (c.Costume)
            {
                case 1:  // player
                    c.Hair = 1; c.Face = 1; c.Body = 1; c.Arms = 1; c.Legs = 1;
                    ShowHair(c, "Hair_Full", c.Hair);
                    break;

                case 2:  // soldier
                    c.Hair = 2; c.Face = 2; c.Body = 2; c.Arms = 2; c.Legs = 2;
                    ShowHair(c, "Helmet", 0);
                    break;

                case 3:  // mystic native
                    c.Hair = 3; c.Face = 7; c.Body = 4; c.Arms = 3; c.Legs = 3;
                    ShowHair(c, "Hair_Full", c.Hair);
                    break;

                case 4: case 13: case 14:  // standard native
                    SetNativeDefaults(c);
                    ShowHair(c, "Hair_Full", c.Hair);
                    break;

                case 5:  // bald native
                    SetNativeDefaults(c);
                    // no hair shown
                    break;

                case 6:  // spikey
                    SetNativeDefaults(c);
                    ShowHair(c, "Hair_Spike", c.Hair);
                    break;

                case 7:  // afro
                    SetNativeDefaults(c);
                    ShowHair(c, "Hair_Afro", c.Hair);
                    break;

                case 8:  // short native (ponytail + thin)
                    SetNativeDefaults(c);
                    ShowHair(c, "Hair_Pony", c.Hair);
                    ShowHair(c, "Hair_Thin", c.Hair);
                    break;

                case 9:  // mohican
                    SetNativeDefaults(c);
                    ShowHair(c, "Hair_Punk", c.Hair);
                    break;

                case 10:  // fringe
                    SetNativeDefaults(c);
                    ShowHair(c, "Hair_Mop", c.Hair);
                    break;

                case 11:  // thick
                    SetNativeDefaults(c);
                    ShowHair(c, "Hair_Thick", c.Hair);
                    break;

                case 12:  // balding
                    SetNativeDefaults(c);
                    ShowHair(c, "Hair_Bald", c.Hair);
                    break;

                case 15: case 16: case 17: case 18: case 19: case 20: case 21:
                    ApplyNovelty(c);
                    break;
            }

            ApplyBodyTextures(c);
        }

        // ── Helpers ───────────────────────────────────────────────────────────

        private static void SetNativeDefaults(CharacterBase c)
        {
            if (c.Hair == 0) c.Hair = 2;
            if (c.Face == 0) c.Face = Random.Range(3, 7);
            if (c.Body == 0) c.Body = Random.Range(3, 5);
            if (c.Arms == 0) c.Arms = 3;
            if (c.Legs == 0) c.Legs = 3;
        }

        private static void HideAllHair(CharacterBase c)
        {
            foreach (var name in AllHairMeshNames)
            {
                var t = c.SceneObject.transform.FindDeep(name);
                if (t != null) t.gameObject.SetActive(false);
            }
        }

        private void ShowHair(CharacterBase c, string meshName, int hairIndex)
        {
            var t = c.SceneObject.transform.FindDeep(meshName);
            if (t == null) return;
            t.gameObject.SetActive(true);
            if (hairIndex > 0 && HairTextures != null && hairIndex < HairTextures.Length && HairTextures[hairIndex] != null)
            {
                var r = t.GetComponent<Renderer>();
                if (r) r.material.mainTexture = HairTextures[hairIndex];
            }
        }

        private void ApplyBodyTextures(CharacterBase c)
        {
            ApplyTex(c, "Face",  FaceTextures, c.Face);
            ApplyTex(c, "Body",  BodyTextures, c.Body);
            ApplyTex(c, "L_Arm", ArmsTextures, c.Arms);
            ApplyTex(c, "R_Arm", ArmsTextures, c.Arms);
            ApplyTex(c, "L_Leg", LegsTextures, c.Legs);
            ApplyTex(c, "R_Leg", LegsTextures, c.Legs);
        }

        private void ApplyTex(CharacterBase c, string meshName, Texture2D[] arr, int idx)
        {
            if (arr == null || idx <= 0 || idx >= arr.Length || arr[idx] == null) return;
            var t = c.SceneObject.transform.FindDeep(meshName);
            if (t == null) return;
            var r = t.GetComponent<Renderer>();
            if (r) r.material.mainTexture = arr[idx];
        }

        private static void ApplyNovelty(CharacterBase c)
        {
            // Novelty costumes 15-21: use random native base + show Hat
            SetNativeDefaults(c);
            var t = c.SceneObject.transform.FindDeep("Hat");
            if (t != null) t.gameObject.SetActive(true);
        }
    }
}
