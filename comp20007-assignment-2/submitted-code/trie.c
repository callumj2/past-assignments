/* * * * * * *
 * Trie module for Assignment 2.
 *
 * created for COMP20007 Design of Algorithms 2020
 * implementation by Callum Johnson <callumj2@student.unimelb.edu.au>
 */
#include "trie.h"
#include "string_IO_a2.h"
#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

#define NUMBER_OF_POSSIBLE_CHARS 27
#define ASCII_OFFSET 'a'
#define TRIE_START_CHAR '^'
#define WORD_END_CHAR '$'
#define TASK_FAILED -1
#define UNASSIGNED -1

// two definitions to ensure our preorder traversal visits word endings first
#define WORD_END_INDEX 0
#define WORD_END_OFFSET 1

// function defaults
#define SEARCH_START_DEPTH 1
#define MAX_NUM_TO_PRINT 5

//**********************************************************  INITIALISING  ************************************************************
Trie *new_trie(){
  Trie *newTrie = (Trie *)mymalloc(sizeof(Trie));
  // newTrie->maxDepth = 0;
  newTrie->top = new_trie_node(TRIE_START_CHAR);
  // Unlike other trie nodes, the root should be initialised to a frequency of 0, going to 1 when the first word is added
  newTrie->top->frequency = 0;
  return newTrie;
}

TrieNode *new_trie_node(char c){
  TrieNode *newNode = (TrieNode *)mymalloc(sizeof(TrieNode));
  newNode->character = c;
  newNode->frequency = 1;
  // Allocate space for possible child nodes a-z
  newNode->children = initializeGateKeeperArray(NUMBER_OF_POSSIBLE_CHARS);
  return newNode;
}

// initializes an array of 27 (corresponding to a-z plus a 27th index for '$') gatekeeper structs to emulate
// possible paths from a node
GateKeeper *initializeGateKeeperArray(int nStructs){
  GateKeeper *newArray = (GateKeeper *)mymalloc(nStructs * sizeof(GateKeeper));
  // each 'gate' starts closed, preventing null pointer exceptions as a gate is only opened once it contains data
  // leading to a trie node
  for (int i = 0; i < nStructs; i ++){
    newArray[i].isOpen = false;
  }
  return newArray;
}

//**********************************************************  INSERTING  ************************************************************
// takes a string and inserts it into a trie
void insert_word(Trie *trie, Deque *string){
  // check if this string will constitute a new max tree depth
  // update_new_tree_depth(trie, deque_size(string));
  TrieNode *currNode = trie->top;
  trie_recursive_insert(currNode, string);
}

// recursive function to insert a word one character at a time into the trie
void trie_recursive_insert(TrieNode *currentNode, Deque *string){
  // increase the node's frequency as it has now been visited
  currentNode->frequency ++;
  // if the string is empty, add a '$' node to the current node or visit an existing one
  if (deque_size(string) == 0){
    visit_word_end(currentNode);
  }
  //otherwise continue adding the letters of the string into the trie
  else {
    int nextChar = deque_pop(string);
    // if the next character has already been added to this section of the trie, move to that node
    if (currentNode->children[get_modified_number(nextChar) + WORD_END_OFFSET].isOpen){
      TrieNode *nextNode = currentNode->children[get_modified_number(nextChar) + WORD_END_OFFSET].nextNode;
      trie_recursive_insert(nextNode, string);
    }
    // otherwise push the character back onto the string and use free_insert to place the rest of the word
    // into the trie
    else {
      deque_push(string, nextChar);
      trie_free_insert(currentNode, string);
    }
  }
}
// function used to insert the remainder of a string where a new path is being made in the tree
void trie_free_insert(TrieNode *currentNode, Deque *string){
  // while there are still elements left to insert
  while (deque_size(string) != 0){
    int nextChar = deque_pop(string);
    currentNode->children[get_modified_number(nextChar) + WORD_END_OFFSET].isOpen = true;
    currentNode->children[get_modified_number(nextChar) + WORD_END_OFFSET].nextNode = new_trie_node(nextChar);
    currentNode = currentNode->children[get_modified_number(nextChar) + WORD_END_OFFSET].nextNode;
  }
  // To finish, add a word ending node to this tree
  visit_word_end(currentNode);
}

void visit_word_end(TrieNode *currentNode){
  // if this word has been entered before, update it's frequency
  if (currentNode->children[WORD_END_INDEX].isOpen){
    currentNode->children[WORD_END_INDEX].nextNode->frequency ++;
  }
  // otherwise initiate a new word ending node
  else {
    currentNode->children[WORD_END_INDEX].isOpen = true;
    currentNode->children[WORD_END_INDEX].nextNode = new_trie_node(WORD_END_CHAR);
  }
}

//***********************************************  INFO FETCHING AND MANIPULATION (P2C) **************************************************

// Takes a trie and word stub returns an array containing all possible suffixes
// as well as their respective probabilities
WordProbInfo *get_sorted_suffix_probabilities(Trie *trie, Deque* prefix){
  TrieNode *currentNode = trie->top;
  int nSteps = deque_size(prefix), data;
  // Navigate to the node representing the end of the prefix in the trie
  for (int i = 0; i < nSteps ; i ++){
    data = deque_pop(prefix);
    if (currentNode->children[get_modified_number(data) + WORD_END_OFFSET].isOpen){
      deque_insert(prefix, data);
      currentNode = currentNode->children[get_modified_number(data) + WORD_END_OFFSET].nextNode;
    }
    // if there is no path to that prefix, return TASK FAILED
    else {
      exit_with_error("Prefix not in trie");
    }
  }
  // create a pointer to our word-probability data set 
  WordProbInfo *wordProbInfo = get_word_prob_pairs(currentNode, prefix);

  // sort the data set then return it
  word_prob_pair_sort(wordProbInfo);

  return wordProbInfo;
}

// Takes a Trie Node, prefix, and a starting node in the trie, and returns a Word-Probability Info
WordProbInfo *get_word_prob_pairs(TrieNode *currentNode, Deque *prefix){
  int i, nWords = currentNode->frequency;
  // initialize the info struct
  WordProbInfo *wordProbInfo = (WordProbInfo *)mymalloc(sizeof(WordProbInfo));

  // Initialize the wordProbPairs array to the maximum possible amount of entries, given by the frequency of the prefix node,
  // this array will be trimmed later
  WordProbPair *wordProbPairs = (WordProbPair *)mymalloc(nWords * sizeof(WordProbPair));

  // Initialize the array, such that each entry already contains the prefix and an UNASSIGNED probability
  for (i = 0; i < currentNode->frequency; i++){
    wordProbPairs[i].word = new_deque();
    deque_merge_copy(wordProbPairs[i].word, prefix);
    wordProbPairs[i].probability = UNASSIGNED;
  }
  
  add_word_prob_pairs(wordProbPairs, currentNode);

  // Trim the probability array before returning
  int arraySize = trim_prob_word_array(&wordProbPairs, nWords);
  // Reallocate the space in memory of our word prob pairs
  WordProbPair *newPairsLocation = realloc(wordProbPairs, arraySize * sizeof(WordProbPair));
  // Wrap the array size and sorted word-probability pairs and return them 
  wordProbInfo->wordProbPairs = newPairsLocation;
  wordProbInfo->arraySize = arraySize;
  return wordProbInfo;
}

// Takes a probability-word array and empties any words without assigned probabilities,
// returning the new size of the array
int trim_prob_word_array(WordProbPair **probArray, int nWords){
  int i, nTrimmed = 0;
  WordProbPair *data = *probArray;
  for (i = 0; i < nWords; i ++ ){
    if (data[i].probability == UNASSIGNED){
      free_deque(data[i].word);
      nTrimmed ++;
    }
  }
  int newSize = nWords - nTrimmed;
  return newSize;
}

void add_word_prob_pairs(WordProbPair *wordProbPairs, TrieNode *startNode){
  int i, currentArrayPos = 0, baseFrequency = startNode->frequency;
  // initialize the branch history - this variable's function is explained in recursive_prob_add
  Deque *branchHistory = new_deque();
  // For each child of the suffix, run add_word_prob_pairs to fill the word prob array
  for (i = 0; i < NUMBER_OF_POSSIBLE_CHARS; i ++){
    if (startNode->children[i].isOpen){
      currentArrayPos = recursive_prob_add(wordProbPairs, startNode->children[i].nextNode, branchHistory, currentArrayPos, baseFrequency);
    }
  }
  // free the allocated memory for the branch history
  free_deque(branchHistory);

}

// Recursive component of get_word_prob_pairs, iterates through the trie calculating suffixes and probability
int recursive_prob_add(WordProbPair *probArray, TrieNode *currentNode, Deque *branchHistory, int currentArrayPos,  int baseFrequency){
  // if this node is a '$', calculate the probability of this word and move on to the next array position
  if (currentNode->character == WORD_END_CHAR){
    float probability = (float)(currentNode->frequency) / baseFrequency;
    probArray[currentArrayPos].probability = probability;
    currentArrayPos ++;
  }

  else {
    // insert the character at this node into the current position in the word-prob array
    deque_insert(probArray[currentArrayPos].word, currentNode->character);
    deque_insert(branchHistory, currentNode->character);
    // While exploring this node, discovering any new nodes except for the first one we find guarantees that a new
    // word exists and as such the current branch history should be copied - or dumped - into the next
    // array position: 'shouldDump' helps to implement this principle
    bool shouldDump = false;
    for (int i = 0; i < NUMBER_OF_POSSIBLE_CHARS; i++){
      if (currentNode->children[i].isOpen){
        if (shouldDump){
          deque_merge_copy(probArray[currentArrayPos].word, branchHistory);
        }
        TrieNode *nextNode = currentNode->children[i].nextNode;
        currentArrayPos = recursive_prob_add(probArray, nextNode, branchHistory, currentArrayPos, baseFrequency);
        shouldDump = true;
      }
    }
    // Once this char has been fully explored, remove it from the branch history
    deque_remove(branchHistory);
  }
  return currentArrayPos;
}

// Takes an array of word-probability pairs and sorts them based on highest probability
void word_prob_pair_sort(WordProbInfo *probInfo){
  // fetch the information from the word-prob info struct
  int nWords = probInfo->arraySize;
  WordProbPair *wordProbPairs = probInfo->wordProbPairs;
  // Since getting the words from the trie naturally yields an alphabetical ordering, we will use a stable sorting 
  // algorithm to sort our words from highest to lowest probability
  // the following algorithm is an adapted version of Insertion Sort:
  float currentProb;
  WordProbPair tempEntry;
  for (int i = 1; i < nWords; i ++){
    // if we are not at the start of the array
    if (i > 0){
      currentProb = wordProbPairs[i].probability;
      // check if the current prob is higher than the previous, and if it is
      //swap it with the previous entry and repeat until it can no longer be switched with a lower value
      if (currentProb > wordProbPairs[i - 1].probability){
        tempEntry = wordProbPairs[i - 1];
        wordProbPairs[i - 1] = wordProbPairs[i];
        wordProbPairs[i] = tempEntry;
        // Move the iterator back 2 since at the end of the loop body it will be incremented by 1
        // and we wish to perform the same check 1 step back from the current
        i = i - 2;
      }
    }
  }
}

//**********************************************************  PRINTING  ************************************************************
// Takes a trie and given prefix and prints out all possible suffixes with their respective
// probabilities, ordered from highest probability to lowest
void print_sorted_suffix_probabilities(Trie *trie, Deque *prefix){
  // retrieve the possible suffixes from the trie sorted from highest probability to lowest
  WordProbInfo *probInfo = get_sorted_suffix_probabilities(trie, prefix);
  WordProbPair *probPairs = probInfo->wordProbPairs;
  // print out the top 5 suffixes, or all of them if there are 5 or less, along with their corresponding probabilities
  int nToPrint = MAX_NUM_TO_PRINT;
  if (probInfo->arraySize < nToPrint){
    nToPrint = probInfo->arraySize;
  }

  for (int i = 0; i < nToPrint; i ++){
    print_word_prob_pair(probPairs[i]);
  }
  // Cleanup any memory allocated
  free_prob_info_struct(probInfo);
}

// Takes a trie and prints all characters stored within it, ordered alphabetically
void print_trie_chars(Trie *trie){
  TrieNode *currentNode = trie->top;
  trie_preorder_traversal_print(currentNode);
}

// Recursively traverses a trie in preorder from a given node
void trie_preorder_traversal_print(TrieNode *currentNode){
  // print the current character
  printf("%c\n", currentNode->character);
  // Check the children of this node and if there are any possible paths, explore them
  if (currentNode->character != WORD_END_CHAR){
    for (int i = 0; i < NUMBER_OF_POSSIBLE_CHARS; i++){
      if (currentNode->children[i].isOpen){
        TrieNode *nextNode = currentNode->children[i].nextNode;
        trie_preorder_traversal_print(nextNode);
      }
    }
  }
}

// Takes a trie and a desired prefix length and prints all prefixes in the tree of
// that length, along with their corresponding frequencies
void trie_prefix_print(Trie *trie, int prefixLen){
  Deque *printQueue = new_deque();
  TrieNode *currentNode = trie->top;
  // Since we don't wish to print the tree root '^' only call print on it's valid children
    for (int i = 0; i < NUMBER_OF_POSSIBLE_CHARS; i++){
      if (currentNode->children[i].isOpen){
        TrieNode *nextNode = currentNode->children[i].nextNode;
        trie_recursive_prefix_print(nextNode, printQueue, prefixLen, SEARCH_START_DEPTH);
      }
    }
  // free the allocated memory
  free_deque(printQueue);
}

// Recursive component of trie_prefix_print funtion
void trie_recursive_prefix_print(TrieNode *currentNode, Deque *printQueue, int prefixLen, int currDepth){
  // add the current node to the print queue
  deque_insert(printQueue, currentNode->character);
  // if we are at the desired depth, print the stack and don't recurse further
  if (currDepth == prefixLen){
    print_trie_branch(printQueue, currentNode->frequency);
  }
  else {
    // check for open nodes, and visit them, however only search for open routes starting at 'a' since we don't want to
    // print '$' symbols
    for (int i = 1; i < NUMBER_OF_POSSIBLE_CHARS; i++){
      if (currentNode->children[i].isOpen){
        TrieNode *nextNode = currentNode->children[i].nextNode;
        trie_recursive_prefix_print(nextNode, printQueue, prefixLen, currDepth + 1);
      }
    }
  }
  // after the current node has been explored, remove it from the queue
  deque_remove(printQueue);
}

// Takes a pointer to a Deque and prints it in trie branch format
void print_trie_branch(Deque *string, int frequency){
  Node *currNode = string->top;
  int nPrints = deque_size(string);
  for (int i = 0; i < nPrints; i++){
    printf("%c", currNode->data);
    currNode = currNode->next;
  }
  printf(" %d\n", frequency);
}

// Takes a word-probability pair and prints it
void print_word_prob_pair(WordProbPair probPair){
  printf("%.2f ", probPair.probability);
  int charsInWord = deque_size(probPair.word);
  Node *currNode = probPair.word->top;
  for (int i = 0; i < charsInWord; i ++){
    printf("%c", currNode->data);
    currNode = currNode->next;
  }
  printf("\n");
}

//**********************************************************  CLEANUP  ************************************************************
// takes a trie and frees the memory associated with it
void free_trie(Trie *trie){
  TrieNode *currentNode = trie->top;
  recursive_free_trie(currentNode);
  free(trie);
}
// recursive component of free_trie, frees the gatekeeper arrays at each level
void recursive_free_trie(TrieNode *currentNode){
  // for each child, if it contains data, explore it
  for (int i = 0; i < NUMBER_OF_POSSIBLE_CHARS; i ++){
    if (currentNode->children[i].isOpen){
      recursive_free_trie(currentNode->children[i].nextNode);
    }
  }
  // free the children array of this node once it has been explored
  free(currentNode->children);
  // free the node itself
  free(currentNode);
}

// Takes the info associatied with a word-probability info struct
void free_prob_info_struct(WordProbInfo *probInfo){
  // for each element in the prob-word table free the word it contains
  WordProbPair *probPairs = probInfo->wordProbPairs;
  for (int i = 0; i < probInfo->arraySize; i++){
    free_deque(probPairs[i].word);
  }
  // free the table itself
  free(probInfo->wordProbPairs);
  // free the info strusct
  free(probInfo);
}