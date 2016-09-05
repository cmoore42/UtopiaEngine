/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utopiaengine.ui;

import java.util.List;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import utopiaengine.Game;
import utopiaengine.actions.Action;
import static utopiaengine.actions.Action.EventType.WASTE_BASKET_CHANGED;
import utopiaengine.actions.ActionListener;

/**
 *
 * @author cmoore
 */
public class WasteBasketUi extends VBox implements ActionListener {
    private Label wasteLabel;
    private GridPane layout;
    private Label[] cells;
    
    public WasteBasketUi() {
        wasteLabel = new Label("Waste Basket");
        wasteLabel.getStyleClass().add("box-title");
        layout = new GridPane();
        cells = new Label[10];
        for (int i=0; i<10; i++) {
            cells[i] = new Label(" ");
            cells[i].setPrefSize(25.0, 25.0);
            int row = i / 5;
            int col = i % 5;
            cells[i].setStyle("-fx-border-color: black");
            layout.add(cells[i], col, row);
        }
        
        getStyleClass().add("boxed");
        getChildren().addAll(wasteLabel, layout);
        
        update();
        
        Game.getInstance().addListener(this);
        
    }

    @Override
    public void handleAction(Action a) {
        if (a.getType() == WASTE_BASKET_CHANGED) {
            update();
        }
    }
    
    private void update() {
        List<Integer> waste = Game.getWasteBasket().getContents();
        int i=0;
        
        for (Integer contents : waste) {
            cells[i].setText(contents.toString());
            ++i;
        }
        
    }
}
