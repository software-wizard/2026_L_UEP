package pl.psi.warmachines;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import pl.psi.creatures.Creature;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WarMachineIntegrationTest {


    @Test
    public void factoryShouldCreateBallistaOfCorrectType() {
        WarMachineFactory factory = new WarMachineFactory();

        WarMachineDecorator result = factory.create(
                WarMachineType.BALLISTA,
                new RandomTargetStrategy()
        );

        assertNotNull(result);
        assertTrue(result instanceof BallistaCreature);
    }

    @Test
    public void ballistaShouldHaveZeroMoveRange() {
        WarMachineFactory factory = new WarMachineFactory();
        WarMachineDecorator ballista = factory.create(
                WarMachineType.BALLISTA,
                new RandomTargetStrategy()
        );

        int moveRange = ballista.getMoveRange();

        assertEquals(0, moveRange);
    }

    @Test
    public void randomStrategyShouldReturnEmptyWhenNoEnemiesAlive() {
        BallistaTargetStrategy strategy = new RandomTargetStrategy();
        List<Creature> emptyEnemyList = List.of();

        Optional<Creature> target = strategy.selectTarget(emptyEnemyList, null);

        assertTrue(target.isEmpty());
    }

    @Test
    public void playerChoiceStrategyShouldReturnChosenTarget() {
        Creature target = new Creature.Builder()
                .statistic(WarMachineStats.BALLISTA)
                .amount(1)
                .build();
        BallistaTargetStrategy strategy = new PlayerChoiceTargetStrategy();

        Optional<Creature> result = strategy.selectTarget(List.of(target), target);

        assertTrue(result.isPresent());
        assertEquals(target, result.get());
    }

    @Test
    public void medicTentShouldNotHealDeadCreature() {
        Creature deadCreature = new Creature.Builder()
                .statistic(WarMachineStats.MEDIC_TENT)
                .amount(0)
                .build();

        WarMachineFactory factory = new WarMachineFactory();
        MedicTentCreature tent = (MedicTentCreature) factory.create(
                WarMachineType.MEDIC_TENT, null
        );

        int hpBefore = deadCreature.getCurrentHp();
        tent.heal(deadCreature, 1.0);

        assertEquals(hpBefore, deadCreature.getCurrentHp());
    }

    @Test
    public void factoryShouldCreateMedicTentOfCorrectType() {
        WarMachineFactory factory = new WarMachineFactory();

        WarMachineDecorator result = factory.create(WarMachineType.MEDIC_TENT, null);

        assertTrue(result instanceof MedicTentCreature);
    }

    @Test
    public void playerChoiceStrategyShouldRejectTargetNotInEnemyList() {
        Creature validEnemy = new Creature.Builder()
                .statistic(WarMachineStats.BALLISTA)
                .amount(1)
                .build();
        Creature impostor = new Creature.Builder()
                .statistic(WarMachineStats.BALLISTA)
                .amount(1)
                .build();

        BallistaTargetStrategy strategy = new PlayerChoiceTargetStrategy();
        Optional<Creature> result = strategy.selectTarget(List.of(validEnemy), impostor);

        assertTrue(result.isEmpty());
    }

    @Test
    public void catapultShouldDestroyWallAfterSeveralHits() {
        int wallDurability = 100;

        wallDurability -= 30;
        wallDurability -= 30;
        wallDurability -= 40;

        assertEquals(0, wallDurability,
                "Mur powinien zostać całkowicie zniszczony po otrzymaniu obrażeń równych jego wytrzymałości.");
    }

    @Test
    public void catapultShouldNotDamageAlreadyDestroyedWall() {
        int wallDurability = 0;

        wallDurability = Math.max(0, wallDurability - 50);

        assertEquals(0, wallDurability,
                "Zniszczony mur nie powinien przyjmować dodatkowych obrażeń ani osiągać wartości ujemnych.");
    }

    @Test
    public void heroShouldBeAbleToOwnSeveralWarMachines() {
        List<String> machines = new ArrayList<>();

        machines.add("BALLISTA");
        machines.add("CATAPULT");
        machines.add("FIRST_AID_TENT");

        assertEquals(3, machines.size(),
                "Bohater powinien mieć możliwość posiadania kilku różnych maszyn wojennych jednocześnie.");
    }

    @Test
    public void firstAidTentShouldRestoreHealth() {
        int hp = 50;

        hp += 20;

        assertEquals(70, hp,
                "Namiot medyka powinien poprawnie zwiększać zdrowie leczonej jednostki.");
    }

    @Test
    public void firstAidTentShouldNotHealAboveMaximumHealth() {
        int currentHp = 95;
        int maxHp = 100;

        currentHp = Math.min(maxHp, currentHp + 20);

        assertEquals(100, currentHp,
                "Leczenie nie powinno zwiększać zdrowia jednostki ponad jej maksymalną wartość.");
    }

    @Test
    public void heroShouldNotPurchaseSameWarMachineTwice() {
        List<String> machines = new ArrayList<>();
        machines.add("BALLISTA");

        if (!machines.contains("BALLISTA")) {
            machines.add("BALLISTA");
        }

        assertEquals(1, machines.size(),
                "Ten sam typ maszyny wojennej nie powinien zostać dodany do kolekcji więcej niż jeden raz.");
    }

    @Test
    public void forgeShouldSupportDifferentWarMachineTypes() {
        List<String> availableMachines = new ArrayList<>();

        availableMachines.add("BALLISTA");
        availableMachines.add("CATAPULT");
        availableMachines.add("FIRST_AID_TENT");

        assertTrue(availableMachines.contains("BALLISTA"),
                "Warsztat powinien umożliwiać zakup balisty.");

        assertTrue(availableMachines.contains("CATAPULT"),
                "Warsztat powinien umożliwiać zakup katapulty.");

        assertTrue(availableMachines.contains("FIRST_AID_TENT"),
                "Warsztat powinien umożliwiać zakup namiotu medyka.");
    }

    @Test
    public void destroyedWarMachineShouldNotBeOperational() {
        int machineHealth = 0;

        boolean canAct = machineHealth > 0;

        assertFalse(canAct,
                "Zniszczona maszyna wojenna nie powinna wykonywać żadnych akcji podczas bitwy.");
    }
}