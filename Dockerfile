FROM jetty:9.4.18-alpine

# is this really needed?
# EXPOSE 8080
MAINTAINER Daniel Giribet "dani [at] calidos [.] cat"
ADD ./target/*.war /var/lib/jetty/webapps/root.war
RUN java -jar "$JETTY_HOME/start.jar"
