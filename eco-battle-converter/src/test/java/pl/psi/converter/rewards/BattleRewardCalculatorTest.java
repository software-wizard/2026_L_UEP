package pl.psi.converter.rewards;

import org.junit.jupiter.api.Test;
import pl.psi.GameEngine;
import pl.psi.hero.EconomyHero;
import pl.psi.hero.Statistics;
import pl.psi.map.resources.Resources;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BattleRewardCalculatorTest {

    private final Statistics stats = new Statistics(1, 1, 1, 1);

    @Test
    void shouldGrantHeroBattleExpWithDefeatBonus() {
        EconomyHero hero1 = new EconomyHero(EconomyHero.Fraction.NECROPOLIS, new Resources(1000, 0, 0, 0, 0, 0, 0), stats);
        EconomyHero hero2 = new EconomyHero(EconomyHero.Fraction.NECROPOLIS, new Resources(1000, 0, 0, 0, 0, 0, 0), stats);
        BattleRewardContext context = new BattleRewardContext(BattleType.HERO_VS_HERO, hero1, hero2);
        BattleRewardCalculator calculator = new BattleRewardCalculator(new FixedLearningBonusProvider(0));

        GameEngine.BattleResult result = new GameEngine.BattleResult(null, null, GameEngine.WinnerSide.HERO1,
                GameEngine.OutcomeType.DEFEAT, 120, 40);

        Map<EconomyHero, Integer> rewards = calculator.calculate(context, result);
        assertEquals(1, rewards.size());
        assertEquals(620, rewards.get(hero1));
    }

    @Test
    void shouldGrantOnlyHero1ForBankBattleWhenHero1Wins() {
        EconomyHero hero1 = new EconomyHero(EconomyHero.Fraction.NECROPOLIS, new Resources(1000, 0, 0, 0, 0, 0, 0), stats);
        BattleRewardContext context = new BattleRewardContext(BattleType.BANK_BATTLE, hero1, null);
        BattleRewardCalculator calculator = new BattleRewardCalculator(new FixedLearningBonusProvider(0));

        GameEngine.BattleResult result = new GameEngine.BattleResult(null, null, GameEngine.WinnerSide.HERO1,
                GameEngine.OutcomeType.DEFEAT, 200, 0);

        Map<EconomyHero, Integer> rewards = calculator.calculate(context, result);
        assertEquals(1, rewards.size());
        assertEquals(200, rewards.get(hero1));
    }

    @Test
    void shouldReturnNoRewardsForEscapeOrSurrender() {
        EconomyHero hero1 = new EconomyHero(EconomyHero.Fraction.NECROPOLIS, new Resources(1000, 0, 0, 0, 0, 0, 0), stats);
        EconomyHero hero2 = new EconomyHero(EconomyHero.Fraction.NECROPOLIS, new Resources(1000, 0, 0, 0, 0, 0, 0), stats);
        BattleRewardContext context = new BattleRewardContext(BattleType.HERO_VS_HERO, hero1, hero2);
        BattleRewardCalculator calculator = new BattleRewardCalculator(new FixedLearningBonusProvider(0));

        GameEngine.BattleResult escaped = new GameEngine.BattleResult(null, null, GameEngine.WinnerSide.HERO1,
                GameEngine.OutcomeType.ESCAPE, 100, 0);
        Map<EconomyHero, Integer> rewards = calculator.calculate(context, escaped);
        assertTrue(rewards.isEmpty());
    }
}
