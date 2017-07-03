package com.rongcapital.wallet.util.xml;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;



public class XmlUtil {

    public static <T> T parseRsp(String xml, Class<T> key) {

        XStream xstream = new XStream(new DomDriver());

        xstream.processAnnotations(key);

        return (T) xstream.fromXML(xml);

    }
}
