package pl.psi.creatures;

import pl.psi.Spells.Spell;

import java.util.Random;

abstract class AbstractCalculateDamageStrategy implements DamageCalculatorIf
{

    public static final int MAX_ATTACK_DIFF = 60;
    public static final int MAX_DEFENCE_DIFF = 12;
    public static final double DEFENCE_BONUS = 0.025;
    public static final double ATTACK_BONUS = 0.05;
    protected final Random rand;

    protected AbstractCalculateDamageStrategy( final Random aRand )
    {
        rand = aRand;
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
        return (int)(aAttacker.getAmount() * oneCreatureDamageToDeal);
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

        // Formula: (Base + SpellPower * 10) reduced by armor
        // Every point of armor reduces magic damage by 2%, up to 80% reduction
        double armorReduction = Math.min(0.8, armor * 0.02);
        double totalDamage = (baseDamage + (aSpellPower * 10)) * (1 - armorReduction);

        return (int) Math.max(1, totalDamage);
    }

    protected int getArmor( final Creature aDefender )
    {
        return aDefender.getArmor();
    }


}

