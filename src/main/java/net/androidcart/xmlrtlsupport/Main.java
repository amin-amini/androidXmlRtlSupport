package net.androidcart.xmlrtlsupport;

import net.androidcart.xmlrtlsupport.utils.Utils;

import java.io.File;

/**
 * Created by Amin Amini on 12/29/17.
 */
public class Main {

    public static final String rootOfXmls = "/root/Android/XmlRtlSupport/opencartXmls";
    public static final String rootOfXmlsOutput = "/root/Android/XmlRtlSupport/opencartXmlsOut";

    public static final boolean isLayoutDesignedInRtl = true;

    public static void main(String[] args) {
        Utils.iteratePath(rootOfXmls, new LayoutXMLFileProcessor());
    }
}
