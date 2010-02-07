#!/bin/sh

./build.sh
pax-provision --vmOptions="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005"

