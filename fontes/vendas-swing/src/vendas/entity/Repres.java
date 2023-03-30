/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Sam
 */
@Entity
@Table(name = "TBREPRES")
@NamedQueries({
    @NamedQuery(name = "Repres.findAll", query = "SELECT r FROM Repres r"),
    @NamedQuery(name = "Repres.findByIdRepres", query = "SELECT r FROM Repres r WHERE r.idRepres = :idRepres"),
    @NamedQuery(name = "Repres.findByRazao", query = "SELECT r FROM Repres r WHERE r.razao = :razao"),
    @NamedQuery(name = "Repres.findByCnpj", query = "SELECT r FROM Repres r WHERE r.cnpj = :cnpj"),
    @NamedQuery(name = "Repres.findByDtInclusao", query = "SELECT r FROM Repres r WHERE r.dtInclusao = :dtInclusao")
})
public class Repres implements Serializable {
    @Id
    @GeneratedValue(generator="TBREPRES_IDREPRES_SEQ", strategy=GenerationType.SEQUENCE)
    @SequenceGenerator(name="TBREPRES_IDREPRES_SEQ", sequenceName="TBREPRES_IDREPRES_SEQ", allocationSize=1)
    @Basic(optional = false)
    @Column(name = "IDREPRES", nullable = false)
    private Integer idRepres;
    @Basic(optional = false)
    @Column(name = "RAZAO", nullable = false, length = 50)
    private String razao;
    @Column(name = "FANTASIA", length = 30)
    private String fantasia;
    @Column(name = "SITE", length = 100)
    private String site;
    @Column(name = "ENDERECO", length = 40)
    private String endereco;
    @Column(name = "CIDADE", length = 30)
    private String cidade;
    @Column(name = "BAIRRO", length = 25)
    private String bairro;
    @Column(name = "CEP", length = 8)
    private String cep;
    @Column(name = "UF", length = 2)
    private String uf;
    @Column(name = "CGC", length = 14)
    private String cnpj;
    @Column(name = "INSEST", length = 18)
    private String insest;
    @Column(name = "FONE1", length = 20)
    private String fone1;
    @Column(name = "FONE2", length = 20)
    private String fone2;
    @Column(name = "FONE3", length = 20)
    private String fone3;
    @Column(name = "DTINCLUS")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtInclusao;
    @Column(name = "DATATABELA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataTabela;
    @Column(name = "TOTAL", precision = 10, scale = 2)
    private BigDecimal total;
    @Column(name = "VALORFRETE", precision = 10, scale = 2)
    private BigDecimal valorFrete;
    @Column(name = "INATIVO")
    private Character inativo;
    @Column(name = "CONTACOR", length = 10)
    private String contacor;
    @Column(name = "AGENCIA", length = 8)
    private String agencia;
    @Basic(optional = false)
    @Column(name = "COMISSAO", nullable = false, precision = 10, scale = 2)
    private BigDecimal comissao;
    @Column(name = "DIASATENDIMENTO")
    private int diasAtendimento;
    @Lob
    @Column(name = "OBSERVACAO", length = 0)
    private String observacao;
    @Lob
    @Column(name = "OBSERVACAOCLIENTE", length = 0)
    private String observacaoCliente;
    @Lob
    @Column(name = "OBSPRODUTO", length = 0)
    private String obsProduto;
    @Lob
    @Column(name = "OBSPROMOCAO", length = 0)
    private String obspromocao;
    @Lob
    @Column(name = "METAVENDA", length = 0)
    private String metaVenda;
    @JoinTable(name = "TBSEGMENTOREPRES", joinColumns = {@JoinColumn(name = "IDREPRES", referencedColumnName = "IDREPRES", nullable = false)}, inverseJoinColumns = {@JoinColumn(name = "IDSEGMENTO", referencedColumnName = "IDSEGMENTO", nullable = false)})
    @ManyToMany
    private Collection<SegMercado> segmentos;
    @JoinTable(name = "TBREPRESTRANSP", joinColumns = {@JoinColumn(name = "IDREPRES", referencedColumnName = "IDREPRES", nullable = false)}, inverseJoinColumns = {@JoinColumn(name = "IDTRANS", referencedColumnName = "IDTRANS", nullable = false)})
    @ManyToMany
    private List<Transportador> transportadores;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "repres")
    private Collection<ContaRepres> contas;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "repres")
    private Collection<VendedorRepres> vendedorRepresCollection;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "repres")
    private Collection<EntradaRepres> entradaRepresCollection;
    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "repres")
    private Collection<FormaPgtoRepres> formaPgtoRepresCollection;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "repres")
    private Collection<ComissaoVendedor> comissaoVendedorCollection;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "repres")
    private Collection<ClienteRepres> clienteRepresCollection;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "repres")
    private Collection<RepresProduto> represProdutoCollection;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "repres")
    private Collection<Pedido> pedidoCollection;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "repres")
    private Collection<RepresContato> contatos;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "repres")
    private List<ArquivoPreco> arquivos;

    public Repres() {
        inativo = '0';
        comissao = new BigDecimal(50);
        dtInclusao = new Date();
    }

    public Repres(Integer idrepres) {
        this.idRepres = idrepres;
    }

    public Repres(Integer idrepres, String razao, BigDecimal comissao) {
        this.idRepres = idrepres;
        this.razao = razao;
        this.comissao = comissao;
    }

    public Integer getIdRepres() {
        return idRepres;
    }

    public void setIdRepres(Integer idrepres) {
        this.idRepres = idrepres;
    }

    public String getRazao() {
        return razao;
    }

    public void setRazao(String razao) {
        this.razao = razao;
    }

    public String getFantasia() {
        return fantasia;
    }

    public void setFantasia(String fantasia) {
        this.fantasia = fantasia;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
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

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cgc) {
        this.cnpj = cgc;
    }
    public Integer getDiasAtendimento() {
        return diasAtendimento;
    }

    public void setDiasAtendimento(int diasAtendimento) {
        this.diasAtendimento = diasAtendimento;
    }
    
    public String getInscrEstadual() {
        return insest;
    }

    public void setInscrEstadual(String insest) {
        this.insest = insest;
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

    public String getFone3() {
        return fone3;
    }

    public void setFone3(String fone3) {
        this.fone3 = fone3;
    }

    public Date getDtInclusao() {
        return dtInclusao;
    }

    public void setDtInclusao(Date dtinclus) {
        this.dtInclusao = dtinclus;
    }

    public Date getDataTabela() {
        return dataTabela;
    }

    public void setDataTabela(Date dataTabela) {
        this.dataTabela = dataTabela;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Character getInativo() {
        return inativo;
    }

    public void setInativo(Character inativo) {
        this.inativo = inativo;
    }

    public String getContacor() {
        return contacor;
    }

    public void setContacor(String contacor) {
        this.contacor = contacor;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public BigDecimal getComissao() {
        return comissao;
    }

    public void setComissao(BigDecimal comissao) {
        this.comissao = comissao;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getObsProduto() {
        return obsProduto;
    }

    public void setObsProduto(String obsproduto) {
        this.obsProduto = obsproduto;
    }

    public String getObsPromocao() {
        return obspromocao;
    }

    public void setObsPromocao(String obspromocao) {
        this.obspromocao = obspromocao;
    }

    public BigDecimal getValorFrete() {
        return valorFrete;
    }

    public void setValorFrete(BigDecimal valorFrete) {
        this.valorFrete = valorFrete;
    }

    public List<ArquivoPreco> getArquivos() {
        return arquivos;
    }

    public void setArquivos(List<ArquivoPreco> arquivos) {
        this.arquivos = arquivos;
    }

    public Collection<SegMercado> getSegmentos() {
        return segmentos;
    }

    public void setSegmentos(Collection<SegMercado> segMercadoCollection) {
        this.segmentos = segMercadoCollection;
    }

    public Collection<ContaRepres> getContas() {
        return contas;
    }

    public Collection<ContaRepres> getContasAtivas() {
        Collection<ContaRepres> lista = getContas();
        Collection<ContaRepres> result = new ArrayList<ContaRepres>();
        for (ContaRepres r : lista) {
            if ("A".equals(r.getAtivo())) {
                result.add(r);
            }
        }
        return result;
    }

    public void setContas(Collection<ContaRepres> contaRepresCollection) {
        this.contas = contaRepresCollection;
    }

    public Collection<VendedorRepres> getVendedores() {
        return vendedorRepresCollection;
    }

    public void setVendedores(Collection<VendedorRepres> vendedorRepresCollection) {
        this.vendedorRepresCollection = vendedorRepresCollection;
    }

    public Collection<EntradaRepres> getRecebimentos() {
        return entradaRepresCollection;
    }

    public void setRecebimentos(Collection<EntradaRepres> entradaRepresCollection) {
        this.entradaRepresCollection = entradaRepresCollection;
    }

    public Collection<FormaPgtoRepres> getFormasPgto() {
        return formaPgtoRepresCollection;
    }

    public void setFormasPgto(Collection<FormaPgtoRepres> formaPgtoRepresCollection) {
        this.formaPgtoRepresCollection = formaPgtoRepresCollection;
    }

    public Collection<ComissaoVendedor> getComissaoVendedorCollection() {
        return comissaoVendedorCollection;
    }

    public void setComissaoVendedorCollection(Collection<ComissaoVendedor> comissaoVendedorCollection) {
        this.comissaoVendedorCollection = comissaoVendedorCollection;
    }

    public Collection<ClienteRepres> getClientes() {
        return clienteRepresCollection;
    }

    public void setClientes(Collection<ClienteRepres> clienteRepresCollection) {
        this.clienteRepresCollection = clienteRepresCollection;
    }

    public Collection<RepresProduto> getProdutos() {
        return represProdutoCollection;
    }

    public void setProdutos(Collection<RepresProduto> represProdutoCollection) {
        this.represProdutoCollection = represProdutoCollection;
    }

    public Collection<Pedido> getPedidoCollection() {
        return pedidoCollection;
    }

    public void setPedidoCollection(Collection<Pedido> pedidoCollection) {
        this.pedidoCollection = pedidoCollection;
    }

    public Boolean getAtivo() {
        return new Boolean(inativo.equals('0'));
    }

    public void setAtivo(Boolean value) {
        if (value)
            inativo = '0' ;
        else
            inativo = '1';
    }

    public String getFones() {
        StringBuilder s = new StringBuilder();
        if (!(fone1 == null) && (fone1.trim().length() > 0))
            s.append(fone1);
        if (!(fone2 == null) && (fone2.trim().length() > 0)) {
            s.append("\n");
            s.append(fone2);
        }
        if (!(fone3 == null) && (fone3.trim().length() > 0)) {
            s.append("\n");
            s.append(fone3);
        }
        return s.toString().trim();
    }

    public String getEmail() {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (RepresContato email: contatos) {
            if (RepresContato.EMAIL.equals(email.getTipoContato())) {
                if (i > 0)
                   sb.append(", ");
                sb.append(email.getEndereco());
                i++;
            }
        }
        return sb.toString();
    }

    public String getNomeVendedores() {
        StringBuilder lista = new StringBuilder();
        int i = 0;
        for (VendedorRepres comprador : vendedorRepresCollection) {
//            if (comprador.getCorrespondencia() != null && comprador.getCorrespondencia() == 1) {
                if (i > 0) {
                    lista.append("/");
                }
                lista.append(comprador.getContato());
                i++;
  //           }
        }
        return lista.toString().trim();
    }

    public Collection<RepresContato> getContatos() {
        return contatos;
    }

    public void setContatos(Collection<RepresContato> contatos) {
        this.contatos = contatos;
    }

    public List<Transportador> getTransportadores() {
        return transportadores;
    }

    public void setTransportadores(List<Transportador> transportadores) {
        this.transportadores = transportadores;
    }

    public String getMetaVenda() {
        return metaVenda;
    }

    public void setMetaVenda(String metaVenda) {
        this.metaVenda = metaVenda;
    }
    
    public String getObservacaoCliente() {
        return observacaoCliente;
    }

    public void setObservacaoCliente(String observacaoCliente) {
        this.observacaoCliente = observacaoCliente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRepres != null ? idRepres.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Repres)) {
            return false;
        }
        Repres other = (Repres) object;
        if ((this.idRepres == null && other.idRepres != null) || (this.idRepres != null && !this.idRepres.equals(other.idRepres))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return razao;
    }

}
