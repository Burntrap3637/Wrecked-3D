using UnityEngine;

namespace Wrecked
{
    /// <summary>
    /// Immutable stats for all item types (weapons, tools, consumables).
    /// Translated from iName/iSound/iAnim/iWeight/iRange/iDamage/iFlammable in Values.bb.
    /// </summary>
    [CreateAssetMenu(fileName = "ItemData", menuName = "Wrecked/ItemData")]
    public class ItemData : ScriptableObject
    {
        [System.Serializable]
        public struct TypeStats
        {
            public int    TypeID;
            public string TypeName;
            public float  Weight;
            public float  Range;
            public int    Damage;
            public int    AnimID;     // attack animation to play
            public bool   Flammable;
            public bool   IsWeapon;
            public bool   IsFood;
        }

        public TypeStats[] Types;

        private static TypeStats[] _table;

        public void BakeTable()
        {
            _table = new TypeStats[21];
            foreach (var t in Types)
                if (t.TypeID >= 0 && t.TypeID < _table.Length)
                    _table[t.TypeID] = t;
        }

        public static TypeStats Get(int typeID)
        {
            if (_table == null || typeID < 0 || typeID >= _table.Length)
                return new TypeStats { Damage = 1, Range = 8f };
            return _table[typeID];
        }

        // Canonical defaults from Values.bb
        public static readonly TypeStats[] Defaults =
        {
            // Resources / consumables (1-5)
            new TypeStats { TypeID=GameConstants.ITEM_VINES, TypeName="Vines",   Weight=1f, Range=0f,  Damage=0, Flammable=true  },
            new TypeStats { TypeID=GameConstants.ITEM_WOOD,  TypeName="Wood",    Weight=2f, Range=0f,  Damage=0, Flammable=true  },
            new TypeStats { TypeID=GameConstants.ITEM_STONE, TypeName="Stone",   Weight=3f, Range=0f,  Damage=0, Flammable=false },
            new TypeStats { TypeID=GameConstants.ITEM_MEAT,  TypeName="Meat",    Weight=1f, Range=0f,  Damage=0, Flammable=true, IsFood=true },
            new TypeStats { TypeID=GameConstants.ITEM_FRUIT, TypeName="Fruit",   Weight=1f, Range=0f,  Damage=0, Flammable=false,IsFood=true },
            // Weapons / tools (10-20)
            new TypeStats { TypeID=GameConstants.ITEM_WHIP,  TypeName="Whip",   Weight=1f, Range=12f, Damage=3, Flammable=false, IsWeapon=true, AnimID=GameConstants.ANIM_ATK_HIGH_L },
            new TypeStats { TypeID=GameConstants.ITEM_STICK, TypeName="Stick",  Weight=2f, Range=10f, Damage=2, Flammable=true,  IsWeapon=true, AnimID=GameConstants.ANIM_ATK_HIGH_L },
            new TypeStats { TypeID=GameConstants.ITEM_ROCK,  TypeName="Rock",   Weight=3f, Range=8f,  Damage=4, Flammable=false, IsWeapon=true, AnimID=GameConstants.ANIM_ATK_GRD_L },
            new TypeStats { TypeID=GameConstants.ITEM_HAMMER,TypeName="Hammer", Weight=4f, Range=9f,  Damage=6, Flammable=false, IsWeapon=true, AnimID=GameConstants.ANIM_ATK_HIGH_L },
            new TypeStats { TypeID=GameConstants.ITEM_SPEAR, TypeName="Spear",  Weight=3f, Range=15f, Damage=7, Flammable=true,  IsWeapon=true, AnimID=GameConstants.ANIM_ATK_HIGH_L },
            new TypeStats { TypeID=GameConstants.ITEM_AXE,   TypeName="Axe",    Weight=4f, Range=10f, Damage=8, Flammable=false, IsWeapon=true, AnimID=GameConstants.ANIM_ATK_HIGH_L },
            new TypeStats { TypeID=GameConstants.ITEM_GUN,   TypeName="Gun",    Weight=3f, Range=50f, Damage=15,Flammable=false, IsWeapon=true, AnimID=GameConstants.ANIM_ATK_HIGH_R },
            new TypeStats { TypeID=GameConstants.ITEM_ARTEFACT,TypeName="Artefact",Weight=2f,Range=0f,Damage=0,Flammable=false },
            new TypeStats { TypeID=GameConstants.ITEM_BONE,  TypeName="Bone",   Weight=2f, Range=9f,  Damage=3, Flammable=false, IsWeapon=true, AnimID=GameConstants.ANIM_ATK_HIGH_L },
            new TypeStats { TypeID=GameConstants.ITEM_TRAP,  TypeName="Trap",   Weight=3f, Range=5f,  Damage=5, Flammable=false, IsWeapon=true },
            new TypeStats { TypeID=GameConstants.ITEM_OAR,   TypeName="Oar",    Weight=3f, Range=12f, Damage=4, Flammable=true,  IsWeapon=true, AnimID=GameConstants.ANIM_ATK_HIGH_L },
        };
    }
}
