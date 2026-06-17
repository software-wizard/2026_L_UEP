package pl.psi.converter.rewards;

import pl.psi.BattleResults.BattleResult;
import pl.psi.BattleResults.OutcomeType;

public class BattleRewardCalculator {
    private static final int HERO_DEFEAT_BONUS_EXP = 500;

    public int calculateBaseExperience(BattleType battleType, BattleResult battleResult) {
        if (battleResult.getOutcomeType() != OutcomeType.DEFEAT) {
            return 0;
        }

        int exp = battleResult.getLoserVanquishedHp();

        if (battleType == BattleType.HERO_VS_HERO) {
            exp += HERO_DEFEAT_BONUS_EXP;
        }

        return exp;
    }
}