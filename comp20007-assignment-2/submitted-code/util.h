/* * * * * * *
 * Functionality used across the Assignment 1 program
 *
 * created for COMP20007 Design of Algorithms 2020
 * by Tobias Edwards <tobias.edwards@unimelb.edu.au>
 * edited by Callum Johnson <callumj2@student.unimelb.edu.au>
 */

#include <stddef.h>

#ifndef UTIL_H
#define UTIL_H


// Prints an error of the format "error: <error message>" and exits the
// program with a non-zero error code
void exit_with_error(char *error);

// Scans a character from stdin and returns it
int mygetchar();

// Custom malloc function with self contained assert
void *mymalloc(size_t memSize);
#endif
