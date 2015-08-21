#!/usr/bin/env bash
get_env(){
	. $home/conf/newhybrid-env.sh
}
start_server(){
    java -classpath $CLASSPATH:$NEWHYBRID_LIBDIR/*:$NEWHYBRID_BINDIR $NEWHYBRID_SERVER_JAVA_OPTS test.Test server
}
start_worker(){
    java -classpath $CLASSPATH:$NEWHYBRID_LIBDIR/*:$NEWHYBRID_BINDIR $NEWHYBRID_WORKER_JAVA_OPTS -Dnewhybrid.worker.dbms.type=$1 test.Test worker 
}
start_client(){
    java -classpath $CLASSPATH:$NEWHYBRID_LIBDIR/*:$NEWHYBRID_BINDIR $NEWHYBRID_CLIENT_JAVA_OPTS test.Test client 
}
home=`cd "$(dirname "$0")";cd ..;pwd`
get_env
case "${1}" in
    server)
        start_server
        ;;
    worker)
        start_worker $2
        ;;
    client)
        start_client
        ;;
    *)
        ;;
esac
