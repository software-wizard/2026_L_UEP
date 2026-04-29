package pl.psi.converter.rewards;

import lombok.Getter;
import pl.psi.hero.EconomyHero;

@Getter
public class BattleRewardContext {
    private final BattleType battleType;
    private final EconomyHero hero1;
    private final EconomyHero hero2;

    public BattleRewardContext(final BattleType aBattleType, final EconomyHero aHero1, final EconomyHero aHero2) {
        battleType = aBattleType;
        hero1 = aHero1;
        hero2 = aHero2;
    }

}
