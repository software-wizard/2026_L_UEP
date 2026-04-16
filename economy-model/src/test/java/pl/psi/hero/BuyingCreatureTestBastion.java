package pl.psi.hero;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.psi.EconomyEngine;
import pl.psi.creatures.EconomyBastionFactory;
import pl.psi.map.resources.Resources;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BuyingCreatureTestBastion
{

    private final EconomyBastionFactory creatureFactory = new EconomyBastionFactory();


    private EconomyHero hero1;
    private EconomyEngine economyEngine;
    private EconomyHero hero2;
    private Resources resources;



    @BeforeEach
    void init()
    {
        Statistics aStats = new Statistics(10, 10, 10, 10); ///
        resources = new Resources(1000,0,0,0,0,0,0);
        hero1 = new EconomyHero( EconomyHero.Fraction.BASTION, resources, aStats);
        hero2 = new EconomyHero( EconomyHero.Fraction.BASTION, resources, aStats);
        economyEngine = new EconomyEngine( hero1 );


    }

    @Test
    void heroShouldCanBuyCreature()
    {
        economyEngine.buy( creatureFactory.create( false, 1, 1) );

        assertEquals( 930, hero1.getResources().getGold() );
    }

    @Test
    void heroShouldCanBuyMoreThanOneCreatureInOneStack()
    {
        economyEngine.buy( creatureFactory.create( false, 1, 2) );

        assertEquals( 860, hero1.getResources().getGold() );
    }

    @Test
    void heroShouldCanBuyMoreThanOneCreatureInFewStack()
    {
        economyEngine.buy( creatureFactory.create( false, 1, 2 ) );
        economyEngine.buy( creatureFactory.create( true, 2, 2 ) );

        assertEquals( 560, hero1.getResources().getGold() );
    }

    @Test
    void heroCannotBuyCreatureWhenHasNotEnoughtGold()
    {
        assertThrows( IllegalStateException.class,
            () -> economyEngine.buy( creatureFactory.create( false, 1, 100 ) ) );
        assertEquals( 1000, hero1.getResources().getGold() );
        assertEquals( 0, hero1.getCreatures()
            .size() );
    }

    @Test
    void heroCannotBuyCreatureIfHeIsFull()
    {
        economyEngine.buy( creatureFactory.create( false, 1, 1 ) );
        economyEngine.buy( creatureFactory.create( false, 1, 1 ) );
        economyEngine.buy( creatureFactory.create( false, 1, 1 ) );
        economyEngine.buy( creatureFactory.create( false, 1, 1 ) );
        economyEngine.buy( creatureFactory.create( false, 1, 1 ) );
        economyEngine.buy( creatureFactory.create( false, 1, 1 ) );
        economyEngine.buy( creatureFactory.create( false, 1, 1 ) );
        assertThrows( IllegalStateException.class,
            () -> economyEngine.buy( creatureFactory.create( false, 1, 1 ) ) );

        assertEquals( 510, hero1.getResources().getGold() );
        assertEquals( 7, hero1.getCreatures()
            .size() );
    }
}
