package com.will.tooljars.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.FileUtils;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class AddXMLContent {
	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException, JDOMException {
//		jdk();
		ConfigXmlModifier.modifySingleNode("target-id", null, "33");
	}

	private static void jdk() throws IOException, ParserConfigurationException,
			SAXException, XPathExpressionException {
		String content = FileUtils.readFileToString(new File("src/test.xml"), "utf-8");
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();
		Document document = docBuilder.parse(new InputSource(new StringReader(content)));
		
		String xpathStr = "host-agent/service[name='base']/service-group";
		
		XPathFactory xPathFactory = XPathFactory.newInstance();
		XPath xpath = xPathFactory.newXPath();
		
		XPathExpression expression = xpath.compile(xpathStr);
		Node node = (Node)expression.evaluate(document, XPathConstants.NODE);
		Element ele = new Element("test");
		ele.addContent("fromwill");
		System.out.println(node.getTextContent());
		System.out.println(node.getNodeName());
		System.out.println(node.getTextContent());
//		System.out.println(document.getElementsByTagName("will").item(0).getNodeName());
//		System.out.println(content);
	}
}
