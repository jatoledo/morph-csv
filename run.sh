#!/bin/bash
cd morphcsv
java -cp .:morph-csv.jar:lib/* es.upm.fi.dia.oeg.Morphcsv -c $1
cd ..