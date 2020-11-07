import random
import threading

from Crypto.Hash import SHA256
from Crypto.Signature import PKCS1_v1_5

from node import Node, nodes, pace


def mine():
    # lucky_node: it this term, this node mine a chizi
    luck_num = random.randint(0, len(nodes) - 1)
    print(f'\nlucky = {luck_num}, len nodes = {len(nodes)}\n')
    lucky_node = nodes[luck_num]

    print("\n\033[32;1m" + f"Congratulations to node {lucky_node.id} for being our next minner" + "\033[0m")

    # lucky node signs the block with its private key
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
