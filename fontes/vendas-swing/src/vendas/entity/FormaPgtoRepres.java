/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Sam
 */
@Entity
@Table(name = "TBFORMAPGTOREPRES")
@NamedQueries({
    @NamedQuery(name = "FormaPgtoRepres.findAll", query = "SELECT f FROM FormaPgtoRepres f"),
    @NamedQuery(name = "FormaPgtoRepres.findByIdrepres", query = "SELECT f FROM FormaPgtoRepres f WHERE f.formaPgtoRepresPK.idrepres = :idrepres"),
    @NamedQuery(name = "FormaPgtoRepres.findByIdpgto", query = "SELECT f FROM FormaPgtoRepres f WHERE f.formaPgtoRepresPK.idpgto = :idpgto")
})
public class FormaPgtoRepres implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FormaPgtoRepresPK formaPgtoRepresPK;
    @Column(name = "FRETE", length = 20)
    private String frete;
    @Column(name = "PGFRETE", length = 20)
    private String pgfrete;
    @Column(name = "IPI", length = 20)
    private String ipi;
    @Column(name = "ICMS", length = 20)
    private String icms;
    @Column(name = "ENTREGA", length = 20)
    private String entrega;
    @Column(name = "PEDMINIMO", length = 20)
    private String pedminimo;
    @Column(name = "PROCEDEN", length = 20)
    private String proceden;
    @Column(name = "NFPERC", length = 20)
    private String percentualNota;
    @Column(name = "OBS", length = 256)
    private String obs;
    @Column(name = "DISP", length = 20)
    private String disp;
    @JoinColumn(name = "IDPGTO", referencedColumnName = "IDPGTO", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private FormaPgto formaPgto;
    @JoinColumn(name = "IDREPRES", referencedColumnName = "IDREPRES", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Repres repres;

    public FormaPgtoRepres() {
        formaPgtoRepresPK = new FormaPgtoRepresPK();
    }

    public FormaPgtoRepres(FormaPgtoRepresPK formaPgtoRepresPK) {
        this.formaPgtoRepresPK = formaPgtoRepresPK;
    }

    public FormaPgtoRepres(int idrepres, String idpgto) {
        this.formaPgtoRepresPK = new FormaPgtoRepresPK(idrepres, idpgto);
    }

    public FormaPgtoRepresPK getFormaPgtoRepresPK() {
        return formaPgtoRepresPK;
    }

    public void setFormaPgtoRepresPK(FormaPgtoRepresPK formaPgtoRepresPK) {
        this.formaPgtoRepresPK = formaPgtoRepresPK;
    }

    public String getFrete() {
        return frete;
    }

    public void setFrete(String frete) {
        this.frete = frete;
    }

    public String getPgtoFrete() {
        return pgfrete;
    }

    public void setPgtoFrete(String pgfrete) {
        this.pgfrete = pgfrete;
    }

    public String getIpi() {
        return ipi;
    }

    public void setIpi(String ipi) {
        this.ipi = ipi;
    }

    public String getIcms() {
        return icms;
    }

    public void setIcms(String icms) {
        this.icms = icms;
    }

    public String getEntrega() {
        return entrega;
    }

    public void setEntrega(String entrega) {
        this.entrega = entrega;
    }

    public String getPedidoMinimo() {
        return pedminimo;
    }

    public void setPedidoMinimo(String pedminimo) {
        this.pedminimo = pedminimo;
    }

    public String getProcedencia() {
        return proceden;
    }

    public void setProcedencia(String proceden) {
        this.proceden = proceden;
    }

    public String getPercentualNota() {
        return percentualNota;
    }

    public void setPercentualNota(String nfperc) {
        this.percentualNota = nfperc;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public String getDisponibilidade() {
        return disp;
    }

    public void setDisponibilidade(String disp) {
        this.disp = disp;
    }

    public FormaPgto getFormaPgto() {
        return formaPgto;
    }

    public void setFormaPgto(FormaPgto formaPgto) {
        this.formaPgto = formaPgto;
    }

    public Repres getRepres() {
        return repres;
    }

    public void setRepres(Repres repres) {
        this.repres = repres;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (formaPgtoRepresPK != null ? formaPgtoRepresPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FormaPgtoRepres)) {
            return false;
        }
        FormaPgtoRepres other = (FormaPgtoRepres) object;
        if ((this.formaPgtoRepresPK == null && other.formaPgtoRepresPK != null) || (this.formaPgtoRepresPK != null && !this.formaPgtoRepresPK.equals(other.formaPgtoRepresPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "vendas.entity.FormaPgtoRepres[formaPgtoRepresPK=" + formaPgtoRepresPK + "]";
    }

}
