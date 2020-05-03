FROM openjdk:13-alpine AS build

LABEL maintainer="Daniel Giribet - dani [at] calidos [dot] cat"

# variables build stage
ARG MAVEN_URL=https://apache.brunneis.com/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz
ENV MAVEN_HOME /usr/share/maven

# install dependencies (bash to launch angular build, ncurses for pretty output with tput, git for npm deps)
RUN apk add --no-cache curl bash ncurses git
RUN apk add --no-cache --update nodejs npm
RUN npm install -g @angular/cli

# install maven
RUN mkdir -p ${MAVEN_HOME}
RUN curl ${MAVEN_URL} | tar zxf - -C ${MAVEN_HOME} --strip-components 1
RUN ln -s ${MAVEN_HOME}/bin/mvn /usr/bin/mvn

# we add the pom and validate the project (does nothing), but some of the downloads will be cached
COPY pom.xml pom.xml
RUN /usr/bin/mvn validate

# add code
COPY src src

# and build (two steps to reuse the lengthy maven download)
RUN /usr/bin/mvn compile
RUN /usr/bin/mvn test package


FROM openjdk:13-alpine AS main

# arguments and variables run stage
ARG VERSION=0.6.2-SNAPSHOT
ENV JETTY_HOME /var/lib/jetty
ENV RESOURCES_PREFIX=file://${JETTY_HOME}/
ENV PROXY_PREFIX=
ENV JETTY_URL https://repo1.maven.org/maven2/org/eclipse/jetty/jetty-distribution/9.4.24.v20191120/jetty-distribution-9.4.24.v20191120.tar.gz
ENV JETTY_BASE /jetty-base

RUN apk add --no-cache curl
RUN mkdir -p ${JETTY_HOME}
RUN curl ${JETTY_URL} | tar zxf - -C ${JETTY_HOME} --strip-components 1

# installing freetype, fontconfig and some fonts so we can generate the SVGs
RUN apk add --no-cache freetype fontconfig ttf-ubuntu-font-family

# create jetty-base folder and add the configuration
RUN mkdir -p ${JETTY_BASE}/webapps ${JETTY_BASE}/logs
COPY --from=build ./target/classes/jetty /jetty-base

# add war
COPY --from=build ./target/morfeu-webapp-${VERSION}.war ${JETTY_BASE}/webapps/root.war

# add test data
RUN mkdir -p ${JETTY_HOME}/target/test-classes/test-resources
COPY --from=build ./target/test-classes/test-resources ${JETTY_HOME}/target/test-classes/test-resources

# start
WORKDIR ${JETTY_HOME}
ENTRYPOINT java -jar ./start.jar jetty.base=${JETTY_BASE} \
	-D__RESOURCES_PREFIX=${RESOURCES_PREFIX} -D__PROXY_PREFIX=${PROXY_PREFIX}
