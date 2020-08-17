FROM gradle

RUN apt update -y

RUN apt install -y python3 python3-pip && pip3 install kubernetes

RUN mkdir -p githubRepo/StubbornJava

COPY ./hack/*.py ./githubRepo/StubbornJava/

COPY ./hack/*.sh ./githubRepo/StubbornJava/

COPY ./ ./githubRepo/StubbornJava

COPY ./stubbornjava-examples/build/libs/*.json ./githubRepo/StubbornJava/

ENV CLASSPATH=/home/gradle/githubRepo/StubbornJava/stubbornjava-examples/build/libs/*

EXPOSE 8081

WORKDIR /home/gradle/githubRepo/StubbornJava

CMD ./resourceplan_controller.sh && java -Denv=local -Xmx640m com.stubbornjava.examples.undertow.rest.RestServer
