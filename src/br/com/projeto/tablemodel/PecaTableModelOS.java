package br.com.projeto.tablemodel;

import br.com.projeto.model.TPeca;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author LUCAS
 */
public class PecaTableModelOS extends AbstractTableModel{

    public ArrayList<TPeca> listaDePecas;
    private final String[] colunas = {"Quantidade", "Descrição", "Total", "id", "Preço Unitário"};

    public PecaTableModelOS() {
        this.listaDePecas = new ArrayList<>();
    }
    
    /**
     * Retorna uma linha completa da tabela
     * @param rowIndex
     * @return TPeca
     */
    public TPeca getPeca(int rowIndex){
        return this.listaDePecas.get(rowIndex);
    }
    
    /**
     * Adiciona uma peça a tabela (cria uma linha)
     * @param peca
     */
    public void addPeca(TPeca peca){
        this.listaDePecas.add(peca);
        fireTableDataChanged();
    }
    
    /**
     * Remove uma peça da tabela (remove uma linha)
     * @param rowIndex 
     */
    public void removePessoa(int rowIndex){
        this.listaDePecas.remove(rowIndex);
        fireTableDataChanged();
    }
    
    /**
     * Retorna a quantidade de linhas da tabela
     *
     * @return int
     */
    @Override
    public int getRowCount() {

        return this.listaDePecas.size();
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
                return this.listaDePecas.get(rowIndex).getQuantidade();
            case 1:
                return this.listaDePecas.get(rowIndex).getDescricao();
            case 2:
                return this.listaDePecas.get(rowIndex).getTotal();
            case 3:
                return this.listaDePecas.get(rowIndex).getId();
            case 4:
                return this.listaDePecas.get(rowIndex).getPrecoUnitario();
            default:
                return this.listaDePecas.get(rowIndex);
        }
    }
    
    @Override
    public String getColumnName(int columnIndex) {
        return this.colunas[columnIndex];
    }
}
