package pl.psi;

public interface SpellCastingState {
    boolean canCast();
    SpellCastingState nextStateOnCast();
    SpellCastingState nextStateOnRoundEnd();
}
