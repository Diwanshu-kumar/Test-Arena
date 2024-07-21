#!/bin/bash

#read the code

code=$(cat -)

#write the code to a file

echo "$code" > main.cpp

#compile the code

g++ -o main main.cpp

#run the code
./main