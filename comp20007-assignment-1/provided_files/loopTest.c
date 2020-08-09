#include <stdio.h>

int main(int argc, char* argv[]){
    int i,j, k = 0;
    int returnFalse = 0;
    for (i = 0; i < 5; i ++){
        // if (returnFalse == 1){
        //     break;
        // }
        k ++;
        for (j = 0; j < 5; j ++){
            if (returnFalse == 1){
                break;
            }
            if ( i == 2 && j == 3){
                returnFalse = 1;
                break;
                break;
            }
        }
    }
    printf("%d", k);
}