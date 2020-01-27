#!/usr/bin/env bash

sbt clean dist

scp target/universal/fuel-meter-1.0-SNAPSHOT.zip opc@devpro:/home/opc
ssh -t opc@devpro 'unzip -o fuel-meter-1.0-SNAPSHOT.zip && sudo systemctl restart fuelmeter.service && exit'

sleep 10
curl http://devpro/fuelmeter

