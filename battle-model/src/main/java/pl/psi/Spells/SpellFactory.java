package pl.psi.Spells;

import pl.psi.creatures.CreatureStats;

public class SpellFactory {
    public static Spell createSpell(String name, int level) {
        if (name.startsWith("Attack Boost")) {
            int val = name.contains("II") ? 5 : 3;
            return new BuffSpell(name, level, 3, CreatureStats.builder()
                    .attack(val)
                    .armor(0)
                    .maxHp(0)
                    .moveRange(0)
                    .name(name + " Buff")
                    .description("Increases attack by " + val + " for 3 turns.")
                    .build(), SpellSchool.FIRE);
        }
        if (name.startsWith("Speed Boost")) {
            int val = name.contains("II") ? 3 : 2;
            return new BuffSpell(name, level, 3, CreatureStats.builder()
                    .attack(0)
                    .armor(0)
                    .maxHp(0)
                    .moveRange(val)
                    .name(name + " Buff")
                    .description("Increases move range by " + val + " for 3 turns.")
                    .build(), SpellSchool.AIR);
        }
        if (name.startsWith("Slow")) {
            int val = name.contains("II") ? 2 : 1;
            return new DebuffSpell(name, level, 3, CreatureStats.builder()
                    .attack(0)
                    .armor(0)
                    .maxHp(0)
                    .moveRange(val)
                    .name(name + " Debuff")
                    .description("Reduces move range by " + val + " for 3 turns.")
                    .build(), SpellSchool.EARTH);
        }
        if (name.startsWith("Weakness")) {
            int att = name.contains("II") ? 3 : 1;
            int arm = name.contains("II") ? 2 : 1;
            return new DebuffSpell(name, level, 3, CreatureStats.builder()
                    .attack(att)
                    .armor(arm)
                    .maxHp(0)
                    .moveRange(0)
                    .name(name + " Debuff")
                    .description("Reduces attack by " + att + " and armor by " + arm + " for 3 turns.")
                    .build(), SpellSchool.WATER);
        }
        if (name.startsWith("Fireball")) {
            return new DamageSpell(name, level, 1, new RadiusArea(2.0), SpellSchool.FIRE);
        }
        if (name.startsWith("Lightning Bolt")) {
            return new DamageSpell(name, level, 1, new SingleTargetArea(), SpellSchool.AIR);
        }
        if (name.startsWith("Fire Wall") || name.equals("Advanced Fire Wall")) {
            return new FireWallSpell(name, level, null, 5.0);
        }
        // Fallback
        return new DamageSpell(name, level, 1, new SingleTargetArea(), SpellSchool.NONE);
    }
}
