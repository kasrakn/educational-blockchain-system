import base64
import random
import threading
import time

from Crypto.Hash import SHA256
from Crypto.Signature import PKCS1_v1_5

from node import Node, nodes, pace

sem = threading.Semaphore()

def mine():
    # lucky_node: it this term, this node mine a chizi
    luck_num = random.randint(0, len(nodes) - 1)
    print(f'lucky = {luck_num}, len nodes = {len(nodes)}')
    lucky_node = nodes[luck_num]

    while not sem.acquire(blocking=False):
        pass
    else:
        if len(lucky_node.mem_pool) != 0:
            print("\033[32;1m" + f"Congratulations to node {lucky_node.id} for being our next minner" + "\033[0m")

            # lucky node signs the block with its private key
            hasher = SHA256.new(str(lucky_node.mem_pool).encode('utf-8'))
            signer = PKCS1_v1_5.new(lucky_node.key)
            block = signer.sign(hasher)
            sem.release()

            # now the lucky node start gossiping to other nodes
            th = threading.Thread(target=lucky_node.gossip, args=(block, 1))
            th.start()
            th.join()


def transact():
    # two random sender and receiver from our nodes
    sender = nodes[random.randint(0, len(nodes) - 1)]
    receiver = nodes[random.randint(0, len(nodes) - 1)]
    print('\n' + f"sender = {sender.id}, receiver = {receiver.id}")

    if sender.id != receiver.id:
        amount = random.randint(1, sender.coin)

        # the transaction is stored as a string
        transaction = f"node {sender.id} sent {amount} ----> node{receiver.id}"

        # in order to prevent the synchronization problems when accessing the
        # the variables that can be changed by different threads simultaneously
        while not sem.acquire(blocking=False):
            pass
        else:
            sender.coin -= amount
            sender.mem_pool.append(transaction)

            receiver.coin += amount
            receiver.mem_pool.append(transaction)

            print("\033[34;1m" + f"a transaction has been occurred between node {sender.id} and {receiver.id}" + "\033[0m")

            sem.release()

            print("==" * 20)
            ts = threading.Thread(target=sender.gossip, args=(transaction, 0))
            tr = threading.Thread(target=receiver.gossip, args=(transaction, 0))

            # start the threads to gossip to other nodes
            ts.start()
            tr.start()
            # join threads to the parent thread
            ts.join()
            tr.join()
            # signal the semaphore
    else:
        print("\033[35;1m" + "Illegal transaction" + "\033[0m")




if __name__ == "__main__":
    #store the number of initial nodes in the system.
    nodesNo = int(input("Number of initial nodes: "))

    # initial nodes
    for i in range(nodesNo):
        node = Node(i)
        nodes.append(node)

    while True:
        # print("\033[31;1m" + "Next Round" + "\033[0m")
        threading.Timer(3, transact).start()
        threading.Timer(8, mine).start()

        time.sleep(12)
        new_node = Node(len(nodes))
        nodes.append(new_node)
        print("\033[31;1m" + "new node added" + "\033[0m")
        for i in nodes:
            i.update_costs()
