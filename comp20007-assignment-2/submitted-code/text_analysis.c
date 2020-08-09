/* * * * * * *
 * Text Analysis module for Assignment 2.
 *
 * created for COMP20007 Design of Algorithms 2020
 * template by Tobias Edwards <tobias.edwards@unimelb.edu.au>
 * implementation by Callum Johnson <callumj2@student.unimelb.edu.au>
 */
#include "text_analysis.h"
#include "string_IO_a2.h"
#include <stdlib.h>
#include <stdio.h>


// Build a character level trie for a given set of words.
//
// The input to your program is an integer N followed by N lines containing
// words of length < 100 characters, containing only lowercase letters.
//
// Your program should build a character level trie where each node indicates
// a single character. Branches should be ordered in alphabetic order.
//
// Your program must output the pre-order traversal of the characters in
// the trie, on a single line.
void problem_2_a(){

  // read in the number of strings
  int nStrings, i;
  scanf("%d\n", &nStrings);

  // initialise the trie
  Trie *trie = new_trie();

  // for each line of input, read the line into the trie then free the line
  for (i = 0; i < nStrings; i++){
    Deque *nextLine = get_next_line();
    insert_word(trie, nextLine);
    free_deque(nextLine);
  }

  //print the trie preorder traversal
  print_trie_chars(trie);

  // free the memory associated with the trie
  free_trie(trie);
}


// Using the trie constructed in Part (a) this program should output all
// prefixes of length K, in alphabetic order along with their frequencies
// with their frequencies. The input will be:
//   n k
//   str_0
//   ...
//   str_(n-1)
// The output format should be as follows:
//   an 3
//   az 1
//   ba 12
//   ...
//   ye 1
void problem_2_b() {
  // read in the number of strings and desired word length
  int nStrings, prefixLen, i;
  scanf("%d %d\n", &nStrings, &prefixLen);

  // initialise the trie
  Trie *trie = new_trie();

  // for each line of input, read the line into the trie, then free the line
  for (i = 0; i < nStrings; i++){
    Deque *nextLine = get_next_line();
    insert_word(trie, nextLine);
    free_deque(nextLine);
  }

  // print out the desired prefixes
  trie_prefix_print(trie, prefixLen);

  // free the trie
  free_trie(trie);
}

// Again using the trie data structure you implemented for Part (a) you will
// provide a list (up to 5) of the most probable word completions for a given
// word stub.
//
// For example if the word stub is "al" your program may output:
//   0.50 algorithm
//   0.25 algebra
//   0.13 alright
//   0.06 albert
//   0.03 albania
//
// The probabilities should be formatted to exactly 2 decimal places and
// should be computed according to the following formula, for a word W with the
// prefix S:
//   Pr(word = W | stub = S) = Freq(word = W) / Freq(stub = S)
//
// The input to your program will be the following:
//   n
//   stub
//   str_0
//   ...
//   str_(n-1)
// That is, there are n + 1 strings in total, with the first being the word
// stub.
//
// If there are two strings with the same probability ties should be broken
// alphabetically (with "a" coming before "aa").
void problem_2_c() {

  // read in the number of strings and the desired word stub
  int nStrings;
  scanf("%d\n", &nStrings);
  Deque *wordStub = get_next_line();

  // create a new trie
  Trie *trie = new_trie();

  // for each line of input, read the line into the trie, then free the line
  for (int i = 0; i < nStrings; i++){
    Deque *nextLine = get_next_line();
    insert_word(trie, nextLine);
    free_deque(nextLine);
  }
  // print the word probabilities
  print_sorted_suffix_probabilities(trie, wordStub);

  // free the memory allocated
  free_deque(wordStub);
  free_trie(trie);
}
