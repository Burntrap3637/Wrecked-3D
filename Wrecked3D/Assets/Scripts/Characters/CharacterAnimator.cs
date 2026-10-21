using UnityEngine;

namespace Wrecked
{
    /// <summary>
    /// Bridges the integer Anim state on CharacterBase to an Animator state machine.
    /// Each character's scene object needs an Animator with the parameters below.
    /// </summary>
    [RequireComponent(typeof(Animator))]
    public class CharacterAnimator : MonoBehaviour
    {
        // Animator parameter hashes — set up matching parameters in the Animator Controller
        private static readonly int HashAnim   = Animator.StringToHash("Anim");
        private static readonly int HashSpeed  = Animator.StringToHash("Speed");
        private static readonly int HashDead   = Animator.StringToHash("Dead");
        private static readonly int HashInWater= Animator.StringToHash("InWater");

        private Animator      _animator;
        private CharacterBase _character;

        public void Bind(CharacterBase character)
        {
            _animator  = GetComponent<Animator>();
            _character = character;
        }

        private void Update()
        {
            if (_animator == null || _character == null) return;

            _animator.SetInteger(HashAnim,    _character.Anim);
            _animator.SetBool(HashDead,       _character.IsDead);

            // Drive locomotion blend from movement state
            bool moving = _character.Anim is GameConstants.ANIM_WALK or GameConstants.ANIM_RUN;
            float targetSpeed = moving ? (_character.Anim == GameConstants.ANIM_RUN ? 2f : 1f) : 0f;
            float current     = _animator.GetFloat(HashSpeed);
            _animator.SetFloat(HashSpeed, Mathf.MoveTowards(current, targetSpeed, Time.deltaTime * 5f));

            // Water swimming state
            bool inWater = _character.Pos.y <= GameConstants.SEA_LEVEL + 0.5f;
            _animator.SetBool(HashInWater, inWater);
        }

        /// <summary>
        /// Trigger a one-shot animation event (attacks, stumbles) from gameplay code.
        /// </summary>
        public void TriggerOnce(string triggerName)
        {
            _animator?.SetTrigger(triggerName);
        }
    }
}
