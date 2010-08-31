#!/bin/sh

JMX_OPTS=
JMX_OPTS="${JMX_OPTS} -Dcom.sun.management.jmxremote"
JMX_OPTS="${JMX_OPTS} -Dcom.sun.management.jmxremote.port=8448"
JMX_OPTS="${JMX_OPTS} -Dcom.sun.management.jmxremote.authenticate=false"
JMX_OPTS="${JMX_OPTS} -Dcom.sun.management.jmxremote.ssl=false"

JVM_OPTS=
JVM_OPTS="${JVM_OPTS} -Dosgi.classloader.lock=classname"

exec $JAVA_HOME/bin/java $JVM_OPTS $JMX_OPTS -jar bundles/org.eclipse.osgi_3.5.1.R35x_v20090827.jar -configuration equinox -console 
