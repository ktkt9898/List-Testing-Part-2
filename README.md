# Design and Concept
Compare DLL to SLL and ArrayList
Why would I pick a DLL? What are the strengths and weaknesses
more memory needed

****************
* Double-Linked List with Tester
* CS 221
* 14 November 2024
* Kyle Austen Truschel
**************** 

# OVERVIEW:
This program functions to serve as a creator of a double linked list data structure, and has the ability to hold any type of object. It also includes a tester file that assures the proper output of said double linked data structure.


# INCLUDED FILES:
 * IUDoubleLinkedList - source file
 * ListTester - driver file and source file
 * IndexedUnsortedList - source file
 * Node - source file
 * README - this file


# COMPILING AND RUNNING:
 Ensure the four included files are in the same directory.
 Then, compile the driver file, ListTester, by inputting into the terminal:

 $ javac ListTester.java

 Once the file is compiled, the same directory should have the compiled java class files.
 Once this is verified, the tester file can be ran by inputting into the terminal:

 $ java ListTester

 This will output many testing scenarios to assure that the double linked list data structure is functioning as
 expected, such as being able to use the getter and setter methods and the iterator methods.

 For instance the tester could display what is expected when starting from an empty list, [], and then calling add(A) which would properly display:

 [A]

 The console output will however display around 9000+ test scenarios, chipping away at any uncertain test scenarios with certain methods on a double linked list data structure.

# PROGRAM DESIGN AND IMPORTANT CONCEPTS:

 This is the sort of information someone who really wants to
 understand your program - possibly to make future enhancements -
 would want to know.

 Explain the main concepts and organization of your program so that
 the reader can understand how your program works. This is not a repeat
 of javadoc comments or an exhaustive listing of all methods, but an
 explanation of the critical algorithms and object interactions that make
 up the program.

 Explain the main responsibilities of the classes and interfaces that make
 up the program. Explain how the classes work together to achieve the program
 goals. If there are critical algorithms that a user should understand, 
 explain them as well.
 
 If you were responsible for designing the program's classes and choosing
 how they work together, why did you design the program this way? What, if 
 anything, could be improved?

 The first concept to be aware of is viewable in the IndexUnsortedList.java file/the interface class.
 - This functions as the basic interface that the IUDoubleLinkedList.java file, the double linked list, implements, including its expected behaviors. The basic overview of this class is that it provides a framework within the double linked list, such as the expected proper output when adding, setting, getting, and removing, and the expected appropriate error handling when an issue propagates.

 The second concept is the Node class...

 The third concept is the IUDoubleLinkedList.java file, as mentioned above, which initially was built by taking the method stubs from the IndexUnsortedList.java interface class.
 - In here, we see the actual design of each constructor and methods functionality.
 Do be aware that I have provided numerous comments explaining my thought process when constructing the methods and constructors.
 There is also a secondary private class within this class that serves to function as a list iterator. This will be discussed later.
 - The instance variables first initialize **head** of type Node, with a generic type T included (this ensures any object can be stored which is the primary reason for this project) which will be used to identify where the beginning of a list begins, as well as a useful way to "translate" between index positions and actual numerical positions, since a linked-list unlike an arraylist does not have the benefit direct indexing. As mentioned, the Node is an object that holds element values.
  a **tail** of type Node, again with a generic type T included, 
  a **size** variable, 
  a **versionNumber** 

# TESTING:

 How did you test your program to be sure it works and meets all of the
 requirements? What was the testing strategy? What kinds of tests were run?
 Can your program handle bad input? Is your program  idiot-proof? How do you 
 know? What are the known issues / bugs remaining in your program?


# DISCUSSION:
 
 Discuss the issues you encountered during programming (development)
 and testing. What problems did you have? What did you have to research
 and learn on your own? What kinds of errors did you get? How did you 
 fix them?
 
 What parts of the project did you find challenging? Is there anything
 that finally "clicked" for you in the process of working on this project?