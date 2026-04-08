package pl.psi.creatures;

import com.google.common.collect.Range;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum CreatureStatistic implements CreatureStatisticIf
{
    // NECROPILIS FRACTION
    SKELETON( "Skeleton", 5, 4, 6, 4, Range.closed( 1, 3 ), 1,
        "Average lvl1 foot soldier, but always in huge numbers thanks to necromancy skill and skeleton transformer.",
        false ), //
    WALKING_DEAD( "Walking Dead", 5, 5, 15, 3, Range.closed( 2, 3 ), 2,
        "Basically its the same skeleton with more hit points. I prefer buying 2 skeletons instead.", false ), //
    WIGHT( "Wight", 7, 7, 18, 5, Range.closed( 3, 5 ), 3,
        "Regenerating ability is really good when fighting weak enemies, especially shooters.\nSpecial: top wight of the stack regenerates all lost damage in the beginning of each round",
        false ), //
    VAMPIRE( "Vampire", 10, 9, 30, 6, Range.closed( 5, 8 ), 4,
        "NOTHING compared to their upgraded brothers. Keep the population growing and recruit after the upgrade.\nSpecial: no enemy retaliation.",
        false ), //
    LICH( "Lich", 13, 10, 30, 6, Range.closed( 11, 15 ), 5,
        "Now they last longer and are able to do more damage! A must for good necropolis army.\nSpecial: death cloud range attack - damages living creatures on adjacent hexes to target.\n",
        false ), //
    BLACK_KNIGHT( "Black Knight", 16, 16, 120, 7, Range.closed( 15, 30 ), 6,
        "Awesome ground unit. As any undead it cannot be blinded, so your enemies will have to look out.\nSpecial: 20% chance to curse enemy.\n",
        false ), //
    BONE_DRAGON( "Bone Dragon", 17, 15, 150, 9, Range.closed( 25, 50 ), 7,
        "They are truly fearsome for enemies with low morale. Simply keeping them on battlefield scares enemies.\nSpecial: -1 to enemy morale.\n",
        false ), //
    SKELETON_WARRIOR( "Skeleton Warrior", 6, 6, 6, 5, Range.closed( 1, 3 ), 1,
        "Numerous skeletons become even better, but running back to town and upgrading is a problem... If there is no room in your army for ordinary skeletons, necromancy skill will resurrect skeleton warriors, but there will be less of them than normal skeletons, so it might be a good idea not to upgrade cursed temple at all.",
        true ), //
    ZOMBIE( "Zombie", 5, 5, 20, 4, Range.closed( 2, 3 ), 2,
        "Attack ratings are way too low... In my opinion, necropolis has the worst lvl2 creature.\nSpecial: 20% chance to disease enemies (-2Att -2Def for 3 rounds)\n",
        true ), //
    WRAITH( "Wraith", 7, 7, 18, 5, Range.closed( 3, 5 ), 3,
        "Regenerating ability is really good when fighting weak enemies, especially shooters.\nSpecial: top wight of the stack regenerates all lost damage in the beginning of each round\n",
        true ), //
    VAMPIRE_LORD( "Vampire Lord", 10, 10, 40, 9, Range.closed( 5, 8 ), 4,
        "My favorite necropolis unit. Use them as main striking unit and you might end up with no losses!\nSpecial: no enemy retaliation ; resurrects members of their own stack by restoring health equal to the amount of damage they do to living enemies.\n",
        true ), //
    POWER_LICH( "Power Lich", 13, 10, 40, 7, Range.closed( 11, 15 ), 5,
        "Now they last longer and are able to do more damage! A must for good necropolis army.\nSpecial: death cloud range attack - damages living creatures on adjacent hexes to target.\n",
        true ), //
    DREAD_KNIGHT( "Dread Knight", 18, 18, 120, 9, Range.closed( 15, 30 ), 6,
        "I think it's the best lvl6 unit in the game! Double damage ability puts Dread Knights above Naga Queens.\nSpecial: 20% chance to curse enemy ; 20% chance to do double damage.\n",
        true ), //
    GHOST_DRAGON( "Ghost Dragon", 19, 17, 200, 14, Range.closed( 25, 50 ), 7,
        "When situation seems hopeless, take a chance on the best enemy stack! If you'll get lucky, half their hit points will be gone instantly!! Ageing ability makes ghost dragons as dangerous as other lvl7 creatures.\nSpecial: -1 to enemy morale ; 20% chance to age enemy (halve hit points of all stack members).\n",
        true ),
    // Bastion FRACTION
    CENTAUR( "Centaur", 5, 3, 8, 6, Range.closed( 2, 3 ), 1,
        "Good lvl1 unit,fast and durable(for lvl1 unit).",false ), //
    BATTLE_CENTAUR( "Battle_Centaur", 6, 3, 10, 8, Range.closed( 2, 3 ), 1,
        "Faster ,more durable centaur.", true ), //
    DWARF( "Dwarf", 6, 7, 20, 3, Range.closed( 2, 4 ), 2,
        "Very durable.",false ), //
    DWARF_WARRIOR( "Dwarf_Warrior", 7, 7, 20, 5, Range.closed( 2, 4 ), 2,
        "Faster ,more durable centaur.", true ), //
    ELF( "Elf", 9, 5, 15, 6, Range.closed( 3, 5 ), 2,
        "Very durable.",false ), //
    HIGH_ELF( "High_Elf", 9, 5, 15, 7, Range.closed( 3, 5 ), 3,
        "Faster ,more durable centaur.", true ), //
    PEGASUS( "Pegasus", 9, 8, 30, 8, Range.closed( 5, 9 ), 4,
        "Very durable.",false ), //
    SILVER_PEGASUS( "Silver_Pegasus", 9, 10, 30, 12, Range.closed( 5, 9 ), 4,
        "Faster ,more durable centaur.", true ), //
    TREEMAN( "Treeman", 9, 12, 55, 3, Range.closed( 10, 14 ), 5,
        "Very durable.",false ), //
    ENT( "Ent", 9, 12, 65, 4, Range.closed( 10, 14 ), 5,
        "Faster ,more durable centaur.", true ), //
    UNICORN( "Unicorn", 15, 14, 90, 7, Range.closed( 5, 9 ), 6,
        "Very durable.",false ), //
    BATTLE_UNICORN( "Battle_Unicorn", 15, 14, 110, 9, Range.closed( 5, 9 ), 6,
        "Faster ,more durable centaur.", true ), //
    GREEN_DRAGON( "Green_Dragon", 18, 18, 180, 10, Range.closed( 40, 50 ), 7,
        "Very durable.",false ), //
    GOLD_DRAGON( "Gold_Dragon", 27, 27, 250, 16, Range.closed( 40, 50 ), 7,
        "Faster ,more durable centaur.", true );






    @Getter
    private final String name;
    @Getter
    @Setter
    private int attack;
    @Getter
    @Setter
    private int armor;
    @Getter
    @Setter
    private int maxHp;
    @Getter
    @Setter
    private int moveRange;
    @Getter
    private final Range< Integer > damage;
    @Getter
    private final int tier;
    @Getter
    private final String description;
    @Getter
    private final boolean isUpgraded;

    CreatureStatistic( final String aName, final int aAttack, final int aArmor, final int aMaxHp,
        final int aMoveRange, final Range< Integer > aDamage, final int aTier, final String aDescription,
        final boolean aIsUpgraded )
    {
        name = aName;
        attack = aAttack;
        armor = aArmor;
        maxHp = aMaxHp;
        moveRange = aMoveRange;
        damage = aDamage;
        tier = aTier;
        description = aDescription;
        isUpgraded = aIsUpgraded;
    }

    String getTranslatedName()
    {
        return name;
    }
}
