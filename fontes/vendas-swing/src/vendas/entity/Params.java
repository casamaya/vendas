/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.entity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.SQLException;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import org.hibernate.Hibernate;

/**
 *
 * @author Sam
 */
@Entity
@Table(name = "TBPARAMS")
@NamedQueries({
    @NamedQuery(name = "Params.findAll", query = "SELECT p FROM Params p"),
    @NamedQuery(name = "Params.findById", query = "SELECT p FROM Params p WHERE p.id = :id")
})
public class Params implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "RAZAO", nullable = false, length = 50)
    private String razao;
    @Column(name = "FANTASIA", length = 50)
    private String fantasia;
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
    @Column(name = "ENDERECOMAIL", length = 100)
    private String enderecoMail;
    @Column(name = "CIDADEMAIL", length = 40)
    private String cidadeMail;
    @Column(name = "BAIRROMAIL", length = 40)
    private String bairroMail;
    @Column(name = "CEPMAIL", length = 8)
    private String cepMail;
    @Column(name = "UFMAIL", length = 2)
    private String ufMail;
    @Column(name = "CGC", length = 14)
    private String cgc;
    @Column(name = "INSEST", length = 18)
    private String insest;
    @Column(name = "FONE", length = 36)
    private String fone;
    @Column(name = "FONE2", length = 36)
    private String fone2;
    @Column(name = "EMAIL", length = 50)
    private String email;
    @Column(name = "CXPOSTAL", length = 15)
    private String cxpostal;
    @Lob
    @Column(name = "PROMOCAO", length = 0)
    private String promocao;
    @Lob
    @Column(name = "EMAILMSG", length = 0)
    private String emailMsg;
    @Lob
    @Column(name = "EMAILMSGCLIENTE", length = 0)
    private String emailMsgCliente;
    @Lob
    @Column(name = "EMAILMSGCOBRANCA", length = 0)
    private String emailMsgCobranca;
    @Lob
    @Column(name = "MSGCOBRANCA", length = 0)
    private String msgCobranca;
    @Lob
    @Column(name = "MSGANIVERSARIO", length = 0)
    private String msgAniversario;
    @Lob
    @Column(name = "ARQUIVO", length = 0)
    private byte[] logo;
    @Lob
    @Column(name = "ANEXOPADRAO", length = 0)
    private byte[] anexoPadrao;

    public Params() {
    }

    public Params(Integer id) {
        this.id = id;
    }

    public Params(Integer id, String razao) {
        this.id = id;
        this.razao = razao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getEnderecoMail() {
        return enderecoMail;
    }

    public void setEnderecoMail(String enderecoMail) {
        this.enderecoMail = enderecoMail;
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

    public String getCidadeMail() {
        return cidadeMail;
    }

    public void setCidadeMail(String cidadeMail) {
        this.cidadeMail = cidadeMail;
    }

    public String getBairroMail() {
        return bairroMail;
    }

    public void setBairroMail(String bairroMail) {
        this.bairroMail = bairroMail;
    }

    public String getCepMail() {
        return cepMail;
    }

    public void setCepMail(String cepMail) {
        this.cepMail = cepMail;
    }

    public String getUfMail() {
        return ufMail;
    }

    public void setUfMail(String ufMail) {
        this.ufMail = ufMail;
    }

    public String getCnpj() {
        return cgc;
    }

    public void setCnpj(String cgc) {
        this.cgc = cgc;
    }

    public String getInscEstadual() {
        return insest;
    }

    public void setInscEstadual(String insest) {
        this.insest = insest;
    }

    public String getFone() {
        return fone;
    }

    public void setFone(String fone) {
        this.fone = fone;
    }

    public String getFone2() {
        return fone2;
    }

    public void setFone2(String fone2) {
        this.fone2 = fone2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCxPostal() {
        return cxpostal;
    }

    public void setCxPostal(String cxpostal) {
        this.cxpostal = cxpostal;
    }

    public String getPromocao() {
        return promocao;
    }

    public void setPromocao(String promocao) {
        this.promocao = promocao;
    }

    public String getEmailMsg() {
        return emailMsg;
    }

    public void setEmailMsg(String emailMsg) {
        this.emailMsg = emailMsg;
    }

    public String getEmailMsgCliente() {
        return emailMsgCliente;
    }

    public void setEmailMsgCliente(String emailMsgCliente) {
        this.emailMsgCliente = emailMsgCliente;
    }

    public String getEmailMsgCobranca() {
        return emailMsgCobranca;
    }

    public void setEmailMsgCobranca(String emailMsgCobranca) {
        this.emailMsgCobranca = emailMsgCobranca;
    }

    public String getMsgCobranca() {
        return msgCobranca;
    }

    public String getMsgAniversario() {
        return msgAniversario;
    }

    public void setMsgAniversario(String msgAniversario) {
        this.msgAniversario = msgAniversario;
    }

    public void setMsgCobranca(String msgCobranca) {
        this.msgCobranca = msgCobranca;
    }
    public byte[] getArquivo() {
        return logo;
    }

    public void setArquivo(byte[] arquivo) {
        this.logo = arquivo;
    }
    
    
    public void setBlobArquivo(Blob imageBlob) {
        this.logo = toByteArray(imageBlob);
    }

    public Blob getBlobArquivo() {
        return Hibernate.createBlob(this.logo);
    }
    ////////
    public byte[] getAnexoPadrao() {
        return anexoPadrao;
    }

    public void setAnexoPadrao(byte[] arquivo) {
        this.anexoPadrao = arquivo;
    }
    
    
    public void setBlobAnexoPadrao(Blob imageBlob) {
        this.anexoPadrao = toByteArray(imageBlob);
    }

    public Blob getBlobAnexoPadrao() {
        return Hibernate.createBlob(this.anexoPadrao);
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
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Params)) {
            return false;
        }
        Params other = (Params) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "vendas.entity.Params[id=" + id + "]";
    }

}
