/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utopiaengine.ui;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import static utopiaengine.Construct.BATTERY;
import static utopiaengine.Construct.SEAL;
import utopiaengine.Game;
import static utopiaengine.Location.WORKSHOP;
import utopiaengine.Treasure;
import utopiaengine.actions.Action;
import utopiaengine.actions.ActionListener;
import utopiaengine.actions.TreasureFoundAction;

/**
 *
 * @author moorechr
 */
public class TreasureUi extends VBox implements ActionListener {
    private VBox treasureBox;
    
    public TreasureUi() {
        Label title = new Label("Treasures");
        title.getStyleClass().add("box-title");
        
        treasureBox = new VBox();
        
        getChildren().addAll(title, treasureBox);
        

        getStyleClass().add("boxed");
        
        Game.getInstance().addListener(this);
        
        update();
    }
    
    
    private void update() {
        Label treasureLabel;
        
        treasureBox.getChildren().clear();
        for (Treasure t : Treasure.values()) {
            if (t.isFound()) {
                treasureLabel = new Label(t.getName());
                treasureLabel.getStyleClass().add("boxed");
                treasureLabel.getStyleClass().add("activated");
                treasureLabel.setTooltip(t.getTooltip());
                treasureBox.getChildren().add(treasureLabel);
            }
        }
    }

    @Override
    public void handleAction(Action a) {
        if (a instanceof TreasureFoundAction) {
            update();
        }
    }
    
}
