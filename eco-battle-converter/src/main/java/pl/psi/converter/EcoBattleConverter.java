package pl.psi.converter;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.psi.*;
import pl.psi.BattleResults.BattleResult;
import pl.psi.Spells.Spell;
import pl.psi.Spells.DamageSpell;
import pl.psi.converter.rewards.BattleRewardService;
import pl.psi.creatures.*;
import pl.psi.economy.Point;
import pl.psi.gui.MainBattleController;
import pl.psi.hero.EconomyHero;
import pl.psi.hero.skills.AbstractSkill;
import pl.psi.converter.rewards.BattleType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static pl.psi.hero.skills.SkillName.ARMORER;
import static pl.psi.hero.skills.SkillName.OFFENCE;

public class EcoBattleConverter {

    private static final Map<pl.psi.hero.skills.SkillName, pl.psi.Spells.SpellSchool> MAGIC_MAP = Map.of(
            pl.psi.hero.skills.SkillName.FIRE_MAGIC, pl.psi.Spells.SpellSchool.FIRE,
            pl.psi.hero.skills.SkillName.WATER_MAGIC, pl.psi.Spells.SpellSchool.WATER,
            pl.psi.hero.skills.SkillName.EARTH_MAGIC, pl.psi.Spells.SpellSchool.EARTH,
            pl.psi.hero.skills.SkillName.AIR_MAGIC, pl.psi.Spells.SpellSchool.AIR
    );

    public static void startBattle(final EconomyHero aPlayer1, final EconomyHero aPlayer2) {
        try {
            final FXMLLoader loader = new FXMLLoader();
            BiMap<BattlePoint, SpecialField> specialFields = HashBiMap.create();
            specialFields.put(new BattlePoint(5, 5), new DmgField());
            specialFields.put(new BattlePoint(3, 8), new SpellField());
            specialFields.put(new BattlePoint(2, 4), new FieldCanOnlyBeFlown());
            loader.setLocation(EcoBattleConverter.class.getClassLoader()
                    .getResource("fxml/main-battle.fxml"));

            Hero convertedHero1 = convert(aPlayer1);
            Hero convertedHero2 = convert(aPlayer2);

            loader.setController(new MainBattleController(
                    convertedHero1,
                    convertedHero2,
                    new HashMap<>(),
                    specialFields,
                    battleResult -> settleBattleExperience(BattleType.HERO_VS_HERO, battleResult, aPlayer1, aPlayer2, convertedHero1, convertedHero2)
            ));

            Scene scene = new Scene(loader.load());
            final Stage aStage = new Stage();
            aStage.setScene(scene);
            aStage.setX(5);
            aStage.setY(5);
            aStage.show();
        } catch (final IOException aE) {
            aE.printStackTrace();
        }
    }

    public static Hero convert(final EconomyHero aPlayer1) {
        final List<Creature> creatures = new ArrayList<>();
        aPlayer1.getCreatures()
                .forEach(ecoCreature -> creatures.add(convertCreatureWithEffects(ecoCreature, aPlayer1)));
        
        List<Spell> spells = aPlayer1.getSpells().stream()
                .map(s -> pl.psi.Spells.SpellFactory.createSpell(s.getName(), 1))
                .collect(Collectors.toList());
        
        Hero battleHero = new Hero(creatures, spells);
        battleHero.setSpellPower(aPlayer1.getPower());

        for (pl.psi.hero.skills.AbstractSkill skill : aPlayer1.getSkills()) {
            pl.psi.Spells.SpellSchool school = MAGIC_MAP.get(skill.getName());
            if (school != null) {
                battleHero.setMagicMasteryLevel(school, (int) skill.getFactor());
            }
        }
        return battleHero;
    }

    public static void startBankBattle(final EconomyHero aPlayer1, final Map<Point, EconomyCreature> bankEnemy) {
        Map<BattlePoint, Creature> bankEnemy1 = convertEnemies(bankEnemy);

        try {
            final FXMLLoader loader = new FXMLLoader();
            loader.setLocation(EcoBattleConverter.class.getClassLoader().getResource("fxml/main-battle.fxml"));

            Hero convertedHero1 = convert(aPlayer1);
            // Pusty bohater dla AI, aby gracz nie walczył przeciwko własnym statystykom
            Hero emptyNeutralHero = new Hero(new ArrayList<>(), new ArrayList<>());

            loader.setController(new MainBattleController(
                    convertedHero1,
                    emptyNeutralHero,
                    bankEnemy1,
                    HashBiMap.create(),
                    battleResult -> settleBattleExperience(BattleType.BANK_BATTLE, battleResult, aPlayer1, null, convertedHero1, emptyNeutralHero)
            ));

            Scene scene = new Scene(loader.load());
            final Stage aStage = new Stage();
            aStage.setScene(scene);
            aStage.setX(5);
            aStage.setY(5);
            aStage.show();
        } catch (final IOException aE) {
            aE.printStackTrace();
        }
    }

    public static Map<BattlePoint, Creature> convertEnemies(Map<Point, EconomyCreature> economyMap) {
        NecropolisFactory factory = new NecropolisFactory();
        Map<BattlePoint, Creature> result = new HashMap<>();

        for (Map.Entry<Point, EconomyCreature> entry : economyMap.entrySet()) {
            EconomyCreature ecoCreature = entry.getValue();
            Creature creature = factory.create(
                    ecoCreature.isUpgraded(),
                    ecoCreature.getTier(),
                    ecoCreature.getAmount()
            );
            result.put(new BattlePoint(entry.getKey().getX(), entry.getKey().getY()), creature);
        }

        return result;
    }

    public static Creature convertCreatureWithEffects(EconomyCreature ecoCreature, EconomyHero ecoHero) {
        CreatureStatistic baseStats = ecoCreature.getStats();
        StatsModifier totalBonus = new StatsModifier(ecoHero.getTotalStatistics().getAttack(), ecoHero.getTotalStatistics().getDefense());

        CreatureStatisticIf modifiedStats = new ModifiedCreatureStats(baseStats, totalBonus);

        Creature creature;
        if (!ecoHero.getSkills().isEmpty()) {
            float reduceDamageFactor = 0;
            float bonusDamageFactor = 0;
            ArrayList<AbstractSkill> skills = new ArrayList<>(ecoHero.getSkills());

            for (AbstractSkill skill : skills) {
                if (skill.getName() == ARMORER) {
                    reduceDamageFactor = skill.getFactor();
                } else if (skill.getName() == OFFENCE) {
                    bonusDamageFactor = skill.getFactor();
                }
            }

            creature = new Creature.Builder()
                    .statistic(modifiedStats)
                    .calculator(new ReducedDamageCalculator(reduceDamageFactor, bonusDamageFactor))
                    .amount(ecoCreature.getAmount())
                    .build();
        } else {
            creature = new Creature.Builder()
                    .statistic(modifiedStats)
                    .amount(ecoCreature.getAmount())
                    .build();
        }

        // Add immunities from hero's artifacts
        for (pl.psi.hero.artifacts.Artifact artifact : ecoHero.getArtifacts()) {
            if (artifact.getType() == pl.psi.hero.artifacts.ArtifactType.BREASTPLATE_OF_BRIMSTONE) {
                creature.addImmunity(new pl.psi.Spells.FireMagicImmunity());
            } else if (artifact.getType() == pl.psi.hero.artifacts.ArtifactType.CROWN_OF_THE_SUPREME_MAGI) {
                creature.addImmunity(new pl.psi.Spells.FireDebuffImmunity());
            }
        }

        return creature;
    }

    private static void settleBattleExperience(final BattleType battleType,
                                               final BattleResult battleResult,
                                               final EconomyHero ecoPlayer1,
                                               final EconomyHero ecoPlayer2,
                                               final Hero convertedHero1,
                                               final Hero convertedHero2) {

        BattleRewardService rewardService = new BattleRewardService();
        rewardService.settleExperience(battleType, battleResult, ecoPlayer1, ecoPlayer2, convertedHero1, convertedHero2);
}}