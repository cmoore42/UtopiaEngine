/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utopiaengine.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import static utopiaengine.Construct.GATE;
import utopiaengine.Game;
import utopiaengine.actions.Action;
import static utopiaengine.actions.Action.EventType.REST;
import utopiaengine.actions.ActionListener;

/**
 *
 * @author moorechr
 */
public class HealthUi extends HBox implements ActionListener {
    private Label label;
    private ProgressBar progressBar;
    private Button restButton;
    private Button recoverButton;
    private float damagePercent;
    
    public HealthUi() {
        super();
        
        damagePercent = 0f;
        
        label = new Label("Damage 0/6");
        progressBar = new ProgressBar(0f);
        restButton = new Button("Rest");
        restButton.setDisable(true);
        recoverButton = new Button("Recover (Rest 6)");
        recoverButton.setDisable(true);
        
        restButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Game.postAction(new Action(REST));
            }
            
        });
        
        recoverButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                int recoveryTime = 6;
                if (GATE.isFound()) {
                    recoveryTime = 4;
                }
                for (int i=0; i<recoveryTime; i++) {
                    Game.postAction(new Action(REST));
                }
            }
            
        });
        
        getChildren().addAll(label, progressBar, restButton, recoverButton);
        getStyleClass().add("boxed");
        
        Game.getInstance().addListener(this);
    }
    
    private void update() {
        float damage = Game.getPlayer().getDamage();
        damagePercent = damage / 6.0f;
        
        progressBar.setProgress(damagePercent);
        label.setText("Damage " + Game.getPlayer().getDamage() + "/6");
        
        if (GATE.isActivated()) {
            recoverButton.setText("Recover (Rest 4)");
        } else {
            recoverButton.setText("Recover (Rest 6)");
        }
        
        if (Game.getPlayer().getDamage() == 0) {
            restButton.setDisable(true);
        } else {
            restButton.setDisable(false);
        }
        
        if (Game.getPlayer().isUnconsious()) {
            recoverButton.setDisable(false);
        } else {
            recoverButton.setDisable(true);
        }
    }

    @Override
    public void handleAction(Action a) {
        switch(a.getType()) {
            case PLAYER_HEALTH_CHANGED:
                update();
                break;
            case END_OF_WORLD:
                restButton.setDisable(true);
                recoverButton.setDisable(true);
                break;

        }

    }

    
}
