JCC = javac

default: master.class message.class process.class simulation.class

master.class: master.java
	$(JCC) master.java

message.class: message.java
	$(JCC) message.java

process.class: process.java
	$(JCC) process.java
	
simulation.class: simulation.java
	$(JCC) simulation.java
	
clean: 
	$(RM) *.class