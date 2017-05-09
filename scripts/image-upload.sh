#!/bin/bash

img_dir=images

for img in $img_dir/*; do
    echo "$img"
    curl -F "image=@$img" \
         -v http://localhost:8001/img
done

echo ""
echo ""
