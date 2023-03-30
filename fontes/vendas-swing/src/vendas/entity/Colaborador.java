/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.entity;

import java.io.Serializable;
import java.util.Date;
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

/**
 *
 * @author jaimeoliveira
 */
@Entity
@Table(name = "TBCOLABORADOR")
@NamedQueries({
    @NamedQuery(name = "Colaborador.findAll", query = "SELECT c FROM Colaborador c")})
public class Colaborador implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "TBCOLABORADOR_ID_FUNCAO_SEQ", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "TBCOLABORADOR_ID_FUNCAO_SEQ", sequenceName = "TBCOLABORADOR_ID_FUNCAO_SEQ", allocationSize = 1)
    @Basic(optional = false)
    @Column(name = "IDCOLABORADOR", nullable = false)
    private Integer idcolaborador;
    @Basic(optional = false)
    @Column(name = "NOME", nullable = false, length = 100)
    private String nome;
    @Column(name = "RAZAO", length = 100)
    private String razao;
    @Column(name = "SITE", length = 100)
    private String site;
    @Column(name = "EMAIL", length = 100)
    private String email;
    @Column(name = "ENDERECO", length = 100)
    private String endereco;
    @Column(name = "BAIRRO", length = 25)
    private String bairro;
    @Column(name = "CIDADE", length = 30)
    private String cidade;
    @Column(name = "CEP", length = 8)
    private String cep;
    @Column(name = "UF", length = 2)
    private String uf;
    @Column(name = "DTANIVER", length = 4)
    private String dtAniversario;
    @Column(name = "CNPJ", length = 14)
    private String cnpj;
    @Column(name = "CPF", length = 11)
    private String cpf;
    @Column(name = "INSEST", length = 18)
    private String inscrEstadual;
    @Column(name = "FONE1", length = 20)
    private String fone1;
    @Column(name = "FONE2", length = 20)
    private String fone2;
    @Column(name = "FONE3", length = 20)
    private String fone3;
    @Column(name = "DTINCLUS")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtinclus;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DTULTVISITA")
    private Date dtUltVisita;
    @Lob
    @Column(name = "FOTO", length = 2147483647)
    private String foto;
    @Lob
    @Column(name = "OBSERVACAO", length = 2147483647)
    private String observacao;
    @JoinColumn(name = "IDFUNCAO", referencedColumnName = "IDFUNCAO")
    @ManyToOne
    private FuncaoColaborador funcaoColaborador;

    public Colaborador() {
    }

    public Colaborador(Integer idcolaborador) {
        this.idcolaborador = idcolaborador;
    }

    public Colaborador(Integer idcolaborador, String nome) {
        this.idcolaborador = idcolaborador;
        this.nome = nome;
    }

    public Integer getIdcolaborador() {
        return idcolaborador;
    }

    public void setIdcolaborador(Integer idcolaborador) {
        this.idcolaborador = idcolaborador;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRazao() {
        return razao;
    }

    public void setRazao(String razao) {
        this.razao = razao;
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

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getInscrEstadual() {
        return inscrEstadual;
    }

    public void setInscrEstadual(String inscrEstadual) {
        this.inscrEstadual = inscrEstadual;
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

    public Date getDtinclus() {
        return dtinclus;
    }

    public void setDtinclus(Date dtinclus) {
        this.dtinclus = dtinclus;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getDtAniversario() {
        return dtAniversario;
    }

    public void setDtAniversario(String dtAniversario) {
        this.dtAniversario = dtAniversario;
    }
    
    public String getDiaAniversario() {
        String mes = null;
        if (dtAniversario != null)
            mes = dtAniversario.substring(2);
        return mes;
    }

    public String getMesAniversario() {
        String mes = null;
        if (dtAniversario != null)
            mes = dtAniversario.substring(0, 2);
        return mes;
    }
    public FuncaoColaborador getFuncaoColaborador() {
        return funcaoColaborador;
    }

    public void setFuncaoColaborador(FuncaoColaborador funcaoColaborador) {
        this.funcaoColaborador = funcaoColaborador;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idcolaborador != null ? idcolaborador.hashCode() : 0);
        return hash;
    }

    public Date getDtUltVisita() {
        return dtUltVisita;
    }

    public void setDtUltVisita(Date dtUltVisita) {
        this.dtUltVisita = dtUltVisita;
    }
    
    public String getFones() {
        StringBuilder sb = new StringBuilder();
        if (fone1 != null) {
           sb.append(fone1).append("\n");
        }
        if (fone2 != null) {
           sb.append(fone2).append("\n");
        }
        if (fone3 != null) {
           sb.append(fone3);
        }
        return sb.toString();
    }
    
    public String getEnderecoExtenso() {
        StringBuilder sb = new StringBuilder();
        if (endereco != null) {
           sb.append(endereco).append(" ");
        }
        if (bairro != null) {
           sb.append(bairro).append(" ");
        }
        if (cidade != null) {
           sb.append(cidade).append(" ");
        }
        if (cep != null) {
           sb.append(cep).append(" ");
        }
        if (uf != null) {
           sb.append(uf).append(" ");
        }
        return sb.toString();
    }
    
    public String getAniversario() {
        return (getDiaAniversario() != null && getMesAniversario() != null) ? getDiaAniversario() + "/" + getMesAniversario() : null;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Colaborador)) {
            return false;
        }
        Colaborador other = (Colaborador) object;
        if ((this.idcolaborador == null && other.idcolaborador != null) || (this.idcolaborador != null && !this.idcolaborador.equals(other.idcolaborador))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nome;
    }
    
}
