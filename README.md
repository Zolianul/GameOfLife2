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
   
3. **Simulation Class**:
   - Manages the simulation process.
   - Contains the main loop where cells interact with the environment.
   - Methods: `start()`, `stop()`.

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

2. **Reproduction Conflicts**:
   - Multiple cells may attempt to reproduce at the same time. This requires synchronization to ensure safe reproduction.

3. **Population Changes**:
   - Cells may be born or die dynamically. Managing the population list requires thread-safe data structures.

## Proposed Java Architecture

### Modules

1. **Core Module**:
   - Contains the `Cell`, `Environment`, and `Simulation` classes.

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

- **Simulation Class**:
  - Manages the simulation process.
  - Contains the main loop where cells interact with the environment.
  - Methods: `start()`, `stop()`.

### Interactions

- Cells interact with the environment to:
  - Consume food units.
  - Reproduce (if conditions are met).
  - Die (if starvation time is exceeded).

- Environment interacts with cells to:
  - Provide food units.
  - Monitor reproduction conditions.
  - Manage population changes.

### Entry Point

The `Main` class in the Main Module serves as the entry point. It initializes the environment, creates the initial population of cells, and starts the simulation.

## Simulation Flow

1. Initialize environment and create initial cell population.
2. Start the environment thread.
3. Start the simulation loop in the Simulation thread.
4. Cells interact with the environment.
5. Environment updates time and resource availability.
6. Monitor conditions for reproduction and starvation.
7. Repeat steps 4-6 until simulation is stopped.
