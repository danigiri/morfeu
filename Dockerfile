FROM openjdk:13-alpine

ENV JETTY_URL https://repo1.maven.org/maven2/org/eclipse/jetty/jetty-distribution/9.4.24.v20191120/jetty-distribution-9.4.24.v20191120.tar.gz
ENV JETTY_HOME /var/lib/jetty 

# is this really needed?
MAINTAINER Daniel Giribet "dani [at] calidos [.] cat"

RUN apk add curl
RUN mkdir -p ${JETTY_HOME}
RUN curl ${JETTY_URL} | tar zxf - -C ${JETTY_HOME} --strip-components 1

# installing freetype, fontconfig and some fonts so we can generate the SVGs
RUN apk add freetype fontconfig ttf-ubuntu-font-family

ADD ./target/*.war ${JETTY_HOME}/webapps/root.war

WORKDIR ${JETTY_HOME}
#ENTRYPOINT java -jar ./start.jar --module=http jetty.http.port=8980 -D__RESOURCES_PREFIX=file://${JETTY_HOME}/
ENTRYPOINT java -jar ./start.jar --module=http jetty.http.port=8980 -D__RESOURCES_PREFIX=http://localhost:8980/

