package pl.psi.warmachines;

import pl.psi.creatures.Creature;
import com.google.common.collect.Range;

public abstract class WarMachineDecorator extends Creature {

    private final Creature decorated;

    protected WarMachineDecorator(final Creature aDecorated) {
        // Nie wywołujemy pustego super(), tylko przekazujemy parametry wyżej jeśli to możliwe,
        // ale skoro rozszerzamy Creature, Java i tak wywoła konstruktor domyślny.
        super();
        this.decorated = aDecorated;
    }

    protected Creature getDecorated() {
        return decorated;
    }

    @Override
    public int getAmount() {
        return decorated.getAmount();
    }

    @Override
    public void setAmount(int aAmount) {
        decorated.setAmount(aAmount);
    }

    @Override
    public int getCurrentHp() {
        return decorated.getCurrentHp();
    }

    @Override
    public int getMaxHp() {
        return decorated.getMaxHp();
    }

    @Override
    public boolean isAlive() {
        return decorated.isAlive();
    }

    @Override
    public int getAttack() {
        return decorated.getAttack();
    }

    @Override
    public int getArmor() {
        return decorated.getArmor();
    }

    @Override
    public String getName() {
        return decorated.getName();
    }

    // Krytyczne dla maszyn wojennych: wymuszamy 0, aby maszyna nie mogła chodzić po hexach!
    @Override
    public int getMoveRange() {
        return 0;
    }

    @Override
    public void applyDamage(final Creature aDefender, final int aDamage) {
        decorated.applyDamage(aDefender, aDamage);
    }

    @Override
    public void attack(final Creature aDefender) {
        decorated.attack(aDefender);
    }

    @Override
    public String toString() {
        return getName();
    }
}