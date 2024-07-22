#!/bin/sh

#exit 0

GREEN=$(tput setaf 2)
RED=$(tput setaf 1)
NORMAL=$(tput sgr0)

if [ -d ${project.basedir}/src/main/typescript ]; then
	cd ${project.basedir}/src/main/typescript
	printf "${GREEN}%s${normal}\n" 'Starting Angular build...'
	# FIXME: using force for as some dependencies do not match
	# npm install --no-optional --force
	npm install --force
	# bundle analysis https://www.npmjs.com/package/webpack-bundle-analyzer
	ng build --configuration production --optimization=true --aot=true --delete-output-path=false --output-path=${project.build.directory}/dist
	if [ $? -eq 0 ]; then
		# looks like in the argo build environment, we are outputting to the 'browser' folder?
		if [ -d ${project.build.directory}/dist ]; then
			printf "${GREEN}%s${NORMAL}\n" 'target dist folder exists as expected'
		else
			printf "${RED}%s '%s' %s${NORMAL}\n" 'target' ${project.build.directory}/dist 'not found, trying alternative folder'
			if [ -d ${project.build.directory}/browser ]; then
				printf "${GREEN}%s${NORMAL}\n" 'moving generated assets from browser folder to dist'
				mv ${project.build.directory}/browser/* ${project.build.directory}/dist/
			else
				printf "${RED}%s${NORMAL}\n" 'NG BUILD DID NOT WORK (no dist and no browser fallback folder)'
				exit -1
			fi
		fi
		printf "${GREEN}%s${NORMAL}\n" 'Angular build finished'
	else
		printf "${RED}%s${NORMAL}\n" 'NG BUILD DID NOT WORK (ng build failed)'
		exit -1
	fi
else
	printf "${RED}%s${NORMAL}\n" 'FOLDER ${project.basedir}/src/main/typescript DOES NOT EXIST'
	exit -1
fi
