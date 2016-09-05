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
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import utopiaengine.Component;
import utopiaengine.Connection;
import utopiaengine.Game;
import static utopiaengine.Treasure.RECORD;
import utopiaengine.actions.Action;
import utopiaengine.actions.ActionListener;
import utopiaengine.actions.CanConnectAction;
import utopiaengine.actions.EndOfWorldAction;
import utopiaengine.actions.StoresChangedAction;

/**
 *
 * @author moorechr
 */
public class ConnectionUi extends VBox implements ActionListener {
    private Connection connection;
    private Label componentLabel;
    private Button connectButton;
    private ProgressBar progressBar;
    
    public ConnectionUi(Connection connection) {
        this.connection = connection;
        
        componentLabel = new Label(connection.getComponent().getName());
        
        progressBar = new ProgressBar(0.0);
        
        connectButton = new Button("Connect");
        connectButton.setDisable(true);
        connectButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                connection.getComponent().decrement();
                
                if (RECORD.isFound()) {
                    Game.info("The " + RECORD.getName() + " allows you to complete the connection.");
                    componentLabel.setText("Connected: 1");
                    connectButton.setDisable(true);
                    connection.connect(1);
                    getStyleClass().clear();
                    getStyleClass().add("activated");
                } else {
                    ConnectionDialog d = new ConnectionDialog(connection);
                    Optional<Integer> result = d.showAndWait();

                    if (result.isPresent()) {
                        int connectionResult = result.get();

                        if (connectionResult >= 0) {
                            componentLabel.setText("Connected: " + connectionResult);
                            connectButton.setDisable(true);
                            connection.connect(connectionResult);
                            getStyleClass().clear();
                            getStyleClass().add("activated");
                        }
                    }
                }
            }
            
        });
        
        getChildren().addAll(componentLabel, progressBar, connectButton);
        
        getStyleClass().clear();
        getStyleClass().add("missing");
        
        Game.getInstance().addListener(this);
    }
    
    Connection getConnection() {
        return connection;
    }

    @Override
    public void handleAction(Action a) {
        if (a instanceof CanConnectAction) {
            CanConnectAction c = (CanConnectAction)a;
            if (c.getConnection() == connection) {
                connectButton.setDisable(!c.canConnect());
            }
        } else if (a instanceof EndOfWorldAction) {
            connectButton.setDisable(true);
        } else if (a instanceof StoresChangedAction) {
            Component c = connection.getComponent();
            double d_qty = (double)c.getQuantity();
            double percent = d_qty / 4.0;
            progressBar.setProgress(percent);
        }
    }
    
}
