/*Author: Omar Flores
  UIN: 653136468
  Summer CS 211 
 */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

/* Enumerations */
typedef enum { FALSE = 0, TRUE, NO = 0, YES } BOOLEAN;
typedef enum { waiting = 0, called = 1 } STATUS;

/* Struct */
typedef struct NODE
{
	char* name;
	int numPeople;
	STATUS status;
	struct NODE* pNext;
} NODE;

// Global variable debugMode 
int debugMode;

// Function Prototypes 
int doesNameExist(NODE** hd, char* name);
void addToList(NODE** pHead, char* name, int size, STATUS status);
BOOLEAN UpdateStatus(NODE** pHead, char* name, int debugMode);
void RetrieveAndRemove(NODE** pHead, int size, int debugMode);
int CountGroupsAhead(NODE** pHead, char* name, int debugMode);
void DisplayGroupSizeAhead(NODE** pHead, int groupsAhead);
void DisplayListInformation(NODE** pHead);
void clearToEoln();
int getNextNWSChar();
int getPosInt();
char* getName();
void printCommands();
void doAdd(NODE** pHead, int debugMode);
void doCallAhead(NODE** pHead, int debugMode);
void doWaiting(NODE** pHead, int debugMode);
void doRetrieve(NODE** pHead, int debugMode);
void doList(NODE** pHead, int debugMode);
void doDisplay(NODE** pHead);
