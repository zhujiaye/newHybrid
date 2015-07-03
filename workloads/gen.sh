for ((i=1;i<=5;i++))
do
	java -jar WorkloadGenerator.jar
	mv load.txt load550500_0.20.1_test$i.txt
	java -jar WorkloadGenerator.jar 0.25
	mv load.txt load550500_0.250.1_test$i.txt
	java -jar WorkloadGenerator.jar 0.3
	mv load.txt load550500_0.30.1_test$i.txt
	
done
