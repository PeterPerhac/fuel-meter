#!/usr/bin/env bash

# sbt clean dist

DEVPRO_NO=1
scp target/universal/fuel-meter-1.0-SNAPSHOT.zip opc@devpro$DEVPRO_NO:/home/opc &

DEVPRO_NO=2
scp target/universal/fuel-meter-1.0-SNAPSHOT.zip opc@devpro$DEVPRO_NO:/home/opc &

wait $(jobs -p)

ssh -t opc@devpro1 'unzip -o fuel-meter-1.0-SNAPSHOT.zip && sudo systemctl restart fuelmeter.service && exit'
ssh -t opc@devpro2 'unzip -o fuel-meter-1.0-SNAPSHOT.zip && sudo systemctl restart fuelmeter.service && exit'

