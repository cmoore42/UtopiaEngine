/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utopiaengine.ui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import static utopiaengine.Construct.BATTERY;
import utopiaengine.Game;
import utopiaengine.Tool;
import utopiaengine.actions.Action;
import utopiaengine.actions.ActionListener;
import utopiaengine.actions.ConstructChangedAction;
import utopiaengine.actions.ToolChangedAction;

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
        if ((a instanceof ToolChangedAction) || 
            (a instanceof ConstructChangedAction)) {
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
