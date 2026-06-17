package pl.psi.Spells;

import pl.psi.Board;
import pl.psi.BattlePoint;

public interface BoardAffectingSpell {
    void castOnBoard(Board board, BattlePoint targetPoint, int spellPower);
}
