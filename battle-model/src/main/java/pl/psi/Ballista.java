package pl.psi;

import pl.psi.creatures.Creature;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;

public class Ballista {

    private final Hero owner;
    private static final int BASE_DAMAGE = 30;

    public Ballista(Hero aOwner) {
        this.owner = aOwner;
    }

    public void attack(Creature aDefender) {
        int artilleryLevel = owner.getSkillLevel("ARTILLERY");

        aDefender.applyDamage(aDefender, BASE_DAMAGE);

        if (artilleryLevel == 3) {
            if (aDefender.isAlive()) {
                aDefender.applyDamage(aDefender, BASE_DAMAGE);
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