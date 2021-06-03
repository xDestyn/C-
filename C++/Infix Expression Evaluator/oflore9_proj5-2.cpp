#include "oflore9_proj5.h"
int debugMode = 0;

void printCommands()
{
	printf("The commands for this program are:\n\n");
	printf("q - to quit the program\n");
	printf("? - to list the accepted commands\n");
	printf("or any infix mathematical expression using operators of (), *, /, +, -\n");
}

// Does the math
int evaluateExpression(int value1, char mathSymbol, int value2)
{
	int result = 0;
	switch (mathSymbol)
	{
		// Addition
	case '+':
		result = value1 + value2;
		break;
		// Subtraction
	case '-':
		result = value1 - value2;
		break;
		// Multiplication
	case '*':
		result = value1 * value2;
		break;
		// Division and check for division by 0
	case '/':
		if (value2 == 0)
		{
			printf("Error: Cannot divide by 0.\n");
			result = -999;
		}
		else
			result = value1 / value2;
		break;
	default:
		printf("Error: Is this an operation?\n");
		result = -999;
		break;
	}
	return result;
} // end of evaluateExpression(..)

void processExpression(Token inputToken, TokenReader* tr)
{
	/**********************************************/
	/* Declare both stack head pointers here      */
	IntegerEntity value;
	OperatorEntity op;
	/* Loop until the expression reaches its End */
	while (inputToken.equalsType(EOLN) == false)
	{
		/* The expression contain a VALUE */
		if (inputToken.equalsType(VALUE))
		{
			/* make this a debugMode statement */
			if (debugMode)
			{
				printf("Val: %d, ", inputToken.getValue());
			}
			// add code to perform this operation here
			value.push(inputToken.getValue());
		}

		/* The expression contains an OPERATOR */
		else if (inputToken.equalsType(OPERATOR))
		{
			/* make this a debugMode statement */
			if (debugMode)
			{
				printf("OP: %c, ", inputToken.getOperator());
			}
			// add code to perform this operation here
			if (inputToken.getOperator() == '(')
				// Pushes operator onto stack
				op.pushChar(inputToken.getOperator());
			else if (inputToken.getOperator() == '+' || inputToken.getOperator() == '-')
			{
				while (op.isEmpty() == false && (op.topChar() == '+' || op.topChar() == '-' || op.topChar() == '*' || op.topChar() == '/'))
				{
					if (value.isEmpty() == true)
					{
						printf("Error: Value stack is empty.\n");
					}

					else
					{
						char mathSymbol = op.topChar();
						int value1, value2, value3 = 0;

						op.popChar();
						value2 = value.topVal();
						value.popVal();
						value1 = value.topVal();
						value.popVal();

						value3 = evaluateExpression(value1, mathSymbol, value2);
						// Pushes result onto stack
						value.push(value3);
					}
				}
				// Pushes operator onto stack
				op.pushChar(inputToken.getOperator());
			}
			else if (inputToken.getOperator() == '*' || inputToken.getOperator() == '/')
			{
				while (op.isEmpty() == false && (op.topChar() == '*' || op.topChar() == '/'))
				{
					if (value.isEmpty() == true)
					{
						printf("Error: Value stack is empty.\n");
					}
					else
					{
						char mathSymbol = op.topChar();
						int value1, value2, value3 = 0;

						op.popChar();
						value2 = value.topVal();
						value.popVal();
						value1 = value.topVal();
						value.popVal();

						value3 = evaluateExpression(value1, mathSymbol, value2);
						// Pushes result onto stack
						value.push(value3);
					}
				}
				// Pushes operator into stack 
				op.pushChar(inputToken.getOperator());
			}
			else if (inputToken.getOperator() == ')')
			{
				while (op.isEmpty() == false && op.topChar() != '(')
				{
					if (value.isEmpty() == true)
					{
						printf("Error: Value stack is empty.\n");
					}
					else
					{
						char mathSymbol = op.topChar();
						int value1, value2, value3 = 0;

						op.popChar();
						value2 = value.topVal();
						value.popVal();
						value1 = value.topVal();
						value.popVal();

						value3 = evaluateExpression(value1, mathSymbol, value2);
						// Pushes result onto stack
						value.push(value3);
					}
				}
				if (op.isEmpty() == true)
					printf("Error: Operator Stack is empty\n");
				else
					op.popChar();
			}
		}
		/* get next token from input */
		inputToken = tr->getNextToken();
	}

	while (op.isEmpty() == false)
	{
		if (value.isEmpty() == true)
		{
			printf("Error: Value stack is empty.\n");
		}
		else
		{
			char mathSymbol = op.topChar();
			int value1, value2, value3 = 0;

			op.popChar();
			value2 = value.topVal();
			value.popVal();
			value1 = value.topVal();
			value.popVal();

			value3 = evaluateExpression(value1, mathSymbol, value2);
			value.push(value3);
		}
	}
	printf("Value on top of Value Stack: %d\n", value.topVal());
	value.popVal();
	if (value.isEmpty() == true)
		printf("Good. Stack is empty.\n");
	else
		printf("Error: Value Stack is not empty.");

	/* The expression has reached its end */

	// add code to perform this operation here

	printf("\n");
}

int main(int argc, char* argv[])
{
	/***************************************************************/
	/* Add code for checking command line arguments for debug mode */


	Token inputToken;
	TokenReader tr;
	OperatorEntity op;
	IntegerEntity value;

	printf("Starting Expression Evaluation Program\n\n");
	printf("Enter Expression: ");

	for (int i = 0; i < argc; i++) {

		if (strcmp(argv[i], "-d") == 0)
		{
			debugMode = 1;
		}
	}

	inputToken = tr.getNextToken();

	while (inputToken.equalsType(QUIT) == false)
	{
		/* check first Token on Line of input */
		if (inputToken.equalsType(HELP))
		{
			printCommands();
			tr.clearToEoln();
		}
		else if (inputToken.equalsType(ERROR))
		{
			printf("Invalid Input - For a list of valid commands, type ?\n");
			tr.clearToEoln();
		}
		else if (inputToken.equalsType(EOLN))
		{
			printf("Blank Line - Do Nothing\n");
			/* blank line - do nothing */
		}
		else
		{
			processExpression(inputToken, &tr);
		}

		printf("\nEnter Expression: ");
		inputToken = tr.getNextToken();
	}

	op.resetChar();
	value.resetVal();
	printf("Quitting Program\n");
	return 1;
}