********************************
* Double-Linked List with Tester
* CS 221
* 14 November 2024
* Kyle Truschel and CS221-3 F24
******************************** 

# OVERVIEW:
This program functions to serve as a creator of a double linked list data structure, and has the ability to hold any type of object. It also includes a tester file that assures the proper output of said double linked data structure.

# INCLUDED FILES:
* IUDoubleLinkedList - source file, creates a double linked list
* ListTester - driver file and source file, test scenario file
* IndexedUnsortedList - source file, interface for IUDoubleLinkedList
* Node - source file, necessary for IUDoubleLinkedList functionality
* README - this file, instructions, and concepts

# COMPILING AND RUNNING:
Ensure the five included files are in the same directory.
Then, compile the driver file, ListTester, by inputting into the terminal:
- Note: The $ represents the console start line

1. $ javac ListTester.java

Once the file is compiled, the same directory should have the compiled java class files.
Once this is verified, the tester file can be ran by inputting into the terminal:

2. $ java ListTester

This will output many testing scenarios to assure that the double linked list data structure is functioning as
expected, such as being able to use the getter and setter methods and the iterator methods.

For instance the tester could display what is expected when starting from an empty list, [], and then calling add(A) which would properly display:

[A]

The console output will however display around 9000+ test scenarios, chipping away at any uncertain test scenarios with certain methods on a double linked list data structure.

# PROGRAM DESIGN AND IMPORTANT CONCEPTS:
1. The first concept to be aware of is viewable in the IndexUnsortedList.java file/the interface class.
- This functions as the basic interface that the IUDoubleLinkedList.java file, the double linked list, implements, including its expected behaviors. The basic overview of this class is that it provides a framework within the double linked list, such as the expected proper output when adding, setting, getting, and removing, and the expected appropriate error handling when an issue propagates.

2. The second concept is the Node.java/Node class. This is a critical concept for the "links" for the double linked list.
- The Node itself serves as an object that contains elements
- The Node class itself has the instance variables of element, which itself is a generic type to hold any object, a nextNode, and previousNode.
- The Node class has two constructors, one that creates an initially empty Node with an element assigned, and another constructor that still creates a new node, but additionally takes in a known/existing nextNode in the double linked list.
- The Node methods consist of **getElement**, simply returns the element, **setElement**, which modifies the generic type inside a Node, **getNextNode**, which returns an existing nextNode in the structure (emphasis on existing, otherwise calling this on no existing next value will result in a null pointer exception), **setNextNode**, which is used to update the double linked list and will be important for ensuring the "links" in the double linked list are pointing in the proper direction, **getPreviousNode**, which similarly to getNextNode returns an existing previousNode (again emphasis on existing to avoid a null pointer exception), and finally a **setPreviousNode** which similar to setNextNode will allow the "links" in the double linked list to be properly updated in the reverse direction, and is critical for the double linked list to function properly.

3. The third concept is the IUDoubleLinkedList.java file, as mentioned above, which initially was built by taking the method stubs from the IndexUnsortedList.java interface class.
In here, we see the actual design of each constructor and methods functionality.
Do be aware that I have provided numerous comments explaining my thought process when constructing the methods and constructors.
There is also a secondary private class within this class that serves to function as a list iterator. This will be discussed later.

The instance variables consist of:
- A **head** of type Node and functions as the start position of a new double linked list. It is a useful way to "translate" between index positions and actual numerical positions, since a linked-list unlike an arraylist does not have the benefit direct indexing. As mentioned, the Node is an object that holds generic type elements (any objects).
- A **tail** of type Node and functions as the end position of a new double linked list. Note that on a brand newly created list, the head and tail will both be the same values. Likewise, it is again a useful way to track and translate between index positions and numerical positions.
- A **size** variable, which helps track the length of the double linked list. This will be incremented anytime a modifying method, like add, are called.
- A **versionNumber**, which helps the later discussed ListIterator by comparing any changes made, to avoid conflict between modifying methods.

There is a single constructor, which serves to create a new list. It initializes the head and tail to null, the size to 0, and the version number to 0. No input parameters.

The methods are completed expected behavior from the interface, in addition to a function toString.
- The addToFront, addToRear, add(element), addAfter, and add(index, element) all involve object creation of a Node to be added to the list and all have their size and versionNumber variables incremented.
Specifically, the add(element) method simply calls addToRear, since their behavior are functionally identical.
The add methods that involve an index parameter utilize a for loop, since the index value is known, whereas the rest utilize while loops.
- The removeFirst, removeLast, remove(element), remove(index) all involve size decrementation, versionNumber incrementation, and a returnValue of the removed element.
Specifically, the remove(index) has a bounds check, to ensure proper index searching, and a for loop since the index is known, whereas on the contrary the remove(element) uses a while loop.
- Set takes in an int index and a generic type element. First performs a bounds check, since an index is used, and simply calls the setElement method onto a known element.
- Get takes in an int index. Also performs a bounds check, and returns a returnValue of the Node's index getElement method.
- indexOf takes in a generic type element and utilizes a currentIndex count to incremenet each iteration of the while loop. If no element was found, a -1 returns, otherwise the proper index of the found element returns.
- first, calls getElement on the head
- last, calls getElement on the tail
- contains, calls indexOf and returns true if greater than -1 (since indexOf returns -1 if no element was found)
- isEmpty, returns true if size is 0
- size, returns the size
- toString, utilizes the StringBuilder to properly output the contents of a double linked list

4. The fourth concept is the List Iterator, which itself is a private class called DLLIterator inside the IUDoubleLinkedList class that iself implements from the ListIterator java class. Also utilizes generic types to ensure any object can be held in the double linked list.

The instance variables consist of:
- A **nextNode** which is the key component of an iterator, since it is the bare minimum to be able to advance throughout a list.
- A **nextIndex** so the iterator can keep track of where it is in the list.
- A **iterVersionNumber** to be compared with the previously mentioned versionNumber.
- A **lastReturnedNode** which will allow removal of next and previous methods.

Two constructors exist that creates a brand new iterator at the beginning, and creating an iterator at a given index.

The methods include the expected classic methods from the Iterator class, as well functionality for moving backwards in a list. These methods include:
- hasNext, which returns true if nextNode is not null, meaning the iterator is not at the end of the next. Also compares version numbers to ensure concurrency while moving throughout the list.
- next, which first checks to ensure hasNext was true, and stores the next Node in the double linked list, which can then be used for removal or add methods.
- hasPrevious, which returns true if the nextNode is not the head, since nothing can be previous to the head.
- previous, which similarly checks if hasPrevious was true, and stores the previous Node in the double linked list, which can then be used for removal or add methods.
- nextIndex, which returns the nextIndex as long as no concurrency issues exist from the version number check
- previousIndex, which returns the nextIndex - 1, the literal previous index, so long as no concurrency issues exist from the version check.
- remove, first requires a concurrency check, then if a lastReturnedNode exists. Consists of an edge case of checking at the head, tail, and in the middle. After completion, the lastReturnedNode is set back to null to prevent conflicts with the iterator.
- set, takes in a generic type element, e, performs a concurency check, and simply sets the lastReturnedNode using the setElement method.
- add, takes in a generic type element, e, performs a concurrency check, and handles edge cases of adding at the head, where addToFront is called, the tail, where addToRear is called, and in the middle.

5. The final concept to understand is the ListTester.java file, which serves the role as the driver class as well as outputting the possible scenarios when testing the methods mentioned above on a double linked list data structure.

There are many different test scenarios to handle an expected output, such as addToFront(A) would throw no issues on an empty list, but if add(3, X) on a single element list, such as [A] would be expected to propagate and error handling, such as IndexOutOfBounds, since there would be no index 3 on a single element list; that would be inaccessible.

The List Tester also have cases for using the List Iterator, such as starting at a specific index, say 1 on a created double linked list [A, B], and expected next() to be valid and calling set(X) would also be valid, since this would first be allowed to call next() on index 1 and "B" would be set to "X" without any issues.
An example of an expected invalid outcome would be trying to call next() since this would be accessing null, and the hasNext method would have already returned false; this would be inaccessible.

6. Much of the concept design was to emphasize the reduction of code duplication by calling already well working methods, such as add(index, element) since if the index is 0, the head, simply call addToFront.

Perhaps some of the methods could be improved in areas that I missed opportunities to avoid code duplication, such as the index checks. Those could be their own separate method.

I believe that using a double linked list is a great alternative to a single linked list because of the ability to move backwards. This helped me understand the penalty if using removeLast on a single linked list, since this requires a shift and copy because there is no fast way to unlink the tail, whereas in a double linked list, the tail can simply call getPreviousNode and the old tail will be unlinked from the list, allowing the java garbage collector to collect the old tail; this results in an O(1) runtime operation as opposed to in a single linked list runtime of O(n).

There is also not as much of a burden when shifting like with an array list. An example of such a burden would be removeFirst on an array list, since the entire array would need to be copied, whereas in a double linked list there is already a position for head, which removeFirst() would simply call getNextNode to unlink the old head's position, and let the java garbage collector reclaim the old head's memory; this goes from an original O(n) operation in an array list to a O(1) operation in a double linked list.

However, the double linked list does come at a cost of more memory usage, since more variables must be tracked.

# TESTING:
As a part of this program, and the concept of learning test driven development, I utilized a List Tester which outlines several scenarios while creating and modifying a double linked list.
As mentioned in part 5 of the concepts, some examples of these test scenarios would ensure that in fact, an empty list can be created and calling add(A) would throw no errors.

In addition, nearly every single method that could have bad input for their parameters can react by throwing an appropriate exception, such as an IndexOutOfBounds error.

There could possible be known issues when modifying a double linked list of size 4, since the List Tester only handles scenarios of list 3.

# DISCUSSION:
Overall, I found creating a double linked list made more sense than a single linked list. Sure, there was more steps to link the Nodes, but the ability to move backwards, with getPreviousNode and setPreviousNode, made it very easy to comprehend when drawing out the steps.

The method that took the longest to conceptualize was the add(index, element) since I had first thought we were adding AFTER the index, instead of adding simply at the index, and shifting the original element right. I helped visualize this by making a comment starting with [A, B, C, D, E] and add(X, 3) would mean adding X between C and D.

I think the most helpful aspect was my numerous comments I displayed all over the code, which helped me walk through the steps even if I did not retain the concepts on the first introduction in class.

The List Tester class actually was the least difficult portion, since it involved a multitude of copying and pasting different test scenarios, and I just had to make sure I was adjusting the appropriate test cases.