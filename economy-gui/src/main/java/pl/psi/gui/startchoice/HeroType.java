package pl.psi.gui.startchoice;

import pl.psi.creatures.EconomyNecropolisFactory;
import pl.psi.creatures.EconomyCreature;
import pl.psi.hero.EconomyHero;
import pl.psi.hero.HeroClass;
import pl.psi.hero.Statistics;
import pl.psi.hero.skills.HeroStartingSkill;
import pl.psi.hero.skills.SkillLevel;
import pl.psi.hero.skills.SkillName;
import pl.psi.map.resources.Resources;

import java.util.List;
import java.util.function.Supplier;

public enum HeroType {

    SANDRO("Sandro", EconomyHero.Fraction.NECROPOLIS, HeroClass.NECROMANCER, () -> {
        var factory = new EconomyNecropolisFactory();
        return new HeroData(new Statistics(1, 0, 2, 1), List.of(
                factory.create(false, 1, 40)
        ), startingSkills(SkillName.NECROMANCY, SkillName.SORCERY));
    }),

    VIDOMINA("Vidomina", EconomyHero.Fraction.NECROPOLIS, HeroClass.NECROMANCER, () -> {
        var factory = new EconomyNecropolisFactory();
        return new HeroData(new Statistics(0, 0, 1, 2), List.of(
                factory.create(false, 1, 40)
        ), startingSkills(SkillName.NECROMANCY));
    }),

    NIMBUS("Nimbus", EconomyHero.Fraction.NECROPOLIS, HeroClass.NECROMANCER, () -> {
        var factory = new EconomyNecropolisFactory();
        return new HeroData(new Statistics(0, 0, 2, 1), List.of(
                factory.create(false, 1, 40)
        ), startingSkills(SkillName.NECROMANCY, SkillName.EAGLE_EYE));
    }),

    THANT("Thant", EconomyHero.Fraction.NECROPOLIS, HeroClass.NECROMANCER, () -> {
        var factory = new EconomyNecropolisFactory();
        return new HeroData(new Statistics(0, 0, 1, 2), List.of(
                factory.create(false, 1, 40)
        ), startingSkills(SkillName.NECROMANCY, SkillName.MYSTICISM));
    }),

    XSI("Xsi", EconomyHero.Fraction.NECROPOLIS, HeroClass.NECROMANCER, () -> {
        var factory = new EconomyNecropolisFactory();
        return new HeroData(new Statistics(1, 0, 1, 1), List.of(
                factory.create(false, 1, 40)
        ), startingSkills(SkillName.NECROMANCY, SkillName.LEARNING));
    }),

    MOANDOR("Moandor", EconomyHero.Fraction.NECROPOLIS, HeroClass.DEATH_KNIGHT, () -> {
        var factory = new EconomyNecropolisFactory();
        return new HeroData(new Statistics(0, 0, 1, 1), List.of(
                factory.create(false, 1, 40)
        ), startingSkills(SkillName.NECROMANCY, SkillName.LEARNING));
    }),

    ISRA("Isra", EconomyHero.Fraction.NECROPOLIS, HeroClass.DEATH_KNIGHT, () -> {
        var factory = new EconomyNecropolisFactory();
        return new HeroData(new Statistics(1, 0, 1, 1), List.of(
                factory.create(false, 1, 40)
        ), startingSkills(SkillName.NECROMANCY));
    }),

    CLAVIUS("Clavius", EconomyHero.Fraction.NECROPOLIS, HeroClass.DEATH_KNIGHT, () -> {
        var factory = new EconomyNecropolisFactory();
        return new HeroData(new Statistics(1, 0, 1, 1), List.of(
                factory.create(false, 1, 40)
        ), startingSkills(SkillName.NECROMANCY, SkillName.ESTATES));
    }),

    TAMIKA("Tamika", EconomyHero.Fraction.NECROPOLIS, HeroClass.DEATH_KNIGHT, () -> {
        var factory = new EconomyNecropolisFactory();
        return new HeroData(new Statistics(2, 1, 1, 1), List.of(
                factory.create(false, 1, 40),
                factory.create(true, 2, 6)
        ), startingSkills(SkillName.NECROMANCY, SkillName.OFFENCE));
    }),

    GALTHRAN("Galthran", EconomyHero.Fraction.NECROPOLIS, HeroClass.DEATH_KNIGHT, () -> {
        var factory = new EconomyNecropolisFactory();
        return new HeroData(new Statistics(1, 2, 1, 1), List.of(
                factory.create(false, 1, 60)
        ), startingSkills(SkillName.NECROMANCY, SkillName.ARMORER));
    }),

    VOKIAL("Vokial", EconomyHero.Fraction.NECROPOLIS, HeroClass.DEATH_KNIGHT, () -> {
        var factory = new EconomyNecropolisFactory();
        return new HeroData(new Statistics(2, 1, 1, 1), List.of(
                factory.create(false, 1, 40),
                factory.create(true, 2, 6)
        ), startingSkills(SkillName.NECROMANCY, SkillName.ARTILLERY));
    }),

    STRAKER("Straker", EconomyHero.Fraction.NECROPOLIS, HeroClass.DEATH_KNIGHT, () -> {
        var factory = new EconomyNecropolisFactory();
        return new HeroData(new Statistics(1, 2, 1, 1), List.of(
                factory.create(true, 2, 6),
                factory.create(false, 3, 5)
        ), startingSkills(SkillName.NECROMANCY, SkillName.OFFENCE));
    }),

    SEPTIENNA("Septienna", EconomyHero.Fraction.NECROPOLIS, HeroClass.NECROMANCER, () -> {
        var factory = new EconomyNecropolisFactory();
        return new HeroData(new Statistics(1, 0, 1, 2), List.of(
                factory.create(false, 1, 40)
        ), startingSkills(SkillName.NECROMANCY, SkillName.SCHOLAR));
    }),

    // Naadir is kept as a project hero outside the base Heroes 3 Necropolis roster.
    NAADIR("Naadir", EconomyHero.Fraction.NECROPOLIS, HeroClass.NECROMANCER, () -> {
        var factory = new EconomyNecropolisFactory();
        return new HeroData(new Statistics(1, 1, 1, 1), List.of(
                factory.create(false, 1, 40)
        ), startingSkills(SkillName.NECROMANCY));
    }),

    // Fiona is not a base Heroes 3 Necropolis hero; use the current faction's default class.
    FIONA("Fiona", EconomyHero.Fraction.NECROPOLIS, HeroClass.NECROMANCER, () -> {
        var factory = new EconomyNecropolisFactory();
        return new HeroData(new Statistics(1, 1, 1, 1), List.of(
                factory.create(false, 1, 40)
        ), startingSkills(SkillName.NECROMANCY));
    }),

    CHARNA("Charna", EconomyHero.Fraction.NECROPOLIS, HeroClass.DEATH_KNIGHT, () -> {
        var factory = new EconomyNecropolisFactory();
        return new HeroData(new Statistics(2, 0, 1, 1), List.of(
                factory.create(false, 1, 40)
        ), startingSkills(SkillName.NECROMANCY, SkillName.TACTICS));
    }),

    // Technical hero used by the project; keep a safe current-faction default class.
    TESTER("Tester", EconomyHero.Fraction.NECROPOLIS, HeroClass.NECROMANCER, () -> {
        var factory = new EconomyNecropolisFactory();
        return new HeroData(new Statistics(1, 1, 1, 1), List.of(
                factory.create(false, 1, 1)
        ));
    });



    public static List<String> getHeroNamesForFraction(EconomyHero.Fraction fraction) {
        // Return hero names based on the fraction
        switch (fraction) {
            case NECROPOLIS:
                return List.of("Sandro", "Vidomina", "Nimbus", "Thant", "Xsi", "Moandor", "Isra", "Clavius", "Tamika", "Galthran", "Vokial", "Straker", "Septienna", "Naadir", "Fiona", "Charna", "Tester");
            default:
                return List.of();  // Return an empty list for unknown fractions
        }
    }

    public final String displayName;
    public final EconomyHero.Fraction fraction;
    public final HeroClass heroClass;
    private final Supplier<HeroData> dataSupplier;

    HeroType(String displayName, EconomyHero.Fraction fraction, HeroClass heroClass, Supplier<HeroData> dataSupplier) {
        this.displayName = displayName;
        this.fraction = fraction;
        this.heroClass = heroClass;
        this.dataSupplier = dataSupplier;
    }

    public HeroData getData() {
        return dataSupplier.get();
    }

    public static class HeroData {
        private final Statistics stats;
        private final List<EconomyCreature> creatures;
        private final List<HeroStartingSkill> startingSkills;

        public HeroData(Statistics stats, List<EconomyCreature> creatures) {
            this(stats, creatures, List.of());
        }

        public HeroData(Statistics stats, List<EconomyCreature> creatures, List<HeroStartingSkill> startingSkills) {
            this.stats = stats;
            this.creatures = creatures;
            this.startingSkills = startingSkills;
        }

        public Statistics getStats() {
            return stats;
        }

        public List<EconomyCreature> getCreatures() {
            return creatures;
        }

        public List<HeroStartingSkill> getStartingSkills() {
            return startingSkills;
        }
    }

    private static List<HeroStartingSkill> startingSkills(SkillName... skillNames) {
        return java.util.Arrays.stream(skillNames)
                .map(skillName -> new HeroStartingSkill(skillName, SkillLevel.BASIC))
                .collect(java.util.stream.Collectors.toUnmodifiableList());
    }


}
