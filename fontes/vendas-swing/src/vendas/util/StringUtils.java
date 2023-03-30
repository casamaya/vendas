/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vendas.util;

import java.io.File;
import java.util.List;
import javax.swing.text.MaskFormatter;

/**
 *
 * @author Sam
 */
public class StringUtils {

    public static String CEP_MASK = "#####-###";
    public static String CPF_MASK = "###.###.###-##";
    public static String CNPJ_MASK = "##.###.###/####-##";

    public static String formatarCep(String texto) {
        texto = texto.replace(".", "");
        texto = texto.replace("-", "");
        if (texto == null || texto.length() == 0) {
            return "00000-000";
        }
        if (texto.length() <= 5) {
            return texto;
        }
        return formatarString(texto, CEP_MASK);
    }
    
    public static String listAsString(List lista) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lista.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(lista.get(i).toString());
        }
        return sb.toString();
    }

    public static String formatarCpf(String texto) {
        texto = texto.replace(".", "");
        texto = texto.replace("-", "");
        return formatarString(texto, CPF_MASK);
    }

    public static String formatarCnpj(String texto) {
        texto = texto.replace(".", "");
        texto = texto.replace("-", "");
        return formatarString(texto, CNPJ_MASK);
    }

    public static String formatarString(String texto, String mascara) {
        if (texto == null || texto.trim().length() == 0) {
            return null;
        }
        try {
            MaskFormatter mf = new MaskFormatter(mascara);
            mf.setValueContainsLiteralCharacters(false);
            return mf.valueToString(texto);
        } catch (Exception e) {
            System.out.println(texto);
            e.printStackTrace();
        }
        return texto;
    }

    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    public static String padl(String value, int len, String pad) {
        StringBuilder sb = new StringBuilder(value);
        int charsToGo = len - sb.length();
        while (charsToGo > 0) {
            sb.insert(0, '0');
            charsToGo--;

        }
        return sb.toString();
    }

    public static void main(String[] arg) {
        System.out.println(padl("12", 2, "0"));
    }

}
