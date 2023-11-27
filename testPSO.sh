#!/bin/bash

# Usage: ./testJavaPSO.sh [language] [testType] [testID] ~[jdkPath]
# Args: (~ = optional argument)
#    [language] = "rust" || "java"
#    [testType] = "custom" || "HCSP"
#    [testID] = The id of the custom test found the Scheduler class || The name of the hcsp data set file in quotes
#   ~[jdkPath] = The string path where the java jdk bin is located

jdkPath=/Library/Java/JavaVirtualMachines/temurin-17.jdk/Contents/Home/bin/java
if [[ $4 -ne 0 ]]; then
  echo "Using custom jdk path..."
  jdkPath=$4
fi

if [[ $1 = "java" ]]; then
  # Run the main Java method from the Scheduler class to generate the algorithm output data
  echo "Java PSO selected"
  /usr/bin/env $jdkPath -XX:+ShowCodeDetailsInExceptionMessages -cp ./psoscheduler-java/target/classes syslab.cloudcomputing.schedule.Scheduler $2 $3
else
  echo "Rust PSO selected"
  cargo run --manifest-path ./psoscheduler-rust/Cargo.toml
fi

# Generate a objective history plot based off the output data
python3 plot_objective_history.py --lang $1 --test $3