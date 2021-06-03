#include "oflore9_proj4.h"

// Adds group to the waitlist, waiting in restaurant 
void doAdd(NODE** pHead, int debugMode)
{
	/* get group size from input */
	int size = getPosInt();
	if (size < 1)
	{
		printf("Error: Add command requires an integer value of at least 1\n");
		printf("Add command is of form: a <size> <name>\n");
		printf("  where: <size> is the size of the group making the reservation\n");
		printf("         <name> is the name of the group making the reservation\n");
		return;
	}

	/* get group name from input */
	char* name = getName();
	if (NULL == name)
	{
		printf("Error: Add command requires a name to be given\n");
		printf("Add command is of form: a <size> <name>\n");
		printf("  where: <size> is the size of the group making the reservation\n");
		printf("         <name> is the name of the group making the reservation\n");
		return;
	}

	// add code to perform this operation here
	if (doesNameExist(pHead, name))
		printf("Error: Name already exists in list!\n");
	
	else
	{
		if (debugMode == TRUE)
		{
			DisplayListInformation(pHead);
		}
		printf("Adding In-restaurant group \"%s\" of size %d\n", name, size);
		addToList(pHead, name, size, waiting);
		return;
	}
} // end of doAdd(..)

// Adds group to the waitlist as a call ahead group 
void doCallAhead(NODE** pHead, int debugMode)
{
	/* get group size from input */
	int size = getPosInt();
	if (size < 1)
	{
		printf("Error: Call-ahead command requires an integer value of at least 1\n");
		printf("Call-ahead command is of form: c <size> <name>\n");
		printf("  where: <size> is the size of the group making the reservation\n");
		printf("         <name> is the name of the group making the reservation\n");
		return;
	}

	/* get group name from input */
	char* name = getName();
	if (NULL == name)
	{
		printf("Error: Call-ahead command requires a name to be given\n");
		printf("Call-ahead command is of form: c <size> <name>\n");
		printf("  where: <size> is the size of the group making the reservation\n");
		printf("         <name> is the name of the group making the reservation\n");
		return;
	}

	// add code to perform this operation here
	if (doesNameExist(pHead, name))
		printf("Error: Name already exists in list!\n");
	

	else
	{
		if (debugMode == TRUE)
		{
			DisplayListInformation(pHead);
		}
		printf("Adding Call-ahead group \"%s\" of size %d\n", name, size);
		addToList(pHead, name, size, called);
		return;
	}
} // end of doCallAhead(..)

// Mark the call ahead group as waiting in the restaurant
void doWaiting(NODE** pHead, int debugMode)
{
	/* get group name from input */
	char* name = getName();
	if (NULL == name)
	{
		printf("Error: Waiting command requires a name to be given\n");
		printf("Waiting command is of form: w <name>\n");
		printf("  where: <name> is the name of the group that is now waiting\n");
		return;
	}

	// add code to perform this operation here
	if (!doesNameExist(pHead, name))
	{
		if (debugMode == TRUE)
		{
			DisplayListInformation(pHead);
		}
		printf("Error: Name does not exist in list!\n");
		return;
	}


	if (UpdateStatus(pHead, name, debugMode) == FALSE)
		printf("Group \"%s\" is already waiting in the restaurant\n", name);
	

	else
		printf("Call-ahead group \"%s\" is now waiting in the restaurant\n", name);
	
} // end of doWaiting(..)

// Retrieve and remove the first group on the wait list that is waiting in the restaurant 
void doRetrieve(NODE** pHead, int debugMode)
{
	/* get table size from input */
	int size = getPosInt();
	if (size < 1)
	{
		printf("Error: Retrieve command requires an integer value of at least 1\n");
		printf("Retrieve command is of form: r <size>\n");
		printf("  where: <size> is the size of the group making the reservation\n");
		return;
	}
	clearToEoln();
	printf("Retrieve (and remove) the first group that can fit at a table of size %d\n", size);

	// add code to perform this operation here
	RetrieveAndRemove(pHead, size, debugMode);
	return;
} // end of doRetrieve(..)

// List total number of groups that are in the wait list in front of the group specified by the given name 
void doList(NODE** pHead, int debugMode)
{
	/* get group name from input */
	char* name = getName();
	if (NULL == name)
	{
		printf("Error: List command requires a name to be given\n");
		printf("List command is of form: l <name>\n");
		printf("  where: <name> is the name of the group to inquire about\n");
		return;
	}

	// add code to perform this operation here
	if (doesNameExist(pHead, name))
	{
		printf("Group \"%s\" is behind the following groups\n", name);
		DisplayGroupSizeAhead(pHead, CountGroupsAhead(pHead, name, debugMode));
		return;
	}

	else
		printf("Error: Name does not exist in the list!\n");
	
} // end of doList(..)

// Display the total number of groups in the wait list from first to last 
void doDisplay(NODE** pHead)
{
	clearToEoln();
	printf("Display information about all groups\n");

	// add code to perform this operation here
	DisplayListInformation(pHead);
} // end of doDisplay(..)
