package com.youssefdirani.HumTemp_v04;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ServerConfig {

    public ServerConfig(String panel_index , String panel_name ) {
        this.panel_index = panel_index;
        this.panel_name = panel_name;
    }

    String panel_name;
    public String staticIP = "";

    static private int port1 = 11359; //choose good ports, e.g. 43111; doesn't work. 256 and 257 work.
    //static private int port1 = 11357;
    static private int port2 = 11360;
    //static private int port2 = 11358;

    public String panel_index;
    int getPortFromIndex(int index) {
        if (index == 0) {
            return port1;
        } else if(index == 1) {
            return port2;
        } else {
            return -1;
        }
    }
    /*
    int getOtherPortFromIndex(int index) { //for debugging
        if (index == 0) {
            return port2;
        } else if(index == 1) {
            return port1;
        } else {
            return -1;
        }
    }
*/

    //MAKE SURE that the internet WiFi of the client at his home does not conflict with that of the modules
    //especially in terms of "192.168.4"
    //Anyway, it's a good practice to dedicate the last 55 IPs (thus from 192.168.4.251 till 192.168.4.255) to the modules,
    //so that later when a second mobile wants to use the network it assigns (increments) automatically his IP from his friend
    //and the IP assignment of the new mobile will imitate a loop (limited to the number of allowable mobiles)...

    public void setInternetDictionary() {
        //staticIP = "91.240.81.106";
        staticIP = "192.168.1.21";
    }
    /*void setInternetDictionary() {
        staticIP = "dirani.jvmhost.net";
    }
    */
}

