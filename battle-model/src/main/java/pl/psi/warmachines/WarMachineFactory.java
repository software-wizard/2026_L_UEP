package pl.psi.warmachines;

import pl.psi.creatures.Creature;

public class WarMachineFactory {

    public WarMachineDecorator create(final WarMachineType aType, final BallistaTargetStrategy aStrategy) {
        // Mapujemy bezpiecznie typ na odpowiadający mu enum statystyk
        WarMachineStats stats = WarMachineStats.valueOf(aType.name());

        // Konstruujemy czysty obiekt Creature za pomocą oryginalnego buildera
        final Creature baseCreature = new Creature.Builder()
                .statistic(stats)
                .amount(1)
                .build();

        switch (aType) {
            case BALLISTA:
                BallistaTargetStrategy strategy = aStrategy != null ? aStrategy : new RandomTargetStrategy();
                return new BallistaCreature(baseCreature, strategy);
            case MEDIC_TENT:
                return new MedicTentCreature(baseCreature);
            case AMMO_CART:
                return new AmmoCartCreature(baseCreature);
            default:
                throw new IllegalArgumentException("Unknown WarMachineType: " + aType);
        }
    }
}