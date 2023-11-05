# GameOfLife



Project specification
The Game of Life

Simulate a population of living cells, each with the goal of feeding and reproducing.

There is a limited number of food units (resources) that cells consume. A food unit sustains a cell for a given time T_full, after which the cell gets hungry. If it doesn't eat within another specified time T_starve, the cell dies, resulting in a random number of food units between 1 and 5.

After eating at least 10 times, a cell will multiply before getting hungry again. There are two types of cells: sexual and asexual. Asexual cells multiply through division, resulting in two hungry cells.

Sexual cells only multiply if they encounter another cell looking to reproduce, resulting in a third cell that was initially hungry.

In the simulation, cells will be represented by distinct threads of execution.





## Overview

The Game of Life is a simulation that models the life cycle of cells in a population. Each cell has the goal of feeding and reproducing. There are two types of cells: asexual and sexual. Asexual cells multiply through division, while sexual cells multiply when they encounter another cell looking to reproduce.

## Key Components

1. **Cell Class**:
   - Attributes: Type (asexual or sexual), Hunger level, Reproduction counter, ID, and a reference to the environment.
   - Methods: `feed()`, `reproduce()`, `die()`, `multiply()`.
   
2. **Environment Class**:
   - Manages the population, food resources, and the passage of time.
   - Attributes: Food units, List of living cells.
   - Methods: `addCell(Cell cell)`, `removeCell(Cell cell)`, `updateTime()`.
   


## Threads

1. **Cell Threads**:
   - Each living cell is represented by a separate thread.
   - They independently perform actions like feeding, reproducing, and dying.
   
2. **Environment Thread**:
   - Responsible for managing resources and time.
   - Periodically updates the environment state and checks for starvation.

## Concurrency Problems

1. **Resource Conflicts**:
   - Cells may compete for the same food unit. This requires synchronization to prevent multiple cells from accessing the same resource simultaneously.

2. **Reproduction/Death Conflicts**:
   - Multiple cells may attempt to reproduce/die at the same time so the total count of cells should be updated accordingly.



## Proposed Java Architecture

### Modules

1. **Core Module**:
   - Contains the `Cell`, `Environment` classes.

2. **Main Module**:
   - Contains the `Main` class which serves as the entry point of the program.

### Classes

- **Cell Class**:
  - Attributes: Type, Hunger level, Reproduction counter, ID, Environment reference.
  - Methods: `feed()`, `reproduce()`, `die()`, `multiply()`.
  - Extends `Thread`.

- **Environment Class**:
  - Attributes: Food units, List of living cells.
  - Methods: `addCell(Cell cell)`, `removeCell(Cell cell)`, `updateTime()`.
  - Extends `Thread`.



### Interactions

- Cells interact with the environment to:
  - Consume food units.
  - Reproduce (if conditions are met).
  - Die (if starvation time is exceeded).

- Environment interacts with cells to:
  - Provide food units.
  - Monitor reproduction conditions.
  - Manage population changes.



# Messaging Server

### Classes and their Responsibilities:

1. **Message**:
    - Contains fields for content, header (with recipient or type information), and a timeout.
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

1. **Use `ReentrantLock` and `Condition`**:
    - Use a `ReentrantLock` to synchronize access to the MessageQueue and Topic.
    - Use `Condition` for handling cases where a consumer might need to wait for a message to be available.
  
2. **Java ExecutorService**: To manage threads efficiently, use Java's `ExecutorService`. It offers a higher-level replacement for the traditional way of managing threads and provides a clean and efficient way to handle concurrency.

3. **ScheduledExecutorService**: Use this for the Cleanup Thread to periodically check and remove expired messages.

4. **Java NIO for Networking**: If you're considering making this server network-enabled, Java NIO (or Netty, a popular NIO-based framework) can be used for scalable and efficient networking.

5. **Observer Pattern for Topics**: Since topics are essentially a publish-subscribe model, use the Observer pattern. When a new message of a particular type is published, all subscribed clients are notified.

6. **Configuration**: To allow for administration and customization, have a configuration system. This could be a properties file or a more advanced configuration management system.

### Additional Suggestions:

1. **Persistence**: Consider adding a persistence layer (like a database) to store messages, especially if you need durability and reliability. 

2. **Logging**: Integrate a robust logging system like SLF4J with Logback to track server operations, errors, and client activities.

3. **Metrics & Monitoring**: Integrate metrics collection libraries like Micrometer or Dropwizard Metrics to monitor the server's health, performance, and other vital statistics.

This is a high-level overview and would need to be refined and detailed further based on exact requirements, scalability needs, fault tolerance, etc.
