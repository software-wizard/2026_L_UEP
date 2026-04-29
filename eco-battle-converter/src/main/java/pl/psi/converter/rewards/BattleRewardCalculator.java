package pl.psi.converter.rewards;

import pl.psi.GameEngine;
import pl.psi.hero.EconomyHero;

import java.util.LinkedHashMap;
import java.util.Map;

public class BattleRewardCalculator {
    private static final int HERO_DEFEAT_BONUS_EXP = 500;

    private final LearningBonusProvider learningBonusProvider;

    public BattleRewardCalculator(final LearningBonusProvider aLearningBonusProvider) {
        learningBonusProvider = aLearningBonusProvider;
    }

    public Map<EconomyHero, Integer> calculate(final BattleRewardContext aContext,
                                               final GameEngine.BattleResult aBattleResult) {
        final Map<EconomyHero, Integer> rewards = new LinkedHashMap<>();
        if (aBattleResult.getOutcomeType() != GameEngine.OutcomeType.DEFEAT) {
            return rewards;
        }

        if (aContext.getBattleType() == BattleType.BANK_BATTLE) {
            if (!aBattleResult.isHero1Winner()) {
                return rewards;
            }
            // Zmiana matematyki ukryta jest wewnątrz metody withLearningBonus
            final int exp = withLearningBonus(aContext.getHero1(), aBattleResult.getHero1VanquishedEnemyHp());
            if (exp > 0) {
                rewards.put(aContext.getHero1(), exp);
            }
            return rewards;
        }

        final boolean hero1Won = aBattleResult.getWinnerSide() == GameEngine.WinnerSide.HERO1;
        final EconomyHero winner = hero1Won ? aContext.getHero1() : aContext.getHero2();
        if (winner == null) {
            return rewards;
        }

        final int baseExp = hero1Won ? aBattleResult.getHero1VanquishedEnemyHp() : aBattleResult.getHero2VanquishedEnemyHp();
        final int exp = withLearningBonus(winner, baseExp + HERO_DEFEAT_BONUS_EXP);

        if (exp > 0) {
            rewards.put(winner, exp);
        }
        return rewards;
    }

    private int withLearningBonus(final EconomyHero aHero, final int aBaseExp) {
        if (aBaseExp <= 0) {
            return 0;
        }
        final int bonusPercent = Math.max(0, learningBonusProvider.getLearningBonusPercent(aHero));
        final int bonusExp = (aBaseExp * bonusPercent) / 100;

        return aBaseExp + bonusExp;
    }
}