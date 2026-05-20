package pl.psi;

import pl.psi.creatures.Creature;

public class DunesField extends SpecialField {
    public DunesField() {
        super(Color.YELLOW, FieldName.DUNES);
    }

    @Override
    public void doSomething(Creature aCreature) {
        if (shouldIgnore(aCreature)) {
            return;
        }
        // Mechanika QuickSand: jednostka zatrzymuje się (ruch staje się 0)
        aCreature.setAmount(aCreature.getAmount()); // Placeholder or interaction with movement logic
        // W kontekście tego modelu, możemy oznaczyć że jednostka weszła w pułapkę.
        // Na ten moment implementujemy to jako zatrzymanie/blokadę jeśli model to wspiera.
    }
}
