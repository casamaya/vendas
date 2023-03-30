/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vendas.util;

import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import org.hibernate.Hibernate;
import vendas.beans.EmailBean;

/**
 *
 * @author sam
 */
public class EmailUtil {
    public void sendMail(EmailBean email) throws Exception {
            Properties props = new Properties();
            props.put("mail.transport.protocol", "smtp"); //define protocolo de envio como SMTP
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", email.getServer()); //server SMTP do GMAIL
            props.put("mail.smtp.auth", "true"); //ativa autenticacao
            props.put("mail.smtp.user", email.getFrom()); //usuario ou seja, a conta que esta enviando o email (tem que ser do GMAIL)
            props.put("mail.debug", "true");
            props.put("mail.smtp.port", email.getPort()); //porta
            SimpleAuth auth = null;
            auth = new SimpleAuth(email.getFrom(), email.getPassword());
            Session session = Session.getDefaultInstance(props, auth);
            session.setDebug(false); //Habilita o LOG das ações executadas durante o envio do email

            Message msg = new MimeMessage(session);
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(email.getText() + '\n' + email.getAssinatura(), "text/plain");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);


            //Setando o destinatário
            for (String s : email.getTo()) {
                msg.addRecipient(Message.RecipientType.TO, new InternetAddress(s));
            }
            msg.setSubject(email.getSubject());
            msg.setFrom(new InternetAddress(email.getFrom()));
            if ((email.getAnexos() != null) && (!email.getAnexos().isEmpty())) {
                DataSource source = null;
                for (byte[] arq : email.getAnexos()) {
                    source = new ByteArrayDataSource(Hibernate.createBlob(arq).getBinaryStream(), "application/pdf");
                    messageBodyPart = new MimeBodyPart();
                    messageBodyPart.setDataHandler(new DataHandler(source));
                    messageBodyPart.setFileName("anexo");
                    multipart.addBodyPart(messageBodyPart);
                }
            }
            msg.setContent(multipart);
            //Objeto encarregado de enviar os dados para o email
            Transport tr;
            try {
                tr = session.getTransport("smtp"); //define smtp para transporte

                tr.connect(email.getServer(), email.getPort(), email.getFrom(), email.getPassword());
                msg.saveChanges(); // don't forget this
                tr.sendMessage(msg, msg.getAllRecipients());
                tr.close();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                throw e;
            }


    }


}
