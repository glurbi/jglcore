ifeq "$(OS)" "Windows_NT"
CLASSPATH="lib\gluegen-rt.jar;lib\jogl.all.jar;lib\nativewindow.all.jar;lib\newt.all.jar;etc;target\classes"
TARGETDIR="target\classes"
else
CLASSPATH="lib/gluegen-rt.jar:lib/jogl.all.jar:lib/nativewindow.all.jar:lib/newt.all.jar:etc:target/classes"
TARGETDIR="target/classes"
endif

LIBRARY_PATH="lib"
FILES=`find src -name *.java`

all: prepare resources
	javac -sourcepath src -deprecation -classpath $(CLASSPATH) -d $(TARGETDIR) $(FILES)

prepare:
	mkdir -p $(TARGETDIR)

resources:
	(cd src ; tar cf - `find -not -name "*.java" -type f -print`) | (cd $(TARGETDIR) ; tar xf -)
    
clean:
	rm -rf target
    
runTutorial01: all
	java -Djava.library.path=$(LIBRARY_PATH) -classpath $(CLASSPATH) glcore.tutorial01.Tutorial01

