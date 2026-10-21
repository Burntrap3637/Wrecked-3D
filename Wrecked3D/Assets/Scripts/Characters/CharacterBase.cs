using UnityEngine;

namespace Wrecked
{
    /// <summary>
    /// Runtime state for one entity slot (human or animal).
    /// All arrays in the original BB code (pHealth, pHunger, etc.) become fields here.
    /// Managed by CharacterManager; not a MonoBehaviour — held as plain data.
    /// </summary>
    public class CharacterBase
    {
        // ── Identity ───────────────────────────────────────────────────────────
        public int   SlotIndex;    // 1-60
        public int   CharType;     // TYPE_* constant
        public int   Island;       // 0=inactive, 1-5=active island
        public int   State;        // 0=dead/inactive, 1=active

        // ── Transform ──────────────────────────────────────────────────────────
        public Vector3 Pos;
        public float   Angle;      // Y-rotation in degrees
        public float   Gravity;    // accumulated fall velocity

        // ── AI / control ───────────────────────────────────────────────────────
        public int     Control;    // 0=CPU, 1=P1, 2=P2, 3=player(story)
        public int     Agenda;     // AGENDA_* constant
        public int     Target;     // slot index of current target
        public Vector3 TargetPos;
        public float   TargetAngle;

        // ── Status ─────────────────────────────────────────────────────────────
        public int   Health;       // current health (0-500)
        public int   MaxHP;        // max HP (charHP * 5)
        public int   Hunger;       // 0-100; 0=starving
        public int   Thirst;       // 0-100; 0=dehydrated
        public int   DT;           // damage timer (immunity frames)

        // ── Carrying ───────────────────────────────────────────────────────────
        public int   CarryA;       // item slot in left hand  (0=empty)
        public int   CarryB;       // item slot in right hand (0=empty)

        // ── Animation ──────────────────────────────────────────────────────────
        public int   Anim;         // current ANIM_* state
        public int   AnimTimer;    // ticks remaining in current anim

        // ── Appearance ─────────────────────────────────────────────────────────
        public int   Costume;
        public int   Hair;
        public int   Face;
        public int   Body;
        public int   Arms;
        public int   Legs;

        // ── Scars (20 limb slots; 1-indexed) ──────────────────────────────────
        private int[] _scars = new int[21];

        public int  GetScar(int limb)              => _scars[limb];
        public void SetScar(int limb, int val)     { if (limb >= 1 && limb <= 20) _scars[limb] = val; }
        public void AddScar(int limb, int amount)  { if (limb >= 1 && limb <= 20) _scars[limb] = Mathf.Min(_scars[limb] + amount, 255); }

        // ── Scene object reference ─────────────────────────────────────────────
        public GameObject SceneObject;          // instantiated prefab
        public Animator   Animator;

        // ── Limb transforms (20 slots; 1-indexed) ─────────────────────────────
        private Transform[] _limbs = new Transform[21];

        public Transform GetLimb(int limb)          => _limbs[limb];
        public void      SetLimb(int limb, Transform t) { if (limb >= 1 && limb <= 20) _limbs[limb] = t; }

        // ── Head world position ────────────────────────────────────────────────
        public Vector3 HeadPosition()
        {
            if (_limbs[1] != null) return _limbs[1].position;
            return Pos + Vector3.up * 18f;
        }

        // ── Helpers ────────────────────────────────────────────────────────────

        public bool IsActive => State > 0 && Island > 0;
        public bool IsDead   => Health <= 0;
        public bool IsHuman  => CharType >= 1 && CharType <= 4;
        public bool IsAnimal => CharType >= 10;

        public bool IsHungry  => Hunger  <= 0;
        public bool IsThirsty => Thirst  <= 0;

        public float SpeedMultiplier
        {
            get
            {
                float s = 1f;
                if (IsHungry)  s *= 0.7f;
                if (IsThirsty) s *= 0.7f;
                return s;
            }
        }

        public void Reset()
        {
            State  = 0; Island = 0;
            Health = 0; Hunger = 0; Thirst = 0;
            CarryA = 0; CarryB = 0;
            Anim   = 0; AnimTimer = 0;
            for (int i = 1; i <= 20; i++) _scars[i] = 0;
        }
    }
}
