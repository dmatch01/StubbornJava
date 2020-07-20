# ReST Server
Example ReST Server Setup

Follow the instructions below to run an example rest server in a docker container on your localhost.

## Running the ReST Server
### Clone This Respository
```
$ git clone git@github.ibm.com:darroyo/restserver.git
Cloning into 'restserver'...
remote: Enumerating objects: 9, done.
remote: Counting objects: 100% (9/9), done.
remote: Compressing objects: 100% (6/6), done.
remote: Total 9 (delta 0), reused 0 (delta 0), pack-reused 0
Receiving objects: 100% (9/9), done.
Checking connectivity... done.
$
```

### Build Docker Image

Run the Docker build command (*make sure to include a space between the `restserver:latest` tag and the `.` (dot) in the `docker build` command below*): 
```
$ cd restserver
$
$ docker build --no-cache --tag restserver:latest .
Sending build context to Docker daemon  152.2MB
Step 1/7 : FROM gradle
 ---> 63a06ad25ffc
Step 2/7 : RUN mkdir -p githubRepo
 ---> Running in df56fda0ab9d
Removing intermediate container df56fda0ab9d
 ---> cc48122be5fe
Step 3/7 : RUN cd githubRepo && git clone https://github.com/dmatch01/StubbornJava.git
 ---> Running in 74a299f3aaaf
Cloning into 'StubbornJava'...
...
Step 6/7 : WORKDIR /home/gradle/githubRepo/StubbornJava
 ---> Running in d3606a336393
Removing intermediate container d3606a336393
 ---> 8c7c838cb613
Step 7/7 : CMD tail -f /dev/null
 ---> Running in 7f93aba960bc
Removing intermediate container 7f93aba960bc
 ---> 039d14ae300b
Successfully built 039d14ae300b
Successfully tagged restserver:latest
$
```
The image build process will clone a sample `github` repo and build all the jars but will __not__ setup the image to run the ReST Server automatically.  To run the ReST server after the container is running, a command must be entered (*see below for the command to start the ReST Server*).

### Run a Docker Container with the Built Image

```
$ docker run -d --name restserver -p 127.0.0.1:8080:8080/tcp restserver:latest
1654e46a . . . 2680506400366c
$
```
### Start the ReST Server
Enter into the container to start the ReST Server:
```
$ docker exec -it restserver /bin/bash 
root@2148a99e1915:/home/gradle/githubRepo/StubbornJava#
```
Now run the ReST Server in the foreground:
```
root@2148a99e1915:/home/gradle/githubRepo/StubbornJava# java -Denv=local -Xmx640m com.stubbornjava.examples.undertow.rest.RestServer
2020-07-15 03:04:23.503 [main] INFO  com.stubbornjava.common.Env - Found env setting local in system properties
2020-07-15 03:04:23.510 [main] INFO  com.stubbornjava.common.Env - Current Env: local
2020-07-15 03:04:23.802 [main] INFO  c.s.common.undertow.SimpleServer - ListenerInfo{protcol='http', address=/0.0.0.0:8080, sslContext=null}
```
### Testing the ReST Server
Since the container has exposed the ReST Server Port `8080` to the `localhost` the following `curl` commands can be done outside the container on the host.

[Create a User](https://www.stubbornjava.com/posts/lightweight-embedded-java-rest-server-without-a-framework#create-user)

[Update a User](https://www.stubbornjava.com/posts/lightweight-embedded-java-rest-server-without-a-framework#update-user)

[List Users](https://www.stubbornjava.com/posts/lightweight-embedded-java-rest-server-without-a-framework#list-users)

#### Request an Allocation Example
```
$ curl -X POST "localhost:8081/quota/alloc" -d '
{
  "id" : "job1",
  "group" : "M",
  "demand" : [
  "1",
  "512"
  ],
  "priority" : 0,
  "preemptable" : false
}
';
{
  "id" : "job1",
  "group" : "M",
  "demand" : 1,
  "priority" : 0,
  "preemptable" : false,
  "dateCreated" : "2020-07-19"
$
```
#### Release an Allocation Example
```
$ curl -X DELETE "localhost:8081/quota/release/job1"
$
```

### Stop the ReST Server

To stop the ReST Server enter:
`<control>-c`

To exit the container:
```
^Croot@9a3872df4aec:/home/gradle/githubRepo/StubbornJava# exit
$
```

### To Stop and Remove the Running Container
```
$ docker rm -f restserver
restserver
$
```
