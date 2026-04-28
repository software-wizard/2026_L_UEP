package pl.psi.converter.rewards;

import pl.psi.hero.EconomyHero;

public class BattleRewardContext {
    private final BattleType battleType;
    private final EconomyHero hero1;
    private final EconomyHero hero2;

    public BattleRewardContext(final BattleType aBattleType, final EconomyHero aHero1, final EconomyHero aHero2) {
        battleType = aBattleType;
        hero1 = aHero1;
        hero2 = aHero2;
    }

    public BattleType getBattleType() {
        return battleType;
    }

    public EconomyHero getHero1() {
        return hero1;
    }

    public EconomyHero getHero2() {
        return hero2;
    }
}
