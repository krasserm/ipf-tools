package org.openehealth.ipf.tools.ide.wizard.codegen.model;

import org.openehealth.ipf.tools.ide.wizard.plugin.*;

public class SampleRouteTestJava
{
  protected static String nl;
  public static synchronized SampleRouteTestJava create(String lineSeparator)
  {
    nl = lineSeparator;
    SampleRouteTestJava result = new SampleRouteTestJava();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "package ";
  protected final String TEXT_2 = ";" + NL + "" + NL + "import static org.junit.Assert.*;" + NL + "" + NL + "import org.apache.camel.ProducerTemplate;" + NL + "import org.junit.After;" + NL + "import org.junit.Before;" + NL + "import org.junit.Test;" + NL + "import org.junit.runner.RunWith;" + NL + "import org.springframework.beans.factory.annotation.Autowired;" + NL + "import org.springframework.test.context.ContextConfiguration;" + NL + "import org.springframework.test.context.TestExecutionListeners;" + NL + "import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;" + NL + "import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;" + NL + "" + NL + "@RunWith(SpringJUnit4ClassRunner.class)" + NL + "@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})" + NL + "@ContextConfiguration(locations = { \"/test-context.xml\" })" + NL + "public class ";
  protected final String TEXT_3 = " {" + NL + "" + NL + "    @Autowired" + NL + "    private ProducerTemplate producerTemplate;" + NL + "    " + NL + "    @Before" + NL + "    public void setUp() throws Exception {" + NL + "    }" + NL + "" + NL + "    @After" + NL + "    public void tearDown() throws Exception {" + NL + "    }" + NL + "\t" + NL + "\t@Test" + NL + "\tpublic void routeTest() throws Exception {" + NL + "\t}" + NL + "}";
  protected final String TEXT_4 = NL;

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    PluginFieldData pluginData = (PluginFieldData)argument;
    stringBuffer.append(TEXT_1);
    stringBuffer.append(pluginData.getRouteBuilderPackage().replaceAll("\\/", "\\."));
    stringBuffer.append(TEXT_2);
    stringBuffer.append(pluginData.getTestRouteName().substring(0, pluginData.getTestRouteName().lastIndexOf(".java")));
    stringBuffer.append(TEXT_3);
    stringBuffer.append(TEXT_4);
    return stringBuffer.toString();
  }
}
