@echo off

set JMX_OPTS=
set JMX_OPTS=%JMX_OPTS% -Dcom.sun.management.jmxremote 
set JMX_OPTS=%JMX_OPTS% -Dcom.sun.management.jmxremote.port=8448
set JMX_OPTS=%JMX_OPTS% -Dcom.sun.management.jmxremote.authenticate=false
set JMX_OPTS=%JMX_OPTS% -Dcom.sun.management.jmxremote.ssl=false
  
set JVM_OPTS=
set JVM_OPTS=%JVM_OPTS% -Dosgi.classloader.lock=classname
  
"%JAVA_HOME%/bin/java" %JVM_OPTS% %JMX_OPTS% -jar bundles/org.eclipse.osgi_3.5.1.R35x_v20090827.jar -configuration equinox -console 
