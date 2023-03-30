/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.beans;

import java.text.SimpleDateFormat;
import java.util.Date;
import vendas.entity.Vendedor;

/**
 *
 * @author sam
 */
public class AniversarioFilter {

    private String mesInicial;
    private String mesFinal;
    private int ordem;
    private Vendedor vendedor;
    private String nome;

    public AniversarioFilter() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMdd");
            String mes = sdf.format(new Date());
            mesInicial = mes;
            mesFinal = mes;
    }

    public String getMesFinal() {
        return mesFinal;
    }

    public void setMesFinal(String mesFinal) {
        this.mesFinal = mesFinal;
    }

    public String getMesInicial() {
        return mesInicial;
    }

    public void setMesInicial(String mesInicial) {
        this.mesInicial = mesInicial;
    }

    public int getOrdem() {
        return ordem;
    }

    public void setOrdem(int ordem) {
        this.ordem = ordem;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

}
