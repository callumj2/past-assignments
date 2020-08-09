/* * * * * * *
 * String input module for Assignment 2.
 *
 * created for COMP20007 Design of Algorithms 2020
 * 
 * Made by Callum Johnson <callumj2@student.unimelb.edu.au>
 */

#include "string_IO_a2.h"
#include "deque.h"
#include "util.h"
#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

#define NEWLINE_CHAR '\n'
#define TASK_FAILED -1
// ASCII table references
#define TRIE_START 94
#define WORD_END 36
#define UPPERCASE_A 65
#define UPPERCASE_Z 90
#define LOWERCASE_A 97
#define LOWERCASE_Z 122
#define ASCII_ZERO 48
#define ASCII_NINE 57
// New Character Offsets
#define UPPERCASE_LOWER_BOUND 26
#define INTEGER_LOWER_BOUND 52 
#define INTEGER_UPPER_BOUND 61

//************************************************************  INPUT HANDLING  ************************************************************
// Takes the next line of input from stdin and returns it as a character deque
Deque *get_next_line(){
  Deque *string = new_deque();
  // while we haven't yet hit a newline character, read in the next character from stdin
  char c;
  while ((c = mygetchar()) != NEWLINE_CHAR){
    deque_insert(string, c);
  }
  return string;
}

// Takes an integer number of lines to read in and returns an array of character deques
// with each entry corresponding to a line of input
Deque **get_input_strings(int nStrings){
  // create an array to store each line's modified integer values in
  Deque **inputStrings = (Deque**)mymalloc(nStrings * sizeof(Deque *));
  // fill our inputStrings array with lines of input
  for (int i = 0; i < nStrings; i ++) {
    inputStrings[i] = get_next_line();
    modify_line(inputStrings[i]);
  }
  return inputStrings;
}

//**********************************************************  STRING MANIPULATION  ************************************************************
void modify_line(Deque *string){
    Node *currentNode = string->top;
    int nModifications = deque_size(string), i;
    // map each character in the string to a modified integer value as specified in a2_spec
    for (i = 0; i < nModifications; i ++){
        currentNode->data = get_modified_number(currentNode->data);
        currentNode = currentNode->next;
    }
}
// takes an ascii character and returns it's modified value for hashing
int get_modified_number(int c){
  // if the character is between 'a' and 'z'
  if (LOWERCASE_A <= c && c <= LOWERCASE_Z){
    return c - LOWERCASE_A;
  }
  // if the character is between 'A' and 'Z'
  else if (UPPERCASE_A <= c && c <= UPPERCASE_Z){
    return c - UPPERCASE_A + UPPERCASE_LOWER_BOUND;
  }
  // if the character is an integer
  else if (ASCII_ZERO <= c && c <= ASCII_NINE){
    return c - ASCII_ZERO + INTEGER_LOWER_BOUND;
  }
	
  else {
    return TASK_FAILED;
  }
}

//*************************************************************  STRING PRINTING  ************************************************************
// Takes a string in the form of a character deque and prints it
void print_string(Deque *string){
  Node *currNode = string->top;
  int i;
  for (i = 0; i < deque_size(string); i++){
    print_char(currNode->data);
    currNode = currNode->next;
  }
}

// takes a modified integer input and prints the corresponding character,
void print_char(int c){
  // if the character is between 'a' and 'z', note that this will also work for strings entered in p1b
  if (0 <= c && c < UPPERCASE_LOWER_BOUND){
    c += LOWERCASE_A;
    printf("%c", c);
  }
  // if the character is between 'A' and 'Z'
  else if (UPPERCASE_LOWER_BOUND <= c && c < INTEGER_LOWER_BOUND){
    c = c + UPPERCASE_A - UPPERCASE_LOWER_BOUND;
    printf("%c", c);
  }
  // if the character is an integer
  else if (INTEGER_LOWER_BOUND <= c && c <= INTEGER_UPPER_BOUND){
    c = c + ASCII_ZERO - INTEGER_LOWER_BOUND;
    printf("%c", c);
  }
}