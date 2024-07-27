#!/bin/bash
#compile the code
g++ -o main main.cpp 2> error.txt
#run the code
# Check if compilation was successful
if [ $? -ne 0 ]; then
    # If there was an error, return the error log
    echo "Compilation error"
else
    # If compilation was successful, execute the program and capture output and time

    # Capture start time in nanoseconds
    START=$(date +%s%N)

    # Run cpp program
    output=$(./Main <input.txt 2>&1)
    program_exit_code=$?

    # Capture end time in nanoseconds
    END=$(date +%s%N)

    # Calculate execution time in milliseconds
    ELAPSED_TIME=$(($(($END - $START)) / 1000000))

    if [ $program_exit_code -ne 0 ]; then
        echo "Runtime error"
        echo "$output" > error.txt
        echo $ELAPSED_TIME >executionTime.txt
    else
        echo "Compilation successful"
        echo "$output" > output.txt
        echo $ELAPSED_TIME >executionTime.txt
    fi
fi