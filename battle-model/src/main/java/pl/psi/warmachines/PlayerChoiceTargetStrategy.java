package pl.psi.warmachines;

import pl.psi.creatures.Creature;
import java.util.List;
import java.util.Optional;

public class PlayerChoiceTargetStrategy implements BallistaTargetStrategy {

    @Override
    public Optional<Creature> selectTarget(List<Creature> enemies, Object contextualData) {
        if (contextualData instanceof Creature) {
            Creature targetedCreature = (Creature) contextualData;
            if (targetedCreature.isAlive() && enemies.contains(targetedCreature)) {
                return Optional.of(targetedCreature);
            }
        }
        return Optional.empty();
    }
}