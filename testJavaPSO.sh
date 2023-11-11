#!/bin/bash

# Usage: ./testJavaPSO.sh [testType] [testID]
# Args: [testType] = "custom" || "HCSP"
#       [testID] = The id of the custom test found the Scheduler class || The name of the hcsp data set file in quotes

# Get the number of the test to run
n=$1

# Run the main Java method from the Scheduler class to generate the algorithm output data
/usr/bin/env /Library/Java/JavaVirtualMachines/temurin-17.jdk/Contents/Home/bin/java -XX:+ShowCodeDetailsInExceptionMessages -cp /Users/davidgerard/Documents/Coding/UMASS/Research/AdaptivePSOScheduler/psoscheduler-java/target/classes syslab.cloudcomputing.schedule.Scheduler $1 $2

# # Generate a objective history plot based off the output data
python3 plot_objective_history.py --test $2
