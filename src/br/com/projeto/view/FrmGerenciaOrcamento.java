/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.projeto.view;

import br.com.projeto.dao.OrcamentoDAO;
import br.com.projeto.jdbc.ConnectionFactory;
import br.com.projeto.model.Orcamento;
import static java.awt.Frame.MAXIMIZED_BOTH;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import java.awt.Toolkit;
import java.text.DateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author LUCAS
 */
public class FrmGerenciaOrcamento extends javax.swing.JFrame {

    private Connection con;

    /**
     * Creates new form FrmGerenciaOS
     */
    public void listar() throws ParseException {
        OrcamentoDAO dao = new OrcamentoDAO();
        List<Orcamento> lista = dao.listaOrcamento();
        DefaultTableModel dados = (DefaultTableModel) tabelaOrcamento.getModel();
        dados.setNumRows(0);

        for (Orcamento o : lista) {
            //Primeiro converte de String para Date
            DateFormat formatUS = new SimpleDateFormat("yyyy-mm-dd");
            Date date = formatUS.parse(o.getData());

            //Depois formata data
            DateFormat formatBR = new SimpleDateFormat("dd/mm/yyyy");
            String dateFormated = formatBR.format(date);
            dados.addRow(new Object[]{
                String.format("%04d", o.getId()),
                dateFormated,
                o.getClient().getNome(),
                o.getClient().getCpf(),
                o.getTotal_geral(),
                o.getId()
            });
        }
    }

    public void listarPorCpf() throws ParseException {
        OrcamentoDAO dao = new OrcamentoDAO();
        List<Orcamento> lista = dao.listaOrcamentoByCpf("%" + txtCpf.getText() + "%");
        DefaultTableModel dados = (DefaultTableModel) tabelaOrcamento.getModel();
        dados.setNumRows(0);

        for (Orcamento o : lista) {
            //Primeiro converte de String para Date
            DateFormat formatUS = new SimpleDateFormat("yyyy-mm-dd");
            Date date = formatUS.parse(o.getData());

            //Depois formata data
            DateFormat formatBR = new SimpleDateFormat("dd/mm/yyyy");
            String dateFormated = formatBR.format(date);
            dados.addRow(new Object[]{
                String.format("%04d", o.getId()),
                dateFormated,
                o.getClient().getNome(),
                o.getClient().getCpf(),
                o.getTotal_geral(),
                o.getId()

            });
        }
    }

    public void listarPorCnpj() throws ParseException {
        OrcamentoDAO dao = new OrcamentoDAO();
        List<Orcamento> lista = dao.listaOrcamentoByCpf("%" + txtCnpj.getText() + "%");
        DefaultTableModel dados = (DefaultTableModel) tabelaOrcamento.getModel();
        dados.setNumRows(0);

        for (Orcamento o : lista) {
            //Primeiro converte de String para Date
            DateFormat formatUS = new SimpleDateFormat("yyyy-mm-dd");
            Date date = formatUS.parse(o.getData());

            //Depois formata data
            DateFormat formatBR = new SimpleDateFormat("dd/mm/yyyy");
            String dateFormated = formatBR.format(date);
            dados.addRow(new Object[]{
                String.format("%04d", o.getId()),
                dateFormated,
                o.getClient().getNome(),
                o.getClient().getCpf(),
                o.getTotal_geral(),
                o.getId()

            });
        }
    }

    public void listarPorNome() throws ParseException {
        OrcamentoDAO dao = new OrcamentoDAO();
        List<Orcamento> lista = dao.listaOSByNome("%" + txtNome.getText() + "%");
        DefaultTableModel dados = (DefaultTableModel) tabelaOrcamento.getModel();
        dados.setNumRows(0);

        for (Orcamento o : lista) {
            //Primeiro converte de String para Date
            DateFormat formatUS = new SimpleDateFormat("yyyy-mm-dd");
            Date date = formatUS.parse(o.getData());

            //Depois formata data
            DateFormat formatBR = new SimpleDateFormat("dd/mm/yyyy");
            String dateFormated = formatBR.format(date);
            dados.addRow(new Object[]{
                String.format("%04d", o.getId()),
                dateFormated,
                o.getClient().getNome(),
                o.getClient().getCpf(),
                o.getTotal_geral(),
                o.getId()

            });
        }
    }

    public void listarPorValor() throws ParseException {
        OrcamentoDAO dao = new OrcamentoDAO();
        List<Orcamento> lista = dao.listaOSByValor(Double.parseDouble(txtValor.getText()));
        DefaultTableModel dados = (DefaultTableModel) tabelaOrcamento.getModel();
        dados.setNumRows(0);
        for (Orcamento o : lista) {
            //Primeiro converte de String para Date
            DateFormat formatUS = new SimpleDateFormat("yyyy-mm-dd");
            Date date = formatUS.parse(o.getData());

            //Depois formata data
            DateFormat formatBR = new SimpleDateFormat("dd/mm/yyyy");
            String dateFormated = formatBR.format(date);
            dados.addRow(new Object[]{
                String.format("%04d", o.getId()),
                dateFormated,
                o.getClient().getNome(),
                o.getClient().getCpf(),
                o.getTotal_geral(),
                o.getId()

            });
        }
    }

    // método para imprimir uma os 
    private void imprimirOs() {
        Connection conexao = null;
        //imprimindo uma os
        int confirma = JOptionPane.showConfirmDialog(null, "Confirma a impressão deste orçamento?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            //imprimindo um relatorio usando o ireport
            try {
                //usando a classe hashmap para criar um filtro
                HashMap filtro = new HashMap();
                filtro.put("id", Integer.parseInt(tabelaOrcamento.getValueAt(tabelaOrcamento.getSelectedRow(), 5).toString()));
                //Usando a classe jasperprint para preparar a impressão de um relatório
                JasperPrint print = JasperFillManager.fillReport("C:\\Program Files (x86)\\Sistema V8\\reports\\orcamento.jasper", filtro, con);
                //a linha abaixo exibe o relatório através do jasperviewer
                //JasperViewer.viewReport(print, false);
                JasperViewer view = new JasperViewer(print, false);
                view.setTitle("Visualizar Orçamento");
                view.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/imagens/print.png")));
                view.setZoomRatio(0.75F);
                view.setExtendedState(MAXIMIZED_BOTH);
                view.setVisible(true);
            } catch (NumberFormatException | JRException e) {
                JOptionPane.showMessageDialog(null, "Exceção: " + e);
                System.out.println(e);
            }
        }
    }

    public FrmGerenciaOrcamento() {
        initComponents();
        txtID.setVisible(false);
        this.con = new ConnectionFactory().getConnection();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFrame1 = new javax.swing.JFrame();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtNome = new javax.swing.JTextField();
        txtValor = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        txtDataInicio = new javax.swing.JFormattedTextField();
        txtDataFim = new javax.swing.JFormattedTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        txtCpf = new javax.swing.JFormattedTextField();
        jLabel7 = new javax.swing.JLabel();
        txtCnpj = new javax.swing.JFormattedTextField();
        jButton8 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelaOrcamento = new javax.swing.JTable();
        btnDetalhar = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        txtID = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Gerenciar Orcamento");
        setIconImage(Toolkit.getDefaultToolkit().getImage("C:/Users/LUCAS/Documents/NetBeansProjects/Projeto Retifica/src/imagens/icon.png"));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Gerenciar Orçamento");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Procurar Orçamento", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("Nome:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("CPF:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setText("Valor:");

        txtNome.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        txtValor.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton1.setText("Buscar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton2.setText("Buscar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton3.setText("Buscar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Data", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))); // NOI18N

        try {
            txtDataInicio.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtDataInicio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtDataInicio.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        try {
            txtDataFim.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtDataFim.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtDataFim.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setText("De:");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel6.setText("à");

        jButton4.setText("Buscar por data");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(txtDataInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39)
                        .addComponent(jLabel6)
                        .addGap(52, 52, 52)
                        .addComponent(txtDataFim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(142, 142, 142)
                        .addComponent(jButton4)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {txtDataFim, txtDataInicio});

        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtDataInicio)
                    .addComponent(jLabel6)
                    .addComponent(txtDataFim))
                .addGap(18, 18, 18)
                .addComponent(jButton4)
                .addGap(14, 14, 14))
        );

        try {
            txtCpf.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###.###.###-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtCpf.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtCpf.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCpfKeyPressed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel7.setText("CNPJ:");

        try {
            txtCnpj.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##.###.###/####-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtCnpj.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtCnpj.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCnpjKeyPressed(evt);
            }
        });

        jButton8.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton8.setText("Buscar");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtValor)
                    .addComponent(txtNome)
                    .addComponent(txtCpf, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
                    .addComponent(txtCnpj, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jButton8))
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jButton2)
                            .addComponent(txtCpf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(jButton8)
                            .addComponent(txtCnpj, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtValor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton3))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(6, 6, 6))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Orçamentos encontrados", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))); // NOI18N

        jScrollPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jScrollPane1MouseClicked(evt);
            }
        });

        tabelaOrcamento.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nº do Orça", "Data", "Nome", "CPF", "Total", "id"
            }
        ));
        tabelaOrcamento.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tabelaOrcamentoFocusGained(evt);
            }
        });
        tabelaOrcamento.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelaOrcamentoMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabelaOrcamento);
        if (tabelaOrcamento.getColumnModel().getColumnCount() > 0) {
            tabelaOrcamento.getColumnModel().getColumn(0).setMinWidth(70);
            tabelaOrcamento.getColumnModel().getColumn(0).setMaxWidth(70);
            tabelaOrcamento.getColumnModel().getColumn(5).setMinWidth(0);
            tabelaOrcamento.getColumnModel().getColumn(5).setPreferredWidth(0);
            tabelaOrcamento.getColumnModel().getColumn(5).setMaxWidth(0);
        }

        btnDetalhar.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnDetalhar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/information.png"))); // NOI18N
        btnDetalhar.setText("Detalhar");
        btnDetalhar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDetalharActionPerformed(evt);
            }
        });

        jButton6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/document(1).png"))); // NOI18N
        jButton6.setText("Apagar");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        txtID.setText("ID");

        jButton5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/report.png"))); // NOI18N
        jButton5.setText("Emitir Orçamento");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        btnEditar.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/edit.png"))); // NOI18N
        btnEditar.setText("Editar");
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });

        jButton7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/refresh.png"))); // NOI18N
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(btnDetalhar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jPanel4Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnDetalhar, jButton5, jButton6});

        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton6)
                        .addComponent(btnDetalhar)
                        .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton5)
                        .addComponent(btnEditar))
                    .addComponent(jButton7)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        try {
            // TODO add your handling code here:
            listar();
        } catch (ParseException ex) {
            Logger.getLogger(FrmGerenciaOrcamento.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_formWindowActivated

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            // TODO add your handling code here:
            listarPorCpf();
        } catch (ParseException ex) {
            Logger.getLogger(FrmGerenciaOrcamento.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void txtCpfKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCpfKeyPressed

    }//GEN-LAST:event_txtCpfKeyPressed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            // TODO add your handling code here:
            listarPorNome();
        } catch (ParseException ex) {
            Logger.getLogger(FrmGerenciaOrcamento.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {
            // TODO add your handling code here:
            listarPorValor();
        } catch (ParseException ex) {
            Logger.getLogger(FrmGerenciaOrcamento.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // Receber as datas
        try {
            String data = txtDataInicio.getText();
            String data2 = txtDataFim.getText();
            String[] split = data.split("/");
            String dataInicio = split[2] + "-" + split[1] + "-" + split[0];
            String[] split2 = data2.split("/");
            String dataFim = split2[2] + "-" + split2[1] + "-" + split2[0];

            OrcamentoDAO dao = new OrcamentoDAO();
            List<Orcamento> lista = dao.listarNotasPorPeriodo(dataInicio, dataFim);

            DefaultTableModel dados = (DefaultTableModel) tabelaOrcamento.getModel();
            dados.setNumRows(0);

            for (Orcamento o : lista) {
                dados.addRow(new Object[]{
                    String.format("%04d", o.getId()),
                    o.getData(),
                    o.getClient().getNome(),
                    o.getClient().getCpf(),
                    o.getTotal_geral(),
                    o.getId()

                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Digite datas válidas");
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void btnDetalharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDetalharActionPerformed
        // Enviar dados a outro JFrame
        FrmDetalhesOrcamento telad = new FrmDetalhesOrcamento();
        Orcamento obj = new Orcamento();
        OrcamentoDAO dao = new OrcamentoDAO();

        int id = Integer.parseInt(tabelaOrcamento.getValueAt(tabelaOrcamento.getSelectedRow(), 5).toString());
        obj = dao.buscarOrdem(id);

        telad.lblNOrdem.setText("000" + String.valueOf(obj.getId()));
        try {
            String fdg = obj.getData();
            SimpleDateFormat formato = new SimpleDateFormat("yyy-MM-dd");
            Date data = formato.parse(fdg);
            formato.applyPattern("dd/MM/yyyy");
            String dataFormatada = formato.format(data);
            telad.txtidNota.setText(String.valueOf(obj.getId()));
            telad.lblData.setText(dataFormatada);
            telad.lblTotal.setText(String.valueOf(obj.getTotal_geral()));
            telad.txtObs.setText(obj.getObservacoes());
            telad.txtTotalPecas.setText(String.valueOf(obj.getTotal_pecas()));
            telad.lblTotalMao.setText(String.valueOf(obj.getTotal_maodeobra()));
            telad.lblTotalMat.setText(String.valueOf(obj.getMateriais()));
            telad.lblTotalG.setText(String.valueOf(obj.getTotal_geral()));
            telad.lblNome.setText(obj.getClient().getNome());
            telad.lblCpf.setText(obj.getClient().getCpf());
            telad.lblRg.setText(obj.getClient().getRg());
            telad.lblTel.setText(obj.getClient().getTel());
            telad.lblTel2.setText(obj.getClient().getTel2());
            telad.lblEnd.setText(obj.getClient().getLogradouro());
            telad.lblNumero.setText(obj.getClient().getNumero());
            telad.lblBairro.setText(obj.getClient().getBairro());
            telad.lblCep.setText(obj.getClient().getCep());
            telad.lblUf.setText(obj.getClient().getEstado());
            telad.lblCidade.setText(obj.getClient().getCidade());
            telad.lblMotor.setText(obj.getMotor().getMotor());
            telad.lblTipo.setText(obj.getMotor().getTipo());
            telad.lblCilindro.setText(obj.getMotor().getCilindros());
            telad.lblCombustivel.setText(obj.getMotor().getComb());
            telad.lblGarantia.setText(obj.getMotor().getGarantia());
            telad.lblTbPeca.setText(String.valueOf(obj.getTotal_pecas()));
            telad.lblTbServices.setText(String.valueOf(obj.getTotal_maodeobra()));

            // Aqui comeca a enviar os dados para a tabela
            DefaultTableModel dados = (DefaultTableModel) telad.tbServicos.getModel();
            OrcamentoDAO dao2 = new OrcamentoDAO();
            List<Orcamento> lista = dao2.listarServicosCliente(obj.getId());
            dados.setNumRows(0);
            for (Orcamento o : lista) {
                dados.addRow(new Object[]{
                    o.getServico().getIdMao(),
                    telad.tbServicos.getRowCount() + 1,
                    o.getServico().getQuantidade(),
                    o.getServico().getServico(),
                    o.getServico().getTipo(),
                    o.getServico().getPrecounitario(),
                    o.getServico().getPrecounitario() * (o.getServico().getQuantidade())
                });
            }
            // Aqui comeca a enviar os dados para a tabela de peças
            DefaultTableModel dados2 = (DefaultTableModel) telad.tbPecas.getModel();
            OrcamentoDAO dao3 = new OrcamentoDAO();
            List<Orcamento> lista1 = dao3.listarPecasByCliente(obj.getId());
            dados2.setNumRows(0);
            for (Orcamento os : lista1) {
                dados2.addRow(new Object[]{
                    os.getPeca().getIdPeca(),
                    telad.tbPecas.getRowCount() + 1,
                    os.getPeca().getQuantidade(),
                    os.getPeca().getPeca(),
                    os.getPeca().getPrecounitario(),
                    os.getPeca().getPrecounitario() * os.getPeca().getQuantidade()
                });
            }
        } catch (Exception e) {
        }

        telad.setVisible(true);
    }//GEN-LAST:event_btnDetalharActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        try {
            imprimirOs();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void tabelaOrcamentoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tabelaOrcamentoFocusGained
        // TODO add your handling code here:
        txtID.setText(tabelaOrcamento.getValueAt(tabelaOrcamento.getSelectedRow(), 5).toString());
    }//GEN-LAST:event_tabelaOrcamentoFocusGained

    private void jScrollPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jScrollPane1MouseClicked

    private void tabelaOrcamentoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelaOrcamentoMouseClicked
        txtID.setText(tabelaOrcamento.getValueAt(tabelaOrcamento.getSelectedRow(), 5).toString());
    }//GEN-LAST:event_tabelaOrcamentoMouseClicked

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        int op = JOptionPane.showConfirmDialog(tabelaOrcamento, "ATENÇÃO\nDeseja apagar esta ordem de serviço?");
        if (op == 0) {
            OrcamentoDAO dao = new OrcamentoDAO();
            dao.inativarOrçamento(Integer.parseInt(tabelaOrcamento.getValueAt(tabelaOrcamento.getSelectedRow(), 5).toString()));
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        // Editar ordem de serviço

        int id = Integer.parseInt(tabelaOrcamento.getValueAt(tabelaOrcamento.getSelectedRow(), 5).toString());
        int linha = tabelaOrcamento.getSelectedRow();
        //System.out.println("Nego ney"+tabelaOrdem.getSelectedRow());
        if (linha == -1) {
            JOptionPane.showMessageDialog(null, "Selecione uma nota");
        } else {
            try {
                FrmOrcamento Ordem = new FrmOrcamento();
                Orcamento obj = new Orcamento();
                OrcamentoDAO dao = new OrcamentoDAO();
                obj = dao.buscarOrdem(id);
                String fdg = obj.getData();
                SimpleDateFormat formato = new SimpleDateFormat("yyy-MM-dd");
                Date data = formato.parse(fdg);
                formato.applyPattern("dd/MM/yyyy");
                String dataFormatada = formato.format(data);
                Ordem.lblOrcamento.setText(String.valueOf(obj.getId()));
                Ordem.txtIdOr.setText(String.valueOf(obj.getId()));
                Ordem.txtNome.setText(obj.getClient().getNome());
                Ordem.txtCpf.setText(obj.getClient().getCpf());
                Ordem.txtRG.setText(obj.getClient().getRg());
                Ordem.txtTel.setText(obj.getClient().getTel());
                Ordem.txtTel2.setText(obj.getClient().getTel2());
                Ordem.txtCep.setText(obj.getClient().getCep());
                Ordem.txtID.setText(String.valueOf(obj.getClient().getIdCliente()));
                Ordem.txtLogradouro.setText(obj.getClient().getLogradouro());
                Ordem.txtNum.setText(obj.getClient().getNumero());
                Ordem.txtBairro.setText(obj.getClient().getBairro());
                Ordem.txtCidade.setText(obj.getClient().getCidade());
                Ordem.txtEstado.setText(obj.getClient().getEstado());
                Ordem.txtObs.setText(obj.getObservacoes());
                Ordem.txtData.setText(dataFormatada);
                // Dados do motor
                Ordem.txtIDM.setText(String.valueOf(obj.getMotor().getIdMotor()));
                Ordem.txtMotor.setText(obj.getMotor().getMotor());
                Ordem.txtCilindro.setText(obj.getMotor().getCilindros());
                Ordem.txtCombustivel.setText(obj.getMotor().getComb());
                Ordem.txtTipo.setText(obj.getMotor().getTipo());
                Ordem.txtGarantia.setText(obj.getMotor().getGarantia());
                // Tabela Serviços
                OrcamentoDAO dao1 = new OrcamentoDAO();
                List<Orcamento> lista = dao1.listarServicosCliente(id);
                Ordem.modelo.setNumRows(0);
                for (Orcamento o : lista) {
                    Ordem.modelo.addRow(new String[]{
                        String.valueOf(Ordem.tabelaOrcamentoServico.getRowCount() + 1),
                        String.valueOf(o.getServico().getQuantidade()),
                        String.valueOf(o.getServico().getServico()),
                        String.valueOf(o.getServico().getTipo()),
                        String.valueOf(o.getServico().getPrecounitario() * (o.getServico().getQuantidade())),
                        String.valueOf(o.getServico().getIdMao()),
                        String.valueOf(o.getServico().getPrecounitario())
                    });
                    Ordem.tabelaOrcamentoServico.setModel(Ordem.modelo);
                }
                // Tabela Peças
                OrcamentoDAO dao2 = new OrcamentoDAO();
                List<Orcamento> lista1 = dao2.listarPecasByCliente(id);
                Ordem.modelPecas.setNumRows(0);
                for (Orcamento os : lista1) {
                    Ordem.modelPecas.addRow(new String[]{
                        String.valueOf(Ordem.tabelaOrcamentoPeca.getRowCount() + 1),
                        String.valueOf(os.getPeca().getQuantidade()),
                        String.valueOf(os.getPeca().getPeca()),
                        String.valueOf(os.getPeca().getPrecounitario() * os.getPeca().getQuantidade()),
                        String.valueOf(os.getId()),
                        String.valueOf(os.getPeca().getPrecounitario() * os.getPeca().getQuantidade())
                    });
                    Ordem.tabelaOrcamentoPeca.setModel(Ordem.modelPecas);
                }

                //Parte de totais
                Ordem.txtTMaoObra.setText(String.valueOf(obj.getTotal_maodeobra()));
                Ordem.txtTotalPecas.setText(String.valueOf(obj.getTotal_pecas()));
                Ordem.txtMateriais.setText(String.valueOf(obj.getMateriais()));
                Ordem.txtTotalNota.setText(String.valueOf(obj.getTotal_geral()));

                //Desabilita o botão de emitir a Ordem de serviço
                Ordem.btnEmitir.setEnabled(false);
                Ordem.btnEmitir.setVisible(false);
                Ordem.btnEditar.setVisible(true);
                Ordem.btnEditar.setEnabled(true);
            } catch (ParseException e) {
                e.getErrorOffset();
            }
        }
    }//GEN-LAST:event_btnEditarActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // Botão atualizar
        FrmGerenciaOrcamento tela = new FrmGerenciaOrcamento();
        this.dispose();
        tela.setVisible(true);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void txtCnpjKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCnpjKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCnpjKeyPressed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        try {
            // TODO add your handling code here:
            listarPorCnpj();
        } catch (ParseException ex) {
            Logger.getLogger(FrmGerenciaOrcamento.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton8ActionPerformed

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
            java.util.logging.Logger.getLogger(FrmGerenciaOrcamento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmGerenciaOrcamento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmGerenciaOrcamento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmGerenciaOrcamento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmGerenciaOrcamento().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDetalhar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JTable tabelaOrcamento;
    private javax.swing.JFormattedTextField txtCnpj;
    private javax.swing.JFormattedTextField txtCpf;
    private javax.swing.JFormattedTextField txtDataFim;
    private javax.swing.JFormattedTextField txtDataInicio;
    private javax.swing.JTextField txtID;
    private javax.swing.JTextField txtNome;
    private javax.swing.JTextField txtValor;
    // End of variables declaration//GEN-END:variables
}
