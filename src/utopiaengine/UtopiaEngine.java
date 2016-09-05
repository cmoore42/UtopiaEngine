/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utopiaengine;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utopiaengine.ui.TopLevelLayout;

/**
 *
 * @author moorechr
 */
public class UtopiaEngine extends Application {
    
    private static Game game;
    
    @Override
    public void start(Stage primaryStage) {
        
        TopLevelLayout layout = new TopLevelLayout();
        
        Scene scene = new Scene(layout);
        
        primaryStage.setTitle("Utopia Engine");
        primaryStage.setScene(scene);
        scene.getStylesheets().add("/resources/main.css");
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        game = Game.getInstance();
        
        launch(args);
        
        game.shutdown();
    }
    
}
