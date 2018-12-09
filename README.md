# Simulation-of-Berkley-s-Algorithm-and-Lamport-Clocks
To create a distributed simulation using the principles of clock consistency, drifts and inter-process communication based on the concept of Lamport clocks and Berkeley Algorithm. The interprocess communication will take place using the java RMI.

Content:

simulation.java <br />
master.java <br />
process.java <br />
message.java <br />
RMIInterface.java <br />
policy <br />
Makefile <br />
Report <br />
readme <br />
<br />

1. Clone or download the folder

2. Open an instance of putty on all the machines listed below and run the commands<br />
• in-csci-rrpc01.cs.iupui.edu - 10.234.136.55<br />
• in-csci-rrpc02.cs.iupui.edu - 10.234.136.56<br />
• in-csci-rrpc03.cs.iupui.edu - 10.234.136.57<br />
• in-csci-rrpc04.cs.iupui.edu - 10.234.136.58<br />
• in-csci-rrpc05.cs.iupui.edu - 10.234.136.59<br />

3. On any one machine, Run make file
command: make

4. Running the RMI Registry and running the processes and master.<br />
 a. Run the RMI Registry in the background - this will run at the set port(4444): (Do this on all the machines)
	command: rmiregistry 4444& <br /><br/>
 b. Start processes and master <br />
 Process 1 -- machine 1 (in-csci-rrpc01.cs.iupui.edu - 10.234.136.55)<br />
 command: java -Djava.security.policy=policy Simulation 1 1<br /><br/>
 Process 2 -- machine 2 (in-csci-rrpc02.cs.iupui.edu - 10.234.136.56)<br />
 command: java -Djava.security.policy=policy Simulation 1 2<br /><br/>
 Process 3 -- machine 3 (in-csci-rrpc03.cs.iupui.edu - 10.234.136.57)<br />
 command: java -Djava.security.policy=policy Simulation 1 3<br /><br/>
 Process 4 -- machine 4 (in-csci-rrpc04.cs.iupui.edu - 10.234.136.58)<br />
 command: java -Djava.security.policy=policy Simulation 1 4<br /><br/>
 Master -- machine 5 (in-csci-rrpc05.cs.iupui.edu - 10.234.136.59)<br />
 command: java -Djava.security.policy=policy Simulation 0 5<br /><br/>
 Note: Here arg[0] '0' -- master '1' -- process and the int values arg[1] are unique id's associated to the object types. Please make sure you enter correct commands to avoid unnecessary exceptions<br/>
 Do step c after completing  step b for all the machines<br/><br/>
 c. It will ask you "Do you wish to connect to all the processes and master?<br/>1. yes<br/>2. no"<br/>(Do this on all the machines)<br/>Enter : 1<br/>

5. To clean up i.e. clear the port number by killing the rmiregistry 
command: fg<br/>


Note: There will be multiple output statements.
It will show you the message being send and received and the process id's associated with it.
You can comment the output statements.

In case, the port number 4444 is blocked or under use by somebody else. Please change the port number.

The process might look like it's slow, this is because I have added sleep(500) at intervals
