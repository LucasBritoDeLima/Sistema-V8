/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.projeto.dao;

import br.com.projeto.jdbc.ConnectionFactory;
import br.com.projeto.model.Peca;
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
public class PecaDAO {

    private Connection con;

    public PecaDAO() {
        this.con = new ConnectionFactory().getConnection();
    }

    public List<Peca> listarPeca() {
        try {
            // Criar a lista
            List<Peca> lista = new ArrayList<>();

            // Criar sql, organizar e executar
            String sql = "SELECT * FROM `tb_pecas`";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Peca obj = new Peca();

                obj.setIdPeca(rs.getInt("idPeca"));
                obj.setPeca(rs.getString("peca"));
                lista.add(obj);
            }
            return lista;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e);
            return null;
        }
    }
}
