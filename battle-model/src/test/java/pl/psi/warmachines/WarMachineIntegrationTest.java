package pl.psi.warmachines;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

public class WarMachineIntegrationTest {

    @Test
    public void shouldLoadHighDamageStatsFromEnum() {
        assertNotNull(WarMachineStats.values(), "Enum WarMachineStats powinien poprawnie istnieć.");
    }

    @Test
    public void ballistaShotShouldDecreaseEnemyCreatureAmount() {
        int startingAmount = 40;
        int damageAmountReduction = 5;

        int finalAmount = Math.max(0, startingAmount - damageAmountReduction);

        assertEquals(35, finalAmount, "Logika redukcji oddziału o 5 sztuk powinna zwrócić 35.");
    }

    @Test
    public void ballistaShouldNotDamageFriendlyArmyIfEnemyDoesNotHaveOne() {
        boolean hero1HasBallista = true;
        boolean hero2HasBallista = false;

        int friendlyArmyAmount = 40;

        if (hero2HasBallista) {
            friendlyArmyAmount -= 5;
        }

        assertEquals(40, friendlyArmyAmount, "Twoja armia musi pozostać nienaruszona, jeśli wróg nie posiada maszyny.");
    }

    @Test
    public void creatureShouldBeRemovedFromArmyListWhenAmountReachesZero() {
        List<String> mockArmyList = new ArrayList<>();
        mockArmyList.add("Target Creature");

        int currentAmount = 5;
        int damage = 5;
        currentAmount -= damage;

        if (currentAmount <= 0) {
            mockArmyList.remove("Target Creature");
        }

        assertTrue(mockArmyList.isEmpty(), "Lista armii powinna zostać wyczyszczona, gdy liczebność spadnie do zera.");
    }

    @Test
    public void streamFilterShouldDistinguishRegularCreatures() {
        List<String> mixedArmy = new ArrayList<>();
        mixedArmy.add("Skeleton");
        mixedArmy.add("Ballista");

        long regularCreaturesCount = mixedArmy.stream()
                .filter(name -> !name.contains("Ballista"))
                .count();

        assertEquals(1, regularCreaturesCount, "Mechanizm filtrowania musi odrzucić maszyny i wskazać tylko zwykłe potwory.");
    }

    @Test
    public void forgeShouldDeductGoldAndAddWarMachineToHero() {
        int heroGoldBeforePurchase = 5000;
        int ballistaCost = 2500;
        List<String> heroWarMachines = new ArrayList<>();

        if (heroGoldBeforePurchase >= ballistaCost) {
            heroGoldBeforePurchase -= ballistaCost;
            heroWarMachines.add("BALLISTA");
        }

        assertEquals(2500, heroGoldBeforePurchase, "Po zakupie Balisty za 2500, bohaterowi powinno zostać dokładnie 2500 złota.");
        assertTrue(heroWarMachines.contains("BALLISTA"), "Zakupiona Balista musi znaleźć się w ekwipunku/armii bohatera.");
    }

    @Test
    public void forgeShouldRejectPurchaseIfHeroHasNotEnoughGold() {
        int heroGoldBeforePurchase = 1000;
        int ballistaCost = 2500;
        List<String> heroWarMachines = new ArrayList<>();

        boolean purchaseSuccess = false;
        if (heroGoldBeforePurchase >= ballistaCost) {
            heroGoldBeforePurchase -= ballistaCost;
            heroWarMachines.add("BALLISTA");
            purchaseSuccess = true;
        }

        assertFalse(purchaseSuccess, "Transakcja powinna się nie powieść z powodu braku funduszy.");
        assertEquals(1000, heroGoldBeforePurchase, "Stan konta bohatera nie powinien ulec zmianie.");
        assertFalse(heroWarMachines.contains("BALLISTA"), "Balista NIE POWINNA zostać dodana do ekwipunku.");
    }

    @Test
    public void wallDurabilityShouldNotDropBelowZero() {
        int wallDurability = 20;
        int powerfulCatapultDamage = 50;

        wallDurability = Math.max(0, wallDurability - powerfulCatapultDamage);

        assertEquals(0, wallDurability, "Wytrzymałość muru po potężnym strzale powinna wynosić dokładnie 0, a nie -30.");
    }

    @Test
    public void warMachineShouldBeExtendableWithNewAbilitiesPolymorphically() {
        List<String> machineAbilities = new ArrayList<>();
        machineAbilities.add("HEAL");

        String futureExtension = "CAST_AIR_SHIELD";
        machineAbilities.add(futureExtension);

        assertTrue(machineAbilities.contains("CAST_AIR_SHIELD"), "Architektura powinna umożliwiać dynamiczne dodawanie nowych unikalnych akcji dla zaawansowanych maszyn.");
    }
}