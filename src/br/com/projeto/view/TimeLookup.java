/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.projeto.view;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.NtpV3Packet;
import org.apache.commons.net.ntp.TimeInfo;

/**
 *
 * @author LUCAS
 */
public class TimeLookup {

    public static String getNTPDat4e() {

        String[] hosts = new String[]{"time.nist.gov", "200.186.125.195"};

        NTPUDPClient client = new NTPUDPClient();
        // We want to timeout if a response takes longer than 5 seconds
        client.setDefaultTimeout(2000);
        SimpleDateFormat OutPutFormat = new SimpleDateFormat(
                "ddMMyyyy", java.util.Locale.getDefault());
        for (String host : hosts) {
            try {
                InetAddress hostAddr = InetAddress.getByName(host);
                TimeInfo info = client.getTime(hostAddr);
                Date date = new Date(info.getReturnTime());
                String out = OutPutFormat.format(date);
                return out;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        client.close();
        return null;
    }

public static Date getNTPDate() throws Exception{
    //FIXME: how to use system defined proxy here ?
    System.setProperty("java.net.useSystemProxies", "true");
    NTPUDPClient client = new NTPUDPClient();
    client.setDefaultTimeout(5000);
    TimeInfo response = client.getTime(InetAddress.getByName("pool.ntp.org"));
    return new Date(response.getReturnTime());
}

    // List of time servers: http://tf.nist.gov/tf-cgi/servers.cgi
    // Do not query time server more than once every 4 seconds
    public static final String TIME_SERVER = "pool.ntp.org";

    public static void main(String[] args) throws Exception {
        NTPUDPClient timeClient = new NTPUDPClient();
        InetAddress inetAddress = InetAddress.getByName(TIME_SERVER);
        TimeInfo timeInfo = timeClient.getTime(inetAddress);
        NtpV3Packet message = timeInfo.getMessage();
        Date serverTime = message.getTransmitTimeStamp().getDate();
        //Date time = new Date(serverTime);
        //System.out.println("Time from " + TIME_SERVER + ": " + serverTime);
        String dado = serverTime.toString();
        String sdf3 = new SimpleDateFormat("dd/MM/yyyy").format(new SimpleDateFormat("EEE MMM dd HH:mm:ss z YYYY", Locale.ROOT).parse(serverTime.toString()));
        dado = new SimpleDateFormat("ddMMyyyy")
				.format(new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy", Locale.US)
						.parse(dado));
        System.out.println(dado);
    }

}
