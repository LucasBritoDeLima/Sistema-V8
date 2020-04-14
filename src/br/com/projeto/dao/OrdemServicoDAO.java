/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.projeto.dao;

import br.com.projeto.jdbc.ConnectionFactory;
import br.com.projeto.model.Cliente;
import br.com.projeto.model.Motor;
import br.com.projeto.model.Orcamento;
import br.com.projeto.model.OrdemServico;
import br.com.projeto.model.Peca;
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
public class OrdemServicoDAO {

    private Connection con;

    public OrdemServicoDAO() {
        this.con = new ConnectionFactory().getConnection();
    }

    //Criar ordem de serviço
    public void emitirOrdemDeServico(OrdemServico obj) {
        try {
            // comando sql
            String sql = "INSERT INTO `tb_ordemdeservico`(data, observacoes, cliente, total_pecas, total_maodeobra, total_geral, materiais, tb_motor_idMotor) "
                    + " VALUES(?,?,?,?,?,?,?,?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, obj.getData());
            stmt.setString(2, obj.getObservacoes());
            stmt.setInt(3, obj.getCliente());
            stmt.setDouble(4, obj.getTotal_pecas());
            stmt.setDouble(5, obj.getTotal_maodeobra());
            stmt.setDouble(6, obj.getTotal_geral());
            stmt.setDouble(7, obj.getMateriais());
            stmt.setInt(8, obj.getTb_motor_idMotor());
            stmt.execute();
            stmt.close();
            JOptionPane.showMessageDialog(null, "Ordem de serviço emitida com sucesso!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro : " + e);
        }
    }

    // Editar ordem já existente
    public void editarOrdemDeServico(OrdemServico obj) {
        try {
            // comando sql
            String sql = "UPDATE `tb_ordemdeservico` SET `observacoes`=?, `cliente`=?, `total_pecas`=?, `total_maodeobra`=?, `total_geral`=?, `materiais`=?, `tb_motor_idMotor`=? WHERE id=?";

            // Segundo passo - conectar com o banco de dados e organizar o comando sql
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, obj.getObservacoes());
            stmt.setInt(2, obj.getCliente());
            stmt.setDouble(3, obj.getTotal_pecas());
            stmt.setDouble(4, obj.getTotal_maodeobra());
            stmt.setDouble(5, obj.getTotal_geral());
            stmt.setDouble(6, obj.getMateriais());
            stmt.setInt(7, obj.getTb_motor_idMotor());
            stmt.setInt(8, obj.getId());
            // Terceiro passo - executar o comando sql
            stmt.execute();
            stmt.close();
            JOptionPane.showMessageDialog(null, "Cliente alterado com sucesso");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e);
        }
    }

    public void apagarPeca(int ordemdeservico) {
        try {
            String sql = "DELETE FROM tb_item_produto_os WHERE ordemdeservico=?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, ordemdeservico);
            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir peças: \n" + e);
        }
    }

    public void apagarServico(int ordemdeservico) {
        try {
            String sql = "DELETE FROM tb_item_maodeobra_os WHERE ordemdeservico=?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, ordemdeservico);
            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao excluir mão de obra: \n" + e);
        }
    }

    public void temPeca(int ordemdeservico) {
        try {
            String sql = "SELECT * FROM tb_item_produto_os WHERE ordemdeservico=?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, ordemdeservico);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                //JOptionPane.showMessageDialog(null, "Há peças para esta ordem de serviço");
                apagarPeca(ordemdeservico);
            } else {
                JOptionPane.showMessageDialog(null, "Não há peças para esta ordem de serviço");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao verificar peças" + e);
        }
    }

    public void temServico(int ordemdeservico) {
        try {
            String sql = "SELECT * FROM tb_item_maodeobra_os WHERE ordemdeservico=?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, ordemdeservico);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                apagarServico(ordemdeservico);
            } else {
                JOptionPane.showMessageDialog(null, "Não há mão de obra para esta ordem de serviço");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao verificar serviço \n" + e);
        }
    }

    //Vincular produtos a os cadastrada
    public void vincularPecas(int ordemdeservico, int pecas, double quantidade, double precounitario) {
        try {
            // Comando SQL
            String sql = "INSERT INTO `tb_item_produto_os`(ordemdeservico, pecas, quantidade, precounitario) VALUES(?,?,?,?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, ordemdeservico);
            stmt.setInt(2, pecas);
            stmt.setDouble(3, quantidade);
            stmt.setDouble(4, precounitario);
            stmt.execute();
            stmt.close();
            //JOptionPane.showMessageDialog(null, "Pecas vinculadas a OS com sucesso");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao vincular as peças: " + e);
            System.out.println(e);
        }
    }

    //Vincular mao de obra a os cadastrada
    public void vincularMaoDeObra(int ordemdeservico, int maodeobra, double quantidade, double precounitario) {
        try {
            // Comando SQL
            String sql = "INSERT INTO `tb_item_maodeobra_os`(ordemdeservico, maodeobra, quantidade, precounitario) VALUES(?,?,?,?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, ordemdeservico);
            stmt.setInt(2, maodeobra);
            stmt.setDouble(3, quantidade);
            stmt.setDouble(4, precounitario);
            stmt.execute();
            stmt.close();
            //JOptionPane.showMessageDialog(null, "Ordem de serviço emitida com sucesso!!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao vincular as peças: " + e);
            System.out.println("Nego ney2" + e);
        }
    }

    //Listar todas as ordens de serviço por cpf
    public List<OrdemServico> listaOSByCpf(String cpf) {
        try {
            // Criar a lista
            List<OrdemServico> lista = new ArrayList<>();

            // Criar e organizar o SQL
            String sql = "SELECT o.id,o.data,o.total_geral,c.nome,c.cpf FROM `tb_ordemdeservico` AS o "
                    + " INNER JOIN `tb_cliente` as  c on(o.cliente = c.idCliente) WHERE c.cpf LIKE ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                OrdemServico obj = new OrdemServico();
                Cliente c = new Cliente();

                obj.setId(rs.getInt("o.id"));
                obj.setData(rs.getString("o.data"));
                obj.setTotal_geral(rs.getDouble("o.total_geral"));

                c.setNome(rs.getString("c.nome"));
                c.setCpf(rs.getString("c.cpf"));

                obj.setClient(c);

                lista.add(obj);
            }
            return lista;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Ordem de serviço não encontrada");
            return null;
        }
    }

    // Listar ordem de serviço por nome
    public List<OrdemServico> listaOSByNome(String nome) {
        try {
            // Criar a lista
            List<OrdemServico> lista = new ArrayList<>();

            // Criar e organizar o SQL
            String sql = "SELECT o.id,o.data,o.total_geral,c.nome,c.cpf FROM `tb_ordemdeservico` AS o "
                    + " INNER JOIN `tb_cliente` as  c on(o.cliente = c.idCliente) WHERE c.nome LIKE ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                OrdemServico obj = new OrdemServico();
                Cliente c = new Cliente();

                obj.setId(rs.getInt("o.id"));
                obj.setData(rs.getString("o.data"));
                obj.setTotal_geral(rs.getDouble("o.total_geral"));

                c.setNome(rs.getString("c.nome"));
                c.setCpf(rs.getString("c.cpf"));

                obj.setClient(c);

                lista.add(obj);
            }
            return lista;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Ordem de serviço não encontrada");
            return null;
        }
    }

    public List<OrdemServico> listaAlgumasInformacoesOS() {
        try {
            // Criar a lista
            List<OrdemServico> lista = new ArrayList<>();

            // Criar e organizar o SQL
            String sql = "SELECT oa.id, c.nome, oa.total_geral, oa.data FROM tb_ordemdeservico oa INNER JOIN tb_cliente AS c ON (oa.cliente = c.idCliente) WHERE oa.situacao <> 'Ativa'";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                OrdemServico obj = new OrdemServico();
                Cliente c = new Cliente();
                obj.setId(rs.getInt("oa.id"));
                obj.setData(rs.getString("oa.data"));
                obj.setTotal_geral(rs.getDouble("oa.total_geral"));
                c.setNome(rs.getString("c.nome"));
                obj.setClient(c);
                lista.add(obj);
            }
            return lista;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Ordem de serviço não encontrada"+e);
            return null;
        }
    }

    // Listar ordem de serviço por valor
    public List<OrdemServico> listaOSByValor(double valor) {
        try {
            // Criar a lista
            List<OrdemServico> lista = new ArrayList<>();

            // Criar e organizar o SQL
            String sql = "SELECT o.id,o.data,o.total_geral,c.nome,c.cpf FROM `tb_ordemdeservico` AS o "
                    + " INNER JOIN `tb_cliente` as  c on(o.cliente = c.idCliente) WHERE o.total_geral = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setDouble(1, valor);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                OrdemServico obj = new OrdemServico();
                Cliente c = new Cliente();

                obj.setId(rs.getInt("o.id"));
                obj.setData(rs.getString("o.data"));
                obj.setTotal_geral(rs.getDouble("o.total_geral"));

                c.setNome(rs.getString("c.nome"));
                c.setCpf(rs.getString("c.cpf"));

                obj.setClient(c);

                lista.add(obj);
            }
            return lista;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Ordem de serviço não encontrada");
            return null;
        }
    }

    public List<OrdemServico> listaOS() {
        try {
            // Criar a lista
            List<OrdemServico> lista = new ArrayList<>();

            // Criar e organizar o SQL
            String sql = "SELECT o.id,o.data,o.total_geral,c.nome,c.cpf FROM `tb_ordemdeservico` AS o "
                    + " INNER JOIN `tb_cliente` as  c on(o.cliente = c.idCliente) WHERE o.situacao='Ativa'";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                OrdemServico obj = new OrdemServico();
                Cliente c = new Cliente();

                obj.setId(rs.getInt("o.id"));
                obj.setData(rs.getString("o.data"));
                obj.setTotal_geral(rs.getDouble("o.total_geral"));

                c.setNome(rs.getString("c.nome"));
                c.setCpf(rs.getString("c.cpf"));

                obj.setClient(c);

                lista.add(obj);
            }
            return lista;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Ordem de serviço não encontrada" + e);
            return null;
        }
    }

    public List<OrdemServico> listarNotasPorPeriodo(String dataInicio, String dataFim) {
        try {
            List<OrdemServico> lista = new ArrayList<>();

            //Criar e organizar o SQL
            String sql = "SELECT o.id, date_format(o.data, '%d/%m/%Y') as data_formatada, o.total_geral,c.nome,c.cpf FROM `tb_ordemdeservico` AS o "
                    + " INNER JOIN `tb_cliente` as c on(o.cliente = c.idCliente) WHERE o.data BETWEEN ? AND ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, dataInicio.toString());
            stmt.setString(2, dataFim.toString());

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                OrdemServico obj = new OrdemServico();
                Cliente c = new Cliente();

                obj.setId(rs.getInt("o.id"));
                obj.setData(rs.getString("data_formatada"));
                obj.setTotal_geral(rs.getDouble("o.total_geral"));

                c.setNome(rs.getString("c.nome"));
                c.setCpf(rs.getString("c.cpf"));

                obj.setClient(c);

                lista.add(obj);
            }

            return lista;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Ordem de serviço não encontrada" + e);
            return null;
        }
    }

    public List<OrdemServico> acharOS(int id) {
        try {
            List<OrdemServico> lista = new ArrayList<>();

            //Criar e organizar o SQL
            String sql = "SELECT os.*,c.*,m.*,p.*,s.*,po.*,so.* FROM tb_ordemdeservico os "
                    + " INNER JOIN tb_cliente c ON (os.cliente = c.idCliente) INNER JOIN tb_motor m ON (os.tb_motor_idMotor = m.idMotor) "
                    + " INNER JOIN tb_item_produto_os po ON (po.ordemdeservico = os.id) INNER JOIN tb_pecas p ON (po.pecas = p.idPeca) "
                    + " INNER JOIN tb_item_maodeobra_os so ON (so.ordemdeservico = os.id) INNER JOIN tb_maoobra s on (so.maodeobra = s.idMao) "
                    + " WHERE os.id=? ";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                OrdemServico os = new OrdemServico();
                Cliente c = new Cliente();
                Peca p = new Peca();
                Motor m = new Motor();
                Servico s = new Servico();

                os.setId(rs.getInt("os.id"));
                os.setData(rs.getString("os.data"));
                os.setObservacoes(rs.getString("os.observacoes"));

                c.setNome(rs.getString("c.nome"));
                c.setCpf(rs.getString("c.cpf"));
                c.setRg(rs.getString("c.rg"));
                c.setTel(rs.getString("c.tel"));
                c.setTel2(rs.getString("c.tel2"));
                c.setLogradouro(rs.getString("c.logradouro"));
                c.setNumero(rs.getString("c.numero"));
                c.setBairro(rs.getString("c.bairro"));
                c.setCep(rs.getString("c.cep"));
                c.setEstado(rs.getString("c.estado"));
                c.setCidade(rs.getString("c.cidade"));

                os.setTotal_pecas(rs.getDouble("os.total_pecas"));
                os.setTotal_maodeobra(rs.getDouble("os.total_maodeobra"));
                os.setTotal_geral(rs.getDouble("os.total_geral"));
                os.setMateriais(rs.getDouble("os.materiais"));

                os.setClient(c);

                m.setMotor(rs.getString("m.motor"));
                m.setTipo(rs.getString("m.tipo"));
                m.setCilindros(rs.getString("m.cilindros"));
                m.setComb(rs.getString("m.comb"));
                m.setGarantia(rs.getString("m.garantia"));

                os.setMotor(m);

                p.setPeca(rs.getString("p.peca"));
                p.setQuantidade(rs.getInt("po.quantidade"));
                p.setPrecounitario(rs.getDouble("po.precounitario"));

                os.setPeca(p);

                s.setServico(rs.getString("s.servico"));
                s.setTipo(rs.getString("s.tipo"));
                s.setQuantidade(rs.getInt("so.quantidade"));
                s.setPrecounitario(rs.getDouble("so.precounitario"));

                os.setServico(s);

                lista.add(os);

            }
            return lista;
        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "Erro: " + erro);
            return null;
        }
    }

    public OrdemServico buscarOrdem(int id) {
        try {
            //Criar e organizar o SQL
            String sql = "SELECT os.*, c.*,m.* FROM `tb_ordemdeservico` os"
                    + " INNER JOIN `tb_cliente` c ON (os.cliente = c.idCliente)"
                    + " INNER JOIN `tb_motor` m ON (os.tb_motor_idMotor = m.idMotor)"
                    + " WHERE os.id=?";

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            OrdemServico os = new OrdemServico();
            Cliente c = new Cliente();
            Peca p = new Peca();
            Motor m = new Motor();
            Servico s = new Servico();

            while (rs.next()) {
                os.setId(rs.getInt("os.id"));
                os.setData(rs.getString("os.data"));
                os.setObservacoes(rs.getString("os.observacoes"));

                c.setIdCliente(rs.getInt("cliente"));
                c.setNome(rs.getString("c.nome"));
                c.setCpf(rs.getString("c.cpf"));
                c.setRg(rs.getString("c.rg"));
                c.setTel(rs.getString("c.tel"));
                c.setTel2(rs.getString("c.tel2"));
                c.setLogradouro(rs.getString("c.logradouro"));
                c.setNumero(rs.getString("c.numero"));
                c.setBairro(rs.getString("c.bairro"));
                c.setCep(rs.getString("c.cep"));
                c.setEstado(rs.getString("c.estado"));
                c.setCidade(rs.getString("c.cidade"));

                os.setTotal_pecas(rs.getDouble("os.total_pecas"));
                os.setTotal_maodeobra(rs.getDouble("os.total_maodeobra"));
                os.setTotal_geral(rs.getDouble("os.total_geral"));
                os.setMateriais(rs.getDouble("os.materiais"));

                os.setClient(c);

                m.setIdMotor(rs.getInt("tb_motor_idMotor"));
                m.setMotor(rs.getString("m.motor"));
                m.setTipo(rs.getString("m.tipo"));
                m.setCilindros(rs.getString("m.cilindros"));
                m.setComb(rs.getString("m.comb"));
                m.setGarantia(rs.getString("m.garantia"));

                os.setMotor(m);

            }
            return os;
        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "Erro: " + erro);
            return null;
        }
    }

    public List<OrdemServico> listarServicosCliente(int id) {
        try {
            // Criar a lista
            List<OrdemServico> lista = new ArrayList<>();

            // Criar o sql organizar e executar
            String sql = "SELECT os.id,s.*,so.* FROM `tb_ordemdeservico` os "
                    + " INNER JOIN `tb_item_maodeobra_os` so ON (so.ordemdeservico = os.id) "
                    + " INNER JOIN `tb_maoobra` s ON (s.idMao = so.maodeobra) WHERE os.id=?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                OrdemServico os = new OrdemServico();
                Servico s = new Servico();

                os.setId(rs.getInt("os.id"));
                s.setIdMao(rs.getInt("s.idMao"));
                s.setServico(rs.getString("s.servico"));
                s.setTipo(rs.getString("s.tipo"));
                s.setQuantidade(Integer.parseInt(rs.getString("so.quantidade")));
                s.setPrecounitario(rs.getDouble("so.precounitario"));
                os.setServico(s);

                lista.add(os);
            }
            return lista;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e);
            return null;
        }
    }

    public List<OrdemServico> listarPecasByCliente(int id) {
        try {
            // Criar a lista
            List<OrdemServico> lista = new ArrayList<>();

            // Criar o sql organizar e executar
            String sql = "SELECT os.id, p.*, po.* FROM tb_ordemdeservico os "
                    + " INNER JOIN tb_item_produto_os po ON (po.ordemdeservico = os.id) "
                    + " INNER JOIN tb_pecas p ON (p.idPeca = po.pecas) WHERE os.id = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                OrdemServico os = new OrdemServico();
                Peca p = new Peca();

                os.setId(rs.getInt("os.id"));
                p.setIdPeca(rs.getInt("p.idPeca"));
                p.setPeca(rs.getString("p.peca"));
                p.setQuantidade(rs.getInt("po.quantidade"));
                p.setPrecounitario(rs.getDouble("po.precounitario"));
                os.setPeca(p);

                lista.add(os);
            }
            return lista;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro: " + e);
            return null;
        }
    }

    public void inativarOrdem(int id) {
        try {
            String sql = "UPDATE tb_ordemdeservico SET situacao='Inativa' WHERE id=?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.execute();
            stmt.close();
            JOptionPane.showMessageDialog(null, "Ordem excluída");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Não foi possivel excluir a ordem de serviço\n" + e);
        }
    }
    
    public void ativarOrdem(int id) {
        try {
            String sql = "UPDATE tb_ordemdeservico SET situacao='Ativa' WHERE id=?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.execute();
            stmt.close();
            JOptionPane.showMessageDialog(null, "Ordem ativa");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Não foi possivel excluir a ordem de serviço\n" + e);
        }
    }
}
