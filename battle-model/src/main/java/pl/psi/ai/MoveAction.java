package pl.psi.ai;

import pl.psi.BattlePoint;

/** Simple data holder for a movement decision. */
public class MoveAction {
    private final BattlePoint target;

    public MoveAction(final BattlePoint target) {
        this.target = target;
    }

    public BattlePoint getTarget() {
        return target;
    }
}

