#!/usr/bin/env bash
export NEWHYBRID_HOME=`cd "$(dirname "$0")";cd ..;pwd`
export NEWHYBRID_LOGDIR=${NEWHYBRID_HOME}/logs
export NEWHYBRID_CONFDIR=${NEWHYBRID_HOME}/conf
export NEWHYBRID_BINDIR=${NEWHYBRID_HOME}/bin
export NEWHYBRID_SCRIPTDIR=${NEWHYBRID_HOME}/script
export NEWHYBRID_WORKLOADDIR=${NEWHYBRID_HOME}/workloads
export NEWHYBRID_LIBDIR=${NEWHYBRID_HOME}/lib
export NEWHYBRID_JOURNALDIR=${NEWHYBRID_HOME}/journal

export NEWHYBRID_JAVA_OPTS+="
 -Dnewhybrid.logdir=$NEWHYBRID_LOGDIR
 -Dnewhybrid.confdir=$NEWHYBRID_CONFDIR
 -Dnewhybrid.workloaddir=$NEWHYBRID_WORKLOADDIR
 -Dlog4j.configuration=file:${NEWHYBRID_CONFDIR}/log4j.properties
 -Dnewhybrid.server.address=10.20.2.20
"
#another configurable system variable
#format name(default value)
#newhybrid.thrift.selector.threads(3)
#newhybrid.thrift.queue.size.per.selector(4000)
#newhybrid.thrift.server.threads(4000)
#newhybrid.server.client.connect.timeout.s(30)
#newhybrid.worker.client.connect.timeout.s(30)
#newhybrid.server.port(12345)
#newhybrid.worker.port(54321)
#newhybrid.temp.folder(/tmp)
#newhybrid.server.use.memmonitor(false)
#newhybrid.model.deterministic(false)
#newhybrid.mysql.username(remote)
#newhybrid.mysql.password(remote)

export NEWHYBRID_SERVER_JAVA_OPTS="
 ${NEWHYBRID_JAVA_OPTS}
 -Dnewhybrid.logname=SERVER_LOGGER
 -Dnewhybrid.server.image.path=/tmp/image
"
export NEWHYBRID_CLIENT_JAVA_OPTS="
 ${NEWHYBRID_JAVA_OPTS}
 -Dnewhybrid.logname=CLIENT_LOGGER
"
export NEWHYBRID_WORKER_JAVA_OPTS="
 ${NEWHYBRID_JAVA_OPTS}
 -Dnewhybrid.logname=WORKER_LOGGER
 -Dnewhybrid.mysql.db.name=tpcc_icde
 -Dnewhybrid.voltdb.capacity.mb=2000
"
