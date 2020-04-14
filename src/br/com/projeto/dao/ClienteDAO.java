/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.projeto.dao;

import br.com.projeto.jdbc.ConnectionFactory;
import br.com.projeto.model.Cliente;
import br.com.projeto.model.WebServiceCep;
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
public class ClienteDAO {

    private Connection con;

    public ClienteDAO() {
        this.con = new ConnectionFactory().getConnection();
    }

    public void cadastrarCliente(Cliente obj) {
        try {
            String sql = "INSERT INTO `tb_cliente`(nome, cpf, rg, tel, tel2, logradouro, numero, bairro, cep, estado, cidade) "
                    + " VALUES(?,?,?,?,?,?,?,?,?,?,?)";

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, obj.getNome());
            stmt.setString(2, obj.getCpf());
            stmt.setString(3, obj.getRg());
            stmt.setString(4, obj.getTel());
            stmt.setString(5, obj.getTel2());
            stmt.setString(6, obj.getLogradouro());
            stmt.setString(7, obj.getNumero());
            stmt.setString(8, obj.getBairro());
            stmt.setString(9, obj.getCep());
            stmt.setString(10, obj.getEstado());
            stmt.setString(11, obj.getCidade());

            stmt.execute();
            stmt.close();

            JOptionPane.showMessageDialog(null, "Cliente inserido com sucesso!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao inserir cliente:" + e);
        }
    }

    public List<Cliente> listarCliente() {
        try {
            // Primeiro passo - criar a lista
            List<Cliente> lista = new ArrayList<>();

            // Segundo passo - Criar o sql, organizar e executar
            String sql = "SELECT * FROM `tb_cliente` WHERE situacao='at'";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Cliente obj = new Cliente();

                obj.setIdCliente(rs.getInt("idCliente"));
                obj.setNome(rs.getString("nome"));
                obj.setCpf(rs.getString("cpf"));
                obj.setRg(rs.getString("rg"));
                obj.setTel(rs.getString("tel"));
                obj.setTel2(rs.getString("tel2"));
                obj.setLogradouro(rs.getString("logradouro"));
                obj.setNumero(rs.getString("numero"));
                obj.setBairro(rs.getString("bairro"));
                obj.setCep(rs.getString("cep"));
                obj.setEstado(rs.getString("estado"));
                obj.setCidade(rs.getString("cidade"));

                lista.add(obj);
            }
            return lista;
        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "Erro: " + erro);
            return null;
        }
    }

    public List<Cliente> buscaClientePorNome(String nome) {
        try {
            // Criar a lista
            List<Cliente> lista = new ArrayList<>();

            // Criar o sql, organizar e executar
            String sql = "SELECT * FROM `tb_cliente` WHERE nome LIKE ? AND situacao='at'";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, nome);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Cliente obj = new Cliente();

                obj.setIdCliente(rs.getInt("idCliente"));
                obj.setNome(rs.getString("nome"));
                obj.setCpf(rs.getString("cpf"));
                obj.setRg(rs.getString("rg"));
                obj.setTel(rs.getString("tel"));
                obj.setTel2(rs.getString("tel2"));
                obj.setLogradouro(rs.getString("logradouro"));
                obj.setNumero(rs.getString("numero"));
                obj.setBairro(rs.getString("bairro"));
                obj.setCep(rs.getString("cep"));
                obj.setEstado(rs.getString("estado"));
                obj.setCidade(rs.getString("cidade"));

                lista.add(obj);
            }
            return lista;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e);
            return null;
        }
    }

    //Método alterar o cliente
    public void alterarCliente(Cliente obj) {
        try {
            // Primeiro passo - Criar o comando sql
            String sql = "UPDATE `tb_cliente` set nome=?, cpf=?, rg=?, tel=?, tel2=?, logradouro=?, numero=?, bairro=?, cep=?, estado=?, cidade=? WHERE idCliente=?";

            // Segundo passo - conectar o banco de dados e organizar o comando sql
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, obj.getNome());
            stmt.setString(2, obj.getCpf());
            stmt.setString(3, obj.getRg());
            stmt.setString(4, obj.getTel());
            stmt.setString(5, obj.getTel2());
            stmt.setString(6, obj.getLogradouro());
            stmt.setString(7, obj.getNumero());
            stmt.setString(8, obj.getBairro());
            stmt.setString(9, obj.getCep());
            stmt.setString(10, obj.getEstado());
            stmt.setString(11, obj.getCidade());

            stmt.setInt(12, obj.getIdCliente());

            // Terceiro passo - executar o comando sql
            stmt.execute();
            stmt.close();

            JOptionPane.showMessageDialog(null, "Cliente alterado com sucesso");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e);
        }
    }

    public void inativarCliente(Cliente obj) {
        try {
            // Criar o comando sql
            String sql = "UPDATE `tb_cliente` SET situacao='in' WHERE idCliente=?";

            // Conectar com o banco de dados, organizar e executar comando sql
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, obj.getIdCliente());

            // Executar o comando sql
            stmt.execute();
            stmt.close();

            JOptionPane.showMessageDialog(null, "Cliente inativado com sucesso");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e);
        }
    }

    //Método consulta cliente por cpf
    public Cliente pesquisaClientePorCpf(String cpf) {
        try {
            // Primeiro passo criar o sql, organizar e executar
            String sql = "SELECT * FROM `tb_cliente` WHERE cpf=? AND situacao='at'";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, cpf);

            ResultSet rs = stmt.executeQuery();
            Cliente obj = new Cliente();

            if (rs.next()) {
                obj.setIdCliente(rs.getInt("idCliente"));
                obj.setNome(rs.getString("nome"));
                obj.setCpf(rs.getString("cpf"));
                obj.setRg(rs.getString("rg"));
                obj.setTel(rs.getString("tel"));
                obj.setTel2(rs.getString("tel2"));
                obj.setLogradouro(rs.getString("logradouro"));
                obj.setNumero(rs.getString("numero"));
                obj.setBairro(rs.getString("bairro"));
                obj.setCep(rs.getString("cep"));
                obj.setEstado(rs.getString("estado"));
                obj.setCidade(rs.getString("cidade"));
            }
            return obj;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Cliente não encontrado!");
            return null;
        }
    }

    public Cliente buscaCep(String cep) {
        WebServiceCep webServiceCep = WebServiceCep.searchCep(cep);
        Cliente obj = new Cliente();
        if (webServiceCep.wasSuccessful()) {
            obj.setLogradouro(webServiceCep.getLogradouroFull());
            obj.setCidade(webServiceCep.getCidade());
            obj.setBairro(webServiceCep.getBairro());
            obj.setEstado(webServiceCep.getUf());
            return obj;
        } else {
            JOptionPane.showMessageDialog(null, "Erro numero: " + webServiceCep.getResulCode());
            JOptionPane.showMessageDialog(null, "Descrição do erro: " + webServiceCep.getResultText());
            return null;
        }
    }
}
