package org.openehealth.ipf.tools.ide.wizard.codegen.model;

public class GroovyPrefs
{
  protected static String nl;
  public static synchronized GroovyPrefs create(String lineSeparator)
  {
    nl = lineSeparator;
    GroovyPrefs result = new GroovyPrefs();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "#";
  protected final String TEXT_2 = NL + "eclipse.preferences.version=1" + NL + "groovy.compiler.output.path=bin-groovy";
  protected final String TEXT_3 = NL;

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(TEXT_1);
    stringBuffer.append(new java.text.SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy").format(new java.util.Date()));
    stringBuffer.append(TEXT_2);
    stringBuffer.append(TEXT_3);
    return stringBuffer.toString();
  }
}
