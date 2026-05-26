package pl.psi;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class BallistaTest {

    @Test
    void heroShouldBeAbleToHaveBallistaSkillLevel() {
        // Given
        Hero hero = new Hero(new ArrayList<>(), new ArrayList<>());

        // When & Then
        // Chcemy, aby domyślnie bohater miał 0 poziom umiejętności Artylerii
        assertEquals(0, hero.getSkillLevel("ARTILLERY"));

        // Chcemy mieć możliwość ustawienia poziomu tego skilla
        hero.setSkillLevel("ARTILLERY", 3);
        assertEquals(3, hero.getSkillLevel("ARTILLERY"));
    }
}
