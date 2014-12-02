package com.will.tooljars.xml;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.will.Consts;
import com.will.exception.ExceptionCollector;

public class XMLReader {
    private final static Log logger = LogFactory.getLog(EncryptXMLReader.class);

    private static DocumentBuilderFactory documentBuilderFactory;
    private static XPathFactory xPathFactory;
    public String xmlContent;

    private File file;

    public XMLReader(File file) {
        super();
        this.file = file;
    }

    /**
     * Get the root document directly.
     * 
     * @return
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     */
    public Document getRootDoc() throws SAXException, IOException,
            ParserConfigurationException {
//      logger.warn(Consts.CHARSET + getDecryptedContent());
        StringReader xmlReader = new StringReader(getDecryptedContent());
        Document rootDocument = getBuilder().parse(new InputSource(xmlReader));
        return rootDocument;
    }

    /**
     * Get the node we specified directly.
     * 
     * @param configXPath
     * @return
     * @throws XPathExpressionException
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     */
    public Node getUniqueNode(String configXPath)
            throws XPathExpressionException, SAXException, IOException,
            ParserConfigurationException {
        XPathExpression expr = getXPath().compile(configXPath);
        NodeList rootNodes = (NodeList) expr.evaluate(getRootDoc(),
                XPathConstants.NODESET);
        Node configEle = rootNodes.item(0);
        return configEle;
    }

    /**
     * Get the node we specified directly.
     * 
     * @param configXPath
     * @return
     * @throws XPathExpressionException
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     */
    public NodeList getNodeList(String subElement, Node parentNode)
            throws XPathExpressionException, SAXException, IOException,
            ParserConfigurationException {
        return (NodeList) getXPath().evaluate(subElement, parentNode,
                XPathConstants.NODESET);
    }

    /**
     * Get the node we specified directly.
     * 
     * @param subElement
     * @return
     * @throws XPathExpressionException
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     */
    public Node getUniqueNode(String subElement, Node parentNode)
            throws XPathExpressionException, SAXException, IOException,
            ParserConfigurationException {
        return (Node) getXPath().evaluate(subElement, parentNode,
                XPathConstants.NODE);
    }

    /**
     * Get the node we specified directly.
     * 
     * @param configXPath
     * @return
     * @throws XPathExpressionException
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     */
    public NodeList getNodeList(String configXPath)
            throws XPathExpressionException, SAXException, IOException,
            ParserConfigurationException {
        XPathExpression expr = getXPath().compile(configXPath);
        NodeList rootNodes = (NodeList) expr.evaluate(getRootDoc(),
                XPathConstants.NODESET);
        return rootNodes;
    }

    /**
     * Extract the attribute's value, if it's null, return defVal.
     * 
     * @param n
     * @param attribute
     * @param defVal
     * @return
     */
    public String getNodeAttrValue(Node n, String attribute, String defVal) {
        NamedNodeMap attributes = n.getAttributes();
        if (attributes == null) {
            return defVal;
        }
        Node attr = attributes.getNamedItem(attribute);
        if (attr == null || attr.getNodeValue() == null) {
            return defVal;
        }
        return attr.getNodeValue();
    }

    /**
     * Get the content, decrypt it, then return.
     * 
     * <br/>
     * 
     * @return
     */
    public String getDecryptedContent() {
        try {
            xmlContent = FileUtils.readFileToString(this.file);
        } catch (Exception e) {
            logger.error("Error happens in : \n "
                    + ExceptionCollector.getStackTrace(e));
            ExceptionCollector.registerException(e);
        }
        return xmlContent;
    }

    /**
     * Get the file content directly, without decoding.We cannot avoid reading
     * the xml, so just get the newest content directly.
     * 
     * <br/>
     * 
     * @return
     * @throws IOException
     */
    public String getContent() throws IOException {
        return FileUtils.readFileToString(this.file, Consts.CHARSET);
    }

    /**
     * For cached content.
     * 
     * <br/>
     * 
     * @return
     */
    public String getContentCache() {
        if (xmlContent == null) {
            xmlContent = getDecryptedContent();
        }
        return xmlContent;
    }

    public DocumentBuilder getBuilder() throws ParserConfigurationException {
        if (documentBuilderFactory == null) {
            documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setNamespaceAware(true); // 指定由此代码生成的解析器将提供对XML名称空间的支持
        }
        return documentBuilderFactory.newDocumentBuilder();
    }

    public XPath getXPath() {
        if (xPathFactory == null) {
            xPathFactory = XPathFactory.newInstance();
        }
        return xPathFactory.newXPath();
    }

}
