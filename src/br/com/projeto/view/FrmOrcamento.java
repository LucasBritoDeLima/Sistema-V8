/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.projeto.view;

import br.com.projeto.dao.ClienteDAO;
import br.com.projeto.dao.MotorDAO;
import br.com.projeto.dao.OrcamentoDAO;
import br.com.projeto.dao.OrdemServicoDAO;
import br.com.projeto.dao.PecaDAO;
import br.com.projeto.dao.ServicoDAO;
import br.com.projeto.jdbc.ConnectionFactory;
import br.com.projeto.model.Cliente;
import br.com.projeto.model.JtextFieldSomenteNumeros;
import java.awt.Toolkit;
import br.com.projeto.model.Motor;
import br.com.projeto.model.Orcamento;
import br.com.projeto.model.Peca;
import br.com.projeto.model.Servico;
import br.com.projeto.model.TPeca;
import br.com.projeto.model.TServico;
import br.com.projeto.tablemodel.PecaTableModelOS;
import br.com.projeto.tablemodel.ServicoTableModelOS;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author LUCAS
 */
public class FrmOrcamento extends javax.swing.JFrame {

    public Connection con;
    OrdemServicoDAO ods = new OrdemServicoDAO();
    /**
     * Creates new form FrmOS
     */

    /**
     * ***************************************************************************************************
     */
    // Implementações antigas / SEM VALIDADE
    String colunas[] = {"Nº", "Quantidade", "Descrição", "Categoria", "Total", "Cód sis", "Preço unitário"};
    String colPecas[] = {"Nº", "Quantidade", "Descrição", "Total", "Cód sis", "Preço unitario"};
    // Cria um DefaultTableModel para manipular os dados do JTable. Os parâmetros são um array que identifica as colunas e um int que seta o número inicial que linhas
    DefaultTableModel modelo = new DefaultTableModel(colunas, 0);
    DefaultTableModel modelPecas = new DefaultTableModel(colPecas, 0);
    /**
     * *****************************************************************************
     */
    // NOVAS IMPLEMENTAÇÕES - QUE VÃO COMEÇAR A VALER AGORA
    //Criar array para receber os ids da tabela de peças
    List<Integer> codigo = new ArrayList<>();
    // Modelo personalizado
    public ServicoTableModelOS tableModel;
    Set<TServico> setServico = new HashSet<TServico>();
    public PecaTableModelOS tableModel2;
    Set<TPeca> setPeca = new HashSet<TPeca>();

    public void getSum() {
        double sum = 0;
        for (double i = 0; i < tabelaOrcamentoServico.getRowCount(); i++) {
            sum = sum + Double.parseDouble(tabelaOrcamentoServico.getValueAt((int) i, 3).toString());
        }
        txtTMaoObra.setText(Double.toString(sum));
    }

    public void getSumProducts() {
        double sum = 0;
        for (double i = 0; i < tabelaOrcamentoPeca.getRowCount(); i++) {
            sum = sum + Double.parseDouble(tabelaOrcamentoPeca.getValueAt((int) i, 2).toString());
        }
        txtTotalPecas.setText(Double.toString(sum));
    }

    public void listarPeca() {
        PecaDAO dao = new PecaDAO();
        List<Peca> lista = dao.listarPeca();
        DefaultTableModel dados = (DefaultTableModel) tabelaPeca.getModel();
        dados.setNumRows(0);

        for (Peca p : lista) {
            dados.addRow(new Object[]{
                p.getIdPeca(),
                p.getPeca()
            });
        }
    }

    public void escolherMotorByCliente() {
        MotorDAO dao = new MotorDAO();
        List<Motor> lista = dao.listarMotorByCliente(Integer.parseInt(txtID.getText()));
        DefaultTableModel dados = (DefaultTableModel) tabelaMotor.getModel();
        dados.setNumRows(0);

        for (Motor c : lista) {
            dados.addRow(new Object[]{
                c.getIdMotor(),
                c.getMotor(),
                c.getTipo(),
                c.getCilindros(),
                c.getComb(),
                c.getGarantia()
            });
        }

    }

    public void listarServico() {
        ServicoDAO dao = new ServicoDAO();
        List<Servico> lista = dao.listarServicoPorCategoria(String.valueOf(txtCategoria.getSelectedItem()));
        DefaultTableModel dados = (DefaultTableModel) tabelaServico.getModel();
        dados.setNumRows(0);

        for (Servico c : lista) {
            dados.addRow(new Object[]{
                c.getIdMao(),
                c.getServico(),
                c.getTipo()
            });
        }
    }

    public FrmOrcamento() {
        initComponents();
        this.tableModel = new ServicoTableModelOS();
        this.tableModel2 = new PecaTableModelOS();
        this.tabelaOrcamentoServico.setModel(this.tableModel);
        this.tabelaOrcamentoPeca.setModel(this.tableModel2);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setVisible(true);
        Color corFrame = new Color(255, 255, 255);
        getContentPane().setBackground(corFrame);
        txtID.setVisible(false);
        txtIDM.setVisible(false);
        Date data = new Date();
        DateFormat dfs = DateFormat.getDateInstance(DateFormat.MEDIUM);
        txtData.setText(dfs.format(data));
        this.con = new ConnectionFactory().getConnection();
        try {
            String sql = "SELECT COUNT(id) from `tb_orcamento`";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                String sum = rs.getString("count(id)");
                int numero_nota = Integer.parseInt(sum) + 1;
                lblOrcamento.setText(String.valueOf(numero_nota));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e);
        }
        btnEditar.setVisible(false);
        btnLinha.setVisible(false);
        txtIdOr.setVisible(false);
        txtIDS.setVisible(false);
        txtCategoriaServico.setVisible(false);
        txtIdPeca.setVisible(false);
        txtMateriais.setText("00.00");
        //ods.temPeca(5, 0, 0, 0);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtNome = new javax.swing.JTextField();
        txtCpf = new javax.swing.JFormattedTextField();
        btnBusca = new javax.swing.JButton();
        txtRG = new javax.swing.JTextField();
        txtTel = new javax.swing.JFormattedTextField();
        txtTel2 = new javax.swing.JFormattedTextField();
        txtCep = new javax.swing.JFormattedTextField();
        txtLogradouro = new javax.swing.JTextField();
        txtNum = new javax.swing.JTextField();
        txtBairro = new javax.swing.JTextField();
        txtCidade = new javax.swing.JTextField();
        txtEstado = new javax.swing.JTextField();
        txtID = new javax.swing.JTextField();
        btnLinha = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txtMotor = new javax.swing.JTextField();
        txtTipo = new javax.swing.JTextField();
        txtGarantia = new javax.swing.JTextField();
        txtCombustivel = new javax.swing.JTextField();
        txtCilindro = new javax.swing.JTextField();
        txtIDM = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelaMotor = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        txtData = new javax.swing.JFormattedTextField();
        jLabel31 = new javax.swing.JLabel();
        lblOrcamento = new javax.swing.JLabel();
        txtIdOr = new javax.swing.JTextField();
        jPanel20 = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        txtObs = new javax.swing.JTextArea();
        jPanel5 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabelaServico = new javax.swing.JTable();
        jLabel18 = new javax.swing.JLabel();
        txtCategoria = new javax.swing.JComboBox();
        jButton2 = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        lblSubTotal = new javax.swing.JLabel();
        txtQtd = new JtextFieldSomenteNumeros();
        txtPrecoS = new JtextFieldSomenteNumeros();
        jLabel22 = new javax.swing.JLabel();
        txtServico = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        txtIDS = new javax.swing.JTextField();
        txtCategoriaServico = new javax.swing.JTextField();
        jPanel14 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tabelaOrcamentoServico = new javax.swing.JTable();
        btnRemoverServicos = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tabelaPeca = new javax.swing.JTable();
        jButton4 = new javax.swing.JButton();
        jPanel18 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        lblSubTotalPeca = new javax.swing.JLabel();
        txtQtdPeca = new JtextFieldSomenteNumeros();
        txtPrecoPeca = new JtextFieldSomenteNumeros();
        jLabel27 = new javax.swing.JLabel();
        txtPeca = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        txtIdPeca = new javax.swing.JTextField();
        jPanel15 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tabelaOrcamentoPeca = new javax.swing.JTable();
        btnAlterarPecas = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        txtTMaoObra = new javax.swing.JTextField();
        txtTotalPecas = new javax.swing.JTextField();
        txtMateriais = new JtextFieldSomenteNumeros();
        txtTotalNota = new javax.swing.JTextField();
        jButton6 = new javax.swing.JButton();
        jPanel19 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        btnEmitir = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Emitir: Orçamento");
        setIconImage(Toolkit.getDefaultToolkit().getImage("C:/Users/LUCAS/Documents/NetBeansProjects/Projeto Retifica/src/imagens/icon.png"));
        setIconImages(null);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(3, 100, 3));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 204, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Orçamento");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1)
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(null, "Dados do Cliente", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("Nome:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("Telefones:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setText("CPF:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setText("Rua:");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel6.setText("CEP:");

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel9.setText("Nº:");

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel10.setText("Cidade:");

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel11.setText("RG:");

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel12.setText("Bairro:");

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel13.setText("Estado:");

        txtNome.setEditable(false);
        txtNome.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

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

        btnBusca.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnBusca.setText("Pesquisar");
        btnBusca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscaActionPerformed(evt);
            }
        });

        txtRG.setEditable(false);
        txtRG.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        txtTel.setEditable(false);
        try {
            txtTel.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##) # ####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtTel.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtTel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        txtTel2.setEditable(false);
        try {
            txtTel2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##) # ####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtTel2.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtTel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        txtCep.setEditable(false);
        try {
            txtCep.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##.###-###")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtCep.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtCep.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        txtLogradouro.setEditable(false);
        txtLogradouro.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        txtNum.setEditable(false);
        txtNum.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        txtBairro.setEditable(false);
        txtBairro.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        txtCidade.setEditable(false);
        txtCidade.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        txtEstado.setEditable(false);
        txtEstado.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        txtID.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtID.setText("ID");

        btnLinha.setText("Numero de linhas");
        btnLinha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLinhaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3)
                    .addComponent(jLabel6)
                    .addComponent(jLabel5)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtCidade, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNum, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 272, Short.MAX_VALUE)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13))
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtBairro, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(txtEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(txtLogradouro)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtCep, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE)
                            .addComponent(txtCpf, javax.swing.GroupLayout.Alignment.LEADING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(btnBusca)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnLinha)
                                .addGap(104, 104, 104)
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtRG))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtTel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtID, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addComponent(txtNome, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtCpf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(btnBusca)
                    .addComponent(txtRG, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLinha))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtTel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtCep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtLogradouro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel12)
                    .addComponent(txtBairro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtCidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(txtEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(47, Short.MAX_VALUE))
        );

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Dados do Motor", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))); // NOI18N

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel7.setText("Motor:");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel8.setText("Cilindros:");

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel14.setText("Combustível:");

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel15.setText("Garantia:");

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel16.setText("Tipo:");

        txtMotor.setEditable(false);
        txtMotor.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        txtTipo.setEditable(false);
        txtTipo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        txtGarantia.setEditable(false);
        txtGarantia.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        txtCombustivel.setEditable(false);
        txtCombustivel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        txtCilindro.setEditable(false);
        txtCilindro.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        txtIDM.setEditable(false);
        txtIDM.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtIDM.setText("ID M");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCombustivel))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtGarantia, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtIDM, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel16))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtMotor, javax.swing.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)
                                    .addComponent(txtTipo)
                                    .addComponent(txtCilindro))))))
                .addGap(0, 42, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtMotor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtCilindro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(txtCombustivel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(txtTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(txtGarantia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtIDM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Selecione o Motor", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))); // NOI18N

        tabelaMotor.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Motor", "Tipo", "Cilindros", "Combustível", "Garantia"
            }
        ));
        jScrollPane1.setViewportView(tabelaMotor);
        if (tabelaMotor.getColumnModel().getColumnCount() > 0) {
            tabelaMotor.getColumnModel().getColumn(0).setMinWidth(0);
            tabelaMotor.getColumnModel().getColumn(0).setPreferredWidth(0);
            tabelaMotor.getColumnModel().getColumn(0).setMaxWidth(0);
        }

        jButton1.setText("Selecionar Motor");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        btnNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/edit-tools_1.png"))); // NOI18N
        btnNext.setText("Próximo");
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 112, Short.MAX_VALUE)
                        .addComponent(btnNext)))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(btnNext)))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("Dados Do Cliente", jPanel3);

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Dados da Nota", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))); // NOI18N

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel17.setText("Data:");

        txtData.setEditable(false);
        try {
            txtData.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtData.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jLabel31.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel31.setText("Número do Orçamento.:");

        lblOrcamento.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblOrcamento.setForeground(new java.awt.Color(255, 0, 0));
        lblOrcamento.setText("0");

        txtIdOr.setText("id orc");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel31)
                        .addGap(18, 18, 18)
                        .addComponent(lblOrcamento, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addGap(18, 18, 18)
                        .addComponent(txtData, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtIdOr, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(364, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31)
                    .addComponent(lblOrcamento))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17)
                    .addComponent(txtIdOr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel20.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Observações", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))); // NOI18N

        jLabel33.setText("Observações:");

        txtObs.setColumns(20);
        txtObs.setFont(new java.awt.Font("Monospaced", 0, 14)); // NOI18N
        txtObs.setRows(5);
        jScrollPane6.setViewportView(txtObs);

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addComponent(jLabel33)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane6))
                .addContainerGap())
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel33)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 399, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Dados da Nota", jPanel4);

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder("Serviços Cadastrados"));

        tabelaServico.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Serviço", "Tipo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabelaServico.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelaServicoMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tabelaServico);
        if (tabelaServico.getColumnModel().getColumnCount() > 0) {
            tabelaServico.getColumnModel().getColumn(0).setMinWidth(0);
            tabelaServico.getColumnModel().getColumn(0).setPreferredWidth(0);
            tabelaServico.getColumnModel().getColumn(0).setMaxWidth(0);
        }

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel18.setText("Categoria:");

        txtCategoria.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        txtCategoria.setForeground(new java.awt.Color(51, 51, 255));
        txtCategoria.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "BLOCO", "BIELAS", "CABEÇOTES", "COMANDO", "VIRABREQUIM" }));
        txtCategoria.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                txtCategoriaItemStateChanged(evt);
            }
        });
        txtCategoria.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                txtCategoriaAncestorAdded(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/edit-tools.png"))); // NOI18N
        jButton2.setText("Adicionar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 528, Short.MAX_VALUE)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton2)))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(txtCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2)
                .addGap(7, 7, 7))
        );

        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder("Quantidade e preço"));

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel19.setText("Quantidade:");

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel20.setText("Preço:");

        jLabel21.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel21.setText("Subtotal:");

        lblSubTotal.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblSubTotal.setText("0");

        txtQtd.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtQtd.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtQtdKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtQtdKeyReleased(evt);
            }
        });

        txtPrecoS.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtPrecoS.setText("0");
        txtPrecoS.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPrecoSKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPrecoSKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrecoSKeyTyped(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel22.setText("Serviço:");

        txtServico.setEditable(false);
        txtServico.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jButton3.setText("Enviar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        txtIDS.setText("ID");

        txtCategoriaServico.setText("Categoria");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblSubTotal))
                    .addComponent(jButton3))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19)
                    .addComponent(jLabel20)
                    .addComponent(jLabel22))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtPrecoS)
                    .addComponent(txtQtd)
                    .addComponent(txtServico)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtIDS, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCategoriaServico, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 45, Short.MAX_VALUE))))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(txtQtd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(txtServico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPrecoS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtIDS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCategoriaServico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSubTotal, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel21, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addGap(8, 8, 8))
        );

        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Nota", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))); // NOI18N

        tabelaOrcamentoServico.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Quantidade", "Descrição", "Categoria", "Total", "Cód Sis", "Preço unitário"
            }
        ));
        jScrollPane3.setViewportView(tabelaOrcamentoServico);
        if (tabelaOrcamentoServico.getColumnModel().getColumnCount() > 0) {
            tabelaOrcamentoServico.getColumnModel().getColumn(4).setMinWidth(0);
            tabelaOrcamentoServico.getColumnModel().getColumn(4).setPreferredWidth(0);
            tabelaOrcamentoServico.getColumnModel().getColumn(4).setMaxWidth(0);
        }

        btnRemoverServicos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/trash.png"))); // NOI18N
        btnRemoverServicos.setText("Remover");
        btnRemoverServicos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoverServicosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(btnRemoverServicos)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRemoverServicos))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Serviços", jPanel5);

        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder("Peças Cadastradas"));

        tabelaPeca.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Peça"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabelaPeca.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelaPecaMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tabelaPeca);
        if (tabelaPeca.getColumnModel().getColumnCount() > 0) {
            tabelaPeca.getColumnModel().getColumn(0).setMinWidth(0);
            tabelaPeca.getColumnModel().getColumn(0).setPreferredWidth(0);
            tabelaPeca.getColumnModel().getColumn(0).setMaxWidth(0);
        }

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/edit-tools.png"))); // NOI18N
        jButton4.setText("Adicionar");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 528, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton4)))
                .addContainerGap())
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jButton4)
                .addGap(7, 7, 7))
        );

        jPanel18.setBorder(javax.swing.BorderFactory.createTitledBorder("Quantidade e preço"));

        jLabel24.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel24.setText("Quantidade:");

        jLabel25.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel25.setText("Preço:");

        jLabel26.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel26.setText("Subtotal:");

        lblSubTotalPeca.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblSubTotalPeca.setText("0");

        txtQtdPeca.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtQtdPeca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtQtdPecaKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtQtdPecaKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtQtdPecaKeyTyped(evt);
            }
        });

        txtPrecoPeca.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtPrecoPeca.setText("0");
        txtPrecoPeca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPrecoPecaKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPrecoPecaKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrecoPecaKeyTyped(evt);
            }
        });

        jLabel27.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel27.setText("Peça:");

        txtPeca.setEditable(false);
        txtPeca.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jButton5.setText("Enviar");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        txtIdPeca.setText("ID");

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addComponent(jLabel26)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblSubTotalPeca))
                    .addComponent(jButton5))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel24)
                    .addComponent(jLabel25)
                    .addComponent(jLabel27))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtPrecoPeca)
                    .addComponent(txtQtdPeca)
                    .addComponent(txtPeca)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addComponent(txtIdPeca, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 81, Short.MAX_VALUE))))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(txtQtdPeca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel27))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPeca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPrecoPeca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtIdPeca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 70, Short.MAX_VALUE)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSubTotalPeca, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel26, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5)
                .addGap(8, 8, 8))
        );

        jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Nota", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))); // NOI18N

        tabelaOrcamentoPeca.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Quantidade", "Descrição", "Total", "Cód sis", "Preço unitario"
            }
        ));
        jScrollPane5.setViewportView(tabelaOrcamentoPeca);
        if (tabelaOrcamentoPeca.getColumnModel().getColumnCount() > 0) {
            tabelaOrcamentoPeca.getColumnModel().getColumn(3).setMinWidth(0);
            tabelaOrcamentoPeca.getColumnModel().getColumn(3).setPreferredWidth(0);
            tabelaOrcamentoPeca.getColumnModel().getColumn(3).setMaxWidth(0);
        }

        btnAlterarPecas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/trash.png"))); // NOI18N
        btnAlterarPecas.setText("Remover");
        btnAlterarPecas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlterarPecasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 743, Short.MAX_VALUE)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(btnAlterarPecas)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAlterarPecas))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Produtos", jPanel6);

        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder("Totais"));

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        jLabel23.setText("Total da Mão de Obra:");

        jLabel28.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        jLabel28.setText("Total de Peças:");

        jLabel29.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        jLabel29.setText("Materiais:");

        jLabel30.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(255, 0, 0));
        jLabel30.setText("TOTAL DA NOTA:");

        txtTMaoObra.setEditable(false);
        txtTMaoObra.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N

        txtTotalPecas.setEditable(false);
        txtTotalPecas.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N

        txtMateriais.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N

        txtTotalNota.setEditable(false);
        txtTotalNota.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        txtTotalNota.setForeground(new java.awt.Color(255, 0, 0));

        jButton6.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 0, 0));
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/technological.png"))); // NOI18N
        jButton6.setText("Calcular Totais");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(108, 108, 108)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel23)
                    .addComponent(jLabel28, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel29, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel30, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                    .addComponent(txtTotalPecas)
                    .addComponent(txtTMaoObra)
                    .addComponent(txtMateriais)
                    .addComponent(txtTotalNota, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(txtTMaoObra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(txtTotalPecas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(txtMateriais, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(txtTotalNota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(46, 46, 46)
                .addComponent(jButton6)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel19.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Emitir", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))); // NOI18N

        jLabel32.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel32.setText("Ao concluir seu orçamento clique no botão emitir");

        btnEmitir.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnEmitir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/copywriting.png"))); // NOI18N
        btnEmitir.setText("Emitir orçamento");
        btnEmitir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmitirActionPerformed(evt);
            }
        });

        btnEditar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/edit.png"))); // NOI18N
        btnEditar.setText("Editar orçamento");
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, 743, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(btnEditar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnEmitir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel32)
                .addGap(34, 34, 34)
                .addComponent(btnEmitir)
                .addGap(28, 28, 28)
                .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Totais", jPanel7);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtCpfKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCpfKeyPressed

    }//GEN-LAST:event_txtCpfKeyPressed

    private void btnBuscaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscaActionPerformed
        // Botão pesquisar por cpf
        String cpf = txtCpf.getText();
        Cliente obj = new Cliente();
        ClienteDAO dao = new ClienteDAO();

        obj = dao.pesquisaClientePorCpf(cpf);
        if (obj.getCpf() != null) {
            //Exibe os valores nos campos
            txtID.setText(String.valueOf(obj.getIdCliente()));
            txtNome.setText(obj.getNome());
            txtCpf.setText(obj.getCpf());
            txtRG.setText(obj.getRg());
            txtTel.setText(obj.getTel());
            txtTel2.setText(obj.getTel2());
            txtLogradouro.setText(obj.getLogradouro());
            txtNum.setText(obj.getNumero());
            txtBairro.setText(obj.getBairro());
            txtCep.setText(obj.getCep());
            txtEstado.setText(obj.getEstado());
            txtCidade.setText(obj.getCidade());
            escolherMotorByCliente();
        } else {
            JOptionPane.showMessageDialog(null, "Cliente não encontrado");
        }
        txtIDM.setText(null);
        txtMotor.setText(null);
        txtCilindro.setText(null);
        txtCombustivel.setText(null);
        txtTipo.setText(null);
        txtGarantia.setText(null);

    }//GEN-LAST:event_btnBuscaActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // Botão selecionar motor - envia os dados da tabela para os campos de texto
        String id;
        String motor;
        String tipo;
        String cilindros;
        String combustivel;
        String garantia;
        id = tabelaMotor.getValueAt(tabelaMotor.getSelectedRow(), 0).toString();
        motor = tabelaMotor.getValueAt(tabelaMotor.getSelectedRow(), 1).toString();
        tipo = tabelaMotor.getValueAt(tabelaMotor.getSelectedRow(), 2).toString();
        cilindros = tabelaMotor.getValueAt(tabelaMotor.getSelectedRow(), 3).toString();
        combustivel = tabelaMotor.getValueAt(tabelaMotor.getSelectedRow(), 4).toString();
        garantia = tabelaMotor.getValueAt(tabelaMotor.getSelectedRow(), 5).toString();
        txtIDM.setText(id);
        txtMotor.setText(motor);
        txtCilindro.setText(cilindros);
        txtCombustivel.setText(combustivel);
        txtTipo.setText(tipo);
        txtGarantia.setText(garantia);

    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        // Avançar para a próxima página
        jTabbedPane1.setSelectedIndex(1);
    }//GEN-LAST:event_btnNextActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
        listarServico();
        listarPeca();
    }//GEN-LAST:event_formWindowActivated

    private void txtCategoriaAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_txtCategoriaAncestorAdded
        // TODO add your handling code here:
        listarServico();
    }//GEN-LAST:event_txtCategoriaAncestorAdded

    private void txtCategoriaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_txtCategoriaItemStateChanged
        // TODO add your handling code here:
        listarServico();
    }//GEN-LAST:event_txtCategoriaItemStateChanged

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // Botão adicionar item ao painel ao lado
        String servico;
        servico = tabelaServico.getValueAt(tabelaServico.getSelectedRow(), 1).toString();
        int id;
        id = Integer.parseInt(tabelaServico.getValueAt(tabelaServico.getSelectedRow(), 0).toString());
        String categoria;
        categoria = tabelaServico.getValueAt(tabelaServico.getSelectedRow(), 2).toString();
        txtServico.setText(servico);
        txtIDS.setText(String.valueOf(id));
        txtCategoriaServico.setText(categoria);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void txtPrecoSKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecoSKeyPressed
        // TODO add your handling code here:
        if (!txtPrecoS.getText().isEmpty() && !txtQtd.getText().isEmpty()) {
            float quantidade = Float.parseFloat(txtQtd.getText());
            float preco = Float.parseFloat(txtPrecoS.getText());
            float subtotal_servico;
            subtotal_servico = quantidade * preco;
            lblSubTotal.setText(String.valueOf(subtotal_servico));
        }
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (txtPrecoS.getText().isEmpty() || txtQtd.getText().isEmpty() || txtIDS.getText().isEmpty() || txtCategoriaServico.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Preencha os campos em branco");
            } else {
                double precoDoServico = Double.parseDouble(txtPrecoS.getText());
                double quantidade = Double.parseDouble(txtQtd.getText());
                String categoria = txtCategoriaServico.getText();
                String desc = txtServico.getText();
                double total = precoDoServico * quantidade;
                int id = Integer.parseInt(txtIDS.getText());
                double precoUnitario = Double.parseDouble(txtPrecoS.getText());

                // Aqui é a parte onde se envia os dados para a tabela de serviços
                // Criamos um hashset que armazena tudo o que enviamos para a tabela
                // Esse HashSet é convertido em um ArrayList para ser mostrado na JTable
                // Com isso impedimos a entrada de itens repetidos na tabela
                // E por conseguinte evitamos entradas duplicadas com o banco de dados
                TServico serv = new TServico(quantidade, desc, categoria, total, id, precoUnitario);
                setServico.add(serv);
                ArrayList<TServico> servicos = null;
                servicos = new ArrayList<>(setServico);
                this.tableModel.listaDeServicos = servicos;
                tableModel.fireTableDataChanged();

                //GuiUtils gu = new GuiUtils();
                //gu.selectAndScroll(tabelaOServico, tabelaOServico.getRowCount()-1);
                tabelaOrcamentoServico.getColumnModel().getColumn(4).setMinWidth(0);
                tabelaOrcamentoServico.getColumnModel().getColumn(4).setMaxWidth(0);
                txtQtd.setText("0");
                txtServico.setText(null);
                txtPrecoS.setText("0");
                txtIDS.setText(null);
                txtCategoriaServico.setText(null);
            }

        }
    }//GEN-LAST:event_txtPrecoSKeyPressed

    private void txtPrecoSKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecoSKeyReleased
        // TODO add your handling code here:
        if (!txtPrecoS.getText().isEmpty() && !txtQtd.getText().isEmpty()) {
            float quantidade = Float.parseFloat(txtQtd.getText());
            float preco = Float.parseFloat(txtPrecoS.getText());
            float subtotal_servico;
            subtotal_servico = quantidade * preco;
            lblSubTotal.setText(String.valueOf(subtotal_servico));
        }
    }//GEN-LAST:event_txtPrecoSKeyReleased

    private void txtPrecoSKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecoSKeyTyped
        // TODO add your handling code here:

    }//GEN-LAST:event_txtPrecoSKeyTyped

    private void txtQtdKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtQtdKeyPressed
        // TODO add your handling code here:
        if (!txtPrecoS.getText().isEmpty() && !txtQtd.getText().isEmpty()) {
            float quantidade = Float.parseFloat(txtQtd.getText());
            float preco = Float.parseFloat(txtPrecoS.getText());
            float subtotal_servico;
            subtotal_servico = quantidade * preco;
            lblSubTotal.setText(String.valueOf(subtotal_servico));
        }
    }//GEN-LAST:event_txtQtdKeyPressed

    private void txtQtdKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtQtdKeyReleased
        // TODO add your handling code here:
        if (!txtPrecoS.getText().isEmpty() && !txtQtd.getText().isEmpty()) {
            float quantidade = Float.parseFloat(txtQtd.getText());
            float preco = Float.parseFloat(txtPrecoS.getText());
            float subtotal_servico;
            subtotal_servico = quantidade * preco;
            lblSubTotal.setText(String.valueOf(subtotal_servico));
        }
    }//GEN-LAST:event_txtQtdKeyReleased

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // Botão enviar para a tabela
        if (txtPrecoS.getText().isEmpty() || txtQtd.getText().isEmpty() || txtIDS.getText().isEmpty() || txtCategoriaServico.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Preencha os campos em branco");
        } else {
            double precoDoServico = Double.parseDouble(txtPrecoS.getText());
            double quantidade = Double.parseDouble(txtQtd.getText());
            String categoria = txtCategoriaServico.getText();
            String desc = txtServico.getText();
            double total = precoDoServico * quantidade;
            int id = Integer.parseInt(txtIDS.getText());
            double precoUnitario = Double.parseDouble(txtPrecoS.getText());

            // Aqui é a parte onde se envia os dados para a tabela de serviços
            // Criamos um hashset que armazena tudo o que enviamos para a tabela
            // Esse HashSet é convertido em um ArrayList para ser mostrado na JTable
            // Com isso impedimos a entrada de itens repetidos na tabela
            // E por conseguinte evitamos entradas duplicadas com o banco de dados
            TServico serv = new TServico(quantidade, desc, categoria, total, id, precoUnitario);
            setServico.add(serv);
            ArrayList<TServico> servicos = null;
            servicos = new ArrayList<>(setServico);
            this.tableModel.listaDeServicos = servicos;
            tableModel.fireTableDataChanged();

            //GuiUtils gu = new GuiUtils();
            //gu.selectAndScroll(tabelaOServico, tabelaOServico.getRowCount()-1);
            tabelaOrcamentoServico.getColumnModel().getColumn(4).setMinWidth(0);
            tabelaOrcamentoServico.getColumnModel().getColumn(4).setMaxWidth(0);
            txtQtd.setText("0");
            txtServico.setText(null);
            txtPrecoS.setText("0");
            txtIDS.setText(null);
            txtCategoriaServico.setText(null);
        }

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // Transferir pecas para area ao lado
        String peca;
        peca = tabelaPeca.getValueAt(tabelaPeca.getSelectedRow(), 1).toString();
        int id;
        id = Integer.parseInt(tabelaPeca.getValueAt(tabelaPeca.getSelectedRow(), 0).toString());
        txtPeca.setText(peca);
        txtIdPeca.setText(String.valueOf(id));
    }//GEN-LAST:event_jButton4ActionPerformed

    private void txtQtdPecaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtQtdPecaKeyPressed
        // TODO add your handling code here:
        if (!txtQtdPeca.getText().isEmpty() && !txtPrecoPeca.getText().isEmpty() && !txtIdPeca.getText().isEmpty()) {
            float quantidade = Float.parseFloat(txtQtdPeca.getText());
            float preco = Float.parseFloat(txtPrecoPeca.getText());
            float subtotal_peca;
            subtotal_peca = quantidade * preco;
            lblSubTotalPeca.setText(String.valueOf(subtotal_peca));
        }
    }//GEN-LAST:event_txtQtdPecaKeyPressed

    private void txtQtdPecaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtQtdPecaKeyReleased
        // TODO add your handling code here:
        if (!txtQtdPeca.getText().isEmpty() && !txtPrecoPeca.getText().isEmpty() && !txtIdPeca.getText().isEmpty()) {
            float quantidade = Float.parseFloat(txtQtdPeca.getText());
            float preco = Float.parseFloat(txtPrecoPeca.getText());
            float subtotal_peca;
            subtotal_peca = quantidade * preco;
            lblSubTotalPeca.setText(String.valueOf(subtotal_peca));
        }
    }//GEN-LAST:event_txtQtdPecaKeyReleased

    private void txtPrecoPecaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecoPecaKeyPressed
        // TODO add your handling code here:
        if (!txtQtdPeca.getText().isEmpty() && !txtPrecoPeca.getText().isEmpty() && !txtIdPeca.getText().isEmpty()) {
            float quantidade = Float.parseFloat(txtQtdPeca.getText());
            float preco = Float.parseFloat(txtPrecoPeca.getText());
            float subtotal_peca;
            subtotal_peca = quantidade * preco;
            lblSubTotalPeca.setText(String.valueOf(subtotal_peca));
        }
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (txtQtdPeca.getText().isEmpty() || txtPeca.getText().isEmpty() || txtPrecoPeca.getText().isEmpty() || txtIdPeca.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Preencha os campos em branco");
            } else {
                double precoDaPeca = Double.parseDouble(txtPrecoPeca.getText());
                double quantidade = Double.parseDouble(txtQtdPeca.getText());
                int id = Integer.parseInt(txtIdPeca.getText());
                String desc = txtPeca.getText();
                double total = precoDaPeca * quantidade;

                // Aqui é a parte onde se envia os dados para a tabela de serviços
                // Criamos um hashset que armazena tudo o que enviamos para a tabela
                // Esse HashSet é convertido em um ArrayList para ser mostrado na JTable
                // Com isso impedimos a entrada de itens repetidos na tabela
                // E por conseguinte evitamos entradas duplicadas com o banco de dados
                TPeca part = new TPeca(quantidade, desc, total, id, precoDaPeca);
                setPeca.add(part);
                ArrayList<TPeca> pecas = null;
                pecas = new ArrayList<>(setPeca);
                this.tableModel2.listaDePecas = pecas;
                tableModel2.fireTableDataChanged();

                tabelaOrcamentoPeca.getColumnModel().getColumn(3).setMinWidth(0);
                tabelaOrcamentoPeca.getColumnModel().getColumn(3).setMaxWidth(0);

                txtQtdPeca.setText("0");
                txtPrecoPeca.setText("0");
                txtIdPeca.setText(null);
                txtPeca.setText(null);
            }
        }
    }//GEN-LAST:event_txtPrecoPecaKeyPressed

    private void txtPrecoPecaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecoPecaKeyReleased
        // TODO add your handling code here:
        if (!txtQtdPeca.getText().isEmpty() && !txtPrecoPeca.getText().isEmpty() && !txtIdPeca.getText().isEmpty()) {
            float quantidade = Float.parseFloat(txtQtdPeca.getText());
            float preco = Float.parseFloat(txtPrecoPeca.getText());
            float subtotal_peca;
            subtotal_peca = quantidade * preco;
            lblSubTotalPeca.setText(String.valueOf(subtotal_peca));
        }
    }//GEN-LAST:event_txtPrecoPecaKeyReleased

    private void txtPrecoPecaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecoPecaKeyTyped
        // TODO add your handling code here:
        if (!txtQtdPeca.getText().isEmpty() && !txtPrecoPeca.getText().isEmpty() && !txtIdPeca.getText().isEmpty()) {
            float quantidade = Float.parseFloat(txtQtdPeca.getText());
            float preco = Float.parseFloat(txtPrecoPeca.getText());
            float subtotal_peca;
            subtotal_peca = quantidade * preco;
            lblSubTotalPeca.setText(String.valueOf(subtotal_peca));
        }
    }//GEN-LAST:event_txtPrecoPecaKeyTyped

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // Enviar para a tabela de peças
        // Botão enviar para a tabela
        if (txtQtdPeca.getText().isEmpty() || txtPeca.getText().isEmpty() || txtPrecoPeca.getText().isEmpty() || txtIdPeca.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Preencha os campos em branco");
        } else {
            double precoDaPeca = Double.parseDouble(txtPrecoPeca.getText());
            double quantidade = Double.parseDouble(txtQtdPeca.getText());
            int id = Integer.parseInt(txtIdPeca.getText());
            String desc = txtPeca.getText();
            double total = precoDaPeca * quantidade;

            // Aqui é a parte onde se envia os dados para a tabela de serviços
            // Criamos um hashset que armazena tudo o que enviamos para a tabela
            // Esse HashSet é convertido em um ArrayList para ser mostrado na JTable
            // Com isso impedimos a entrada de itens repetidos na tabela
            // E por conseguinte evitamos entradas duplicadas com o banco de dados
            TPeca part = new TPeca(quantidade, desc, total, id, precoDaPeca);
            setPeca.add(part);
            ArrayList<TPeca> pecas = null;
            pecas = new ArrayList<>(setPeca);
            this.tableModel2.listaDePecas = pecas;
            tableModel2.fireTableDataChanged();

            tabelaOrcamentoPeca.getColumnModel().getColumn(3).setMinWidth(0);
            tabelaOrcamentoPeca.getColumnModel().getColumn(3).setMaxWidth(0);

            txtQtdPeca.setText("0");
            txtPrecoPeca.setText("0");
            txtIdPeca.setText(null);
            txtPeca.setText(null);
        }

    }//GEN-LAST:event_jButton5ActionPerformed

    private void txtQtdPecaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtQtdPecaKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtQtdPecaKeyTyped

    private void btnRemoverServicosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoverServicosActionPerformed
        // Botão remover item
        // Botão remover item
        //RECEBER OS DADOS DA TABELA
        double qt = Double.parseDouble(tabelaOrcamentoServico.getValueAt(tabelaOrcamentoServico.getSelectedRow(), 0).toString());
        String desc = tabelaOrcamentoServico.getValueAt(tabelaOrcamentoServico.getSelectedRow(), 1).toString();
        String cat = tabelaOrcamentoServico.getValueAt(tabelaOrcamentoServico.getSelectedRow(), 2).toString();
        double total = Double.parseDouble(tabelaOrcamentoServico.getValueAt(tabelaOrcamentoServico.getSelectedRow(), 3).toString());
        int id = Integer.parseInt(tabelaOrcamentoServico.getValueAt(tabelaOrcamentoServico.getSelectedRow(), 4).toString());
        double pu = Double.parseDouble(tabelaOrcamentoServico.getValueAt(tabelaOrcamentoServico.getSelectedRow(), 5).toString());
        // CRIAÇÃO DO OBJETO QUE IRÁ RECEBER O SERVIÇO A SER REMOVIDO
        // RECUPERAR OS DADOS EM FORMA DE OBJETO SELECIONADO DA TABELA
        TServico delSer = new TServico(qt, desc, cat, total, id, pu);
        // CRIAÇÃO DE LAÇO QUE IRÁ PERCORRER PELO HASHSET A PROCURA DO OBJETO CRIADO E COMPARAR COM O OBJETO JÁ EXISTENTE
        for (Iterator<TServico> it = setServico.iterator(); it.hasNext();) {
            TServico t = it.next();
            if (t.equals(delSer)) {
                it.remove();
            }
        }

        if (tabelaOrcamentoServico.getSelectedRow() >= 0) {
            this.tableModel.removePessoa(this.tabelaOrcamentoServico.getSelectedRow());
        } else {
            JOptionPane.showMessageDialog(null, "Por favor selecione uma linha");
        }

    }//GEN-LAST:event_btnRemoverServicosActionPerformed

    private void btnAlterarPecasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarPecasActionPerformed
        // Botão remover item
        // RECEBER OS DADOS DA TABELA
        double quantidade = Double.parseDouble(tabelaOrcamentoPeca.getValueAt(tabelaOrcamentoPeca.getSelectedRow(), 0).toString());
        String descricao = tabelaOrcamentoPeca.getValueAt(tabelaOrcamentoPeca.getSelectedRow(), 1).toString();
        double total = Double.parseDouble(tabelaOrcamentoPeca.getValueAt(tabelaOrcamentoPeca.getSelectedRow(), 2).toString());
        int id = Integer.parseInt(tabelaOrcamentoPeca.getValueAt(tabelaOrcamentoPeca.getSelectedRow(), 3).toString());
        double precoUnitario = Double.parseDouble(tabelaOrcamentoPeca.getValueAt(tabelaOrcamentoPeca.getSelectedRow(), 4).toString());
        // CRIAÇÃO DO OBJETO QUE IRÁ RECEBER O SERVIÇO A SER REMOVIDO
        // RECUPERAR OS DADOS EM FORMA DE OBJETO SELECIONADO DA TABELA
        TPeca delPeca = new TPeca(quantidade, descricao, total, id, precoUnitario);
        // CRIAÇÃO DE LAÇO QUE IRÁ PERCORRER PELO HASHSET A PROCURA DO OBJETO CRIADO E COMPARAR COM O OBJETO JÁ EXISTENTE
        for (Iterator<TPeca> it = setPeca.iterator(); it.hasNext();) {
            TPeca t = it.next();
            if (t.equals(delPeca)) {
                it.remove();
            }
        }        
        if (tabelaOrcamentoPeca.getSelectedRow() >= 0) {
            this.tableModel2.removePessoa(this.tabelaOrcamentoPeca.getSelectedRow());
        } else {
            JOptionPane.showMessageDialog(null, "Por favor selecione uma linha");
        }
    }//GEN-LAST:event_btnAlterarPecasActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        getSum();
        getSumProducts();
        double materiais;
        materiais = Double.parseDouble(txtMateriais.getText());
        double maoDeObra;
        maoDeObra = Double.parseDouble(txtTMaoObra.getText());
        double pecas;
        pecas = Double.parseDouble(txtTotalPecas.getText());
        double total;
        total = maoDeObra + pecas + materiais;
        txtTotalNota.setText(Double.toString(total));

    }//GEN-LAST:event_jButton6ActionPerformed

    private void btnEmitirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmitirActionPerformed
        // Botão emitir orçamento
        int con = JOptionPane.showConfirmDialog(null, "Deseja emitir o orçamento?"); // se o valor de con for 0, quer dizer que o usuário clicou em sim
        if (con == 0) {
            if (txtID.getText().isEmpty() || txtTotalPecas.getText().isEmpty() || txtTMaoObra.getText().isEmpty() || txtMateriais.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Revise os totais e preencha os campos em branco");
            } else {
                SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
                Date data = new Date();
                Orcamento os = new Orcamento();
                os.setData(sd.format(data));
                os.setObservacoes(txtObs.getText());
                os.setCliente(Integer.parseInt(txtID.getText()));
                os.setTotal_pecas(Double.parseDouble(txtTotalPecas.getText()));
                os.setTotal_maodeobra(Double.parseDouble(txtTMaoObra.getText()));
                os.setTotal_geral(Double.parseDouble(txtTotalNota.getText()));
                os.setMateriais(Double.parseDouble(txtMateriais.getText()));
                os.setTb_motor_idMotor(Integer.parseInt(txtIDM.getText()));
                OrcamentoDAO dao = new OrcamentoDAO();
                dao.emitirOrcamento(os);

                // Vinculação de peças a os
                for (int i = 0; i < tabelaOrcamentoPeca.getRowCount(); i++) {
                    int ordemservico = Integer.parseInt(lblOrcamento.getText());
                    int pecas = Integer.parseInt(tabelaOrcamentoPeca.getValueAt(i, 3).toString());
                    double quantidade = Double.parseDouble(tabelaOrcamentoPeca.getValueAt(i, 0).toString());
                    double precounitario = Double.parseDouble(tabelaOrcamentoPeca.getValueAt(i, 4).toString());
                    dao.vincularPecas(ordemservico, pecas, quantidade, precounitario);
                }
                // Vinculação de mão de obra a ordem de servico
                for (int p = 0; p < tabelaOrcamentoServico.getRowCount(); p++) {
                    int ordemservico = Integer.parseInt(lblOrcamento.getText());
                    int maodeobra = Integer.parseInt(tabelaOrcamentoServico.getValueAt(p, 4).toString());
                    double quantidade = Double.parseDouble(tabelaOrcamentoServico.getValueAt(p, 0).toString());
                    double precoUnitario = Double.parseDouble(tabelaOrcamentoServico.getValueAt(p, 5).toString());
                    dao.vincularMaoDeObra(ordemservico, maodeobra, quantidade, precoUnitario);
                }
            }
        }
    }//GEN-LAST:event_btnEmitirActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        // Botão editar ordem de serviço
        int con = JOptionPane.showConfirmDialog(null, "Deseja re-emitir o orçamento?"); // se o valor de con for 0, quer dizer que o usuário clicou em sim
        if (con == 0) {
            if (txtID.getText().isEmpty() || txtTotalPecas.getText().isEmpty() || txtTMaoObra.getText().isEmpty() || txtMateriais.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Revise os totais e preencha os campos em branco");
            } else {
                Orcamento os = new Orcamento();
                os.setId(Integer.parseInt(lblOrcamento.getText()));
                os.setObservacoes(txtObs.getText());
                os.setCliente(Integer.parseInt(txtID.getText()));
                os.setTotal_pecas(Double.parseDouble(txtTotalPecas.getText()));
                os.setTotal_maodeobra(Double.parseDouble(txtTMaoObra.getText()));
                os.setTotal_geral(Double.parseDouble(txtTotalNota.getText()));
                os.setMateriais(Double.parseDouble(txtMateriais.getText()));
                os.setTb_motor_idMotor(Integer.parseInt(txtIDM.getText()));
                OrcamentoDAO dao = new OrcamentoDAO();
                dao.editarOrcamento(os);
                dao.temPeca(Integer.parseInt(lblOrcamento.getText()));
                dao.temServico(Integer.parseInt(lblOrcamento.getText()));
                // Vinculação de peças a os

                if (tabelaOrcamentoPeca.getRowCount() == 0) {
                    dao.temPeca(Integer.parseInt(lblOrcamento.getText()));
                } else if (tabelaOrcamentoPeca.getRowCount() >= 1) {
                    for (int i = 0; i < tabelaOrcamentoPeca.getRowCount(); i++) {
                        int ordemservico = Integer.parseInt(lblOrcamento.getText());
                        int pecas = Integer.parseInt(tabelaOrcamentoPeca.getValueAt(i, 3).toString());
                        double quantidade = Double.parseDouble(tabelaOrcamentoPeca.getValueAt(i, 0).toString());
                        double precounitario = Double.parseDouble(tabelaOrcamentoPeca.getValueAt(i, 4).toString());
                        dao.vincularPecas(ordemservico, pecas, quantidade, precounitario);
                    }
                }
                // Vinculação de mão de obra a ordem de servico
                if (tabelaOrcamentoServico.getRowCount() == 0) {
                    dao.temServico(Integer.parseInt(lblOrcamento.getText()));
                } else if (tabelaOrcamentoServico.getRowCount() >= 1) {
                    for (int p = 0; p < tabelaOrcamentoServico.getRowCount(); p++) {
                        int ordemservico = Integer.parseInt(lblOrcamento.getText());
                        int maodeobra = Integer.parseInt(tabelaOrcamentoServico.getValueAt(p, 4).toString());
                        double quantidade = Double.parseDouble(tabelaOrcamentoServico.getValueAt(p, 0).toString());
                        double precoUnitario = Double.parseDouble(tabelaOrcamentoServico.getValueAt(p, 5).toString());
                        dao.vincularMaoDeObra(ordemservico, maodeobra, quantidade, precoUnitario);
                    }
                }
            }
        }
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnLinhaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLinhaActionPerformed
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(null, "Número de linhas da tabela de produtos " + tabelaOrcamentoPeca.getRowCount());
        JOptionPane.showMessageDialog(null, "Número de linhas da tabela de mão de obra " + tabelaOrcamentoServico.getRowCount());
    }//GEN-LAST:event_btnLinhaActionPerformed

    private void tabelaServicoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelaServicoMouseClicked
        // Evento mouse clicked
        if (evt.getClickCount() == 2) {
            String servico;
            servico = tabelaServico.getValueAt(tabelaServico.getSelectedRow(), 1).toString();
            int id;
            id = Integer.parseInt(tabelaServico.getValueAt(tabelaServico.getSelectedRow(), 0).toString());
            String categoria;
            categoria = tabelaServico.getValueAt(tabelaServico.getSelectedRow(), 2).toString();
            txtServico.setText(servico);
            txtIDS.setText(String.valueOf(id));
            txtCategoriaServico.setText(categoria);
            txtQtd.requestFocus();
        }
    }//GEN-LAST:event_tabelaServicoMouseClicked

    private void tabelaPecaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelaPecaMouseClicked
        // Evento mouse clicked da tabela de peças
        if (evt.getClickCount() == 2) {
            String peca;
            peca = tabelaPeca.getValueAt(tabelaPeca.getSelectedRow(), 1).toString();
            int id;
            id = Integer.parseInt(tabelaPeca.getValueAt(tabelaPeca.getSelectedRow(), 0).toString());
            txtPeca.setText(peca);
            txtIdPeca.setText(String.valueOf(id));
            txtQtdPeca.requestFocus();
        }
    }//GEN-LAST:event_tabelaPecaMouseClicked

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
            java.util.logging.Logger.getLogger(FrmOrcamento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmOrcamento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmOrcamento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmOrcamento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmOrcamento().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAlterarPecas;
    private javax.swing.JButton btnBusca;
    public javax.swing.JButton btnEditar;
    public javax.swing.JButton btnEmitir;
    private javax.swing.JButton btnLinha;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnRemoverServicos;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    public javax.swing.JLabel lblOrcamento;
    private javax.swing.JLabel lblSubTotal;
    private javax.swing.JLabel lblSubTotalPeca;
    private javax.swing.JTable tabelaMotor;
    public javax.swing.JTable tabelaOrcamentoPeca;
    public javax.swing.JTable tabelaOrcamentoServico;
    private javax.swing.JTable tabelaPeca;
    private javax.swing.JTable tabelaServico;
    public javax.swing.JTextField txtBairro;
    private javax.swing.JComboBox txtCategoria;
    private javax.swing.JTextField txtCategoriaServico;
    public javax.swing.JFormattedTextField txtCep;
    public javax.swing.JTextField txtCidade;
    public javax.swing.JTextField txtCilindro;
    public javax.swing.JTextField txtCombustivel;
    public javax.swing.JFormattedTextField txtCpf;
    public javax.swing.JFormattedTextField txtData;
    public javax.swing.JTextField txtEstado;
    public javax.swing.JTextField txtGarantia;
    public javax.swing.JTextField txtID;
    public javax.swing.JTextField txtIDM;
    private javax.swing.JTextField txtIDS;
    public javax.swing.JTextField txtIdOr;
    private javax.swing.JTextField txtIdPeca;
    public javax.swing.JTextField txtLogradouro;
    public javax.swing.JTextField txtMateriais;
    public javax.swing.JTextField txtMotor;
    public javax.swing.JTextField txtNome;
    public javax.swing.JTextField txtNum;
    public javax.swing.JTextArea txtObs;
    private javax.swing.JTextField txtPeca;
    private javax.swing.JTextField txtPrecoPeca;
    private javax.swing.JTextField txtPrecoS;
    private javax.swing.JTextField txtQtd;
    private javax.swing.JTextField txtQtdPeca;
    public javax.swing.JTextField txtRG;
    private javax.swing.JTextField txtServico;
    public javax.swing.JTextField txtTMaoObra;
    public javax.swing.JFormattedTextField txtTel;
    public javax.swing.JFormattedTextField txtTel2;
    public javax.swing.JTextField txtTipo;
    public javax.swing.JTextField txtTotalNota;
    public javax.swing.JTextField txtTotalPecas;
    // End of variables declaration//GEN-END:variables

}
