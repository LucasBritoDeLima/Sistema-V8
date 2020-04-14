/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.projeto.jdbc;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author LUCAS
 */
public class JdbcConnection {

    private String user;
    private String passwd;
    private String url;
    private String database;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public void setProps() {
        Properties props = new Properties();

        props.setProperty("jdbc.user", "root");
        props.setProperty("jdbc.passwd", "");
        props.setProperty("jdbc.url", "jdbc:mysql://127.0.0.1");
        props.setProperty("jdbc.dataBase", "retifica");

        try {
            FileOutputStream fos = new FileOutputStream("C:\\config.xml");
            props.storeToXML(fos, "FILE JDBC PROPERTIES:", "ISO-8859-1");
            fos.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    public void getProps() {
        Properties props = new Properties();

        try {
            //Setamos o arquivo que será lido
            FileInputStream fis = new FileInputStream("C:\\config.xml");
            //método load faz a leitura através do objeto fis
            props.loadFromXML(fis);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        //Captura o valor da propriedade, através do nome da propriedade(Key)
        this.setUser(props.getProperty("jdbc.user"));
        this.setPasswd(props.getProperty("jdbc.passwd"));
        this.setUrl(props.getProperty("jdbc.url"));
        this.setDatabase(props.getProperty("jdbc.dataBase"));

    }
}
