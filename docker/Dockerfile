FROM gradle

RUN mkdir -p githubRepo/StubbornJava

COPY ./ ./githubRepo/StubbornJava

COPY ./stubbornjava-examples/build/libs/*.json ./githubRepo/StubbornJava/

ENV CLASSPATH=/home/gradle/githubRepo/StubbornJava/stubbornjava-examples/build/libs/*

EXPOSE 8081

WORKDIR /home/gradle/githubRepo/StubbornJava

CMD java -Denv=local -Xmx640m com.stubbornjava.examples.undertow.rest.RestServer 
