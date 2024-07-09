FROM eclipse-temurin:20 AS build

LABEL maintainer="Daniel Giribet - dani [at] calidos [dot] cat"

# variables build stage
ARG MAVEN_URL=https://archive.apache.org/dist/maven/maven-3/3.9.8/binaries/apache-maven-3.9.8-bin.tar.gz
ARG MAVEN_HOME=/usr/share/maven

# install dependencies (bash to launch angular build, ncurses for pretty output with tput, git for npm deps)
# and sed for the proxy configuration
# RUN apk add --no-cache curl bash ncurses git sed
# RUN apk add --no-cache --update nodejs npm
RUN apt-get update
RUN apt-get install nodejs npm -y
RUN npm install -g @angular/cli

# install maven
RUN mkdir -p ${MAVEN_HOME}
RUN curl ${MAVEN_URL} | tar zxf - -C ${MAVEN_HOME} --strip-components 1
RUN ln -s ${MAVEN_HOME}/bin/mvn /usr/bin/mvn

# we add the pom and validate the project (does nothing), but some of the downloads will be cached
# and this layer will not be built unless the pom is changed
COPY pom.xml pom.xml
RUN /usr/bin/mvn dependency:go-offline

# cache some node stuff to speed up builds
RUN mkdir /cache
COPY src/main/typescript/*.json /cache/
COPY src/main/typescript/*.js /cache/
RUN cd /cache/ && npm install

# add code
COPY src src

# and build (in multiple steps steps to try to reuse the lengthy maven download)
RUN /usr/bin/mvn compile
# copy cached node modules before the package
RUN cp -r /cache/node_modules /src/main/typescript/node_modules
RUN /usr/bin/mvn test package
RUN echo 'build finished'


FROM eclipse-temurin:20 AS main
# dFROM openjdk:13-alpine AS main

# arguments and variables run stage
ENV JETTY_HOME /var/lib/jetty
ENV JETTY_URL https://repo1.maven.org/maven2/org/eclipse/jetty/jetty-home/12.0.11/jetty-home-12.0.11.tar.gz
ARG JETTY_BASE=/jetty-base

# RUN apk add --no-cache curl
RUN mkdir -p ${JETTY_HOME}
RUN curl ${JETTY_URL} | tar zxf - -C ${JETTY_HOME} --strip-components 1

# installing freetype, fontconfig and some fonts so we can generate the SVGs, bash for the entrypoing
#RUN apk add --no-cache freetype fontconfig ttf-ubuntu-font-family bash
RUN apt-get install fontconfig

# create jetty-base folder and add the jetty configuration and folder structure
COPY --from=build ./target/classes/jetty ${JETTY_BASE}
RUN mkdir -p ${JETTY_BASE}/webapps ${JETTY_BASE}/resources ${JETTY_BASE}/lib/ext
COPY --from=build ./target/classes/jetty-logging.properties /${JETTY_BASE}/resources
# uncomment to create logs folder if we want to persist them (also enable the module, renaming it from .disabled)
# RUN mkdir -p ${JETTY_BASE}/logs

# add war
COPY --from=build ./target/morfeu-webapp-*.war ${JETTY_BASE}/webapps/root.war

# add test data
RUN mkdir -p ${JETTY_HOME}/target/test-classes/test-resources
COPY --from=build ./target/test-classes/test-resources ${JETTY_HOME}/target/test-classes/test-resources

# start (configuration seems not to be loading
WORKDIR ${JETTY_BASE}
ENTRYPOINT ["java", "-jar", "${JETTY_HOME}/start.jar"]
