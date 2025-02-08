#!/bin/sh

[[ -d logs ]] || mkdir logs

rm logs/*

sel="c"
if [ $# -eq 1 ]; then
  sel="$1"
fi

retval=0
if [[ $sel == *"c"* ]]; then
  cd Sample
  if ! ./gradlew build; then
    retval=-1
    echo "Failed to build the JAR"
  else
    echo "Successfully built the JAR"
  fi
  cd ..
fi

if [ $retval -eq 0 ]; then
  docker compose build --push > ./logs/docker.build.log 2> ./logs/docker.result.log
  nr="$(grep -c "Built" "./logs/docker.result.log")"
  if [[ "$nr" != "4" ]]; then
    retval=-1
    echo "Failed to build docker images $nr. See details at ./logs/docker.build.log"
  else
    echo "Successfully build and pushed $nr docker images"
    retval=0
  fi
fi

if [ $retval -eq 0 ]; then
   if [[ $sel == *"d"* ]]; then
      docker compose down
      docker compose up -d
   fi
fi

exit $retval