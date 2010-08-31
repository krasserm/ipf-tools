package org.openehealth.ipf.tools.ide.wizard.codegen.model;

import java.util.*;
import org.openehealth.ipf.tools.ide.wizard.plugin.*;

public class ManifestMF
{
  protected static String nl;
  public static synchronized ManifestMF create(String lineSeparator)
  {
    nl = lineSeparator;
    ManifestMF result = new ManifestMF();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "Manifest-Version: 1.0" + NL + "Bundle-ManifestVersion: 2" + NL + "Bundle-Name: ";
  protected final String TEXT_2 = NL + "Bundle-SymbolicName: ";
  protected final String TEXT_3 = NL + "Bundle-Version: ";
  protected final String TEXT_4 = NL + "Bundle-Vendor: ";
  protected final String TEXT_5 = NL + "Build-Jdk: ";
  protected final String TEXT_6 = NL + "Created-By: ";
  protected final String TEXT_7 = " (";
  protected final String TEXT_8 = ")";
  protected final String TEXT_9 = NL + "Extension-Classes: ";
  protected final String TEXT_10 = ".";
  protected final String TEXT_11 = NL + "Export-Package: ";
  protected final String TEXT_12 = ",";
  protected final String TEXT_13 = NL + " ";
  protected final String TEXT_14 = NL + "Import-Package: ";
  protected final String TEXT_15 = ",";
  protected final String TEXT_16 = NL + " ";
  protected final String TEXT_17 = NL;

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    PluginFieldData pluginData = (PluginFieldData)argument;
    stringBuffer.append(TEXT_1);
    stringBuffer.append(pluginData.getName());
    stringBuffer.append(TEXT_2);
    stringBuffer.append(pluginData.getId());
    stringBuffer.append(TEXT_3);
    stringBuffer.append(pluginData.getVersion());
    stringBuffer.append(TEXT_4);
    stringBuffer.append(pluginData.getProvider());
    stringBuffer.append(TEXT_5);
    stringBuffer.append(System.getProperty("java.runtime.version"));
    stringBuffer.append(TEXT_6);
    stringBuffer.append(System.getProperty("java.runtime.version"));
    stringBuffer.append(TEXT_7);
    stringBuffer.append(System.getProperty("java.vm.vendor"));
    stringBuffer.append(TEXT_8);
    if (pluginData.isUseExtension()){ 
    stringBuffer.append(TEXT_9);
    stringBuffer.append(pluginData.getExtensionPackage().replaceAll("\\/", "\\."));
    stringBuffer.append(TEXT_10);
    stringBuffer.append(pluginData.getExtensionName().substring(0, pluginData.getExtensionName().lastIndexOf(".")));
    }
    Iterator<String> exportPackagesIterator = pluginData.getExportPackageNames().iterator(); if (exportPackagesIterator.hasNext()) { String pack = exportPackagesIterator.next();
    stringBuffer.append(TEXT_11);
    stringBuffer.append(pack);
    while(exportPackagesIterator.hasNext()) { pack = exportPackagesIterator.next();
    stringBuffer.append(TEXT_12);
    stringBuffer.append(TEXT_13);
    stringBuffer.append(pack);
    }
    }
    Iterator<String> importPackagesIterator = pluginData.getImportPackageNames().iterator(); if (importPackagesIterator.hasNext()) { String pack = importPackagesIterator.next();
    stringBuffer.append(TEXT_14);
    stringBuffer.append(pack);
    while(importPackagesIterator.hasNext()) { pack = importPackagesIterator.next();
    stringBuffer.append(TEXT_15);
    stringBuffer.append(TEXT_16);
    stringBuffer.append(pack);
    }
    }
    stringBuffer.append(TEXT_17);
    return stringBuffer.toString();
  }
}
