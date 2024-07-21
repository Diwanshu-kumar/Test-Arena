#!/bin/bash

# Read the code from standard input
code=$(cat -)

# Write the code to a file
echo "$code" > Main.java


#Compile the java code first
javac Main.java

#Execute the code

java Main
