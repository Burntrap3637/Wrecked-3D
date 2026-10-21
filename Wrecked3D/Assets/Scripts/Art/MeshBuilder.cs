using System.Collections.Generic;
using UnityEngine;

namespace Wrecked
{
    /// <summary>
    /// Generates real 3D geometry from raw vertex/triangle data — our own models,
    /// not Unity's built-in primitive meshes.  Base dimensions match Unity's
    /// primitives (unit cube, radius-0.5 sphere, height-2 cylinder/capsule) so
    /// the model factory's existing scale values still read correctly.
    ///
    /// Meshes are cached by shape+resolution so 60 characters share geometry.
    /// </summary>
    public static class MeshBuilder
    {
        private static readonly Dictionary<string, Mesh> _cache = new();

        public enum Shape { Cube, Sphere, Cylinder, Capsule, Cone, Quad, TaperedBox }

        public static Mesh Get(Shape shape)
        {
            string key = shape.ToString();
            if (_cache.TryGetValue(key, out var m) && m != null) return m;

            m = shape switch
            {
                Shape.Cube       => BuildBox(Vector3.one),
                Shape.Sphere     => BuildSphere(0.5f, 10, 14),
                Shape.Cylinder   => BuildCylinder(0.5f, 0.5f, 2f, 12),
                Shape.Capsule    => BuildCapsule(0.5f, 2f, 10),
                Shape.Cone       => BuildCylinder(0.5f, 0.001f, 2f, 12),
                Shape.Quad       => BuildQuad(),
                Shape.TaperedBox => BuildTaperedBox(1f, 0.6f, 1f),
                _                => BuildBox(Vector3.one),
            };
            m.name = "Wrecked_" + key;
            _cache[key] = m;
            return m;
        }

        // ════════════════════════════════════════════════════════════════════
        //  BOX  (24 verts — hard edges)
        // ════════════════════════════════════════════════════════════════════

        public static Mesh BuildBox(Vector3 size)
        {
            Vector3 h = size * 0.5f;
            var verts = new Vector3[24];
            var norms = new Vector3[24];
            var uvs   = new Vector2[24];
            var tris  = new int[36];

            // 6 faces: +X,-X,+Y,-Y,+Z,-Z
            Vector3[] faceN =
            {
                Vector3.right, Vector3.left, Vector3.up,
                Vector3.down, Vector3.forward, Vector3.back
            };

            for (int f = 0; f < 6; f++)
            {
                Vector3 n = faceN[f];
                Vector3 u, v;
                BasisFor(n, out u, out v);

                int b = f * 4;
                verts[b + 0] = Vector3.Scale(n * 1f + (-u - v), h);
                verts[b + 1] = Vector3.Scale(n * 1f + ( u - v), h);
                verts[b + 2] = Vector3.Scale(n * 1f + ( u + v), h);
                verts[b + 3] = Vector3.Scale(n * 1f + (-u + v), h);

                for (int k = 0; k < 4; k++) norms[b + k] = n;
                uvs[b + 0] = new Vector2(0, 0);
                uvs[b + 1] = new Vector2(1, 0);
                uvs[b + 2] = new Vector2(1, 1);
                uvs[b + 3] = new Vector2(0, 1);

                int t = f * 6;
                tris[t + 0] = b + 0; tris[t + 1] = b + 2; tris[t + 2] = b + 1;
                tris[t + 3] = b + 0; tris[t + 4] = b + 3; tris[t + 5] = b + 2;
            }

            return Finish(verts, tris, norms, uvs);
        }

        private static void BasisFor(Vector3 n, out Vector3 u, out Vector3 v)
        {
            if (Mathf.Abs(n.y) > 0.5f) { u = Vector3.right;   v = Vector3.forward; }
            else                        { u = Vector3.Cross(Vector3.up, n).normalized; v = Vector3.up; }
        }

        // ════════════════════════════════════════════════════════════════════
        //  TAPERED BOX  (limb/torso frustum, narrows toward +Y)
        // ════════════════════════════════════════════════════════════════════

        public static Mesh BuildTaperedBox(float bottom, float top, float height)
        {
            float hb = bottom * 0.5f, ht = top * 0.5f, hy = height * 0.5f;

            // 8 corners: bottom (y=-hy) wide, top (y=+hy) narrow
            Vector3[] c =
            {
                new(-hb, -hy, -hb), new( hb, -hy, -hb), new( hb, -hy,  hb), new(-hb, -hy,  hb),
                new(-ht,  hy, -ht), new( ht,  hy, -ht), new( ht,  hy,  ht), new(-ht,  hy,  ht),
            };

            int[][] faces =
            {
                new[]{0,1,2,3}, // bottom
                new[]{7,6,5,4}, // top
                new[]{0,4,5,1}, // -Z
                new[]{1,5,6,2}, // +X
                new[]{2,6,7,3}, // +Z
                new[]{3,7,4,0}, // -X
            };

            var verts = new List<Vector3>();
            var tris  = new List<int>();
            foreach (var f in faces)
            {
                int b = verts.Count;
                verts.Add(c[f[0]]); verts.Add(c[f[1]]); verts.Add(c[f[2]]); verts.Add(c[f[3]]);
                tris.AddRange(new[] { b, b + 2, b + 1, b, b + 3, b + 2 });
            }
            return Finish(verts.ToArray(), tris.ToArray(), null, null);
        }

        // ════════════════════════════════════════════════════════════════════
        //  CYLINDER / CONE  (radiusTop can differ from radiusBottom)
        // ════════════════════════════════════════════════════════════════════

        public static Mesh BuildCylinder(float radiusBottom, float radiusTop, float height, int sides)
        {
            var verts = new List<Vector3>();
            var tris  = new List<int>();
            float hy  = height * 0.5f;

            // Side ring verts (doubled so each side quad has its own normal set via recalc)
            for (int i = 0; i < sides; i++)
            {
                float a0 = (float)i       / sides * Mathf.PI * 2f;
                float a1 = (float)(i + 1) / sides * Mathf.PI * 2f;

                Vector3 b0 = new(Mathf.Cos(a0) * radiusBottom, -hy, Mathf.Sin(a0) * radiusBottom);
                Vector3 b1 = new(Mathf.Cos(a1) * radiusBottom, -hy, Mathf.Sin(a1) * radiusBottom);
                Vector3 t0 = new(Mathf.Cos(a0) * radiusTop,     hy, Mathf.Sin(a0) * radiusTop);
                Vector3 t1 = new(Mathf.Cos(a1) * radiusTop,     hy, Mathf.Sin(a1) * radiusTop);

                int b = verts.Count;
                verts.Add(b0); verts.Add(b1); verts.Add(t1); verts.Add(t0);
                tris.AddRange(new[] { b, b + 2, b + 1, b, b + 3, b + 2 });
            }

            // Caps
            AddFan(verts, tris, radiusBottom,  -hy, sides, facingDown:true);
            AddFan(verts, tris, radiusTop,      hy, sides, facingDown:false);

            return Finish(verts.ToArray(), tris.ToArray(), null, null);
        }

        private static void AddFan(List<Vector3> verts, List<int> tris, float radius, float y, int sides, bool facingDown)
        {
            if (radius < 0.002f) return;   // cone tip — no cap
            int center = verts.Count;
            verts.Add(new Vector3(0f, y, 0f));
            int ring = verts.Count;
            for (int i = 0; i < sides; i++)
            {
                float a = (float)i / sides * Mathf.PI * 2f;
                verts.Add(new Vector3(Mathf.Cos(a) * radius, y, Mathf.Sin(a) * radius));
            }
            for (int i = 0; i < sides; i++)
            {
                int cur = ring + i;
                int nxt = ring + (i + 1) % sides;
                if (facingDown) tris.AddRange(new[] { center, cur, nxt });
                else            tris.AddRange(new[] { center, nxt, cur });
            }
        }

        // ════════════════════════════════════════════════════════════════════
        //  SPHERE  (UV sphere)
        // ════════════════════════════════════════════════════════════════════

        public static Mesh BuildSphere(float radius, int rings, int segments)
        {
            var verts = new List<Vector3>();
            var uvs   = new List<Vector2>();
            var tris  = new List<int>();

            for (int y = 0; y <= rings; y++)
            {
                float v   = (float)y / rings;
                float phi = v * Mathf.PI;
                for (int x = 0; x <= segments; x++)
                {
                    float u     = (float)x / segments;
                    float theta = u * Mathf.PI * 2f;
                    Vector3 p = new(
                        Mathf.Sin(phi) * Mathf.Cos(theta),
                        Mathf.Cos(phi),
                        Mathf.Sin(phi) * Mathf.Sin(theta));
                    verts.Add(p * radius);
                    uvs.Add(new Vector2(u, 1f - v));
                }
            }

            int stride = segments + 1;
            for (int y = 0; y < rings; y++)
            {
                for (int x = 0; x < segments; x++)
                {
                    int i0 = y * stride + x;
                    int i1 = i0 + 1;
                    int i2 = i0 + stride;
                    int i3 = i2 + 1;
                    tris.AddRange(new[] { i0, i2, i1, i1, i2, i3 });
                }
            }

            return Finish(verts.ToArray(), tris.ToArray(), null, uvs.ToArray());
        }

        // ════════════════════════════════════════════════════════════════════
        //  CAPSULE  (cylinder body + two hemisphere caps, total height 2)
        // ════════════════════════════════════════════════════════════════════

        public static Mesh BuildCapsule(float radius, float height, int segments)
        {
            var verts = new List<Vector3>();
            var tris  = new List<int>();

            float cyl  = Mathf.Max(0f, height - radius * 2f);
            float half = cyl * 0.5f;
            int   rings = Mathf.Max(2, segments / 2);

            // Build as stacked rings from bottom pole to top pole
            var rows = new List<(float y, float r)>();

            // bottom hemisphere
            for (int i = 0; i <= rings; i++)
            {
                float a = Mathf.PI * 0.5f * (float)i / rings;   // 0..90
                float y = -half - Mathf.Cos(a) * radius;
                float r = Mathf.Sin(a) * radius;
                rows.Add((y, r));
            }
            // top hemisphere
            for (int i = 0; i <= rings; i++)
            {
                float a = Mathf.PI * 0.5f * (float)i / rings;
                float y = half + Mathf.Sin(a) * radius;
                float r = Mathf.Cos(a) * radius;
                rows.Add((y, r));
            }

            int stride = segments + 1;
            foreach (var (y, r) in rows)
                for (int x = 0; x <= segments; x++)
                {
                    float theta = (float)x / segments * Mathf.PI * 2f;
                    verts.Add(new Vector3(Mathf.Cos(theta) * r, y, Mathf.Sin(theta) * r));
                }

            for (int row = 0; row < rows.Count - 1; row++)
                for (int x = 0; x < segments; x++)
                {
                    int i0 = row * stride + x;
                    int i1 = i0 + 1;
                    int i2 = i0 + stride;
                    int i3 = i2 + 1;
                    tris.AddRange(new[] { i0, i2, i1, i1, i2, i3 });
                }

            return Finish(verts.ToArray(), tris.ToArray(), null, null);
        }

        // ════════════════════════════════════════════════════════════════════
        //  QUAD  (10x10 plane in XZ, matches Unity Plane footprint at scale 1)
        // ════════════════════════════════════════════════════════════════════

        public static Mesh BuildQuad()
        {
            float h = 5f;
            var verts = new[]
            {
                new Vector3(-h, 0f, -h), new Vector3(-h, 0f, h),
                new Vector3( h, 0f,  h), new Vector3( h, 0f, -h),
            };
            var uvs  = new[] { new Vector2(0,0), new Vector2(0,1), new Vector2(1,1), new Vector2(1,0) };
            var tris = new[] { 0, 1, 2, 0, 2, 3 };
            var norms= new[] { Vector3.up, Vector3.up, Vector3.up, Vector3.up };
            return Finish(verts, tris, norms, uvs);
        }

        // ════════════════════════════════════════════════════════════════════

        private static Mesh Finish(Vector3[] verts, int[] tris, Vector3[] norms, Vector2[] uvs)
        {
            var m = new Mesh();
            if (verts.Length > 65000) m.indexFormat = UnityEngine.Rendering.IndexFormat.UInt32;
            m.vertices  = verts;
            m.triangles = tris;
            if (uvs != null) m.uv = uvs;
            if (norms != null) m.normals = norms;
            else m.RecalculateNormals();
            m.RecalculateBounds();
            return m;
        }
    }
}
