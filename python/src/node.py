import random
import sys
import threading
import time
import base64

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

# Behzad: preEvents is a list which stores the string of a gossip object to check it is a new object or not
    preEvents = []

    def __init__ (self, id):
        self.coin = 1000
        # each node has an unique id
        self.id = id
        self.costs = [0 for i in range(len(nodes) + 1)]

        # generate a key pair for this node
        self.key = RSA.generate(1024)
        # self.public_key, self.private_key = rsa.newkeys(256)
    
        for num, node in enumerate(nodes):
            # print('num = {} and id = {}'.format(num, self.id))
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
                    self.costs[num] = sys.maxsize
                    
                    # append the edge value to the other vertex of the edge1
                    if len(node.costs) <= self.id:
                        node.costs.append(self.id)
                    else:
                        node.costs[self.id] = sys.maxsize


# Behzad : gossip has been changed for block and transaction
    def gossip(self, obj, mode):
        # mode 0 : transaction | 1: mine
        threads = []
        # if obj not in self.events:
        if mode == 1:
            # gossip a mined block
            for num, i in enumerate(nodes):
                if num != self.id:
                    print("\033[36;1m" + f"node {self.id} is gossiping a block {base64.b64encode(obj)} with node {num}" + "\033[0m")
                    th = threading.Thread(target=i.receive, args=(obj, mode, self.costs[num]))
                    threads.append(th)
                    th.start()
                    # i.receive(obj, mode, self.costs[num])
        elif mode == 0:
            # gossip a transaction
            for num, i in enumerate(nodes):
                if num != self.id:
                    print(f"node {self.id} is gossiping a transactoin with node {num}")
                    th = threading.Thread(target=i.receive, args=(obj, mode, self.costs[num]))
                    threads.append(th)
                    th.start()
                    # i.receive(obj, mode, self.costs[num])

        # join the threads to the current parent thread
        for t in threads:
            t.join()


# Behzad: receive has been changed for block and transaction
    def receive(self, obj, mode, cost_needed):
        # It is just for the simulating the latency of the network
        latency = int(cost_needed * pace)

        if obj not in self.preEvents:
            if type(obj) != str:
                self.preEvents.append(base64.b64encode(obj))
            else:
                self.preEvents.append(obj)

            # this if placed just to prevent the overflow problem
            if latency < 25:
                time.sleep(latency)
        
            if mode == 1:
                # append the received block to its blockchain
                self.blocks.append(obj)

                # remove all items in the memory pool after receiving a block
                self.mem_pool.clear()
            else:
                self.mem_pool.append(obj)
# Behzad : each node, which receives an object have to gossip it to other nodes
            time.sleep(2)
            # self.gossip(obj, mode)
            th = threading.Thread(target=self.gossip, args=(obj, mode))
            th.start()
            th.join()

    
    def update_costs(self):
        # Time to code: It is your turn to complete this method
        # This method is doing some stuff in order to update the costs
        pass
