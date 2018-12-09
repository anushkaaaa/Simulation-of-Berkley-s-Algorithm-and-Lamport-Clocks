JCC = javac

default: Master.class Message.class Process.class Simulation.class RMIInterface.class

Master.class: Master.java
	$(JCC) Master.java

Message.class: Message.java
	$(JCC) Message.java

Process.class: Process.java
	$(JCC) Process.java
	
Simulation.class: Simulation.java
	$(JCC) Simulation.java
	
RMIInterface.class: RMIInterface.java
	$(JCC) RMIInterface.java
	
clean: 
	$(RM) *.class