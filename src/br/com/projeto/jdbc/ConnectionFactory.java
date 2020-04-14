/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.projeto.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author LUCAS
 */
public class ConnectionFactory {

    public Connection getConnection() {
        Connection con = null;
        try {
            JdbcConnection jdbc = new JdbcConnection();
            jdbc.getProps();
            if (jdbc.getUrl() == null) {
                jdbc.setProps();
                jdbc.getProps();
            }
            con = (Connection) DriverManager.getConnection(jdbc.getUrl() + "/" + jdbc.getDatabase(), jdbc.getUser(), jdbc.getPasswd());

        } catch (Exception erro) {
            throw new RuntimeException(erro);
        }
        return con;

    }

}
