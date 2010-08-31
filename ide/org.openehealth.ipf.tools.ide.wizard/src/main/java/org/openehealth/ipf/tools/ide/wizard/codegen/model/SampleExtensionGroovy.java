package org.openehealth.ipf.tools.ide.wizard.codegen.model;

import org.openehealth.ipf.tools.ide.wizard.plugin.*;

public class SampleExtensionGroovy
{
  protected static String nl;
  public static synchronized SampleExtensionGroovy create(String lineSeparator)
  {
    nl = lineSeparator;
    SampleExtensionGroovy result = new SampleExtensionGroovy();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "package ";
  protected final String TEXT_2 = NL + NL + "public class ";
  protected final String TEXT_3 = " {" + NL + "" + NL + "     static extensions = {" + NL + "         " + NL + "     }" + NL + "    " + NL + "}";
  protected final String TEXT_4 = NL;

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    PluginFieldData pluginData = (PluginFieldData)argument;
    stringBuffer.append(TEXT_1);
    stringBuffer.append(pluginData.getExtensionPackage().replaceAll("\\/", "\\."));
    stringBuffer.append(TEXT_2);
    stringBuffer.append(pluginData.getExtensionName().substring(0, pluginData.getExtensionName().lastIndexOf(".")));
    stringBuffer.append(TEXT_3);
    stringBuffer.append(TEXT_4);
    return stringBuffer.toString();
  }
}
