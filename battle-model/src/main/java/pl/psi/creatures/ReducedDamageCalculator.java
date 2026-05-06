package pl.psi.creatures;

import pl.psi.Spells.Spell;

import java.util.Random;

public class ReducedDamageCalculator extends AbstractCalculateDamageStrategy{

    private final float reduceDamageFactor;
    private final float bonusAttackFactor;

    public ReducedDamageCalculator(float aReduceDamageFactor,float aBonusAttackFactor) {
        super(new Random());
        this.reduceDamageFactor = aReduceDamageFactor;
        this.bonusAttackFactor = aBonusAttackFactor;
    }

    @Override
    public int calculateDamage( final Creature aAttacker, final Creature aDefender )
    {
        final int armor = getArmor( aDefender );

        final int randValue = rand.nextInt( aAttacker.getDamage()
                .upperEndpoint()
                - aAttacker.getDamage()
                .lowerEndpoint()
                + 1 ) + aAttacker.getDamage()
                .lowerEndpoint();

        double oneCreatureDamageToDeal;
        if( aAttacker.getAttack() >= armor )
        {
            int attackPoints = aAttacker.getAttack() - armor;
            if( attackPoints > MAX_ATTACK_DIFF )
            {
                attackPoints = MAX_ATTACK_DIFF;
            }
            oneCreatureDamageToDeal = randValue * (1 + attackPoints * ATTACK_BONUS);
        }
        else
        {
            int defencePoints = armor - aAttacker.getAttack();
            if( defencePoints > MAX_DEFENCE_DIFF )
            {
                defencePoints = MAX_DEFENCE_DIFF;
            }
            oneCreatureDamageToDeal = randValue * (1 - defencePoints * DEFENCE_BONUS);
        }

        if( oneCreatureDamageToDeal < 0 )
        {
            oneCreatureDamageToDeal = 0;
        }
        return (int)(aAttacker.getAmount() * oneCreatureDamageToDeal * (1-reduceDamageFactor) * (1 + bonusAttackFactor));
    }

    @Override
    public int calculateMagicDamage(Creature aDefender, Spell aSpell, int aSpellPower) {
        final int armor = getArmor(aDefender);

        int baseDamage;

        switch (aSpell.getSpellLevel()) {
            case 1:
                baseDamage = 10;
                break;
            case 2:
                baseDamage = 20;
                break;
            case 3:
                baseDamage = 30;
                break;
            default:
                baseDamage = 10;
        }

        // Apply reduction/bonus factors from this calculator
        double totalDamage = (baseDamage + (aSpellPower * 10)) * (1 - reduceDamageFactor) * (1 + bonusAttackFactor);

        // Armor reduction (2% per point, max 80%)
        double armorReduction = Math.min(0.8, armor * 0.02);
        totalDamage *= (1 - armorReduction);

        return (int) Math.max(1, totalDamage);
    }

}
