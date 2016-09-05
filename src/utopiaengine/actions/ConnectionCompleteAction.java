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
public class ConnectionCompleteAction extends Action {
    private final Connection connection;
    
    public ConnectionCompleteAction(Connection connection) {
        this.connection = connection;
    }
    
    public Connection getConnection() {
        return connection;
    }
    
}
