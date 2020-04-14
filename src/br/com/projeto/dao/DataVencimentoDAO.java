/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.projeto.dao;

import br.com.projeto.jdbc.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author LUCAS
 */
public class DataVencimentoDAO {

    private Connection con;

    public DataVencimentoDAO() {
        this.con = new ConnectionFactory().getConnection();
    }

    public void SalvarData(String data) {
        try {
            String sql = "INSERT INTO `tb_vencimento`(dataVencimento) VALUES(?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, data);
            stmt.execute();
            JOptionPane.showMessageDialog(null, "Data inserida com sucesso!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro:" + e);
        }
    }
    
    public void SalvaValidacoes(String idSistema, String codUnlock){
        try {
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro: "+e);
        }
    }
    
    public void checarData(String idSis, String cod){
        try {
            String sql = "SELECT idSistema,codUnlock FROM `tb_vencimento` WHERE idVencimento=1";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                String codUnlock = rs.getString("codUnlock");
                String idSistema = rs.getString("idSistema");
                if(codUnlock == null && idSistema == null){
                    atualizarPrimeiraData(idSis, cod);
                }
            }else{
                JOptionPane.showMessageDialog(null, "Aqui");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro: "+e);
        }
    }
    
    public void atualizarPrimeiraData(String idSistema, String codUnlock){
        try {
            String sql = "UPDATE `tb_vencimento` SET idSistema=?, codUnlock=? WHERE idVencimento=1";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, idSistema);
            stmt.setString(2, codUnlock);
            stmt.execute();
            stmt.close();
            JOptionPane.showMessageDialog(null, "Serial e id cadastrados!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    public void dataAbrir(){
        try {
            String sql = "INSERT INTO `tb_data`(data) VALUES (?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            Date data = new Date();
            DateFormat formatDate = new SimpleDateFormat("ddMMyyyy");
            String dataF = formatDate.format(data);
            stmt.setString(1, dataF);
            stmt.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

}
