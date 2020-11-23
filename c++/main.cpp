#include <iostream>
#include <math.h>
#include <time.h>
#include <thread>
#include <random>
#include "Node.h"

using namespace std;

int main(){
    cout << "Number of initial nodes: ";
    int nodesNo;
    cin >> nodesNo;

    // initial nodes
    for (int i = 0; i < nodesNo; i++){
        Node node = new Node(i);
    }


    return 0;
}

