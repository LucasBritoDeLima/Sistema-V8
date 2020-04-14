/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.projeto.model;

/**
 *
 * @author LUCAS
 */
public class TPeca {
    private double quantidade;
    private String descricao;
    private double total;
    private int id;
    private double precoUnitario;

    public TPeca(double quantidade, String descricao, double total, int id, double precoUnitario) {
        this.quantidade = quantidade;
        this.descricao = descricao;
        this.total = total;
        this.id = id;
        this.precoUnitario = precoUnitario;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(double precoUnitario) {
        this.precoUnitario = precoUnitario;
    }
    
    @Override
    public int hashCode(){
        return this.id;
    }

    @Override
    public boolean equals(Object obj) {
        //se nao forem objetos da mesma classe sao objetos diferentes
        if(!(obj instanceof TPeca)) return false; 
        
        //se forem o mesmo objeto, retorna true
        if(obj == this) return true;
        
        // aqui o cast é seguro por causa do teste feito acima
        TPeca peca = (TPeca) obj;
        
        //aqui você compara a seu gosto, o ideal é comparar atributo por atributo
        return this.id == peca.getId();
    }
       
}
