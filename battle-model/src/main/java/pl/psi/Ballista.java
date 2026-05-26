package pl.psi;

import pl.psi.creatures.Creature;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;

public class Ballista {

    private final Hero owner;

    public Ballista(Hero aOwner) {
        this.owner = aOwner;
    }

    public void attack(Creature aDefender) {
        int artilleryLevel = owner.getSkillLevel("ARTILLERY");

        int calculatedDamage = 20 * (owner.getAttack() + 1);

        aDefender.applyDamage(aDefender, calculatedDamage);

        if (artilleryLevel == 3) {
            if (aDefender.isAlive()) {
                aDefender.applyDamage(aDefender, calculatedDamage);
            }
        }
    }

    public void attack(List<Creature> aDefenders) {
        if (aDefenders == null || aDefenders.isEmpty()) {
            return;
        }

        List<Creature> aliveEnemies = new ArrayList<>();
        for (Creature c : aDefenders) {
            if (c.isAlive()) {
                aliveEnemies.add(c);
            }
        }

        if (!aliveEnemies.isEmpty()) {
            Collections.shuffle(aliveEnemies);
            Creature randomTarget = aliveEnemies.get(0);
            attack(randomTarget);
        }
    }
}