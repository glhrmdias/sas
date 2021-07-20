#!/bin/sh

chmod +r getdown/ -R

rsync -rplczv -e ssh getdown/ \
    iprev@172.16.205.150:/var/www/apps/sas

