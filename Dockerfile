FROM openjdk:13-alpine AS build

LABEL maintainer="Daniel Giribet - dani [at] calidos [dot] cat"

# variables build stage
ARG MAVEN_URL=https://downloads.apache.org/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz
ARG MAVEN_HOME=/usr/share/maven

# install dependencies (bash to launch angular build, ncurses for pretty output with tput, git for npm deps)
# and sed for the proxy configuration
RUN apk add --no-cache curl bash ncurses git sed
RUN apk add --no-cache --update nodejs npm
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
COPY src/main/angular/*.json /cache/
COPY src/main/angular/*.js /cache/
RUN cd /cache/ && npm install

# add code
COPY src src

# and build (in multiple steps steps to try to reuse the lengthy maven download)
RUN /usr/bin/mvn compile
# copy cached node modules before the package
RUN cp -r /cache/node_modules /src/main/angular/node_modules
RUN /usr/bin/mvn test package
RUN echo 'build finished'


FROM openjdk:13-alpine AS main

# arguments and variables run stage
ENV JETTY_HOME /var/lib/jetty
ENV JETTY_URL https://repo1.maven.org/maven2/org/eclipse/jetty/jetty-distribution/10.0.0.beta1/jetty-distribution-10.0.0.beta1.tar.gz
ARG JETTY_BASE=/jetty-base

RUN apk add --no-cache curl
RUN mkdir -p ${JETTY_HOME}
RUN curl ${JETTY_URL} | tar zxf - -C ${JETTY_HOME} --strip-components 1

# installing freetype, fontconfig and some fonts so we can generate the SVGs, bash for the entrypoing
RUN apk add --no-cache freetype fontconfig ttf-ubuntu-font-family bash

# create jetty-base folder and add the configuration
RUN mkdir -p ${JETTY_BASE}/webapps ${JETTY_BASE}/logs
COPY --from=build ./target/classes/jetty /jetty-base

# add war
COPY --from=build ./target/morfeu-webapp-*.war ${JETTY_BASE}/webapps/root.war

# add test data
RUN mkdir -p ${JETTY_HOME}/target/test-classes/test-resources
COPY --from=build ./target/test-classes/test-resources ${JETTY_HOME}/target/test-classes/test-resources

# start (configuration seems not to be loading
WORKDIR ${JETTY_HOME}
ENTRYPOINT ["java", "-jar", "./start.jar", "jetty.base=/jetty-base", "--module=http", "jetty.http.port=8980"]
