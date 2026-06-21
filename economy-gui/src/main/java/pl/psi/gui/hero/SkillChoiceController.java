package pl.psi.gui.hero;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import pl.psi.hero.EconomyHero;
import pl.psi.hero.skills.AbstractSkill;

import java.util.List;

public class SkillChoiceController {

    @FXML private Button skill1Button;
    @FXML private Button skill2Button;

    private EconomyHero hero;
    private List<AbstractSkill> options;
    private Runnable onSkillSelected;

    public void init(EconomyHero hero) {
        init(hero, null);
    }

    public void init(EconomyHero hero, Runnable onSkillSelected) {
        this.hero = hero;
        this.onSkillSelected = onSkillSelected;
        this.options = hero.getPossibleSkills();

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
        notifySkillSelected();
        close();
    }

    @FXML
    private void onSkill2Clicked() {
        hero.upgradeSkill(options.get(1));
        notifySkillSelected();
        close();
    }

    private void notifySkillSelected() {
        if (onSkillSelected != null) {
            onSkillSelected.run();
        }
    }

    private void close() {
        ((Stage) skill1Button.getScene().getWindow()).close();
    }
}
