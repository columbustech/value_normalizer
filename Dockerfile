from ubuntu:18.04

RUN apt-get update && apt-get install -y nginx
COPY nginx.conf /etc/nginx/
COPY frontend.conf /etc/nginx/conf.d
COPY  normalizer-ui/dist/normalizer-ui/ /var/www/frontend/

RUN apt-get update -y && apt-get install -y default-jre default-jdk vim git curl wget

WORKDIR /maven
RUN wget http://ftp.wayne.edu/apache/maven/maven-3/3.6.1/binaries/apache-maven-3.6.1-bin.tar.gz
RUN tar -C /opt -xzf apache-maven-3.6.1-bin.tar.gz
RUN mv /opt/apache-maven-3.6.1 /opt/maven

ENV M2_HOME /opt/maven
ENV PATH $M2_HOME/bin:$PATH

WORKDIR /code
RUN git clone https://github.com/columbustech/value_normalizer.git

WORKDIR /code/value_normalizer/normalizer
RUN mvn clean install

CMD service nginx start && java -jar target/normalizer-0.0.1-SNAPSHOT.jar
