using UnityEngine;

namespace Wrecked
{
    /// <summary>
    /// Immutable stats table for each character type.
    /// Translated from Dim charName$()/charAttack()/charHP() etc. in Values.bb.
    /// Index matches TYPE_* constants in GameConstants.
    /// </summary>
    [CreateAssetMenu(fileName = "CharacterData", menuName = "Wrecked/CharacterData")]
    public class CharacterData : ScriptableObject
    {
        [System.Serializable]
        public struct TypeStats
        {
            public int   TypeID;
            public string TypeName;
            public int   Attack;     // base attack (charAttack)
            public int   HP;         // base hit-points multiplier  (charHP * 5 = max health)
            public float Speed;      // movement speed (charSpeed)
            public float Range;      // attack range   (charRange)
            public int   Power;      // knockback/stagger power (charPower)
            public int   MeatYield;  // meat dropped on death (charMeat)
            public float Width;      // collision radius (charWidth)
            public float Height;     // collision height (charHeight)
            public bool  IsAnimal;   // type >= 10
        }

        public TypeStats[] Types;

        // ── Static lookup (populated at runtime) ───────────────────────────────
        private static TypeStats[] _table;

        public void BakeTable()
        {
            _table = new TypeStats[21];   // indices 0-20
            foreach (var t in Types)
                if (t.TypeID >= 0 && t.TypeID < _table.Length)
                    _table[t.TypeID] = t;
        }

        public static TypeStats Get(int typeID)
        {
            if (_table == null || typeID < 0 || typeID >= _table.Length)
                return new TypeStats { HP = 10, Speed = 1f, Width = 5f, Height = 18f };
            return _table[typeID];
        }

        // ── Default stats matching Values.bb ──────────────────────────────────
        // (these are the canonical numbers from the original; populate via
        //  the ScriptableObject in the Unity editor, or override at runtime)

        public static readonly TypeStats[] Defaults =
        {
            // ── Humanoids ───────────────────────────────────────────────────
            new TypeStats { TypeID=GameConstants.TYPE_PLAYER,  TypeName="Human",   Attack=3, HP=10, Speed=1.5f, Range=10f, Power=3, MeatYield=3, Width=5f,  Height=18f },
            new TypeStats { TypeID=GameConstants.TYPE_SOLDIER, TypeName="Soldier", Attack=5, HP=12, Speed=1.6f, Range=12f, Power=4, MeatYield=3, Width=5f,  Height=18f },
            new TypeStats { TypeID=GameConstants.TYPE_MYSTIC,  TypeName="Mystic",  Attack=2, HP= 8, Speed=1.3f, Range=10f, Power=2, MeatYield=3, Width=5f,  Height=18f },
            new TypeStats { TypeID=GameConstants.TYPE_NATIVE,  TypeName="Native",  Attack=3, HP=10, Speed=1.5f, Range=10f, Power=3, MeatYield=3, Width=5f,  Height=18f },
            // ── Animals ──────────────────────────────────────────────────────
            new TypeStats { TypeID=GameConstants.TYPE_LEOPARD, TypeName="Leopard", Attack=6, HP=14, Speed=2.0f, Range=12f, Power=5, MeatYield=5, Width=7f,  Height=14f, IsAnimal=true },
            new TypeStats { TypeID=GameConstants.TYPE_WOLF,    TypeName="Wolf",    Attack=5, HP=12, Speed=1.9f, Range=10f, Power=4, MeatYield=4, Width=6f,  Height=14f, IsAnimal=true },
            new TypeStats { TypeID=GameConstants.TYPE_DEER,    TypeName="Deer",    Attack=1, HP= 8, Speed=2.2f, Range= 6f, Power=1, MeatYield=5, Width=6f,  Height=16f, IsAnimal=true },
            new TypeStats { TypeID=GameConstants.TYPE_RABBIT,  TypeName="Rabbit",  Attack=1, HP= 4, Speed=2.5f, Range= 4f, Power=1, MeatYield=2, Width=3f,  Height= 8f, IsAnimal=true },
            new TypeStats { TypeID=GameConstants.TYPE_FISH,    TypeName="Fish",    Attack=0, HP= 2, Speed=1.5f, Range= 3f, Power=0, MeatYield=1, Width=2f,  Height= 4f, IsAnimal=true },
        };
    }
}
