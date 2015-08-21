#!/usr/bin/env bash
home=`cd "$(dirname "$0")";cd ..;pwd`

get_env(){
	. $home/conf/newhybrid-env.sh
}
get_env
rm -r $NEWHYBRID_HOME/src/thrift/
thrift --gen java --out $NEWHYBRID_HOME/src $NEWHYBRID_HOME/thrift/newhybrid.thrift
