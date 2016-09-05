/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utopiaengine.actions;

import utopiaengine.Connection;

/**
 *
 * @author moorechr
 */
public class CanConnectAction extends Action {
    private Connection connection;
    private boolean canConnect;
    
    public CanConnectAction(Connection connection, boolean canConnect) {
        this.connection = connection;
        this.canConnect = canConnect;
    }
    
    public Connection getConnection() {
        return connection;
    }
    
    public boolean canConnect() {
        return this.canConnect;
    }
    
}
