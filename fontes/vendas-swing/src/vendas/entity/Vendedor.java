/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author Sam
 */
@Entity
@Table(name = "TBVENDEDOR")
@NamedQueries({
    @NamedQuery(name = "Vendedor.findAll", query = "SELECT v FROM Vendedor v order by v.nome"),
    @NamedQuery(name = "Vendedor.findByName", query = "SELECT v FROM Vendedor v WHERE v.nome like :nome order by v.nome")
})
public class Vendedor implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator="TBVENDEDOR_IDVENDEDOR_SEQ", strategy=GenerationType.SEQUENCE)
    @SequenceGenerator(name="TBVENDEDOR_IDVENDEDOR_SEQ", sequenceName="TBVENDEDOR_IDVENDEDOR_SEQ", allocationSize=1)
    @Basic(optional = false)
    @Column(name = "IDVENDEDOR", nullable = false)
    private Integer idVendedor;
    @Basic(optional = false)
    @Column(name = "NOME", nullable = false, length = 40)
    private String nome;
    @Column(name = "ENDERECO", length = 40)
    private String endereco;
    @Column(name = "BAIRRO", length = 30)
    private String bairro;
    @Column(name = "CIDADE", length = 30)
    private String cidade;
    @Column(name = "CEP", length = 8)
    private String cep;
    @Column(name = "UF", length = 2)
    private String uf;
    @Column(name = "COMISSAO", length = 1)
    private String comissionado;
    @Column(name = "FONE1", length = 20)
    private String fone1;
    @Column(name = "FONE2", length = 20)
    private String fone2;
    @Column(name = "ATIVO", length = 1)
    @Basic(optional = false)
    private String ativo;
    @Column(name = "SIGLA", length = 3)
    @Basic(optional = false)
    private String sigla;
    @Column(name = "VALCOMISSAO", precision = 5, scale = 2)
    private BigDecimal valorComissao;
    @Column(name = "ANIVERSARIO", length = 4)
    private String dtAniver;
    @Column(name = "EMAIL", length = 100)
    private String email;
    @OneToMany(cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, mappedBy = "vendedor")
    private Collection<VisitaCliente> visitas;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "vendedor")
    private Collection<ComissaoVendedor> comissoesVendedor;
    @OneToMany(cascade = {CascadeType.REMOVE, CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "vendedor")
    private Collection<RoteiroVendedor> roteiros;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "vendedor")
    private Collection<Pedido> pedidos;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "vendedor")
    private Collection<ContatoVendedor> contatos;
    @ManyToMany(mappedBy = "vendedores")
    private List<Cliente> clientes;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "vendedor")
    private List<ReciboComissao> reciboComissaoList;
        
    public Vendedor() {
        comissionado = "0";
        valorComissao = new BigDecimal(50);
    }

    public Vendedor(Integer idvendedor) {
        this.idVendedor = idvendedor;
    }

    public Vendedor(String value) {
        nome = value;
    }

    public Vendedor(Integer idvendedor, String nome) {
        this.idVendedor = idvendedor;
        this.nome = nome;
    }

    public Integer getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(Integer idvendedor) {
        this.idVendedor = idvendedor;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getComissionado() {
        return comissionado;
    }

    public void setComissionado(String comissao) {
        this.comissionado = comissao;
    }

    public String getFone1() {
        return fone1;
    }

    public void setFone1(String fone1) {
        this.fone1 = fone1;
    }

    public String getFone2() {
        return fone2;
    }

    public void setFone2(String fone2) {
        this.fone2 = fone2;
    }

    public BigDecimal getValorComissao() {
        return valorComissao;
    }

    public void setValorComissao(BigDecimal valcomissao) {
        this.valorComissao = valcomissao;
    }

    public String getAtivo() {
        return ativo;
    }

    public void setAtivo(String ativo) {
        this.ativo = ativo;
    }

    public void setAtivo(Boolean ativo) {
        if (ativo)
            this.ativo = "1";
        else
            this.ativo = "0";
    }

    public Boolean isAtivo() {
        return "1".equals(ativo);
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public Collection<VisitaCliente> getVisitas() {
        return visitas;
    }

    public void setVisitas(Collection<VisitaCliente> visitaClienteCollection) {
        this.visitas = visitaClienteCollection;
    }

    public Collection<Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(List<Cliente> clienteCollection) {
        this.clientes = clienteCollection;
    }

    public Collection<ComissaoVendedor> getComissoesVendedor() {
        return comissoesVendedor;
    }

    public void setComissoesVendedor(Collection<ComissaoVendedor> comissaoVendedorCollection) {
        this.comissoesVendedor = comissaoVendedorCollection;
    }

    public String getDtAniver() {
        return dtAniver;
    }

    public void setDtAniver(String dtAniver) {
        this.dtAniver = dtAniver;
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

    public String getAniversarioExtenso() {
        if (dtAniver != null)
            return getMesAniversario() + "/" + getAnoAniversario();
        else
            return null;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public Collection<RoteiroVendedor> getRoteiros() {
        return roteiros;
    }

    public void setRoteiros(Collection<RoteiroVendedor> roteiroVendedorCollection) {
        this.roteiros = roteiroVendedorCollection;
    }

    public Collection<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(Collection<Pedido> pedidoCollection) {
        this.pedidos = pedidoCollection;
    }

    public Boolean getRecebeComissao() {
        return comissionado.equals("1");
    }

    public void setRecebeComissao(Boolean value) {
        if (value)
            comissionado = "1";
        else
            comissionado = "0";
    }

    public void setComissionado(Boolean value) {
        if (value)
            comissionado = "1";
        else
            comissionado = "0";
    }

    public Collection<ContatoVendedor> getContatos() {
        return contatos;
    }

    public void setContatos(Collection<ContatoVendedor> contatos) {
        this.contatos = contatos;
    }

    public List<ReciboComissao> getReciboComissaoList() {
        return reciboComissaoList;
    }

    public void setReciboComissaoList(List<ReciboComissao> reciboComissaoList) {
        this.reciboComissaoList = reciboComissaoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idVendedor != null ? idVendedor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Vendedor)) {
            return false;
        }
        Vendedor other = (Vendedor) object;
        if ((this.idVendedor == null && other.idVendedor != null) || (this.idVendedor != null && !this.idVendedor.equals(other.idVendedor))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nome;
    }

}
