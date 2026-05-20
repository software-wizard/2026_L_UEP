package pl.psi;

import java.util.HashMap;
import java.util.List;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Range;
import org.junit.jupiter.api.Disabled;
import pl.psi.creatures.Creature;
import pl.psi.creatures.CreatureStats;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

// @Disabled
public class SpecialFieldsTest {

    private static final Range<Integer> NOT_IMPORTANT_DMG = Range.closed(0, 0);

    @Test
    void isFieldGivingDmg() {
        final Creature creature = new Creature.Builder().statistic(CreatureStats.builder()
                        .maxHp(100)
                        .damage(NOT_IMPORTANT_DMG)
                        .attack(0)
                        .moveRange(5)
                        .armor(10)
                        .build())
                .build();

        final Creature dragon = new Creature.Builder().statistic(CreatureStats.builder()
                        .maxHp(100)
                        .damage(NOT_IMPORTANT_DMG)
                        .attack(0)
                        .moveRange(5)
                        .armor(10)
                        .build())
                .build();

        final List< Creature > c1 = List.of( creature, dragon );
        final List< Creature > c2 = List.of();
        final BiMap <BattlePoint, SpecialField > specialFields = HashBiMap.create();
        specialFields.put(new BattlePoint(3, 3), new DmgField());
        specialFields.put(new BattlePoint(4, 4), new DmgField());
        final Board board = new Board( c1, c2,  specialFields, new HashMap<>());

        //when
        board.move( creature, new BattlePoint( 3, 3 ) );
        board.move( dragon, new BattlePoint(4, 4));

        //then
        assertThat(creature.getCurrentHp()).isEqualTo(80);
        assertThat(dragon.getCurrentHp()).isEqualTo(80);
    }

//    @Test
//    void fieldCanOnlyBeFlown() {
//        final Creature creature = new Creature.Builder().statistic(CreatureStats.builder()
//                        .maxHp(100)
//                        .damage(NOT_IMPORTANT_DMG)
//                        .attack(0)
//                        .name("Knight")
//                        .moveRange(5)
//                        .armor(10)
//                        .build())
//                .build();
//
//        final Creature dragon = new Creature.Builder().statistic(CreatureStats.builder()
//                        .maxHp(100)
//                        .name("Ghost Dragon")
//                        .damage(NOT_IMPORTANT_DMG)
//                        .attack(0)
//                        .moveRange(5)
//                        .armor(10)
//                        .build())
//                .build();
//
//        final List< Creature > c1 = List.of( creature, dragon );
//        final List< Creature > c2 = List.of();
//        final BiMap <BattlePoint, SpecialField > specialFields = HashBiMap.create();
//        specialFields.put(new BattlePoint(3, 3), new FieldCanOnlyBeFlown());
//        final Board board = new Board( c1, c2,  specialFields, new HashMap<>());
//        final BattlePoint initialCreaturePoint = creature.getCurrentCreaturePoint();
//
//        //when
//        board.move( creature, new BattlePoint( 3, 3 ) );
//        board.move( dragon, new BattlePoint(3, 3));
//
//        //then
//        assertThat(creature.getCurrentCreaturePoint()).isEqualTo(initialCreaturePoint);
//        assertThat(dragon.getCurrentCreaturePoint()).isEqualTo(new BattlePoint(3,3));
//    }

    @Test
    void holyGroundFieldTest(){
        final Creature goodCreature = new Creature.Builder().statistic(CreatureStats.builder()
                        .name("Test").maxHp(100).moveRange(5).build()).build();
        goodCreature.setFaction(pl.psi.creatures.Faction.GOOD);
        goodCreature.setMorale(0);

        final Creature evilCreature = new Creature.Builder().statistic(CreatureStats.builder()
                        .name("Test").maxHp(100).moveRange(5).build()).build();
        evilCreature.setFaction(pl.psi.creatures.Faction.EVIL);
        evilCreature.setMorale(0);

        final List< Creature > c1 = List.of( goodCreature, evilCreature);
        final BiMap <BattlePoint, SpecialField > specialFields = HashBiMap.create();
        specialFields.put(new BattlePoint(3, 3), new HolyGroundField());
        specialFields.put(new BattlePoint(4, 4), new HolyGroundField());
        final Board board = new Board( c1, List.of(),  specialFields, new HashMap<>());

        board.move(goodCreature, new BattlePoint(3, 3));
        board.move(evilCreature, new BattlePoint(4, 4));

        assertThat(goodCreature.getMorale()).isEqualTo(1);
        assertThat(evilCreature.getMorale()).isEqualTo(-1);
    }

    @Test
    void evilFogFieldTest(){
        final Creature goodCreature = new Creature.Builder().statistic(CreatureStats.builder()
                        .name("Test").maxHp(100).moveRange(5).build()).build();
        goodCreature.setFaction(pl.psi.creatures.Faction.GOOD);
        goodCreature.setMorale(0);

        final Creature evilCreature = new Creature.Builder().statistic(CreatureStats.builder()
                        .name("Test").maxHp(100).moveRange(5).build()).build();
        evilCreature.setFaction(pl.psi.creatures.Faction.EVIL);
        evilCreature.setMorale(0);

        final List< Creature > c1 = List.of( goodCreature, evilCreature);
        final BiMap <BattlePoint, SpecialField > specialFields = HashBiMap.create();
        specialFields.put(new BattlePoint(3, 3), new EvilFogField());
        specialFields.put(new BattlePoint(4, 4), new EvilFogField());
        final Board board = new Board( c1, List.of(),  specialFields, new HashMap<>());

        board.move(goodCreature, new BattlePoint(3, 3));
        board.move(evilCreature, new BattlePoint(4, 4));

        assertThat(goodCreature.getMorale()).isEqualTo(-1);
        assertThat(evilCreature.getMorale()).isEqualTo(1);
    }

    @Test
    void cloverFieldTest(){
        final Creature neutralCreature = new Creature.Builder().statistic(CreatureStats.builder()
                        .name("Test").maxHp(100).moveRange(5).build()).build();
        neutralCreature.setFaction(pl.psi.creatures.Faction.NEUTRAL);
        neutralCreature.setLuck(0);

        final Creature goodCreature = new Creature.Builder().statistic(CreatureStats.builder()
                        .name("Test").maxHp(100).moveRange(5).build()).build();
        goodCreature.setFaction(pl.psi.creatures.Faction.GOOD);
        goodCreature.setLuck(0);

        final List< Creature > c1 = List.of( neutralCreature, goodCreature);
        final BiMap <BattlePoint, SpecialField > specialFields = HashBiMap.create();
        specialFields.put(new BattlePoint(3, 3), new CloverField());
        specialFields.put(new BattlePoint(4, 4), new CloverField());
        final Board board = new Board( c1, List.of(),  specialFields, new HashMap<>());

        board.move(neutralCreature, new BattlePoint(3, 3));
        board.move(goodCreature, new BattlePoint(4, 4));

        assertThat(neutralCreature.getLuck()).isEqualTo(2);
        assertThat(goodCreature.getLuck()).isEqualTo(0);
    }

    @Test
    void flyingUnitsBypassSpecialFieldsTest(){
        final Creature ghostDragon = new Creature.Builder().statistic(CreatureStats.builder()
                        .name("Ghost Dragon").maxHp(100).moveRange(5).build()).build();
        ghostDragon.setFaction(pl.psi.creatures.Faction.GOOD);
        ghostDragon.setMorale(0);
        ghostDragon.setLuck(0);
        ghostDragon.setMovementCostModifier(1.0);

        final List< Creature > c1 = List.of( ghostDragon );
        final BiMap <BattlePoint, SpecialField > specialFields = HashBiMap.create();
        specialFields.put(new BattlePoint(1, 1), new HolyGroundField());
        specialFields.put(new BattlePoint(2, 2), new EvilFogField());
        specialFields.put(new BattlePoint(3, 3), new CloverField());
        specialFields.put(new BattlePoint(4, 4), new FavorableWindsField());
        final Board board = new Board( c1, List.of(),  specialFields, new HashMap<>());

        board.move(ghostDragon, new BattlePoint(1, 1));
        assertThat(ghostDragon.getMorale()).isEqualTo(0);

        board.move(ghostDragon, new BattlePoint(2, 2));
        assertThat(ghostDragon.getMorale()).isEqualTo(0);

        ghostDragon.setFaction(pl.psi.creatures.Faction.NEUTRAL); // For clover field
        board.move(ghostDragon, new BattlePoint(3, 3));
        assertThat(ghostDragon.getLuck()).isEqualTo(0);

        board.move(ghostDragon, new BattlePoint(4, 4));
        assertThat(ghostDragon.getMovementCostModifier()).isEqualTo(1.0);
    }

    @Test
    void magicTerrainFieldsTest(){
        final Creature walker = new Creature.Builder().statistic(CreatureStats.builder()
                        .name("Test Walker").maxHp(100).moveRange(5).build()).build();
        walker.setEarthMagicLevel(pl.psi.creatures.MagicLevel.NONE);
        walker.setFireMagicLevel(pl.psi.creatures.MagicLevel.NONE);
        walker.setWaterMagicLevel(pl.psi.creatures.MagicLevel.NONE);
        walker.setAirMagicLevel(pl.psi.creatures.MagicLevel.NONE);

        final List< Creature > c1 = List.of( walker );
        final BiMap <BattlePoint, SpecialField > specialFields = HashBiMap.create();
        specialFields.put(new BattlePoint(1, 1), new RocklandField());
        specialFields.put(new BattlePoint(2, 2), new FieryFieldsField());
        specialFields.put(new BattlePoint(3, 3), new LucidPoolsField());
        specialFields.put(new BattlePoint(4, 4), new MagicCloudsField());
        final Board board = new Board( c1, List.of(),  specialFields, new HashMap<>());

        board.move(walker, new BattlePoint(1, 1));
        assertThat(walker.getEarthMagicLevel()).isEqualTo(pl.psi.creatures.MagicLevel.EXPERT);

        board.move(walker, new BattlePoint(2, 2));
        assertThat(walker.getFireMagicLevel()).isEqualTo(pl.psi.creatures.MagicLevel.EXPERT);

        board.move(walker, new BattlePoint(3, 3));
        assertThat(walker.getWaterMagicLevel()).isEqualTo(pl.psi.creatures.MagicLevel.EXPERT);

        board.move(walker, new BattlePoint(4, 4));
        assertThat(walker.getAirMagicLevel()).isEqualTo(pl.psi.creatures.MagicLevel.EXPERT);
    }

    @Test
    void flyingUnitsBypassMagicTerrainFieldsTest(){
        final Creature ghostDragon = new Creature.Builder().statistic(CreatureStats.builder()
                        .name("Ghost Dragon").maxHp(100).moveRange(5).build()).build();
        ghostDragon.setEarthMagicLevel(pl.psi.creatures.MagicLevel.NONE);
        ghostDragon.setFireMagicLevel(pl.psi.creatures.MagicLevel.NONE);
        ghostDragon.setWaterMagicLevel(pl.psi.creatures.MagicLevel.NONE);
        ghostDragon.setAirMagicLevel(pl.psi.creatures.MagicLevel.NONE);

        final List< Creature > c1 = List.of( ghostDragon );
        final BiMap <BattlePoint, SpecialField > specialFields = HashBiMap.create();
        specialFields.put(new BattlePoint(1, 1), new RocklandField());
        specialFields.put(new BattlePoint(2, 2), new FieryFieldsField());
        specialFields.put(new BattlePoint(3, 3), new LucidPoolsField());
        specialFields.put(new BattlePoint(4, 4), new MagicCloudsField());
        final Board board = new Board( c1, List.of(),  specialFields, new HashMap<>());

        board.move(ghostDragon, new BattlePoint(1, 1));
        assertThat(ghostDragon.getEarthMagicLevel()).isEqualTo(pl.psi.creatures.MagicLevel.NONE);

        board.move(ghostDragon, new BattlePoint(2, 2));
        assertThat(ghostDragon.getFireMagicLevel()).isEqualTo(pl.psi.creatures.MagicLevel.NONE);

        board.move(ghostDragon, new BattlePoint(3, 3));
        assertThat(ghostDragon.getWaterMagicLevel()).isEqualTo(pl.psi.creatures.MagicLevel.NONE);

        board.move(ghostDragon, new BattlePoint(4, 4));
        assertThat(ghostDragon.getAirMagicLevel()).isEqualTo(pl.psi.creatures.MagicLevel.NONE);
    }

    @Test
    void favorableWindsFieldTest(){
        final Creature creature = new Creature.Builder().statistic(CreatureStats.builder()
                        .name("Test").maxHp(100).moveRange(5).build()).build();
        creature.setMovementCostModifier(1.0);

        final List< Creature > c1 = List.of( creature );
        final BiMap <BattlePoint, SpecialField > specialFields = HashBiMap.create();
        specialFields.put(new BattlePoint(3, 3), new FavorableWindsField());
        final Board board = new Board( c1, List.of(),  specialFields, new HashMap<>());

        board.move(creature, new BattlePoint(3, 3));

        assertThat(creature.getMovementCostModifier()).isCloseTo(0.66, org.assertj.core.data.Offset.offset(0.01));
    }

    @Test
    void buffFieldTest(){
        final Creature creature1 = new Creature.Builder().statistic(CreatureStats.builder()
                        .maxHp(100)
                        .moveRange(5)
                        .damage(Range.closed(10, 10))
                        .attack(50)
                        .armor(0)
                        .build())
                .build();

        final Creature dragon = new Creature.Builder().statistic(CreatureStats.builder()
                        .maxHp(100)
                        .moveRange(5)
                        .damage(Range.closed(10, 10))
                        .attack(50)
                        .armor(0)
                        .build())
                .build();

        final List< Creature > c1 = List.of( creature1, dragon);
        final List< Creature > c2 = List.of();
        final BiMap <BattlePoint, SpecialField > specialFields = HashBiMap.create();
        specialFields.put(new BattlePoint(3, 3), new BuffField());
        final Board board = new Board( c1, c2,  specialFields, new HashMap<>());

        CreatureStats oldCreature1Stats = (CreatureStats) creature1.getStats();
        CreatureStats oldDragonStats = (CreatureStats) creature1.getStats();

        //When
        board.move(creature1, new BattlePoint(3, 3));
        board.move(dragon, new BattlePoint(4, 4));


        //Then
        assertThat(creature1.getAttack()).isEqualTo(oldCreature1Stats.getAttack() + 5);
        assertThat(creature1.getArmor()).isEqualTo(oldCreature1Stats.getArmor() + 10);
        assertThat(creature1.getMaxHp()).isEqualTo(oldCreature1Stats.getMaxHp() + 20);
        assertThat(creature1.getMoveRange()).isEqualTo(oldCreature1Stats.getMoveRange() + 1);
        assertThat(dragon.getAttack()).isEqualTo(oldDragonStats.getAttack());
        assertThat(dragon.getArmor()).isEqualTo(oldDragonStats.getArmor());
        assertThat(dragon.getMaxHp()).isEqualTo(oldDragonStats.getMaxHp());
        assertThat(dragon.getMoveRange()).isEqualTo(oldDragonStats.getMoveRange());
    }

    @Test
    void debuffFieldTest(){
        final Creature creature1 = new Creature.Builder().statistic(CreatureStats.builder()
                        .maxHp(100)
                        .moveRange(5)
                        .damage(Range.closed(10, 10))
                        .attack(50)
                        .armor(0)
                        .build())
                .build();

        final Creature dragon = new Creature.Builder().statistic(CreatureStats.builder()
                        .maxHp(100)
                        .moveRange(5)
                        .damage(Range.closed(10, 10))
                        .attack(50)
                        .armor(0)
                        .build())
                .build();

        final List< Creature > c1 = List.of( creature1, dragon);
        final List< Creature > c2 = List.of();
        final BiMap <BattlePoint, SpecialField > specialFields = HashBiMap.create();
        specialFields.put(new BattlePoint(3, 3), new DebuffField());
        final Board board = new Board( c1, c2,  specialFields, new HashMap<>());

        CreatureStats oldCreature1Stats = (CreatureStats) creature1.getStats();
        CreatureStats oldDragonStats = (CreatureStats) creature1.getStats();

        //When
        board.move(creature1, new BattlePoint(3, 3));
        board.move(dragon, new BattlePoint(4, 4));


        //Then
        assertThat(creature1.getAttack()).isEqualTo(oldCreature1Stats.getAttack() - 5);
        assertThat(creature1.getArmor()).isEqualTo(oldCreature1Stats.getArmor() - 10);
        assertThat(creature1.getMaxHp()).isEqualTo(oldCreature1Stats.getMaxHp() - 20);
        assertThat(creature1.getMoveRange()).isEqualTo(oldCreature1Stats.getMoveRange() - 1);
        assertThat(dragon.getAttack()).isEqualTo(oldDragonStats.getAttack());
        assertThat(dragon.getArmor()).isEqualTo(oldDragonStats.getArmor());
        assertThat(dragon.getMaxHp()).isEqualTo(oldDragonStats.getMaxHp());
        assertThat(dragon.getMoveRange()).isEqualTo(oldDragonStats.getMoveRange());
    }
}

