package br.com.projeto.view;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class TimeValidationHook {

  private static String getServerHttpDate(String serverUrl) throws IOException {
    URL url = new URL(serverUrl);
    URLConnection connection = url.openConnection();

    Map<String, List<String>> httpHeaders = connection.getHeaderFields();

    for (Map.Entry<String, List<String>> entry : httpHeaders.entrySet()) {
      String headerName = entry.getKey();
      if (headerName != null && headerName.equalsIgnoreCase("date")) {
        return entry.getValue().get(0);
      }
    }
    return null;
  }
  
  public String getNetData() throws IOException {
        String serverUrl = "https://www.google.com.br";
        //System.out.println(getServerHttpDate(serverUrl));
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        //System.out.println(ts);

        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);

        SimpleDateFormat sdf2 = new SimpleDateFormat("ddMMyyyy");
        Date date2 = null;
        Date date1 = null;
        try {
            date1 = sdf.parse(getServerHttpDate(serverUrl));
            date2 = sdf2.parse(ts.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sdf2.format(date1);
    }

  public static void main(String[] args) throws IOException {

    String serverUrl = args.length > 0 ? args[0] : "https://www.google.com.br";
    System.out.println(getServerHttpDate(serverUrl));
    Timestamp ts = new Timestamp(System.currentTimeMillis());
    System.out.println(ts);

//formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US)    Tue, 24 Jul 2018 13:25:37 GMT
    SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);

    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
    Date date2 = null;
    Date date1 = null;
        try
        {
            date1 = sdf.parse(getServerHttpDate(serverUrl));
            date2 = sdf2.parse(ts.toString());
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        System.out.println("date1 : " + sdf2.format(date1));
    System.out.println("date2 : " + sdf2.format(date2));

    if (date1.compareTo(date2) > 0) {
        System.out.println("Date1 is after Date2"); 

        List<String> cmd = new ArrayList<String>();
        //cmd.add("hg rollback");
        //ProcessBuilder pb = new ProcessBuilder(cmd);
        //pb.directory(new File("E:\\repos\\Trunk"));
        //Process p = pb.start();
    } else if (date1.compareTo(date2) < 0) {
        System.out.println("Date1 is before Date2");
    } else if (date1.compareTo(date2) == 0) {
        System.out.println("Date1 is equal to Date2");
    } else {
        System.out.println("How to get here?");
    }

  }
}

