#include "BST.h"
#include <iostream>
#include <algorithm>
#include <vector>

// Stores values for tree one and tree two respectively. 
std::vector<std::string> temp; 
std::vector<std::string> temp2;

// Recursive helper function used to deallocate nodes from BST
BST::Node* BST::m_clearTree(Node* node) {
	if (node == NULL)
		return NULL;

	BST::m_clearTree(node->left_child);
	BST::m_clearTree(node->right_child);
	delete node;
}

// Recursive helper function used to add elements into respective BST
BST::Node* BST::m_Insert(std::string data, Node* node) {
	if (node == NULL) {
		node = new Node();
		node->data = data;
		node->left_child = node->right_child = NULL;
	}

	else if (data < node->data)
		node->left_child = BST::m_Insert(data, node->left_child);

	else if (data > node->data)
		node->right_child = BST::m_Insert(data, node->right_child);

	return node;
}

// Recursive helper function used to print elements of respective BST
void BST::m_inOrder(Node* node) {
	if (node == NULL)
		return;
	BST::m_inOrder(node->left_child);
	std::cout << node->data << " " << std::endl;
	BST::m_inOrder(node->right_child);
}

// Adds elements from BST one to vector one
std::vector<std::string> BST::m_inOrderTreeOne(Node* node) {
	if (node == NULL)
		return temp;
	BST::m_inOrderTreeOne(node->left_child);
	temp.push_back(node->data);
	BST::m_inOrderTreeOne(node->right_child);

	return temp;
}

// Adds elements from BST two to vector two
std::vector<std::string> BST::m_inOrderTreeTwo(Node* node) {
	if (node == NULL)
		return temp2;
	BST::m_inOrderTreeTwo(node->left_child);
	temp2.push_back(node->data);
	BST::m_inOrderTreeTwo(node->right_child);
	
	return temp2;
}

// Constructor of BST
BST::BST() {
	BST::m_Root = NULL;
}

// Destructor of BST
BST::~BST() {
	BST::m_Root = BST::m_clearTree(BST::m_Root);
	BST::m_Root = NULL;
}

// Adds element to respective BST
void BST::m_Insert(std::string data) {
	BST::m_Root = BST::m_Insert(data, BST::m_Root);
}

// Prints element from BST 
void BST::m_Print() {
	BST::m_inOrder(BST::m_Root);
	std::cout << std::endl;
}

// Retrieves root for given tree
BST::Node* BST::m_getRoot() {
	return BST::m_Root;
}

// Assignment operator for BST
BST& BST::operator=(const BST& other) {
	std::cout << "Assignment operator called!" << std::endl;
	if (this != &other) {
		this->~BST();
		Node* curr = other.m_Root;
		assignHelper(curr);
	}

	return *this;
}

// Copy constructor for BST
BST::BST (const BST& other) {
	std::cout << "Copy constructor called!" << std::endl;
	m_Root = BST::copyHelper(other.m_Root);
}

// Recursive helper function for Copy Constructor
BST::Node* BST::copyHelper(const Node* other) {
	if (other == NULL)
		return NULL;
	Node* copyNode = new Node;
	copyNode->data = other->data;
	copyNode->left_child = copyHelper(other->left_child);
	copyNode->right_child = copyHelper(other->right_child);

	return copyNode;
}

// Recursive helper function for assignment operator
void BST::assignHelper(Node* other) {
	if (other != NULL) {
		this->m_Insert(other->data);
		assignHelper(other->left_child);
		assignHelper(other->right_child);
	}
}

// Returns vector containing elements from BST one
std::vector<std::string> BST::m_collectTreeOne() {
	temp.clear();
	std::vector<std::string> tempVec;
	tempVec = BST::m_inOrderTreeOne(BST::m_Root);
	return tempVec;
}

// Returns vector containing elements from BST two
std::vector<std::string> BST::m_collectTreeTwo() {
	temp2.clear();
	std::vector<std::string> tempVec2;
	BST::m_inOrderTreeTwo(BST::m_Root);
	tempVec2 = BST::m_inOrderTreeTwo(BST::m_Root);
	return tempVec2;
}