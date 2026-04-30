package pl.psi.converter.rewards;

import pl.psi.BattleResults.BattleResult;
import pl.psi.Hero;
import pl.psi.hero.EconomyHero;

public class BattleRewardService {
    public static void settleExperience(final BattleType battleType,
                                        final BattleResult battleResult,
                                        final EconomyHero ecoPlayer1,
                                        final EconomyHero ecoPlayer2,
                                        final Hero convertedHero1,
                                        final Hero convertedHero2) {

        BattleRewardCalculator calculator = new BattleRewardCalculator();
        int baseExperience = calculator.calculateBaseExperience(battleType, battleResult);

        if (baseExperience <= 0) {
            return;
        }

        // Ustalamy, który bohater ekonomiczny odpowiada zwycięskiemu bohaterowi z bitwy
        EconomyHero winnerEcoHero = null;
        if (battleResult.getWinner() == convertedHero1) {
            winnerEcoHero = ecoPlayer1;
        } else if (battleResult.getWinner() == convertedHero2) {
            winnerEcoHero = ecoPlayer2;
        }

        // Przypisujemy punkty doświadczenia (wraz z wewnętrznymi modyfikatorami bohatera)
        if (winnerEcoHero != null) {
            winnerEcoHero.addExperience(baseExperience);
        }
    }
}
