#!/bin/sh

workdir=$(cd $(dirname $0) && pwd)
cd $workdir

# api_pid=$(netstat -tunlp | grep ':8000' | awk '{print $7}' | awk -F '/' {'print $1'})
# front_pid=$(netstat -tunlp | grep ':3000' | awk '{print $7}' | awk -F '/' {'print $1'})
java_excutor=/opt/fenda/module/jdk-17.0.11/bin/java
mvn_excutor=/opt/fenda/module/apache-maven-3.9.8/bin/mvn
git_excutor=/usr/bin/git

log=log.txt
pid=$(netstat -tunlp | grep ':8080' | awk '{print $7}' | awk -F '/' {'print $1'})

get_pid() {
    pid=$(netstat -tunlp | grep ':8080' | awk '{print $7}' | awk -F '/' {'print $1'})
}

pull() {
    $git_excutor pull
}

stop() {
    get_pid
    if [ -n "$pid" ]; then
        kill -9 $pid
    fi
}

build(){
  $mvn_excutor clean package
}

start() {
    get_pid
    if [ -z $pid ]; then
        nohup java -jar target/deploy_tool_api-0.0.1-SNAPSHOT.jar >${log} 2>&1 &
    else
        echo "pid already exist: $pid"
    fi
}


case "$1" in
    "pull")
        pull
        ;;
    "stop")
        stop
        ;;
    "build")
        build
        ;;
    "start")
        start
        ;;
    "restart")
        stop
        sleep 1
        start
        ;;
    "pid")
        get_pid
        echo $pid
        ;;
esac

commonds(){
    for i in "$@"; do
        api $i
    done
}

args=("$@")
args_length="${#args[*]}"
if [ $args_length -eq 0 ]; then
    exit
fi

commonds "${args[*]}"