# Messaging Server

### Classes and their Responsibilities:

1. **Message**:
    - Contains fields for content, header (with recipient or topic information).
    - Contains methods to get and set these fields.
  
2. **QueueMessage extends Message**:
    - Contains a field for recipient.
  
3. **TopicMessage extends Message**:
    - Contains a field for type.
  
4. **MessageQueue**:
    - Uses a `LinkedList<QueueMessage>` (FIFO structure) to store messages.
    - Methods to add, remove, and get messages based on recipient.
    - Limit to the number of messages it can hold.

5. **Topic**:
    - Uses a `List<TopicMessage>` to store messages.
    - Methods to add and get messages based on type.
    - No explicit limit, but messages have a timeout.

6. **MessageServer**:
    - Contains instances of MessageQueue and Topic.
    - Responsible for routing messages to the correct queue or topic.
    - Handles administration tasks, like setting expiration times.
  
7. **Client**:
    - Represents an application communicating with the server.
    - Can send and receive messages.
    - For topic messages, a client subscribes to a certain type.

### Threads and Concurrency:

- **Producer Thread**: These threads are created when a client wants to send a message. They will add messages to the relevant queue or topic.

- **Consumer Thread**: These threads are created when a client wants to receive a message. They will remove messages from the queue or read from topics.

- **Cleanup Thread**: This is a background thread that periodically checks for expired messages in the topics and removes them.

Concurrency Problems:

1. **Race Conditions**: Multiple clients might try to send or read messages concurrently. Without proper synchronization, this can lead to issues like reading the same message multiple times or missing messages.

2. **Deadlocks**: Improper ordering of locks can lead to a situation where one thread holds one lock and waits for another, which is held by another thread.

### Proposed Java Architecture:

1.**Server**:
The server component is the core of the architecture, responsible for accepting incoming connections, managing message resources, and performing administrative tasks.
It uses the ServerSocket to listen for incoming client connections.
Multithreading is employed to handle multiple client connections simultaneously.

2.**Client**:
Clients represent individual applications or users that connect to the server to send and receive messages.
The code for clients can be embedded in various applications to facilitate communication.

3.**Message Queues and Topics**:
Message resources, including message queues and topics, are maintained by the server to organize and distribute messages effectively.
Message Queues ensure that messages are delivered to their intended recipients following a First-In-First-Out (FIFO) order.
Topics provide a publish-subscribe model for message distribution, categorizing messages by their types.

4.**ClientHandler**:
The ClientHandler class is responsible for managing individual client connections.
It handles communication with clients through Socket connections and manages message sending and reception using PrintWriter and BufferedReader.
The class also maintains data structures for message history and processes !history commands.

### Additional Suggestions:

1. **Persistence**: Consider adding a persistence layer (like a database) to store messages. 

2. **Logging**: Integrate a logging system.


