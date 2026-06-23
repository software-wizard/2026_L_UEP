package pl.psi.ai;

import lombok.Getter;
import pl.psi.BattlePoint;

/** Simple data holder for an attack decision. */
@Getter
public class AttackAction implements Action {
	private final BattlePoint target;

	public AttackAction(final BattlePoint target) {
		this.target = target;
	}

}


