#pragma once
#include <string>

class Collection {

private:


public:
	// Constructor
	Collection() {};
	// Destructor
	virtual ~Collection() {};

	virtual void m_Append(const std::string& data) = 0;

	virtual void m_Prepend(const std::string& data) = 0;

	virtual void m_PrintSPL() = 0;

	virtual void m_PrintNodes() = 0;

	//virtual void m_Remove() = 0;

};