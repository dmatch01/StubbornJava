FROM gradle

RUN mkdir -p githubRepo/StubbornJava

COPY ./ ./githubRepo/StubbornJava

COPY ./stubbornjava-examples/build/libs/TestTree.json ./githubRepo/StubbornJava

ENV CLASSPATH=/home/gradle/githubRepo/StubbornJava/stubbornjava-examples/build/libs/*

WORKDIR /home/gradle/githubRepo/StubbornJava

CMD tail -f /dev/null
#CMD java -Denv=local -Xmx640m com.stubbornjava.examples.undertow.rest.RestServer 
