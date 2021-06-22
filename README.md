# 15-Puzzle
An assignment for an Artificial Intelligence class. Implements several searching algorithms to solve a 15-puzzle

To use, input a puzzle, the desired algorithm, and the optional third arguemnt if needed.
Example: 
  The board state as a single line in quotations, "12345678 9BCDEFA"
  The desired algorithm, AStar
  the huerstic type (hamming as h1, M-distance as h2)
  Input would be: "12345678 9BCDEFA" AStar h1
  Output would be
  
  12345678 9BCDEFA, AStar, h1
  Complete: [depth], [numCreated], [numExpanded], [maxFringe]
  12, 524, 252, 273
  
 Algorithm types: BFS, DFS, AStar, GBFS, DLS
 Optioanl 3rd arguemnt: int depth for DLS, h1 or h2 fpor AStar and GBFS
