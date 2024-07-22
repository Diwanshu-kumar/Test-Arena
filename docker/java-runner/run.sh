#!/bin/bash

# Read the code from standard input
code=$(cat -)

# Write the code to a file
echo "$code" > Main.java

# Compile the Java code and capture errors
javac Main.java 2> error.log

# Check if compilation was successful
if [ $? -ne 0 ]; then
    # If there was an error, return the error log
    echo "Compilation error"
    cat error.log
else
    # If compilation was successful, execute the program and capture output and time

    # Capture start time in nanoseconds
    START=$(date +%s%N)

    # Run Java program
    output=$(java Main 2>&1)
    program_exit_code=$?

    # Capture end time in nanoseconds
    END=$(date +%s%N)

    # Calculate execution time in milliseconds
    ELAPSED_TIME=$(($(($END - $START)) / 1000000))

    if [ $program_exit_code -ne 0 ]; then
        echo "Runtime error"
        echo "$output"
        echo "Execution time: $ELAPSED_TIME ms"
    else
        echo "Compilation successful"
        echo "$output"
        echo "Execution time: $ELAPSED_TIME ms"
    fi
fi
