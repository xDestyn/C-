#include <cstdio>
#include <cstring>
#include <cctype>

enum TokenType {
	ERROR, OPERATOR, VALUE, EOLN, QUIT, HELP, EOFILE
};

class Token
{
private:
	TokenType type;
	char      op;       // using '$' as undefined/error
	int       val;      // using -999 as undefined/error

public:

	Token();
	Token(TokenType t);
	void setToType(TokenType t);
	void setToOperator(char c);
	void setToValue(int v);
	bool equalsType(TokenType t);
	bool equalsOperator(char c);
	char getOperator();
	int getValue();
};

class TokenReader
{
private:
	char inputline[300];  // this assumes that all input lines are 300 characters or less in length
	bool needline;
	int pos;


public:

	// initialize the TokenReader class to read from Standard Input
	TokenReader();
	void clearToEoln();
	Token getNextToken();
};

class IntegerEntity
{
public:

	IntegerEntity();
	bool isEmpty();
	void push(int element);
	int topVal();
	void popVal();
	void resetVal();

private:
	int* valArray;
	int size;
	int activeSpace;

};

class OperatorEntity
{

public:
	OperatorEntity();
	bool isEmpty();
	void pushChar(char element);
	char topChar();
	void popChar();
	void resetChar();

private:
	char activeSpace;
	int size;
	char* charArray;
};

void processExpression(Token inputToken, TokenReader* tr);
int evaluateExpression(int value1, char mathSymbol, int value2);
void printCommands();