#!/bin/sh

# we have a timestamp of when this was built
date +%s > ${project.build.outputDirectory}/metadata/build-time.txt
