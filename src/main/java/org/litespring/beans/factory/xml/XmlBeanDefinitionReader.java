package org.litespring.beans.factory.xml;


import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.litespring.aop.config.ConfigBeanDefinitionParser;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.ConstructorArgument;
import org.litespring.beans.PropertyValue;
import org.litespring.beans.factory.BeanDefinitionStoreException;
import org.litespring.beans.factory.config.RuntimeBeanReference;
import org.litespring.beans.factory.config.TypedStringValue;
import org.litespring.beans.factory.support.BeanDefinitionRegistry;
import org.litespring.beans.factory.support.GenericBeanDefinition;
import org.litespring.context.annotation.ClassPathBeanDefinitionScanner;
import org.litespring.core.io.Resource;
import org.litespring.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;


public class XmlBeanDefinitionReader {

    private final Logger logger = Logger.getLogger(this.getClass());

    public static final String ID_ATTRIBUTE = "id";

    public static final String CLASS_ATTRIBUTE = "class";

    public static final String SCOPE_ATTRIBUTE = "scope";

    public static final String PROPERTY_ELEMENT = "property";

    public static final String REF_ATTRIBUTE = "ref";

    public static final String VALUE_ATTRIBUTE = "value";

    public static final String NAME_ATTRIBUTE = "name";

    public static final String CONSTRUCTOR_ARG_ELEMENT = "constructor-arg";

    public static final String TYPE_ATTRIBUTE = "type";

    public static final String BEANS_NAMESPACE_URI = "http://www.springframework.org/schema/beans";

    public static final String CONTEXT_NAMESPACE_URI = "http://www.springframework.org/schema/context";

    public static final String AOP_NAMESPACE_URI = "http://www.springframework.org/schema/aop";

    private static final String BASE_PACKAGE_ATTRIBUTE = "base-package";

    BeanDefinitionRegistry registry;

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    public XmlBeanDefinitionReader() {
    }

    public void loadBeanDefinitions(Resource resource) {
        InputStream is = null;
        try {
            is = resource.getInputStream();
            SAXReader reader = new SAXReader();
            Document doc = reader.read(is);
            Element rootElement = doc.getRootElement();
            Iterator iter = rootElement.elementIterator();
            while (iter.hasNext()) {
                Element element = (Element) iter.next();
                String namespaceURI = element.getNamespaceURI();
                if (this.isDefaultNamespace(namespaceURI)) {
                    parseDefaultElement(element);
                } else if (this.isContextNamespace(namespaceURI)) {
                    parseComponentElement(element);
                } else if (this.isAOPNamespace(namespaceURI)) {
                    parseAOPElement(element);
                }

            }
        } catch (Exception e) {
            throw new BeanDefinitionStoreException("IOException parsing XML document from " + resource.getDescription(), e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void parseComponentElement(Element element) {
        String basePackages = element.attributeValue(BASE_PACKAGE_ATTRIBUTE);
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry);
        scanner.doScan(basePackages);

    }

    private void parseDefaultElement(Element element) {
        String id = element.attributeValue(ID_ATTRIBUTE);
        String beanClassName = element.attributeValue(CLASS_ATTRIBUTE);
        BeanDefinition bd = new GenericBeanDefinition(id, beanClassName);
        if (element.attribute(SCOPE_ATTRIBUTE) != null) {
            bd.setScope(element.attributeValue(SCOPE_ATTRIBUTE));
        }
        parsePropertyElement(element, bd);
        parseConstructorArgElements(element, bd);
        this.registry.registerBeanDefinition(id, bd);
    }

    public void parseConstructorArgElements(Element element, BeanDefinition bd) {
        Iterator iter = element.elementIterator(CONSTRUCTOR_ARG_ELEMENT);
        while (iter.hasNext()) {
            Element ele = (Element) iter.next();
            parseConstructorArgElement(ele, bd);
        }
    }

    public void parsePropertyElement(Element element, BeanDefinition bd) {
        Iterator iter = element.elementIterator(PROPERTY_ELEMENT);
        while (iter.hasNext()) {
            Element propElem = (Element) iter.next();
            String propertyName = propElem.attributeValue(NAME_ATTRIBUTE);
            if (!StringUtils.hasLength(propertyName)) {
                logger.fatal("Tag 'property' must have a 'name' attribute");
                return;
            }
            Object val = parsePropertyValue(propElem, propertyName);
            PropertyValue propertyValue = new PropertyValue(propertyName, val);
            bd.getPropertyValues().add(propertyValue);
        }
    }

    public void parseConstructorArgElement(Element ele, BeanDefinition bd) {
        String typeAttr = ele.attributeValue(TYPE_ATTRIBUTE);
        String nameAttr = ele.attributeValue(NAME_ATTRIBUTE);
        Object value = parsePropertyValue(ele, null);
        ConstructorArgument.ValueHolder valueHolder = new ConstructorArgument.ValueHolder(value);
        if (StringUtils.hasLength(typeAttr)) {
            valueHolder.setType(typeAttr);
        } else if (StringUtils.hasLength(nameAttr)) {
            valueHolder.setName(nameAttr);
        }
        bd.getConstructorArgument().addArgumentValue(valueHolder);

    }

    public Object parsePropertyValue(Element ele, String propertyName) {
        String elementName = (propertyName != null) ?
                "<property> element for property '" + propertyName + "'" :
                "<constructor-arg> element";

        boolean hasRefAttribute = (ele.attributeValue(REF_ATTRIBUTE) != null);
        boolean hasValueAttribute = (ele.attributeValue(VALUE_ATTRIBUTE) != null);

        if (hasRefAttribute) {
            String refName = ele.attributeValue(REF_ATTRIBUTE);
            if (!StringUtils.hasLength(refName)) {
                logger.error(elementName + " contains empty 'ref' attribute");
            }
            RuntimeBeanReference reference = new RuntimeBeanReference(refName);
            return reference;
        } else if (hasValueAttribute) {
            String valueName = ele.attributeValue(VALUE_ATTRIBUTE);
            TypedStringValue typedStringValue = new TypedStringValue(valueName);
            return typedStringValue;
        } else {
            throw new RuntimeException(elementName + " must specify a ref or value");
        }
    }

    private void parseAOPElement(Element ele){
        ConfigBeanDefinitionParser parser = new ConfigBeanDefinitionParser();
        parser.parse(ele, this.registry);
    }

    public boolean isDefaultNamespace(String namespaceUri) {
        return (!StringUtils.hasLength(namespaceUri) || BEANS_NAMESPACE_URI.equals(namespaceUri));
    }

    public boolean isContextNamespace(String namespaceUri) {
        return (!StringUtils.hasLength(namespaceUri) || CONTEXT_NAMESPACE_URI.equals(namespaceUri));
    }

    public boolean isAOPNamespace(String namespaceUri) {
        return (!StringUtils.hasLength(namespaceUri) || AOP_NAMESPACE_URI.equals(namespaceUri));
    }
}
