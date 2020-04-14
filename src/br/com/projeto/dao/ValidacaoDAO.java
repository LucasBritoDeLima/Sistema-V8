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
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author LUCAS
 */
public class ValidacaoDAO {

    private Connection con;
    int valida;

    public ValidacaoDAO() {
        this.con = new ConnectionFactory().getConnection();
    }

    public void valida(String senha) throws SQLException {
        try {
            String sql = "SELECT * FROM `vencimento`";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            if (rs.last()) {
                valida = Integer.parseInt(rs.getString("data_vencimento"));
                int operacao = (valida + 132) / 4;
                int senhaValidacao = Integer.parseInt(senha);
                if (operacao == senhaValidacao) {
                    int dia, mes, ano;
                    String AcertaMes, AcertaDia, ProxSenha;
                    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                    Date hoje = new Date();
                    String data = df.format(hoje);
                    char[] senhachar = data.toCharArray();
                    dia = Integer.parseInt("" + senhachar[0] + senhachar[1]);
                    mes = Integer.parseInt("" + senhachar[3] + senhachar[4]);
                    ano = Integer.parseInt("" + senhachar[6] + senhachar[7] + senhachar[8] + senhachar[9]);

                    if (mes < 12) {
                        mes++;
                        if(mes<10){
                            AcertaMes = "0"+mes;
                        }
                        else {
                            AcertaMes = String.valueOf(mes);
                        }
                    }else {
                        AcertaMes = "0"+mes;
                        mes=1;
                        ano++;
                    }
                    if(dia<10){
                        AcertaDia = "0"+dia;
                    }
                    else {
                        AcertaDia = String.valueOf(dia);
                    }
                    ProxSenha = AcertaDia+AcertaMes+ano;
                    PreparedStatement pst = con.prepareStatement("INSERT INTO `vencimento`(data_vencimento) VALUES (?)");
                    pst.setString(1, ProxSenha);
                    pst.execute();
                    JOptionPane.showMessageDialog(null, ProxSenha);
                } else {
                    JOptionPane.showMessageDialog(null, "Código incorreto");
                }
                JOptionPane.showMessageDialog(null, valida);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e);
        }
    }
}
