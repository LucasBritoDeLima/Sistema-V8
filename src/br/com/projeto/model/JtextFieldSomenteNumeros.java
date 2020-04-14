package br.com.projeto.model;

import java.awt.event.KeyEvent;
import javax.swing.JTextField;

public final class JtextFieldSomenteNumeros extends JTextField {

    private int maximoCaracteres = -1;// definição de -1 
// como  valor normal de um textfield sem limite de caracters

    public JtextFieldSomenteNumeros() {
        super();
        addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldKeyTyped(evt);
            }
        });
    }

    public JtextFieldSomenteNumeros(int maximo) {
        super();
        setMaximoCaracteres(maximo);// define o tamanho máximo
//que deve ser aceito no jtextfield que foi recebido no  construtor

        addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldKeyTyped(evt);
            }
        });
    }

    private void jTextFieldKeyTyped(KeyEvent evt) {

        String caracteres = "0987654321.";// lista de caracters que não devem ser aceitos
        if (!caracteres.contains(evt.getKeyChar() + "")) {
            evt.consume();//aciona esse propriedade para eliminar a ação do evento
        }
        if ((getText().length() >= getMaximoCaracteres()) && (getMaximoCaracteres() != -1)) {
//if para saber se precisa verificar também o tamanho da string do campo
// maior ou igual ao tamanho máximo, cancela e nao deixa inserir mais
            evt.consume();
            setText(getText().substring(0, getMaximoCaracteres()));

        }

    }

    public int getMaximoCaracteres() {
        return maximoCaracteres;
    }

    public void setMaximoCaracteres(int maximoCaracteres) {
        this.maximoCaracteres = maximoCaracteres;
    }
}
