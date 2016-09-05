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
import utopiaengine.actions.ActionListener;
import utopiaengine.actions.InfoAction;

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
        if (a instanceof InfoAction) {
            InfoAction i = (InfoAction) a;
            write(i.getLine() + "\n");
        }
    }
    
}
