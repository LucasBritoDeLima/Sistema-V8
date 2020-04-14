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
public class Motor {
    private int idMotor;
    private String motor;
    private String tipo;
    private String cilindros;
    private String comb;
    private String garantia;
    private int tb_cliente_idCliente; //Id do cliente da tabela tb_cliente
    private String situacao;
    private Cliente cliente;

    public int getIdMotor() {
        return idMotor;
    }

    public void setIdMotor(int idMotor) {
        this.idMotor = idMotor;
    }

    public String getMotor() {
        return motor;
    }

    public void setMotor(String motor) {
        this.motor = motor;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCilindros() {
        return cilindros;
    }

    public void setCilindros(String cilindros) {
        this.cilindros = cilindros;
    }

    public String getComb() {
        return comb;
    }

    public void setComb(String comb) {
        this.comb = comb;
    }

    public String getGarantia() {
        return garantia;
    }

    public void setGarantia(String garantia) {
        this.garantia = garantia;
    }

    public int getTb_cliente_idCliente() {
        return tb_cliente_idCliente;
    }

    public void setTb_cliente_idCliente(int tb_cliente_idCliente) {
        this.tb_cliente_idCliente = tb_cliente_idCliente;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }    
}
