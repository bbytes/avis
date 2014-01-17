#!/bin/sh

. ./setenv.sh

if [ -a $PID_FILE ]; then
        echo "Stopping AVIS $INSTANCE_ID"
        kill `cat $PID_FILE`
        rm $PID_FILE
else
        echo "AVIS $INSTANCE_ID not running..."
fi
