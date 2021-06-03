#include "oflore9_proj5.h"

// Default to initialize to the ERROR TokenType
Token::Token()
{
	type = ERROR;
	op = '$';
	val = -999;
}

// Initialize to a specific TokenType
Token::Token(TokenType t)
{
	type = t;
	op = '$';
	val = -999;
}

// Set to a specific TokenType
void Token::setToType(TokenType t)
{
	type = t;
	op = '$';
	val = -999;
}

// Set to a OPERATOR TokenType with specific operator value
void Token::setToOperator(char c)
{
	type = OPERATOR;
	op = c;
	val = -999;
}

// Set to a VALUE TokenType with a specific numeric value
void Token::setToValue(int v)
{
	type = VALUE;
	op = '$';
	val = v;
}

// return true if the Current Token is of the given TokenType
bool Token::equalsType(TokenType t)
{
	if (type == t)
		return true;
	else
		return false;
}

// return true if the Current Token is of the OPERATOR TokenType
//     and contains the given operator character
bool Token::equalsOperator(char c)
{
	if (type == OPERATOR && op == c)
		return true;
	else
		return false;
}

// Return the Operator for the current Token
	//   verify the current Token is of the OPERATOR TokenType
char Token::getOperator()
{
	if (type == OPERATOR)
		return op;
	else
		return '$';   // using $ to indicate an error value
}

// Return the Value for the current Token
//   verify the current token is of the value TokenType
int Token::getValue()
{
	if (type == VALUE)
		return val;
	else
		return -999;  // using -999 to indicate an error value
}

// initialize the TokenReader class to read from Standard Input
TokenReader::TokenReader()
{
	// set to read from Standard Input
	inputline[0] = '\0';
	pos = 0;
	needline = true;
}

// Force the next getNextToken to read in a line of input
void TokenReader::clearToEoln()
{
	needline = true;
}

// Return the next Token from the input line
Token TokenReader::getNextToken()
{
	char* endCheck;

	//printf ("getToken %d, %d, %s\n", pos, needline, inputline);

	// get a new line of input from user
	if (needline)
	{
		endCheck = fgets(inputline, 300, stdin);

		if (endCheck == NULL)
		{
			printf("Error in reading");
			Token t(EOFILE);
			return t;
		}

		for (int i = 0; i < strlen(inputline); i++)
			if ('\n' == inputline[i])
				inputline[i] = ' ';
		strcat(inputline, " ");    // add a space at end to help deal with digit calculation
		needline = false;
		pos = 0;
	}

	// skip over any white space characters in the beginning of the input
	while (pos < strlen(inputline) && isspace(inputline[pos]))
	{
		pos++;
	}

	// check for the end of the current line of input
	if (pos >= strlen(inputline))
	{ // at end of line
		needline = true;
		Token t(EOLN);
		return t;
	}

	// Get the next character from the input line
	char ch = inputline[pos]; pos++;

	// check if 'q' or 'Q' was entered ==> QUIT Token
	if ('q' == ch || 'Q' == ch)
	{
		return Token(QUIT);
	}

	// check if "?" was entered ==> HELP Token
	if ('?' == ch)
	{
		return Token(HELP);
	}

	// check for Operator values of: + - * / ( )  ==> OPERATOR Token
	if (('+' == ch) || ('-' == ch) || ('*' == ch) ||
		('/' == ch) || ('(' == ch) || (')' == ch))
	{
		Token t;
		t.setToOperator(ch);
		return t;
	}

	// check for a number  ==> VALUE Token
	if (isdigit(ch))
	{
		int number = int(ch) - int('0');  // subtract ascii value of ch from ascii value of '0'
		ch = inputline[pos]; pos++;
		while (isdigit(ch))
		{
			number = number * 10 + int(ch) - int('0');
			ch = inputline[pos]; pos++;
		}
		pos--; // since number calculation check one character after the last digit
		Token t;
		t.setToValue(number);
		return t;
	}

	// Input in not valid if code get to this part ==> ERROR Token
	return Token(ERROR);
}

// Default Constructor - Initializes private data members
IntegerEntity::IntegerEntity()
{
	size = 2;                  // Size of our array
	activeSpace = 0;           // Current space used
	valArray = new int[size];  // Dynamic array 
}

// Returns true if the stack has no members
bool IntegerEntity::isEmpty()
{
	if (activeSpace == 0)
		return true;
	else
		return false;
}

// Adds the data value on the top of the stack
void IntegerEntity::push(int element)
{
	// Check if top is equal to size
	if (activeSpace == size)
	{
		// Copies values from initial array to newArray
		int* newArray = new int[size + 2];
		for (unsigned int i = 0; i < size; i++) {
			newArray[i] = valArray[i];
		}
		// Deallocates initial array
		delete[] valArray;
		// Array now points to newly allocated array
		valArray = newArray;
		// Increments size 
		size += 2;
	}
	valArray[activeSpace] = element;
	activeSpace += 1;
}

// Returns the data value on the top of the stack
int IntegerEntity::topVal()
{
	return valArray[activeSpace - 1];
}

// Removes the data value from the top of the stack
void IntegerEntity::popVal()
{
	if (isEmpty() == true)
		return;
	else
		activeSpace--;

}

// Resets stack of integers 
void IntegerEntity::resetVal()
{
	delete[] valArray;
	activeSpace = 0;
	size = 2;
	valArray = new int[size];

}

// Default Constructor - Initializes private data members
OperatorEntity::OperatorEntity()
{
	size = 2;                // Size of our array
	activeSpace = 0;         // Current space used
	charArray = new char[size]; // Dynamic Array
}

// Returns true if the stack has no members
bool OperatorEntity::isEmpty()
{
	// Checks if size used is 0
	if (activeSpace == 0)
		return true;
	else
		return false;
}

// Adds data to the top of the stack 
void OperatorEntity::pushChar(char element)
{
	// Checks to see if activeSpace is equal to size of dynamic array
	if (activeSpace == size)
	{
		// Grow the dynamic array by 2
		char* newArray = new char[size + 2];

		// Copies the data from initial array to newArray
		for (unsigned int i = 0; i < size; i++) {

			newArray[i] = charArray[i];
		}
		// Deallocates initial array
		delete[] charArray;
		// Array is set to point to the newly allocated array
		charArray = newArray;
		// Increment size of array
		size += 2;
	}

	charArray[activeSpace] = element;
	activeSpace += 1;
}

// Removes the data value from the top of the stack 
void OperatorEntity::popChar()
{
	// Check if stack is empty
	if (isEmpty() == true)
		return;
	else
		activeSpace--;
}

// Returns the data value on the top of the stack
char OperatorEntity::topChar()
{
	return charArray[activeSpace - 1];
}

// Resets stack of characters
void OperatorEntity::resetChar()
{
	delete[] charArray;
	activeSpace = 0;
	size = 2;
	charArray = new char[size];
}