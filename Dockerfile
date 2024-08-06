FROM eclipse-temurin:20 AS build

LABEL maintainer="Daniel Giribet - dani [at] calidos [dot] cat"

# variables build stage
ARG MAVEN_URL=https://archive.apache.org/dist/maven/maven-3/3.9.8/binaries/apache-maven-3.9.8-bin.tar.gz
ARG MAVEN_HOME=/usr/share/maven
# set a maven repo URL and a matching repo name ('central' recommended)
ARG MAVEN_CENTRAL_MIRROR=none
ENV MAVEN_CENTRAL_MIRROR_=${MAVEN_CENTRAL_MIRROR}

# install dependencies (bash to launch angular build, ncurses for pretty output with tput, git for npm deps)
# and sed for the proxy configuration
# RUN apk add --no-cache curl bash ncurses git sed
# this installs node and npm (using the 'n' package manager)
RUN curl -fsSL https://raw.githubusercontent.com/tj/n/master/bin/n | bash -s lts
RUN npm install -g @angular/cli

# install maven
RUN mkdir -p ${MAVEN_HOME}
RUN curl ${MAVEN_URL} | tar zxf - -C ${MAVEN_HOME} --strip-components 1
RUN ln -s ${MAVEN_HOME}/bin/mvn /usr/bin/mvn

# we add the pom and validate the project (does nothing), but some of the downloads will be cached
# and this layer will not be built unless the pom is changed
COPY pom.xml pom.xml
COPY src/main/resources/maven/settings.xml /tmp/settings.xml
RUN if [ "${MAVEN_CENTRAL_MIRROR_}" != 'none' ]; then \
  sed -i "s^MAVEN_CENTRAL_MIRROR^${MAVEN_CENTRAL_MIRROR_}^" /tmp/settings.xml && \
  mkdir -v ${HOME}/.m2 &&  cp -v /tmp/settings.xml ${HOME}/.m2; \
  fi
  
RUN if [ "${MAVEN_CENTRAL_MIRROR_}" != 'none' ]; then \
  /usr/bin/mvn dependency:go-offline \
  else \
  /usr/bin/mvn -Daether.connector.basic.downstreamThreads=2
fi

# cache some node stuff to speed up builds
RUN mkdir /cache
COPY src/main/typescript/*.json /cache/
COPY src/main/typescript/*.js /cache/
# force is reuired until all node packages are stable dependency-wise
RUN cd /cache/ && npm install --force

# add code
COPY src src

# and build (in multiple steps steps to try to reuse the lengthy maven download)
RUN /usr/bin/mvn compile
# copy cached node modules before the package
RUN cp -r /cache/node_modules /src/main/typescript/node_modules
RUN /usr/bin/mvn test package
RUN echo 'build finished'


FROM eclipse-temurin:20 AS main

# arguments and variables run stage
ENV JETTY_HOME /var/lib/jetty
ENV JETTY_URL https://repo1.maven.org/maven2/org/eclipse/jetty/jetty-home/12.0.11/jetty-home-12.0.11.tar.gz
ARG JETTY_BASE=/jetty-base

# we will add the test resources prefix but in produciton you can put real data as http:// or file://
ARG TEST_RESOURCES_PREFIX=${JETTY_BASE}
ENV __RESOURCES_PREFIX=file://$TEST_RESOURCES_PREFIX/

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

# add test data (may or may not be accessible)
RUN mkdir -p ${TEST_RESOURCES_PREFIX}/target/test-classes/test-resources
COPY --from=build ./target/test-classes/test-resources ${TEST_RESOURCES_PREFIX}/target/test-classes/test-resources

# start jetty from its base folder (uncomment the scan interval when testing), this way of starting it means
# we do not do a fork of the java process to run jetty, and also means ENV vars (like __RESOURCES_PREFIX) will be
# received
WORKDIR ${JETTY_BASE}
ENTRYPOINT sh -c "$(java -jar ${JETTY_HOME}/start.jar jetty.deploy.scanInterval=1 --dry-run)"

