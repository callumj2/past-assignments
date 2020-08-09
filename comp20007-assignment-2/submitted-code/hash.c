/* * * * * * *
 * Hashing module for Assignment 2.
 *
 * created for COMP20007 Design of Algorithms 2020
 * template by Tobias Edwards <tobias.edwards@unimelb.edu.au>
 * implementation by <Insert Name Here>
 */

#include <stdlib.h>
#include <stdio.h>
#include <assert.h>
#include "hash.h"
#include "util.h"
#include "deque.h"
#include "string_IO_a2.h"

#define TASK_FAILED -1
#define MAX_STRING_SIZE 256
#define BINARY_STRING_SIZE 64
#define NEWLINE_CHAR '\n'

//*******************************************************  P1A  ************************************************************
// Implements a solution to Problem 1 (a), which reads in from stdin:
//   N M
//   str_1
//   str_2
//   ...
//   str_N
// And outputs (to stdout) the hash values of the N strings 1 per line.

void problem_1_a() {
  // read in the number of strings and mod value from stdin
  int nStrings, modVal, i;
  scanf("%d %d\n", &nStrings, &modVal);
  // for each line of input, calculate the hash value and print it
  for (i = 0; i < nStrings; i ++){
    Deque *string = get_next_line();
    modify_line(string);
    int hashValue = get_hash_value(string,modVal);
    printf("%d\n", hashValue);
    // clean up the used memory
    free_deque(string);
  }
}

// takes a Deque containing a string of character values and a modulus value and returns a hashed value for the string
int get_hash_value(Deque *string, int modVal){
  int numberOfCharacters = deque_size(string);
  // initialise the hash value to the first character in the string's modified int value
  Node *currNode = string->top;
  int hashValue = currNode->data;

  // continuously compute the updated hash value using horners rule until all coefficients have been included, start at i = 1
  // since the first coefficient has been already added.
  for (int i = 1; i < numberOfCharacters; i++){
    currNode = currNode->next;
    hashValue = ((hashValue * BINARY_STRING_SIZE % modVal)  + currNode->data % modVal);
  }
  // return the final hash value
  return hashValue % modVal;
}

//*******************************************************  P1B  ************************************************************
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
void problem_1_b() {

  // read in the first line of input
  int nStrings, tableSize, stepSize;
  scanf("%d %d %d\n", &nStrings, &tableSize, &stepSize);

  // Create the hash table and fill it
  HashTable *hashTable = new_hash_table(tableSize, stepSize);
  Deque **inputStrings = get_input_strings(nStrings);
  fill_hash_table(hashTable, inputStrings, nStrings);

  // Print the hash table
  print_hash_table(hashTable);

  // Free the allocated memory
  free_deque_array(inputStrings, nStrings);
  free(inputStrings);
  free_hash_table(hashTable);
}


//*******************************************************  INITIALIZING  ************************************************************
// returns a pointer to a new hash table
HashTable *new_hash_table(int tableSize, int stepSize) {
  HashTable *newTable = (HashTable *)mymalloc(sizeof(HashTable));
  // initialize the table size and step size
  newTable->tableSize = tableSize;
  newTable->stepSize = stepSize;
  // initialize the table as an array of empty deques
  newTable->table = instantiate_empty_table(tableSize);
  return newTable;
}

// returns a fresh "table" section of a hash table
Deque **instantiate_empty_table(int tableSize){
  Deque **empty_table = (Deque **)mymalloc(tableSize * sizeof(Deque *));
  int i;
  for (i = 0; i < tableSize; i ++){
    empty_table[i] = new_deque();
  }
  return empty_table;
}

//*********************************************************  INSERTING  ************************************************************
void fill_hash_table(HashTable *hashTable, Deque **inputStrings, int nStrings){
  // for each string, insert it at the given hash value
  for (int i = 0; i < nStrings; i++){
    int index = hash_insert(hashTable, inputStrings[i]);
    // if hash insert fails, attempt to double the table size and rehash
    if (index == TASK_FAILED){
      rehash(hashTable, hashTable->tableSize * 2);
      // after rehashing, reattempt to insert the current string
      hash_insert(hashTable, inputStrings[i]);
    }
  }
}

// takes a hash table and a string and attempts to insert the string into the hash table, returning 
// the index of insertion or returning TASK_FAILED if no valid placement exists
int hash_insert(HashTable *hashTable, Deque *string){
  // extract the relevant information about the hash table
  Deque **table = hashTable->table;
  int tableSize = hashTable->tableSize;
  // calculate the hash value needed
  int index = get_hash_value(string, tableSize);
  // if there is nothing in the indexed position, insert the data
  if (table[index]->size == 0){
    apply_insertion(hashTable, index, string);
    return index;
  }
  // otherwise attempt to place it in a new spot
  else {
    index = find_valid_insertion(hashTable, index, index);
    // if we find a valid position, insert the string, otherwise return TASK_FAILED
    if (index != TASK_FAILED){
      apply_insertion(hashTable, index, string);
      return index;
    }
    else {
      return TASK_FAILED;
    }
  }
}

// applies an insertion at a valid empty position in the hash table
void apply_insertion(HashTable *hashTable, int index, Deque *string){
  // Place a copy of the string into this position in the hash table
  // Deque *copyOfString = copy_of_deque(string);
  deque_merge_copy(hashTable->table[index], string);
}

// uses linear probing to return a valid empty spot in the hash table or an error if no such spot exists
int find_valid_insertion(HashTable *hashTable, int originalTry, int previousTry){
  int newTry = (previousTry + hashTable->stepSize) % hashTable->tableSize;
  // if we have reached the original index we tried, return TASK_FAILED
  if (newTry == originalTry){
    return TASK_FAILED;
  }
  // if there is room for an entry at this index, return it
  else if (hashTable->table[newTry]->size == 0){
    return newTry;
  }
  // otherwise recurse and try a new index
  else {
    return find_valid_insertion(hashTable, originalTry, newTry);
  }
}

// takes a hash table and replaces it with one at a given size, rehashing the elements in it in the order they appear in 
// the original table
void rehash(HashTable *hashTable, int newSize){
  // Log information about the old table
  Deque **oldTable = hashTable->table;
  int oldSize = hashTable->tableSize;
  
  // Have the hash table point to an empty table of the new size
  hashTable->table = instantiate_empty_table(newSize);
  hashTable->tableSize = newSize;
  // for each spot in the table, if it is filled rehash it into the new table
  for (int i = 0; i < oldSize; i ++){
    if (deque_size(oldTable[i]) > 0){
      // insert a copy of the string into the table
      // Deque* copyOfString = copy_of_deque(oldTable[i]);
      hash_insert(hashTable, oldTable[i]);
    }
  }
  // Clean up the old memory
  free_deque_array(oldTable, oldSize);
}

//*******************************************************  PRINTING  ************************************************************
// takes a hash table and prints it
void print_hash_table(HashTable *hashTable){
  int i;
  // print each element in the table
  for (i = 0; i < hashTable->tableSize; i++){
    printf("%d: ", i);
    // if there is a stiring here, print it
    if (hashTable->table[i]->size > 0){
      print_string(hashTable->table[i]);
    }
    printf("\n");
  }
}

//*******************************************************  CLEANUP  ************************************************************
// Free the memory associated with a HashTable
void free_hash_table(HashTable *hashTable){
  // free the table values
  Deque **table = hashTable->table;
  free_deque_array(table, hashTable->tableSize);
  free(table);
  // free the hash table itself
  free(hashTable);
}
