package pl.psi.gui.hero;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import pl.psi.hero.EconomyHero;
import pl.psi.hero.skills.AbstractSkill;

import java.util.Collections;
import java.util.List;

public class SkillChoiceController {

    @FXML private Button skill1Button;
    @FXML private Button skill2Button;

    private EconomyHero hero;
    private List<AbstractSkill> options;

    public void init(EconomyHero hero) {
        this.hero = hero;
        List<AbstractSkill> possible = hero.getPossibleSkills();
        Collections.shuffle(possible);
        this.options = possible.subList(0, Math.min(2, possible.size()));

        if (options.size() >= 1) {
            skill1Button.setText(getSkillLabel(options.get(0)));
        } else {
            skill1Button.setVisible(false);
        }

        if (options.size() >= 2) {
            skill2Button.setText(getSkillLabel(options.get(1)));
        } else {
            skill2Button.setVisible(false);
        }
    }

    private String getSkillLabel(AbstractSkill skillTemplate) {
        // Find if hero already has it
        return hero.getSkills().stream()
                .filter(s -> s.getName().equals(skillTemplate.getName()))
                .findFirst()
                .map(s -> "Upgrade " + s.getName() + " to " + getNextLevel(s.getLevel()))
                .orElse("Learn " + skillTemplate.getName() + " (BASIC)");
    }

    private String getNextLevel(pl.psi.hero.skills.SkillLevel current) {
        switch (current) {
            case BASIC: return "ADVANCED";
            case ADVANCED: return "EXPERT";
            default: return "MAXED";
        }
    }

    @FXML
    private void onSkill1Clicked() {
        hero.upgradeSkill(options.get(0));
        close();
    }

    @FXML
    private void onSkill2Clicked() {
        hero.upgradeSkill(options.get(1));
        close();
    }

    private void close() {
        ((Stage) skill1Button.getScene().getWindow()).close();
    }
}
