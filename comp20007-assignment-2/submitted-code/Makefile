# Makefile for Assignment 2
#
# created for COMP20007 Design of Algorithms 2020
# written by Tobias Edwards <tobias.edwards@unimelb.edu.au>
# changes by Callum Johnson <callumj2@student.unimelb.edu.au>

# The compiler flags used when compiling
CFLAGS=-Wall -g -Wno-comment -std=c99

a2: main.o hash.o text_analysis.o trie.o string_IO_a2.o deque.o util.o 
	gcc $(CFLAGS) -o a2 main.o hash.o text_analysis.o trie.o string_IO_a2.o deque.o util.o

main.o: main.c hash.h text_analysis.h
	gcc $(CFLAGS) -c main.c

hash.o: hash.c hash.h
	gcc $(CFLAGS) -c hash.c

text_analysis.o: text_analysis.c text_analysis.h
	gcc $(CFLAGS) -c text_analysis.c

# added packages
trie.o: trie.c trie.h deque.h
	gcc $(CFLAGS) -c trie.c

string_IO_a2.o: string_IO_a2.c string_IO_a2.h deque.h
	gcc $(CFLAGS) -c string_IO_a2.c

deque.o: deque.c deque.h util.h
	gcc $(CFLAGS) -c deque.c

util.o: util.c util.h
	gcc $(CFLAGS) -c util.c

# Add or update any of these rules to include additional files/modules you may
# include. You can add additional compiler flags as well, however you should
# not remove the -Wall flag.

# This .PHONY command declares the "clean" rule as a phony one, i.e., it means
# that the clean rule will run instructions but it wont create a file called
# "clean" like a normal rule would (e.g., the util.o rule results in a file
# called util.o being created).
.PHONY: clean

clean:
	rm *.o
	rm a2
