package org.openehealth.ipf.tools.ide.wizard.codegen.model;

import org.openehealth.ipf.tools.ide.wizard.plugin.*;

public class SampleRouteBuilderGroovy
{
  protected static String nl;
  public static synchronized SampleRouteBuilderGroovy create(String lineSeparator)
  {
    nl = lineSeparator;
    SampleRouteBuilderGroovy result = new SampleRouteBuilderGroovy();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "package ";
  protected final String TEXT_2 = NL + NL + "import org.apache.camel.spring.SpringRouteBuilder" + NL + "" + NL + "" + NL + "public class ";
  protected final String TEXT_3 = " extends SpringRouteBuilder {" + NL + "" + NL + "     void configure() {" + NL + "" + NL + "     }" + NL + "    " + NL + "}";
  protected final String TEXT_4 = NL;

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    PluginFieldData pluginData = (PluginFieldData)argument;
    stringBuffer.append(TEXT_1);
    stringBuffer.append(pluginData.getRouteBuilderPackage().replaceAll("\\/", "\\."));
    stringBuffer.append(TEXT_2);
    stringBuffer.append(pluginData.getRouteBuilderName().substring(0, pluginData.getRouteBuilderName().lastIndexOf(".")));
    stringBuffer.append(TEXT_3);
    stringBuffer.append(TEXT_4);
    return stringBuffer.toString();
  }
}
