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
 * When we got a new config infomation, check the part to be modified, generate a new xml.
 * 
 * <br/><br/>
 * @author will
 *
 * @2014-7-19
 */
public class ConfigXmlModifier {
	private static SAXBuilder saxBuilder;
	private static XMLOutputter outputter;
	private static Document doc;
	static String CONFIG = "src/test.xml";
	static String TARGET_FILE = "src/test_new.xml";

	/**
	 * Modify the target's text value.
	 *
	 * <br/>
	 * @param xpath
	 * @param target
	 * @param newVal
	 * @return
	 * @throws JDOMException
	 */
	public static Element modifySingleNode(String xpath, Element target, String newVal) throws JDOMException {
		Element targetNodes = (Element) XPath.selectSingleNode(target, xpath);
		targetNodes.setText(newVal);
		return targetNodes;
	}
	
	/**
	 * Add service-item like mysql, apache...
	 *
	 * <br/>
	 * @param services
	 * @param xpath
	 * @throws JDOMException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void addServiceItems(Map services, String xpath)
			throws JDOMException, FileNotFoundException, IOException {
		List<Element> selectNodes = (List<Element>) XPath.selectNodes(
				getRootElement(), xpath);
		for (Element serviceGroupEle : selectNodes) {
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
		getXmlOutputter().output(getDocument(),
				new FileOutputStream(TARGET_FILE));
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
		List<Element> serviceGroupEle = (List<Element>) XPath.selectNodes(
				getRootElement(), xPath);
		for (Element e : serviceGroupEle) {
			System.out.println(e.getName());
			System.out.println(e.getChild("id").getTextTrim());
			if (e.getChild("id").getTextTrim().equals(id)) {
				e.getParent().removeContent(e);
				break;
			}
		}
		getXmlOutputter().output(getDocument(),
				new FileOutputStream(TARGET_FILE));
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
		deleteServiceItem(serviceItem.get("id").toString(), xPath);
		addServiceItems(serviceItem, xPath);
	}

	static public void main(String ars[]) throws JDOMException, IOException {
		Map configs = new HashMap();
		configs.put("user", "will");
		configs.put("password", "ab1");
		configs.put(
				"uri",
				"jdbc:mysql://10.0.1.137:3306/mysql?&amp;useUnicode=true&amp;characterEncoding=UTF-8");
		configs.put("slow-threshold", "100");
		Map serviceItem = new HashMap();
		serviceItem.put("active", "y");
		serviceItem.put("freq", 5);
		serviceItem.put("id", "dsgfdgdfgddfsfds324324");
		serviceItem.put("probability", "100");
		serviceItem.put("configs", configs);
		String xpath = "service[name='mysql']/service-group";

//		addServiceItems(serviceItem, xpath);// add

		String deleteXPath = xpath + "/service-item";
		String id = "sdfsdfdsf";
		System.out.println(id);
		deleteServiceItem(id, deleteXPath);

	}

	private static SAXBuilder getSAXBuilder() {
		if (saxBuilder == null) {
			saxBuilder = new SAXBuilder();
		}
		return saxBuilder;
	}

	private static Document getDocument() throws JDOMException, IOException {
		return getSAXBuilder().build(getFile());
	}

	private static String getFile() {
		return CONFIG;
	}

	private static Element getRootElement() throws JDOMException, IOException {
		return getDocument().getRootElement();
	}

	private static XMLOutputter getXmlOutputter() {
		if (outputter == null) {
			outputter = new XMLOutputter();
		}
		return outputter;
	}
}