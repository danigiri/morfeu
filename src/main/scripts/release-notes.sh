
# see https://blogs.sap.com/2018/06/22/generating-release-notes-from-git-commit-messages-using-basic-shell-commands-gitgrep/
#git log $(git describe --tags --abbrev=0)..HEAD --pretty=format:"%s" -i -E --grep="^(\[INTERNAL\]|\[FEATURE\]|\[FIX\]|\[DOC\])*\[FEATURE\]"