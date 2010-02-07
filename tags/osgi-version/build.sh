#!/bin/sh

mvn clean pax:clean install pax:eclipse -DdownloadSources=true -DdownloadJavadocs=true
