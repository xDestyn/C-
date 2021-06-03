#include "LinkedList.h"
#include <iostream>

// Node default constructor
LinkedList::Node::Node() {
	LinkedList::Node::pData = "";
	LinkedList::Node::pNext = nullptr;
}

// Node Parametrized constructor
LinkedList::Node::Node(Node* next, const std::string& argData) {
	LinkedList::Node::pData = argData;
	LinkedList::Node::pNext = next;
}

// Don't have to do anything in destructor of Node class as nothing is being created in heap
LinkedList::Node::~Node() {

}

// Returns the data
std::string LinkedList::Node::getData() {
	return LinkedList::Node::pData;

}

// Returns the next node
LinkedList::Node* LinkedList::Node::getNext() {
	return LinkedList::Node::pNext;
}

// Set the next node
void LinkedList::Node::setNext(LinkedList::Node* nextNode) {
	LinkedList::Node::pNext = nextNode;
}

// LinkedList Constructors
LinkedList::LinkedList() {
	m_Root = nullptr;
	m_Length = 0;
}

LinkedList::LinkedList(Node* node, int size) {
	m_Root = node;
	m_Length = size;
}


// LinkedList Destructor
LinkedList::~LinkedList() {
	// If head is not empty
	if (LinkedList::m_Root != nullptr) {
		LinkedList::Node* currNode = LinkedList::m_Root;
		while (currNode->getNext() != nullptr) {
			delete currNode;
			currNode = currNode->getNext();
		}
	}
}

// LinkedList check for Empty
bool LinkedList::isEmpty() const {
	return m_Length == 0;
}

// LinkedList return list length
int LinkedList::m_GetLength() const {
	return m_Length;
}

// LinkedList Prepend 
void LinkedList::m_Prepend(const std::string& data) {
	// Create new node to contain given data
	LinkedList::Node* newNode = new LinkedList::Node(LinkedList::m_Root, data);

	// Next node must be the old head
	LinkedList::m_Root = newNode;

	// Increase length of linked list
	LinkedList::m_Length++;
}

void LinkedList::m_Append(const std::string& data) {
	// Traverse until the end of the linked list
	// If list is empty, prepend
	if (LinkedList::m_Root == nullptr)
		LinkedList::m_Prepend(data);
	else {
		LinkedList::Node* currNode = LinkedList::m_Root;
		while (currNode->getNext() != nullptr)
			currNode = currNode->getNext();

		// Instantiate a new node with given data
		LinkedList::Node* newNode = new LinkedList::Node(nullptr, data);

		// Set the last node's next pointer to the newly created node
		currNode->setNext(newNode);
	}

	// Increment length of list
	LinkedList::m_Length++;
}

// Print the SPL nodes in the LinkedList
void LinkedList::m_PrintSPL() {
	LinkedList::Node* currNode;
	currNode = LinkedList::m_Root;

	while (currNode != nullptr) {
		std::cout << currNode->getData() << " " << std::endl;
		currNode = currNode->getNext();
	}
}

// Print the value nodes in the LinkedList
void LinkedList::m_PrintNodes() {
	LinkedList::Node* currNode;
	currNode = LinkedList::m_Root;

	while (currNode != nullptr) {
		std::cout << currNode->getData() << " ";
		currNode = currNode->getNext();
	}
}

LinkedList::Node* LinkedList::m_GetHead() {
	return LinkedList::m_Root;
}
