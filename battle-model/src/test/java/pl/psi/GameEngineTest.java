package pl.psi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.google.common.collect.HashBiMap;
import pl.psi.creatures.CastleCreatureFactory;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * TODO: Describe this class (The first line - until the first dot - will interpret as the brief description).
 */
public class GameEngineTest
{
    @Test
    void shoudWorksHeHe()
    {
        final CastleCreatureFactory creatureFactory = new CastleCreatureFactory();
        final GameEngine gameEngine =
                new GameEngine( new Hero( List.of( creatureFactory.create( 1, false, 5 ) ), new ArrayList<>()),
                        new Hero( List.of( creatureFactory.create( 1, false, 5 ) ), new ArrayList<>()) );

        gameEngine.attack( new BattlePoint( 1, 1 ) );
    }

    @Test
    void shouldFinishBattleAndExposeResultForRewards() {
        final CastleCreatureFactory creatureFactory = new CastleCreatureFactory();
        final Hero hero1 = new Hero(List.of(creatureFactory.create(1, false, 25)), new ArrayList<>());
        final Hero hero2 = new Hero(new ArrayList<>(), new ArrayList<>());
        final HashMap<BattlePoint, pl.psi.creatures.Creature> bankEnemy = new HashMap<>();
        bankEnemy.put(new BattlePoint(1, 1), creatureFactory.create(1, false, 1));

        final GameEngine gameEngine = new GameEngine(hero1, hero2, HashBiMap.create(), bankEnemy);

        int attempts = 0;
        while (!gameEngine.isBattleOver() && attempts < 30) {
            if (gameEngine.canAttack(new BattlePoint(1, 1))) {
                gameEngine.attack(new BattlePoint(1, 1));
            } else {
                gameEngine.pass();
            }
            attempts++;
        }

        assertTrue(gameEngine.isBattleOver());
        assertTrue(gameEngine.getBattleResult().isPresent());
        assertTrue(gameEngine.isHero1Winner());
        assertTrue(gameEngine.getBattleResult().get().getLoserVanquishedHp() > 0);
    }
}