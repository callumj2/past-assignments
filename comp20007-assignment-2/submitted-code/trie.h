/* * * * * * *
 * Trie module for Assignment 2.
 *
 * created for COMP20007 Design of Algorithms 2020
 * implementation by Callum Johnson <callumj2@student.unimelb.edu.au>
 */
#ifndef TRIE_H
#define TRIE_H

#include "deque.h"
#include "util.h"
#include <stdbool.h>

// Our trie is a modified linked list
typedef struct trie_t Trie;

// The nodes that will make up the trie
typedef struct trienode_t TrieNode;

// Special 'gate' struct that will keep track of whether searching down through itself is viable
typedef struct gatekeeper_t GateKeeper;

// Wrapper Class for our Word-Probability info in Q2C
typedef struct wordprobinfo_t WordProbInfo;
// A structure linking a word to it's frequency-based probability relative to a given parent suffix
typedef struct wordprobpair_t WordProbPair;

struct trie_t {
    TrieNode *top;
};

struct trienode_t {
    char character;
    int frequency;
    GateKeeper *children;
};

struct gatekeeper_t {
    bool isOpen;
    TrieNode *nextNode;
};

struct wordprobinfo_t {
    int arraySize;
    WordProbPair *wordProbPairs;   
};

struct wordprobpair_t {
    float probability;
    Deque *word;
};

//*********************************************************  INITIALISING  **********************************************************

// Create a new Trie containing only the root node '^' and return a pointer to it
Trie *new_trie();

// Create a new Trie Node and return a pointer t it
TrieNode *new_trie_node(char c);

//**********************************************************  INSERTING  ************************************************************

// Insert a new word into the trie
void insert_word(Trie *trie, Deque *string);

// The recursive component of insert_word
void trie_recursive_insert(TrieNode *currentNode, Deque *string);

// Special case of insertion where the remainder of a word is completely new to the trie
void trie_free_insert(TrieNode *currentNode, Deque *string);

// Special case of insertion for adding a word ending node to a trie
void visit_word_end(TrieNode *currentNode);

//***********************************************  INFO FETCHING AND MANIPULATION (P2C) **************************************************

// Takes a trie and word stub returns an array of all possible suffixes and their respective
// probabilities
WordProbInfo *get_sorted_suffix_probabilities(Trie *trie, Deque* prefix);

// Takes a probability-word array and removes any non-assigned elements, returning the new size of the array
int trim_prob_word_array(WordProbPair **probArray, int nWords);

// Takes a Trie Node, prefix, and a starting node in the trie, and returns a Word-Probability Info Class
WordProbInfo *get_word_prob_pairs(TrieNode *currentNode, Deque *prefix);

// Takes a position in memory to hold word-probability pairs and a starting node in a trie, and fills it
// by traversing the tree
void add_word_prob_pairs(WordProbPair *wordProbPairs, TrieNode *startNode);

// Recursive component of add_word_prob_pairs, iterates through the trie calculating suffixes and probability, returns the current
// position for insertion into the word-probability array
int recursive_prob_add(WordProbPair *probArray, TrieNode *currentNode, Deque *branchHistory, int currentArrayPos,  int baseFrequency);

// Takes an array of word-probability pairs and sorts them based on highest probability
void word_prob_pair_sort(WordProbInfo *wordProbInfo);

//**********************************************************  PRINTING  *************************************************************
// Takes a trie and given prefix and prints out all possible suffixes with their respective
// probabilities, ordered from highest probability to lowest
void print_sorted_suffix_probabilities(Trie *trie, Deque *prefix);

// Takes a trie and prints all characters stored within it, ordered alphabetically
void print_trie_chars(Trie *trie);

// Takes a pointer to a Deque corresponding to a branch
// in a trie and prints it followed by it's frequency
void print_trie_branch(Deque *string, int frequency);

// The recursive component of print_trie
void trie_preorder_traversal_print(TrieNode *currentNode);

// Takes a trie and a desired prefix length and prints all prefixes in the tree of
// that length, along with their corresponding frequencies
void trie_prefix_print(Trie *trie, int prefixLen);

// Recursive component of trie_prefix_print funtion
void trie_recursive_prefix_print(TrieNode *currentNode, Deque *printQueue, int prefixLen, int currDepth);

// Initialize an array of n gatekeeper structs
GateKeeper *initializeGateKeeperArray(int nStructs);

// Takes a word-probability pair and prints it
void print_word_prob_pair(WordProbPair probPair);

//**********************************************************  CLEANUP  ************************************************************
// Takes a trie and frees the memory associated with it
void free_trie(Trie *trie);

// recursive component of free_trie, frees the gatekeeper arrays at each level
void recursive_free_trie(TrieNode *currentNode);

// Takes the info associatied with a word-probability info struct
void free_prob_info_struct(WordProbInfo *probInfo);
#endif
