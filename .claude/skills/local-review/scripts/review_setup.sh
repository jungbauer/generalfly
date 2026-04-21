#!/bin/bash

DATETIME="$(date +%Y%m%d_%H%M)"
DIR_NAME="LR_$DATETIME"

mkdir "local-review/$DIR_NAME"

git diff main...HEAD > "local-review/$DIR_NAME/changes.diff"

echo "local-review/$DIR_NAME"