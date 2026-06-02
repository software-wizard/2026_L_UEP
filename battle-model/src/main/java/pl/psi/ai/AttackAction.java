package pl.psi.ai;

import pl.psi.BattlePoint;

/** Simple data holder for an attack decision. */
public class AttackAction implements Action {
	private final BattlePoint target;

	public AttackAction(final BattlePoint target) {
		this.target = target;
	}

	public BattlePoint getTarget() {
		return target;
	}
}


