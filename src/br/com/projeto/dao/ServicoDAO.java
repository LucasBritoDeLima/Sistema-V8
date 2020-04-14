/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.projeto.dao;

import br.com.projeto.jdbc.ConnectionFactory;
import br.com.projeto.model.Servico;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author LUCAS
 */
public class ServicoDAO {
    private Connection con;

    public ServicoDAO() {
        this.con = new ConnectionFactory().getConnection();
    }
    
    public List<Servico> listarServicoPorCategoria(String categoria){
        try {
            // Criar a lista
            List<Servico> lista = new ArrayList<>();
            
            // Criar sql, organizar e executar
            String sql = "SELECT * FROM `tb_maoobra` WHERE tipo=?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, categoria);
            ResultSet rs = stmt.executeQuery();
            
            while(rs.next()){
                Servico obj = new Servico();
                
                obj.setIdMao(rs.getInt("idMao"));
                obj.setServico(rs.getString("servico"));
                obj.setTipo(rs.getString("tipo"));
                
                lista.add(obj);
            }
            return lista;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro: "+ e);
            return null;
        }
    }
}
