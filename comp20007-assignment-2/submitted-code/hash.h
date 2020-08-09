/* * * * * * *
 * Hashing module for Assignment 2.
 *
 * created for COMP20007 Design of Algorithms 2020
 * template by Tobias Edwards <tobias.edwards@unimelb.edu.au>
 * implementation by Callum Johnson <callumj2@student.unimelb.edu.au>
 */

#include "deque.h"

#ifndef HASH_H
#define HASH_H

// Typedefs
// structure for storing hash table information
typedef struct hashTable_t HashTable;
struct hashTable_t {
  Deque **table;
  int tableSize;
  int stepSize;
};
// Implements a solution to Problem 1 (a), which reads in from stdin:
//   N M
//   str_1
//   str_2
//   ...
//   str_N
// And outputs (to stdout) the hash values of the N strings 1 per line.
void problem_1_a();

// takes a Deque containing a string of character values and a modulus value and returns a hashed value for the string
int get_hash_value(Deque *string, int modVal);
// Implements a solution to Problem 1 (b), which reads in from stdin:
//   N M K
//   str_1
//   str_2
//   ...
//   str_N
// Each string is inputed (in the given order) into a hash table with size
// M. The collision resolution strategy must be linear probing with step
// size K. If an element cannot be inserted then the table size should be
// doubled and all elements should be re-hashed (in index order) before
// the element is re-inserted.
//
// This function must output the state of the hash table after all insertions
// are performed, in the following format
//   0: str_k
//   1:
//   2: str_l
//   3: str_p
//   4:
//   ...
//   (M-2): str_q
//   (M-1):
void problem_1_b();

//*******************************************************  INITIALIZING  ************************************************************
// returns a pointer to a new hash table
HashTable *new_hash_table(int tableSize, int stepSize);

// returns a fresh "table" section of a hash table
Deque **instantiate_empty_table(int tableSize);

//*********************************************************  INSERTING  ************************************************************
// Takes a hash table, an array of strings, and the number of strings to insert into the table and inserts them
void fill_hash_table(HashTable *hashTable, Deque **inputStrings, int nStrings);

// takes a hash table and a string and attempts to insert the string into the hash table, returning 
// the index of insertion or returning TASK_FAILED if no valid placement exists
int hash_insert(HashTable *hashTable, Deque *string);

// applies an insertion at a valid empty position in the hash table
void apply_insertion(HashTable *hashTable, int index, Deque *string);

// uses linear probing to return a valid empty spot in the hash table or an error if no such spot exists
int find_valid_insertion(HashTable *hashTable, int originalTry, int previousTry);

// takes a hash table and replaces it with one at a given size, rehashing the elements in it in the order they appear in 
// the original table
void rehash(HashTable *hashTable, int newSize);

//*******************************************************  PRINTING  ************************************************************
// takes a hash table and prints it
void print_hash_table(HashTable *hashTable);

// Free the memory associated with a HashTable
void free_hash_table(HashTable *hashTable);

#endif
