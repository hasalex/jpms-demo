#!/bin/bash
origin="${BASH_SOURCE[0]}"

source /etc/environment
#source $(dirname $origin)/setenv.sh
set +e  # sinon on quitte le terminal à chaque erreur.

if [[ "$1" == "9" ]]
then
    JAVA_HOME="/opt/jdk9"
    PATH="$JAVA_HOME/bin:$PATH"
else
    unset JAVA_HOME
    unset MAVEN_OPTS
fi
