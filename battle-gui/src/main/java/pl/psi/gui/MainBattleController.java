package pl.psi.gui;

import com.google.common.collect.BiMap;
import javafx.fxml.FXML;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import pl.psi.GameEngine;
import pl.psi.Hero;
import pl.psi.gui.proxy.GameEngineProxy;
import pl.psi.BattlePoint;
import pl.psi.SpecialField;
import pl.psi.BattleResults.BattleResult;
import pl.psi.creatures.Creature;
import pl.psi.gui.SpellGUI.SpellCastingManager;
import pl.psi.gui.SpellGUI.SpellUIManager;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class MainBattleController implements PropertyChangeListener {
    private final GameEngineProxy gameEngine;
    private final Consumer<BattleResult> battleFinishedHandler;
    private final SpellCastingManager spellManager = new SpellCastingManager();
    private SpellUIManager spellUIManager;

    @FXML
    private GridPane gridMap;
    @FXML
    private Button passButton;
    @FXML
    private Button spellButton;

    public MainBattleController(final Hero aHero1, final Hero aHero2, final Map<BattlePoint, Creature> bankEnemy,
                                final BiMap<BattlePoint, SpecialField> aSpecialField,
                                final Consumer<BattleResult> aBattleFinishedHandler) {
        gameEngine = new GameEngineProxy(aHero1, aHero2, aSpecialField, bankEnemy);
        battleFinishedHandler = aBattleFinishedHandler;
    }

    @FXML
    private void initialize() {
        spellUIManager = new SpellUIManager(gameEngine, spellManager, this::refreshGui);

        refreshGui();
        gameEngine.addObserver(this);

        passButton.setOnMouseClicked(e -> pass());

        if (spellButton != null) {
            spellButton.setOnMouseClicked(e -> spellUIManager.openSpellDialog());
        }
    }

    private void refreshGui() {
        gridMap.getChildren().clear();

        // Bezpieczne pobranie list, na wypadek gdyby silnik zwrócił null
        java.util.List<pl.psi.creatures.Creature> h1Creatures =
                (gameEngine.getHero1() != null) ? gameEngine.getHero1().getCreatures() : new java.util.ArrayList<>();
        java.util.List<pl.psi.creatures.Creature> h2Creatures =
                (gameEngine.getHero2() != null) ? gameEngine.getHero2().getCreatures() : new java.util.ArrayList<>();

        for (int x = 0; x < 15; x++) {
            for (int y = 0; y < 10; y++) {
                BattlePoint currentBattlePoint = new BattlePoint(x, y);
                Optional<Creature> creature = gameEngine.getCreature(currentBattlePoint);

                final MapTile mapTile = new MapTile("");
                StringBuilder tileText = new StringBuilder();

                // 1. Sprawdzenie standardowe z silnika bitwy
                if (creature.isPresent()) {
                    Creature c = creature.get();
                    try {
                        tileText.append(c.toString() != null ? c.toString() : "Creature");
                    } catch (Exception e) {
                        tileText.append("Creature");
                    }
                }

                // 2. Przypisanie awaryjne tylko dla ŻYWYCH indeksów
                if (tileText.toString().isEmpty()) {
                    // --- NASZ BOHATER (LEWA STRONA) ---
                    if (x == 0) {
                        if (y == 9) {
                            if (gameEngine.getHero1() != null && gameEngine.getHero1().hasBallista()) {
                                tileText.append("Ballista");
                                mapTile.setBackground(Color.LIGHTBLUE);
                            }
                        } else if (y < h1Creatures.size() && h1Creatures.get(y) != null) {
                            tileText.append(h1Creatures.get(y).toString());
                            mapTile.setBackground(Color.LIGHTBLUE);
                        }
                    }
                    // --- PRZECIWNIK (PRAWA STRONA) ---
                    else if (x == 14) {
                        if (y == 9) {
                            if (gameEngine.getHero2() != null && gameEngine.getHero2().hasBallista()) {
                                tileText.append("Ballista");
                                mapTile.setBackground(Color.LIGHTCORAL);
                            }
                        } else if (y < h2Creatures.size() && h2Creatures.get(y) != null) {
                            tileText.append(h2Creatures.get(y).toString());
                            mapTile.setBackground(Color.LIGHTCORAL);
                        }
                    }
                }

                if (!tileText.toString().isEmpty()) {
                    mapTile.setName(tileText.toString());
                }

                // Bezpieczne sprawdzanie akcji (otoczone blokiem try-catch, aby żaden pojedynczy kafelek nie wysadził całej planszy)
                try {
                    if (gameEngine.isCurrentCreature(currentBattlePoint)) {
                        mapTile.setBackground(Color.GREENYELLOW);
                    }
                    if (spellManager.isActive()) {
                        SpellTargetingUI.attachTargeting(mapTile, currentBattlePoint, spellManager, spellUIManager, gridMap);
                    }
                    if (gameEngine.canMove(currentBattlePoint)) {
                        mapTile.setBackground(Color.GREY);
                        mapTile.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
                            gameEngine.move(currentBattlePoint);
                            refreshGui();
                        });
                    }
                    if (gameEngine.canAttack(currentBattlePoint)) {
                        mapTile.setBackground(Color.RED);
                        mapTile.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
                            gameEngine.attack(currentBattlePoint);
                            refreshGui();
                        });
                    }
                } catch (Exception e) {
                    // Ignorujemy błędy sprawdzania zasięgu dla maszyn wojennych, które nie stoją fizycznie na planszy
                }

                // Pola specjalne
                if (gameEngine.getSpecialFields() != null) {
                    SpecialField specialField = gameEngine.getSpecialFields().get(currentBattlePoint);
                    if (specialField != null) {
                        mapTile.setBackground(getColor(specialField));
                        mapTile.setName(getFieldName(specialField).toString());
                        mapTile.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
                            gameEngine.interact(currentBattlePoint);
                            refreshGui();
                        });
                    }
                }

                gridMap.add(mapTile, x, y);
            }
        }
    }

    private Color getColor(SpecialField specialField) {
        if (specialField.getColor() == SpecialField.Color.BROWN) {
            return Color.BROWN;
        } else if (specialField.getColor() == SpecialField.Color.CYAN) {
            return Color.CYAN;
        } else if (specialField.getColor() == SpecialField.Color.YELLOW) {
            return Color.YELLOW;
        } else if (specialField.getColor() == SpecialField.Color.ORANGE) {
            return Color.ORANGE;
        } else if (specialField.getColor() == SpecialField.Color.GRAY) {
            return Color.GRAY;
        }
        return null;
    }

    private SpecialField.FieldName getFieldName(SpecialField specialField) {
       return specialField.getFieldName();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("SPELL_CAST".equals(evt.getPropertyName())) {
            spellUIManager.showSpellCastDialog();
        } else if (GameEngine.BATTLE_FINISHED.equals(evt.getPropertyName())) {
            handleBattleFinished();
            return;
        }

        // Wymuszamy odświeżenie GUI przy każdej zmianie stanu w silniku gry tez mizninoe tutaj
        Platform.runLater(this::refreshGui);
    }

    private void pass() {
        // 1. Sprawdzamy, czy jednostka mająca teraz ruch to maszyna wojenna
        boolean isWarMachine = gameEngine.isCurrentCreatureWarMachine();

        // 2. Przekazujemy turę w silniku bitwy
        gameEngine.pass();

        // 3. Logika bezpiecznego, symetrycznego ostrzału
        if (isWarMachine) {
            System.out.println("====== SYSTEM MASZYN WOJENNYCH: Aktywacja ostrzału ======");

            try {
                java.util.List<pl.psi.creatures.Creature> h1Creatures = gameEngine.getHero1().getCreatures();
                java.util.List<pl.psi.creatures.Creature> h2Creatures = gameEngine.getHero2().getCreatures();

                // Sprawdzamy fizyczną obecność Balisty na listach armii zamiast metodą hasBallista()
                boolean hero1PosiadaBaliste = h1Creatures.stream()
                        .anyMatch(c -> c instanceof pl.psi.warmachines.WarMachineDecorator);

                boolean hero2PosiadaBaliste = h2Creatures.stream()
                        .anyMatch(c -> c instanceof pl.psi.warmachines.WarMachineDecorator);

                // Filtrujemy listy, aby celować tylko w prawdziwe potwory (Szkielety itp.)
                java.util.List<pl.psi.creatures.Creature> h1Targets = h1Creatures.stream()
                        .filter(c -> !(c instanceof pl.psi.warmachines.WarMachineDecorator))
                        .filter(c -> c.getAmount() > 0)
                        .collect(java.util.stream.Collectors.toList());

                java.util.List<pl.psi.creatures.Creature> h2Targets = h2Creatures.stream()
                        .filter(c -> !(c instanceof pl.psi.warmachines.WarMachineDecorator))
                        .filter(c -> c.getAmount() > 0)
                        .collect(java.util.stream.Collectors.toList());

                // --- STRZAŁ BALISTY GRACZA 1 (Zada obrażenia tylko, jeśli Gracz 1 ma ją na liście) ---
                if (hero1PosiadaBaliste) {
                    if (!h2Targets.isEmpty()) {
                        pl.psi.creatures.Creature target = h2Targets.get(0);
                        int currentAmount = target.getAmount();
                        int newAmount = Math.max(0, currentAmount - 5);
                        target.setAmount(newAmount);
                        System.out.println("Balista Gracza 1 rani " + target.getName() + ". Zostało: " + newAmount);

                        if (newAmount <= 0) {
                            gameEngine.getHero2().getCreatures().remove(target);
                        }
                    }
                } else {
                    System.out.println("Gracz 1 fizycznie nie posiada obiektu Balisty w armii.");
                }

                // --- STRZAŁ BALISTY GRACZA 2 (Zada obrażenia tylko, jeśli Gracz 2 ma ją na liście) ---
                if (hero2PosiadaBaliste) {
                    if (!h1Targets.isEmpty()) {
                        pl.psi.creatures.Creature target = h1Targets.get(0);
                        int currentAmount = target.getAmount();
                        int newAmount = Math.max(0, currentAmount - 5);
                        target.setAmount(newAmount);
                        System.out.println("Balista Gracza 2 rani " + target.getName() + ". Zostało: " + newAmount);

                        if (newAmount <= 0) {
                            gameEngine.getHero1().getCreatures().remove(target);
                        }
                    }
                } else {
                    System.out.println("Gracz 2 fizycznie nie posiada obiektu Balisty w armii.");
                }

            } catch (Exception e) {
                System.out.println("Błąd podczas walki maszyn: " + e.getMessage());
            }
        }

        // 4. Przerysowujemy interfejs graficzny
        refreshGui();
    }

    private void handleBattleFinished() {
        if (!gameEngine.isBattleOver()) {
            return;
        }
        gameEngine.getBattleResult().ifPresent(battleFinishedHandler);
        closeBattleWindow();
    }

    private void closeBattleWindow() {
        Platform.runLater(() -> {
            if (gridMap != null && gridMap.getScene() != null && gridMap.getScene().getWindow() != null) {
                gridMap.getScene().getWindow().hide();
            }
        });
    }
}
