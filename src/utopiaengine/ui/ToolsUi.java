/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utopiaengine.ui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import static utopiaengine.Construct.BATTERY;
import utopiaengine.Game;
import utopiaengine.Tool;
import utopiaengine.actions.Action;
import static utopiaengine.actions.Action.EventType.CONSTRUCT_CHANGED;
import static utopiaengine.actions.Action.EventType.TOOL_CHANGED;
import utopiaengine.actions.ActionListener;

/**
 *
 * @author moorechr
 */
public class ToolsUi extends VBox implements ActionListener {
    private Label[] labels;
    private Button batteryButton;
    
    public ToolsUi() {
        labels = new Label[3];
        int i=0;
        
        for (Tool t : Tool.values()) {
            labels[i] = new Label(t.getName());
            labels[i].setTooltip(t.getTooltip());
            ++i;
        }
        
        batteryButton = new Button("Use Crystal Battery");
        batteryButton.setDisable(true);
        
        getChildren().addAll(labels);
        getChildren().add(batteryButton);
        
        update();
        
        getStyleClass().add("boxed");
        
        Game.getInstance().addListener(this);
    }

    @Override
    public void handleAction(Action a) {
        if ((a.getType() == TOOL_CHANGED) || (a.getType() == CONSTRUCT_CHANGED)) { 
            update();
        }
    }
    
    private void update() {
        int i = 0;
        
        for (Tool t : Tool.values()) {
            labels[i].setText(t.getName());
            if (t.isCharged()) {
                labels[i].getStyleClass().clear();
                labels[i].getStyleClass().add("boxed");
                labels[i].getStyleClass().add("activated");
            } else {
                labels[i].getStyleClass().clear();
                labels[i].getStyleClass().add("boxed");
                labels[i].getStyleClass().add("missing");
            }
            
            i++;
        }
        
        if (BATTERY.isActivated() && !BATTERY.isUsed()) {
            batteryButton.setDisable(false);
        } else {
            batteryButton.setDisable(true);
        }
    }
    
}
