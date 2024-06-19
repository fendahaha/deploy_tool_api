#!/bin/sh

workdir=$(cd $(dirname $0) && pwd)
cd $workdir

# api_pid=$(netstat -tunlp | grep ':8000' | awk '{print $7}' | awk -F '/' {'print $1'})
# front_pid=$(netstat -tunlp | grep ':3000' | awk '{print $7}' | awk -F '/' {'print $1'})
python_excutor=/usr/bin/score_python
git_excutor=/usr/bin/git
npm_excutor=/usr/bin/npm

api_path=/opt/project/app/src/wf-score-x-api
api_log=/opt/project/app/logs/api.log
front_path=/opt/project/app/src/wf-score-x-front
front_log=/opt/project/app/logs/front.log
api_pid=''
front_pid=''

get_api_pid() {
    api_pid=$(netstat -tunlp | grep ':8000' | awk '{print $7}' | awk -F '/' {'print $1'})
}
get_front_pid() {
    front_pid=$(netstat -tunlp | grep ':3000' | awk '{print $7}' | awk -F '/' {'print $1'})
}

# ###################################
front_pull() {
    cd $front_path
    $git_excutor pull >${front_log} 2>&1
}

front_install() {
    cd $front_path
    $npm_excutor install >${front_log} 2>&1
}

front_build() {
    get_front_pid
    if [ -z $front_pid ]; then
        cd $front_path
        $npm_excutor run gte
        $npm_excutor run build >${front_log} 2>&1
    else
        echo "pid already exist: $front_pid, please stop first"
    fi
}

front_dev() {
    cd $front_path
    $npm_excutor run dev >${front_log} 2>&1
}

front_stop() {
    get_front_pid
    if [ -n $front_pid ]; then
        kill -9 $front_pid
    fi
}

front_start() {
    get_front_pid
    if [ -z $front_pid ]; then
        cd $front_path
        nohup npm run start >${front_log} 2>&1 &
    else
        echo "pid already exist: $front_pid"
    fi
}

front_restart() {
    cd $front_path
    cd ..
    mkdir tmp_front
    rsync -a "$front_path/" tmp_front/
    cd tmp_front
    $npm_excutor install >${front_log} 2>&1
    $npm_excutor run gte
    $npm_excutor run build >${front_log} 2>&1
    if [ $? -eq 0 ]; then
        front_stop
        cd ..
        rsync -a tmp_front/ "$front_path/"
        cd $front_path
        nohup npm run start >${front_log} 2>&1 &
        cd .. && rm -rf tmp_front
    else
        echo "'npm run build' has error! restart fail."
    fi
}

front() {
    case "$1" in
    "pull")
        front_pull
        ;;
    "install")
        front_install
        ;;
    "dev")
        front_dev
        ;;
    "build")
        front_build
        ;;
    "stop")
        front_stop
        ;;
    "start")
        front_start
        ;;
    "restart")
        front_stop
        sleep 1
        front_start
        ;;
    "pull_restart")
        front_pull
        front_restart
        ;;
    "pid")
        get_front_pid
        echo $front_pid
        ;;
    esac
}
# #################################
api_pull() {
    cd $api_path
    $git_excutor pull >$api_log 2>&1 &
}

api_stop() {
    get_api_pid
    if [ -n $api_pid ]; then
        kill -9 $api_pid
    fi
}

api_start() {
    get_api_pid
    if [ -z $api_pid ]; then
        cd $api_path
        nohup ${python_excutor} manage.py runserver 127.0.0.1:8000 >$api_log 2>&1 &
    else
        echo "pid already exist: $api_pid"
    fi
}

api() {
    case "$1" in
    "pull")
        api_pull
        ;;
    "stop")
        api_stop
        ;;
    "start")
        api_start
        ;;
    "restart")
        api_stop
        sleep 1
        api_start
        ;;
    "pid")
        get_api_pid
        echo $api_pid
        ;;
    esac
}

api_commonds(){
    for i in $@; do
        api $i
    done
}

front_commonds(){
    for i in $@; do
        front $i
    done
}

args=("$@")
args2=("${args[*]:1}")
args2_length="${#args2[*]}"

if [ $args2_length -eq 0 ]; then
    exit
fi

case "${args[0]}" in
"api")
    api_commonds "${args2[*]}"
    ;;
"front")
    front_commonds "${args2[*]}"
    ;;
esac
