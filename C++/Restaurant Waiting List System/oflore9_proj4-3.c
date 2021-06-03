#include "oflore9_proj4.h"

// Adds new Node to the end of the linked list 
void addToList(NODE** pHead, char* name, int size, STATUS status)
{
	NODE* newNode = (NODE*)malloc(sizeof(NODE)); // Allocate memory 
	NODE* lastNode = *pHead;

	newNode->name = name;      // Stores name
	newNode->numPeople = size; // Stores size of group 
	newNode->pNext = NULL;     // Initializes pointer to next Node to NULL 

	/* In restaurant status */
	if (status == called)
		newNode->status = called;
	else
		newNode->status = waiting;
	// Case 1: pHead is empty
	if (*pHead == NULL)
	{
		*pHead = newNode;
		return;
	}

	else
	{
		// Traverses List until final Node
		while (lastNode->pNext != NULL)
		{
			lastNode = lastNode->pNext;
		}

		lastNode->pNext = newNode; // Assign's last's Node pNext* to the newNode 
		return;
	}
} // end of addToList(..)

// Returns boolean integer whether name exists in Node 
int doesNameExist(NODE** hd, char* name)
{
	NODE* pCurrent = *hd; // Initialize & declare current Node 

	/* If list is empty */
	if (pCurrent == NULL)
	{
		return 0;
	}

	/* List is not empty */
	else
	{
		while (pCurrent != NULL)
		{
			//  If both strings are equal, TRUE 
			if (strcmp(pCurrent->name, name) == 0)
				return 1;
			pCurrent = pCurrent->pNext; // go to the next name on the list 
		}
	}
	return 0;
} // end of doesNameExist(..)

// Returns boolean whether group is in restaurant waiting 
BOOLEAN UpdateStatus(NODE** pHead, char* name, int debugMode)
{
	NODE* pCurrent = *pHead;
	char* inRestaurantStatus;
	int counter = 0;

	// Case 1: Linked List is empty
	if (pCurrent == NULL)
	{
		printf("Error: List is empty!\n");
		return FALSE;
	}

	// Case 2: Linked List is not empty 
	else
	{
		while (pCurrent != NULL)
		{
			if (debugMode == TRUE)
			{
				// If in restaurant, waiting
				if (pCurrent->status == waiting)
					inRestaurantStatus = "PRESENT";
				// Not in restaurant
				else
					inRestaurantStatus = "NOT PRESENT";
				counter++;
				printf("Debug Mode:\n");
				printf("Number of groups: %d\t", counter);
				printf("Name: %s\t", pCurrent->name);
				printf("Size of group: %d\t", pCurrent->numPeople);
				printf("Status: %s\n", inRestaurantStatus);
			}
			// Checks if string names are same
			if (strcmp(pCurrent->name, name) == 0)
			{
				// If they're in the restaurant
				if (pCurrent->status == waiting)
					return FALSE;
				else
				{
					pCurrent->status = waiting;
					return TRUE;
				}
			}
			// Moves to next name on list
			pCurrent = pCurrent->pNext; 
		}
	}

	return FALSE;
} // end of UpdateStatus(..)

// Finds the first in-restaurant group that can fit at a given table.
// Returns name of group and removed from linked list
void RetrieveAndRemove(NODE** pHead, int size, int debugMode)
{
	NODE* pCurrent = *pHead;
	NODE* pTrail = NULL;
	char* inRestaurantStatus;
	int counter = 0;

	// Case 1: pHead is Empty 
	if (pCurrent == NULL)
	{
		printf("Error: List is empty!\n");
		return;
	}

	// If size matches and in restaurant, delete from list
	if (pCurrent->numPeople <= size && pCurrent->status == 0)
	{
		*pHead = (*pHead)->pNext;
		free(pCurrent);
		return;
	}

	while (pCurrent->pNext != NULL)
	{
		// If debugMode on
		if (debugMode == TRUE)
		{
			// If in restaurant, waiting
			if (pCurrent->status == waiting)
				inRestaurantStatus = "PRESENT";
			// Not in restaurant
			else
				inRestaurantStatus = "NOT PRESENT";
			counter++;
			printf("Debug Mode:\n");
			printf("Number of groups: %d\t", counter);
			printf("Name: %s\t", pCurrent->name);
			printf("Size of group: %d\t", pCurrent->numPeople);
			printf("Status: %s\n", inRestaurantStatus);
		}
		// Checks the other nodes to see if size matches and in restaurant 
		if (pCurrent->pNext->numPeople <= size)
		{
			if (pCurrent->pNext->status == 0)
			{
				pTrail = pCurrent->pNext;
				pCurrent->pNext = pTrail->pNext;
				free(pTrail);
				return;
			}
		}
		pCurrent = pCurrent->pNext;
	}
	printf("Error: Group size does not match table size\n");
	return;
} // end of RetrieveAndRemove(..)

// Returns the number of groups waiting ahead of a group with a specific name
int CountGroupsAhead(NODE** pHead, char* name, int debugMode)
{
	NODE* pCurrent = *pHead;
	char* inRestaurantStatus;
	int counter = 0;
	int numGroupsAhead = 0;

	// Case 1: List is empty
	if (pCurrent == NULL)
	{
		return 0;
	}

	// Case 2: List is not empty
	while (pCurrent != NULL)
	{
		// If debug mode on
		if (debugMode == TRUE)
		{
			// If in restaurant, waiting
			if (pCurrent->status == waiting)
				inRestaurantStatus = "PRESENT";
			// Not in restaurant
			else
				inRestaurantStatus = "NOT PRESENT";
			counter++;
			printf("Debug Mode:\n");
			printf("Number of groups: %d\t", counter);
			printf("Name: %s\t", pCurrent->name);
			printf("Size of group: %d\t", pCurrent->numPeople);
			printf("Status: %s\n", inRestaurantStatus);
		}
		
		// Check if names are equal
		if (strcmp(pCurrent->name, name) == 0)
		{
			return numGroupsAhead;
		}
		numGroupsAhead++;
		pCurrent = pCurrent->pNext;
	}
	return 0;
} // end of CountGroupsAhead(..)

// Traverses down the list until a specific group name is encountered
// Traverses and prints out each node's group size
void DisplayGroupSizeAhead(NODE** pHead, int groupsAhead)
{
	NODE* pCurrent = *pHead;
	char* inRestaurantStatus;
	int groupList = 0;
	int counter = 0;

	// Checks if group is next up
	if (groupList == groupsAhead)
	{
		printf("Up next!\n");
		return;
	}

	// List is empty
	if (pCurrent == NULL)
	{
		printf("Error: List is empty!\n");
		return;
	}

	while (pCurrent != NULL & groupList != groupsAhead)
	{
		if (pCurrent->status == waiting)
			inRestaurantStatus = "PRESENT";
		else
			inRestaurantStatus = "NOT PRESENT";
		counter++;
		printf("Number of groups: %d\t", counter);
		printf("Name: %s\t", pCurrent->name);
		printf("Size of group: %d\t", pCurrent->numPeople);
		printf("Status: %s\n", inRestaurantStatus);
		pCurrent = pCurrent->pNext;
		groupsAhead++;
	}

	return;
}// end of DisplayGroupSizeAhead(..)

// Prints out the node's group name, group size, and in-restaurant status
void DisplayListInformation(NODE** pHead)
{
	NODE* pCurrent = *pHead;
	char* inRestaurantStatus;
	int counter = 0;

	if (pCurrent == NULL)
	{
		printf("List is empty!\n");
		return;
	}

	while (pCurrent != NULL)
	{
		if (pCurrent->status == waiting)

			inRestaurantStatus = "PRESENT";
		else
			inRestaurantStatus = "NOT PRESENT";

		counter++;
		printf("Number of groups: %d\t", counter);
		printf("Name: %s\t", pCurrent->name);
		printf("Size of group: %d\t", pCurrent->numPeople);
		printf("Status: %s\n", inRestaurantStatus);
		pCurrent = pCurrent->pNext;
	}

	return;
} // end of DisplayListInformation(..)