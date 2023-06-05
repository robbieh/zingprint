
record: recording/zing.yml
	./node_modules/.bin/terminalizer record recording/zing.yml -c ./recording/terminalizer-config.yml   -k

render: zing-fast.gif
	./node_modules/.bin/terminalizer render recording/zing.yml -o zing.gif
	convert -delay 1x20 zing.gif zing-fast.gif

#uberscript: uberscript/simplescrape.clj
#	bb uberscript uberscript/simplescrape.clj -f bb/simplescrape/core.clj -m simplescrape.core

testloop:
	echo bb/zingprint/zingprint.clj | entr ./zingtest.sh

repl:
	bb nrepl

