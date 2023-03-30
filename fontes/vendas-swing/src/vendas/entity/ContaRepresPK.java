/*
 * ContaRepresPK.java
 *
 * Created on 30 de Junho de 2007, 13:58
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package vendas.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Classe de Chave Prim�ria ContaRepresPK para classe de entidade ContaRepres
 * 
 * @author Sam
 */
@Embeddable
public class ContaRepresPK implements Serializable {

    @Column(name = "IDREPRES", nullable = false)
    private int idRepres;

    @Column(name = "IDBANCO", nullable = false)
    private String idBanco;

    @Column(name = "AGENCIA", nullable = false)
    private String agencia;

    @Column(name = "CCORRENTE", nullable = false)
    private String contaCorrente;
    
    /** Creates a new instance of ContaRepresPK */
    public ContaRepresPK() {
    }

    /**
     * Define o idrepres deste ContaRepresPK.
     * @return o idrepres
     */
    public int getIdRepres() {
        return this.idRepres;
    }

    /**
     * Define o idrepres deste ContaRepresPK para o valor especificado.
     * @param idrepres o novo idrepres
     */
    public void setIdRepres(int idrepres) {
        this.idRepres = idrepres;
    }

    /**
     * Define o idbanco deste ContaRepresPK.
     * @return o idbanco
     */
    public String getIdBanco() {
        return this.idBanco;
    }

    /**
     * Define o idbanco deste ContaRepresPK para o valor especificado.
     * @param idbanco o novo idbanco
     */
    public void setIdBanco(String idbanco) {
        this.idBanco = idbanco;
    }

    /**
     * Define o agencia deste ContaRepresPK.
     * @return o agencia
     */
    public String getAgencia() {
        return this.agencia;
    }

    /**
     * Define o agencia deste ContaRepresPK para o valor especificado.
     * @param agencia o novo agencia
     */
    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    /**
     * Define o ccorrente deste ContaRepresPK.
     * @return o ccorrente
     */
    public String getContaCorrente() {
        return this.contaCorrente;
    }

    /**
     * Define o ccorrente deste ContaRepresPK para o valor especificado.
     * @param ccorrente o novo ccorrente
     */
    public void setContaCorrente(String ccorrente) {
        this.contaCorrente = ccorrente;
    }

    /**
     * Retorna um valor de c�digo hash para o objeto.  Esta implementa��o computa
     * um valor de c�digo hash baseado nos campos id deste objeto.
     * @return um valor de c�digo hash para este objeto.
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.contaCorrente != null ? this.contaCorrente.hashCode() : 0);
        hash += (this.agencia != null ? this.agencia.hashCode() : 0);
        hash += (this.idBanco != null ? this.idBanco.hashCode() : 0);
        hash += (int)idRepres;
        return hash;
    }

    /**
     * Determina se outro objeto � igual a este ContaRepresPK.  O resultado �
     * <code>true</code> se e somente se o argumento n�o for nulo e for um objeto ContaRepresPK o qual
     * tem o mesmo valor para o campo id como este objeto.
     * @param object o objeto de refer�ncia com o qual comparar
     * @return <code>true</code> se este objeto � o mesmo como o argumento;
     * <code>false</code> caso contr�rio.
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ContaRepresPK)) {
            return false;
        }
        ContaRepresPK other = (ContaRepresPK)object;
        if (this.contaCorrente != other.contaCorrente && (this.contaCorrente == null || !this.contaCorrente.equals(other.contaCorrente))) return false;
        if (this.agencia != other.agencia && (this.agencia == null || !this.agencia.equals(other.agencia))) return false;
        if (this.idBanco != other.idBanco && (this.idBanco == null || !this.idBanco.equals(other.idBanco))) return false;
        if (this.idRepres != other.idRepres) return false;
        return true;
    }

    /**
     * Retorna uma representa��o literal deste objeto.  Esta implementa��o cria
     * uma representa��o baseada nos campos id.
     * @return uma representa��o literal deste objeto.
     */
    @Override
    public String toString() {
        return "[ccorrente=" + contaCorrente + ", agencia=" + agencia + ", idbanco=" + idBanco + ", idrepres=" + idRepres + "]";
    }
    
}
