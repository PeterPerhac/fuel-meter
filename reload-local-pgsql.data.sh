#!/usr/bin/env bash
psql -h localhost -p 35432 --user fuelmeter -d fuelmeter -f db/002-load-data.sql
open http://devpro-test:9000/fuelmeter/vehicles/HY13VLV/readings/list

