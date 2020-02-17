FROM openjdk:13-alpine

MAINTAINER Daniel Giribet "dani [at] calidos [.] cat"

# variables
ENV JETTY_URL https://repo1.maven.org/maven2/org/eclipse/jetty/jetty-distribution/9.4.24.v20191120/jetty-distribution-9.4.24.v20191120.tar.gz
ENV JETTY_HOME /var/lib/jetty 

RUN apk add --no-cache curl
RUN mkdir -p ${JETTY_HOME}
RUN curl ${JETTY_URL} | tar zxf - -C ${JETTY_HOME} --strip-components 1

# installing freetype, fontconfig and some fonts so we can generate the SVGs
RUN apk add --no-cache freetype fontconfig ttf-ubuntu-font-family

# add war
ADD ./target/*.war ${JETTY_HOME}/webapps/root.war

# add test data
RUN mkdir -p ${JETTY_HOME}/target/test-classes/test-resources
ADD ./target/test-classes/test-resources ${JETTY_HOME}/target/test-classes/test-resources

WORKDIR ${JETTY_HOME}
#ENTRYPOINT java -jar ./start.jar --module=http jetty.http.port=8980 -D__RESOURCES_PREFIX=file://${JETTY_HOME}/
#ENTRYPOINT java -jar ./start.jar  -D__RESOURCES_PREFIX=file://${JETTY_HOME}/
ENTRYPOINT java -jar ./start.jar --module=http jetty.http.port=8980 -D__RESOURCES_PREFIX=file://${JETTY_HOME}/

