package pl.psi;

public class AlreadyCastedState implements SpellCastingState {
    @Override
    public boolean canCast() {
        return false;
    }

    @Override
    public SpellCastingState nextStateOnCast() {
        return this;
    }

    @Override
    public SpellCastingState nextStateOnRoundEnd() {
        return new ReadyToCastState();
    }
}
