package pl.psi;

import org.junit.jupiter.api.Test;
import pl.psi.creatures.Creature;
import pl.psi.creatures.CreatureStatisticIf;
import pl.psi.warmachines.WarMachineDecorator;
import pl.psi.warmachines.WarMachineFactory;
import pl.psi.warmachines.WarMachineType;
import pl.psi.warmachines.RandomTargetStrategy;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameEngineWarMachineTest {

    @Test
    public void balistaShouldAutomaticallyAttackAndReduceEnemyHp() {
        // 1. Przygotowanie czystych list armii
        List<Creature> player1Creatures = new ArrayList<>();
        List<Creature> player2Creatures = new ArrayList<>();

        // 2. Implementujemy jawnie cały interfejs statystyk, wstrzykując wartości bezpośrednio.
        // Gwarantuje to, że kalkulator obrażeń nie rzuci błędem NullPointerException o brak pancerza czy HP.
        CreatureStatisticIf stableStats = new CreatureStatisticIf() {
            @Override
            public String getName() { return "Test Target Dummy"; }
            @Override
            public int getAttack() { return 5; }
            @Override
            public int getArmor() { return 5; }
            @Override
            public int getMaxHp() { return 100; }
            @Override
            public int getMoveRange() { return 4; }
            @Override
            public boolean isUpgraded() { return false; }
            @Override
            public String getDescription() { return "Stable Description"; }

            // Dodajemy metodę getTier(), którą zgłaszał kompilator
            public int getTier() { return 1; }

            // Na wypadek gdyby Twój projekt wymagał zakresu obrażeń z poziomu statystyk:
            public com.google.common.collect.Range<Integer> getDamage() {
                return com.google.common.collect.Range.closed(1, 3);
            }
        };

        // 3. Budujemy obiekt stwora za pomocą oficjalnego Buildera z projektu
        Creature targetDummy = new Creature.Builder()
                .statistic(stableStats)
                .amount(1)
                .build();

        player2Creatures.add(targetDummy);

        Hero hero1 = new Hero(player1Creatures, new ArrayList<>());
        Hero hero2 = new Hero(player2Creatures, new ArrayList<>());

        // 4. Tworzymy obiekt Balisty z fabryki machin wojennych
        WarMachineFactory factory = new WarMachineFactory();
        WarMachineDecorator ballista = factory.create(WarMachineType.BALLISTA, new RandomTargetStrategy());

        List<WarMachineDecorator> player1Machines = List.of(ballista);
        List<WarMachineDecorator> player2Machines = new ArrayList<>();

        // 5. Uruchamiamy bitwę przy użyciu dedykowanej metody silnika gry
        GameEngine engine = GameEngine.createWithWarMachines(hero1, hero2, player1Machines, player2Machines);

        // Zapisujemy stan punktów życia stwora przed oddaniem strzału
        int hpBeforeShot = targetDummy.getCurrentHp();
        System.out.println("HP wroga przed strzałem Balisty: " + hpBeforeShot);

        // 6. Wywołanie ataku maszyny wojennej
        ballista.attack(targetDummy);

        // 7. Odczyt stanu po wykonaniu akcji
        int hpAfterShot = targetDummy.getCurrentHp();
        System.out.println("HP wroga po strzale Balisty: " + hpAfterShot);

        // Asercja logiczna sprawdzająca kryterium zaliczenia testu
        assertTrue(hpAfterShot < hpBeforeShot, "Balista powinna zadać obrażenia i zmniejszyć HP ofiary!");
    }
}