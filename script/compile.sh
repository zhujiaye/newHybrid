#!/usr/bin/env bash

get_env(){
	. $home/conf/newhybrid-env.sh
}
home=`cd "$(dirname "$0")";cd ..;pwd`
get_env
find $NEWHYBRID_HOME/src/ -name "*.java"  > files
javac -classpath $CLASSPATH:$NEWHYBRID_HOME/lib/* -d $NEWHYBRID_HOME/bin @files 
rm files
