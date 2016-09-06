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
import javafx.scene.control.TextArea;
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
    private TextArea statusBox;
    private DiceUi dice;
    private boolean chassisEffect = false;
    private int attack;
    private int hit;
    
    public CombatDialog(int combatLevel) {
        setTitle("Combat");
        
        
        opponent = Game.getPlayer().getLocation().getMonster(combatLevel);
        setHeaderText("Encounter with " + opponent.getName() + " (level " + combatLevel + ")");
        
        HBox outer = new HBox();
        
        VBox left = new VBox();
        
        statusBox = new TextArea();
        statusBox.setWrapText(true);
        statusBox.setEditable(false);
        
        postCombatInfo("Opponent: " + opponent.getName()+ "(Level " + combatLevel + ")");
        
        dice = new DiceUi();
        
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
                postCombatInfo("You activate the Paralysis Wand.");
                wandUsed = true;
                WAND.setCharged(false);
                wandButton.setDisable(true);
            }
            
        });
        
        attack = opponent.getAttack();
        hit = opponent.getHit();
        
        boolean goalsChanged = false;
        reportGoals(attack, hit);
        
        if (PLATE.isFound()) {
            postCombatInfo("The Ice Plate makes you harder to hit.");
            if (attack > 1) {
                --attack;
                goalsChanged = true;
            } else {
                postCombatInfo("But its attack was already 1.");
            }
            
        }
        
        if (SHARD.isFound()) {
            postCombatInfo("The Molten Shard makes the monster more vulnerable.");
            --hit;
            goalsChanged = true;
        }
        
        if (CHASSIS.isActivated() && opponent.isSpirit()) {
            chassisEffect = true;
            postCombatInfo("The Golden Chassis makes your attacks more effective.");
        }
        
        if (goalsChanged) {
            reportGoals(attack, hit);
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
                int origDie1 = die1;
                int origDie2 = die2;
                
                if (wandUsed) {
                    die1 += 2;
                    die2 += 2;
                }
                
                if (die1 > 6) {
                    die1 = 6;
                }
                
                if (die2 > 6) {
                    die2 = 6;
                }
                
                if (die1 != origDie1) {
                    postCombatInfo("The Paralysis Wand turns your " + origDie1 + " into " + die1);
                    origDie1 = die1;
                }
                
                if (die2 != origDie2) {
                    postCombatInfo("The Paralysis Wand turns your " + origDie2 + " into " + die2);
                    origDie2 = die2;
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
                
                if (die1 != origDie1) {
                    postCombatInfo("The Golden Chassis turns your " + origDie1 + " into " + die1);
                }
                
                if (die2 != origDie2) {
                    postCombatInfo("The Golden Chassis turns your " + origDie2 + " into " + die2);
                }
                
                int highlight = 0;
                if (die1 <= attack) {
                    /* It hit us */
                    Game.getPlayer().dealDamage();
                    ++hitMe;
                    highlight |= 1;
                }
                if (die2 <= attack) {
                    /* It hit us */
                    Game.getPlayer().dealDamage();
                    ++hitMe;
                    highlight |= 2;
                }
                if (die1 >= hit) {
                    /* We killed it */
                    killedIt = true;
                    highlight |=1 ;
                }
                if (die2 >= hit) {
                    /* We killed it */
                    killedIt = true;
                    highlight |= 2;
                }
                
                dice.highlight(highlight);
                
                if (Game.getPlayer().isDead()) {
                    resultLabel.setText("You've died!");
                    getDialogPane().lookupButton(doneButton).setDisable(false);
                    rollButton.setDisable(true);
                    postCombatInfo("You're dead!");
                    return;
                }
                
                if (Game.getPlayer().isUnconsious()) {
                    resultLabel.setText("You're unconscious.");
                    Game.postAction(new Action(TRAVEL, WORKSHOP));
                    getDialogPane().lookupButton(doneButton).setDisable(false);
                    rollButton.setDisable(true);
                    postCombatInfo("You're unconscious.");
                    return;
                }
                
                if ((hitMe == 0) && (!killedIt)) {
                    resultLabel.setText("Result: no change");
                } else {
                    switch(hitMe) {
                        case 0:
                            resultString.append("Killed it.");
                            postCombatInfo("You killed it.");
                            break;
                        case 1:
                            resultString.append("Hit me once");
                            postCombatInfo("The monster hit you.");
                            break;
                        case 2:
                            resultString.append("Hit me twice");
                            postCombatInfo("The monster hit you twice!");
                            break;
                    }
                    
                    if (killedIt && (hitMe > 0)) {
                        resultString.append(", killed it.");
                        postCombatInfo("You killed it");
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
        
        left.getChildren().addAll(dice, resultLabel, rollButton, wandButton);
        
        outer.getChildren().addAll(left, statusBox);
        
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
        
        Game.getInstance().addListener(this);
    }

    @Override
    public void handleAction(Action a) {
        switch (a.getType()) {
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
    
    private void postCombatInfo(String text) {
        statusBox.appendText(text + "\n");
    }
    
    private void reportGoals(int attack, int hit) {
        if (attack < 2) {
            postCombatInfo("It hits you on a roll of 1.");
        } else {
            postCombatInfo("It hits you on a roll of 1 to " + attack + ".");
        }
        
        if (hit > 5) {
            postCombatInfo("You kill it on a roll of 6.");
        } else {
            postCombatInfo("You kill it on a roll of " + hit + " to 6.");
        }
    }
    
}
