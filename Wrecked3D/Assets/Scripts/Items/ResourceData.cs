using UnityEngine;

namespace Wrecked
{
    /// <summary>
    /// Immutable stats for all resource types (trees, rocks, bushes).
    /// Translated from resName/resSound/resWidth/resHeight/resStrength/
    /// resVines/resWood/resStone/resFruit in Values.bb.
    /// </summary>
    [CreateAssetMenu(fileName = "ResourceData", menuName = "Wrecked/ResourceData")]
    public class ResourceData : ScriptableObject
    {
        [System.Serializable]
        public struct TypeStats
        {
            public int    TypeID;
            public string TypeName;
            public float  Width;
            public float  Height;
            public int    Strength;    // base HP
            public int    YieldsVines;
            public int    YieldsWood;
            public int    YieldsStone;
            public int    YieldsFruit;
            public bool   IsFlammable;
        }

        public TypeStats[] Types;

        private static TypeStats[] _table;

        public void BakeTable()
        {
            _table = new TypeStats[9];
            foreach (var t in Types)
                if (t.TypeID >= 0 && t.TypeID < _table.Length)
                    _table[t.TypeID] = t;
        }

        public static TypeStats Get(int typeID)
        {
            if (_table == null || typeID < 0 || typeID >= _table.Length)
                return new TypeStats { Strength = 3, Width = 5f, Height = 20f };
            return _table[typeID];
        }

        // Canonical defaults from Values.bb
        public static readonly TypeStats[] Defaults =
        {
            new TypeStats { TypeID=GameConstants.RES_BARE_TREE,  TypeName="Dead Tree",   Width=5f,  Height=30f, Strength=3, YieldsWood=2, IsFlammable=true  },
            new TypeStats { TypeID=GameConstants.RES_PALM_TREE,  TypeName="Palm Tree",   Width=6f,  Height=35f, Strength=4, YieldsWood=2, IsFlammable=true  },
            new TypeStats { TypeID=GameConstants.RES_TREE,       TypeName="Tree",        Width=6f,  Height=32f, Strength=5, YieldsWood=3, YieldsVines=1, IsFlammable=true },
            new TypeStats { TypeID=GameConstants.RES_FRUIT_TREE, TypeName="Fruit Tree",  Width=6f,  Height=32f, Strength=4, YieldsWood=2, YieldsFruit=3, IsFlammable=true },
            new TypeStats { TypeID=GameConstants.RES_BUSH,       TypeName="Bush",        Width=4f,  Height=8f,  Strength=2, YieldsVines=2, IsFlammable=true },
            new TypeStats { TypeID=GameConstants.RES_FRUIT_BUSH, TypeName="Fruit Bush",  Width=4f,  Height=8f,  Strength=2, YieldsVines=1, YieldsFruit=2, IsFlammable=true },
            new TypeStats { TypeID=GameConstants.RES_PLANT,      TypeName="Plant",       Width=3f,  Height=6f,  Strength=1, YieldsVines=1, IsFlammable=false },
            new TypeStats { TypeID=GameConstants.RES_ROCK,       TypeName="Rock",        Width=8f,  Height=10f, Strength=6, YieldsStone=3, IsFlammable=false },
        };
    }
}
