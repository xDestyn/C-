#include <iostream>
#include <string>
#include <fstream>
#include "Collection.h"
#include "LinkedList.h"
#include <unordered_map>
#include <sstream>
#include <vector>

// Enum class to be used for switch case
enum class operator_name {
	NEWID,
	COMBINE,
	COPY,
	HEAD,
	TAIL,
	ASSIGN,
	CHS,
	ADD,
	IF,
	HLT
};

// Union to help with multiple data types
union Value
{
	int num;
	LinkedList* root;
};

// Struct containing the union + helping 
struct HV {
	bool isNum;
	Value val;
} HashVal;

// Returning the correct operator based on the operatorName
operator_name convertToEnum(std::string const& operatorName) {
	if (operatorName == "NEWID") return operator_name::NEWID;
	if (operatorName == "COMBINE") return operator_name::COMBINE;
	if (operatorName == "COPY") return operator_name::COPY;
	if (operatorName == "HEAD") return operator_name::HEAD;
	if (operatorName == "TAIL") return operator_name::TAIL;
	if (operatorName == "ASSIGN") return operator_name::ASSIGN;
	if (operatorName == "CHS") return operator_name::CHS;
	if (operatorName == "ADD") return operator_name::ADD;
	if (operatorName == "IF") return operator_name::IF;
	if (operatorName == "HLT") return operator_name::HLT;
}

// Removes whitespaces from secondIdentifierName
void removeSpaces(std::string secondIdentifierName) {

	// Keeps track of non-space character count
	int count = 0;

	// Traverse the given string. If current char is not space
	// Then place it at index 'count++'
	for (int i = 0; secondIdentifierName[i]; i++) {
		if (secondIdentifierName[i] != ' ')
			secondIdentifierName[count++] = secondIdentifierName[i];
	}

	secondIdentifierName[count] = '\0';

}

// Returns if the second identifier is an integer
bool checkNum(const std::string identifierName) {
	std::vector<int> myVec;
	for (const char& x : identifierName) {
		myVec.push_back(x);
	}

	for (int x : myVec) {
		if (x <= 0 || x > 0)
			continue;
		else
			return false;
	}

	return true;
}


// Performs NEWID command 
void doNEWID(std::unordered_map<std::string, HV>& myHash, std::string identifierName) {

	// Sets the identifier to an empty list
	myHash[identifierName].isNum = false;
	myHash[identifierName].val.root = nullptr;

	//myHash[identifierName] = hashVal;

}

// Performs COMBINE command
void doCOMBINE(std::unordered_map<std::string, HV>& myHash, std::string identifierName, std::string secondIdentifierName) {
	// Returns iterator if identifier names are in hashmap
	std::unordered_map<std::string, HV>::const_iterator got = myHash.find(identifierName);
	std::unordered_map<std::string, HV>::const_iterator got2 = myHash.find(secondIdentifierName);

	// If first identifier is found
	if (got != myHash.end()) {
		// If second identifier is found
		if (got2 != myHash.end()) {
			// If the list of the second identifier has not been initialize, make a new list
			if (myHash[secondIdentifierName].val.root == nullptr) {
				std::cout << "\nCreating list: " + secondIdentifierName << std::endl;
				myHash[secondIdentifierName].val.root = new LinkedList();
			}
			// Check to see if the first identifier's value is an Integer
			if (myHash[identifierName].isNum) {
				// Prepend that integer to list identifier
				myHash[secondIdentifierName].val.root->m_Prepend(std::to_string(myHash[identifierName].val.num));
				std::cout << secondIdentifierName + " identifier contains: [ ";
				myHash[secondIdentifierName].val.root->m_PrintNodes();
				std::cout << "]" << std::endl;
			}
			// First identifier is a LinkedList
			else {
				std::cout << '\n';
				std::cout << identifierName + " identifier contains: [ ";

				myHash[identifierName].val.root->m_PrintSPL();
				std::cout << "]" << std::endl;

				// If first identifier's value is a list, prepend the first element into the second identifier's list
				std::string firstElem = myHash[identifierName].val.root->m_GetHead()->getData();
				myHash[secondIdentifierName].val.root->m_Prepend(firstElem);

				std::cout << secondIdentifierName + " identifier contains: [ ";
				myHash[secondIdentifierName].val.root->m_PrintNodes();
				std::cout << "]" << std::endl;
			}
		}
		else {
			std::cout << '\n';
			std::cout << secondIdentifierName + " identifier key does not exist..." << std::endl;
			std::cout << "Proceeding to next instruction..." << std::endl;
		}
	}
	else {
		std::cout << '\n';
		std::cout << identifierName + " identifier key does not exist..." << std::endl;
		std::cout << "Proceeding to next instruction..." << std::endl;
	}
}

// Performs HEAD command
void doHEAD(std::unordered_map<std::string, HV>& myHash, std::string identifierName, std::string secondIdentifierName) {
	// Returns iterator if identifier names are in hashmap
	std::unordered_map<std::string, HV>::const_iterator got = myHash.find(identifierName);
	std::unordered_map<std::string, HV>::const_iterator got2 = myHash.find(secondIdentifierName);

	std::string firstElem = "";
	// Check to see if identifier is in hash map
	if (got != myHash.end()) {
		// Check to see if second identifier is in map
		if (got2 != myHash.end()) {
			// Check to see if first identifier in map is an integer
			if (myHash[identifierName].isNum == true) {
				int firstElem = myHash[identifierName].val.num;
				std::cout << "First element in " + identifierName + ": " + std::to_string(firstElem) << std::endl;

				// Store the int value to int identifier
				myHash[secondIdentifierName].isNum = true;
				myHash[secondIdentifierName].val.num = firstElem;
			}
			// First identifier is a list
			else {
				LinkedList* list = myHash[identifierName].val.root;
				LinkedList* ll = nullptr;
				if (list->m_GetLength() > 1) {
					ll = new LinkedList(list->m_GetHead(), 1);
				}
				else {
					ll = new LinkedList();
				}

				myHash[secondIdentifierName].val.root = ll;
			}
		}
	}
}

// Performs TAIL command
void doTAIL(std::unordered_map<std::string, HV>& myHash, std::string identifierName, std::string secondIdentifierName) {
	// Returns iterator if identifier names are in hashmap
	std::unordered_map<std::string, HV>::const_iterator got = myHash.find(identifierName);
	std::unordered_map<std::string, HV>::const_iterator got2 = myHash.find(secondIdentifierName);

	std::string firstElem = "";
	// Check to see if identifier is in hash map
	if (got != myHash.end()) {
		// Check to see if second identifier is in map
		if (got2 != myHash.end()) {
			// Check to see if first identifier in map is an integer
			if (myHash[identifierName].isNum == true) {
				firstElem = myHash[identifierName].val.num;
				int firstElem = myHash[identifierName].val.num;
				std::cout << "First element in " + identifierName + ": " + std::to_string(firstElem) << std::endl;

				// Store the int value to int identifier
				myHash[secondIdentifierName].isNum = true;
				myHash[secondIdentifierName].val.num = firstElem;
			}
			// First identifier is a list
			else {
				LinkedList* list = myHash[identifierName].val.root;
				LinkedList* ll = nullptr;
				if (list->m_GetLength() > 1) {
					int size = list->m_GetLength();
					auto node = list->m_GetHead();
					while (node && node->getNext())
					{
						node = node->getNext();
						size--;
					}
					ll = new LinkedList(node, size);
				}
				else {
					ll = new LinkedList();
				}

				myHash[secondIdentifierName].val.root = ll;
				std::cout << secondIdentifierName + " contains: [ ";
				myHash[secondIdentifierName].val.root->m_PrintNodes();
				std::cout << " ]" << std::endl;
			}
		}
	}
}

// Performs TAIL command
void doCOPY(std::unordered_map<std::string, HV>& myHash, std::string identifierName, std::string secondIdentifierName) {
	// Returns iterator if identifier names are in hashmap
	std::unordered_map<std::string, HV>::const_iterator got = myHash.find(identifierName);
	std::unordered_map<std::string, HV>::const_iterator got2 = myHash.find(secondIdentifierName);

	// Check to see if identifier is in hash map
	if (got != myHash.end()) {
		// Check to see if second identifier is in map
		if (got2 != myHash.end()) {
			// Check to see if first identifier in map is an integer
			if (myHash[identifierName].isNum == true) {
				std::cout << "error: can't copy number" << std::endl;
			}
			// First identifier is a list
			else {
				LinkedList* list = myHash[identifierName].val.root;
				LinkedList* ll = new LinkedList();

				// copy all nodes except last one
				auto node = list->m_GetHead();
				while (node && node->getNext()) {
					ll->m_Append(node->getData());
				}
				// copy last node
				if (node) {
					ll->m_Append(node->getData());
				}

				myHash[secondIdentifierName].val.root = ll;
			}
		}
	}
}


// Performs CHS command
void doCHS(std::unordered_map<std::string, HV>& myHash, std::string identifierName) {
	// Returns iterator if identifier names are in hashmap
	std::unordered_map<std::string, HV>::const_iterator got = myHash.find(identifierName);

	// Check if identifier name exists in hashmap 
	if (got != myHash.end()) {
		// Check to see if value is an integer
		if (myHash[identifierName].isNum) {
			std::cout << "Before CHS: " << std::to_string(myHash[identifierName].val.num) << std::endl;
			// Negate the sign
			myHash[identifierName].val.num = myHash[identifierName].val.num * -1;
			std::cout << "After CHS: " << std::to_string(myHash[identifierName].val.num) << std::endl;
		}
		else {
			std::cout << "Identifier is not bound to an integer.\n Proceeding to next isntruction..." << std::endl;
		}
	}
	else {
		std::cout << "Key: " + identifierName + " does not exist..." << std::endl;
		std::cout << "Proceeding to next isntruction..." << std::endl;
	}
}

// Performs ASSIGN command
void doASSIGN(std::unordered_map<std::string, HV>& myHash, std::string identifierName, std::string secondIdentifierName) {
	// Returns iterator if identifier names are in hashmap
	std::unordered_map<std::string, HV>::const_iterator got = myHash.find(identifierName);

	// Stores boolean int flag
	bool isInt = false;

	// Checks to see if identifier name exists in hashmap
	if (got != myHash.end()) {
		// Checks if it's an int
		if (checkNum(secondIdentifierName)) {
			// If key exists, then assign int value
			myHash[identifierName].isNum = true;
			// Object from the class stringstream
			std::stringstream ss(secondIdentifierName);

			// Object has int value and stream it to integer variable
			int intValue = 0;
			ss >> intValue;

			// Store the int value to int identifier
			myHash[identifierName].val.num = intValue;
			std::cout << "Stored at " + identifierName + " = " + std::to_string(myHash[identifierName].val.num) << std::endl;
		}
		else {
			std::cout << "Element trying to be assigned is not an integer." << std::endl;
			std::cout << "Proceeding to next instruction..." << std::endl;
			return;
		}
	}
	else {
		std::cout << "No key exists..." << std::endl;
		std::cout << "Proceeding to next instruction" << std::endl;
		return;
	}

}
// Performs ADD command
void doADD(std::unordered_map<std::string, HV>& myHash, std::string identifierName, std::string secondIdentifierName) {
	// Returns iterator if identifier names are in hashmap
	std::unordered_map<std::string, HV>::const_iterator got = myHash.find(identifierName);
	std::unordered_map<std::string, HV>::const_iterator got2 = myHash.find(secondIdentifierName);

	// Check if first identifier exists
	if (got != myHash.end()) {
		// Check if second identifier exists 
		if (got2 != myHash.end()) {
			// Check to see if both identifiers are integers 
			if (myHash[identifierName].isNum && myHash[secondIdentifierName].isNum) {
				// Adds the integers together and stores result in first identifier
				myHash[identifierName].val.num += myHash[secondIdentifierName].val.num;
				std::cout << identifierName + " stores: " + std::to_string(myHash[identifierName].val.num) << std::endl;
			}
			else {
				std::cout << "Both identifiers need to be integers...\nProceeding to next instruction..." << std::endl;
				return;
			}
		}
		else {
			std::cout << secondIdentifierName + " identifier does not exist..." << std::endl;
			std::cout << "Proceeding to next instruction..." << std::endl;
			return;
		}
	}
	else {
		std::cout << identifierName + " identifier does not exist..." << std::endl;
		std::cout << "Proceeding to next instruction..." << std::endl;
		return;
	}
}

// Performs IF command
int doIF(std::unordered_map<std::string, HV>& myHash, std::string identifierName, std::string secondIdentifierName) {
	if (myHash[identifierName].val.root == nullptr || myHash[identifierName].val.num == 0) {

		// Object from the class stringstream
		std::stringstream ss(secondIdentifierName);

		// Object has int value and stream it to integer variable
		int intValue = 0;
		ss >> intValue;

		return intValue;
	}
	return -1;
}

// Function that will handle all the commands + call sub functions for proper command
void doCommand(std::unordered_map<std::string, HV>& myHash, std::string instructionText, int& ifNum, bool& ifHLT) {

	int numSpaces = 0;

	// Count number of spaces in instructionText
	for (int i = 0; i < instructionText.length(); i++) {
		if (instructionText[i] == ' ')
			numSpaces++;
	}

	// Returns the index of the last white space found
	int lastSpaceIndex = instructionText.find_last_of(' ');

	// Gets the last word from the whole instructionText
	std::string operatorName = instructionText.substr(++lastSpaceIndex);

	// Gets the identifier name
	std::string identifierName = instructionText.substr(0, instructionText.find(" "));

	// Gets the second identifier name
	std::string secondIdentifierName = "";


	// Second identifier exists for given command
	if (numSpaces > 1) {

		int firstWhiteSpace = instructionText.find(" ") + 1;
		int count = firstWhiteSpace;
		while (instructionText.at(count) != ' ') {
			secondIdentifierName.push_back(instructionText.at(count));
			count++;
		}
		//secondIdentifierName = instructionText.substr(firstWhiteSpace, instructionText.find(" ") + 1);
		//int secondWhiteSpace = instructionText.find(" ", firstWhiteSpace + 1);
		//secondIdentifierName = instructionText.substr(firstWhiteSpace, instructionText.find(" ", firstWhiteSpace + 2));
		//secondIdentifierName = instructionText.substr(++secondWhiteSpace);
		// Removes the whitespaces from the secondIdentifierName
		removeSpaces(secondIdentifierName);
	}



	//std::cout << secondIdentifierName << std::endl;

	// Get an enum class using operatorName to use switch statement
	switch (convertToEnum(operatorName)) {
	case operator_name::NEWID:
		doNEWID(myHash, identifierName);
		break;
	case operator_name::COMBINE:
		doCOMBINE(myHash, identifierName, secondIdentifierName);
		break;
	case operator_name::COPY:
		doCOPY(myHash, identifierName, secondIdentifierName);
		break;
	case operator_name::HEAD:
		doHEAD(myHash, identifierName, secondIdentifierName);
		break;
	case operator_name::TAIL:
		doTAIL(myHash, identifierName, secondIdentifierName);
		break;
	case operator_name::ASSIGN:
		doASSIGN(myHash, identifierName, secondIdentifierName);
		break;
	case operator_name::CHS:
		doCHS(myHash, identifierName);
		break;
	case operator_name::ADD:
		doADD(myHash, identifierName, secondIdentifierName);
		break;
	case operator_name::IF:
		ifNum = doIF(myHash, identifierName, secondIdentifierName);
		break;
	case operator_name::HLT:
		ifHLT = true;
		break;
	default:
		break;
	}
}

int main()
{
	int lineNumber = 0;
	std::cout << "Enter file containing SPLI program: " << std::endl;

	// Get the file name being used from the user
	std::string fileName;

	// Stores file length
	int fileLength = 0;

	// Stores line from file
	std::string instructionText;

	// Gets the file name
	getline(std::cin, fileName);

	// Root for List
	LinkedList* root = new LinkedList();

	// Hash Map used to store identifiers w/ their values
	std::unordered_map<std::string, HV> myHash;

	// Union instance
	//HV hashValue; 
	/*hv.isNum = true;
	hv.val.num = 10;*/
	//hv.isNum = false;
	//hv.val.root = new LinkedList();

	std::ifstream myFile(fileName);

	// Check if file exists / counts the number of lines in the text file
	if (myFile.is_open()) {
		//std::cout << "First loop: " << std::endl;
		while (getline(myFile, instructionText)) {
			fileLength++;

			//std::cout << instructionText << std::endl;

			//break;
		}

		/*std::cout << "Second loop: " << std::endl;
		while (getline(myFile, instructionText)) {

			std::cout << instructionText << std::endl;
		}*/

		myFile.close();
	}
	else {
		std::cout << "Unable to open file..." << std::endl;
	}

	// Opening file again 
	std::ifstream myFile2(fileName);

	// Check if file exists 
	if (myFile2.is_open()) {

		// Infinite loop for continous execution of commands
		while (true && lineNumber < fileLength) {
			std::cout << "\nEnter desired command: " << std::endl;

			// Gets user input of command
			char command = ' ';
			std::cin >> command;

			// Uppercases the character if not already
			command = toupper(command);

			// Number for if
			int ifNum = 0;

			// Count for if
			int ifCount = 0;

			// bool for HLT
			bool ifHLT = false;

			switch (command) {
			case 'L':
				while (getline(myFile2, instructionText) || lineNumber < fileLength) {
					//std::cout << instructionText << std::endl;
					root->m_Append(instructionText);

					std::cout << std::endl;
					std::cout << "Printing current nodes: " << std::endl;
					root->m_PrintSPL();
					std::cout << std::endl;
					// Perform command with given instruction
					doCommand(myHash, instructionText, ifNum, ifHLT);
					lineNumber++;

					if (ifHLT) {
						std::cout << "HLT encountered. Exiting application..." << std::endl;
						return 0;
					}


					// If If command was called and satifies requirements
					if (ifNum > 0) {
						// Check boundary cases
						if (ifNum < 0 || (ifNum + lineNumber) > fileLength) {
							std::cout << "Line number must be positive and within range of length of file..." << std::endl;
							std::cout << "Proceeding to next instruction..." << std::endl;
						}
						else {
							while (getline(myFile2, instructionText) && ifCount < ifNum && lineNumber < fileLength) {
								ifCount++;
								lineNumber++;
							}
						}
					}

					break;
				}
				break;
			case 'A':
				while (getline(myFile2, instructionText) || lineNumber < fileLength) {
					//std::cout << instructionText << std::endl;
					root->m_Append(instructionText);

					std::cout << std::endl;
					std::cout << "Printing current nodes: " << std::endl;
					root->m_PrintSPL();
					std::cout << std::endl;
					// Perform command with given instruction
					doCommand(myHash, instructionText, ifNum, ifHLT);
					lineNumber++;

					if (ifHLT) {
						std::cout << "HLT encountered. Exiting application..." << std::endl;
						return 0;
					}

					// If If command was called and satifies requirements
					if (ifNum > 0) {
						// Check boundary cases
						if (ifNum < 0 || (ifNum + lineNumber) > fileLength) {
							std::cout << "Line number must be positive and within range of length of file..." << std::endl;
							std::cout << "Proceeding to next instruction..." << std::endl;
						}
						else {
							while (getline(myFile2, instructionText) && ifCount < ifNum && lineNumber < fileLength) {
								ifCount++;
								lineNumber++;
							}
						}
					}
				}
				break;
			case 'Q':
				std::cout << "Exiting application..." << std::endl;
				return 0;
				break;
			default:
				break;
			}
		}

		std::cout << std::endl;
		std::cout << "File has finished..." << std::endl;
		std::cout << "Exiting..." << std::endl;

		myFile2.close();
		return 0;
	}
	else {
		std::cout << "unable to open file..." << std::endl;
	}

	return 0;
}