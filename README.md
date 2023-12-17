#### Classes and their Responsibilities:

1. **Message**:
    - Contains fields for content and header (with recipient or topic information).
    - Methods to get and set these fields.

2. **QueueMessage extends Message**:
    - Contains a field for recipient.
    - Methods to get and set the recipient.

3. **TopicMessage extends Message**:
    - Contains fields for type, timestamp, and time-to-live (TTL).
    - Methods to access these fields.

4. **MessageQueue**:
    - Uses a `LinkedList<QueueMessage>` (FIFO structure) to store messages.
    - Methods to add, remove, and get messages based on recipient.
    - Limit to the number of messages it can hold.
    - Integrated with RabbitMQ for message handling.

5. **Topic**:
    - Uses a `List<TopicMessage>` to store messages.
    - Methods to add and get messages based on type.
    - No explicit limit, but messages have a TTL.
    - Integrated with RabbitMQ for message distribution.

6. **MessageServer**:
    - Contains instances of MessageQueue and Topic.
    - Responsible for routing messages to the correct queue or topic.
    - Handles administrative tasks, like setting expiration times.
    - Manages RabbitMQ connections for message handling.

7. **Client**:
    - Represents an application communicating with the server.
    - Can send messages to queues and subscribe to topics.
    - Interacts with MessageQueue and Topic.

8. **Viral**:
    - Service that receives all messages and tracks trending hashtags.
    - Maintains separate hashtag counts for broadcast and topic messages.
    - Integrated with RabbitMQ for message consumption.

#### Threads and Concurrency:

- **Producer Thread**: Creates messages and adds them to queues or topics.
- **Consumer Thread**: Retrieves messages from queues or reads from topics.
- **Cleanup Thread**: Removes expired messages from topics.

Concurrency Problems and Solutions:
1. **Race Conditions**: Synchronized access to message queues and topics using locks.
2. **Deadlocks**: Careful ordering of locks and avoiding nested locks.

#### Proposed Java Architecture:

- **Server**: Manages incoming connections, message resources, and administrative tasks.
- **Client**: Connects to the server to send/receive messages.
- **Message Queues and Topics**: Organize and distribute messages effectively.
- **ClientHandler**: Manages client connections and message transactions.

#### Solution Structure:

- **Viral Service**: Monitors all messages, counts, and displays trending hashtags.
- **Main App Channels**: Sends messages on two channels, one for broadcast and another for topic messages.
- **Event-based Communication**: Utilizes queues and message types for efficient message handling.

Entry Points:
- **Main Server**: Initializes and starts the message server.
- **Client Applications**: Connects to the server for messaging activities.
- **Viral Service**: Activates to monitor and analyze message trends.
