#!/bin/sh

if [ -d ."git" ]; then
   echo "Error: Delete the .git directory"
   exit 1
fi
   
if [ $# -eq 1 ]; then
    mv src/myapp/ "src/$1"
    mv test/myapp/ "test/$1"
    find . -type f -exec sed -i "s/myapp/$1/g" {} +
else
    echo "Usage: $0 project_name"
fi


