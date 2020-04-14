/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.projeto.dao;

import br.com.projeto.jdbc.ConnectionFactory;
import br.com.projeto.model.Cliente;
import br.com.projeto.model.Motor;
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
public class MotorDAO {

    private Connection con;

    public MotorDAO() {
        this.con = new ConnectionFactory().getConnection();
    }

    public void cadastrarMotor(Motor obj) {
        try {
            String sql = "INSERT INTO `tb_motor`(motor, tipo, cilindros, comb, garantia, tb_cliente_idCliente) "
                    + " VALUES(?,?,?,?,?,?)";

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, obj.getMotor());
            stmt.setString(2, obj.getTipo());
            stmt.setString(3, obj.getCilindros());
            stmt.setString(4, obj.getComb());
            stmt.setString(5, obj.getGarantia());
            stmt.setInt(6, obj.getTb_cliente_idCliente());

            stmt.execute();
            stmt.close();

            JOptionPane.showMessageDialog(null, "Motor cadastrado com sucesso");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e);
        }
    }

    public void editarMotor(Motor obj) {
        try {
            String sql = "UPDATE `tb_motor` SET motor=?, tipo=?, cilindros=?, comb=?, garantia=? WHERE idMotor=?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, obj.getMotor());
            stmt.setString(2, obj.getTipo());
            stmt.setString(3, obj.getCilindros());
            stmt.setString(4, obj.getComb());
            stmt.setString(5, obj.getGarantia());
            stmt.setInt(6, obj.getIdMotor());
            stmt.execute();
            stmt.close();
            JOptionPane.showMessageDialog(null, "Motor alterado com sucesso");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e);
        }
    }

    public List<Motor> listarMotor() {
        try {
            // Primeiro passo - criar a lista
            List<Motor> lista = new ArrayList<>();

            // Segundo passo - Criar o sql, organizar e executar
            String sql = "SELECT m.idMotor, m.motor, m.tipo, m.cilindros, m.comb, m.garantia, c.nome FROM tb_motor m INNER JOIN tb_cliente c on (c.idCliente = m.tb_cliente_idCliente) WHERE m.situacao='at'";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Motor obj = new Motor();

                obj.setIdMotor(rs.getInt("m.idMotor"));
                obj.setMotor(rs.getString("m.motor"));
                obj.setTipo(rs.getString("m.tipo"));
                obj.setCilindros(rs.getString("m.cilindros"));
                obj.setComb(rs.getString("m.comb"));
                obj.setGarantia(rs.getString("m.garantia"));
                Cliente c = new Cliente();
                c.setNome(rs.getString("c.nome"));
                obj.setCliente(c);
                lista.add(obj);
            }
            return lista;
        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "Erro: " + erro);
            return null;
        }
    }

    // Método que inativa o motor
    public void inativarMotor(Motor obj) {
        try {
            String sql = "UPDATE `tb_motor` SET situacao='in' WHERE idMotor=?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, obj.getIdMotor());
            stmt.execute();
            stmt.close();
            JOptionPane.showMessageDialog(null, "Motor inativado com sucesso!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e);
        }
    }

    // Buscar motores baseado no id do cliente
    public List<Motor> listarMotorByCliente(int id) {
        try {
            // Primeiro passo - criar a lista
            List<Motor> lista = new ArrayList<>();

            // Segundo passo - Criar o sql, organizar e executar
            String sql = "SELECT * FROM `tb_motor` WHERE tb_cliente_idCliente = ? AND situacao='at'";
            PreparedStatement stmt = con.prepareStatement(sql);
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Motor obj = new Motor();
                obj.setIdMotor(rs.getInt("idMotor"));
                obj.setMotor(rs.getString("motor"));
                obj.setTipo(rs.getString("tipo"));
                obj.setCilindros(rs.getString("cilindros"));
                obj.setComb(rs.getString("comb"));
                obj.setGarantia(rs.getString("garantia"));
                lista.add(obj);
            }
            return lista;
        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "Erro: " + erro);
            return null;
        }
    }
}
