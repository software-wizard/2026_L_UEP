package pl.psi.warmachines;

import pl.psi.creatures.Creature;
import java.util.List;
import java.util.Optional;

public class BallistaCreature extends WarMachineDecorator {

    private final BallistaTargetStrategy targetStrategy;

    public BallistaCreature(final Creature aDecorated, final BallistaTargetStrategy aTargetStrategy) {
        super(aDecorated);
        this.targetStrategy = aTargetStrategy;
    }

    public Optional<Creature> selectTarget(List<Creature> enemies, Object contextualData) {
        return targetStrategy.selectTarget(enemies, contextualData);
    }

    public void shoot(Creature aDefender) {
        if (aDefender != null && aDefender.isAlive()) {
            int damage = getDecorated().getCalculator().calculateDamage(this, aDefender);
            // Wywołujemy applyDamage na celu, przekazując siebie jako atakującego i wyliczone obrażenia
            aDefender.applyDamage(this, damage);
        }
    }
}