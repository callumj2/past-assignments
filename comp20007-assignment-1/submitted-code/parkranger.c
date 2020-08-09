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

// reads vertices from stdin into an array of deques
Deque **get_vertices(int nTrees, int mEdges); 

// topologically sorts a ski slope graph
int *dfs_sort(Deque **vertices, int nTrees, int *visited); 

// recursive component of dfs_sort
int dfs_search(Deque **vertices, int *topnum, int *visited, int v, int n);

// checks if a topsort implies a single run is possible
bool check_topsort(int *topnum, Deque **vertices, int nTrees); 


// This function reads in a ski slope map and determine whether or not
// it is possible for the park ranger to trim all of the trees on the ski slope
// in a single run starting from the top of the mountain.
bool is_single_run_possible() {
  
  //*********************************READING INPUT STAGE**********************************************
  int nTrees, mEdges;
  scanf("%d%d", &nTrees, &mEdges);
  // We will increase the amount of 'trees' by one to included our starting position 0, so that 'nTrees'
  // represents the number of nodes in the scenario
  nTrees ++;

  // We will use an array of deques to read info about the edges, forming an adjacency list
  Deque **vertices = get_vertices(nTrees,mEdges);

  //*********************************TOPSORT STAGE****************************************************

  // We are going to implement a DFS algorithm to topologically sort the nodes (trees) on the mountain
  // Create a new array to hold our topSort info, in this array topnum[n] = the number that node n is 
  // given in the topological ordering

  // Create an array to keep track of which vertices have been visited, where a value of 0
  // indicates an unvisited node
  int *visited = (int *)calloc(nTrees, sizeof(int));
  assert(visited);
  int *topnum = dfs_sort(vertices, nTrees, visited);

  //*********************************VERIFY PATH STAGE***********************************************
  // if any nodes were not visited in the dfs_sort, it means there are nodes unreachable from 
  // the start (0) so a single run is not possible
  int counter;
  bool singleRunPossible = true;
  for (counter = 0; counter < nTrees; counter++){
    if (visited[counter] == false){
      singleRunPossible = false;
      break;
    }
  }

  // if a single run is still possible, check if there is a valid single run inside the topsort 
  // ordering
  if (singleRunPossible){
    singleRunPossible = check_topsort(topnum, vertices, nTrees);
  }

  // at this point, if the topological ordering has not yet failed a test then we can confirm that
  // a single run is possible, free the remaining allocated memory and return the result
  for (counter = 0; counter < nTrees; counter ++){
    free_deque(vertices[counter]);
  }
  free(vertices);
  free(topnum);
  free(visited);

  return singleRunPossible;
}
// End of main function



//*********************************HELPER FUNCTIONS*********************************************

// Reads input from stdin and returns an adjacency list for the graph
Deque **get_vertices(int nTrees, int mEdges){
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
  return vertices;
}


// Explore the graph, return an array containing the topologically sorted nodes from the 
// top of the mountain to the bottom
int *dfs_sort(Deque **vertices, int nTrees, int *visited){
  // Initialise our array of topsorted numbers where topnum[n] = the vertice corresponding to
  // the nth step in the topsort ordering
  int *topnum = (int *)malloc(nTrees * sizeof(int));
  assert(topnum);
  // v = start vertex, n = current topological number (start at n-1 so that the last entry is 0)
  int v = 0, n = (nTrees - 1);
  dfs_search(vertices, topnum, visited, v, n);
  return topnum;
}

// The 'search' part of our dfs algorithm recurses through exploring each possible route 
// from the current node
int dfs_search(Deque **vertices, int *topnum, int *visited, int v, int n){
  visited[v] = true;
  int i, newNode;
  Deque *currNode = vertices[v];
  // Examine each possible route from the current node
  // if there is no outgoing edges this part will skip and assign the current node 
  // as the nth element in the topological ordering
  for (i = 0; i < deque_size(currNode); i ++){
    newNode = deque_pop(currNode);
    // Check if the node has been visited yet, if not then explore it and update 
    // the current n value.
    if (!visited[newNode]){
      n = dfs_search(vertices, topnum, visited, newNode, n);
    }
    // place the examined vertice back into the adjacency list
    deque_insert(currNode, newNode);
  }

  // Once explored, place the current node into the nth 
  // poisition in the topnum array
  topnum[n] = v;
  return (n - 1);
}

// Takes a topsort ordering, the number of nodes in the topsort and a corresponding adjacency list, 
// and returns whether the ordering can be completed in a 'single run'
bool check_topsort(int *topnum, Deque **vertices, int nTrees){
    
    // for each step in the topsort, check that moving from one node to the next is valid 
    // (This will involve a time complexity O(m)) since each node is only visited once
    Deque *currentNode;
    int i, j, nextNode, validMove, examine, edgesToExamine;
    for (i = 1; i < nTrees; i ++){

      validMove = false;
      nextNode = topnum[i];
      currentNode = vertices[topnum[i - 1]];
      edgesToExamine = deque_size(currentNode);
      // examine each edge for validity
      for (j = 0; j < edgesToExamine; j ++){
        examine = deque_pop(currentNode);
        // If the next node is in the adjacency list for the previous node then the move is valid
        if (examine == nextNode){
          validMove = true;
        }
      }
      // if each edge has been checked for the previous node and no valid move was found, then a single run is not possible.
      if (validMove == false){
        return false;
      }
    }
    // if every move in the topsort is valid then we can return that a single run is possible
    return true;
}