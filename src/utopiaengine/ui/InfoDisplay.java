/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utopiaengine.ui;

import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import utopiaengine.Game;
import utopiaengine.actions.Action;
import static utopiaengine.actions.Action.EventType.INFO;
import utopiaengine.actions.ActionListener;


/**
 *
 * @author moorechr
 */
public class InfoDisplay extends HBox implements ActionListener {
    private final TextArea ta;
    
    public InfoDisplay() {
        ta = new TextArea();
        
        ta.setWrapText(true);
        ta.setEditable(false);
        
        getStyleClass().add("boxed");
        
        getChildren().add(ta);
        
        Game.getInstance().addListener(this);
        
    }
    
    public void write(String line) {
        ta.appendText(line);
    }

    @Override
    public void handleAction(Action a) {
        if (a.getType() == INFO) {
            write(a.getText() + "\n");
        }
    }
    
}
