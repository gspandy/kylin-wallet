package com.rongcapital.wallet.constants;

import java.util.HashMap;
import java.util.Map;

import com.rongcapital.wallet.util.ConfigUtil;

public class ClientConstants {

    public static final Map<String,Integer> clientMap=new HashMap<>();
    public static final String CLIENT_PC = "pc";
    public static final String CLIENT_ANDRIOD = "andriod";
    public static final String CLIENT_APPLE = "apple";
    public static final String CLIENT_IPAD = "ipad";
    
    static{
        clientMap.put(CLIENT_ANDRIOD, 1);
        clientMap.put(CLIENT_APPLE, 2);
        clientMap.put(CLIENT_PC, 3);
        clientMap.put(CLIENT_IPAD, 4);
    }
  
}
