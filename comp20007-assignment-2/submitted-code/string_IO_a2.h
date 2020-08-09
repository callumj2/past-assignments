/* * * * * * *
 * String input module for Assignment 2.
 *
 * created for COMP20007 Design of Algorithms 2020
 * 
 * Made by Callum Johnson <callumj2@student.unimelb.edu.au>
 */

#include "deque.h"
#include <stdbool.h>

// Takes the next line of input from stdin and returns it as a character deque
Deque *get_next_line();

// Takes an integer number of lines to read in and returns an array of character deques
// with each entry corresponding to a line of input
Deque **get_input_strings(int nStrings);

// Takes a string and modifies it's integer representation according to p1a of a2_spec
void modify_line(Deque *string);

// Takes an ascii character and returns it's modified value
int get_modified_number(int c);

// Takes a string in the form of a character deque and prints it
void print_string(Deque *string);

// Takes a modified integer input and prints the corresponding character,
void print_char(int c);


