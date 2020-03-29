package com.owen.study.aop.ioc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPath;

public class ClassPathXmlApplicationContext implements BeanFactory
{
	// 容器，用来存放注入的Bean
	private Map<String, Object> container = new HashMap<String, Object>();

	// 解析xml文件，通过反射将配置的bean放到container中
	public ClassPathXmlApplicationContext(String fileName) throws Exception
	{
		SAXBuilder sb = new SAXBuilder();
        
		Document doc = sb.build(ClassPathXmlApplicationContext.class
				.getResource("/" + fileName));

		Element root = doc.getRootElement();
		List<Element> list = (List<Element>) XPath.selectNodes(root, "/beans/bean");
		

		for (int i = 0; i < list.size(); i++)
		{
			Element bean = list.get(i);
			String id = bean.getAttributeValue("id");
			String clazz = bean.getAttributeValue("class");
			Object o = Class.forName(clazz).newInstance();
			container.put(id, o);
		}
	}

	@Override
	public Object getBean(String id)
	{
		return container.get(id);
	}

}