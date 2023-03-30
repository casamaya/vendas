/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.entity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.hibernate.Hibernate;
import ritual.jasperreports.StringUtils;

/**
 *
 * @author Sam
 */
@Entity
@Table(name = "TBCLIENTE")
@NamedQueries({
    @NamedQuery(name = "Cliente.findAll", query = "SELECT c FROM Cliente c"),
    @NamedQuery(name = "Cliente.findByIdCliente", query = "SELECT c FROM Cliente c WHERE c.idCliente = :idCliente"),
    @NamedQuery(name = "Cliente.findByRazao", query = "SELECT c FROM Cliente c WHERE c.razao = :razao"),
    @NamedQuery(name = "Cliente.findByCidade", query = "SELECT c FROM Cliente c WHERE c.cidade = :cidade"),
    @NamedQuery(name = "Cliente.findByUf", query = "SELECT c FROM Cliente c WHERE c.uf = :uf"),
    @NamedQuery(name = "Cliente.findByCnpj", query = "SELECT c FROM Cliente c WHERE c.cnpj = :cnpj"),
    @NamedQuery(name = "Cliente.findByDtInclusao", query = "SELECT c FROM Cliente c WHERE c.dtInclusao = :dtInclusao")
})
public class Cliente implements Serializable {

    @Id
    @GeneratedValue(generator = "TBCLIENTE_IDCLIENTE_SEQ", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "TBCLIENTE_IDCLIENTE_SEQ", sequenceName = "TBCLIENTE_IDCLIENTE_SEQ", allocationSize = 1)
    @Basic(optional = false)
    @Column(name = "IDCLIENTE", nullable = false)
    private Integer idCliente;
    @Column(name = "BLOQUEADO", length = 1, nullable = false)
    private String bloqueado;
    @Column(name = "IDTIPO")
    private Integer idTipo;
    @Basic(optional = false)
    @Column(name = "RAZAO", nullable = false, length = 50)
    private String razao;
    @Column(name = "FANTASIA", length = 30)
    private String fantasia;
    @Column(name = "SITE", length = 100)
    private String site;
    @Column(name = "ENDERECO", length = 60)
    private String endereco;
    @Column(name = "BAIRRO", length = 25)
    private String bairro;
    @Column(name = "CIDADE", length = 30)
    private String cidade;
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
    @Column(name = "DTCANCEL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcancel;
    @Column(name = "COBRANCA", length = 50)
    private String cobranca;
    @Column(name = "ENTREGARBOLETO")
    private Character entregarBoleto;
    @Column(name = "EMPILHADEIRA")
    private Character empilhadeira;
    @Column(name = "SUBSTRIBUTARIA")
    private Character substituicaoTributaria;
    @Lob
    @Column(name = "FOTO", length = 0)
    private byte[] foto;
    @Lob
    @Column(name = "HISTORICO", length = 0)
    private String historico;
    @Column(name = "LIMITECREDITO", precision = 10, scale = 2)
    private BigDecimal limiteCredito;
    
    @Column(name = "NUMDIASATRASO", precision = 10, scale = 2)
    private BigDecimal numDiasAtraso;
    
    @Transient
    private BigDecimal valorPendente;
    @Transient
    private BigDecimal pgtoPendente;
    @Transient
    private Integer ultimoPedido;
    @JoinTable(name = "TBROTEIROCLIENTE", joinColumns = {
        @JoinColumn(name = "IDCLIENTE", referencedColumnName = "IDCLIENTE", nullable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "IDROTEIRO", referencedColumnName = "IDROTEIRO", nullable = false)})
    @ManyToMany
    private Collection<RoteiroVendedor> roteiros;
    @JoinTable(name = "TBSEGMENTOCLIENTE", joinColumns = {
        @JoinColumn(name = "IDCLIENTE", referencedColumnName = "IDCLIENTE", nullable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "IDSEGMENTO", referencedColumnName = "IDSEGMENTO", nullable = false)})
    @ManyToMany
    private Collection<SegMercado> segmentos;

    @JoinTable(name = "TBCLIENTEGRUPO", joinColumns = {
        @JoinColumn(name = "IDCLIENTE", referencedColumnName = "IDCLIENTE", nullable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "IDGRPCLIENTE", referencedColumnName = "IDGRPCLIENTE", nullable = false)})
    @ManyToMany
    private List<GrupoCliente> gruposCliente;

    @JoinTable(name = "TBVENDEDORCLIENTE", joinColumns = {
        @JoinColumn(name = "IDCLIENTE", referencedColumnName = "IDCLIENTE", nullable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "IDVENDEDOR", referencedColumnName = "IDVENDEDOR", nullable = false)})
    @ManyToMany
    private List<Vendedor> vendedores;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "cliente")
    private Collection<VisitaCliente> visitaClienteCollection;
    @JoinColumn(name = "IDSITCLIENTE", referencedColumnName = "IDSITCLIENTE", nullable = false)
    @ManyToOne(optional = false)
    private SituacaoCliente situacaoCliente;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "cliente")
    private Collection<ClienteEndereco> enderecos;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "cliente")
    private Collection<Comprador> compradorCollection;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "cliente")
    private Collection<ClienteRepres> representadas;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "cliente")
    private Collection<ClienteContato> contatos;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "cliente")
    private Collection<Pedido> pedidoCollection;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "cliente")
    private List<ReferenciaCliente> referenciasCliente;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "cliente")
    private List<BancoCliente> referenciasBanco;

    //@OneToMany(mappedBy = "clienteResponsavel")
    //private Collection<Pedido> pedidoCollection1;
    public Cliente() {
        empilhadeira = '0';
        entregarBoleto = '0';
        dtInclusao = new Date();
        situacaoCliente = new SituacaoCliente();
        bloqueado = "N";
    }

    public Cliente(Integer idcliente) {
        this();
        this.idCliente = idcliente;
    }

    public Cliente(String nome) {
        this();
        razao = nome;
    }

    public Cliente(Integer idcliente, String razao) {
        this();
        this.idCliente = idcliente;
        this.razao = razao;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idcliente) {
        this.idCliente = idcliente;
    }

    public Integer getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(Integer idTipo) {
        this.idTipo = idTipo;
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
        if (site == null) site = "";
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getEndereco() {
        return endereco;
    }
    public String getEnderecoExtenso() {
        String s = String.format("%s %s CEP %s %s/%s", endereco, bairro, StringUtils.formatarCep(cep), cidade, uf);
        return s;
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

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cgc) {
        this.cnpj = cgc;
    }

    public String getInscrEstadual() {
        return insest;
    }
    
    public String getCleanInscrEstadual() {
        if (insest == null) return "";
        String tmp = insest.replace(".", "");
        tmp = tmp.replace("-", "");
        tmp = tmp.replace("/", "");
        return tmp;
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

    public Date getDtcancel() {
        return dtcancel;
    }

    public void setDtcancel(Date dtcancel) {
        this.dtcancel = dtcancel;
    }

    public BigDecimal getLimiteCredito() {
        return limiteCredito;
    }

    public void setLimiteCredito(BigDecimal limiteCredito) {
        this.limiteCredito = limiteCredito;
    }

    public String getHistorico() {
        return historico;
    }

    public void setHistorico(String historico) {
        this.historico = historico;
    }

    public String getCobranca() {
        String value = "";
        for (ClienteEndereco endereco : enderecos) {
            if (endereco.getClienteEnderecoPK().getTipo() == ClienteEndereco.COBRANCA) {
                value = endereco.toString();
            }
        }
        return value;
    }

    public String getEntrega() {
        String value = "";
        for (ClienteEndereco endereco : enderecos) {
            if (endereco.getClienteEnderecoPK().getTipo() == ClienteEndereco.ENTREGA) {
                value = endereco.toString();
            }
        }
        return value;
    }

    public BigDecimal getNumDiasAtraso() {
        return numDiasAtraso;
    }

    public void setNumDiasAtraso(BigDecimal numDiasAtraso) {
        this.numDiasAtraso = numDiasAtraso;
    }

    public void setCobranca(String cobranca) {
        this.cobranca = cobranca;
    }

    public Character getEmpilhadeira() {
        return empilhadeira;
    }

    public void setEmpilhadeira(Character empilhadeira) {
        this.empilhadeira = empilhadeira;
    }

    public Character getSubstituicaoTributaria() {
        return substituicaoTributaria;
    }

    public Boolean isSubstituicaoTributaria() {
        return ((substituicaoTributaria != null) && (substituicaoTributaria == '1'));
    }
        
    public void setSubstituicaoTributaria(Character substituicaoTributaria) {
        this.substituicaoTributaria = substituicaoTributaria;
    }

    public Character getEntregarBoleto() {
        return entregarBoleto;
    }

    public void setEntregarBoleto(Character entregarBoleto) {
        this.entregarBoleto = entregarBoleto;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public BigDecimal getPgtoPendente() {
        return pgtoPendente;
    }

    public void setPgtoPendente(BigDecimal pgtoPendente) {
        this.pgtoPendente = pgtoPendente;
    }

    public BigDecimal getValorPendente() {
        return valorPendente;
    }

    public void setValorPendente(BigDecimal valorPendente) {
        this.valorPendente = valorPendente;
    }

    public Integer getUltimoPedido() {
        return ultimoPedido;
    }

    public void setUltimoPedido(Integer ultimoPedido) {
        this.ultimoPedido = ultimoPedido;
    }

    public void setBlob(Blob imageBlob) {
        this.foto = toByteArray(imageBlob);
    }

    public Blob getBlob() {
        return Hibernate.createBlob(this.foto);
    }

    public String getBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(String bloqueado) {
        this.bloqueado = bloqueado;
    }
    
    public void setBloqueado(Boolean isbloqueado) {
        this.bloqueado = isbloqueado ? "S" : "N";
    }
        
    public Boolean isBloqueado() {
        return "S".equals(this.bloqueado);
    }
        
    public VisitaCliente getUltimaVisita() {
        if (getVisitas() != null && !getVisitas().isEmpty()) {
            Object[] a = getVisitas().toArray();
            return (VisitaCliente) a[a.length - 1];
        } else {
            return null;
        }
        
    }

    private byte[] toByteArray(Blob fromImageBlob) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            return toByteArrayImpl(fromImageBlob, baos);
        } catch (Exception e) {
        }
        return null;
    }

    private byte[] toByteArrayImpl(Blob fromImageBlob,
            ByteArrayOutputStream baos) throws SQLException, IOException {
        byte buf[] = new byte[4000];
        int dataSize;
        InputStream is = fromImageBlob.getBinaryStream();

        try {
            while ((dataSize = is.read(buf)) != -1) {
                baos.write(buf, 0, dataSize);
            }
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return baos.toByteArray();
    }

    public Collection<RoteiroVendedor> getRoteiros() {
        return roteiros;
    }

    public void setRoteiros(Collection<RoteiroVendedor> roteiroVendedorCollection) {
        this.roteiros = roteiroVendedorCollection;
    }

    public Collection<SegMercado> getSegmentos() {
        return segmentos;
    }
    
    public String getSegmentosExtenso() {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        
        if (segmentos != null) {
            for (SegMercado seg : segmentos) {
                if (i > 0) {
                    sb.append(" / ");
                }
                sb.append(seg.getNome());
                i++;
            }
        }
        
        return sb.toString();
    }

    public void setSegmentos(Collection<SegMercado> segMercadoCollection) {
        this.segmentos = segMercadoCollection;
    }

    public List<GrupoCliente> getGruposCliente() {
        return gruposCliente;
    }

    public void setGruposCliente(List<GrupoCliente> gruposCliente) {
        this.gruposCliente = gruposCliente;
    }

    public Collection<VisitaCliente> getVisitas() {
        return visitaClienteCollection;
    }

    public void setVisitas(Collection<VisitaCliente> visitaClienteCollection) {
        this.visitaClienteCollection = visitaClienteCollection;
    }

    public SituacaoCliente getSituacaoCliente() {
        return situacaoCliente;
    }

    public void setSituacaoCliente(SituacaoCliente idsitcliente) {
        this.situacaoCliente = idsitcliente;
    }

    public Collection<ClienteEndereco> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(Collection<ClienteEndereco> clienteEnderecoCollection) {
        this.enderecos = clienteEnderecoCollection;
    }

    public Collection<Comprador> getCompradores() {
        return compradorCollection;
    }

    public void setCompradores(Collection<Comprador> compradorCollection) {
        this.compradorCollection = compradorCollection;
    }
    
    public String getVendedoresExtenso() {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (Vendedor vend : getVendedores()) {
            sb.append(vend.getNome());
            if (i > 0) sb.append(", ");
            i++;
        }
        return sb.toString();
    }

    public List<Vendedor> getVendedores() {
        return vendedores;
    }

    public void setVendedores(List<Vendedor> vendedores) {
        this.vendedores = vendedores;
    }

    public void setVendedor(Vendedor vendedor) {
        List<Vendedor> lista = new ArrayList();
        lista.add(vendedor);
        vendedores = lista;
    }

    public Collection<ClienteRepres> getRepresentadas() {
        return representadas;
    }

    public void setRepresentadas(Collection<ClienteRepres> clienteRepresCollection) {
        this.representadas = clienteRepresCollection;
    }

    public Collection<Pedido> getPedidos() {
        return pedidoCollection;
    }

    public void setPedidos(Collection<Pedido> pedidoCollection) {
        this.pedidoCollection = pedidoCollection;
    }
    /*
    public Collection<Pedido> getPedidoCollection1() {
    return pedidoCollection1;
    }
    
    public void setPedidoCollection1(Collection<Pedido> pedidoCollection1) {
    this.pedidoCollection1 = pedidoCollection1;
    }
     */

    public Boolean isEmpilhadeira() {
        return "1".equals(empilhadeira.toString());
    }

    public Boolean isEntregarBoleto() {
        return "1".equals(entregarBoleto.toString());
    }

    public Boolean getTemEmpilhadeira() {
        return "1".equals(empilhadeira.toString());
    }

    public void setTemEmpilhadeira(Boolean value) {
        if (value) {
            this.empilhadeira = '1';
        } else {
            this.empilhadeira = '0';
        }
    }

    public void setEmpilhadeira(Boolean value) {
        if (value) {
            this.empilhadeira = '1';
        } else {
            this.empilhadeira = '0';
        }
    }

    public void setSubstituicaoTributaria(Boolean value) {
        if (value) {
            this.substituicaoTributaria = '1';
        } else {
            this.substituicaoTributaria = '0';
        }
    }

    public void setEntregarBoleto(Boolean value) {
        if (value) {
            this.entregarBoleto = '1';
        } else {
            this.entregarBoleto = '0';
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCliente != null ? idCliente.hashCode() : 0);
        return hash;
    }

    public ClienteEndereco getEnderecoCobranca() {
        ClienteEndereco ce = null;
        if (enderecos != null) {
            for (ClienteEndereco ec : enderecos) {
                if (ec.getClienteEnderecoPK().getTipo() == ClienteEndereco.COBRANCA) {
                    ce = ec;
                    break;
                }
            }
        }
        if (ce == null) {
            ce = new ClienteEndereco();
            ce.getClienteEnderecoPK().setTipo(ClienteEndereco.COBRANCA);
            ce.getClienteEnderecoPK().setIdCliente(idCliente);
        }
        return ce;
    }

    public ClienteEndereco getEnderecoEntrega() {
        ClienteEndereco ce = null;
        if (enderecos != null) {
            for (ClienteEndereco ee : enderecos) {
                if (ee.getClienteEnderecoPK().getTipo() == ClienteEndereco.ENTREGA) {
                    ce = ee;
                    break;
                }
            }
        }
        if (ce == null) {
            ce = new ClienteEndereco();
            ce.getClienteEnderecoPK().setTipo(ClienteEndereco.ENTREGA);
            ce.getClienteEnderecoPK().setIdCliente(idCliente);
        }
        return ce;
    }

    public void updateEnderecos(ClienteEndereco value) {
        for (ClienteEndereco ec : enderecos) {
            if (ec.getClienteEnderecoPK().getTipo() == value.getClienteEnderecoPK().getTipo()) {
                ec = value;
            }
        }
    }

    public String getNomeCompradores() {
        StringBuilder lista = new StringBuilder();
        int i = 0;
        for (Comprador comprador : compradorCollection) {
            if ("C".equals(comprador.getTipoComprador())) {
                if (i > 0) {
                    lista.append("/");
                }
                lista.append(comprador.getContato());
                i++;
            }
        }
        return lista.toString().trim();
    }

    public String getFones() {
        StringBuilder s = new StringBuilder();
        if (!(fone1 == null) && (fone1.trim().length() > 0)) {
            s.append(fone1);
        }
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
    
    public String getFonesExtenso() {
        StringBuilder sb = new StringBuilder();
        if (!(fone1 == null) && (fone1.trim().length() > 0)) {
            sb.append(fone1).append(" / ");
        }
        if (!(fone2 == null) && (fone2.trim().length() > 0)) {
            sb.append(fone2).append(" / ");
        }
        if (!(fone3 == null) && (fone3.trim().length() > 0)) {
            sb.append(fone3).append(" /");
        }
        String value = sb.toString();
        value = value.substring(0, value.length() - 2);
        return value;
    }

    public Collection<ClienteContato> getContatos() {
        return contatos;
    }

    public void setContatos(Collection<ClienteContato> contatos) {
        this.contatos = contatos;
    }

    public String getEmail() {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (ClienteContato email : contatos) {
            if (ClienteContato.EMAILCOMERCIAL.equals(email.getTipoContato()) || ClienteContato.EMAILFINANCEIRO.equals(email.getTipoContato())) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(email.getEndereco());
                i++;
            }
        }
        return sb.toString();
    }

    public String getEmailMalaDireta() {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (ClienteContato email : contatos) {
            if (ClienteContato.EMAILCOMERCIAL.equals(email.getTipoContato())) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(email.getEndereco());
                i++;
            }
        }
        return sb.toString();
    }
    public List<String> getEmailMalaDireta2() {
        List<String> lista = new ArrayList<String>();
        int i = 0;
        for (ClienteContato email : contatos) {
            if (ClienteContato.EMAILCOMERCIAL.equals(email.getTipoContato())) {
                lista.add(email.getEndereco());
            }
        }
        return lista;
    }

    public String getEmailFNE() {
        for (ClienteContato email : contatos) {
            if (ClienteContato.EMAILFNE.equals(email.getTipoContato())) {
                return email.getEndereco();
            }
        }
        return "";
    }

    public List<ReferenciaCliente> getReferenciasCliente() {
        return referenciasCliente;
    }

    public void setReferencias(List<ReferenciaCliente> referencias) {
        this.referenciasCliente = referenciasCliente;
    }

    public List<BancoCliente> getReferenciasBanco() {
        return referenciasBanco;
    }

    public void setReferenciasBanco(List<BancoCliente> referenciasBanco) {
        this.referenciasBanco = referenciasBanco;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cliente)) {
            return false;
        }
        Cliente other = (Cliente) object;
        if ((this.idCliente == null && other.idCliente != null) || (this.idCliente != null && !this.idCliente.equals(other.idCliente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return razao;
    }

    public Object getListaVendedores() {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (Vendedor email : vendedores) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(email.getNome());
            i++;
        }
        return sb.toString();
    }
}
