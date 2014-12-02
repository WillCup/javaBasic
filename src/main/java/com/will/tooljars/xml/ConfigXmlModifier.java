package com.will.tooljars.xml;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPath;

/**
 * When we got a new config infomation, check the part to be modified, generate
 * a new xml.
 * 
 * <br/>
 * <br/>
 * 
 * @author will
 * 
 * @2014-7-19
 */
public class ConfigXmlModifier {
	private static SAXBuilder saxBuilder;
	private static XMLOutputter outputter;
	private static Document doc;
	static String CONFIG = "src/test.xml";
	static String RESULT = "src/test_result.xml";
	
	private static String installPath;
	
	public static void setInstallPath(String installDir) {
		installPath = installDir;
	}

	/**
	 * Modify the target's text value.
	 * 
	 * <br/>
	 * 
	 * @param xpath
	 * @param target
	 * @param newVal
	 * @return
	 * @throws JDOMException
	 */
	public static void modifySingleNode(String xpath, Element target,
			String newVal) throws JDOMException {
		List<Element> targetNodes = null;
		try {
			SAXBuilder saxBuilder = new SAXBuilder();
			Document doc = saxBuilder.build(getFile());
			Element rootElement = doc.getRootElement();
			
			if (target == null) {
				target = rootElement;
			}
			
			targetNodes = (List<Element>) XPath.selectNodes(target, xpath);
			if (targetNodes.size() > 0) {
				System.out.println("-----------------size > 0 ------------------");
				targetNodes.get(0).setText(newVal);
			} else {
				System.out.println("-----------------size = 0 ------------------");
				return;
			}
			getXmlOutputter().output(doc,
					new FileOutputStream(getFile()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
//		return targetNodes;
	}

	/**
	 * Add service-item like mysql, apache...
	 * 
	 * <br/>
	 * 
	 * @param services
	 * @param xpath
	 * @throws JDOMException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void addServiceItems(Map services, String xpath)
			throws JDOMException, FileNotFoundException, IOException {
		SAXBuilder saxBuilder = new SAXBuilder();
		Document doc = saxBuilder.build(getFile());
		Element rootElement = doc.getRootElement();
		System.err.println(xpath);
		System.err.println(services);
		List<Element> selectNodes = (List<Element>) XPath.selectNodes(
				rootElement, xpath);
		for (Element serviceGroupEle : selectNodes) {
			System.out.println(serviceGroupEle.getName());
			System.out.println(serviceGroupEle.getName());
			Element serviceItemEle = new Element("service-item");
			Iterator<Entry> iterator = services.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry next = iterator.next();
				Element ele = new Element((String) next.getKey());
				Object value = next.getValue();
				if (value instanceof Map) {
					Iterator<Entry> innerIte = ((Map) value).entrySet()
							.iterator();
					while (innerIte.hasNext()) {
						Entry innerNext = innerIte.next();
						Element innerEle = new Element(String.valueOf(innerNext
								.getKey()));
						innerEle.setText(String.valueOf(innerNext.getValue()));
						ele.addContent(innerEle);
					}
				} else {
					ele.setText(String.valueOf(value));
				}
				serviceItemEle.addContent(ele);
			}
			serviceGroupEle.addContent(serviceItemEle);
		}
		getXmlOutputter().output(doc,
				new FileOutputStream(getFile()));
	}

	/**
	 * Delete a service-item element by a given id and xpath.
	 * 
	 * <br/>
	 * 
	 * @param id
	 * @param xPath
	 * @throws JDOMException
	 * @throws IOException
	 */
	public static void deleteServiceItem(String id, String xPath)
			throws JDOMException, IOException {
		SAXBuilder saxBuilder = new SAXBuilder();
		Document doc = saxBuilder.build(getFile());
		Element rootElement = doc.getRootElement();
		System.out.println(id + "   id in delete");
		System.out.println(xPath + "    xpath in delete");
		List<Element> serviceItemEle = (List<Element>) XPath.selectNodes(
				rootElement, xPath);
		for (Element e : serviceItemEle) {
			System.out.println(e.getName());
			System.out.println(e.getChild("id").getTextTrim());
			if (e.getChild("id").getTextTrim().equals(id)) {
				System.out.println(" to be deleted........");
				e.getParent().removeContent(e);
				break;
			}
		}
		getXmlOutputter().output(doc,
				new FileOutputStream(RESULT));
	}

	/**
	 * Update a service item's info. Just delete old one and create a new one.
	 * 
	 * <br/>
	 * 
	 * @param serviceItem
	 * @param xPath
	 * @throws JDOMException
	 * @throws IOException
	 */
	public static void updateServiceItem(Map serviceItem, String xPath)
			throws JDOMException, IOException {
		deleteServiceItem(serviceItem.get("id").toString(), xPath+"/service-item");
		System.out.println("--------------");
		addServiceItems(serviceItem, xPath);
	}

	static public void main(String ars[]) throws JDOMException, IOException {
//		SAXBuilder saxBuilder = new SAXBuilder();
//		Document doc = saxBuilder.build(getFile());
//		System.out.println(doc.getContent());
//		System.out.println(doc.toString());
//		Map configs = new HashMap();
//		configs.put("user", "will");
//		configs.put("password", "ab1");
//		configs.put(
//				"uri",
//				"jdbc:mysql://10.0.1.137:3306/mysql?&amp;useUnicode=true&amp;characterEncoding=UTF-8");
//		configs.put("slow-threshold", "100");
//		Map serviceItem = new HashMap();
//		serviceItem.put("active", "y");
//		serviceItem.put("freq", 5);
//		serviceItem.put("id", "dsgfdgdfgddfsfds324324");
//		serviceItem.put("probability", "100");
//		serviceItem.put("configs", configs);
		String xpath = "service[name='mysql']/service-group";
//
//		addServiceItems(serviceItem, xpath);// add
//
		String deleteXPath = xpath + "/service-item";
		String id = "sdfsdfdsf";
//		System.out.println(id);
		 deleteServiceItem(id, deleteXPath);
		
	}

/*	private static SAXBuilder getSAXBuilder() {
		if (saxBuilder == null) {
			saxBuilder = new SAXBuilder();
		}
		return saxBuilder;
	}

	private static Document getDocument() throws JDOMException, IOException {
		return getSAXBuilder().build(getFile());
	}*/

	private static String getFile() {
		return CONFIG;
	}

	/*private static Element getRootElement() throws JDOMException, IOException {
		return getDocument().getRootElement();
	}*/

	private static XMLOutputter getXmlOutputter() {
		if (outputter == null) {
			outputter = new XMLOutputter();
		}
		return outputter;
	}
}