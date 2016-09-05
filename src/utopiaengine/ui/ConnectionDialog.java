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
import utopiaengine.actions.EndOfWorldAction;

/**
 *
 * @author moorechr
 */
public class ConnectionDialog extends Dialog<Integer> implements ActionListener {
    private Connection connection;
    private ButtonType doneButton;
    private Button[] rollButtons;
    private Label[] results;
    private int currentDie;
    private Button wasteButton;
    
    public ConnectionDialog(Connection connection) {
        setTitle("Connect");
        this.connection = connection;
        
        VBox outer = new VBox();
        DiceUi dice = new DiceUi();
        
        rollButtons = new Button[6];
        results = new Label[3];
        
        HBox middle = new HBox();
        for (int i=0; i<3; i++) {
            VBox inner = new VBox();
            rollButtons[2*i] = new Button(" ");
            rollButtons[2*i+1] = new Button(" ");
            results[i] = new Label(" ");
            inner.getChildren().addAll(rollButtons[2*i], rollButtons[2*i+1], results[i]);
            middle.getChildren().add(inner);
        }
        
        for (int i=0; i<6; i++) {
            rollButtons[i].setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    Button source = (Button)event.getSource();
                    
                    source.setText(Integer.toString(Game.getDice().getDie(currentDie)));
                    if (currentDie == 1) {
                        currentDie = 2;
                    } else {
                        Game.getDice().roll();
                        currentDie = 1;
                    }
                    source.setDisable(true);
                    dice.highlight(currentDie);
                    
                    if (updateDisplay()) {
                        wasteButton.setDisable(true);
                    }
                }
            });
        }
        
        wasteButton = new Button("Throw Away");
        wasteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Game.getWasteBasket().throwAway(Game.getDice().getDie(currentDie));
                if (Game.getWasteBasket().isFull()) {
                    wasteButton.setDisable(true);
                }
                if (currentDie == 1) {
                    currentDie = 2;
                } else {
                    Game.getDice().roll();
                    currentDie = 1;
                }
                dice.highlight(currentDie);
            }
            
        });
        
        outer.getChildren().addAll(dice, middle, wasteButton);
        
        doneButton = new ButtonType("Done", ButtonBar.ButtonData.OK_DONE);
        
        getDialogPane().getButtonTypes().add(doneButton);
        getDialogPane().setContent(outer);
        
        setResultConverter(new Callback<ButtonType, Integer>() {

            @Override
            public Integer call(ButtonType param) {
                
                return checkResults();
            }
            
        });
        
        Game.getDice().roll();
        dice.highlight(1);
        currentDie = 1;
    }
    
    private boolean updateDisplay() { 
        int columnsFilled = 0;
        
        for (int col=0; col<3; col++) {
            if (!(results[col].getText().equals(" "))) {
                /* This column already done */
                ++columnsFilled;
                continue;
            }
            try {
                int top = Integer.parseInt(rollButtons[2*col].getText());
                int bottom = Integer.parseInt(rollButtons[2*col+1].getText());
                int diff = top - bottom;
                if (diff < 0) {
                    Game.info("Component vaporized");
                    Game.getPlayer().dealDamage();
                    /* If the player has another component, spend it and continue */
                    if (connection.getComponent().getQuantity() != 0) {
                        Game.info("You spend another " + connection.getComponent().getName() + " to continue.");
                        connection.getComponent().decrement();
                        diff = 2;
                    } else {
                        Game.info("You are out of " + connection.getComponent().getName() + " and can't continue.");
                        for (int i=0; i<6; i++) {
                            rollButtons[i].setDisable(true);
                        }
                        break;
                    }
                }
                results[col].setText(Integer.toString(diff));
                ++columnsFilled;
            } catch (NumberFormatException e) {
                /* NumberFormatException just means one of the two buttons
                 * in the column doesn't have a value yet, so we just do
                 * nothing here.
                 * Good programming practice suggests that I test for
                 * the condition rather than relying on the exception,
                 * but I'm lazy.
                 */
            }
        }
        
        return (columnsFilled > 2);

    }
    
    private int checkResults() {
        int total = 0;
        int columnsFilled = 0;
        
        for (int col=0; col<3; col++) {
            if (results[col].getText().equals(" ")) {
                continue;
            }
            ++columnsFilled;
            total += Integer.parseInt(results[col].getText());
        }
        
        if (columnsFilled == 3) {
            return total;
        } else {
            return -1;
        }
    }

    @Override
    public void handleAction(Action a) {
        if (a instanceof EndOfWorldAction) {
            for (int i=0; i<6; i++) {
                rollButtons[i].setDisable(true);
            }
            wasteButton.setDisable(true);
        }
    }
    
}
