#!/bin/bash

in_="$1"
out_="$2"


printf 'Converting %s to debug mode in %s\n' "$in_" "$out_"

#if
# perl -p -e 's/[^end]if(\s+)(\S+)(\s+)?(-?)%\}/if\1\2\3\4%}{%\4 set x=deb("if ", "\2", "(", \2, ")")\4%}/g' < "$in_" > "$out_"
perl -p -e 's/{%(-?)(\s+)if(\s+)(\S+)(\s+)?(-?)%\}/{%\1 set x=debn("if ", "\4", "(", \4, ")")\1%}{%\1\2if\3\4\5\6%}/g' < "$in_" > "$out_"

# else
perl -pi -e 's/else(\s+)(-?)%\}/else\1\2%}{%\1 set x=debn("else")\2%}/g' "$out_"


#elseif (TODO: prepend)
perl -pi -e 's/elseif(\s+)(\S+)(\s+)?(-?)%\}/elseif\1\2\3\4%}{%\4 set x=debn("elseif ", "\2", "(", \2, ")")\4%}/g' "$out_"


#for
perl -pi -e 's/for(\s+)(\S+)(\s+)in(\s+)(\S+)(\s+)?(-?)%\}/for\1\2\3in\4\5\7%}{%\7 set x=debn("for \2 in \5 (", loop.index0, ")")\7%}/g' "$out_"

#set
perl -pi -e 's/set(\s+)(\S+)(\s+)=(\s+)([^\-]+)(\s+)?(-?)%\}/set\1\2\3=\4\5\7%}{%\4 set x=debn("set \2 = \5 (", \2, ")")\7%}/g' "$out_"

# output (only for single lines)
# perl -pi -e 's/{{(-?)(.+?)(-?)}}/{{\1\2\3}}{%\1set x=deb(\2)\3%}/g' "$out_"


printf 'Done.\n'