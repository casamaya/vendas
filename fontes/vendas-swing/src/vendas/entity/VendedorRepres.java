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
@Table(name = "TBVENDEDORREPRES")
@NamedQueries({
    @NamedQuery(name = "VendedorRepres.findAll", query = "SELECT v FROM VendedorRepres v"),
    @NamedQuery(name = "VendedorRepres.findByIdvendedor", query = "SELECT v FROM VendedorRepres v WHERE v.idvendedor = :idvendedor")
})
public class VendedorRepres implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator="TBVENDEDORREPRES_IDVENDEDOR_SEQ", strategy=GenerationType.SEQUENCE)
    @SequenceGenerator(name="TBVENDEDORREPRES_IDVENDEDOR_SEQ", sequenceName="TBVENDEDORREPRES_IDVENDEDOR_SEQ", allocationSize=1)
    @Basic(optional = false)
    @Column(name = "IDVENDEDOR", nullable = false)
    private Integer idvendedor;
    @Basic(optional = false)
    @Column(name = "CONTATO", nullable = false, length = 30)
    private String contato;
    @Column(name = "ANIVERSARIO", length = 4)
    private String dtaniver;
    @Column(name = "CORRESPONDENCIA")
    private Short correspondencia;
    @Column(name = "MSN", length = 40)
    private String msn;
    @Lob
    @Column(name = "OBSERVACAO", length = 0)
    private String observacao;
    @JoinColumn(name = "IDREPRES", referencedColumnName = "IDREPRES", nullable = false)
    @ManyToOne(optional = false)
    private Repres repres;

    public VendedorRepres() {
    }

    public VendedorRepres(Integer idvendedor) {
        this.idvendedor = idvendedor;
        correspondencia = 1;
    }

    public VendedorRepres(Integer idvendedor, String contato) {
        this.idvendedor = idvendedor;
        this.contato = contato;
        correspondencia = 1;
    }

    public Integer getIdVendedor() {
        return idvendedor;
    }

    public void setIdVendedor(Integer idvendedor) {
        this.idvendedor = idvendedor;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public String getDtAniversario() {
        return dtaniver;
    }

    public void setDtAniversario(String dtaniver) {
        this.dtaniver = dtaniver;
    }

    public Short getCorrespondencia() {
        return correspondencia;
    }

    public void setCorrespondencia(Short correspondencia) {
        this.correspondencia = correspondencia;
    }

    public String getMsn() {
        return msn;
    }

    public void setMsn(String msn) {
        this.msn = msn;
    }

    public Repres getRepres() {
        return repres;
    }

    public void setRepres(Repres idrepres) {
        this.repres = idrepres;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idvendedor != null ? idvendedor.hashCode() : 0);
        return hash;
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
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VendedorRepres)) {
            return false;
        }
        VendedorRepres other = (VendedorRepres) object;
        if ((this.idvendedor == null && other.idvendedor != null) || (this.idvendedor != null && !this.idvendedor.equals(other.idvendedor))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return contato;
    }

}
