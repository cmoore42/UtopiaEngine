/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utopiaengine.ui;

import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import utopiaengine.Component;
import static utopiaengine.Construct.SEAL;
import utopiaengine.Event;
import utopiaengine.Game;
import utopiaengine.Location;
import utopiaengine.Player;
import utopiaengine.actions.Action;
import static utopiaengine.actions.Action.EventType.SEARCH;
import static utopiaengine.actions.Action.EventType.TRAVEL;
import utopiaengine.actions.ActionListener;

/**
 *
 * @author moorechr
 */
public class RegionUi  extends VBox implements ActionListener {
    Location location;
    
    private final Label name;
    private final Label construct;
    private final HBox componentBox;
    private final Label component;
    private final ProgressBar componentQty;
    private final Label treasure;
    private final ImageView[] searches;
    private final Image freeSearch;
    private final Image costlySearch;
    private final Image usedSearch;
    private final Button searchButton;
    private final Button travelButton;
    private final Button sealButton;
    private VBox eventBox;
    
    public RegionUi(Location location) {
        super();
        
        this.location = location;
             
        name = new Label(location.getName());
        name.getStyleClass().add("box-title");
        
        VBox descriptionBox = new VBox();
        construct = new Label(location.getConstruct().getName());
        construct.setTooltip(location.getConstruct().getTooltip());
        componentBox = new HBox();
        component = new Label(location.getComponent().getName());
        componentQty = new ProgressBar(0.0);
        componentBox.getChildren().addAll(component, componentQty);
        treasure = new Label(location.getTreasure().getName());
        treasure.setTooltip(location.getTreasure().getTooltip());
        descriptionBox.getChildren().addAll(construct, componentBox, treasure);
        descriptionBox.getStyleClass().add("boxed");
        
        eventBox = new VBox();

        
        HBox topBox = new HBox();
        topBox.getChildren().addAll(descriptionBox, eventBox);
        
        
        HBox searchBox = new HBox();
        freeSearch = new Image(getClass().getResourceAsStream("/resources/region_free_day.jpg"));
        costlySearch = new Image(getClass().getResourceAsStream("/resources/region_day.jpg"));
        usedSearch = new Image(getClass().getResourceAsStream("/resources/region_used_day.jpg"));
        
        searches = new ImageView[6];
        for (int i=0; i<6; i++) {
            searches[i] = new ImageView();
        }
        
        searchBox.getChildren().addAll(searches);
        
        refreshSearchCounter();
        
        searchButton = new Button("Search");
        searchButton.setDisable(true);
        searchButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                Game.postAction(new Action(SEARCH, location));
                
                refreshSearchCounter();
            }
            
        });
        
        travelButton = new Button("Travel Here");
        travelButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {                
                Game.postAction(new Action(TRAVEL, location));
            }
            
        });
        
        sealButton = new Button("Use Seal of Balance");
        sealButton.setOnAction((ActionEvent event) -> {
            Game.info("You use the Seal of Balance to clear the events at " + location.getName());
            location.clearEvents();
            SEAL.setUsed(true);
        });
        if (SEAL.isActivated() && (!SEAL.isUsed()) && (Game.getPlayer().getLocation() == location)) {
            sealButton.setDisable(false);
        } else {
            sealButton.setDisable(true);
        }
        sealButton.setTooltip(SEAL.getTooltip());
        
        getChildren().addAll(name, topBox, searchBox, searchButton, travelButton, sealButton);
        
        getStyleClass().clear();
        getStyleClass().add("location-non-active");
        
        Game.getInstance().addListener(this);
        
    }

    @Override
    public void handleAction(Action a) {
        switch(a.getType()) {
            case TRAVEL:
                if (a.getLocation() == location) {
                    searchButton.setDisable(false);
                    if (SEAL.isActivated() && (!SEAL.isUsed())) {
                        sealButton.setDisable(false);
                    }
                    travelButton.setText("Leave");
                    getStyleClass().clear();
                    getStyleClass().add("location-active");
                    refreshSearchCounter();
                } else {
                    searchButton.setDisable(true);
                    sealButton.setDisable(true);
                    travelButton.setText("Travel Here");
                    getStyleClass().clear();
                    getStyleClass().add("location-non-active");
                }
                break;
            case PLAYER_HEALTH_CHANGED:
                Player p = Game.getPlayer();
                if (p.isUnconsious()|| p.isDead()) {
                    /* Can't search or travel if we're dead or unconscious */
                    searchButton.setDisable(true);
                    travelButton.setDisable(true);
                } else {
                    if (p.getLocation() == location) {
                        searchButton.setDisable(false);
                    } else {
                        searchButton.setDisable(true);
                    }
                    travelButton.setDisable(false);
                }
                break;
            case EVENT_CHANGED:
                eventBox.getChildren().clear();
                List<Event> eventList = location.getEvents();
                for (Event e : eventList) {
                    Label eventLabel;
                    eventLabel = new Label(e.getName());
                    eventLabel.getStyleClass().add("boxed");
                    eventLabel.getStyleClass().add("activated");
                    eventLabel.setTooltip(e.getTooltip());
                    eventBox.getChildren().add(eventLabel);
                }
                break;
            case LOCATION_CHANGED:
                refreshSearchCounter();
                break;
            case END_OF_WORLD:
                searchButton.setDisable(true);
                travelButton.setDisable(true);
                break;
            case CONSTRUCT_CHANGED:
                if (SEAL.isActivated() && (!SEAL.isUsed()) && (Game.getPlayer().getLocation() == location)) {
                sealButton.setDisable(false);
                } else {
                    sealButton.setDisable(true);
                }
                
                if (location.getConstruct().isActivated()) {
                    construct.getStyleClass().add("activated");
                } else if (location.getConstruct().isFound()) {
                    construct.getStyleClass().add("found");
                }
                break;
            case STORES_CHANGED:
                Component c = location.getComponent();
                double d_qty = (double) c.getQuantity();
                double percent = d_qty / 4.0;
                componentQty.setProgress(percent);
                break;
            case TREASURE_FOUND:
                if (location.getTreasure().isFound()) {
                    treasure.getStyleClass().add("activated");
                }
                break;
        }
    }
    
    private void refreshSearchCounter() {
        int unused = 0;
        
        for (int i=0; i<6; i++) {
            switch(location.getState(i)) {
                case FREE:
                    searches[i].setImage(freeSearch);
                    ++unused;
                    break;
                case COSTLY:
                    searches[i].setImage(costlySearch);
                    ++unused;
                    break;
                case USED:
                    searches[i].setImage(usedSearch);
                    break;
            }
        }

        if (unused == 0) {
            searchButton.setDisable(true);
        }
    }
    
}
