package pl.psi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Range;
import pl.psi.creatures.Creature;
import pl.psi.creatures.CreatureStats;
import pl.psi.creatures.Faction;
import pl.psi.creatures.MagicLevel;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SpecialFieldsTest {

    private static final Range<Integer> NOT_IMPORTANT_DMG = Range.closed(0, 0);

    @Test
    void isFieldGivingDmg() {
        final Creature creature = new Creature.Builder().statistic(CreatureStats.builder()
                        .name("Knight")
                        .maxHp(100)
                        .damage(NOT_IMPORTANT_DMG)
                        .attack(0)
                        .moveRange(5)
                        .armor(10)
                        .build())
                .build();

        final Creature dragon = new Creature.Builder().statistic(CreatureStats.builder()
                        .name("Ghost Dragon")
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


        board.move( creature, new BattlePoint( 3, 3 ) );
        board.move( dragon, new BattlePoint(4, 4));

        assertThat(creature.getCurrentHp()).isEqualTo(80);
        assertThat(dragon.getCurrentHp()).isEqualTo(100);
    }

    @Test
    void holyGroundFieldTest(){
        final Creature goodCreature = new Creature.Builder().statistic(CreatureStats.builder()
                        .name("Good").maxHp(100).moveRange(5).build()).build();
        goodCreature.setFaction(Faction.GOOD);
        goodCreature.setMorale(0);

        final Creature evilCreature = new Creature.Builder().statistic(CreatureStats.builder()
                        .name("Evil").maxHp(100).moveRange(5).build()).build();
        evilCreature.setFaction(Faction.EVIL);
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
                        .name("Good").maxHp(100).moveRange(5).build()).build();
        goodCreature.setFaction(Faction.GOOD);
        goodCreature.setMorale(0);

        final Creature evilCreature = new Creature.Builder().statistic(CreatureStats.builder()
                        .name("Evil").maxHp(100).moveRange(5).build()).build();
        evilCreature.setFaction(Faction.EVIL);
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
                        .name("Neutral").maxHp(100).moveRange(5).build()).build();
        neutralCreature.setFaction(Faction.NEUTRAL);
        neutralCreature.setLuck(0);

        final Creature goodCreature = new Creature.Builder().statistic(CreatureStats.builder()
                        .name("Good").maxHp(100).moveRange(5).build()).build();
        goodCreature.setFaction(Faction.GOOD);
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
    void flyingUnitsBenefitFromPositiveFieldsButIgnoreNegativeTest(){
        final Creature ghostDragon = new Creature.Builder().statistic(CreatureStats.builder()
                        .name("Ghost Dragon").maxHp(100).moveRange(5).build()).build();
        ghostDragon.setFaction(Faction.GOOD);
        ghostDragon.setMorale(0);
        ghostDragon.setLuck(0);

        final List< Creature > c1 = List.of( ghostDragon );
        final BiMap <BattlePoint, SpecialField > specialFields = HashBiMap.create();
        specialFields.put(new BattlePoint(1, 1), new HolyGroundField());
        specialFields.put(new BattlePoint(2, 2), new DmgField());
        final Board board = new Board( c1, List.of(),  specialFields, new HashMap<>());

        board.move(ghostDragon, new BattlePoint(1, 1));
        assertThat(ghostDragon.getMorale()).isEqualTo(1);

        board.move(ghostDragon, new BattlePoint(2, 2));
        assertThat(ghostDragon.getCurrentHp()).isEqualTo(100);
    }

    @Test
    void magicTerrainFieldsTest(){
        final Creature walker = new Creature.Builder().statistic(CreatureStats.builder()
                        .name("Test Walker").maxHp(100).moveRange(5).build()).build();
        walker.setEarthMagicLevel(MagicLevel.NONE);
        walker.setFireMagicLevel(MagicLevel.NONE);
        walker.setWaterMagicLevel(MagicLevel.NONE);
        walker.setAirMagicLevel(MagicLevel.NONE);

        final List< Creature > c1 = List.of( walker );
        final BiMap <BattlePoint, SpecialField > specialFields = HashBiMap.create();
        specialFields.put(new BattlePoint(1, 1), new RocklandField());
        specialFields.put(new BattlePoint(2, 2), new FieryFieldsField());
        specialFields.put(new BattlePoint(3, 3), new LucidPoolsField());
        specialFields.put(new BattlePoint(4, 4), new MagicCloudsField());
        final Board board = new Board( c1, List.of(),  specialFields, new HashMap<>());

        board.move(walker, new BattlePoint(1, 1));
        assertThat(walker.getEarthMagicLevel()).isEqualTo(MagicLevel.EXPERT);

        board.move(walker, new BattlePoint(2, 2));
        assertThat(walker.getFireMagicLevel()).isEqualTo(MagicLevel.EXPERT);

        board.move(walker, new BattlePoint(3, 3));
        assertThat(walker.getWaterMagicLevel()).isEqualTo(MagicLevel.EXPERT);

        board.move(walker, new BattlePoint(4, 4));
        assertThat(walker.getAirMagicLevel()).isEqualTo(MagicLevel.EXPERT);
    }

    @Test
    void crackedIceFieldTest(){
        final Creature creature = new Creature.Builder().statistic(CreatureStats.builder()
                        .name("Knight")
                        .maxHp(100)
                        .moveRange(5)
                        .armor(10)
                        .build())
                .build();

        final List< Creature > c1 = List.of( creature );
        final BiMap <BattlePoint, SpecialField > specialFields = HashBiMap.create();
        specialFields.put(new BattlePoint(3, 3), new CrackedIceField());
        final Board board = new Board( c1, List.of(),  specialFields, new HashMap<>());

        board.move(creature, new BattlePoint(3, 3));

        assertThat(creature.getArmor()).isEqualTo(5);
    }

    @Test
    void fieldsOfGloryFieldTest() {
        final Creature creature = new Creature.Builder().statistic(CreatureStats.builder()
                        .name("Knight")
                        .maxHp(100)
                        .moveRange(5)
                        .build())
                .build();
        creature.setLuck(0);

        final List< Creature > c1 = List.of( creature );
        final BiMap <BattlePoint, SpecialField > specialFields = HashBiMap.create();
        specialFields.put(new BattlePoint(3, 3), new FieldsOfGloryField());
        final Board board = new Board( c1, List.of(),  specialFields, new HashMap<>());

        board.move(creature, new BattlePoint(3, 3));

        assertThat(creature.getLuck()).isEqualTo(-2);
    }

    @Test
    void buffFieldDurationTest() {
        final Creature creature = new Creature.Builder().statistic(CreatureStats.builder()
                        .name("Knight")
                        .maxHp(100)
                        .moveRange(5)
                        .attack(10)
                        .armor(10)
                        .damage(NOT_IMPORTANT_DMG)
                        .build())
                .build();

        final List<Creature> c1 = List.of(creature);
        final BiMap<BattlePoint, SpecialField> specialFields = HashBiMap.create();
        specialFields.put(new BattlePoint(3, 3), new BuffField());
        final Board board = new Board(c1, List.of(), specialFields, new HashMap<>());

        board.move(creature, new BattlePoint(3, 3));

        assertThat(creature.getAttack()).isEqualTo(15);

        creature.propertyChange(new java.beans.PropertyChangeEvent(this, "END_OF_TURN", 0, 1));
        assertThat(creature.getAttack()).isEqualTo(15);

        creature.propertyChange(new java.beans.PropertyChangeEvent(this, "END_OF_TURN", 1, 2));
        assertThat(creature.getAttack()).isEqualTo(15);

        creature.propertyChange(new java.beans.PropertyChangeEvent(this, "END_OF_TURN", 2, 3));
        assertThat(creature.getAttack()).isEqualTo(10);
    }

    @Test
    void debuffFieldDurationTest() {
        final Creature creature = new Creature.Builder().statistic(CreatureStats.builder()
                        .name("Knight")
                        .maxHp(100)
                        .moveRange(5)
                        .attack(10)
                        .armor(10)
                        .damage(NOT_IMPORTANT_DMG)
                        .build())
                .build();

        final List<Creature> c1 = List.of(creature);
        final BiMap<BattlePoint, SpecialField> specialFields = HashBiMap.create();
        specialFields.put(new BattlePoint(3, 3), new DebuffField());
        final Board board = new Board(c1, List.of(), specialFields, new HashMap<>());

        board.move(creature, new BattlePoint(3, 3));

        assertThat(creature.getAttack()).isEqualTo(5);

        creature.propertyChange(new java.beans.PropertyChangeEvent(this, "END_OF_TURN", 0, 1));
        assertThat(creature.getAttack()).isEqualTo(5);

        creature.propertyChange(new java.beans.PropertyChangeEvent(this, "END_OF_TURN", 1, 2));
        assertThat(creature.getAttack()).isEqualTo(5);

        creature.propertyChange(new java.beans.PropertyChangeEvent(this, "END_OF_TURN", 2, 3));
        assertThat(creature.getAttack()).isEqualTo(10);
    }

    @Test
    void fieldCanOnlyBeFlownTest() {
        final Creature walker = new Creature.Builder().statistic(CreatureStats.builder()
                        .name("Walker")
                        .maxHp(100)
                        .moveRange(5)
                        .damage(NOT_IMPORTANT_DMG)
                        .build())
                .build();

        final Creature flyer = new Creature.Builder().statistic(CreatureStats.builder()
                        .name("Ghost Dragon")
                        .maxHp(100)
                        .moveRange(5)
                        .damage(NOT_IMPORTANT_DMG)
                        .build())
                .build();

        final List<Creature> c1 = List.of(walker, flyer);
        final BiMap<BattlePoint, SpecialField> specialFields = HashBiMap.create();
        specialFields.put(new BattlePoint(1, 1), new FieldCanOnlyBeFlown());
        final Board board = new Board(c1, List.of(), specialFields, new HashMap<>());

        board.move(flyer, new BattlePoint(1, 1));
        assertThat(board.getPosition(flyer)).isEqualTo(new BattlePoint(1, 1));

        try {
            board.move(walker, new BattlePoint(1, 1));
        } catch (Exception e) {
            assertThat(e).isInstanceOf(pl.psi.Exceptions.CannotPassFieldException.class);
        }
    }

    @Test
    void quicksandFieldTest() {
        final Creature creature = new Creature.Builder().statistic(CreatureStats.builder()
                        .name("Walker")
                        .maxHp(100)
                        .moveRange(10)
                        .build())
                .build();

        final Creature flyingDragon = new Creature.Builder().statistic(CreatureStats.builder()
                        .name("Ghost Dragon")
                        .maxHp(100)
                        .moveRange(10)
                        .build())
                .build();

        // On a 15x15 board, default positions for list are (0, 1), (0, 3) etc.
        // We will set their positions manually via bankCreatures map to be precise.
        final List<Creature> emptyList = List.of();
        final Map<BattlePoint, Creature> bankCreatures = new HashMap<>();
        bankCreatures.put(new BattlePoint(0, 1), creature);
        bankCreatures.put(new BattlePoint(0, 3), flyingDragon);

        final QuicksandField quicksand1 = new QuicksandField();
        final QuicksandField quicksand2 = new QuicksandField();

        final BiMap<BattlePoint, SpecialField> specialFields = HashBiMap.create();
        specialFields.put(new BattlePoint(2, 1), quicksand1);
        specialFields.put(new BattlePoint(2, 3), quicksand2);

        final Board board = new Board(emptyList, emptyList, specialFields, bankCreatures);

        // Walker moves from (0,1) to (4,1), crossing quicksand at (2,1)
        board.move(creature, new BattlePoint(4, 1));
        // Walker should stop EXACTLY at (2,1) because of quicksand
        assertThat(board.getPosition(creature)).isEqualTo(new BattlePoint(2, 1));
        assertThat(quicksand1.isRevealed()).isTrue();

        // Flyer moves from (0,3) to (4,3), crossing quicksand at (2,3)
        board.move(flyingDragon, new BattlePoint(4, 3));
        // Flyer ignores quicksand and successfully reaches (4,3)
        assertThat(board.getPosition(flyingDragon)).isEqualTo(new BattlePoint(4, 3));
        assertThat(quicksand2.isRevealed()).isFalse();
    }

    @Test
    void magicPlainsFieldTest() {
        final Creature walker = new Creature.Builder().statistic(CreatureStats.builder()
                        .name("Test Walker").maxHp(100).moveRange(5).build()).build();
        walker.setEarthMagicLevel(MagicLevel.NONE);
        walker.setFireMagicLevel(MagicLevel.NONE);
        walker.setWaterMagicLevel(MagicLevel.NONE);
        walker.setAirMagicLevel(MagicLevel.NONE);

        final List<Creature> c1 = List.of(walker);
        final BiMap<BattlePoint, SpecialField> specialFields = HashBiMap.create();
        specialFields.put(new BattlePoint(1, 1), new MagicPlainsField());
        final Board board = new Board(c1, List.of(), specialFields, new HashMap<>());

        board.move(walker, new BattlePoint(1, 1));
        assertThat(walker.getEarthMagicLevel()).isEqualTo(MagicLevel.EXPERT);
        assertThat(walker.getFireMagicLevel()).isEqualTo(MagicLevel.EXPERT);
        assertThat(walker.getWaterMagicLevel()).isEqualTo(MagicLevel.EXPERT);
        assertThat(walker.getAirMagicLevel()).isEqualTo(MagicLevel.EXPERT);
    }

    @Test
    void mapPersistenceTest() throws java.io.IOException {
        BiMap<BattlePoint, SpecialField> originalFields = HashBiMap.create();
        originalFields.put(new BattlePoint(1, 1), new HolyGroundField());
        originalFields.put(new BattlePoint(3, 4), new QuicksandField());
        originalFields.put(new BattlePoint(5, 5), new MagicPlainsField());

        MapPersistenceManager manager = new MapPersistenceManager();
        MapData mapData = manager.convertToMapData(originalFields, 15, 11);

        java.io.File tempFile = java.io.File.createTempFile("h3map", ".json");
        tempFile.deleteOnExit();

        manager.saveMap(mapData, tempFile);

        MapData loadedData = manager.loadMap(tempFile);
        assertThat(loadedData.getWidth()).isEqualTo(15);
        assertThat(loadedData.getHeight()).isEqualTo(11);

        BiMap<BattlePoint, SpecialField> loadedFields = manager.convertToBiMap(loadedData);
        assertThat(loadedFields).hasSize(3);
        assertThat(loadedFields.get(new BattlePoint(1, 1)).getFieldName()).isEqualTo(SpecialField.FieldName.BUFF_FIELD);
        assertThat(loadedFields.get(new BattlePoint(3, 4)).getFieldName()).isEqualTo(SpecialField.FieldName.QUICKSAND);
        assertThat(loadedFields.get(new BattlePoint(5, 5)).getFieldName()).isEqualTo(SpecialField.FieldName.MAGIC_PLAINS);
    }
}
