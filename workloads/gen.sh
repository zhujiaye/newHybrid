for ((i=1;i<=5;i++))
do
	java -jar WorkloadGenerator.jar 5_50_500 2000 0.25 0.1 1.0
	mv load.txt load550500_0.250.1_test$i.txt
	java -jar WorkloadGenerator.jar 5_50_500 2000 0.3 0.1 1.0
	mv load.txt load550500_0.30.1_test$i.txt
	java -jar WorkloadGenerator.jar 5_50_500 2000 0.35 0.1 1.0
	mv load.txt load550500_0.350.1_test$i.txt
	java -jar WorkloadGenerator.jar 5_50_500 2000 0.4 0.1 1.0
	mv load.txt load550500_0.40.1_test$i.txt
done
