package pl.psi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true) // <-- TA LINIJKA

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
