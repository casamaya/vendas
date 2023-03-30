/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.beans;

import java.util.List;

/**
 *
 * @author Sam
 */
public class EmailBean {
    
    private String[] to;
    private String from;
    private String subject;
    private String text;
    private String server;
    private String user;
    private String password;
    private String assinatura;
    private Integer port;
    private List<byte[]> anexos;
    private Boolean incluiAnexoPadrao;

    public EmailBean() {
        to = new String[0];        
    }

    public Boolean getIncluiAnexoPadrao() {
        return incluiAnexoPadrao;
    }

    public void setIncluiAnexoPadrao(Boolean incluiAnexoPadrao) {
        this.incluiAnexoPadrao = incluiAnexoPadrao;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getAssinatura() {
        return assinatura;
    }

    public void setAssinatura(String assinatura) {
        this.assinatura = assinatura;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
    
    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }
    
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String[] getTo() {
        return to;
    }

    public void setTo(String[] to) {
        this.to = to;
    }

    public void setTo(List to) {
        String[]lista = (String[])to.toArray(new String[to.size()]);
        this.setTo(lista);
    }

    public void setTo(String to) {
        this.to = new String[1];
        this.to[0] = to;
    }

    public List<byte[]> getAnexos() {
        return anexos;
    }

    public void setAnexos(List<byte[]> anexos) {
        this.anexos = anexos;
    }
}
