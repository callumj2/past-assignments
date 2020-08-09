/* * * * * * *
 * Park Ranger module for Assignment 1
 *
 * created for COMP20007 Design of Algorithms 2020
 * template by Tobias Edwards <tobias.edwards@unimelb.edu.au>
 * implementation by Callum Johnson <callumj2@student.unimelb.edu.au>
 */

#ifndef PARKRANGER_H
#define PARKRANGER_H

#include <stdbool.h>

// reads vertices from stdin into an array of deques
Deque **get_vertices(int nTrees, int mEdges); 

// topologically sort a ski slope graph
int *dfs_sort(Deque **vertices, int nTrees, int *visited); 

// recursive component of dfs_sort
int dfs_search(Deque **vertices, int *topnum, int *visited, int v, int n); 

// check if a topsort implies a single run is possible
bool check_topsort(int *topnum, Deque **vertices, int nTrees); 

// This function reads in a ski slope map and determine whether or not
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
//   0 1                         /  1
//   0 2                        / /
//   1 2                        2
//   2 3                          \
//                                 3
//
// In this example the program returns `true` as there is a way to trim
// all trees in a single run. This run is (0, 1, 2, 3).
//
// The function:
//  - Reads in this data from stdin
//  - Stores this data in an appropriate data structure
//  - Rusn the algorithm you have designed to solve this problem
//  - Does any clean up required (e.g., free allocated memory)
//  - Returns `true` or `false` (included in the stdbool.h library)
//
// For full marks your algorithm must run in O(n + m) time.
bool is_single_run_possible();


#endif
