package pl.psi.creatures;

import com.google.common.collect.Range;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum CreatureStatistic implements CreatureStatisticIf
{
    // NECROPOLIS FRACTION
    // name, atk, armor, hp, move, damage, tier, desc, upgraded, movementType, ranged, doubleAttacker
    SKELETON( "Skeleton", 5, 4, 6, 4, Range.closed( 1, 3 ), 1,
            "Average lvl1 foot soldier.", false, MovementType.WALKING, false, false ),
    WALKING_DEAD( "Walking Dead", 5, 5, 15, 3, Range.closed( 2, 3 ), 2,
            "Basically its the same skeleton with more hit points.", false, MovementType.WALKING, false, false ),
    WIGHT( "Wight", 7, 7, 18, 5, Range.closed( 3, 5 ), 3,
            "Regenerating ability is really good when fighting weak enemies.",
            false, MovementType.WALKING, false, false ),
    VAMPIRE( "Vampire", 10, 9, 30, 6, Range.closed( 5, 8 ), 4,
            "Special: no enemy retaliation.", false, MovementType.FLYING, false, false ),
    LICH( "Lich", 13, 10, 30, 6, Range.closed( 11, 15 ), 5,
            "Special: death cloud range attack.", false, MovementType.WALKING, false, false ),
    BLACK_KNIGHT( "Black Knight", 16, 16, 120, 7, Range.closed( 15, 30 ), 6,
            "Special: 20% chance to curse enemy.", false, MovementType.WALKING, false, false ),
    BONE_DRAGON( "Bone Dragon", 17, 15, 150, 9, Range.closed( 25, 50 ), 7,
            "Special: -1 to enemy morale.", false, MovementType.FLYING, false, false ),
    SKELETON_WARRIOR( "Skeleton Warrior", 6, 6, 6, 5, Range.closed( 1, 3 ), 1,
            "Upgraded skeleton.", true, MovementType.WALKING, false, false ),
    ZOMBIE( "Zombie", 5, 5, 20, 4, Range.closed( 2, 3 ), 2,
            "Special: 20% chance to disease enemies.", true, MovementType.WALKING, false, false ),
    WRAITH( "Wraith", 7, 7, 18, 5, Range.closed( 3, 5 ), 3,
            "Special: regenerates lost hp each round.", true, MovementType.FLYING, false, false ),
    VAMPIRE_LORD( "Vampire Lord", 10, 10, 40, 9, Range.closed( 5, 8 ), 4,
            "Special: no retaliation; life drain.", true, MovementType.FLYING, false, false ),
    POWER_LICH( "Power Lich", 13, 10, 40, 7, Range.closed( 11, 15 ), 5,
            "Special: death cloud range attack.", true, MovementType.WALKING, false, false ),
    DREAD_KNIGHT( "Dread Knight", 18, 18, 120, 9, Range.closed( 15, 30 ), 6,
            "Special: 20% chance to curse; 20% double damage.", true, MovementType.WALKING, false, false ),
    GHOST_DRAGON( "Ghost Dragon", 19, 17, 200, 14, Range.closed( 25, 50 ), 7,
            "Special: -1 morale; 20% age enemy.", true, MovementType.FLYING, false, false ),

    // BASTION FRACTION
    CENTAUR( "Centaur", 5, 3, 8, 6, Range.closed( 2, 3 ), 1,
            "Good lvl1 unit, fast and durable.", false, MovementType.WALKING, false, false ),
    BATTLE_CENTAUR( "Battle_Centaur", 6, 3, 10, 8, Range.closed( 2, 3 ), 1,
            "Faster, more durable centaur.", true, MovementType.WALKING, false, false ),
    DWARF( "Dwarf", 6, 7, 20, 3, Range.closed( 2, 4 ), 2,
            "Very durable.", false, MovementType.WALKING, false, false ),
    DWARF_WARRIOR( "Dwarf_Warrior", 7, 7, 20, 5, Range.closed( 2, 4 ), 2,
            "Faster, more durable dwarf.", true, MovementType.WALKING, false, false ),
    ELF( "Elf", 9, 5, 15, 6, Range.closed( 3, 5 ), 3,
            "Ranged unit.", false, MovementType.WALKING, true, false ),
    HIGH_ELF( "High_Elf", 9, 5, 15, 7, Range.closed( 3, 5 ), 3,
            "Ranged, attacks twice.", true, MovementType.WALKING, true, true ),
    PEGASUS( "Pegasus", 9, 8, 30, 8, Range.closed( 5, 9 ), 4,
            "Flying unit.", false, MovementType.FLYING, false, false ),
    SILVER_PEGASUS( "Silver_Pegasus", 9, 10, 30, 12, Range.closed( 5, 9 ), 4,
            "Faster, more durable pegasus.", true, MovementType.FLYING, false, false ),
    TREEMAN( "Treeman", 9, 12, 55, 3, Range.closed( 10, 14 ), 5,
            "Special: roots enemy on hit.", false, MovementType.WALKING, false, false ),
    ENT( "Ent", 9, 12, 65, 4, Range.closed( 10, 14 ), 5,
            "Special: roots enemy on hit.", true, MovementType.WALKING, false, false ),
    UNICORN( "Unicorn", 15, 14, 90, 7, Range.closed( 5, 9 ), 6,
            "Very durable.", false, MovementType.WALKING, false, false ),
    BATTLE_UNICORN( "Battle_Unicorn", 15, 14, 110, 9, Range.closed( 5, 9 ), 6,
            "Faster, more durable unicorn.", true, MovementType.WALKING, false, false ),
    GREEN_DRAGON( "Green_Dragon", 18, 18, 180, 10, Range.closed( 40, 50 ), 7,
            "Flying dragon.", false, MovementType.FLYING, false, false ),
    GOLD_DRAGON( "Gold_Dragon", 27, 27, 250, 16, Range.closed( 40, 50 ), 7,
            "Faster, more durable dragon.", true, MovementType.FLYING, false, false );

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
    private final Range<Integer> damage;
    @Getter
    private final int tier;
    @Getter
    private final String description;
    @Getter
    private final boolean isUpgraded;
    @Getter
    private final MovementType movementType;
    @Getter
    private final boolean ranged;
    @Getter
    private final boolean doubleAttacker;

    CreatureStatistic( final String aName, final int aAttack, final int aArmor, final int aMaxHp,
                       final int aMoveRange, final Range<Integer> aDamage, final int aTier, final String aDescription,
                       final boolean aIsUpgraded, final MovementType aMovementType,
                       final boolean aRanged, final boolean aDoubleAttacker )
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
        movementType = aMovementType;
        ranged = aRanged;
        doubleAttacker = aDoubleAttacker;
    }

    String getTranslatedName()
    {
        return name;
    }
}