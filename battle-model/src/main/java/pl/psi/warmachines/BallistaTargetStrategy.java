package pl.psi.warmachines;

import pl.psi.creatures.Creature;
import java.util.List;
import java.util.Optional;

public interface BallistaTargetStrategy {
    Optional<Creature> selectTarget(List<Creature> enemies, Object contextualData);
}