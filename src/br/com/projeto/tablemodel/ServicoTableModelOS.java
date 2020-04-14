package br.com.projeto.tablemodel;

import br.com.projeto.model.TServico;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class ServicoTableModelOS extends AbstractTableModel {

    public ArrayList<TServico> listaDeServicos;
    private final String[] colunas = {"Quantidade", "Descrição", "Categoria", "Total", "id", "Preço Unitário"};

    public ServicoTableModelOS(){
        this.listaDeServicos = new ArrayList<>();
    }
    
    /**
     * Retorna uma linha completa da tabela
     * @param rowIndex
     * @return Pessoa
     */
    public TServico getPessoa(int rowIndex){
        return this.listaDeServicos.get(rowIndex);
    }
    
    /**
     * Adiciona um serviço a tabela (cria uma linha)
     * @param tTServico
     */
    public void addPessoa(TServico tTServico){
        this.listaDeServicos.add(tTServico);
        fireTableDataChanged();
    }
    
    /**
     * Remove um serviço da tabela (remove uma linha)
     * @param rowIndex 
     */
    public void removePessoa(int rowIndex){
        this.listaDeServicos.remove(rowIndex);
        fireTableDataChanged();
    }
    
    /**
     * Retorna a quantidade de linhas da tabela
     *
     * @return int
     */
    @Override
    public int getRowCount() {

        return this.listaDeServicos.size();
    }

    /**
     * Retorna o numero de colunas da tabela
     *
     * @return int
     */
    @Override
    public int getColumnCount() {
        return colunas.length;
    }
    
    
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        //switch na coluna
        switch(columnIndex){
            //Agora vão os nomes das colunas
            case 0: // Coluna Quantidade
                return this.listaDeServicos.get(rowIndex).getQuantidade();
            case 1:
                return this.listaDeServicos.get(rowIndex).getDescricao();
            case 2:
                return this.listaDeServicos.get(rowIndex).getCategoria();
            case 3:
                return this.listaDeServicos.get(rowIndex).getTotal();
            case 4:
                return this.listaDeServicos.get(rowIndex).getId();
            case 5:
                return this.listaDeServicos.get(rowIndex).getPrecoUnitario();
            default:
                return this.listaDeServicos.get(rowIndex);
        }
    }
    
    @Override
    public String getColumnName(int columnIndex) {
        return this.colunas[columnIndex];
    }
    
}
