/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.projeto.dao;

import br.com.projeto.jdbc.ConnectionFactory;
import br.com.projeto.model.Retifica;
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
public class RetificaDAO {
    private Connection con;
    
    public RetificaDAO(){
        this.con = new ConnectionFactory().getConnection();
    }
        
    // Metodo cadastrar retifica
    public void cadastrarRetifica(Retifica obj){
        try{
        // Primeiro passo - criar o comando sql
        String sql = "INSERT INTO `tb_retifica`(nome_fantasia, razao_social, cnpj, ie, tel, tel2, tel3, tel4, logradouro, numero, bairro, cep, estado, cidade) "
                + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        
        // Segundo passo - conectar com o banco de dados e organizar o sql
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, obj.getNome_fantasia());
        stmt.setString(2, obj.getRazao_social());
        stmt.setString(3, obj.getCnpj());
        stmt.setString(4, obj.getIe());
        stmt.setString(5, obj.getTel());
        stmt.setString(6, obj.getTel2());
        stmt.setString(7, obj.getTel3());
        stmt.setString(8, obj.getTel4());
        stmt.setString(9, obj.getLogradouro());
        stmt.setString(10, obj.getNumero());
        stmt.setString(11, obj.getBairro());
        stmt.setString(12, obj.getCep());
        stmt.setString(13, obj.getEstado());
        stmt.setString(14, obj.getCidade());
        
        // Terceiro passo - executar o comando sql
        stmt.execute();
        stmt.close();
        
        JOptionPane.showMessageDialog(null, "Cadastrado com sucesso!");
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Ocorreu um erro, Erro: " + e);
        }
    }
    
    // Método listar a retifica
    public Retifica listarRetifica(){
        try {
            // Primeiro passo criar o sql, organizar e executar
            String sql = "SELECT * FROM `tb_retifica` WHERE idRetifica = 1";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            Retifica obj = new Retifica();
            while (rs.next()){
                obj.setNome_fantasia(rs.getString("nome_fantasia"));
                obj.setRazao_social(rs.getString("razao_social"));
                obj.setCnpj(rs.getString("cnpj"));
                obj.setIe(rs.getString("ie"));
                obj.setTel(rs.getString("tel"));
                obj.setTel2(rs.getString("tel2"));
                obj.setTel3(rs.getString("tel3"));
                obj.setTel4(rs.getString("tel4"));
                obj.setLogradouro(rs.getString("logradouro"));
                obj.setNumero(rs.getString("numero"));
                obj.setBairro(rs.getString("bairro"));
                obj.setCep(rs.getString("cep"));
                obj.setEstado(rs.getString("estado"));
                obj.setCidade(rs.getString("cidade"));                
            }
            return obj;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e);
            return null;
        }
    }
    
    // Método editar retifica
    public void alterarRetifica(Retifica obj){
        try{
        // Primeiro passo criar o comando sql
        String sql = "UPDATE `tb_retifica` set nome_fantasia=?, razao_social=?, cnpj=?, ie=?, tel=?, tel2=?, tel3=?, tel4=?, logradouro=?, numero=?, bairro=?, cep=?, estado=?, cidade=? WHERE idRetifica=1";
        
        // Segundo passo - conectar o banco de dados e organizar o comando sql
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, obj.getNome_fantasia());
        stmt.setString(2, obj.getRazao_social());
        stmt.setString(3, obj.getCnpj());
        stmt.setString(4, obj.getIe());
        stmt.setString(5, obj.getTel());
        stmt.setString(6, obj.getTel2());
        stmt.setString(7, obj.getTel3());
        stmt.setString(8, obj.getTel4());
        stmt.setString(9, obj.getLogradouro());
        stmt.setString(10, obj.getNumero());
        stmt.setString(11, obj.getBairro());
        stmt.setString(12, obj.getCep());
        stmt.setString(13, obj.getEstado());
        stmt.setString(14, obj.getCidade());
        
       // Terceiro passo - executar o comando sql
       stmt.execute();
       stmt.close();
       
       JOptionPane.showMessageDialog(null, "Dados Alterados com sucesso!");
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "ERRO: " + e);
    }
    }
    
    // Método alterar o logotipo
    public void alterarFoto(Retifica obj){
        try {
            String sql = "UPDATE `tb_retifica` set foto=? WHERE idRetifica=1";
            
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setBytes(1, obj.getFoto());
            stmt.execute();
            stmt.close();
            JOptionPane.showMessageDialog(null, "Logomarca adicionada");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro: "+e);
        }
    }
    
    // Método para mostrar a imagem do banco de dados
    public Retifica consultaPorId(){
        try {
            String sql = "SELECT foto FROM `tb_retifica` WHERE idRetifica=1";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            Retifica obj = new Retifica();
            if(rs.next()){
                obj.setFoto(rs.getBytes("foto"));
            }
            return obj;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Foto não encotrada!!");
            return null;
        }
    }
}
