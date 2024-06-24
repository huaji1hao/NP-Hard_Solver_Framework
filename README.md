# UAV Animal Feeding Problem🦒

[中文版 zh-CN](README.zh-CN.md)

## Overview🦈

This project implements a suite of optimization algorithms focused on solving the UAV Animal Feeding Problem(**modified TSP**) using various heuristic and hyper-heuristic methods. 

The goal is to efficiently find optimal or near-optimal routes for UAVs under different operational constraints.

## Features

- **Heuristic Algorithms**: Implements several heuristic strategies such as Edge Recombination, Next Descent, and Order Crossover to efficiently explore and exploit solution spaces.
- **Hyper-Heuristics**: Includes a Modified Choice Function (MCF) hyper-heuristic that dynamically selects from a pool of heuristics based on their past performance, synergistic potential and time since last use.
- **Crossover Operations**: Utilizes genetic algorithm techniques for solution recombination, enhancing diversity and solution quality.
- **Optimization Framework**: Leverages an abstract problem domain to generalize the solution approach, making it applicable to a variety of routing and scheduling problems.

## Components

### Heuristics

- **AdjacentSwap**: Performs simple adjacent swaps within the solution to explore local neighborhoods and enhance the solution iteratively.
- **DavissHillClimbing**: Applies hill climbing optimization to improve the solution by making incremental changes that result in an improved objective function value.
- **EdgeRecombination**: A heuristic that selects and swaps adjacent pairs of nodes in the solution representation to explore the neighborhood structure for better solutions.
- **HeuristicOperators**: Provides basic operations such as swaps, reinsertions, and mutations that are commonly used across different heuristics.
- **KDavisHillClimbing**: An extended version of Daviss Hill Climbing that incorporates additional complexity or constraints into the hill climbing process.
- **NextDescent**: Iteratively applies small changes to the current solution, accepting any change that improves the solution.
- **OrderCrossover (OX)**: Generates offspring by combining segments from two parent solutions while preserving the relative order of nodes.
- **PMX (Partially-Mapped Crossover)**: A crossover method that combines material from two parent solutions ensuring that each element and its position from a parent are preserved as much as possible in the offspring.
- **RandomSwap**: Randomly selects two elements within the solution and swaps their positions, offering a way to escape local optima by random perturbation.
- **Reinsertion**: Removes one or more elements from the solution and reinserts them at different positions, potentially improving the objective function.
- **SegmentInversion**: Reverses the order of a segment within the solution to test whether a different sequence offers a better outcome.

### Hyper-Heuristics

- **MCF_HH (Modified Choice Function Hyper-Heuristic)**: Utilizes a combination of multiple heuristics, managing a balance between exploration and exploitation through a scoring system that adapts to the success frequency,  synergistic potential and time since last use of individual heuristics. This hyper-heuristic dynamically adjusts its strategy based on ongoing results to optimize the search process.
- **SR_IE_HH (Simple Random Iterative Enhancement Hyper-Heuristic)**: This hyper-heuristic employs a straightforward random selection approach to apply heuristics iteratively, seeking to enhance the solution by continuously testing different heuristic applications without prior bias or complex rules. It represents a baseline strategy to compare more sophisticated hyper-heuristic performances.
- **TB_IE_HH (Tabu Search Iterative Enhancement Hyper-Heuristic)**: Implements a Tabu Search-based hyper-heuristic that uses a list to forbid or penalize recently used heuristics to avoid cycling back to previous solutions. This approach is designed to help escape local optima by promoting the exploration of new areas in the solution space.

### Utilities

- **HeuristicOperators**: Provides basic operations such as setting objective function, comparation, shuffle permutation, delta evaluation calculation, swaps and getting mutation times that are commonly used across different heuristics.
- **UAVInstanceReader**: read instance from `instances/uzf/...`

## Installation

To set up the project environment:

1. Ensure you have **OPENJDK 21** or later installed.

2. Clone the repository:

   ```
   git clone https://github.com/huaji1hao/UAV-Animal-Feeding-Problem.git
   ```

3. Import these two packages

   - `chesc-fixed-no-ps.jar`
   - `chesc-ps.jar`

## Configuration

Modify the parameters in the `UZFDomain.java` file to adjust the behavior of dos and iom:

```
this.setDepthOfSearch(0.6);
this.setIntensityOfMutation(0.3);
```

Details see method `getOperationTimes()`  in`HeuristicOperators.java`

## Usage

Use `SR_IE_VisualRunner.java` to run different hyper-heuristic algorithms.

## Contributing

Contributions to enhance or extend the project are welcome. Please adhere to the following steps:

1. Fork the repository.
2. Create a new branch (git checkout -b feature-branch).
3. Make your changes and commit them (git commit -am 'Add some feature').
4. Push to the branch (git push origin feature-branch).
5. Open a new Pull Request.

## Showcase

<img src="https://eumcm.com/file/6fc729ea83e46e89dd523.png" width="560" height="720" />