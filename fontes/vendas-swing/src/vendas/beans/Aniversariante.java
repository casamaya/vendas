/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.beans;

/**
 *
 * @author sam
 */
public class Aniversariante {
    
    public static int CLIENTE = 0;
    public static int REPRES = 1;
    public static int CONTATOVENDEDOR = 2;
    public static int VENDEDOR = 3;

    private Integer id;
    private String nome;
    private String dtAniver;
    private String tipo;
    private String msn;
    private String responsavel;
    private String observacao;
    private String email;
    private int classe;

    public int getClasse() {
        return classe;
    }

    public void setClasse(int classe) {
        this.classe = classe;
    }

    public String getDtAniver() {
        return dtAniver;
    }

    public void setDtAniver(String dtAniver) {
        this.dtAniver = dtAniver;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMsn() {
        return msn;
    }

    public void setMsn(String msn) {
        this.msn = msn;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public String getMesAniversario() {
        String mes = null;
        if (dtAniver != null)
            mes = dtAniver.substring(2);
        return mes;
    }

    public String getAnoAniversario() {
        String mes = null;
        if (dtAniver != null)
            mes = dtAniver.substring(0, 2);
        return mes;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAniversarioExtenso() {
        if (dtAniver != null)
            return getMesAniversario() + "/" + getAnoAniversario();
        else
            return null;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    
}
