package pl.psi.warmachines;

import pl.psi.creatures.Creature;
import java.util.Random;

public class MedicTentCreature extends WarMachineDecorator {

    private final Random random = new Random();

    public MedicTentCreature(final Creature aDecorated) {
        super(aDecorated);
    }

    public void heal(Creature aTarget, double factor) {
        if (aTarget != null && aTarget.isAlive()) {
            int baseHeal = 10 + random.nextInt(11); // Losowanie 10-20 HP
            int totalHeal = (int) (baseHeal * factor);

            int missingHp = aTarget.getMaxHp() - aTarget.getCurrentHp();
            int pointsToHeal = Math.min(totalHeal, missingHp);

            if (pointsToHeal > 0) {
                // Przekazanie wartości ujemnej do applyDamage symuluje leczenie bez naruszania hermetyzacji
                aTarget.applyDamage(this, -pointsToHeal);
            }
        }
    }

    public void resurrect(Creature aTarget, int hpPool) {
        // Miejsce na przyszłą logikę poziomu 3 umiejętności
    }
}