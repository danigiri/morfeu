#!/bin/sh

#exit 0

GREEN=$(tput setaf 2)
RED=$(tput setaf 1)
NORMAL=$(tput sgr0)

if [ -d ${project.basedir}/src/main/typescript ]; then
	cd ${project.basedir}/src/main/typescript
	printf "${GREEN}%s${normal}\n" 'Starting Angular build...'
	# FIXME: using force for as some dependencies do not match
	npm install --no-optional --force
	# bundle analysis https://www.npmjs.com/package/webpack-bundle-analyzer
	ng build --configuration production --optimization=true --aot=true --delete-output-path=false --output-path=${project.build.directory}/dist
	if [ $? -eq 0 ]; then
		printf "${GREEN}%s${NORMAL}\n" 'Angular build finished'
	else
		printf "${RED}%s${NORMAL}\n" 'NG BUILD DID NOT WORK'
		exit -1		
	fi
else
	printf "${RED}%s${NORMAL}\n" 'FOLDER ${project.basedir}/src/main/typescript DOES NOT EXIST'
	exit -1
fi
