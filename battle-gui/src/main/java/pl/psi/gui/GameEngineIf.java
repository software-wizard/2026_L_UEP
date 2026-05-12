package pl.psi.gui;

import com.google.common.collect.BiMap;
import pl.psi.BattlePoint;
import pl.psi.Hero;
import pl.psi.SpecialField;
import pl.psi.Spells.Spell;
import pl.psi.creatures.Creature;
import pl.psi.BattleResults.BattleResult;
import java.beans.PropertyChangeListener;
import java.util.Optional;

public interface GameEngineIf {
    String BATTLE_FINISHED = "BATTLE_FINISHED";
    boolean canMove(BattlePoint aBattlePoint);
    void move(BattlePoint aBattlePoint);
    boolean canAttack(BattlePoint aBattlePoint);
    void attack(BattlePoint aBattlePoint);
    void pass();
    void interact(BattlePoint aCurrentBattlePoint);
    boolean isCurrentCreature(BattlePoint aBattlePoint);
    Optional<Creature> getCreature(BattlePoint aBattlePoint);
    Hero getCurrentHero();
    void castSpell(Spell spell, Creature targetCreature);
    void castSpell(Spell spell, BattlePoint targetPoint);
    BiMap<BattlePoint, SpecialField> getSpecialFields();
    void addObserver(PropertyChangeListener aObserver);
    boolean isBattleOver();
    Optional<BattleResult> getBattleResult();
}