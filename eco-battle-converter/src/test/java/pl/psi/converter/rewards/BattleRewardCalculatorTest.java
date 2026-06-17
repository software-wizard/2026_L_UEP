package pl.psi.converter.rewards;

import org.junit.jupiter.api.Test;
import pl.psi.BattleResults.BattleResult;
import pl.psi.BattleResults.OutcomeType;
import pl.psi.Hero;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BattleRewardCalculatorTest {

    private final BattleRewardCalculator calculator = new BattleRewardCalculator();
    private final Hero dummyWinner = new Hero(List.of(), List.of()); // Pusty bohater używany w konstruktorze BattleResult

    @Test
    void shouldReturnBaseExpWithDefeatBonusForHeroVsHero() {
        // Arrange
        // Wynik bitwy: wygrana, przeciwnik stracił 120 HP
        BattleResult result = new BattleResult(dummyWinner, OutcomeType.DEFEAT, 120);

        // Act
        int baseExp = calculator.calculateBaseExperience(BattleType.HERO_VS_HERO, result);

        // Assert
        // 120 (HP) + 500 (Bonus za pokonanie bohatera) = 620
        assertEquals(620, baseExp);
    }

    @Test
    void shouldReturnOnlyVanquishedHpForBankBattle() {
        // Arrange
        // Wynik bitwy: wygrana w banku, potwory straciły 200 HP
        BattleResult result = new BattleResult(dummyWinner, OutcomeType.DEFEAT, 200);

        // Act
        int baseExp = calculator.calculateBaseExperience(BattleType.BANK_BATTLE, result);

        // Assert
        // W banku nie ma bonusu za bohatera, więc exp to tylko HP zabitych potworów
        assertEquals(200, baseExp);
    }

    @Test
    void shouldReturnNoRewardsForEscapeOrSurrender() {
        // Arrange
        // Wynik bitwy: przeciwnik uciekł, stracił 100 HP przed ucieczką
        BattleResult escaped = new BattleResult(dummyWinner, OutcomeType.ESCAPE, 100);

        // Act
        int baseExp = calculator.calculateBaseExperience(BattleType.HERO_VS_HERO, escaped);

        // Assert
        // Zgodnie z OutcomeType.ESCAPE nie powinniśmy przyznawać exp za walkę
        assertEquals(0, baseExp);
    }
}