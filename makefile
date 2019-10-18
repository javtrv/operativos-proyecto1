JFLAGS = -cp
JC = javac
JAR = .:json-simple-1.1.1.jar
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $(JAR) $*.java

CLASSES = \
	Parser.java \
	RedBlackTree.java \
	Scheduler.java \
	Process.java \
	main.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class

run:
	java -cp $(JAR) main  procesos1.json

test:
	java RedBlackTree
