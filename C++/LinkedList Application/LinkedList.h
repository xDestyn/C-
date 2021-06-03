#pragma once

#include <string>
#include "Collection.h"

class LinkedList : public Collection {
private:
	// Private Class Node
	class Node {
	private:
		Node* pNext;
		std::string pData;

	public:
		Node();
		Node(Node* pNext, const std::string& data);
		~Node();
		std::string getData();
		Node* getNext();
		void setNext(Node*);
	};

	Node* m_Root; // Head node
	int m_Length;

public:

	LinkedList();
	LinkedList(Node* node, int size);
	~LinkedList();
	bool isEmpty() const;
	int m_GetLength() const;
	void m_Append(const std::string& data);
	void m_Prepend(const std::string& data);
	void m_PrintSPL();
	void m_PrintNodes();
	LinkedList::Node* m_GetHead();


};