package pl.psi.hero.skills.modifiers;

import pl.psi.hero.skills.SkillName;

public interface SpellModifierIf {
    int changeSpellPower(SkillName spellSchool, int currentSpellPower);
}
