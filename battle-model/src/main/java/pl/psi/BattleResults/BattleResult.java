package pl.psi.BattleResults;

import lombok.Getter;
import pl.psi.GameEngine;
import pl.psi.Hero;

@Getter
public class BattleResult {
    private final Hero winner;
    private final OutcomeType outcomeType;
    private final int loserVanquishedHp;

    public BattleResult(final Hero aWinner, final OutcomeType aOutcomeType, int loserVanquishedHp1) {
        winner = aWinner;
        outcomeType = aOutcomeType;
        loserVanquishedHp = loserVanquishedHp1;
    }
}
