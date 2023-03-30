package ritual.jasperreports;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.text.MaskFormatter;
import net.sf.jasperreports.engine.JRDefaultScriptlet;

/**
 *
 * @author Sam
 */
public class StringUtils extends JRDefaultScriptlet {

    public static String CEP_MASK = "#####-###";
    public static String CPF_MASK = "###.###.###-##";
    public static String CNPJ_MASK = "##.###.###/####-##";

    public static String formatarCep(String texto) {
        if (texto == null || texto.length() == 0) return "";
        return formatarString(texto, CEP_MASK);
    }
    public static String formatarCpf(String texto) {
        return formatarString(texto, CPF_MASK);
    }
    public static String formatarCnpj(String texto) {
        return formatarString(texto, CNPJ_MASK);
    }

    public static String formatarData(Date dt) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        formatter.setLenient(false);
        return formatter.format(dt);
    }

    public static String formatarString(String texto, String mascara) {
        try {
            MaskFormatter mf = new MaskFormatter(mascara);
            mf.setValueContainsLiteralCharacters(false);
            return mf.valueToString(texto);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return texto;
    }

}
