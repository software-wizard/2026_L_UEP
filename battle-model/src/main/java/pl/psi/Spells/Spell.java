package pl.psi.Spells;

import lombok.Getter;
import pl.psi.creatures.Creature;
import pl.psi.creatures.CreatureStatisticIf;

@Getter
public abstract class Spell {

    String name;
    int spellLevel;
    int duration;
    private final SpellSchool school;
    @Getter
    private final SpellAreaIf areaStrategy;

    public Spell(String name, int spellLevel, SpellSchool school) {
        this.name = name;
        this.spellLevel = spellLevel;
        this.school = school;
        this.areaStrategy = new SingleTargetArea();
    }

    public Spell(String name, int spellLevel) {
        this(name, spellLevel, SpellSchool.NONE);
    }

    public Spell(String name, int spellLevel, SpellAreaIf areaStrategy, SpellSchool school) {
        this.name = name;
        this.spellLevel = spellLevel;
        this.school = school;
        this.areaStrategy = areaStrategy;
    }

    public Spell(String name, int spellLevel, SpellAreaIf areaStrategy) {
        this(name, spellLevel, areaStrategy, SpellSchool.NONE);
    }

    public Spell(String name, int spellLevel, int duration, SpellSchool school) {
        this.name = name;
        this.duration = duration;
        this.spellLevel = spellLevel;
        this.school = school;
        this.areaStrategy = new SingleTargetArea();
    }

    public Spell(String name, int spellLevel, int duration) {
        this(name, spellLevel, duration, SpellSchool.NONE);
    }

    public Spell(String name, int spellLevel, int duration, SpellAreaIf areaStrategy, SpellSchool school) {
        this.name = name;
        this.duration = duration;
        this.spellLevel = spellLevel;
        this.school = school;
        this.areaStrategy = areaStrategy;
    }

    public Spell(String name, int spellLevel, int duration, SpellAreaIf areaStrategy) {
        this(name, spellLevel, duration, areaStrategy, SpellSchool.NONE);
    }

    public abstract void cast(Creature targetCreature, int spellPower);

    public CreatureStatisticIf modifyStats(CreatureStatisticIf baseStats) {
        return baseStats;
    }
}

