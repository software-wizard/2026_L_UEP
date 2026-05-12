package pl.psi.Spells;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import pl.psi.creatures.Creature;
import pl.psi.creatures.CreatureStatisticIf;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
@Getter
public abstract class Spell {

    String name;
    int spellLevel;
    int duration;
    @Getter
    private SpellAreaIf areaStrategy;

    public Spell(String name, int spellLevel) {
        this.name = name;
        this.spellLevel = spellLevel;
        this.areaStrategy = new SingleTargetArea();
    }

    public Spell(String name, int spellLevel, SpellAreaIf areaStrategy) {
        this.name = name;
        this.spellLevel = spellLevel;
        this.areaStrategy = areaStrategy;
    }

    public Spell(String name, int spellLevel, int duration ) {
        this.name = name;
        this.duration = duration;
        this.spellLevel = spellLevel;
        this.areaStrategy = new SingleTargetArea();
    }

    public Spell(String name, int spellLevel, int duration, SpellAreaIf areaStrategy) {
        this.name = name;
        this.duration = duration;
        this.spellLevel = spellLevel;
        this.areaStrategy = areaStrategy;
    }

    public Spell() {
    }

    public abstract void cast(Creature targetCreature, int spellPower);

    public CreatureStatisticIf modifyStats(CreatureStatisticIf baseStats) {
        return baseStats;
    }
}

