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
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import utopiaengine.Connection;
import static utopiaengine.Connection.GUM_CONNECTION;
import static utopiaengine.Connection.LEAD_CONNECTION;
import static utopiaengine.Connection.QUARTZ_CONNECTION;
import static utopiaengine.Connection.SILICA_CONNECTION;
import static utopiaengine.Connection.SILVER_CONNECTION;
import static utopiaengine.Connection.WAX_CONNECTION;
import static utopiaengine.Construct.BATTERY;
import static utopiaengine.Construct.CHASSIS;
import static utopiaengine.Construct.GATE;
import static utopiaengine.Construct.LENS;
import static utopiaengine.Construct.MIRROR;
import static utopiaengine.Construct.SEAL;
import utopiaengine.Game;
import utopiaengine.actions.Action;
import utopiaengine.actions.ActionListener;
import utopiaengine.actions.ConnectionCompleteAction;
import utopiaengine.actions.EngineActivatedAction;

/**
 *
 * @author moorechr
 */
public class AllConstructsUi extends GridPane implements ActionListener {
    
    private Button finalActivation;
    
    public AllConstructsUi() {
        super();
        ConstructUi construct;
        ConnectionUi connection;

        /* Create 11 equal sized columns */
        ColumnConstraints narrow[] = new ColumnConstraints[11];
        for (int i=0; i<11; i++) {
            narrow[i] = new ColumnConstraints();
            narrow[i].setPercentWidth(10);
        }
        getColumnConstraints().addAll(narrow);

        construct = new ConstructUi(LENS);
        construct.getStyleClass().add("boxed");
        add(construct, 0, 0, 3, 1);
        
        connection = new ConnectionUi(SILVER_CONNECTION);
        connection.getStyleClass().add("boxed");
        add(connection, 1, 1);
        
        construct = new ConstructUi(SEAL);
        construct.getStyleClass().add("boxed");
        add(construct, 0, 2, 3, 1);
        
        connection = new ConnectionUi(SILICA_CONNECTION);
        connection.getStyleClass().add("boxed");
        add(connection, 1, 3);
        
        construct = new ConstructUi(MIRROR);
        construct.getStyleClass().add("boxed");
        add(construct, 0, 4, 3, 1);
        
        connection = new ConnectionUi(QUARTZ_CONNECTION);
        connection.getStyleClass().add("boxed");
        add(connection, 3, 2);
        
        connection = new ConnectionUi(WAX_CONNECTION);
        connection.getStyleClass().add("boxed");
        add(connection, 3, 4);
        
        construct = new ConstructUi(CHASSIS);
        construct.getStyleClass().add("boxed");
        add(construct, 4, 2, 3, 1);
        
        connection = new ConnectionUi(GUM_CONNECTION);
        connection.getStyleClass().add("boxed");
        add(connection, 5, 3);
        
        construct = new ConstructUi(GATE);
        construct.getStyleClass().add("boxed");
        add(construct, 4, 4, 3, 1);
        
        connection = new ConnectionUi(LEAD_CONNECTION);
        connection.getStyleClass().add("boxed");
        add(connection, 7, 2);
        
        construct = new ConstructUi(BATTERY);
        construct.getStyleClass().add("boxed");
        add(construct, 8, 2, 3, 1);
        
        finalActivation = new Button("Final Activation");
        finalActivation.setDisable(true);
        finalActivation.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                FinalActivationDialog d = new FinalActivationDialog();
                
                Optional<Integer> result = d.showAndWait();
                if (result.isPresent()) {
                    int i = result.get();
                    if (i != 0) {
                        Game.postAction(new EngineActivatedAction());
                    }
                }
            }
            
        });
        add(finalActivation, 8, 0, 3, 1);
        
        getStyleClass().add("boxed");
        setId("allConstructsUi");
        
        Game.getInstance().addListener(this);
    }

    @Override
    public void handleAction(Action a) {
        if (a instanceof ConnectionCompleteAction) {
            boolean allConnected = true;
            
            for (Connection c : Connection.values()) {
                if (!c.isConnected()) {
                    allConnected = false;
                }
            }
            
            if (allConnected) {
                finalActivation.setDisable(false);
            }
        }
    }
    
}
