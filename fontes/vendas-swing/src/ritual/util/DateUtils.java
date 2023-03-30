/*
 * DateUtils.java
 *
 * Created on November 25, 2006, 11:26 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package ritual.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.text.DateFormatter;

/**
 *
 * @author jaime
 */
public class DateUtils {

    static SimpleDateFormat formatter;
    static DateUtils du;
    static DateFormatter df;

    static {
        du = new DateUtils();
    }

    public int diferencaEmDias(Date dataLow, Date dataHigh) {

        GregorianCalendar startTime = new GregorianCalendar();
        GregorianCalendar endTime = new GregorianCalendar();

        GregorianCalendar curTime = new GregorianCalendar();
        GregorianCalendar baseTime = new GregorianCalendar();

        startTime.setTime(dataLow);
        endTime.setTime(dataHigh);

        int dif_multiplier = 1;

        // Verifica a ordem de inicio das datas  
        if (dataLow.compareTo(dataHigh) < 0) {
            baseTime.setTime(dataHigh);
            curTime.setTime(dataLow);
            dif_multiplier = 1;
        } else {
            baseTime.setTime(dataLow);
            curTime.setTime(dataHigh);
            dif_multiplier = -1;
        }

        int result_years = 0;
        int result_months = 0;
        int result_days = 0;

        // Para cada mes e ano, vai de mes em mes pegar o ultimo dia para import acumulando  
        // no total de dias. Ja leva em consideracao ano bissesto  
        while (curTime.get(GregorianCalendar.YEAR) < baseTime.get(GregorianCalendar.YEAR)
                || curTime.get(GregorianCalendar.MONTH) < baseTime.get(GregorianCalendar.MONTH)) {

            int max_day = curTime.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
            result_months += max_day;
            curTime.add(GregorianCalendar.MONTH, 1);

        }

        // Marca que é um saldo negativo ou positivo  
        result_months = result_months * dif_multiplier;


        // Retirna a diferenca de dias do total dos meses  
        result_days += (endTime.get(GregorianCalendar.DAY_OF_MONTH) - startTime.get(GregorianCalendar.DAY_OF_MONTH));

        return result_years + result_months + result_days;

    }

    private DateUtils() {
        formatter = new SimpleDateFormat("dd/MM/yyyy");
        formatter.setLenient(false);
        df = new DateFormatter(formatter);
    }

    public static Date parse(String dt) {
        try {
        return formatter.parse(dt);
        } catch (Exception e) {
            return null;
        }
    }

    public static String format(Date dt) {
        return formatter.format(dt);
    }
    
    public static String format(Date dt, String mask) {
        SimpleDateFormat sdf = new SimpleDateFormat(mask);
        return sdf.format(dt);
    }

    public static int getMonth(Date dt) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt);
        return calendar.get(Calendar.MONTH);
    }

    public static DateFormatter getDateFormatter() {
        return df;
    }

    public static SimpleDateFormat getFormat() {
        return formatter;
    }

    public static Date getDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    public static Date getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        String s = format(calendar.getTime());
        return parse(s);
    }
    
    public static Date getSimpleDate(Date dt) {
        String s = format(dt, "dd/MM/yyyy");
        return parse(s);
    }
    
    public static Date getNextDate(Date dt, int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt);
        calendar.add(Calendar.DATE, n);
        return calendar.getTime();
    }

    public static Date getFirstDate(Date dt) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    public static Date getLastDate(Date dt) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    public static Date addMonthToDate(Date dt, int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt);
        calendar.add(Calendar.MONTH, n);
        return calendar.getTime();
    }

    public static String[] getLastMonths(int mes) {
        String[] meses = {"JAN", "FEV", "MAR", "ABR", "MAI", "JUN", "JUL", "AGO", "SET", "OUT", "NOV", "DEZ"};
        String[] cabecalho = {"JAN", "FEV", "MAR", "ABR", "MAI", "JUN", "JUL", "AGO", "SET", "OUT", "NOV", "DEZ"};
        mes -= 11;
        if (mes < 0) {
            mes += 12;
        }
        for (int i = 0; i < 12; i++) {
            cabecalho[i] = meses[mes];
            mes++;
            if (mes > 11) {
                mes = 0;
            }
        }
        return cabecalho;
    }

    public static int getDiaSemana(Date value) {
        Calendar cal = Calendar.getInstance();    // hoje
        cal.setTime(value);    // uma Date
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    public static String getHora(Date value) {
        SimpleDateFormat f = new SimpleDateFormat("HH:mm");
        f.setLenient(false);
        return f.format(new Date());
    }

    public static void main(String args[]) {
       
        System.out.println(format(new Date(), "yyyy/MM/dd"));
    }

    public static int dataDiff(java.util.Date dataLow, java.util.Date dataHigh) {

        GregorianCalendar startTime = new GregorianCalendar();
        GregorianCalendar endTime = new GregorianCalendar();

        GregorianCalendar curTime = new GregorianCalendar();
        GregorianCalendar baseTime = new GregorianCalendar();

        startTime.setTime(dataLow);
        endTime.setTime(dataHigh);

        int dif_multiplier = 1;

        // Verifica a ordem de inicio das datas  
        if (dataLow.compareTo(dataHigh) < 0) {
            baseTime.setTime(dataHigh);
            curTime.setTime(dataLow);
            dif_multiplier = 1;
        } else {
            baseTime.setTime(dataLow);
            curTime.setTime(dataHigh);
            dif_multiplier = -1;
        }

        int result_years = 0;
        int result_months = 0;
        int result_days = 0;

        // Para cada mes e ano, vai de mes em mes pegar o ultimo dia para import acumulando  
        // no total de dias. Ja leva em consideracao ano bissesto  
        while (curTime.get(GregorianCalendar.YEAR) < baseTime.get(GregorianCalendar.YEAR)
                || curTime.get(GregorianCalendar.MONTH) < baseTime.get(GregorianCalendar.MONTH)) {

            int max_day = curTime.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
            result_months += max_day;
            curTime.add(GregorianCalendar.MONTH, 1);

        }

        // Marca que é um saldo negativo ou positivo  
        result_months = result_months * dif_multiplier;


        // Retirna a diferenca de dias do total dos meses  
        result_days += (endTime.get(GregorianCalendar.DAY_OF_MONTH) - startTime.get(GregorianCalendar.DAY_OF_MONTH));

        return result_years + result_months + result_days;
    }
}
