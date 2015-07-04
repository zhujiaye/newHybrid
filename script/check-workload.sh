FRUGALDB_HOME=`cd "$(dirname "$0")";cd ..;pwd`
workloadlist=`ls $FRUGALDB_HOME/workloads | grep .txt$`
echo $workloadlist

