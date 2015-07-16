#!/usr/bin/env bash
FRUGALDB_HOME=`cd "$(dirname "$0")";cd ..;pwd`
thrift --gen java --out $FRUGALDB_HOME/src $FRUGALDB_HOME/thrift/newhybrid.thrift
