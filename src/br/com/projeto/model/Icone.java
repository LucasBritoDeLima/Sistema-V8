/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.projeto.model;

import java.awt.Toolkit;
import javax.swing.JFrame;

/**
 *
 * @author LUCAS
 */
public class Icone {
    public void mudarIcone(JFrame frm){
        try {
            frm.setIconImage(Toolkit.getDefaultToolkit().getImage("src/imagens/logo.png"));
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
  
    }
}
