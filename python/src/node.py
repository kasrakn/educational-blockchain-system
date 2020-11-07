import random
import sys
import threading
import time

from Crypto.PublicKey import RSA

edge_value_range = 25
# The probability of there is no edge between two nodes
prob = 0.2
# pace of gossiping between blocks per unit
pace = 0.5
nodes = []


class Node():
    # memory pool
    mem_pool = []
    blocks = []

    def __init__ (self, id):
        #e ach node has an unique id
        self.id = id
        self.costs = [0 for i in range(len(nodes) + 1)]

        # generate a key pair for this node
        self.key = RSA.generate(1024)
        # self.public_key, self.private_key = rsa.newkeys(256)
    
        for num, node in enumerate(nodes):
            print('num = {} and id = {}'.format(num, self.id))
            if node.id == self.id:
                self.costs[num] = 0
            else:
                edge_val = random.randint(1, edge_value_range + 1)

                if edge_val < edge_value_range * prob:
                    
                    # set the edge value of this vertex
                    self.costs[num] = edge_val
                    # append the edge value to the other vertex of the edge
                    if len(node.costs) <= self.id:
                        node.costs.append(self.id)
                    else:
                        node.costs[self.id] = edge_val
                else:
                    # print(self.costs)
                    self.costs[num] = sys.maxsize
                    
                    # append the edge value to the other vertex of the edge1
                    # print(node.costs)
                    if len(node.costs) <= self.id:
                        node.costs.append(self.id)
                    else:
                        node.costs[self.id] = sys.maxsize


    def gossip(self, block):
        for num, i in enumerate(nodes):
            print("\n\033[36;1m" + f"node {self.id} is gossiping with node {num}" + "\033[0m")
            thread = threading.Thread(target=i.receive, args=(block, self.costs[num])).start()
        


    def receive(self, block, cost_needed):
        # It is just for the simulating the latency of the network
        latency = int(cost_needed * pace)

        # this if placed just to prevent the overflow problem
        if latency < 25:
            time.sleep(latency)

        # append the received block to its blockchain
        self.blocks.append(block)

    
    def update_costs(self):
        # Time to code: It is your turn to complete this method
        # This method is doing some stuff in order to update the costs
        pass
