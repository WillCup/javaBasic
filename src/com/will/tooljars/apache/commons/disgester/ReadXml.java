package com.will.tooljars.apache.commons.disgester;

import org.apache.commons.digester3.Digester;

public class ReadXml {
    public static void main(String[] args) {
        Digester digester = new Digester();
        digester.addObjectCreate("Server/Service/Engine/Host", "com.mycompany.ServletBean");
        digester.addCallMethod("web-app/servlet/servlet-name",
                "setServletName", 0);
        digester.addCallMethod("web-app/servlet/servlet-class",
                "setServletClass", 0);
        digester.addCallMethod("web-app/servlet/init-param", "addInitParam", 2);
        digester.addCallParam("web-app/servlet/init-param/param-name", 0);
        digester.addCallParam("web-app/servlet/init-param/param-value", 1);
    }
}
