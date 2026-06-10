package pl.psi;

import lombok.Getter;
import lombok.Setter;
import pl.psi.creatures.Creature;

@Getter
@Setter
public class QuicksandField extends SpecialField {
    private boolean revealed = false;

    public QuicksandField() {
        super(FieldName.QUICKSAND);
    }

    @Override
    public void doSomething(Creature aCreature) {
        if (shouldIgnore(aCreature)) {
            return;
        }
        this.revealed = true;
    }
}
