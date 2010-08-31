package org.openehealth.ipf.tools.ide.wizard.codegen.model;

import org.openehealth.ipf.tools.ide.wizard.plugin.*;

public class RouteContextXML
{
  protected static String nl;
  public static synchronized RouteContextXML create(String lineSeparator)
  {
    nl = lineSeparator;
    RouteContextXML result = new RouteContextXML();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = NL + "<beans xmlns=\"http://www.springframework.org/schema/beans\"" + NL + "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" + NL + "    xmlns:camel-osgi=\"http://camel.apache.org/schema/osgi\" " + NL + "    xmlns:camel-spring=\"http://camel.apache.org/schema/spring\" " + NL + "    xmlns:context=\"http://www.springframework.org/schema/context\"" + NL + "    xsi:schemaLocation=\"" + NL + "http://www.springframework.org/schema/beans " + NL + "http://www.springframework.org/schema/beans/spring-beans-2.5.xsd" + NL + "http://camel.apache.org/schema/osgi " + NL + "http://camel.apache.org/schema/osgi/camel-osgi.xsd" + NL + "http://camel.apache.org/schema/spring " + NL + "http://camel.apache.org/schema/spring/camel-spring.xsd" + NL + "http://www.springframework.org/schema/context" + NL + "http://www.springframework.org/schema/context/spring-context-2.5.xsd\">" + NL + "" + NL + "    <context:annotation-config />" + NL + "    " + NL + "    <camel-osgi:camelContext id=\"camelContext\" >" + NL + "\t\t<camel-spring:routeBuilder ref=\"routeBuilder\"/>" + NL + "\t</camel-osgi:camelContext >" + NL + "" + NL + "    <bean id=\"routeBuilder\" " + NL + "        class=\"";
  protected final String TEXT_2 = ".";
  protected final String TEXT_3 = "\">" + NL + "    </bean>" + NL + "</beans>";

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    PluginFieldData pluginData = (PluginFieldData)argument;
    stringBuffer.append(TEXT_1);
    stringBuffer.append(pluginData.getRouteBuilderPackage().replaceAll("\\/", "\\."));
    stringBuffer.append(TEXT_2);
    stringBuffer.append(pluginData.getRouteBuilderName().substring(0, pluginData.getRouteBuilderName().lastIndexOf(".")));
    stringBuffer.append(TEXT_3);
    return stringBuffer.toString();
  }
}
