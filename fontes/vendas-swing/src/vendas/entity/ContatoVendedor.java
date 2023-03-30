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
@Table(name = "TBCONTATOVENDEDOR")
@NamedQueries({
    @NamedQuery(name = "ContatoVendedor.findAll", query = "SELECT c FROM ContatoVendedor c"),
    @NamedQuery(name = "ContatoVendedor.findByIdcomprador", query = "SELECT c FROM ContatoVendedor c WHERE c.idContato = :idContato"),
    @NamedQuery(name = "ContatoVendedor.findByContato", query = "SELECT c FROM ContatoVendedor c WHERE c.contato = :contato")
})
public class ContatoVendedor implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator="TBCONTATOVENDEDOR_IDCONTATO_SEQ", strategy=GenerationType.SEQUENCE)
    @SequenceGenerator(name="TBCONTATOVENDEDOR_IDCONTATO_SEQ", sequenceName="TBCONTATOVENDEDOR_IDCONTATO_SEQ", allocationSize=1)    @Basic(optional = false)
    @Column(name = "IDCONTATO", nullable = false)
    private Integer idContato;
    @Basic(optional = false)
    @Column(name = "CONTATO", nullable = false, length = 30)
    private String contato;
    @Column(name = "DTANIVER", length = 4)
    private String aniversario;
    @Column(name = "TIPOCONTATO", length = 1)
    private String tipoContato;
    @Column(name = "MSN", length = 40)
    private String msn;
    @Lob
    @Column(name = "OBSERVACAO", length = 0)
    private String observacao;
    @JoinColumn(name = "IDVENDEDOR", referencedColumnName = "IDVENDEDOR", nullable = false)
    @ManyToOne(optional = false)
    private Vendedor vendedor;

    public ContatoVendedor() {
        tipoContato = "C";
    }

    public ContatoVendedor(Integer idContato) {
        this.idContato = idContato;
        tipoContato = "C";
    }

    public Integer getidContato() {
        return idContato;
    }

    public void setidContato(Integer idContato) {
        this.idContato = idContato;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public String getAniversario() {
        return aniversario;
    }

    public String getDiaAniversario() {
        String mes = null;
        if (aniversario != null)
            mes = aniversario.substring(2);
        return mes;
    }

    public String getMesAniversario() {
        String mes = null;
        if (aniversario != null)
            mes = aniversario.substring(0, 2);
        return mes;
    }

    public String getAniversarioExtenso() {
        if (aniversario != null)
            return getDiaAniversario() + "/" + getMesAniversario();
        else
            return null;
    }

    public void setAniversario(String dtaniver) {
        this.aniversario = dtaniver;
    }

    public String getTipoContatoVendedor() {
        return tipoContato;
    }

    public void setTipoContatoVendedor(String tipoContato) {
        this.tipoContato = tipoContato;
    }

    public String getMsn() {
        return msn;
    }

    public void setMsn(String msn) {
        this.msn = msn;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idContato != null ? idContato.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ContatoVendedor)) {
            return false;
        }
        ContatoVendedor other = (ContatoVendedor) object;
        if ((this.idContato == null && other.idContato != null) || (this.idContato != null && !this.idContato.equals(other.idContato))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return contato;
    }

}
