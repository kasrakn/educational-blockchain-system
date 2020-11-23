#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <vector>
#include <string>
#include <openssl/rsa.h>
#include <openssl/pem.h>
#include <time.h>
#include "keyGenerator.h"
#include "blockchainSystem.h"
#define INFINITY 9999

using namespace std;

srand(time(0))

class Node
{
private:
    /* data */
public:
    // memory pool
    int id;
    int coin = 1000;

    vector<string> memPool;   // memory pool (transactions store here)
    vector<string> blocks;     // temprory string -> bytes
    vector<string> preEvents;  // all events occured for this obj store in the vector
    vector<int> costs;         // edge value to other nodes in the system

    Node(/* args */);
    ~Node();
};

Node::Node(int id)
{
    this->id = id;
    // generate_key(this->id);
    char private_key_pem[12] = "private_key";
    char public_key_pem[11]  = "public_key";
    RSA *keypair = RSA_generate_key(KEY_LENGTH, PUBLIC_EXPONENT, NULL, NULL);

    // storing the private and public keys in two fields.
    private_key = create_RSA(keypair, PRIVATE_KEY_PEM, private_key_pem);
    public_key  = create_RSA(keypair, PUBLIC_KEY_PEM, public_key_pem);

    blockchainSystem::nodes.push_back(this);

    for (int i = 0; i < blockchainSystem::nodes.size(); i++){
        if(blockchainSystem::nodes[i].id == this->id)
            this.costs[i] = 0;

        else{
            int edgeValue = rand() % blockchainSystem::edgeValueRange + 1;
            if (edgeValue <= prob * blockchainSystem::edgeValueRange){
                this.costs[i] = edgeValue;
                blockchainSystem::nodes[i].costs[this->id] = edgeValue;
            }
            else{
                this.costs[i] = INFINITY;
                blockchainSystem::nodes[i].costs[this->id] = INFINITY;
            }
        }

        cout<<"\033[31;1m"<<"Node "<<this->id<<" has been initialized..."<<"\033[0m"<<endl;
    }

}

Node::~Node()
{
}
