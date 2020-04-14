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
public class Orcamento {

    private int id;
    private String data;
    private String observacoes;
    private int cliente;
    private double total_pecas;
    private double total_maodeobra;
    private double total_geral;
    private double materiais;
    private int tb_motor_idMotor;
    private Cliente client;
    private Motor motor;
    private Peca peca;
    private Servico servico;
    private String situacao;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public int getCliente() {
        return cliente;
    }

    public void setCliente(int cliente) {
        this.cliente = cliente;
    }

    public double getTotal_pecas() {
        return total_pecas;
    }

    public void setTotal_pecas(double total_pecas) {
        this.total_pecas = total_pecas;
    }

    public double getTotal_maodeobra() {
        return total_maodeobra;
    }

    public void setTotal_maodeobra(double total_maodeobra) {
        this.total_maodeobra = total_maodeobra;
    }

    public double getTotal_geral() {
        return total_geral;
    }

    public void setTotal_geral(double total_geral) {
        this.total_geral = total_geral;
    }

    public double getMateriais() {
        return materiais;
    }

    public void setMateriais(double materiais) {
        this.materiais = materiais;
    }

    public int getTb_motor_idMotor() {
        return tb_motor_idMotor;
    }

    public void setTb_motor_idMotor(int tb_motor_idMotor) {
        this.tb_motor_idMotor = tb_motor_idMotor;
    }

    public Cliente getClient() {
        return client;
    }

    public void setClient(Cliente client) {
        this.client = client;
    }

    public Motor getMotor() {
        return motor;
    }

    public void setMotor(Motor motor) {
        this.motor = motor;
    }

    public Peca getPeca() {
        return peca;
    }

    public void setPeca(Peca peca) {
        this.peca = peca;
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }
}
