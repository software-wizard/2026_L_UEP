package pl.psi.map.buildings;

import lombok.Getter;
import pl.psi.hero.EconomyHero;
import pl.psi.hero.artifacts.EconomySpell;
import pl.psi.map.InteractableIf;
import pl.psi.map.buildings.enterAction.EnterAction;

import java.util.List;
import java.util.Random;

public class Shrine implements InteractableIf {

    @Getter
    private final int level;
    private final Random random = new Random();

    public Shrine(int level) {
        this.level = level;
    }

    public Shrine() {
        this.level = 1;
    }

    @Override
    public String getPath() {
        return "/objects/Artifact_Crown_of_the_Supreme_Magi.gif";
    }

    @Override
    public void endOfTurn() {
    }

    @Override
    public void generateResource() {
    }

    @Override
    public void generateUnits() {
    }

    @Override
    public void interact(EconomyHero hero) {
        String spellName = getRandomSpellName(level);
        hero.addSpell(new EconomySpell(spellName));
    }

    private String getRandomSpellName(int lvl) {
        if (lvl == 2) {
            List<String> spells = List.of("Speed Boost II", "Weakness II", "Slow II", "Fireball");
            return spells.get(random.nextInt(spells.size()));
        } else if (lvl == 3) {
            List<String> spells = List.of("Lightning Bolt", "Advanced Fire Wall");
            return spells.get(random.nextInt(spells.size()));
        } else {
            List<String> spells = List.of("Attack Boost I", "Speed Boost I", "Slow I", "Weakness I");
            return spells.get(random.nextInt(spells.size()));
        }
    }

    @Override
    public typeOfObject getTypeOfObject() {
        return typeOfObject.BUILDING;
    }

    @Override
    public EconomyHero getOwner() {
        return null;
    }

    @Override
    public EnterAction firstInteraction() {
        return null;
    }

    @Override
    public EnterAction secondInteraction() {
        return null;
    }

    @Override
    public void resetBuildingOption() {
    }
}
