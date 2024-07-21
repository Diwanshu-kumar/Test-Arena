#!/bin/bash
# Read the code from standard input
code=$(cat -)

# Write the code to a file
echo "$code" > script.py

# Execute the Python script and capture the output
python script.py
