import random
import threading

import rsa

from node import Node, pace, nodes


def mine():
    # lucky_node: it this term, this node mine a chizi
    lucky_node = nodes[random.randint(1, len(nodes))]
    print(f"Congratulations to node {lucky_node.id} for being our next minner")

    # lucky node signs the block with its private key
    block = rsa.sign(lucky_node.mem_pool, lucky_node.private_key)
    print("lucky node sign its mem pool")
    # now the lucky node start gossiping to other nodes
    lucky_node.gossip(block)



def period_calculator():
    # after this time, it is time to mine 
    return pace * nodes



if __name__ == "__main__":
    #store the number of initial nodes in the system.
    nodesNo = int(input("Number of initial nodes: "))

    # initial nodes
    for i in range(nodesNo):
        node = Node(i)
        nodes.append(node)

    period = period_calculator()
    threading.Timer(period, mine).start()

    transaction_rate = 10
    threading.Timer(transaction_rate, transact).start()
    
    for i in nodes:
        threading.Thread(target=i.update_costs).start()
