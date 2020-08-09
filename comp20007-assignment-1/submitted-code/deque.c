/* * * * * * *
 * Deque module (i.e., double ended queue) for Assignment 1
 *
 * created for COMP20007 Design of Algorithms 2020
 * template by Tobias Edwards <tobias.edwards@unimelb.edu.au>
 * implementation by Callum Johnson <callumj2@student.unimelb.edu.au>
 */

// You must not change any of the code already provided in this file, such as
// type definitions, constants or functions.
//
// You may, however, add additional functions and/or types which you may need
// while implementing your algorithms and data structures.

#include <assert.h>
#include <stdio.h>
#include <stdlib.h>

#include "deque.h"
#include "util.h"

// Create a new empty Deque and return a pointer to it
Deque *new_deque() {
	Deque *deque = malloc(sizeof(*deque));
	assert(deque);

	deque->top = NULL;
	deque->bottom = NULL;
	deque->size = 0;

	return deque;
}

// Free the memory associated with a Deque
void free_deque(Deque *deque) {
  // Remove (and thus free) all of the nodes in the Deque.
  while (deque->size > 0) {
    deque_remove(deque);
  }

	// Free the deque struct itself
	free(deque);
}

// Create a new Node with a given piece of data
Node *new_node(Data data) {
  Node *node = malloc(sizeof(*node));
  assert(node);

  node->next = NULL;
  node->prev = NULL;
  node->data = data;

  return node;
}

// Free the memory associated with a Node
void free_node(Node *node) {
  free(node);
}

// Add an element to the top of a Deque
void deque_push(Deque *deque, Data data) {
  Node *new = new_node(data);

  if (deque->size > 0) {
    new->next = deque->top;
    deque->top->prev = new;
  } else {
    // If the Deque was initially empty then new is both the top and bottom
    deque->bottom = new;
  }

  deque->top = new;
  deque->size++;
}

// Add an element to the bottom of a Deque
void deque_insert(Deque *deque, Data data) {
  Node *new = new_node(data);

  if (deque->size > 0) {
    new->prev = deque->bottom;
    deque->bottom->next = new;
  } else {
    // If the Deque was initially empty then new is both the top and bottom
    deque->top = new;
  }

  deque->bottom = new;
  deque->size++;
}

// Remove and return the top element from a Deque
Data deque_pop(Deque *deque) {
  if (deque->size == 0) {
    exit_with_error("can't pop from empty Deque");
  }

  Data data = deque->top->data;
  Node *old_top = deque->top;

  if (deque->size == 1) {
    deque->top = NULL;
    deque->bottom = NULL;
  } else {
    deque->top = old_top->next;
    deque->top->prev = NULL;
  }

  deque->size--;

  free(old_top);

  return data;
}

// Remove and return the bottom element from a Deque
Data deque_remove(Deque *deque) {
  if (deque->size == 0) {
    exit_with_error("can't remove from empty Deque");
  }

  Data data = deque->bottom->data;
  Node *old_bottom = deque->bottom;

  if (deque->size == 1) {
    deque->top = NULL;
    deque->bottom = NULL;
  } else {
    deque->bottom = old_bottom->prev;
    deque->bottom->next = NULL;
  }

  deque->size--;

  free(old_bottom);

  return data;
}

// Return the number of elements in a Deque
int deque_size(Deque *deque) {
  return deque->size;
}

// Print the Deque on its own line with the following format:
//   [x_1, x_2, ..., x_n]
//     ^              ^
//    top           bottom
void print_deque(Deque *deque) {
  Node *current = deque->top;
  int i = 0;

  printf("[");

  while (current) {
    printf("%d", current->data);
    // Print a comma unless we just printed the final element
    if (i < deque->size - 1) {
      printf(", ");
    }
    current = current->next;
    i++;
  }

  printf("]\n");
}

// Reverse the Deque using an iterative approach
void iterative_reverse(Deque *deque) {

  // For each element in the queue, swap it's prev/next values.
  // the only cases which require a different operation are the top and bottom of the deque

  // We will define our first element as the top of the deque, and initialise some variables
  Node *element = deque->top, *old_next, *old_top;

  // Swap pointers for the first element
  element->prev = element->next;
  element->prev = NULL;

  // While there is still a next element we can continue to swap prev and next values
  while (element->next != NULL)
  {
    old_next = element->next;
    element->next = element->prev;
    element->prev = old_next;
    element = element->prev;
  }
  // Now swap pointers for the last element
  element->next = element->prev;
  element->prev = NULL;
    
  // Reassign pointers to the new 'top' and 'bottom'
  old_top = deque->top;
  deque->top = deque->bottom;
  deque->bottom = old_top;
}

// Reverse the Deque using a recursive approach
void recursive_reverse(Deque *deque) {
  // Recursive Case, pop the top of the queue and run the function again before inserting the value again
  if (deque->size != 1)
  {
    // As we recurse we pop items off the deque
    Data topval = deque_pop(deque);
    recursive_reverse(deque);
    // then once the bottom is reached re add them in the order they were removed
    deque_insert(deque, topval);
  }
  // if the deque only has one element we do not need to do anything
}

// Split the Deque given a critical value k, such that the Deque contains
// all elements greater than equal to k above (i.e., closer to the top)
// the elements less than k.
//
// Within the two parts of the array (>= k and < k) the elements should
// be in their original order.
//
// This function must run in linear time.

void split_deque(Deque *deque, int k) {
  // For each element in the deque, if it's less than k we will put it in another deque,
  // if it is greater than or equal to, place it on the bottom of the original deque
  int count, examine, original_size = deque_size(deque);
  Deque *alt_deque = new_deque();
  for (count = 0; count < original_size; count++){
    examine = deque_pop(deque);
    if (examine >= k){
      deque_insert(deque, examine);
    }
    else {
      deque_insert(alt_deque, examine);
    }   
  }

  // If no elements were larger than the critical value, return the alt_deque as the split deque
  if (deque_size(deque) == 0){
    deque->top = alt_deque->top;
    deque->bottom = alt_deque->bottom;
    deque->size = alt_deque->size;
  }
  // if we have two deques with a non-zero amount of entries, merge them
  else if (deque_size(alt_deque) != 0){
    deque->bottom->next = alt_deque->top;
    alt_deque->top->prev = deque->bottom;
    deque->bottom = alt_deque->bottom;
    deque->size = deque_size(deque) + deque_size(alt_deque);
  }
  // otherwise if all elements were larger than the critical value then the deque will remain unchanged

  // free the extra memory allocated, we don't use the free_deque() function here as if the alt_deque is
  // non empty, it's constituents are now part of the original deque and we want to keep them.
  free(alt_deque);
}

