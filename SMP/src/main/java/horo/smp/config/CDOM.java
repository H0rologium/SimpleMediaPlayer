package horo.smp.config;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

public class CDOM {

    public void loadConfig(Object callingClass)
    {
        Config cc = (Config)callingClass;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder b = factory.newDocumentBuilder();
            Document doc = b.parse(cc.configFile.getAbsolutePath());

            XPath xPath = XPathFactory.newInstance().newXPath();
            //My design for the config is that no tag will have more than one node ever.
            NodeList cfgNodes = (NodeList)xPath.evaluate("/Config/*",doc, XPathConstants.NODESET);

            for (int i  = 0; i < cfgNodes.getLength(); i++)
            {
                Node nd = cfgNodes.item(i);
                //This is a bad implementation?
                String val = nd.getTextContent();
                Object value = val.matches("-?\\d+") ? Integer.parseInt(val) :
                        val.matches("-?\\d*\\.\\d+") ? Double.parseDouble(val) :
                        val.equalsIgnoreCase("true") || val.equalsIgnoreCase("false") ? Boolean.parseBoolean(val) : val;

                cc.setConfigValue(value,nd.getNodeName());
            }
        } catch (Exception e)
        {
            Log.Error(this,"Failed to read config file");
            Log.Error(this,e.getMessage());
        }
    }

    public void saveConfig(Object callingClass)
    {
        Config cc = (Config) callingClass;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder b = factory.newDocumentBuilder();
            Document doc = b.newDocument();
            //
            Element ntravroot = createElementWithValue(doc,"Config");
            doc.appendChild(ntravroot);
            Element ntravchild = createElementWithValue(doc,"oneInstance");
            ntravchild.appendChild(doc.createTextNode(String.valueOf(cc.getOneInstance())));
            ntravroot.appendChild(ntravchild);
            ntravchild = createElementWithValue(doc,"winDimensionW");
            ntravchild.appendChild(doc.createTextNode(String.valueOf(cc.getWinDimensions()[0])));
            ntravroot.appendChild(ntravchild);
            ntravchild = createElementWithValue(doc,"winDimensionH");
            ntravchild.appendChild(doc.createTextNode(String.valueOf(cc.getWinDimensions()[0])));
            ntravroot.appendChild(ntravchild);
            ntravchild = createElementWithValue(doc,"volume");
            ntravchild.appendChild(doc.createTextNode(String.valueOf(cc.getVolume())));
            ntravroot.appendChild(ntravchild);
            //
            TransformerFactory tf_irl =  TransformerFactory.newInstance();
            Transformer transformer = tf_irl.newTransformer();
            DOMSource source = new DOMSource(doc);

            StreamResult sr = new StreamResult(cc.getConfigPath());
            transformer.transform(source,sr);
        } catch (Exception e)
        {
            Log.Error(this,"Failed to save config file");
            Log.Error(this,e.getMessage());
        }

    }

    private Element createElementWithValue(Document d, String elementName)
    {
        return d.createElement(elementName);
    }
}
