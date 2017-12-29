package net.androidcart.xmlrtlsupport;

import com.sun.org.apache.xml.internal.serialize.LineSeparator;
import net.androidcart.xmlrtlsupport.utils.IFileProcessor;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;

/**
 * Created by Amin Amini on 12/29/17.
 */
public class LayoutXMLFileProcessor implements IFileProcessor {

    boolean didFileChanged = false;

    public boolean process(String path) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(path);
        fixChildren(doc.getDocumentElement());

        writeMeToFile(doc, path);
        return didFileChanged;
    }

    void fixChildren(Element nodeIn){
        if(nodeIn==null){
            return;
        }

        NodeList nodeList = nodeIn.getChildNodes();
        for(int i=0 ; i<nodeList.getLength() ; i++) {
            Node currentNode = nodeList.item(i);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE){
                fixChildren( (Element) currentNode);
            }
        }
        fixNode(nodeIn);
    }

    void fixNode(Element node){
        NamedNodeMap attribs = node.getAttributes();

        for (int i=0 ; i<attribs.getLength() ; i++){
            Node attrib = attribs.item(i);
            String atName = attrib.getNodeName();
            String atVal = attrib.getNodeValue();
            //System.out.println(attrib.getNodeName() + " : " + attrib.getNodeValue());

            String newName = getRequiredAttributeName(atName, Main.isLayoutDesignedInRtl);
            if(newName!=null && !node.hasAttribute(newName) ) {
                //System.out.println(attrib.getNodeName() + "  ->  " + newName);
                node.setAttribute(newName, atVal );
                didFileChanged = true;
            }

            if( atVal!=null ){
                node.setAttribute(atName, getRequiredAttributeValue(atVal));
            }
        }
    }

    String getRequiredAttributeName(String inAttributeName, boolean rtl){
        if( inAttributeName.startsWith("android:") ){
            if( inAttributeName.contains("Right") ){
                return inAttributeName.replace("Right" , (rtl ? "Start" : "End") );
            }
            if( inAttributeName.contains("Left") ){
                return inAttributeName.replace("Left" , (rtl ? "End" : "Start") );
            }
        }
        return null;
    }

    String getRequiredAttributeValue(String inAttributeValue){
        String[] values = inAttributeValue.split("\\|");
        String[] ans = new String[values.length];

        for(int i=0 ;i<values.length ; i++){
            String val = values[i];
            if( val.equals("right") ){
                val = (Main.isLayoutDesignedInRtl ? "start" : "end");
                didFileChanged = true;
            }
            else if( val.equals("left") ){
                val = (Main.isLayoutDesignedInRtl ? "end" : "start");
                didFileChanged = true;
            }
            ans[i] = val;
        }

        return String.join("|",ans);
    }

    static void writeMeToFile(Document doc, String path) throws Exception{
        String pathOut = path.replace(Main.rootOfXmls, Main.rootOfXmlsOutput);
        File fileOut = new File(pathOut);
        fileOut.getParentFile().mkdirs();
        DOMSource source = new DOMSource(doc);
        FileWriter writer = new FileWriter(fileOut);
        StreamResult result = new StreamResult(writer);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        transformer.transform(source, result);
    }

    public boolean shouldProcessFile(String path) {
        return path!=null && path.toLowerCase().endsWith(".xml");
    }
}
