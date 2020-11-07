import random
import threading

import rsa

from node import Node, pace, nodes
from Crypto.Signature import PKCS1_v1_5
from Crypto.Hash import SHA256


def mine():
    # lucky_node: it this term, this node mine a chizi
    luck_num = random.randint(0, len(nodes) - 1)
    print(f'\nlucky = {luck_num}, len nodes = {len(nodes)}\n')
    lucky_node = nodes[luck_num]

    print("\nCongratulations to node {} for being our next minner".format(lucky_node.id))

    # lucky node signs the block with its private key
    # block = rsa.sign(lucky_node.mem_pool, lucky_node.private_key, hash_method='SHA-256')
    hasher = SHA256.new(bytes(lucky_node.mem_pool))
    signer = PKCS1_v1_5.new(lucky_node.key)
    block = signer.sign(hasher)

    print("\nlucky node sign its mem pool")
    # now the lucky node start gossiping to other nodes
    lucky_node.gossip(block)



def period_calculator():
    # after this time, it is time to mine 
    return pace * len(nodes)



if __name__ == "__main__":
    #store the number of initial nodes in the system.
    nodesNo = int(input("Number of initial nodes: "))

    # initial nodes
    for i in range(nodesNo):
        node = Node(i)
        nodes.append(node)
        print(len(nodes))

    period = period_calculator()
    threading.Timer(period, mine).start()

    transaction_rate = 10
    # threading.Timer(transaction_rate, transact).start()
    
    for i in nodes:
        threading.Thread(target=i.update_costs).start()
