#!/bin/sh

GREEN=$(tput setaf 2)
RED=$(tput setaf 1)
normal=$(tput sgr0)

if [ -d ${project.basedir}/src/main/angular ]; then
	cd ${project.basedir}/src/main/angular
	printf "${GREEN}%s${normal}" 'Starting Angular build...'
	ng build --prod --optimization=true --aot=true --deleteOutputPath=false --outputPath=${project.build.directory}/dist
	printf "${GREEN}%s${normal}" 'Angular build finished'
else
	printf "${RED}%s${normal}" 'FOLDER ${project.basedir}/src/main/angular DOES NOT EXIST'
	exit -1
fi
