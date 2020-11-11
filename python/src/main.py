import random
import threading
import time
import base64

from Crypto.Hash import SHA256
from Crypto.Signature import PKCS1_v1_5

from node import Node, nodes, pace


def mine():
    # lucky_node: it this term, this node mine a chizi
    luck_num = random.randint(0, len(nodes) - 1)
    print(f'lucky = {luck_num}, len nodes = {len(nodes)}')
    lucky_node = nodes[luck_num]

    print("\033[32;1m" + f"Congratulations to node {lucky_node.id} for being our next minner" + "\033[0m")

    lock = threading.Lock()
    lock.acquire(blocking=True)
    # lucky node signs the block with its private key
    hasher = SHA256.new(str(lucky_node.mem_pool).encode('utf-8'))
    signer = PKCS1_v1_5.new(lucky_node.key)
    block = signer.sign(hasher)

    print("lucky node sign its mem pool")
    # now the lucky node start gossiping to other nodes
    # Behzad : do it with a thread
    lucky_node.preEvents.append(base64.b64encode(block))
    th = threading.Thread(target=lucky_node.gossip, args=(block, 1))
    th.start()
    lock.release()
    th.join()


def period_calculator():
    # after this time, it is time to mine 
    return pace * len(nodes) * 4


def transact():
    # two random sender and receiver from our nodes
    sender = nodes[random.randint(0, len(nodes) - 1)]
    receiver = nodes[random.randint(0, len(nodes) - 1)]

    if sender.id != receiver.id:
        amount = random.randint(1, sender.coin)

        # the transaction is stored as a string
        transaction = f"node{sender.id} sent {amount} ----> node{receiver.id}"

        # in order to prevent the synchronization problems when accessing the 
        # the variables that can be changed by different threads simultaneously
        lock = threading.Lock()
        lock.acquire(blocking=True)
        sender.coin -= amount
        sender.mem_pool.append(transaction)

        receiver.coin += amount
        receiver.mem_pool.append(transaction)
        lock.release()

        print("\033[32;1m" + f"a transaction has been occured between node {sender.id} and {receiver.id}" + "\033[0m")

        sender.preEvents.append(transaction)
        receiver.preEvents.append(transaction)

        ts = threading.Thread(target=sender.gossip, args=(transaction, 0))
        tr = threading.Thread(target=receiver.gossip, args=(transaction, 0))

        # start the threads to gossip to other nodes
        ts.start()
        tr.start()

        # join threads to the parent thread
        ts.join()
        tr.join()
    else:
        print("\033[35;1m" + "Iligal transaction" + "\033[0m")




if __name__ == "__main__":
    #store the number of initial nodes in the system.
    nodesNo = int(input("Number of initial nodes: "))

    # initial nodes
    for i in range(nodesNo):
        node = Node(i)
        nodes.append(node)

    transaction_rate = 10
    threading.Timer(transaction_rate, transact).start()

    period = period_calculator()
    threading.Timer(period, mine).start()

        
    while True:
        print("\033[31;1m" + "Next Round" + "\033[0m")
        time.sleep(5)

        for i in nodes:
            i.update_costs()
            # threading.Thread(target=i.update_costs).start()
