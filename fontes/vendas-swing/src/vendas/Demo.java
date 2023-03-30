/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;


/**
 *
 * @author sam
 */
public class Demo {

    private Date dateValue;

    public void gera(int tab) throws Exception {
        
        System.out.println("Importando tab " + tab);
        
        int i = -1;
        try {

            FileInputStream file = new FileInputStream(new File("/Users/sam/Dropbox/planosul.xls"));
            
            DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            String stData;

            //Get the workbook instance for XLS file 
            HSSFWorkbook workbook = new HSSFWorkbook(file);

            //Get first sheet from the workbook
            HSSFSheet sheet = workbook.getSheetAt(tab);
            System.out.println(sheet.getSheetName());

            //Iterate through each rows from first sheet
            Iterator<Row> rowIterator = sheet.iterator();
            MovTO to = new MovTO();
            StringBuilder s = new StringBuilder();
            
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Cell item = row.getCell(0);
                i++;
                if ((item == null) || (item.getCellType() != 0)) {
                    continue;
                }

                to.setDtMov(getValueAsDate(row.getCell(0)));
                to.setDoc(getValueAsString(row.getCell(1)));
                to.setDescr(getValueAsString(row.getCell(2)));
                to.setDebito(getValueAsNumeric(row.getCell(3)));
                to.setCredito(getValueAsNumeric(row.getCell(4)));
                to.setObs(getValueAsString(row.getCell(8)));

                s.append("insert into tbmovimento (idmovimento, idconta, dtmov, documento, descricao, tipo, valor, obs, idvendedor) values (");
                s.append("gen_id(TBMOVIMENTO_IDMOVIMENTO_SEQ, 1)").append(", ");
                s.append(tab).append(", ");
                stData = format.format(to.getDtMov());
                s.append("'").append(stData).append("', ");
                if (to.getDoc() == null)
                    s.append("null").append(", ");
                else
                    s.append("'").append(to.getDoc()).append("', ");
                if (to.getDescr() == null)
                    s.append("null").append(", ");
                else
                    s.append("'").append(to.getDescr()).append("', ");
                if (to.getDebito() == null) {
                    s.append("'1'").append(", ");
                } else {
                    s.append("'0'").append(", ");                    
                }
                if ((to.getDebito() == null) && (to.getCredito() == null)) {
                    s.append("0").append(", ");
                } else {
                    if (to.getDebito() == null) {
                        s.append(to.getCredito()).append(", ");
                    } else {
                        s.append(to.getDebito()).append(", ");                    
                    }
                }
                if (to.getObs() == null)
                    s.append("null, ");
                else    
                    s.append("'").append(to.getObs()).append("', ");
                s.append("1);\n");
                
            }
            file.close();
            FileOutputStream out
                    = new FileOutputStream(new File("/Users/sam/Downloads/planosul_" + tab + ".sql"));
            out.write(s.toString().getBytes());
            out.close();

        } catch (FileNotFoundException e) {
            System.out.println("Erro em " + i);
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Erro em " + i);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Demo demo = new Demo();
        demo.send();
    }

    private Date getValueAsDate(Cell cell) {
        if (cell == null) {
            return null;
        }
        Date value = null;
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    value = cell.getDateCellValue();
                }
                break;
            default:
                value = null;
        }
        return value;
    }

    private Boolean getValueAsBoolean(Cell cell) {
        if (cell == null) {
            return null;
        }
        Boolean value;
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_BOOLEAN:
                value = cell.getBooleanCellValue();
                break;
            default:
                value = null;

        }
        return value;
    }

    private String getValueAsString(Cell cell) {
        String value;
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                value = cell.getStringCellValue();
                break;
            default:
                value = null;

        }
        return value;
    }

    private Double getValueAsNumeric(Cell cell) {
        if (cell == null) {
            return null;
        }
        Double value;
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    value = null;
                } else {
                    value = cell.getNumericCellValue();
                }
                break;
            default:
                value = null;

        }
        return value;
    }
    
    public void send() {
        try {
            Properties props = new Properties();

            String server = "smtp.gmail.com";
            String from = "luiz@planosuldf.com.br";
            String passw = "lfl@2017";
            String user = "luiz@planosuldf.com.br";

            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.starttls.required", "true");
            props.put("mail.smtp.host", server); //server SMTP do GMAIL
            props.put("mail.smtp.auth", "true"); //ativa autenticacao
            props.put("mail.smtp.user", from); //usuario ou seja, a conta que esta enviando o email (tem que ser do GMAIL)
            props.put("mail.debug", "true");
            props.put("mail.smtp.port", "587"); //porta
            props.put("mail.smtp.auth.mechanisms", "PLAIN");
            props.put("mail.smtp.socketFactory.fallback", "false");
            props.put("mail.smtp.socketFactory.port", "465"); //SSL Port
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); //SSL Factory Class
            props.put("mail.smtp.ssl.protocols", "TLSv1.2");
            props.put("mail.smtp.ssl.enable", "true");

            Authenticator auth = new Authenticator() {
			//override the getPasswordAuthentication method
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, passw);
			}
		};
            Session session = Session.getDefaultInstance(props, auth);
            session.setDebug(true); //Habilita o LOG das ações executadas durante o envio do email

            //Objeto que contém a mensagem
            Message msg = new MimeMessage(session);

//            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
//            msg.addHeader("format", "flowed");
//            msg.addHeader("Content-Transfer-Encoding", "8bit");
            msg.setSentDate(new Date());

            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("messageBodyPart");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            
            msg.setContent(multipart);

            msg.addRecipient(Message.RecipientType.TO, new InternetAddress("jaime.oliveira@outlook.com"));

            //Setando a origem do email
            msg.setFrom(new InternetAddress(from));
            //Setando o assunto
            msg.setSubject("Teste de email");

            //Objeto encarregado de enviar os dados para o email
//            Transport.send(msg);
            Transport tr;
            tr = session.getTransport("smtp"); //define smtp para transporte
            //tr.connect();
            tr.connect(server, 587, user, passw);
            msg.saveChanges(); // don't forget this
            tr.sendMessage(msg, msg.getAllRecipients());
            tr.close();
        } catch (Exception e) {
            System.out.println(">> Erro: Envio Mensagem");
            e.printStackTrace();
        }
    }
}

class MovTO {

    Date dtMov;
    String doc;
    String descr;
    String tipo;
    String obs;
    Double debito;
    Double credito;

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public Date getDtMov() {
        return dtMov;
    }

    public void setDtMov(Date dtMov) {
        this.dtMov = dtMov;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Double getDebito() {
        return debito;
    }

    public void setDebito(Double debito) {
        this.debito = debito;
    }

    public Double getCredito() {
        return credito;
    }

    public void setCredito(Double credito) {
        this.credito = credito;
    }

}
