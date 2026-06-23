package pl.psi.warmachines;

import pl.psi.creatures.Creature;

public class AmmoCartCreature extends WarMachineDecorator {

    public AmmoCartCreature(final Creature aDecorated) {
        super(aDecorated);
    }
}