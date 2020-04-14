/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.projeto.dao;

import br.com.projeto.jdbc.ConnectionFactory;
import br.com.projeto.model.Cliente;
import br.com.projeto.model.Usuario;
import br.com.projeto.view.FrmLogin;
import br.com.projeto.view.FrmMenu;
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
public class UsuarioDAO {

    private Connection con;

    public UsuarioDAO() {
        this.con = new ConnectionFactory().getConnection();
    }

    //Método Cadastrar usuário
    public void cadastrarUsuario(Usuario obj) {
        try {
            // Criar o comando sql
            String sql = "INSERT INTO `tb_users`(user,senha,nivel_acesso) VALUES (?,?,?)";

            //Conectar com o banco de dados e executar codigo sql
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, obj.getUser());
            stmt.setString(2, obj.getSenha());
            stmt.setString(3, obj.getNivel_acesso());

            // Executar o comando sql
            stmt.execute();
            stmt.close();

            JOptionPane.showMessageDialog(null, "Usuário cadastrado com sucesso!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e);
        }
    }

    // Método para alterar o usuário
    public void alterarUsuario(Usuario obj) {
        try {
            //Criar o comando sql
            String sql = "UPDATE `tb_users` SET user=?, senha=?, nivel_acesso=? WHERE idUser=?";

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, obj.getUser());
            stmt.setString(2, obj.getSenha());
            stmt.setString(3, obj.getNivel_acesso());
            stmt.setInt(4, obj.getIdUser());
            stmt.execute();
            stmt.close();
            JOptionPane.showMessageDialog(null, "Usuário alterado com sucesso!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERRO: " + e);
        }
    }

    public List<Usuario> listarUsuario() {
        try {
            // Primeiro passo - criar a lista
            List<Usuario> lista = new ArrayList<>();

            // Segundo passo - Criar o sql, organizar e executar
            String sql = "SELECT * FROM `tb_users` WHERE `nivel_acesso` <> 'Desenvolvedor'";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Usuario obj = new Usuario();

                obj.setIdUser(rs.getInt("idUser"));
                obj.setUser(rs.getString("user"));
                obj.setSenha(rs.getString("senha"));
                obj.setNivel_acesso(rs.getString("nivel_acesso"));
                obj.setSituacao(rs.getString("situacao"));
                lista.add(obj);
            }
            return lista;
        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "Erro: " + erro);
            return null;
        }
    }

    public void inativarUsuario(Usuario obj) {
        try {
            //Criar o comando sql
            String sql = "UPDATE `tb_users` SET situacao='Inativo' WHERE idUser=?";

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, obj.getIdUser());
            stmt.execute();
            stmt.close();
            JOptionPane.showMessageDialog(null, "Usuário inativado com sucesso!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERRO: " + e);
        }
    }

    public void ativarUsuario(Usuario obj) {
        try {
            //Criar o comando sql
            String sql = "UPDATE `tb_users` SET situacao='Ativo' WHERE idUser=?";

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, obj.getIdUser());
            stmt.execute();
            stmt.close();
            JOptionPane.showMessageDialog(null, "Usuário Ativado com sucesso!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "ERRO: " + e);
        }
    }

    public void efetuaLogin(String usuario, String senha) {
        try {
            // Código SQL
            String sql = "SELECT * FROM `tb_users` WHERE user=? AND senha=? AND situacao='Ativo'";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, usuario);
            stmt.setString(2, senha);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // Usuário logou
                // Privilégio Administrador
                if (rs.getString("nivel_acesso").equals("Administrador")) {
                    JOptionPane.showMessageDialog(null, "Seja bem vindo ao sistema!");
                    FrmMenu tela = new FrmMenu();
                    tela.lblUser.setText(rs.getString("user"));
                    tela.lblTipo.setText("Administrador");
                    tela.setVisible(true);
                    tela.mSistema.setVisible(false);
                } //Caso o usuário seja do tipo limitado
                else if (rs.getString("nivel_acesso").equals("Limitado")) {
                    JOptionPane.showMessageDialog(null, "Seja bem vindo ao sistema!");
                    FrmMenu tela = new FrmMenu();
                    tela.lblUser.setText(rs.getString("user"));
                    tela.lblTipo.setText("Limitado");
                    tela.mSistema.setVisible(false);
                    tela.inativos.setEnabled(false);
                    tela.menuGerenciamento.setEnabled(false);
                    tela.menuOrcamentoPorData.setEnabled(false);
                    tela.menuOrdemPorData.setEnabled(false);
                    tela.menuRetifica.setEnabled(false);
                    tela.menuLogomarca.setEnabled(false);
                    tela.menuGerenciamento.setEnabled(false);
                    
                    //tela.menu_posicao.setEnabled(false);
                    //tela.menu_controlevendas.setVisible(false);
                    tela.setVisible(true);
                }else if (rs.getString("nivel_acesso").equals("Desenvolvedor")){
                    JOptionPane.showMessageDialog(null, "Modo do desenvolvedor, opções reservadas ao desenvolvedor");
                    FrmMenu tela = new FrmMenu();
                    tela.lblUser.setText(rs.getString("user"));
                    tela.lblTipo.setText("Desenvolvedor");
                    tela.mSistema.setVisible(true);
                } else {
                    //Dados incorretos
                    JOptionPane.showMessageDialog(null, "Dados incorretos ou inválidos");
                    new FrmLogin().setVisible(true);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e);
        }
    }
}
