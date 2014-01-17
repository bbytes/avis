#!/bin/bash

. ./setenv.sh

if [ -a $PID_FILE ]; then
        echo "Instance $INSTANCE_ID is already running... not doing anything."
else
        echo "Starting instance $INSTANCE_ID"
	nohup java -cp .:lib/*:avis-server-0.0.1-SNAPSHOT.jar com.bbytes.avis.Main &

        echo "$!" > $PID_FILE
fi
