#include <iostream>
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <functional>
#include <mutex>
#include <vector>
#include "Node.h"
#include <openssl/rsa.h>
#include <openssl/pem.h>
#include <openssl/sha.h>
#include <string>

using namespace std;
srand(time(0));

class System
{
private:
    /* data */
public:
    const int edgeValueRange = 25;      // maximum value that can be assigned to an edge.
    const float prob = 0.4;             // the probability that there is a direct edge between two nodes.
    const int transactionRate = 3;          // the time to wait for next transaction.
    const int mineRate = 8;                 // the time to wait for next mine (if the selected node has can do the mine)
    vector<Node> nodes;                 // all nodes in the system are stored at this vector.
    mutex mineMutex;
    mutex transactinonMutex;

    Parameters(/* args */);
    ~Parameters();
};

Parameters::Parameters(/* args */)
{
}

Parameters::~Parameters()
{
}

void System::mine(){
    int luckNum = rand() % this.nodes.size()  + 1;
    Node luckyNode = this->nodes[luck_num];

    // lock the mutex to prevent other nodes to access it.
    m.lock();
    sh
    if (luckyNode.memPool.size() != 0){
        cout << "Congratulations to node "
             << luckNum
             << "for being our next miner"
             << endl;

        // lucky node signs the block with its private key
        hash<vector<string>> hasher;
        string hashedMemPool = hasher(this->memPool);
        RSA *r = NULL;
        r = RSA_new()


    }

}