FROM ubuntu:18.04

RUN apt-get update && apt-get install -y openjdk-8-jdk nano less git
RUN mkdir /morphcsv
RUN mkdir /configs
RUN mkdir /results
RUN mkdir /queries
COPY . /morphcsv

RUN cp /morphcsv/target/morph-csv-1.0-jar-with-dependencies.jar /morphcsv/morph-csv.jar


CMD ["tail", "-f", "/dev/null"]
