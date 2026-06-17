package pl.psi.converter.rewards;

import pl.psi.hero.EconomyHero;
import pl.psi.hero.skills.AbstractSkill;

public class HeroSkillLearningBonusProvider implements LearningBonusProvider {

    // Zakładamy, że posiadasz np. SkillName.LEARNING.
    // Zwróci bonus na podstawie poziomu umiejętności bohatera.
    @Override
    public int getLearningBonusPercent(final EconomyHero hero) {
        if (hero.getSkills() == null || hero.getSkills().isEmpty()) {
            return 0;
        }

        for (AbstractSkill skill : hero.getSkills()) {
            // Zamień "LEARNING", jeśli Twój enum nazywa się inaczej (np. MYSTICISM, itp.)
            if (skill.getName().name().equals("LEARNING")) {
                // Zakładając, że getFactor() dla Learning zwraca np. 0.05 (5%), 0.10 (10%)
                // Zamieniamy to na procenty całkowite:
                return (int) (skill.getFactor() * 100);
            }
        }
        return 0; // Brak umiejętności
    }
}