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
import utopiaengine.Component;
import utopiaengine.Construct;
import utopiaengine.Game;
import utopiaengine.Treasure;
import utopiaengine.actions.Action;
import utopiaengine.actions.ActionListener;

/**
 *
 * @author moorechr
 */
public class TestDialog extends Dialog<Integer> implements ActionListener {
    private HBox mainPane;
    private ButtonType doneButton;
    private VBox componentBox;
    private VBox constructBox;
    private VBox treasureBox;
    private VBox otherBox;
    
    public TestDialog() {
        setTitle("Test");
        
        mainPane = new HBox();
        
        componentBox = new VBox();        
        for (Component c : Component.values()) {
            HBox container = new HBox();
            container.getChildren().add(new Label(c.getName()));
            
            Button findButton = new Button("Find");
            findButton.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    c.increment();
                }
                
            });
            container.getChildren().add(findButton);
            
            componentBox.getChildren().add(container);
        }
        componentBox.setStyle("-fx-border-color: black");
                
        constructBox = new VBox();
        for (Construct c : Construct.values()) {
            HBox container = new HBox();
            container.getChildren().add(new Label(c.getName()));
            
            Button findButton = new Button("Find");
            findButton.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    c.setFound();
                }
                
            });
            container.getChildren().add(findButton);
            
            Button activateButton = new Button("Activate");
            activateButton.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    c.activate();
                }
                
            });
            container.getChildren().add(activateButton);
            
            constructBox.getChildren().add(container);
        }
        constructBox.setStyle("-fx-border-color: black");
        
        treasureBox = new VBox();
        for (Treasure c : Treasure.values()) {
            HBox container = new HBox();
            container.getChildren().add(new Label(c.getName()));
            
            Button findButton = new Button("Find");
            findButton.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    c.setFound();
                }
                
            });
            container.getChildren().add(findButton);
            
            treasureBox.getChildren().add(container);
        }
        treasureBox.setStyle("-fx-border-color: black");
        
        otherBox = new VBox();
        Button damageButton = new Button("Damage");
        damageButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                Game.getPlayer().dealDamage();
            }
            
        });
        Button healButton = new Button("Heal");
        healButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                Game.getPlayer().heal();
            }
            
        });
        Button timeButton = new Button("Time Tick");
        timeButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                Game.getTimeTrack().tick();
            }
            
        });
        Button addPowerButton = new Button("Power Up God's Hand");
        addPowerButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                Game.getTimeTrack().powerGodsHand();
            }
            
        });
        Button delPowerButton = new Button("Poser Down God's Hand");
        delPowerButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

            }
            
        });
        Button delayButton = new Button("Delay Doomsday");
        delayButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                Game.getTimeTrack().extendDay();
            }
            
        });
        otherBox.getChildren().addAll(damageButton, healButton, timeButton, addPowerButton, delPowerButton, delayButton);
        
        
        doneButton = new ButtonType("Done", ButtonBar.ButtonData.OK_DONE);
        
        mainPane.getChildren().addAll(componentBox, constructBox, treasureBox, otherBox);
        getDialogPane().getButtonTypes().add(doneButton);
        getDialogPane().setContent(mainPane);
        
        setResultConverter(new Callback<ButtonType, Integer>() {

            @Override
            public Integer call(ButtonType param) {
                return 0;
            }
            
        });
        
    }

    @Override
    public void handleAction(Action a) {
    }
    
}
