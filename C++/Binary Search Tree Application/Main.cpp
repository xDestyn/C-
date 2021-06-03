// Author: Omar Flores
// Class: CS 474 
// Project 3 - Set Calculator
#include <iostream>
#include <vector>
#include <unordered_set>
#include "BST.h"

std::vector<std::string> stringDuplicates(std::vector<std::string> strings) {
	std::vector<std::string> interVect; 
	std::unordered_set<std::string> s;

	// Uses unordered_set to find duplicates 
	for (int i = 0; i < strings.size(); ++i) {
		if (s.find(strings[i]) != s.end())
			interVect.push_back(strings[i]);
		else
			s.insert(strings[i]);
	}

	return interVect;
}

int main()
{
	// Set1 
	BST root;

	// Set2
	BST root2; 

	// Temporary set used for swapping
	BST temp;

	// Array going to be used to store elements for both Sets
	std::vector<std::string> treeOne;
	std::vector<std::string> treeTwo;
	std::vector<std::string> mergedTrees;
	std::vector<std::string> intersectTrees;

	// Stores info needed to process commands
	std::vector<std::string> arguments;

	// Stores user selection
	char userSelection = ' ';

	// Stores the whole command 
	std::string commandLine = " ";

	size_t pos = 0;

	// Stores every word in the command line
	std::string token;

	// Stores the state of the loop
	bool active = true;

	// Infinite loop until broken by user
	while (active)
	{

		std::cout << "Enter valid command to perform with valid arguments: " << std::endl;
		std::getline(std::cin, commandLine);
		std::cout << std::endl;

		// Delimeter used to split command line 
		std::string delimiter = " ";


		// Goes through every word in the string up until the delimiter
		while ((pos = commandLine.find(delimiter)) != std::string::npos) {
			token = commandLine.substr(0, pos);
			// Stores that word into vector
			arguments.push_back(token);
			// Erases that word from the command line string
			commandLine.erase(0, pos + delimiter.length());
		}

		// Checks to see if there's no extra argument
		if (arguments.size() == 0) {
			// If there is, make the command line char the userSelection
			for (auto& c : commandLine)
				c = toupper(c);

			userSelection = commandLine[0];
		}
			
		else
			// If there's not, arguments vector holds first argument of userSelection
			userSelection = toupper(arguments[0][0]);

		switch (userSelection)
		{
			// Erasing elements from Set1 
			case 'E':
				std::cout << "Erasing Set1 in progress..." << std::endl;
				root.~BST();
				if (root.m_getRoot() == NULL)
					std::cout << "Set has been erased." << std::endl;
				else
					std::cout << "Error: Set1 has not been erased." << std::endl;
				break;
			// Switching elements of Set1 and Set2
			case 'S':
				std::cout << "Switching Set1 and Set2's content..." << std::endl;
				temp = root;
				root = root2;
				root2 = temp;
				break;
			// Set1 is deep copied into Set2 
			case 'C': {
				BST copyTemp = root;
				root2 = copyTemp;
				break;
			}
			// Lists elements from Set1 and Set2. Breaks immediately. 
			case 'L':
				break;
			// Adding string elements in Set1
			case 'A':
				std::cout << "Adding element to S1... " << std::endl;
				root.m_Insert(commandLine);
				break;
			// Takes the union of Set1 and Set2 and stores the result in Set1
			case 'U':
				// Clearing the vectors intially
				treeOne.clear();
				treeTwo.clear();
				mergedTrees.clear();

				std::cout << "Merging all contents from Set1 and Set2 into Set1. Previous content will be lost..." << std::endl;
				std::cout << std::endl;
				treeOne = root.m_collectTreeOne();
				treeTwo = root2.m_collectTreeTwo();

				// Combining both vectors containing elements from Set1 and Set2 into one vector
				mergedTrees.reserve(treeOne.size() + treeTwo.size());
				mergedTrees.insert(mergedTrees.end(), treeOne.begin(), treeOne.end());
				mergedTrees.insert(mergedTrees.end(), treeTwo.begin(), treeTwo.end());

				// Clearing everything that was in Set1 originally after the merging of vectors
				root.~BST();

				// Inserting every element in the merged vector into the BST 
				for (auto s : mergedTrees) {
					root.m_Insert(s);
				}

				break;
			// Takes the intersect of Set1 and Set2 and stores the result in Set1 
			case'I':
				// Clearing the vectors and sets initially
				treeOne.clear();
				treeTwo.clear();
				mergedTrees.clear();
				intersectTrees.clear();

				std::cout << "Merging elements that are in both Sets. Previous content will be lost..." << std::endl;
				std::cout << std::endl;
				treeOne = root.m_collectTreeOne();
				treeTwo = root2.m_collectTreeTwo();

				// Combining both vectors containing elements from Set1 and Set2 into one vector
				mergedTrees.reserve(treeOne.size() + treeTwo.size());
				mergedTrees.insert(mergedTrees.end(), treeOne.begin(), treeOne.end());
				mergedTrees.insert(mergedTrees.end(), treeTwo.begin(), treeTwo.end());

				// Clearing everything that was in Set1 originally after the merging of vectors
				root.~BST();

				// Finding duplicates found from merged vector created by elements from Set1 and Set2
				intersectTrees = stringDuplicates(mergedTrees);

				// Add intersect elements into BST
				for (auto s : intersectTrees)
					root.m_Insert(s);

				break;
			// Listing the elements from Set1 and Set2 and exits the application
			case 'Q':
				std::cout << "Listing set contents... " << std::endl;
				std::cout << "Set 1: " << std::endl;
				root.m_Print();
				std::cout << "Set 2: " << std::endl;
				root2.m_Print();
				std::cout << "Exiting application..." << std::endl;
				exit(-1);
			// If command doesn't match our desired input. 
			default:
				std::cout << "Invalid command entered. Please retry again." << std::endl;
				break;
		}

		// Printing the contents of both sets
		std::cout << "Listing set contents... " << std::endl;
		std::cout << std::endl;
		std::cout << "Set 1: " << std::endl;
		root.m_Print();
		std::cout << "Set 2: " << std::endl;
		root2.m_Print();
		// Clears the vector and prepares it for the next command iteration
		arguments.clear();
		
	}

}