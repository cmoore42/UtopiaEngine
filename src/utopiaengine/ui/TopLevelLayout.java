/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utopiaengine.ui;

import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author moorechr
 */
public class TopLevelLayout extends VBox {
    private StoresUi stores;
    private HealthUi health;
    private DiceUi dice;
    private TimeTrackUi timeTrack;
    private AllRegionsUi regions;
    private AllConstructsUi constructs;
    private TreasureUi treasures;
    private InfoDisplay info;
    private WasteBasketUi wasteBasket;
    private ToolsUi tools;
    
    public TopLevelLayout() {
        super();
        
        stores = new StoresUi();
        health = new HealthUi();
        dice = new DiceUi();
        timeTrack = new TimeTrackUi();
        regions = new AllRegionsUi();
        constructs = new AllConstructsUi();
        treasures = new TreasureUi();
        info = new InfoDisplay();
        wasteBasket = new WasteBasketUi();
        tools = new ToolsUi();
        
        HBox box1 = new HBox();
        box1.getChildren().addAll(/* stores, */ tools, treasures, wasteBasket, info);
        box1.getStyleClass().add("top-section");
        
        constructs.getStyleClass().add("upper-section");
        
        HBox box2 = new HBox();
        box2.getChildren().addAll(timeTrack, health);
        box2.getStyleClass().add("lower-section");

        // Test Code
        Button testButton = new Button("Test");
        testButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                TestDialog d = new TestDialog();
                
                Optional<Integer> result = d.showAndWait();
            }
            
        });
        
        HBox box3 = new HBox();
        box3.getChildren().addAll(regions, testButton);
        box3.getStyleClass().add("bottom-section");
        
        getChildren().addAll(box1, constructs, box2, box3);
        
        setId("topLevelLayoutUi");
        
        
    }

    
}
