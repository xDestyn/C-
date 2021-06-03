#ifndef _GRID_WORLD_H
#define _GRID_WORLD_H

#include <vector>
#include <iostream>

using std::vector;


class GridWorld {

private:
	// private stuff goes here!
	//   typedefs
	//   data members
	//   private helper functions
	//   etc.

	int total;
	struct Node
	{
		int personID; 
		Node* pNext;
		Node* pBack;

		Node(int _personID = 0, Node* _pNext = nullptr, Node* _pBack = nullptr)
		{
			personID = _personID; pNext = _pNext, pBack = _pBack;
		}
	};

	class LinkedList
	{
	public:
		LinkedList()
		{
			pFront = nullptr;
			back = nullptr;
			size = 0;
		}

		~LinkedList()
		{
			Node* node = pFront;
			while (node)
			{
				pFront = node;
				node = node->pNext;
				delete pFront;
			}
		}

		bool is_empty()
		{
			return pFront == nullptr;
		}

		int pop_front()
		{
			if (pFront == nullptr)
				return -1;
			else
			{
				Node* pTemp = pFront;
				pFront = pFront->pNext;

				if (pFront == nullptr)
				{
					back = nullptr;
				}

				int value = pTemp->personID;
				delete pTemp;
				
				size--;
				return value;
			}

			return -1;
		}

		Node* push_back(int id)
		{
			Node* newNode = new Node();
			newNode->personID = id;
			newNode->pNext = nullptr;

			if (pFront == nullptr)
			{
				
				pFront = newNode;
				back = newNode;
			}
			else
			{
				newNode->pBack = back;
				back->pNext = newNode;
				back = newNode;
				
			}


			size++;
			return newNode;
		}
		
		void deleteNode(Node* oldNode)
		{
			Node* prevNode, * nextNode;
			prevNode = oldNode->pBack;
			nextNode = oldNode->pNext;

			// Deleting First Node
			if (!prevNode)
			{
				pFront = nextNode;
				if (nextNode)
				{
					nextNode->pBack = nullptr;
				}
				if (back == oldNode)
				{
					back = pFront;
				}
			}
			// Deleting last 
			else if (!nextNode)
			{
				prevNode->pNext = nullptr;
				back = prevNode;
			}
			// Middle
			else
			{
				prevNode->pNext = nextNode;
				nextNode->pBack = prevNode;
			}

			size--;
			delete oldNode;
		}

		Node* getFirst()
		{
			return pFront;
		}

		int getSize()
		{
			return size;
		}

	private:
		Node* pFront;
		Node* back;
		int size;
	};


	struct PersonInfo
	{
		bool isAlive;
		int row, col;
		Node* pCrossPtr;
		
		PersonInfo(bool _isAlive = true, int _row = 0, int _col = 0)
		{
			isAlive = _isAlive; row = _row; col = _col;
		}
	};

	vector<PersonInfo> people;


	struct DistrictInfo
	{
		LinkedList *pMembers;

		DistrictInfo(LinkedList* _pMembers = nullptr)
		{
			pMembers = _pMembers;
		}

		~DistrictInfo()
		{
			delete pMembers;
		}
	};
	DistrictInfo** grid;
	int nRows, nCols;

	
	DistrictInfo deadPool;
	int maxId;


public:
	/**
	* constructor:  initializes a "world" with nrows and
	*    ncols (nrows*ncols districtcs) in which all
	*    districts are empty (a wasteland!).
	*/
	GridWorld(unsigned nrows, unsigned ncols) {

		nRows = nrows;
		nCols = ncols;
		total = 0;
		deadPool = nullptr;
		maxId = 0;

		// your constructor code here!
		grid = (DistrictInfo**) new DistrictInfo *[nRows];
		for (unsigned int i = 0; i < nRows; i++)
		{
			grid[i] = new DistrictInfo[nCols];
			for (unsigned int j = 0; j < nCols; j++)
			{
				grid[i][j].pMembers = new LinkedList();
			}
		}

		deadPool.pMembers = new LinkedList();
	}
	~GridWorld() {
		// your destructor code here.

		for (int i = 0; i < nRows; i++)
		{
			delete[] grid[i];
		}

		delete[] grid;
	}

	/*
	 * function: birth
	 * description:  if row/col is valid, a new person is created
	 *   with an ID according to rules in handout.  New person is
	 *   placed in district (row, col)
	 *
	 * return:  indicates success/failure
	 */
		bool birth(int row, int col, int& id) {
		
		//int personID = 0;
		if (row < 0 || row >= nRows || col < 0 || col >= nCols)
		{
			return false;
		}

		
		LinkedList* dead = deadPool.pMembers;
		if (!dead->is_empty())
		{
			id = dead->pop_front();
		}
		else
		{
			id = maxId++;
		}


		Node* node = grid[row][col].pMembers->push_back(id);

		PersonInfo newPerson(true, row, col);

		newPerson.pCrossPtr = node;
		if (people.size() <= id)
		{
			people.resize(id+1);
		}

		people[id] = newPerson;

		total++;
		return true;
	}

	/*
	 * function: death
	 * description:  if given person is alive, person is killed and
	 *   data structures updated to reflect this change.
	 *
	 * return:  indicates success/failure
	 */
	bool death(int personID) {

		if (people.size() <= personID || !people[personID].isAlive)
			return false;

		LinkedList *dead = deadPool.pMembers;
		dead->push_back(personID);
		people[personID].isAlive = false;
		grid[people[personID].row][people[personID].col].pMembers->deleteNode(people[personID].pCrossPtr);
		total--;
		return true;
	}

	/*
	 * function: whereis
	 * description:  if given person is alive, his/her current residence
	 *   is reported via reference parameters row and col.
	 *
	 * return:  indicates success/failure
	 */
	bool whereis(int id, int& row, int& col)const {


		if (people.size() <= id || !people[id].isAlive)
		{
			return false;
		}

		else if (people[id].isAlive)
		{
			row = people[id].row;
			col = people[id].col;
			return true;
		}
	}

	/*
	 * function: move
	 * description:  if given person is alive, and specified target-row
	 *   and column are valid, person is moved to specified district and
	 *   data structures updated accordingly.
	 *
	 * return:  indicates success/failure
	 *
	 * comment/note:  the specified person becomes the 'newest' member
	 *   of target district (least seniority) --  see requirements of members().
	 */
	bool move(int id, int targetRow, int targetCol) {

		if (people.size() <= id || !people[id].isAlive || targetRow < 0 || targetRow >= nRows || targetCol < 0 || targetCol >= nCols)
		{
			return false;
		}

		grid[people[id].row][people[id].col].pMembers->deleteNode(people[id].pCrossPtr);


		Node* node = grid[targetRow][targetCol].pMembers->push_back(id);
		PersonInfo newPerson(true, targetRow, targetCol);

		newPerson.pCrossPtr = node;

		people[id] = newPerson;

		return true;
	}

	std::vector<int>* members(int row, int col)const {

		vector<int>* peopleList = new vector<int>();

		if (row < 0 || row >= nRows || col < 0 || col >= nCols)
		{
			return peopleList;
		}
		
		if (grid[row][col].pMembers->getSize() == 0)
		{
			return peopleList;
		}
		else
		{
			Node* pTemp = grid[row][col].pMembers->getFirst();

			int ID;

			while (pTemp)
			{
				ID = pTemp->personID;
				if (people[ID].isAlive)
				{
					peopleList->push_back(ID);
				}
				pTemp = pTemp->pNext;
			}
		}

		return peopleList;
	}

	/*
	 * function: population
	 * description:  returns the current (living) population of the world.
	 */
	int population()const {

		return total;
	}

	/*
	 * function: population(int,int)
	 * description:  returns the current (living) population of specified
	 *   district.  If district does not exist, zero is returned
	 */
	int population(int row, int col)const {

		
		return grid[row][col].pMembers->getSize();
	}

	/*
	 * function: num_rows
	 * description:  returns number of rows in world
	 */
	int num_rows()const {
		return nRows;
	}

	/*
	 * function: num_cols
	 * description:  returns number of columns in world
	 */
	int num_cols()const {
		return nCols;
	}

};

#endif