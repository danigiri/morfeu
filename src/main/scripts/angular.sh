#!/bin/sh

#exit 0
if [ -v TERM ]; then
	GREEN=$(tput setaf 2)
	RED=$(tput setaf 1)
	NORMAL=$(tput sgr0)
else 
	export TERM=vt100
	GREEN=$(tput bold)
	RED=$(tput bold)
	NORMAL=$(tput sgr0)
fi

if [ -d ${project.basedir}/src/main/typescript ]; then
	cd ${project.basedir}/src/main/typescript
	printf "${GREEN}%s${normal}\n" 'Starting Angular build...'
	# FIXME: using force for as some dependencies do not match
	# npm install --no-optional --force
	npm install --force
	# bundle analysis https://www.npmjs.com/package/webpack-bundle-analyzer
	# be aware that ng build appends a /browser to the output path, we will solve this in the war building
	# it is also **relative**, so we go from src/main/typescript down and then go to target, which is hardcoded
	# unfortunately, it looks like ng build does not support absolute paths anymore
	ng build --configuration production --optimization=true --aot=true --delete-output-path=false --output-path=../../../target/dist
	if [ $? -eq 0 ]; then
		printf "${GREEN}%s${NORMAL}\n" 'Angular build finished'
	else
		printf "${RED}%s${NORMAL}\n" 'NG BUILD DID NOT WORK (ng build failed)'
		exit -1
	fi
else
	printf "${RED}%s${NORMAL}\n" 'FOLDER ${project.basedir}/src/main/typescript DOES NOT EXIST'
	exit -1
fi
