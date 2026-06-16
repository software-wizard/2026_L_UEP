package pl.psi.converter.rewards;

import pl.psi.hero.EconomyHero;

public class FixedLearningBonusProvider implements LearningBonusProvider {
    private final int learningBonusPercent;

    public FixedLearningBonusProvider(final int aLearningBonusPercent) {
        learningBonusPercent = aLearningBonusPercent;
    }

    @Override
    public int getLearningBonusPercent(final EconomyHero hero) {
        return learningBonusPercent;
    }
}
