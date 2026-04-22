package pl.psi;

import java.util.List;

import pl.psi.Spells.ActiveSpellEffect;
import pl.psi.Spells.Spell;
import pl.psi.creatures.Creature;

import lombok.Getter;
import lombok.Setter;

/**
 * TODO: Describe this class (The first line - until the first dot - will interpret as the brief description).
 */
public class Hero implements java.beans.PropertyChangeListener {
    @Getter
    private final List< Creature > creatures;
    @Getter
    private List<Spell> spells;
    @Getter
    @Setter
    private int spellPower = 1;

    @Getter
    private SpellCastingState spellCastingState = new ReadyToCastState();

    public Hero(final List< Creature > aCreatures, List<Spell> aSpells)
    {
        creatures = aCreatures;
        spells = new java.util.ArrayList<>(aSpells);
    }

    public void apply(Spell s, Creature c) {
        checkCastState();
        s.cast(c, spellPower);
        spellCastingState = spellCastingState.nextStateOnCast();
    }

    public void castSpell(Spell s, Creature c) {
        checkCastState();
        s.cast(c, spellPower);
        spellCastingState = spellCastingState.nextStateOnCast();
    }

    public void castSpell(Spell s, List<Creature> targets) {
        checkCastState();
        for (Creature c : targets) {
            s.cast(c, spellPower);
        }
        spellCastingState = spellCastingState.nextStateOnCast();
    }

    private void checkCastState() {
        if (!spellCastingState.canCast()) {
            throw new IllegalStateException("Bohater rzucił już zaklęcie w tej rundzie!");
        }
    }

    @Override
    public void propertyChange(java.beans.PropertyChangeEvent evt) {
        if (TurnQueue.END_OF_TURN.equals(evt.getPropertyName())) {
            spellCastingState = spellCastingState.nextStateOnRoundEnd();
        }
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
}
