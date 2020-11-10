import random
import threading

import rsa

from node import Node, pace, nodes

#TODO: gossinpng -> cascade --> ignore redundant gossiping

def mine():
    # lucky_node: it this term, this node mine a chizi
    lucky_node = nodes[random.randint(1, len(nodes))]
    print(f"\nCongratulations to node {lucky_node.id} for being our next minner")

    # lucky node signs the block with its private key
    block = rsa.sign(lucky_node.mem_pool, lucky_node.private_key)
    print("\nlucky node sign its mem pool")
    # now the lucky node start gossiping to other nodes
    lucky_node.gossip(block)



def period_calculator():
    # after this time, it is time to mine 
    return pace * len(nodes)



def transact():
    sender = random.randint(1, len(nodes))
    receiver = random.randint(1, len(nodes))

    if sender == receiver:
        print("\noops: iligal transaction")
    else:
        amount = random.randint(1, nodes[sender].coin)
        nodes[sender].coin = nodes[sender].coin - amount
        nodes[receiver].coin = nodes[receiver].coin + amount

    #TODO : add to mem pool - hash the transaction 



if __name__ == "__main__":
    #store the number of initial nodes in the system.
    nodesNo = int(input("Number of initial nodes: "))

    # initial nodes
    for i in range(nodesNo):
        node = Node(i)
        nodes.append(node)

    period = period_calculator()
    threading.Timer(period, mine).start()

    # 10 seconds to next transaction
    transaction_rate = 10
    threading.Timer(transaction_rate, transact).start()
    
    cost_update_rate = 15
    threading.Timer(cost_update_rate, cost_update).start()
    #TODO : add new nodes in cost_update
