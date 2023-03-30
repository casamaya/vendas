/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author Sam
 */
@Entity
@Table(name = "TBVISITACLIENTE")
@NamedQueries({
    @NamedQuery(name = "VisitaCliente.findAll", query = "SELECT v FROM VisitaCliente v"),
    @NamedQuery(name = "VisitaCliente.findByIdcliente", query = "SELECT v FROM VisitaCliente v WHERE v.cliente.idCliente = :idCliente"),
    @NamedQuery(name = "VisitaCliente.findByDtVisita", query = "SELECT v FROM VisitaCliente v WHERE v.dtVisita = :dtVisita")
})
public class VisitaCliente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(generator = "TBVISITACLIENTE_IDVISITA_SEQ", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "TBVISITACLIENTE_IDVISITA_SEQ", sequenceName = "TBVISITACLIENTE_IDVISITA_SEQ", allocationSize = 1)
    @Column(name = "IDVISITACLIENTE")
    private Integer idVisitaCliente;
    @Basic(optional = false)
    @Column(name = "DTVISITA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtVisita;
    @Column(name = "TIPOVISITA", length = 1)
    private String tipoVisita;
    @Column(name = "GEROUPEDIDO", length = 1)
    private String gerouPedido;
    @Column(name = "DTVISITAREAL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtVisitaRealizada;
    @JoinColumn(name = "IDVENDEDOR", referencedColumnName = "IDVENDEDOR")
    @ManyToOne(optional = false)
    private Vendedor vendedor;
    @JoinColumn(name = "IDCLIENTE", referencedColumnName = "IDCLIENTE")
    @ManyToOne(optional = true)
    private Cliente cliente;
    @Transient
    private Integer numeroClientes;
    @Transient
    private BigDecimal totalComissao;
    @Transient
    private Boolean showCliente;
    @Lob
    @Column(name = "OBS", length = 0)
    private String obs;
    @Transient
    private Boolean semVisita;
    @Transient
    private List<Cliente> listaClientes;

    public VisitaCliente() {
        tipoVisita = "V";
        gerouPedido = "0";
    }

    public Integer getIdVisitaCliente() {
        return idVisitaCliente;
    }

    public void setIdVisitaCliente(Integer idVisitaCliente) {
        this.idVisitaCliente = idVisitaCliente;
    }

    public String getTipoVisita() {
        return tipoVisita;
    }

    public void setTipoVisita(String tipoVisita) {
        this.tipoVisita = tipoVisita;
    }

    public Boolean getGerouPedido() {
        return ("1").equals(gerouPedido);
    }

    public void setGerouPedido(String gerouPedido) {
        this.gerouPedido = gerouPedido;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public Boolean getSemVisita() {
        return semVisita;
    }

    public void setSemVisita(Boolean semVisita) {
        this.semVisita = semVisita;
    }

    public Boolean getShowCliente() {
        return showCliente;
    }

    public void setShowCliente(Boolean showCliente) {
        this.showCliente = showCliente;
    }

    public BigDecimal getTotalComissao() {
        return totalComissao;
    }

    public void setTotalComissao(BigDecimal totalComissao) {
        this.totalComissao = totalComissao;
    }

    public Date getDtVisita() {
        return dtVisita;
    }

    public void setDtVisita(Date dtVisita) {
        this.dtVisita = dtVisita;
    }

    public Date getDtVisitaRealizada() {
        return dtVisitaRealizada;
    }

    public void setDtVisitaRealizada(Date dtVisitaRealizada) {
        this.dtVisitaRealizada = dtVisitaRealizada;
    }

    /**
     * Define o geroupedido deste VisitaCliente para o valor especificado.
     *
     * @param gerouPedido o novo geroupedido
     */
    public void setGerouPedido(Boolean gerouPedido) {
        if (gerouPedido) {
            this.gerouPedido = "1";
        } else {
            this.gerouPedido = "0";
        }
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor idvendedor) {
        this.vendedor = idvendedor;
    }

    public Integer getNumeroClientes() {
        return numeroClientes;
    }

    public void setNumeroClientes(Integer numeroClientes) {
        this.numeroClientes = numeroClientes;
    }

    public List<Cliente> getListaClientes() {
        return listaClientes;
    }

    public void setListaClientes(List<Cliente> listaClientes) {
        this.listaClientes = listaClientes;
    }
    
    public String getNomeCliente() {
        return cliente.getRazao();
    }

    public String getRazaoExtenso() {
        StringBuilder values = new StringBuilder();
        if (tipoVisita.equals("N")) {
            values.append("SEM VISITA");   
        } else {

            if (listaClientes == null || listaClientes.size() == 0) {
                return null;
            }

            String razao;
            int i = 0;

            for (Cliente item : listaClientes) {
                if (item != null) {
                    razao = item.getRazao();
                    if (razao.length() > 20) {
                        razao = razao.substring(0, 20).trim();
                    }

                    if (i++ > 0) {
                        values.append(" * ");
                    }

                    values.append(razao);
                }
            }
        }
        
        if (obs != null) {
            values.append(": ").append(obs);
        }

        return values.toString();
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idVisitaCliente != null ? idVisitaCliente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VisitaCliente)) {
            return false;
        }
        VisitaCliente other = (VisitaCliente) object;
        if ((this.idVisitaCliente == null && other.idVisitaCliente != null) || (this.idVisitaCliente != null && !this.idVisitaCliente.equals(other.idVisitaCliente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "vendas.entity.VisitaCliente[idVisitaCliente=" + idVisitaCliente + "]";
    }

}
