# Simulation-of-Berkley-s-Algorithm-and-Lamport-Clocks
 To create a pseudo-distributed simulation using the principles of clock consistency, drifts and inter-process communication based on the concept of Lamport clocks and Berkeley Algorithm

Content:
1) simulation.java
2) master.java
3) process.java
4) message.java
5) Makefile
6) Report
7) readme


------------------------------
To compile the file: make
To run the file: java simulation

------------------------------

The program runs on any machine from the following list:
in-csci-rrpc01.cs.iupui.edu 10.234.136.55
in-csci-rrpc02.cs.iupui.edu 10.234.136.56
in-csci-rrpc03.cs.iupui.edu 10.234.136.57
in-csci-rrpc04.cs.iupui.edu 10.234.136.58
in-csci-rrpc05.cs.iupui.edu 10.234.136.59
in-csci-rrpc06.cs.iupui.edu 10.234.136.60

-----------------------------
Note: When you run the file, you might notice several print statements on the output screen
To distinguish between the functionalities you can comment those print statements.
There are several comments that will tell you the purpose of the function or statement.

Interpretation of few print statements: 

Receive Event :: Process  5 Current Logical Clock 240 <br>
--- Process 5 received a msg and its current logical clock is 240 <br>
[2]  iteration 260 Changed logical Clock 2222 <br>
--- At iteration 260 process 2's logical clock was changed to 2222 <br>
Send Event :: Process 1 :: to Process 5 <br>
--- Process 1 sends a message to process 5 <br>

-----------------------------

