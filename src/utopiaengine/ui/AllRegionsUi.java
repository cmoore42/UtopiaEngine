/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utopiaengine.ui;

import javafx.scene.layout.GridPane;
import static utopiaengine.Location.CANYON;
import static utopiaengine.Location.CITY;
import static utopiaengine.Location.MARSHES;
import static utopiaengine.Location.MAW;
import static utopiaengine.Location.PEAK;
import static utopiaengine.Location.WILDS;
import static utopiaengine.Location.WORKSHOP;

/**
 *
 * @author moorechr
 */
public class AllRegionsUi extends GridPane {
    public AllRegionsUi() {
        super();
        
        getStyleClass().add("boxed");
        setId("allRegionsUi");
        
        add(new RegionUi(PEAK), 0, 0);
        add(new RegionUi(WILDS), 1, 0);
        add(new RegionUi(MARSHES), 2, 0);
        add(new RegionUi(CANYON), 0, 1);
        add(new RegionUi(CITY), 1, 1);
        add(new RegionUi(MAW), 2, 1);
    }
    
}
