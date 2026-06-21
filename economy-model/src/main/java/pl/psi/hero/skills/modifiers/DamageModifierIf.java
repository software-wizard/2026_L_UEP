package pl.psi.hero.skills.modifiers;

public interface DamageModifierIf {
    int changeDamage(int currentDamage);

    float getDamageBonusFactor();
}
