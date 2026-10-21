using UnityEngine;

namespace Wrecked
{
    /// <summary>
    /// Resolves a lit shader that exists in whatever render pipeline the project
    /// is using, so procedural materials never fall back to the magenta error
    /// shader.  Checked in order: URP Lit, built-in Standard, legacy Diffuse.
    /// </summary>
    public static class ShaderUtil
    {
        private static Shader _lit;

        public static Shader Lit
        {
            get
            {
                if (_lit != null) return _lit;
                _lit = Shader.Find("Universal Render Pipeline/Lit")
                     ?? Shader.Find("Standard")
                     ?? Shader.Find("Legacy Shaders/Diffuse")
                     ?? Shader.Find("Sprites/Default");
                return _lit;
            }
        }
    }
}
