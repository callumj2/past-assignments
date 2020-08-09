/* * * * * * *
 * Park Ranger module for Assignment 1
 *
 * created for COMP20007 Design of Algorithms 2020
 * template by Tobias Edwards <tobias.edwards@unimelb.edu.au>
 * implementation by Callum Johnson <callumj2@student.unimelb.edu.au>
 */

#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <assert.h>
#include <stdio.h>
#include <stdlib.h>
#include <assert.h>
#include "deque.h"
#include "parkranger.h"
#include "util.h"

// This function must read in a ski slope map and determine whether or not
// it is possible for the park ranger to trim all of the trees on the ski slope
// in a single run starting from the top of the mountain.
//
// The ski slope map is provided via stdin in the following format:
//
//   n m
//   from to
//   from to
//   ...
//   from to
//
// Here n denotes the number of trees that need trimming, which are labelled
// {1, ..., n}. The integer m denotes the number "reachable pairs" of trees.
// There are exactly m lines which follow, each containing a (from, to) pair
// which indicates that tree `to` is directly reachable from tree `from`.
// `from` and `to` are integers in the range {0, ..., n}, where {1, ..., n}
// denote the trees and 0 denotes the top of the mountain.
//
// For example the following input represents a ski slope with 3 trees and
// 4 reachable pairs of trees.
//
// input:            map:          0
//   3 4                          / \
//   0 1                         /  2
//   0 2                        / /
//   2 1                        1
//   1 3                          \
//                                 3
//
// In this example your program should return `true` as there is a way to trim
// all trees in a single run. This run is (0, 2, 1, 3).
//
// Your function should must:
//  - Read in this data from stdin
//  - Store this data in an appropriate data structure
//  - Run the algorithm you have designed to solve this problem
//  - Do any clean up required (e.g., free allocated memory)
//  - Return `true` or `false` (included in the stdbool.h library)
//
// For full marks your algorithm must run in O(n + m) time.

int *dfs_sort(Deque **vertices, int nTrees, int *visited);
int dfs_search(Deque **vertices, int *topnum, int *visited, int v, int n);

bool is_single_run_possible() {
  
  //*********************************READING INPUT STAGE***************************************
  int nTrees, mEdges;
  scanf("%d%d", &nTrees, &mEdges);
  // We will increase the amount of 'trees' by one to included our starting position 0, so that 'nTrees' represents
  // the number of nodes in the scenario
  nTrees ++;

  // We will use an array of deques to read in in info about the edges 
  Deque **vertices = (Deque **)malloc((nTrees) * sizeof(Deque *));
  assert(vertices);
  int counter, start, finish, current_vertex = 0;
  for (counter = 0; counter < nTrees; counter ++){
    vertices[counter] = new_deque();
  }
  for (counter = 0; counter < mEdges; counter ++){
    scanf("%d%d", &start, &finish);
    if (start != current_vertex)
    {
      current_vertex = start;
    }
    deque_insert(vertices[current_vertex], finish);
  }

  //*********************************TOPSORT STAGE*********************************************

  // We are going to implement a DFS algorithm to topologically sort the nodes (trees) on the mountain
  // Create a new array to hold our topSort info, in this array topnum[n] = the number that node n is given in the topological ordering

  // Create an array to keep track of which vertices have been visited, where visited_nodes[n] = a boolean value to indicate whether
  // tree n has been visited or not
  int *visited = (int *)calloc(nTrees, sizeof(int));
  assert(visited);
  int *topnum = dfs_sort(vertices, nTrees, visited);

  //*********************************VERIFY PATH STAGE*********************************************
  // if any nodes were not visited in the dfs_sort, it means there are nodes unreachable from the start (0) so we should return false
  for (counter = 0; counter < nTrees; counter++){
    if (visited[counter] == false){
      return false;
    }
  }

  // for each step in the topsort, check that moving from one node to the next is valid 
  // (This will involve a time complexity O(m)) since each node is only visited once
  Deque *currentNode;
  int nextNode, validMove, i, examine, edgesToExamine;
  bool singleRunPossible = true;

  // While there are still steps in the topSort to check and while a single run is still possible, continue to 
  // check for valid moves
  for (counter = 1; counter < nTrees; counter ++){

    validMove = false;
    nextNode = topnum[counter];
    currentNode = vertices[topnum[counter - 1]];
    edgesToExamine = deque_size(currentNode);
    for (i = 0; i < edgesToExamine; i ++){
      examine = deque_pop(currentNode);
      // If the next node is in the adjacency list for the previous node then the move is valid
      if (examine == nextNode){
        validMove = true;
      }
    }
    // if each edge has been checked for the previous node and no valid move was found, then a single run is not possible.
    if (validMove == false){
      singleRunPossible = false;
      break;
    }
  }
  // If we reach this point in the program, then all the trees on the mountain can be topologically sorted in a way that 
  // lets the groundskeeper reach every tree on his way down the mountain so we can clean up memory and return true.

  for (counter = 0; counter < nTrees; counter ++){
    free_deque(vertices[counter]);
  }
  free(vertices);
  free(topnum);
  free(visited);

  return singleRunPossible;
}

//*********************************HELPER FUNCTIONS*********************************************

// Explore the graph, return an array containing the topsorted nodes in order from the top of the mountain to the bottom
int *dfs_sort(Deque **vertices, int nTrees, int *visited){
  // Initialise our array of topsorted numbers where topnum[n] = vertice n when traversing the topsorted nodes
  int *topnum = (int *)malloc(nTrees * sizeof(int));
  assert(topnum);
  // visited initialisation was here
  // v = start vertex, n = current topological number (start at n-1 so that the last entry is 0)
  int v = 0, n = (nTrees - 1);
  dfs_search(vertices, topnum, visited, v, n);
  return topnum;
}

// The 'search' part of our dfs algorithm recurses through exploring each possible route from the current node
int dfs_search(Deque **vertices, int *topnum, int *visited, int v, int n){
  visited[v] = true;
  int i, newNode;
  Deque *currNode = vertices[v];
  // Examine each possible route from the current node
  // if there is no outgoing edges this part will skip and assign the current node as the end of the top sort
  for (i = 0; i < deque_size(currNode); i ++){
    newNode = deque_pop(currNode);
    // Check if the node has been visited yet, if not then explore it and update the current n value.
    if (!visited[newNode]){
      n = dfs_search(vertices, topnum, visited, newNode, n);
    }
    deque_insert(currNode, newNode);
  }

  // Once explored, place the current node's topnum into the topnum array
  topnum[n] = v;
  return (n - 1);
}