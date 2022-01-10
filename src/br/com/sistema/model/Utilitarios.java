/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sistema.model;

import br.com.sistema.view.Frmmenu;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Tampelini
 */
public class Utilitarios {

    //metodo limparCampos
    public void LimpaTela(JPanel container) {
        Component components[] = container.getComponents();
        for (Component component : components) {
            if (component instanceof JTextField) {
                ((JTextField) component).setText(null);
            }
        }
    }

    //Metodo para adicionar imagem de fundo JDesktopPane
//    public void adicionaImagem() {
//
//        ImageIcon icon = new ImageIcon(Frmmenu.class.getResource("br.com.projeto.images/fundo.jpg"));
//        Image img = icon.getImage();
//
//        JDesktopPane painel = new JDesktopPane() {
//            public void paintComponent(Graphics g) {
//                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
//            }
//
//        }
    
    public boolean validarCpf(String cpf){
        cpf = cpf.replace(".", "").replace("-", "");
        int digito_verificador_1 = cpf.charAt(9) - '0';
        int digito_verificador_2 = cpf.charAt(10) - '0';
        int soma1 = 0;
        int soma2 = 0;

        int digitos_iguais = 1;
        //verifica se todos os digitos sao iguais
        for(int i=0; i < 10; i++){
            if(cpf.charAt(i) != cpf.charAt(i+1)){
                digitos_iguais = 0;
                break;
            }
        }
        //se forem iguais, cpf eh invalido e a funcao retorna 0
        if(digitos_iguais == 1)
            return false;

        //multiplica os 9 primeiros digitos sequencialmente por 10 ate 2 e soma os resultados
        for(int i=10; i > 1; i--){
            soma1 += (cpf.charAt(10-i) - '0') * i;
        }

        //pega o resto da divisao
        int res1 = (soma1 * 10) % 11;

        //se resto da divisao for 10, transforma em 0
        if(res1 == 10)
            res1 = 0;

        //verifica se o resto da divisao eh diferent do primeiro digito verificador
        //se for diferente, cpf eh invalido e retorna 0
        if(res1 != digito_verificador_1)
            return false;

        //faz o mesmo que a primeira parte, porem incluindo o o primeiro digito verificador
        for(int i=11; i > 1; i--){
            soma2 += (cpf.charAt(11-i) - '0') * i;
        }

        int res2 = (soma2 * 10) % 11;

        if(res2 == 10)
            res2 = 0;

        if(res2 != digito_verificador_2)
            return false;

        return true;       
    }
    
    public boolean validarEmail(String email){
        Pattern pattern = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
        Matcher match = pattern.matcher(email);
        boolean found = match.find();
        
        return found;
    }
                
                }
