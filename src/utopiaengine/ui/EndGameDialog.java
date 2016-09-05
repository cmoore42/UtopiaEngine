/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utopiaengine.ui;

import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;

/**
 *
 * @author moorechr
 */
public class EndGameDialog extends Dialog<Integer> {
    private TextArea ta;
    private ButtonType doneButton;
    
    public EndGameDialog(String text) {
        ta = new TextArea();
        
        ta.setWrapText(true);
        ta.setEditable(false);
        
        ta.getStyleClass().add("boxed");
        
        ta.setText(text);
        
        doneButton = new ButtonType("Done", ButtonBar.ButtonData.OK_DONE);
        
        getDialogPane().getButtonTypes().add(doneButton);
        getDialogPane().setContent(ta);

    }
    
}
