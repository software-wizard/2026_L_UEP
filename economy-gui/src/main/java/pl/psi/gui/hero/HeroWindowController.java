package pl.psi.gui.hero;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import pl.psi.hero.EconomyHero;
import pl.psi.hero.skills.AbstractSkill;

import java.util.stream.Collectors;

public class HeroWindowController {

    @FXML private Label levelLabel;
    @FXML private Label experienceLabel;
    @FXML private Label attackLabel;
    @FXML private Label defenseLabel;
    @FXML private Label powerLabel;
    @FXML private Label knowledgeLabel;
    @FXML private Label movementLabel;
    @FXML private ListView<String> skillsListView;

    public void setHero(EconomyHero hero) {
        levelLabel.setText(String.valueOf(hero.getLevel()));
        experienceLabel.setText(String.valueOf(hero.getExperience()));
        attackLabel.setText(String.valueOf(hero.getAttack()));
        defenseLabel.setText(String.valueOf(hero.getDefense()));
        powerLabel.setText(String.valueOf(hero.getPower()));
        knowledgeLabel.setText(String.valueOf(hero.getKnowledge()));
        movementLabel.setText(hero.getRemainingMoveRange() + " / " + hero.getMoveRange());

        skillsListView.getItems().clear();

        for (AbstractSkill skill : hero.getSkills()) {
            skillsListView.getItems().add(
                    skill.getName().toString() + " - " + skill.getLevel().toString()
            );
        }
    }
}
