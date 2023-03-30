/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author Sam
 */
@Entity
@Table(name = "TBCOMPRADOR")
@NamedQueries({
    @NamedQuery(name = "Comprador.findAll", query = "SELECT c FROM Comprador c"),
    @NamedQuery(name = "Comprador.findByIdcomprador", query = "SELECT c FROM Comprador c WHERE c.idcomprador = :idcomprador"),
    @NamedQuery(name = "Comprador.findByContato", query = "SELECT c FROM Comprador c WHERE c.contato = :contato"),
    @NamedQuery(name = "Comprador.findByAniversario", query = "SELECT c FROM Comprador c WHERE c.dtaniver between :aniversario1 and :aniversario2")
})
public class Comprador implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator="TBCOMPRADOR_IDCOMPRADOR_SEQ", strategy=GenerationType.SEQUENCE)
    @SequenceGenerator(name="TBCOMPRADOR_IDCOMPRADOR_SEQ", sequenceName="TBCOMPRADOR_IDCOMPRADOR_SEQ", allocationSize=1)    @Basic(optional = false)
    @Column(name = "IDCOMPRADOR", nullable = false)
    private Integer idcomprador;
    @Basic(optional = false)
    @Column(name = "CONTATO", nullable = false, length = 30)
    private String contato;
    @Column(name = "ANIVERSARIO", length = 4)
    private String dtaniver;
    @Column(name = "TIPOCOMPRADOR", length = 1)
    private String tipocomprador;
    @Column(name = "MSN", length = 40)
    private String msn;
    @Lob
    @Column(name = "OBSERVACAO", length = 0)
    private String observacao;
    @JoinColumn(name = "IDCLIENTE", referencedColumnName = "IDCLIENTE", nullable = false)
    @ManyToOne(optional = false)
    private Cliente cliente;

    public Comprador() {
        tipocomprador = "C";
    }

    public Comprador(Integer idcomprador) {
        this.idcomprador = idcomprador;
        tipocomprador = "C";
    }

    public Comprador(Integer idcomprador, String contato) {
        this.idcomprador = idcomprador;
        this.contato = contato;
        tipocomprador = "C";
    }

    public Integer getIdComprador() {
        return idcomprador;
    }

    public void setIdComprador(Integer idcomprador) {
        this.idcomprador = idcomprador;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public String getDtAniver() {
        return dtaniver;
    }

    public void setDtAniver(String dtaniver) {
        this.dtaniver = dtaniver;
    }

    public String getTipoComprador() {
        return tipocomprador;
    }

    public void setTipoComprador(String tipocomprador) {
        this.tipocomprador = tipocomprador;
    }

    public String getMsn() {
        return msn;
    }

    public void setMsn(String msn) {
        this.msn = msn;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    public String getMesAniversario() {
        String mes = null;
        if (dtaniver != null)
            mes = dtaniver.substring(2);
        return mes;
    }

    public String getAnoAniversario() {
        String mes = null;
        if (dtaniver != null)
            mes = dtaniver.substring(0, 2);
        return mes;
    }

    public String getAniversarioExtenso() {
        if (dtaniver != null)
            return getMesAniversario() + "/" + getAnoAniversario();
        else
            return null;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idcomprador != null ? idcomprador.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Comprador)) {
            return false;
        }
        Comprador other = (Comprador) object;
        if ((this.idcomprador == null && other.idcomprador != null) || (this.idcomprador != null && !this.idcomprador.equals(other.idcomprador))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return contato;
    }

}
