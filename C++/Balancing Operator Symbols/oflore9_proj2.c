/*Project 2: Balanced Symbol Checker
  Author: Omar Flores
  UIN: 653136468
  Summer CS 211
 */
#include <stdio.h>
#include <stdlib.h>
#include <string.h> // Used for string methods
#define TRUE 1;
#define FALSE 0;
int debugMode; // Global variable debugMode

typedef struct Stack
{
	int* dArray; /* pointer to dynamic array  */
	int size;	 /* amount of space allocated */
	int activeValue; /*top of stack indicator
					  - counts how many values are on the stack*/
} Stack;

// Initializes members in the struct
void init(Stack* s)
{
	s->size = 2;
	s->dArray = (int*)malloc(sizeof(int) * s->size);
	s->activeValue = 0;
}// end of init(..)

// Returns boolean integer whether stack is empty
int isEmpty(Stack* s)
{
	if (s->activeValue == 0)
		return TRUE
	else
		return FALSE

}// end of isEmpty(..)

// Returns the top character on the stack
char top(Stack * s)
{
	return (s->dArray[s->activeValue - 1]);
}// end of top(..)

// Pushes opening characters onto the stack
void push(Stack * s, char* val, char* result)
{
	int currentSize = s->size;
	/* check if enough space currently on stack and grow if needed */
	if (s->activeValue == s->size) {

		s->size += 2;
		s->dArray = (int*)realloc(s->dArray, s->size * sizeof(int));
	}
	// Stores current size and new size on a buffer character
	if (debugMode)
	{
		char aux[100]; // Used as buffer
		sprintf(aux, "Old size of dynamic array: %d\n", currentSize);
		strcat(result, aux); // Concatenates result and the buffer aux
		sprintf(aux, "New size of dynamic array: %d\n", s->size);
		strcat(result, aux); // Concatenates again with new size
	}

	/* add val onto stack */
	s->dArray[s->activeValue] = val;
	s->activeValue = s->activeValue + 1;
} // end of push(..)

// Removes element off the stack
void pop(Stack * s)
{
	s->activeValue = s->activeValue - 1;
} // end of pop(..)

// Resets the struct 
void clear(Stack * s)
{
	free(s->dArray);
	s->dArray = NULL;
	s->size = 0;
	s->activeValue = 0;
} // end of clear(..)

// Returns boolean integer whether they're matching pairs 
int CheckIfPair(char* top, char* current)
{
	if ((top == '(') && (current == ')'))
		return TRUE

	else if ((top == '{') && (current == '}'))
		return TRUE

	else if ((top == '[') && (current == ']'))
		return TRUE

	else if ((top == '<') && (current == '>'))
		return TRUE

		return FALSE
} // end of CheckIfPair(..)

// Returns the missing character
char MissingCharacter(char symbol)
{
	char characterMissing = ' ';
	if (symbol == ')')
		characterMissing = '(';
	else if (symbol == ']')
		characterMissing = '[';
	else if (symbol == '}')
		characterMissing = '{';
	else if (symbol == '>')
		characterMissing = '<';
	else if (symbol == '(')
		characterMissing = ')';
	else if (symbol == '[')
		characterMissing = ']';
	else if (symbol == '{')
		characterMissing = '}';
	else if (symbol == '<')
		characterMissing = '>';

	return characterMissing;
} // end of MissingCharacter(..)

int main(int argc, char** argv)
{
	char input[301];
	int length;    // Used for length of input
	Stack Entity;  // Instance of struct

	init(&Entity); // Initializes our Struct

	int error = 0; // Used for while loop ending

	/* set up an infinite loop */
	while (error == 0)
	{
		// Checks to see if there's a "flag" variable
		for (int i = 0; i < argc; i++) {
			if ('-' == argv[i][0]) {
				// True if hyphen detected
				debugMode = TRUE;
			}
		}
		/* get line of input from standard input */
		printf("\nEnter input to check or q to quit\n");
		fgets(input, 300, stdin);

		/* remove the newline character from the input */
		int i = 0;
		while (input[i] != '\n' && input[i] != '\0')
		{
			i++;
		}
		input[i] = '\0';

		/* check if user enter q or Q to quit program */
		if ((strcmp(input, "q") == 0) || (strcmp(input, "Q") == 0))
			break;

		printf("%s\n", input);

		length = strlen(input); // Gets length of input
		int indexFound = 0;		// Used to determine where the logical error was found
		char characterMissing = ' ';
		char itemPushed = ' ';
		char itemPopped = ' ';
		char result[1000000];   // Used as our buffer character
		char aux[100];			// Same purpose as result

		strcpy(result, "");     // Way to initialize our result buffer

		/* run the algorithm to determine if input is balanced */
		for (int i = 0; i < length; i++) {
			if (input[i] == '(' || input[i] == '[' ||
				input[i] == '{' || input[i] == '<')
			{
				// Pushes onto stack if it's an opening symbol
				push(&Entity, input[i], result);

				// Stores debugging information on our aux buffer and concatenates it with our result 
				if (debugMode)
				{
					itemPushed = top(&Entity);
					sprintf(aux, "Item pushed: %c \n", itemPushed);
					strcat(result, aux);
				}
			}
			else if (input[i] == ')' || input[i] == ']' ||
				input[i] == '}' || input[i] == '>')
			{
				char topValue = top(&Entity); // Stores the top character on the stack

				if (isEmpty(&Entity) || !CheckIfPair(topValue, input[i]))
				{
					// If stack empty, missing an opening symbol. (Error #2)
					if (isEmpty(&Entity))
					{
						indexFound = i;
						for (int i = 0; i < indexFound; i++) {
							printf(" ");
						}
						printf("^");

						// Retrieves what character is missing
						characterMissing = MissingCharacter(input[i]);
						printf("\nUnbalanced. Missing an opening symbol: %c\n", characterMissing);

						error = 1;
						break;
					}
					else if (!CheckIfPair(topValue, input[i]))
					{
						// If both top of stack character and current input don't match, wrong closing symbol
						// Error #1
						indexFound = i;
						for (int i = 0; i < indexFound; i++) {
							printf(" ");
						}
						printf("^");
						// Retrieves what character is missing
						characterMissing = MissingCharacter(topValue);
						printf("\nUnbalanced. Wrong closing symbol is at the top of the stack. Missing: %c\n", characterMissing);

						error = 1;
						break;
					}
				}
				else
				{
					// Removes if they're corresponding symbols
					pop(&Entity);

					// Stores debugging information on aux buffer and concatenates it with our result buffer 
					if (debugMode)
					{
						itemPopped = input[i];
						sprintf(aux, "Item popped: %c \n", itemPopped);
						strcat(result, aux);
					}
				}

			}
		}

		// Checks to see if there's errors at the end of the for loop
		if (error == 0)
		{
			if (isEmpty(&Entity))
				printf("Perfectly balanced!\n");

			else
			{
				indexFound = length;
				for (int i = 0; i < indexFound; i++) {
					printf(" ");
				}
				printf("^");
				characterMissing = MissingCharacter(top(&Entity));

				printf("\nUnbalanced. Missing closing symbol: %c\n", characterMissing);

			}
		}

		// Resets Stack 
		clear(&Entity);

		// Prints final message if in debugMode 
		if (debugMode)
		{
			printf("Printing debugging information: \n");
		}
		printf("%s", result);
	}

	printf("\nGoodbye\n");
	return 0;
} // end of main(..)