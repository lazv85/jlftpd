#!/bin/sh
mvn compile
mvn exec:java -Dexec.mainClass="Main"
