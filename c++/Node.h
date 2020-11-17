#include <vector>
#include <string>
#include <openssl/rsa.h>
#include <openssl/pem.h>

#include <keyGenerator.h>

using namespace std;

vector<Node> nodes;

class Node
{
private:
    /* data */
public:
    // memory pool
    int id;
    int coin = 1000;

    vector<string> mem_pool;   // memory pool (transactions store here)
    vector<string> blocks;     // temprory string -> bytes
    vector<string> preEvents;  // all events occured for this obj store in the vecotor
    vector<int> cotst;         // edge value to other nodes in the system
    Node(/* args */);
    ~Node();
};

Node::Node(int id)
{
    this->id = id;
    generate_key(this->id);

}

Node::~Node()
{
}
