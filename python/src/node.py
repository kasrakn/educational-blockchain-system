import base64
import random
import sys
import threading
import time

from Crypto.PublicKey import RSA

from students_part import cost_minimizer

edge_value_range = 25
# The probability of there is no edge between two nodes
prob = 0.4
# pace of gossiping between blocks per unit
pace = 0.5
nodes = []


class Node():
    # memory pool
    mem_pool = []
    blocks = []
    preEvents = []
    sem = threading.Semaphore()

    def __init__ (self, id):
        self.coin = 1000
        # each node has an unique id
        self.id = id
        self.costs = [0 for i in range(len(nodes) + 1)]
        self.preEvents = []

        # generate a key pair for this node
        self.key = RSA.generate(1024)

        for num, node in enumerate(nodes):
            if node.id == self.id:
                self.costs[num] = 0
            else:
                edge_val = random.randint(1, edge_value_range + 1)

                if edge_val < edge_value_range * prob:

                    # set the edge value of this vertex
                    self.costs[num] = edge_val
                    # append the edge value to the other vertex of the edge
                    if len(node.costs) <= self.id:
                        node.costs.append(edge_val)
                    else:
                        node.costs[self.id] = edge_val
                else:
                    self.costs[num] = sys.maxsize

                    # append the edge value to the other vertex of the edge1
                    if len(node.costs) <= self.id:
                        node.costs.append(sys.maxsize)
                    else:
                        node.costs[self.id] = sys.maxsize


    def gossip(self, obj, mode):
        # mode 0 : transaction | 1: mine

        strObj = obj
        if mode == 1:
            strObj = base64.b64encode(obj)

        threads = []
        # if obj not in self.events:
        while self.sem.acquire(blocking=False):
            pass
        else:
            if strObj not in self.preEvents:
                for num, i in enumerate(nodes):
                    if num != self.id:
                        latency = int(self.costs[num] * pace)
                        if latency < 25:
                            self.preEvents.append(strObj)
                            th = threading.Thread(target=i.receive, args=(obj, mode, latency))
                            threads.append(th)
                            th.start()
                            self.sem.release()
                            if mode == 1:
                                print("\033[36;1m" + f"node {self.id} is gossiping a block with node {num}" + "\033[0m")
                            else:
                                print(f"node {self.id} is gossiping a transaction (( {strObj} )) with node {num}")

        # join the threads to the current parent thread
        for t in threads:
            t.join()


    def receive(self, obj, mode, latency):
        # It is just for the simulating the latency of the network

        while not self.sem.acquire(blocking=False):
            pass
        else:
            # this if placed just to prevent the overflow problem
            time.sleep(latency)

                if mode == 1:
                    # append the received block to its blockchain
                    self.blocks.append(obj)
                    # remove all items in the memory pool after receiving a block
                    self.mem_pool.clear()
                else:
                    self.mem_pool.append(obj)
                time.sleep(0.5)
                th = threading.Thread(target=self.gossip, args=(obj, mode))
                th.start()
                self.sem.release()
                th.join()


    def update_costs(self):
        cost_minimizer(self.id, nodes)