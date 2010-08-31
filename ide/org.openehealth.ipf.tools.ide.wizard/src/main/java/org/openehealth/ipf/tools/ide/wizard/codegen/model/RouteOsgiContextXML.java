package org.openehealth.ipf.tools.ide.wizard.codegen.model;

import org.openehealth.ipf.tools.ide.wizard.plugin.*;

public class RouteOsgiContextXML
{
  protected static String nl;
  public static synchronized RouteOsgiContextXML create(String lineSeparator)
  {
    nl = lineSeparator;
    RouteOsgiContextXML result = new RouteOsgiContextXML();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + NL + "<beans xmlns=\"http://www.springframework.org/schema/beans\"" + NL + "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" + NL + "    xmlns:osgi=\"http://www.springframework.org/schema/osgi\"" + NL + "    xsi:schemaLocation=\"" + NL + "http://www.springframework.org/schema/beans" + NL + "http://www.springframework.org/schema/beans/spring-beans.xsd" + NL + "http://www.springframework.org/schema/osgi" + NL + "http://www.springframework.org/schema/osgi/spring-osgi.xsd\">" + NL;
  protected final String TEXT_2 = NL + "    <osgi:reference id=\"jms\" bean-name=\"jms\" timeout=\"10000\" " + NL + "        interface=\"org.apache.camel.Component\">" + NL + "    </osgi:reference>";
  protected final String TEXT_3 = NL + "    <osgi:reference id=\"flowManager\" timeout=\"10000\" " + NL + "        interface=\"org.openehealth.ipf.commons.flow.FlowManager\">" + NL + "    </osgi:reference>" + NL + "" + NL + "    <bean class=\"org.openehealth.ipf.platform.camel.flow.osgi.OsgiReplayStrategyRegistry\" />";
  protected final String TEXT_4 = NL + "</beans>";

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    PluginFieldData pluginData = (PluginFieldData)argument;
    stringBuffer.append(TEXT_1);
    if (pluginData.isUseJms()) {
    stringBuffer.append(TEXT_2);
    }
    if (pluginData.isUseFlowManagement()) {
    stringBuffer.append(TEXT_3);
    }
    stringBuffer.append(TEXT_4);
    return stringBuffer.toString();
  }
}
