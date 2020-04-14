/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.projeto.view;

import br.com.projeto.dao.DataVencimentoDAO;
import br.com.projeto.dao.ValidacaoDAO;
import br.com.projeto.jdbc.ConnectionFactory;
import br.com.projeto.model.HashUtils;
import java.awt.Toolkit;
import br.com.projeto.model.Validacao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import javax.swing.JOptionPane;

/**
 *
 * @author LUCAS
 */
public class FrmValidaSis extends javax.swing.JFrame {

    private Connection con;
    Validacao val = new Validacao();
    ValidacaoDAO vdao = new ValidacaoDAO();

    /**
     * Creates new form FrmValidaSis2
     */
    public void gerarAleatorio() {
        String letras = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();

        String armazenaChaves = "";
        int index = -1;
        for (int i = 0; i < 6; i++) {
            index = random.nextInt(letras.length());
            armazenaChaves += letras.substring(index, index + 1);
            //txtId.setText(armazenaChaves);
        }

        String numeros = "0123456789";
        Random random2 = new Random();
        String armazenaNumeros = "";
        int index0 = -1;
        for (int i = 0; i < 4; i++) {
            index = random.nextInt(numeros.length());
            armazenaNumeros += numeros.substring(index, index + 1);
        }

        String juncao = armazenaChaves + armazenaNumeros;
        Random random1 = new Random();
        String novaCombinacao = "";
        int index1 = -1;
        for (int i = 0; i < 10; i++) {
            index1 = random.nextInt(juncao.length());
            novaCombinacao += juncao.substring(index1, index1 + 1);
        }

        txtId.setText(novaCombinacao);
    }

    public void verificarSerial() {
        String serialInvertido = txtCodigo.getText();
        /*Recebendo a string invertida*/
        serialInvertido = new StringBuilder(serialInvertido).reverse().toString();/*Desinvertendo a string*/
        String idPc = txtId.getText();
        /*Recebendo a string id do sistema*/
        String resultado = HashUtils.getHashMd5(idPc).substring(0, 10).toUpperCase(); //Gerando o hash da string novamente
        if (resultado.equals(serialInvertido)) {
            JOptionPane.showMessageDialog(null, "Sistema validado!");
        } else {
            JOptionPane.showMessageDialog(null, "Código incorreto");
        }
    }

    public void verificarSerialDataVencimento(String idSistema, String codUnlock, String dataVencimento) {
        String serialInvertido = txtCodigo.getText();
        /*Recebendo a string invertida*/
        serialInvertido = new StringBuilder(serialInvertido).reverse().toString();/*Desinvertendo a string*/
        String idPc = txtId.getText();
        /*Recebendo a string id do sistema*/
        String resultado = HashUtils.getHashMd5(idPc).substring(0, 10).toUpperCase(); //Gerando o hash da string novamente
        if (resultado.equals(serialInvertido)) {
            JOptionPane.showMessageDialog(null, "Sistema validado!");
            String vencimentoAtual = vencimentoAtual().substring(0, 2);
            String vencimentoAnterior = vencimentoAnteriorUltimo().substring(0, 2);
            if (vencimentoAtual.equals(vencimentoAnterior)) {
                btnVencimento.setEnabled(false);
            } else {
                validaAlteracaoVencimento(idSistema, codUnlock, dataVencimento);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Código inválido!");
        }
    }

    public void verificaSerialAndValida(String idSistema, String codUnlock) {
        String serialInvertido = txtCodigo.getText();
        /*Recebendo a string invertida*/
        serialInvertido = new StringBuilder(serialInvertido).reverse().toString();/*Desinvertendo a string*/
        String idPc = txtId.getText();
        /*Recebendo a string id do sistema*/
        String resultado = HashUtils.getHashMd5(idPc).substring(0, 10).toUpperCase(); //Gerando o hash da string novamente
        if (resultado.equals(serialInvertido)) {
            JOptionPane.showMessageDialog(null, "Sistema validado!");
            verificaAddData(idSistema, codUnlock);
        } else {
            System.out.println("inválido");
        }
    }

    public FrmValidaSis() {
        initComponents();
        this.gerarAleatorio();
        this.con = new ConnectionFactory().getConnection();
        this.firstIsEmpty();
        this.lastIsEmpty();
    }

    public void firstIsEmpty() {
        try {
            String sql = "SELECT idSistema,codUnlock FROM `tb_vencimento` WHERE idVencimento=1";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String idSistema = rs.getString("idSistema");
                String codUnlock = rs.getString("codUnlock");
                if (idSistema != null && codUnlock != null) {
                    btnValidarFirst.setEnabled(false);
                }
            } else {
                btnValidarFirst.setEnabled(true);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void lastIsEmpty() {
        try {
            String sql = "SELECT idSistema,codUnlock FROM `tb_vencimento`";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            if (rs.last()) {
                String idSistema = rs.getString("idSistema");
                String codUnlock = rs.getString("codUnlock");
                if (idSistema != null && codUnlock != null) {
                    btnVencimento.setEnabled(false);
                }
            } else {
                btnVencimento.setEnabled(true);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void verificaAddData(String idSistema, String codUnlock) {
        try {
            String sql = "SELECT * FROM `tb_vencimento`";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            if (rs.last()) {
                String dataDB = rs.getString("dataVencimento");
                char[] dataQuebrada = dataDB.toCharArray();
                String diaC, mesC, dataNova;
                int dia = Integer.parseInt("" + dataQuebrada[0] + dataQuebrada[1]);
                int mes = Integer.parseInt("" + dataQuebrada[2] + dataQuebrada[3]);
                int ano = Integer.parseInt("" + dataQuebrada[4] + dataQuebrada[5] + dataQuebrada[6] + dataQuebrada[7]);
                if (mes < 12) {
                    mes++;
                    if (mes < 10) {
                        mesC = "0" + mes;
                    } else {
                        mesC = String.valueOf(mes);
                    }
                } else {
                    mesC = "0" + mes;
                    mes = 1;
                    ano++;
                }
                if (dia < 10) {
                    diaC = "0" + dia;
                } else {
                    diaC = String.valueOf(dia);
                }
                dataNova = diaC + mesC + ano;
                validarLicenca(dataNova, idSistema, codUnlock);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro:" + e);
        }
    }

    public void validarLicenca(String dataNova, String idSistema, String codUnlock) {
        try {
            String sql = "INSERT INTO `tb_vencimento`(dataVencimento,idSistema,codUnlock) VALUES(?,?,?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, dataNova);
            stmt.setString(2, idSistema);
            stmt.setString(3, codUnlock);
            stmt.execute();
            JOptionPane.showMessageDialog(null, "Sistema validado com sucesso!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public String vencimentoAtual() {
        try {
            String sql = "SELECT * FROM `tb_vencimento`";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            if (rs.last()) {
                String dataVencimentoAtual = rs.getString("dataVencimento");
                return dataVencimentoAtual;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e);
        }
        return null;
    }

    public String vencimentoAnteriorUltimo() {
        try {
            String sql = "SELECT * FROM `tb_vencimento`";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            if (rs.last()) {
                if (rs.previous()) {
                    String dataVencimentoAnterior = rs.getString("dataVencimento");
                    return dataVencimentoAnterior;
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e);
        }
        return null;
    }

    public void validaAlteracaoVencimento(String idSistema, String codUnlock, String dataVencimento) {
        try {
            String sql = "UPDATE `tb_vencimento` SET idSistema=?, codUnlock=? WHERE dataVencimento=?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, idSistema);
            stmt.setString(2, codUnlock);
            stmt.setString(3, dataVencimento);
            stmt.execute();
            stmt.close();
            JOptionPane.showMessageDialog(null, "Sistema validado com sucesso!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtCodigo = new javax.swing.JTextField();
        btnValidarFirst = new javax.swing.JButton();
        btnSair = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        btnValidar = new javax.swing.JButton();
        btnVencimento = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Validação do sistema");
        setIconImage(Toolkit.getDefaultToolkit().getImage("C:/Users/LUCAS/Documents/NetBeansProjects/Projeto Retifica/src/imagens/icon.png"));
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("VALIDAÇÃO DO SISTEMA");

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("CÓDIGO PARA VALIDAÇÃO");
        jLabel2.setToolTipText("");

        txtCodigo.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txtCodigo.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        btnValidarFirst.setText("VALIDAR PRIMEIRA VEZ");
        btnValidarFirst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnValidarFirstActionPerformed(evt);
            }
        });

        btnSair.setText("SAIR");
        btnSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSairActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("ID DO SISTEMA");

        txtId.setEditable(false);
        txtId.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txtId.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtId.setToolTipText("");

        btnValidar.setText("VALIDAR");
        btnValidar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnValidarActionPerformed(evt);
            }
        });

        btnVencimento.setText("DATA DE VENCIMENTO");
        btnVencimento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVencimentoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtId)
                    .addComponent(txtCodigo))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnVencimento)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(btnValidar, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnSair, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnValidarFirst)))
                .addGap(108, 108, 108))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnValidar, btnVencimento});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGap(2, 2, 2)
                .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnValidarFirst)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSair)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnValidar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnVencimento)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnValidarFirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnValidarFirstActionPerformed
        // Botão validar pela primeira vez
        if (txtCodigo.getText().length() < 10) {
            JOptionPane.showMessageDialog(null, "O código informado deve ter ao menos 10 digítos");
        } else {
            verificarSerial();
            DataVencimentoDAO dao = new DataVencimentoDAO();
            String idSis = txtId.getText();
            String cod = txtCodigo.getText();
            dao.checarData(idSis, cod);
        }
    }//GEN-LAST:event_btnValidarFirstActionPerformed

    private void btnSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSairActionPerformed
        // Botão sair
        dispose();
    }//GEN-LAST:event_btnSairActionPerformed

    private void btnValidarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnValidarActionPerformed
        // Botão validar de mês em mês
        if (txtCodigo.getText().length() < 10) {
            JOptionPane.showMessageDialog(null, "O código informado deve ter ao menos 10 digítos");
        } else {
            String idSistema = txtId.getText();
            String codUnlock = txtCodigo.getText();
            verificaSerialAndValida(idSistema, codUnlock);
            this.dispose();
        }
    }//GEN-LAST:event_btnValidarActionPerformed

    private void btnVencimentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVencimentoActionPerformed
        // Botão validar a data de vencimento alterada
        if (txtCodigo.getText().length() < 10) {
            JOptionPane.showMessageDialog(null, "O código informado deve ter ao menos 10 digítos");
        } else {
            String idSistema = txtId.getText();
            String codUnlock = txtCodigo.getText();
            verificarSerialDataVencimento(idSistema, codUnlock, vencimentoAtual());
        }
    }//GEN-LAST:event_btnVencimentoActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrmValidaSis.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmValidaSis.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmValidaSis.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmValidaSis.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmValidaSis().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSair;
    private javax.swing.JButton btnValidar;
    private javax.swing.JButton btnValidarFirst;
    private javax.swing.JButton btnVencimento;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextField txtId;
    // End of variables declaration//GEN-END:variables
}
