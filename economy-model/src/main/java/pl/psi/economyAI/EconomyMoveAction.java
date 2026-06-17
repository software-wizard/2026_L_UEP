package pl.psi.economyAI;

import lombok.Getter;
import pl.psi.economy.Point;

@Getter
public class EconomyMoveAction implements EconomyAction {
    private final Point target;

    public EconomyMoveAction(Point target) {
        this.target = target;
    }

}
