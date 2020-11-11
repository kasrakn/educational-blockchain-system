import random
import sys
import threading
import time

import rsa


edge_value_range = 25
# The probability of there is no edge between two nodes
prob = 0.2
# pace of gossiping between blocks per unit
pace = 0.5
nodes = []


class Node():
    # memory pool
    mem_pool = []
    costs = []
    blocks = []
    coin = 1000


    def __init__ (self, id):
        #e ach node has an unique id
        self.id = id
        self.public_key, self.private_key = rsa.newkeys(256)

        for i in range(len(nodes)):
            if i == id:
                # each node has edge_val equal to 0 to itself
                edge_val = 0
            else:   
                edge_val = random.randint(1, edge_value_range + 1)

                if edge_val >= edge_value_range * prob:
                    
                    # set the edge value of this vertex
                    self.costs[i] = edge_val
                    # append the edge value to the other vertex of the edge
                    nodes[i].costs[self.id] = edge_val
                else:
                    self.costs[i] = sys.maxsize
                    
                    # append the edge value to the other vertex of the edge
                    nodes[i].costs[self.id] = sys.maxsize


    def gossip(self, block):
        for i in nodes:
            thread = threading.Thread(target=i.receive, args=(block, self.costs[i])).start()
        


    def receive(self, block, cost_needed):
        #TODO: cascading - get node id
        # It is just for the simulating the latency of the network
        time.sleep(cost_needed * pace)

        # append the received block to its blockchain
        self.blocks.append(block)

    
    def update_costs(self):
        # Time to code: It is your turn to complete this method
        # This method is doing some stuff in order to update the costs
        pass
