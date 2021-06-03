#pragma once
#include <string>
#include <vector>

class BST
{
private:

	struct Node {
		std::string data;
		Node* left_child;
		Node* right_child;
	};

	Node* m_Root;

	Node* m_Insert(std::string data, Node* node);

	void m_inOrder(Node* node);

	Node* m_clearTree(Node* node);

	std::vector<std::string> m_inOrderTreeOne(Node* node);

	std::vector<std::string> m_inOrderTreeTwo(Node* node);

public:
	// Constructor
	BST();

	// Destructor
	~BST();

	// Inserts data into node
	void m_Insert(std::string data);

	// Displays information 
	void m_Print();

	// Retrieves root of BST
	Node* m_getRoot();

	// Copy Constructor
	BST(const BST& other);

	// Assignment Operator
	BST& operator =(const BST& other);

	Node* copyHelper(const Node* other);

	void assignHelper(Node* other);

	// Used for merging / intersect for tree one and tree two
	std::vector<std::string> m_collectTreeOne();

	std::vector<std::string> m_collectTreeTwo();
};