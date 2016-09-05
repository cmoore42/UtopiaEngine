/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utopiaengine.ui;

import java.util.ArrayList;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import utopiaengine.Component;
import utopiaengine.Game;
import utopiaengine.actions.Action;
import utopiaengine.actions.ActionListener;
import utopiaengine.actions.StoresChangedAction;

/**
 *
 * @author moorechr
 */
public class StoresUi extends VBox implements ActionListener {
    private Label title;
    private ArrayList<StoresDisplay> displays;
    
    private class StoresDisplay extends HBox {
        private Component component;
        private Label label;
        private ProgressBar progressBar;
        
        public StoresDisplay(Component component) {
            this.component = component;
            this.label = new Label(component.getName() + ": 0/4");
            label.setPrefWidth(100);
            this.progressBar = new ProgressBar(0f);
            
            
            getChildren().addAll(this.label, this.progressBar);
        }
        
        public void update() {
            int qty = component.getQuantity();
            double d_qty = (double)(qty);
            double percent = d_qty / 4.0;
            
            label.setText(component.getName() + ": " + qty + "/4");
            progressBar.setProgress(percent);
        }
    }
    
    public StoresUi() {
        super();
        
        title = new Label("Stores");
        title.getStyleClass().add("box-title");
        
        displays = new ArrayList<>();
        
        for (Component c : Component.values()) {
            displays.add(new StoresDisplay(c));
        }
        
        getChildren().add(title);
        getChildren().addAll(displays);
        
        getStyleClass().add("boxed");
        setId("storesUi");
        
        Game.getInstance().addListener(this);
    }
    
    public void update() {
        for (StoresDisplay d : displays) {
            d.update();
        }
    }

    @Override
    public void handleAction(Action a) {
        if (a instanceof StoresChangedAction) {
            update();
            
        }
    }
    
}
