/* * * * * * *
 * Functionality used accross the Assignment 1 program
 *
 * created for COMP20007 Design of Algorithms 2020
 * by Tobias Edwards <tobias.edwards@unimelb.edu.au>
 * edited by Callum Johnson <callumj2@student.unimelb.edu.au>
 */

#include <stdio.h>
#include <stdlib.h>
#include <assert.h>
#include <stddef.h>
#include "util.h"

// Prints an error of the format "error: <error message>" and exits the
// program with a non-zero error code
void exit_with_error(char *error) {
  fprintf(stderr, "error: %s\n", error);
  exit(EXIT_FAILURE);
}

// Scans a character from stdin and returns it
int mygetchar() {
    int c;
    while ((c=getchar())=='\r')
    {
    }
    return c;
}

// Custom malloc function with self contained assert
void *mymalloc(size_t memSize){
  void *memoryAllocated = malloc(memSize);
  assert(memoryAllocated);
  return memoryAllocated;
}


