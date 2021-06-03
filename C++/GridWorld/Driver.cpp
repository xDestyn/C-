//#include "GridWorld.h"
//#include <string>
//#include <iostream>
//#include <sstream>
//#include <vector>
//#include "_test.h"
//#include "_util.h"
//
//using std::cout;
//using std::cin;
//using std::string;
//
//
//
//bool test_all(int n) {
//	GridWorld* gw = new GridWorld(5, 5);
//	int i, id;
//	vector<int> ref;
//	vector<int>* m;
//
//	for (i = 0; i < n; i++) {
//		gw->birth(0, 0, id);
//		if (id != i) {
//			std::cout << "FAILURE type-1:  id mismatch on birth\n";
//			return false;
//		}
//		ref.push_back(i);
//	}
//
//	m = gw->members(0, 0);
//	if (*m != ref) {
//		std::cout << "FAILURE type-2:  members wrong?\n";
//		return false;
//	}
//
//
//	for (i = n / 2; i < n; i++) {
//		if (!gw->death(i)) {
//			std::cout << "FAILURE type-3:  attempted death failed?\n";
//			std::cout << "    attempted kill:  " << i << "\n";
//			return false;
//		}
//	}
//
//
//
//	if (gw->population() != n / 2) {
//		std::cout << "FAILURE type-4:  world pop wrong?\n";
//		std::cout << "  expected:  " << n / 2 << "\n";
//		std::cout << "  reported:  " << gw->population() << "\n";
//		return false;
//	}
//
//	vector<int> ref2;
//	for (i = 0; i < n / 2; i++) {
//		ref2.push_back(i);
//	}
//	delete m;
//	m = gw->members(0, 0);
//	if (*m != ref2) {
//		std::cout << "FAILURE type-5:  members (0,0) wrong after death ops??\n";
//		return false;
//	}
//	delete m;
//	delete gw;
//	return true;
//}
//
//bool t2A(int n) {
//	GridWorld* gw = new GridWorld(5, 5);
//	int i, id;
//	vector<int> ref;
//	vector<int>* m;
//
//	for (i = 0; i < n; i++) {
//		gw->birth(0, 0, id);
//		if (id != i) {
//			std::cout << "FAILURE type-1:  id mismatch on birth\n";
//			return false;
//		}
//		ref.push_back(i);
//	}
//
//	m = gw->members(0, 0);
//	if (*m != ref) {
//		std::cout << "FAILURE type-2:  members wrong?\n";
//		return false;
//	}
//
//
//	for (i = n / 2; i < n; i++) {
//		if (!gw->death(i)) {
//			std::cout << "FAILURE type-3:  attempted death failed?\n";
//			std::cout << "    attempted kill:  " << i << "\n";
//			return false;
//		}
//	}
//
//
//
//	if (gw->population() != n / 2) {
//		std::cout << "FAILURE type-4:  world pop wrong?\n";
//		std::cout << "  expected:  " << n / 2 << "\n";
//		std::cout << "  reported:  " << gw->population() << "\n";
//		return false;
//	}
//
//	vector<int> ref2;
//	for (i = 0; i < n / 2; i++) {
//		ref2.push_back(i);
//	}
//	delete m;
//	m = gw->members(0, 0);
//	if (*m != ref2) {
//		std::cout << "FAILURE type-5:  members (0,0) wrong after death ops??\n";
//		return false;
//	}
//
//	//  make sure the 1st half is still alive and in (0,0)
//	for (i = 0; i < n / 2; i++) {
//		int r, c;
//		r = -1; c = -1;
//		if (!gw->whereis(i, r, c)) {
//			std::cout << "FAILURE type-5.1:  whereis failed?\n";
//			return false;
//		}
//		if (r != 0 || c != 0) {
//			std::cout << "FAILURE type-5.2:  whereis " << i << " wrong?\n";
//			std::cout << "  expected:  (0, 0)\n";
//			std::cout << "  reported:  (" << r << ", " << c << ")\n";
//			return false;
//		}
//	}
//	// make sure the 2nd half is dead
//	for (i = n / 2; i < n; i++) {
//		int r, c;
//
//		r = -1; c = -1;
//		if (gw->whereis(i, r, c)) {
//			std::cout << "FAILURE type-5.3:  whereis error?\n";
//			std::cout << "  person " << i << " should be dead\n";
//			std::cout << "  reported as alive!\n";
//			return false;
//		}
//	}
//	// this sequence of births should restore (0,0) to exactly original
//	//   member list of 0..n-1
//	for (i = n / 2; i < n; i++) {
//		gw->birth(0, 0, id);
//		if (id != i) {
//			std::cout << "FAILURE type-6:  birth(0,0) wrong ??\n";
//			std::cout << "   expected id:  " << i << "\n";
//			std::cout << "   reported id:  " << id << "\n";
//			return false;
//		}
//	}
//	delete m;
//	m = gw->members(0, 0);
//	if (*m != ref) {
//		std::cout << "FAILURE type-7:  members (0,0) wrong?\n";
//		return false;
//	}
//
//
//
//	if (gw->population(0, 0) != n) {
//		std::cout << "FAILURE type-8:  (0,0) pop wrong?\n";
//		std::cout << "  expected:  " << n << "\n";
//		std::cout << "  reported:  " << gw->population(0, 0) << "\n";
//		return false;
//	}
//	if (gw->population() != n) {
//		std::cout << "FAILURE type-9:  world pop wrong?\n";
//		std::cout << "  expected:  " << n << "\n";
//		std::cout << "  reported:  " << gw->population(0, 0) << "\n";
//		return false;
//	}
//	int r, c;
//
//	for (i = 0; i < n; i++) {
//		r = -1; c = -1;
//		if (!gw->whereis(i, r, c)) {
//			std::cout << "FAILURE type-8:  whereis failed?\n";
//			return false;
//		}
//		if (r != 0 || c != 0) {
//			std::cout << "FAILURE type-9:  whereis " << i << " wrong?\n";
//			std::cout << "  expected:  (0, 0)\n";
//			std::cout << "  reported:  (" << r << ", " << c << ")\n";
//			return false;
//		}
//	}
//	delete m;
//	delete gw;
//	return true;
//}
//
//bool t2B(int n) {
//	GridWorld* gw = new GridWorld(5, 5);
//	int i, id;
//	vector<int> ref;
//	vector<int>* m;
//
//	for (i = 0; i < n; i++) {
//		gw->birth(0, 0, id);
//		if (id != i) {
//			std::cout << "FAILURE type-1:  id mismatch on birth\n";
//			return false;
//		}
//		ref.push_back(i);
//	}
//
//	/**
//	m = gw->members(0, 0);
//	if(*m != ref) {
//	  std::cout << "FAILURE type-2:  members wrong?\n";
//	  return false;
//	}
//	**/
//
//
//	for (i = n / 2; i < n; i++) {
//		if (!gw->death(i)) {
//			std::cout << "FAILURE type-3:  attempted death failed?\n";
//			std::cout << "    attempted kill:  " << i << "\n";
//			return false;
//		}
//	}
//
//
//
//	if (gw->population() != n / 2) {
//		std::cout << "FAILURE type-4:  world pop wrong?\n";
//		std::cout << "  expected:  " << n / 2 << "\n";
//		std::cout << "  reported:  " << gw->population() << "\n";
//		return false;
//	}
//
//	/**
//	vector<int> ref2;
//	for(i=0; i<n/2; i++) {
//	  ref2.push_back(i);
//	}
//	delete m;
//	m = gw->members(0, 0);
//	if(*m != ref2) {
//	  std::cout << "FAILURE type-5:  members (0,0) wrong after death ops??\n";
//	  return false;
//	}
//	**/
//
//	//  make sure the 1st half is still alive and in (0,0)
//	for (i = 0; i < n / 2; i++) {
//		int r, c;
//		r = -1; c = -1;
//		if (!gw->whereis(i, r, c)) {
//			std::cout << "FAILURE type-5.1:  whereis failed?\n";
//			return false;
//		}
//		if (r != 0 || c != 0) {
//			std::cout << "FAILURE type-5.2:  whereis " << i << " wrong?\n";
//			std::cout << "  expected:  (0, 0)\n";
//			std::cout << "  reported:  (" << r << ", " << c << ")\n";
//			return false;
//		}
//	}
//	// make sure the 2nd half is dead
//	for (i = n / 2; i < n; i++) {
//		int r, c;
//
//		r = -1; c = -1;
//		if (gw->whereis(i, r, c)) {
//			std::cout << "FAILURE type-5.3:  whereis error?\n";
//			std::cout << "  person " << i << " should be dead\n";
//			std::cout << "  reported as alive!\n";
//			return false;
//		}
//	}
//	delete gw;
//	return true;
//}
//
//bool t2C(int n) {
//	GridWorld* gw = new GridWorld(5, 5);
//	int i, id;
//	vector<int> ref;
//	vector<int>* m;
//
//	for (i = 0; i < n; i++) {
//		gw->birth(0, 0, id);
//		if (id != i) {
//			std::cout << "FAILURE type-1:  id mismatch on birth\n";
//			return false;
//		}
//		ref.push_back(i);
//	}
//
//	m = gw->members(0, 0);
//	if (*m != ref) {
//		std::cout << "FAILURE type-2:  members wrong?\n";
//		return false;
//	}
//
//
//	for (i = n / 2; i < n; i++) {
//		if (!gw->death(i)) {
//			std::cout << "FAILURE type-3:  attempted death failed?\n";
//			std::cout << "    attempted kill:  " << i << "\n";
//			return false;
//		}
//	}
//
//
//
//	if (gw->population() != n / 2) {
//		std::cout << "FAILURE type-4:  world pop wrong?\n";
//		std::cout << "  expected:  " << n / 2 << "\n";
//		std::cout << "  reported:  " << gw->population() << "\n";
//		return false;
//	}
//
//	vector<int> ref2;
//	for (i = 0; i < n / 2; i++) {
//		ref2.push_back(i);
//	}
//	delete m;
//	m = gw->members(0, 0);
//	if (*m != ref2) {
//		std::cout << "FAILURE type-5:  members (0,0) wrong after death ops??\n";
//		return false;
//	}
//
//	//  make sure the 1st half is still alive and in (0,0)
//	for (i = 0; i < n / 2; i++) {
//		int r, c;
//		r = -1; c = -1;
//		if (!gw->whereis(i, r, c)) {
//			std::cout << "FAILURE type-5.1:  whereis failed?\n";
//			return false;
//		}
//		if (r != 0 || c != 0) {
//			std::cout << "FAILURE type-5.2:  whereis " << i << " wrong?\n";
//			std::cout << "  expected:  (0, 0)\n";
//			std::cout << "  reported:  (" << r << ", " << c << ")\n";
//			return false;
//		}
//	}
//	// make sure the 2nd half is dead
//	for (i = n / 2; i < n; i++) {
//		int r, c;
//
//		r = -1; c = -1;
//		if (gw->whereis(i, r, c)) {
//			std::cout << "FAILURE type-5.3:  whereis error?\n";
//			std::cout << "  person " << i << " should be dead\n";
//			std::cout << "  reported as alive!\n";
//			return false;
//		}
//	}
//	// this sequence of births should restore (0,0) to exactly original
//	//   member list of 0..n-1
//	for (i = n / 2; i < n; i++) {
//		gw->birth(0, 0, id);
//		if (id != i) {
//			std::cout << "FAILURE type-6:  birth(0,0) wrong ??\n";
//			std::cout << "   expected id:  " << i << "\n";
//			std::cout << "   reported id:  " << id << "\n";
//			return false;
//		}
//	}
//	delete m;
//	m = gw->members(0, 0);
//	if (*m != ref) {
//		std::cout << "FAILURE type-7:  members (0,0) wrong?\n";
//		return false;
//	}
//
//
//
//	if (gw->population(0, 0) != n) {
//		std::cout << "FAILURE type-8:  (0,0) pop wrong?\n";
//		std::cout << "  expected:  " << n << "\n";
//		std::cout << "  reported:  " << gw->population(0, 0) << "\n";
//		return false;
//	}
//	if (gw->population() != n) {
//		std::cout << "FAILURE type-9:  world pop wrong?\n";
//		std::cout << "  expected:  " << n << "\n";
//		std::cout << "  reported:  " << gw->population(0, 0) << "\n";
//		return false;
//	}
//	int r, c;
//
//	for (i = 0; i < n; i++) {
//		r = -1; c = -1;
//		if (!gw->whereis(i, r, c)) {
//			std::cout << "FAILURE type-8:  whereis failed?\n";
//			return false;
//		}
//		if (r != 0 || c != 0) {
//			std::cout << "FAILURE type-9:  whereis " << i << " wrong?\n";
//			std::cout << "  expected:  (0, 0)\n";
//			std::cout << "  reported:  (" << r << ", " << c << ")\n";
//			return false;
//		}
//	}
//	delete m;
//	delete gw;
//	return true;
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
///*
// *
// * Optional cmd-line arg is "n" which is used to determine
// *   number of operations (births, etc.) performed.
// *
// * This test case focuses on birth, move and whereis
// */
//int main(int argc, char* argv[]) {
//	int n = 10;
//
//	if (argc == 2) {
//		n = atoi(argv[1]);
//	}
//
//	START("~ COLLECTION OF FUNCTIONAL-TESTS ");
//	{
//
//		PtsPer = 5.0;
//		TEST_RET(t2A(n),
//			"n births + members + n/2 deaths + pop + members (functional only)",
//			1, PtsPer);
//
//	}
//	END
//		return 0;
//}



#include "GridWorld.h"
#include <string>
#include <iostream>
#include <sstream>
#include <vector>

using std::cout;
using std::cin;
using std::string;

/**
 * simple driver program exercising some of the
 *   member functions in GridWorld.
 *
 */

enum CmdResult { failure, success, quit };

/* prints contens of an integer vector */
void pvec(vector<int>* v) {
	cout << "  [ ";
	for (int x : *v) {
		cout << x << " ";
	}
	cout << "]\n";
}

void arg_err(const string& cmd, int correct_nargs) {
	cout << "usage error: " << cmd << " expects "
		<< correct_nargs << " argument(s)\n";
}

/* function for evaluating/applying a command:
 *
 *   cmd is a command name
 *   args[] is an array of nargs integer arguments
 *   gw is the GridWorld to which the command is applied if possible.
 *
 * returns:
 *   success / failure depending whether the command exists, has correct
 *     number of parameters and if/when invoked on the gw object, the success
 *     or failure operation.
 *
 *   Or returns quit if the "quit" command was issued.
 *
 */
CmdResult eval_cmd(GridWorld* gw, const string& cmd, int args[], int nargs) {

	if (cmd == "birth") {
		int id;

		if (nargs != 2) {
			arg_err(cmd, 2);
			return failure;
		}
		else {
			if (!gw->birth(args[0], args[1], id)) {
				cout << "  operation failed\n";
				return failure;
			}
			else {
				cout << "  operation succeeded\n";
				cout << "  PersonID: " << id << "\n";
				return success;
			}
		}
	}
	if (cmd == "death" || cmd == "kill") {
		if (nargs != 1) {
			arg_err(cmd, 1);
			return failure;
		}
		else {
			if (!gw->death(args[0])) {
				cout << "  operation failed\n";
				return failure;
			}
			else {
				cout << "  operation succeeded\n";
				return success;
			}
		}
	}
	if (cmd == "move") {
		if (nargs != 3) {
			arg_err(cmd, 3);
			return failure;
		}
		else {
			if (!gw->move(args[0], args[1], args[2])) {
				cout << "  operation failed\n";
				return failure;
			}
			else {
				cout << "  operation succeeded\n";
				return success;
			}
		}
	}
	if (cmd == "members") {
		if (nargs != 2) {
			arg_err(cmd, 2);
			return failure;
		}
		else {
			vector<int>* members;
			members = gw->members(args[0], args[1]);
			pvec(members);
			delete members;
			return success;
		}
	}
	if (cmd == "whereis") {
		if (nargs != 1) {
			arg_err(cmd, 1);
			return failure;
		}
		else {
			int row, col;

			if (!gw->whereis(args[0], row, col)) {
				cout << "  operation failed\n";
				return failure;
			}
			else {
				cout << "  district (" << row << ", " << col << ")\n";
				return success;
			}
		}
	}
	if (cmd == "pop" || cmd == "population") {
		if (nargs == 0) {
			cout << gw->population() << "\n";
			return success;
		}
		else if (nargs == 2) {
			cout << gw->population(args[0], args[1]) << "\n";
			return success;
		}
		else {
			arg_err(cmd, 0);
			cout << "      OR\n";
			arg_err(cmd, 2);
			return failure;
		}
	}
	if (cmd == "nrows" || cmd == "num_rows") {
		if (nargs != 0) {
			arg_err(cmd, 0);
			return failure;
		}
		else {
			cout << gw->num_rows() << "\n";
			return success;
		}
	}
	if (cmd == "ncols" || cmd == "num_cols") {
		if (nargs != 0) {
			arg_err(cmd, 0);
			return failure;
		}
		else {
			cout << gw->num_cols() << "\n";
			return success;
		}
	}
	if (cmd == "quit") {
		if (nargs != 0) {
			arg_err(cmd, 0);
			return failure;
		}
		return quit;
	}
	cout << "command '" << cmd << "' not supported\n";
	return failure;
}




/*
 * defaults to a 5x5 grid.
 *
 * Recommendation:  why not modify so you can take command-line
 *  arguments specifying the grid dimensions?
 */
int main() {
	GridWorld* gw = new GridWorld(5, 5);
	string line;


	cout << "Welcome to the GridWorld Interactive Frontend\n";
	cout << "=============================================\n";

	cout << "COMMANDS:\n\n";

	cout << "    birth <row> <col>\n";
	cout << "    death <id>\n";
	cout << "    move <id> <targe-row> <target-col>\n";
	cout << "    members <row> <col>\n";
	cout << "    whereis <id> \n";
	cout << "    population \n";
	cout << "    population <row> <col>\n";
	cout << "    num_rows\n";
	cout << "    num_cols\n";
	cout << "    quit\n\n";


	bool done = false;

	do {
		cout << "cmd> ";

		// read a complete line
		std::getline(std::cin, line);

		// now create a "stringstream" on the line just read
		std::stringstream ss(line);

		int i = 0;
		string cmd;
		string junk;

		ss >> cmd;  // extract first token as command

		// up to 3 integer arguments should follow
		int args[3];
		int n = 0;
		// extract command arguments
		while (n < 3 && ss >> args[n]) {
			n++;
		}

		// now we have the command string and have
		//   parsed 0-3 integer arguments into args[]
		//
		// let eval_cmd try to apply the command
		if (eval_cmd(gw, cmd, args, n) == quit)
			done = true;

	} while (!done && !cin.eof());

	delete gw;

	return 0;
}