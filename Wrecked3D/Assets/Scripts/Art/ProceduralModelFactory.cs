using UnityEngine;

namespace Wrecked
{
    /// <summary>
    /// Builds placeholder character/item/resource models entirely from Unity
    /// primitives, with the exact child-transform names the gameplay code looks
    /// up (Head, Neck, Body, L_Bicep, Hair_Full, etc.).  Lets the whole game run
    /// with zero imported art — drop real meshes in later and these go away.
    /// </summary>
    public static class ProceduralModelFactory
    {
        // Skin / clothing tints per character type
        private static readonly Color SkinHuman   = new Color(0.86f, 0.68f, 0.52f);
        private static readonly Color ClothHuman  = new Color(0.55f, 0.42f, 0.30f);
        private static readonly Color ClothSoldier= new Color(0.30f, 0.34f, 0.24f);
        private static readonly Color ClothMystic = new Color(0.78f, 0.74f, 0.90f);
        private static readonly Color ClothNative = new Color(0.70f, 0.45f, 0.28f);
        private static readonly Color FurLeopard  = new Color(0.82f, 0.68f, 0.32f);
        private static readonly Color FurWolf     = new Color(0.45f, 0.45f, 0.48f);
        private static readonly Color FurDeer     = new Color(0.60f, 0.42f, 0.26f);
        private static readonly Color FurRabbit   = new Color(0.85f, 0.82f, 0.78f);
        private static readonly Color ScaleFish   = new Color(0.40f, 0.60f, 0.72f);

        // ════════════════════════════════════════════════════════════════════
        //  CHARACTERS
        // ════════════════════════════════════════════════════════════════════

        public static GameObject BuildCharacter(int charType)
        {
            switch (charType)
            {
                case GameConstants.TYPE_PLAYER:  return BuildHumanoid("Human",   SkinHuman,  ClothHuman);
                case GameConstants.TYPE_SOLDIER: return BuildHumanoid("Soldier", SkinHuman,  ClothSoldier);
                case GameConstants.TYPE_MYSTIC:  return BuildHumanoid("Mystic",  SkinHuman,  ClothMystic);
                case GameConstants.TYPE_NATIVE:  return BuildHumanoid("Native",  SkinHuman,  ClothNative);
                case GameConstants.TYPE_LEOPARD: return BuildQuadruped("Leopard", FurLeopard, hasPaws:true,  longTail:true);
                case GameConstants.TYPE_WOLF:    return BuildQuadruped("Wolf",    FurWolf,    hasPaws:true,  longTail:true);
                case GameConstants.TYPE_DEER:    return BuildQuadruped("Deer",    FurDeer,    hasPaws:false, longTail:false);
                case GameConstants.TYPE_RABBIT:  return BuildRabbit("Rabbit",     FurRabbit);
                case GameConstants.TYPE_FISH:    return BuildFish("Fish",         ScaleFish);
                default:                         return BuildHumanoid("Human",   SkinHuman,  ClothHuman);
            }
        }

        // ── Humanoid rig ──────────────────────────────────────────────────────

        private static GameObject BuildHumanoid(string typeName, Color skin, Color cloth)
        {
            var root = new GameObject(typeName);

            // Hips sit ~9 units up; whole figure ~17 units tall
            var hips = Bone("Hips", root.transform, new Vector3(0f, 9f, 0f));
            Mesh(MeshBuilder.Shape.Cube, hips, cloth, new Vector3(3.2f, 1.6f, 2.0f), Vector3.zero);

            var body = Bone("Body", hips, new Vector3(0f, 2.4f, 0f));
            Mesh(MeshBuilder.Shape.Cube, body, cloth, new Vector3(3.6f, 3.6f, 2.2f), new Vector3(0f, 0.6f, 0f));

            var neck = Bone("Neck", body, new Vector3(0f, 2.6f, 0f));
            Mesh(MeshBuilder.Shape.Cylinder, neck, skin, new Vector3(0.8f, 0.5f, 0.8f), Vector3.zero);

            var head = Bone("Head", neck, new Vector3(0f, 1.3f, 0f));
            Mesh(MeshBuilder.Shape.Sphere, head, skin, new Vector3(2.0f, 2.2f, 2.0f), Vector3.zero);
            BuildHair(head, cloth);

            // Arms
            BuildArm(body, "L", -1f, skin, cloth);
            BuildArm(body, "R",  1f, skin, cloth);

            // Legs
            BuildLeg(hips, "L", -1f, skin, cloth);
            BuildLeg(hips, "R",  1f, skin, cloth);

            return root;
        }

        private static void BuildArm(Transform body, string side, float dir, Color skin, Color cloth)
        {
            var bicep = Bone(side + "_Bicep", body, new Vector3(dir * 2.3f, 1.4f, 0f));
            Mesh(MeshBuilder.Shape.Capsule, bicep, cloth, new Vector3(0.9f, 1.3f, 0.9f), new Vector3(0f, -1.1f, 0f));

            var arm = Bone(side + "_Arm", bicep, new Vector3(0f, -2.3f, 0f));
            Mesh(MeshBuilder.Shape.Capsule, arm, skin, new Vector3(0.8f, 1.2f, 0.8f), new Vector3(0f, -1.0f, 0f));

            var hand = Bone(side + "_Hand", arm, new Vector3(0f, -2.2f, 0f));
            Mesh(MeshBuilder.Shape.Cube, hand, skin, new Vector3(0.9f, 0.9f, 0.6f), Vector3.zero);
        }

        private static void BuildLeg(Transform hips, string side, float dir, Color skin, Color cloth)
        {
            var thigh = Bone(side + "_Thigh", hips, new Vector3(dir * 1.0f, -1.0f, 0f));
            Mesh(MeshBuilder.Shape.Capsule, thigh, cloth, new Vector3(1.1f, 1.6f, 1.1f), new Vector3(0f, -1.3f, 0f));

            var leg = Bone(side + "_Leg", thigh, new Vector3(0f, -2.8f, 0f));
            Mesh(MeshBuilder.Shape.Capsule, leg, skin, new Vector3(1.0f, 1.5f, 1.0f), new Vector3(0f, -1.2f, 0f));

            var foot = Bone(side + "_Foot", leg, new Vector3(0f, -2.6f, 0.4f));
            Mesh(MeshBuilder.Shape.Cube, foot, cloth, new Vector3(1.0f, 0.6f, 2.0f), new Vector3(0f, 0f, 0.4f));
        }

        private static void BuildHair(Transform head, Color cloth)
        {
            // Every variant the costume system can toggle — hidden by default,
            // CostumeSystem enables exactly one.
            string[] names =
            {
                "Helmet","Hat","Hair_Short","Hair_Spike","Hair_Quiff","Hair_Mop",
                "Hair_Afro","Hair_Full","Hair_Punk","Hair_Bald","Hair_Thin","Hair_Thick","Hair_Pony"
            };
            foreach (var n in names)
            {
                var hair = Bone(n, head, new Vector3(0f, 0.9f, 0f));
                var prim = n is "Helmet" or "Hair_Bald" ? MeshBuilder.Shape.Sphere : MeshBuilder.Shape.Cube;
                Color c  = n == "Helmet" ? new Color(0.4f,0.4f,0.45f)
                         : n == "Hat"    ? cloth
                         : new Color(0.25f, 0.18f, 0.12f);   // hair brown; retinted by costume
                Mesh(prim, hair, c, new Vector3(2.1f, 1.1f, 2.1f), Vector3.zero);
                hair.gameObject.SetActive(false);
            }
        }

        // ── Quadruped rig (leopard / wolf / deer) ───────────────────────────────

        private static GameObject BuildQuadruped(string typeName, Color fur, bool hasPaws, bool longTail)
        {
            var root = new GameObject(typeName);

            var hips = Bone("Hips", root.transform, new Vector3(0f, 5f, -2.5f));
            Mesh(MeshBuilder.Shape.Cube, hips, fur, new Vector3(2.2f, 2.2f, 2.6f), Vector3.zero);

            var torso = Bone("Torso", hips, new Vector3(0f, 0.2f, 4.0f));
            Mesh(MeshBuilder.Shape.Capsule, torso, fur, new Vector3(2.2f, 3.0f, 2.2f),
                 new Vector3(0f, 0f, -2.0f), new Vector3(90f, 0f, 0f));

            var neck = Bone("Neck", torso, new Vector3(0f, 0.6f, 2.4f));
            Mesh(MeshBuilder.Shape.Capsule, neck, fur, new Vector3(1.2f, 1.2f, 1.2f), Vector3.zero, new Vector3(60f,0f,0f));

            var head = Bone("Head", neck, new Vector3(0f, 0.8f, 1.6f));
            Mesh(MeshBuilder.Shape.Cube, head, fur, new Vector3(1.6f, 1.6f, 2.4f), Vector3.zero);

            // Front legs hang off the torso, rear off the hips
            BuildQuadLeg(torso, "L", -1f, fur, hasPaws, front:true);
            BuildQuadLeg(torso, "R",  1f, fur, hasPaws, front:true);
            BuildQuadLeg(hips,  "L", -1f, fur, hasPaws, front:false);
            BuildQuadLeg(hips,  "R",  1f, fur, hasPaws, front:false);

            if (longTail)
            {
                var t1 = Bone("Tail01", hips, new Vector3(0f, 0.6f, -1.6f));
                Mesh(MeshBuilder.Shape.Capsule, t1, fur, new Vector3(0.6f,0.9f,0.6f), new Vector3(0f,0f,-0.8f), new Vector3(70f,0f,0f));
                var t2 = Bone("Tail02", t1, new Vector3(0f, 0f, -1.4f));
                Mesh(MeshBuilder.Shape.Capsule, t2, fur, new Vector3(0.5f,0.8f,0.5f), new Vector3(0f,0f,-0.7f), new Vector3(80f,0f,0f));
                var t3 = Bone("Tail03", t2, new Vector3(0f, 0f, -1.2f));
                Mesh(MeshBuilder.Shape.Capsule, t3, fur, new Vector3(0.4f,0.7f,0.4f), new Vector3(0f,0f,-0.6f), new Vector3(85f,0f,0f));
            }
            else
            {
                var tail = Bone("Tail", hips, new Vector3(0f, 1.0f, -1.6f));
                Mesh(MeshBuilder.Shape.Sphere, tail, Color.white, new Vector3(0.9f,0.9f,0.9f), Vector3.zero);
            }

            return root;
        }

        private static void BuildQuadLeg(Transform parent, string side, float dir, Color fur, bool hasPaws, bool front)
        {
            string biName = front ? side + "_Bicep" : side + "_Thigh";
            string loName = front ? side + "_Arm"   : side + "_Leg";
            string ftName = front
                ? (hasPaws ? side + "_Paw"  : side + "_Hand")
                : (hasPaws ? side + "_Paw"  : side + "_Foot");

            var upper = Bone(biName, parent, new Vector3(dir * 1.2f, -1.0f, front ? 1.2f : -0.6f));
            Mesh(MeshBuilder.Shape.Capsule, upper, fur, new Vector3(0.7f,1.3f,0.7f), new Vector3(0f,-1.1f,0f));

            var lower = Bone(loName, upper, new Vector3(0f, -2.2f, 0f));
            Mesh(MeshBuilder.Shape.Capsule, lower, fur, new Vector3(0.6f,1.2f,0.6f), new Vector3(0f,-1.0f,0f));

            var foot = Bone(ftName, lower, new Vector3(0f, -2.2f, 0.3f));
            Mesh(MeshBuilder.Shape.Cube, foot, fur, new Vector3(0.8f,0.5f,1.2f), Vector3.zero);
        }

        // ── Rabbit (bipedal-ish small) ──────────────────────────────────────────

        private static GameObject BuildRabbit(string typeName, Color fur)
        {
            var root = new GameObject(typeName);

            var hips = Bone("Hips", root.transform, new Vector3(0f, 2.5f, 0f));
            Mesh(MeshBuilder.Shape.Sphere, hips, fur, new Vector3(2.0f,1.8f,2.2f), Vector3.zero);

            var torso = Bone("Torso", hips, new Vector3(0f, 1.2f, 0.3f));
            Mesh(MeshBuilder.Shape.Sphere, torso, fur, new Vector3(1.8f,2.0f,1.8f), Vector3.zero);

            var neck = Bone("Neck", torso, new Vector3(0f, 1.2f, 0.2f));
            Mesh(MeshBuilder.Shape.Cylinder, neck, fur, new Vector3(0.5f,0.3f,0.5f), Vector3.zero);

            var head = Bone("Head", neck, new Vector3(0f, 0.8f, 0.2f));
            Mesh(MeshBuilder.Shape.Sphere, head, fur, new Vector3(1.4f,1.4f,1.6f), Vector3.zero);

            var lEar = Bone("L_Ear", head, new Vector3(-0.4f, 1.1f, 0f));
            Mesh(MeshBuilder.Shape.Capsule, lEar, fur, new Vector3(0.4f,0.9f,0.3f), new Vector3(0f,0.7f,0f));
            var rEar = Bone("R_Ear", head, new Vector3(0.4f, 1.1f, 0f));
            Mesh(MeshBuilder.Shape.Capsule, rEar, fur, new Vector3(0.4f,0.9f,0.3f), new Vector3(0f,0.7f,0f));

            BuildQuadLeg(torso, "L", -1f, fur, hasPaws:true, front:true);
            BuildQuadLeg(torso, "R",  1f, fur, hasPaws:true, front:true);
            BuildQuadLeg(hips,  "L", -1f, fur, hasPaws:false, front:false);
            BuildQuadLeg(hips,  "R",  1f, fur, hasPaws:false, front:false);

            var tail = Bone("Tail", hips, new Vector3(0f, 0.5f, -1.4f));
            Mesh(MeshBuilder.Shape.Sphere, tail, Color.white, new Vector3(0.8f,0.8f,0.8f), Vector3.zero);

            return root;
        }

        // ── Fish ────────────────────────────────────────────────────────────────

        private static GameObject BuildFish(string typeName, Color scale)
        {
            var root = new GameObject(typeName);

            var hips = Bone("Hips", root.transform, new Vector3(0f, 2f, 0f));
            Mesh(MeshBuilder.Shape.Capsule, hips, scale, new Vector3(1.4f,2.6f,1.4f), Vector3.zero, new Vector3(0f,0f,90f));

            var torso = Bone("Torso", hips, new Vector3(0f,0f,1.6f));
            Mesh(MeshBuilder.Shape.Capsule, torso, scale, new Vector3(1.6f,2.0f,1.6f), Vector3.zero, new Vector3(0f,0f,90f));

            var neck = Bone("Neck", torso, new Vector3(0f,0f,1.4f));
            var head = Bone("Head", neck, new Vector3(0f,0f,0.8f));
            Mesh(MeshBuilder.Shape.Sphere, head, scale, new Vector3(1.6f,1.6f,1.8f), Vector3.zero);

            var tail = Bone("Tail", hips, new Vector3(0f,0f,-1.8f));
            Mesh(MeshBuilder.Shape.Cube, tail, scale, new Vector3(0.3f,2.2f,1.6f), Vector3.zero);

            return root;
        }

        // ════════════════════════════════════════════════════════════════════
        //  ITEMS
        // ════════════════════════════════════════════════════════════════════

        public static GameObject BuildItem(int itemType)
        {
            (MeshBuilder.Shape prim, Color col, Vector3 size, string name) spec = itemType switch
            {
                GameConstants.ITEM_VINES    => (MeshBuilder.Shape.Capsule, new Color(0.30f,0.55f,0.20f), new Vector3(0.3f,1.2f,0.3f), "Vines"),
                GameConstants.ITEM_WOOD     => (MeshBuilder.Shape.Cylinder, new Color(0.45f,0.30f,0.16f), new Vector3(0.4f,1.4f,0.4f), "Wood"),
                GameConstants.ITEM_STONE    => (MeshBuilder.Shape.Sphere,   new Color(0.55f,0.55f,0.58f), new Vector3(0.9f,0.7f,0.9f), "Stone"),
                GameConstants.ITEM_MEAT     => (MeshBuilder.Shape.Sphere,   new Color(0.70f,0.25f,0.25f), new Vector3(0.8f,0.6f,0.8f), "Meat"),
                GameConstants.ITEM_FRUIT    => (MeshBuilder.Shape.Sphere,   new Color(0.85f,0.55f,0.15f), new Vector3(0.6f,0.6f,0.6f), "Fruit"),
                GameConstants.ITEM_WHIP     => (MeshBuilder.Shape.Capsule,  new Color(0.35f,0.22f,0.12f), new Vector3(0.2f,1.6f,0.2f), "Whip"),
                GameConstants.ITEM_STICK    => (MeshBuilder.Shape.Cylinder, new Color(0.40f,0.28f,0.15f), new Vector3(0.3f,1.8f,0.3f), "Stick"),
                GameConstants.ITEM_ROCK     => (MeshBuilder.Shape.Sphere,   new Color(0.50f,0.50f,0.52f), new Vector3(0.7f,0.6f,0.7f), "Rock"),
                GameConstants.ITEM_HAMMER   => (MeshBuilder.Shape.Cube,     new Color(0.60f,0.60f,0.62f), new Vector3(0.6f,1.6f,0.6f), "Hammer"),
                GameConstants.ITEM_SPEAR    => (MeshBuilder.Shape.Cylinder, new Color(0.50f,0.35f,0.20f), new Vector3(0.2f,2.4f,0.2f), "Spear"),
                GameConstants.ITEM_AXE      => (MeshBuilder.Shape.Cube,     new Color(0.55f,0.40f,0.25f), new Vector3(0.5f,1.8f,0.3f), "Axe"),
                GameConstants.ITEM_GUN      => (MeshBuilder.Shape.Cube,     new Color(0.20f,0.20f,0.22f), new Vector3(0.4f,0.6f,1.4f), "Gun"),
                GameConstants.ITEM_ARTEFACT => (MeshBuilder.Shape.Sphere,   new Color(0.90f,0.80f,0.30f), new Vector3(1.0f,1.0f,1.0f), "Artefact"),
                GameConstants.ITEM_BONE     => (MeshBuilder.Shape.Capsule,  new Color(0.90f,0.88f,0.80f), new Vector3(0.3f,1.4f,0.3f), "Bone"),
                GameConstants.ITEM_TRAP     => (MeshBuilder.Shape.Cylinder, new Color(0.45f,0.45f,0.48f), new Vector3(1.2f,0.2f,1.2f), "Trap"),
                GameConstants.ITEM_OAR      => (MeshBuilder.Shape.Cube,     new Color(0.50f,0.36f,0.22f), new Vector3(0.4f,2.6f,0.15f),"Oar"),
                _                           => (MeshBuilder.Shape.Cube,     Color.magenta,                 Vector3.one,                 "Item"),
            };

            var root = new GameObject(spec.name);
            Mesh(spec.prim, root.transform, spec.col, spec.size, Vector3.zero);
            return root;
        }

        // ════════════════════════════════════════════════════════════════════
        //  RESOURCES
        // ════════════════════════════════════════════════════════════════════

        public static GameObject BuildResource(int resType)
        {
            var root = new GameObject(ResName(resType));

            switch (resType)
            {
                case GameConstants.RES_BARE_TREE:
                    Trunk(root, 10f, new Color(0.40f,0.28f,0.16f));
                    break;
                case GameConstants.RES_PALM_TREE:
                    Trunk(root, 13f, new Color(0.45f,0.32f,0.18f));
                    Canopy(root, 13f, new Color(0.25f,0.50f,0.22f), wide:true);
                    break;
                case GameConstants.RES_TREE:
                    Trunk(root, 11f, new Color(0.38f,0.26f,0.15f));
                    Canopy(root, 11f, new Color(0.20f,0.45f,0.18f), wide:false);
                    break;
                case GameConstants.RES_FRUIT_TREE:
                    Trunk(root, 10f, new Color(0.40f,0.28f,0.16f));
                    Canopy(root, 10f, new Color(0.24f,0.48f,0.20f), wide:false);
                    Fruits(root, 10f);
                    break;
                case GameConstants.RES_BUSH:
                    Canopy(root, 1.5f, new Color(0.22f,0.42f,0.18f), wide:true);
                    break;
                case GameConstants.RES_FRUIT_BUSH:
                    Canopy(root, 1.5f, new Color(0.24f,0.44f,0.20f), wide:true);
                    Fruits(root, 2.5f);
                    break;
                case GameConstants.RES_PLANT:
                    Mesh(MeshBuilder.Shape.Capsule, root.transform, new Color(0.30f,0.55f,0.22f),
                         new Vector3(1.5f,1.2f,1.5f), new Vector3(0f,1.2f,0f));
                    break;
                case GameConstants.RES_ROCK:
                    Mesh(MeshBuilder.Shape.Sphere, root.transform, new Color(0.52f,0.52f,0.55f),
                         new Vector3(4f,3f,4f), new Vector3(0f,1.2f,0f));
                    break;
                default:
                    Trunk(root, 10f, new Color(0.40f,0.28f,0.16f));
                    break;
            }

            return root;
        }

        private static void Trunk(GameObject root, float height, Color c)
            => Mesh(MeshBuilder.Shape.Cylinder, root.transform, c,
                    new Vector3(1.4f, height * 0.5f, 1.4f), new Vector3(0f, height * 0.5f, 0f));

        private static void Canopy(GameObject root, float atHeight, Color c, bool wide)
            => Mesh(MeshBuilder.Shape.Sphere, root.transform, c,
                    wide ? new Vector3(10f, 5f, 10f) : new Vector3(7f, 7f, 7f),
                    new Vector3(0f, atHeight, 0f));

        private static void Fruits(GameObject root, float atHeight)
        {
            for (int i = 0; i < 4; i++)
            {
                float a = i * Mathf.PI * 0.5f;
                Mesh(MeshBuilder.Shape.Sphere, root.transform, new Color(0.85f,0.45f,0.12f),
                     new Vector3(0.8f,0.8f,0.8f),
                     new Vector3(Mathf.Cos(a)*2.5f, atHeight, Mathf.Sin(a)*2.5f));
            }
        }

        // ════════════════════════════════════════════════════════════════════
        //  LOW-LEVEL HELPERS
        // ════════════════════════════════════════════════════════════════════

        private static Transform Bone(string name, Transform parent, Vector3 localPos)
        {
            var go = new GameObject(name);
            go.transform.SetParent(parent, false);
            go.transform.localPosition = localPos;
            return go.transform;
        }

        private static void Mesh(MeshBuilder.Shape shape, Transform parent, Color color,
                                 Vector3 scale, Vector3 localPos, Vector3 localEuler = default)
        {
            var go = new GameObject(parent.name + "_mesh");
            go.transform.SetParent(parent, false);
            go.transform.localPosition = localPos;
            go.transform.localEulerAngles = localEuler;
            go.transform.localScale = scale;

            var mf = go.AddComponent<MeshFilter>();
            mf.sharedMesh = MeshBuilder.Get(shape);

            var rend = go.AddComponent<MeshRenderer>();
            rend.material = new Material(ShaderUtil.Lit);
            rend.material.color = color;
        }

        private static string ResName(int resType) => resType switch
        {
            GameConstants.RES_BARE_TREE  => "BareTree",
            GameConstants.RES_PALM_TREE  => "PalmTree",
            GameConstants.RES_TREE       => "Tree",
            GameConstants.RES_FRUIT_TREE => "FruitTree",
            GameConstants.RES_BUSH       => "Bush",
            GameConstants.RES_FRUIT_BUSH => "FruitBush",
            GameConstants.RES_PLANT      => "Plant",
            GameConstants.RES_ROCK       => "Rock",
            _                            => "Resource",
        };
    }
}
