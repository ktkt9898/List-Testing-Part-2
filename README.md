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

 The second concept is the Node.java/Node class. This is a critical concept for the "links" for the double linked list.

 - The Node itself serves as an object that contains elements
 - The Node class itself has the instance variables of element, which itself is a generic type to hold any object, a nextNode, and previousNode.
 - The Node class has two constructors, one that creates an initially empty Node with an element assigned, and another constructor that still creates a new node, but additionally takes in a known/existing nextNode in the double linked list.
 - The Node methods consist of **getElement**, simply returns the element, **setElement**, which modifies the generic type inside a Node, **getNextNode**, which returns an existing nextNode in the structure (emphasis on existing, otherwise calling this on no existing next value will result in a null pointer exception), **setNextNode**, which is used to update the double linked list and will be important for ensuring the "links" in the double linked list are pointing in the proper direction, **getPreviousNode**, which similarly to getNextNode returns an existing previousNode (again emphasis on existing to avoid a null pointer exception), and finally a **setPreviousNode** which similar to setNextNode will allow the "links" in the double linked list to be properly updated in the reverse direction, and is critical for the double linked list to function properly.

 The third concept is the IUDoubleLinkedList.java file, as mentioned above, which initially was built by taking the method stubs from the IndexUnsortedList.java interface class.
 In here, we see the actual design of each constructor and methods functionality.
 Do be aware that I have provided numerous comments explaining my thought process when constructing the methods and constructors.
 There is also a secondary private class within this class that serves to function as a list iterator. This will be discussed later.

The instance variables consist of:
 - A **head** of type Node and functions as the start position of a new double linked list. It is a useful way to "translate" between index positions and actual numerical positions, since a linked-list unlike an arraylist does not have the benefit direct indexing. As mentioned, the Node is an object that holds generic type elements (any objects).
 - A **tail** of type Node and functions as the end position of a new double linked list. Note that on a brand newly created list, the head and tail will both be the same values. Likewise, it is again a useful way to track and translate between index positions and numerical positions.
 - A **size** variable, which helps track the length of the double linked list. This will be incremented anytime a modifying method, like add, are called.
 - A **versionNumber**, which helps the later discussed ListIterator by comparing any changes made, to avoid conflict between modifying methods.

There is a single constructor, which serves to create a new list. It initializes the head and tail to null, the size to 0, and the version number to 0. No input parameters.

The methods are completed expected behavior from the interface, in addition to a function toString. The methods in detail are:
- addToFront, takes in an generic type element paramter and first creates a Node object and its first check is isEmpty (explained in detail later), which in brief, if returns true, then the list is brand new and the new Node object becomes the head and the tail. 
Otherwise, if the list has contents, the newNode is set to become the head , the front, with setNextNode and the old head calls setPrevious to complete the double link. 
Lastly, the head is assigned to be the newNode, the size is incremented, and the versionNumber is incremented.

- addToRear, takes in an generic type element, and like all add methods, creates a Node object and similarly calls isEmpty, which if true, assigns the Node to become the head and the tail.
Otherwise, the position in the list is indicated from a targetNode, different from the newly created Node, to point to the tail and the setNextNode method is called with the new Node passed in.
Next, the new Node calls setPreviousNode onto the old tail, completing the link.
Lastly, size is incremented and versionNumber is incremented.

- add, to avoid code duplication, this method essential does the exact same function as addToRear, and simply takes in an element paramter and calls addToRear.
There is no incrementation of size or versionNumber, since addToRear handles that function.

- addAfter, takes in an generic type element paramter and generic type a target to be seeked and input the element after.
First sets up a position in the list with a targetNode that points at the head.
Next, a while loop is entered to continously call getNextNode onto targetNode so long as targetNode is not null and it does not equal the original generic type element parameter.
If targetNode is null, the end of the list was reached and no element was found, thus a NoSuchElementException is thrown.
Otherwise, a new Node is created and the setNextNode is called upon the targetNode's getNextNode call. The new Node has its setPreviousNode to the targetNode and the targetNode has its nextNode set to the new Node. A final conditional check is performed so that if the new Node's getNextNode is null, then the tail will become the newNode, otherwise setPreviousNode is called to avoid a null pointer exception.

- add, takes in an index and a generic type element. The first check ensures that the index is in range, so not a negative value, and not greater than size (cannot be greater than size because this would access outside of the link) and if true, will throw an IndexOutOfBoundsException.
Otherwise, like all add methods, will create a new Node. Then, check if the index paramter was 0, which would mean adding to front. To avoid code duplication, this simply calls the previously mentioned addToFront method.
Likewise, if the first conditonal check passes, then the index is compared to the size method (mentioned later). If so, this is the same as adding to the end of the list, and to avoid code duplication, the addToRear method is called.
Lastly, if both these conditional checks pass, this would be the same as adding in the middle. This first begins by creating a new Node and entering a for loop, since we know the exact length to go to, by the index parameter. We first set our position with a targetNode to start at the head. The loop continuously calls getNextNode onto the targetNode and goes up to but not including index - 1, since we will eventually need to temporarily store an after index Node. (This method is similar to set, only that it does not overwrite the element at the index, but rather inserts it at the index and shifts the element to the right). An appropriate variable named afterIndexNode retrieves the targetNode's getNextNode. Lastly the connections are made by setting the targetNode's nextNode to be the new Node, the new Node's previous to the targetNode, the new Node's nextNode to the afterIndexNode, again this is would have been the targetNode's original link, and the afterIndexNode calls setPreviousNode to the new Node. 
An example in which we have a list [A, B, C, D, E] and we want to call add(3, X), we would account index - 1, or 3 - 1, to become 2 which the loop would go up to index 1, B, and call getNextNode to retrieve C. The afterIndexNode would have C call getNextNode to retrieve D, and we would insert element "X" in between C and D, so the final output is: [A, B, C, X, D, E].

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