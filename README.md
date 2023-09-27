
# TicTacToe-Android
Tic Tac Toe with Minimax Algorithm
This Tic Tac Toe game is a modern adaptation of the classic two-player game, optimized for Android using Jetpack Compose. What makes this version unique is the integration of the Minimax algorithm, which traditionally allows the computer to play perfectly. However, to enhance gameplay and keep the game enjoyable, this version introduces a feature that causes the computer to make mistakes 40% of the time.

Features:
Modern UI: Built with Jetpack Compose.
Smart AI Opponent: Uses the Minimax algorithm for decision making.
Enhanced Playability: Computer makes occasional mistakes to keep the game challenging but winnable.
Minimax Algorithm:
The Minimax algorithm is a decision-making algorithm, especially useful for games like Tic Tac Toe, Chess, etc. It calculates all possible moves, both for the AI player and the human player, to determine the best possible move for the current player.

For Tic Tac Toe:

It returns +10 for a winning move by the computer.
It returns -10 for a winning move by the human.
It returns 0 for a draw.
The algorithm then chooses the move with the maximum score when it's the computer's turn and the move with the minimum score when it's the human's turn.

40% Mistake Feature:
To prevent the computer from always playing a perfect game and to increase the fun, the game has been programmed such that there's a 40% chance that the computer will not choose the optimal move according to Minimax. This means the computer might occasionally make a move that does not directly align with the Minimax evaluation, making the game more unpredictable and entertaining.

How to Play:
Start the game.
Players take turns, with the user as 'X' and the computer as 'O'.
The goal is to get three of your symbols in a row, either horizontally, vertically, or diagonally.
If the board fills up without any winning combinations, the game is a draw.
Installation:
Clone the repository.
Open the project in your preferred Android IDE.
Build and run on an emulator or a real device.
Contributions:
Contributions are welcome! Please make sure to test your changes thoroughly before submitting a pull request.
