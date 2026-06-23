package pl.psi.warmachines;

import pl.psi.creatures.Creature;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class RandomTargetStrategy implements BallistaTargetStrategy {
    private final Random random = new Random();

    @Override
    public Optional<Creature> selectTarget(List<Creature> enemies, Object contextualData) {
        List<Creature> aliveEnemies = enemies.stream()
                .filter(Creature::isAlive)
                .collect(Collectors.toList());

        if (aliveEnemies.isEmpty()) {
            return Optional.empty();
        }

        int randomIndex = random.nextInt(aliveEnemies.size());
        return Optional.of(aliveEnemies.get(randomIndex));
    }
}