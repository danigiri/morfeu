#!/bin/sh

#exit 0

GREEN=$(tput setaf 2)
RED=$(tput setaf 1)
NORMAL=$(tput sgr0)

if [ -d ${project.basedir}/src/main/angular ]; then
	cd ${project.basedir}/src/main/angular
	printf "${GREEN}%s${normal}\n" 'Starting Angular build...'
	npm install --no-optional
	ng build --prod --optimization=true --aot=true --deleteOutputPath=false --outputPath=${project.build.directory}/dist
	printf "${GREEN}%s${NORMAL}\n" 'Angular build finished'
else
	printf "${RED}%s${NORMAL}\n" 'FOLDER ${project.basedir}/src/main/angular DOES NOT EXIST'
	exit -1
fi
