package pl.psi.converter.rewards;

import pl.psi.hero.EconomyHero;

public interface LearningBonusProvider {
    int getLearningBonusPercent(EconomyHero hero);
}
