# syntax=docker/dockerfile:1

FROM centos:centos7

RUN yum install java-11-openjdk-devel -y

RUN yum install python3 -y

RUN yum install python3-pip -y

WORKDIR /app

COPY src/challenge/.mvn/ .mvn
COPY src/challenge/mvnw src/challenge/pom.xml ./
RUN ./mvnw dependency:go-offline

COPY src/challenge/src ./src
COPY src/challengePython ./srcPython

COPY data/* .src/main/resources/input/

COPY deploy/run.sh .
RUN chmod +x run.sh 

RUN mkdir ./srcPython/output

RUN python3 -m pip install pandas

ENTRYPOINT ["/bin/bash", "run.sh"]
