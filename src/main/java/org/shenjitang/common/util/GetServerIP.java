package org.shenjitang.common.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class GetServerIP {

	public static String getIp(){   
        String LOCAL_IP=null;
        try{
        	Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
	        while (netInterfaces.hasMoreElements()) {
	            NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
	            Enumeration<InetAddress> inets = ni.getInetAddresses();//这个叠代不能少，否则在Linux下会有错
	            while(inets.hasMoreElements()){
	                InetAddress ip = inets.nextElement();
	                System.out.println(ip.getHostName() + "=" + ip.getHostAddress());
	                
	                if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {
	                    LOCAL_IP = ip.getHostAddress();
	                    
	                    System.out.println(LOCAL_IP + " is site local address!");
	                    return LOCAL_IP;
	                    //break;
	                }
	            }
	        }
        }catch (SocketException e){
        	e.printStackTrace();
        }
        return LOCAL_IP;
    }
	
}
