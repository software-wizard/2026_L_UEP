package pl.psi;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import pl.psi.Spells.ActiveSpellEffect;
import pl.psi.Spells.Spell;
import pl.psi.creatures.Creature;

import lombok.Getter;

/**
 * TODO: Describe this class (The first line - until the first dot - will interpret as the brief description).
 */
public class Hero {
    @Getter
    private final List< Creature > creatures;
    @Getter
    private List<Spell> spells;

    private final Map<String, Integer> skills = new HashMap<>(); //

    public Hero(final List< Creature > aCreatures, List<Spell> aSpells)
    {
        creatures = aCreatures;
        spells = new java.util.ArrayList<>(aSpells);
    }

    public void apply(Spell s, Creature c) {
        s.cast(c);
    }

    public void addSpell(Spell spell) {
        spells.add(spell);
    }

    public void removeSpell(Spell spell) {
        spells.remove(spell);
    }

    public void removeCreature(Creature creature) {
        creatures.remove(creature);
    }

    public int getSkillLevel(String skillName) {
        return skills.getOrDefault(skillName.toUpperCase(), 0);
    }

    public void setSkillLevel(String skillName, int level) {
        skills.put(skillName.toUpperCase(), level);
    }
}
