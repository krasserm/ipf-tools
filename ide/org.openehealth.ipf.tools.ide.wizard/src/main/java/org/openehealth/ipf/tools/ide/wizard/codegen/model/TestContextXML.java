package org.openehealth.ipf.tools.ide.wizard.codegen.model;

import org.openehealth.ipf.tools.ide.wizard.plugin.*;

public class TestContextXML
{
  protected static String nl;
  public static synchronized TestContextXML create(String lineSeparator)
  {
    nl = lineSeparator;
    TestContextXML result = new TestContextXML();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "<beans xmlns=\"http://www.springframework.org/schema/beans\"" + NL + "       xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" + NL + "       xmlns:lang=\"http://www.springframework.org/schema/lang\"" + NL + "       xmlns:camel=\"http://camel.apache.org/schema/spring\"" + NL + "       xsi:schemaLocation=\"" + NL + "http://www.springframework.org/schema/beans " + NL + "http://www.springframework.org/schema/beans/spring-beans-2.5.xsd" + NL + "http://www.springframework.org/schema/lang " + NL + "http://www.springframework.org/schema/lang/spring-lang-2.5.xsd" + NL + "http://camel.apache.org/schema/spring " + NL + "http://camel.apache.org/schema/spring/camel-spring.xsd " + NL + "\">" + NL + "" + NL + "    <camel:camelContext id=\"camelContext\">" + NL + "        <camel:jmxAgent id=\"agent\" disabled=\"true\" />" + NL + "        <camel:routeBuilder ref=\"routeBuilder\"/>        " + NL + "    </camel:camelContext>" + NL + "" + NL + "    <bean id=\"routeBuilder\" depends-on=\"routeModelExtender\"" + NL + "        class=\"";
  protected final String TEXT_2 = ".";
  protected final String TEXT_3 = "\">" + NL + "    </bean>" + NL + "" + NL + "    <bean id=\"testModelExtension\" " + NL + "        class=\"";
  protected final String TEXT_4 = ".";
  protected final String TEXT_5 = "\">" + NL + "    </bean>" + NL + "" + NL + "    <bean id=\"coreModelExtension\" " + NL + "        class=\"org.openehealth.ipf.platform.camel.core.extend.CoreModelExtension\">" + NL + "    </bean>" + NL + "" + NL + "    <bean id=\"routeModelExtender\" " + NL + "        class=\"org.openehealth.ipf.platform.camel.core.extend.DefaultModelExtender\">" + NL + "        <property name=\"routeModelExtensions\">" + NL + "            <list>" + NL + "                <ref bean=\"coreModelExtension\" />" + NL + "                <ref bean=\"testModelExtension\" />" + NL + "            </list>" + NL + "        </property>" + NL + "    </bean>" + NL + "" + NL + "</beans>";
  protected final String TEXT_6 = NL;

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    PluginFieldData pluginData = (PluginFieldData)argument;
    stringBuffer.append(TEXT_1);
    stringBuffer.append(pluginData.getRouteBuilderPackage().replaceAll("\\/", "\\."));
    stringBuffer.append(TEXT_2);
    stringBuffer.append(pluginData.getRouteBuilderName().substring(0, pluginData.getRouteBuilderName().lastIndexOf(".")));
    stringBuffer.append(TEXT_3);
    stringBuffer.append(pluginData.getExtensionPackage().replaceAll("\\/", "\\."));
    stringBuffer.append(TEXT_4);
    stringBuffer.append(pluginData.getExtensionName().substring(0, pluginData.getExtensionName().lastIndexOf(".")));
    stringBuffer.append(TEXT_5);
    stringBuffer.append(TEXT_6);
    return stringBuffer.toString();
  }
}
