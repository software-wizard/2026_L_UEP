package pl.psi.gui.hero;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import pl.psi.gui.WindowManager;
import pl.psi.hero.EconomyHero;
import pl.psi.hero.skills.AbstractSkill;
import pl.psi.hero.skills.HeroStatModifierManager;

public class HeroWindowController {
    private static final int DEMO_BASE_EXP_REWARD = 100;

    @FXML private Label levelLabel;
    @FXML private Label experienceLabel;
    @FXML private Label attackLabel;
    @FXML private Label defenseLabel;
    @FXML private Label powerLabel;
    @FXML private Label knowledgeLabel;
    @FXML private Label baseMovementLabel;
    @FXML private Label effectiveMovementLabel;
    @FXML private Label remainingMovesLabel;
    @FXML private Label baseExpRewardLabel;
    @FXML private Label effectiveExpRewardLabel;
    @FXML private ListView<String> skillsListView;
    @FXML private Button testLevelUpButton;

    private final HeroStatModifierManager heroStatModifierManager = new HeroStatModifierManager();
    private EconomyHero hero;

    public void setHero(EconomyHero hero) {
        this.hero = hero;
        refresh();
    }

    private void refresh() {
        levelLabel.setText(String.valueOf(hero.getLevel()));
        experienceLabel.setText(String.valueOf(hero.getExperience()));
        attackLabel.setText(String.valueOf(hero.getAttack()));
        defenseLabel.setText(String.valueOf(hero.getDefense()));
        powerLabel.setText(String.valueOf(hero.getPower()));
        knowledgeLabel.setText(String.valueOf(hero.getKnowledge()));
        baseMovementLabel.setText(String.valueOf(hero.getMoveRange()));
        effectiveMovementLabel.setText(String.valueOf(
                heroStatModifierManager.applyMovement(hero, hero.getMoveRange())
        ));
        remainingMovesLabel.setText(String.valueOf(hero.getRemainingMoveRange()));
        baseExpRewardLabel.setText(String.valueOf(DEMO_BASE_EXP_REWARD));
        effectiveExpRewardLabel.setText(String.valueOf(
                heroStatModifierManager.applyExperience(hero, DEMO_BASE_EXP_REWARD)
        ));

        skillsListView.getItems().clear();

        for (AbstractSkill skill : hero.getSkills()) {
            skillsListView.getItems().add(
                    skill.getName() + " " + skill.getLevel()
            );
        }
    }

    @FXML
    private void onTestLevelUpClicked() {
        // Demonstration mode: opens the normal secondary-skill choice without changing level thresholds.
        WindowManager.openSkillChoice(hero, this::refresh);
    }
}
