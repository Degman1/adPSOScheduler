#!/bin/bash

# Get the number of the test to run
n=$1

# Run the main Java method from the Scheduler class to generate the algorithm output data
/usr/bin/env /Library/Java/JavaVirtualMachines/temurin-17.jdk/Contents/Home/bin/java -XX:+ShowCodeDetailsInExceptionMessages -cp /Users/davidgerard/Documents/Coding/UMASS/Research/adPSOScheduler/psoscheduler/target/classes syslab.cloudcomputing.schedule.Scheduler $n

# Generate a objective history plot based off the output data
python3 plot_objective_history.py --test $n
