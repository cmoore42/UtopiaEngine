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
import static utopiaengine.Construct.CHASSIS;
import utopiaengine.Game;
import static utopiaengine.Location.WORKSHOP;
import utopiaengine.Monster;
import static utopiaengine.Tool.WAND;
import static utopiaengine.Treasure.PLATE;
import static utopiaengine.Treasure.SHARD;
import utopiaengine.actions.Action;
import static utopiaengine.actions.Action.EventType.END_OF_WORLD;
import static utopiaengine.actions.Action.EventType.TRAVEL;
import utopiaengine.actions.ActionListener;

/**
 *
 * @author cmoore
 */
public class CombatDialog extends Dialog<Integer> implements ActionListener {
    private Label resultLabel;
    private ButtonType doneButton;
    private Monster opponent;
    private boolean wandUsed = false;
    private Button rollButton;
    private Button wandButton;
    
    public CombatDialog(int combatLevel) {
        setTitle("Combat");
        
        
        opponent = Game.getPlayer().getLocation().getMonster(combatLevel);
        setHeaderText("Encounter with " + opponent.getName() + " (level " + combatLevel + ")");
        
        VBox outer = new VBox();
        
        HBox upper = new DiceUi();
        
        resultLabel = new Label();
        rollButton = new Button("Roll");
        
        wandButton = new Button("Use Paralysis Wand");
        if (WAND.isCharged()) {
            wandButton.setDisable(false);
        } else {
            wandButton.setDisable(true);
        }
        wandButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                Game.info("You activate the Paralysis Wand.");
                wandUsed = true;
                WAND.setCharged(false);
                wandButton.setDisable(true);
            }
            
        });
        
        if (PLATE.isFound() && (opponent.getAttack() > 1)) {
            Game.info("The Ice Plate makes you harder to hit.");
        }
        
        if (SHARD.isFound()) {
            Game.info("The Molten Shard makes the mosnter more vulnerable.");
        }
        
        if (CHASSIS.isActivated() && opponent.isSpirit()) {
            Game.info("The Golden Chassis makes your attacks more effective.");
        }
        
        rollButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                StringBuilder resultString = new StringBuilder();
                int hitMe = 0;
                boolean killedIt = false;
                
                resultString.append("Result: ");
                Game.getDice().roll();
                int die1 = Game.getDice().getDie(1);
                int die2 = Game.getDice().getDie(2);
                int opponentAttack = opponent.getAttack();
                int opponentHit = opponent.getHit();
                
                if (PLATE.isFound() && (opponentAttack > 1)) {
                    --opponentAttack;  
                }
                
                if (SHARD.isFound()) {
                    --opponentHit;
                }
                
                if (wandUsed) {
                    die1 += 2;
                    
                    die2 += 2;
                    
                }
                
                if (CHASSIS.isActivated() && opponent.isSpirit()) {
                    ++die1;
                    ++die2;
                }
                
                if (die1 > 6) {
                    die1 = 6;
                }
                
                if (die2 > 6) {
                    die2 = 6;
                }
                
                if (die1 <= opponentAttack) {
                    /* It hit us */
                    Game.getPlayer().dealDamage();
                    ++hitMe;
                }
                if (die2 <= opponentAttack) {
                    /* It hit us */
                    Game.getPlayer().dealDamage();
                    ++hitMe;
                }
                if (die1 >= opponentHit) {
                    /* We killed it */
                    killedIt = true;
                }
                if (die2 >= opponentHit) {
                    /* We killed it */
                    killedIt = true;
                }
                
                if (Game.getPlayer().isDead()) {
                    resultLabel.setText("You've died!");
                    getDialogPane().lookupButton(doneButton).setDisable(false);
                    rollButton.setDisable(true);
                    Game.info("You're dead!");
                    return;
                }
                
                if (Game.getPlayer().isUnconsious()) {
                    resultLabel.setText("You're unconscious.");
                    Game.postAction(new Action(TRAVEL, WORKSHOP));
                    getDialogPane().lookupButton(doneButton).setDisable(false);
                    rollButton.setDisable(true);
                    Game.info("You're unconscious.");
                    return;
                }
                
                if ((hitMe == 0) && (!killedIt)) {
                    resultLabel.setText("Result: no change");
                } else {
                    switch(hitMe) {
                        case 0:
                            resultString.append("Killed it.");
                            Game.info("You killed it.");
                            break;
                        case 1:
                            resultString.append("Hit me once");
                            Game.info("The monster hit you.");
                            break;
                        case 2:
                            resultString.append("Hit me twice");
                            Game.info("The monster hit you twice!");
                            break;
                    }
                    
                    if (killedIt && (hitMe > 0)) {
                        resultString.append(", killed it.");
                    } else {
                        resultString.append(".");
                    }
                    
                    resultLabel.setText(resultString.toString());
                    
                    if (killedIt) {
                        getDialogPane().lookupButton(doneButton).setDisable(false);
                        rollButton.setDisable(true);
                        wandButton.setDisable(true);
                    }
                }
                
            }
            
        });
        
        outer.getChildren().addAll(upper, resultLabel, rollButton, wandButton);
        
        doneButton = new ButtonType("Done", ButtonBar.ButtonData.OK_DONE);
        
        getDialogPane().getButtonTypes().add(doneButton);
        getDialogPane().setContent(outer);
        getDialogPane().lookupButton(doneButton).setDisable(true);
        
        setResultConverter(new Callback<ButtonType, Integer>() {

            @Override
            public Integer call(ButtonType param) {

                
                return 0;
                
            }
            
        });
    }

    @Override
    public void handleAction(Action a) {
        if (a.getType() == END_OF_WORLD) {
            getDialogPane().lookupButton(doneButton).setDisable(false);
            rollButton.setDisable(true);
            wandButton.setDisable(true);
        }
    }
    
}
