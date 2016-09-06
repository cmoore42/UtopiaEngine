/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utopiaengine.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import utopiaengine.Connection;
import utopiaengine.Game;
import utopiaengine.actions.Action;
import utopiaengine.actions.ActionListener;

/**
 *
 * @author moorechr
 */
public class FinalActivationDialog extends Dialog<Integer> implements ActionListener {
    private HBox outerBox;
    private VBox labelBox;
    private VBox diceBox;
    private Label difficultyLabel;
    private Label adjustmentLabel;
    private Label netDifficultyLabel;
    private Button rollButton;
    private Button spendHealthButton;
    private DiceUi dice;
    private ButtonType doneButton;
    
    private int initialDifficulty;
    private int healthAdjustment;
    private boolean successful;
    
    public FinalActivationDialog() {
        successful = false;
        
        for (Connection c : Connection.values()) {
            initialDifficulty += c.getCost();
        }
        
        outerBox = new HBox();
        
        labelBox = new VBox();
        difficultyLabel = new Label("Difficulty: " + initialDifficulty);
        
        healthAdjustment = 0;
        adjustmentLabel = new Label("Health Adjustment: " + healthAdjustment);
        
        netDifficultyLabel = new Label("Target number: " + (initialDifficulty - healthAdjustment));
        labelBox.getChildren().addAll(difficultyLabel, adjustmentLabel, netDifficultyLabel);
        
        diceBox = new VBox();
        dice = new DiceUi();
        rollButton = new Button("Roll");
        rollButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                Game.getDice().roll();
                int total = Game.getDice().getDie(1) + Game.getDice().getDie(2);
                if (total >= (initialDifficulty - healthAdjustment)) {
                    /* Win */
                    Game.info("You've activated the Utopia Engine!");
                    rollButton.setDisable(true);
                    spendHealthButton.setDisable(true);
                    getDialogPane().lookupButton(doneButton).setDisable(false);
                    successful = true;
                } else {
                    Game.getTimeTrack().tick();
                    Game.getPlayer().dealDamage();
                    if ((Game.getPlayer().isDead()) || (Game.getTimeTrack().outOfTime())) {
                        /* Lose */
                        rollButton.setDisable(true);
                        spendHealthButton.setDisable(true);
                        getDialogPane().lookupButton(doneButton).setDisable(false);
                        successful = false;
                    } else {
                        /* Still trying */
                    }
                }
            }
            
        });
        spendHealthButton = new Button("Spend Hit Point");
        spendHealthButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                Game.getPlayer().dealDamage();
                ++healthAdjustment;
                update();
            }
            
        });
        if (Game.getPlayer().getDamage() < 6) {
            spendHealthButton.setDisable(false);
        } else {
            spendHealthButton.setDisable(true);
        }
        diceBox.getChildren().addAll(dice, rollButton, spendHealthButton);
        
        outerBox.getChildren().addAll(diceBox, labelBox);
        
        doneButton = new ButtonType("Done", ButtonBar.ButtonData.OK_DONE);
        
        getDialogPane().getButtonTypes().add(doneButton);
        getDialogPane().lookupButton(doneButton).setDisable(true);
        getDialogPane().setContent(outerBox);
        
        setResultConverter(new Callback<ButtonType, Integer>() {

            @Override
            public Integer call(ButtonType param) {
                if (successful) {
                    return 1;
                } else {
                    return 0;
                }
            }
            
        });
        
    }

    @Override
    public void handleAction(Action a) {
        switch(a.getType()) {
            case END_OF_WORLD:
                close();
                break;
            case PLAYER_HEALTH_CHANGED:
                if (Game.getPlayer().isDead()) {
                    close();
                }
                break;              
        }
    }
    
    private void update() {
        adjustmentLabel.setText("Health Adjustment: " + healthAdjustment);
        netDifficultyLabel.setText("Target number: " + (initialDifficulty - healthAdjustment));
    }
    
}
