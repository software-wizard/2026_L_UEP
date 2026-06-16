package pl.psi;

public class ReadyToCastState implements SpellCastingState {
    @Override
    public boolean canCast() {
        return true;
    }

    @Override
    public SpellCastingState nextStateOnCast() {
        return new AlreadyCastedState();
    }

    @Override
    public SpellCastingState nextStateOnRoundEnd() {
        return this; // Still ready
    }
}
