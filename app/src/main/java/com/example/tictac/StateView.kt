package com.example.tictac

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class StateView : ViewModel() {

    var state by mutableStateOf(GameState())
    private val _boardCells = mutableStateListOf(*List(9) { BoardCellValue.EMPTY }.toTypedArray())
    val boardCells: List<BoardCellValue> get() = _boardCells
    private var event by mutableStateOf<GameEvent?>(null)


    fun onAction(action: PlayerActions) {
        when (action) {
            is PlayerActions.BoardState -> {
                addValueToCell(action.cellNo)
            }
            PlayerActions.RePlayButtonClicked -> {
                gameReset()
            }
        }
    }

    private fun gameReset() {
        for (i in _boardCells.indices) {
            _boardCells[i] = BoardCellValue.EMPTY
        }
        state = state.copy(
            turnText = "Your Turn",
            currentTurn = BoardCellValue.CROSS,
            isWinner = false
        )
    }

    private fun addValueToCell(cellNo: Int) {
        // If cell is not empty or the game has ended, simply return
        if (_boardCells[cellNo - 1] != BoardCellValue.EMPTY || state.isWinner || state.turnText == "Draw Game") {
            return
        }

        // If it's the computer's turn, ignore player input
        if (state.currentTurn == BoardCellValue.CIRCLE) {
            return
        }

        _boardCells[cellNo - 1] = BoardCellValue.CROSS
        updateGameState(BoardCellValue.CROSS)

        // Only let the computer play if the game hasn't ended after player's move
        if (!state.isWinner && state.turnText != "Draw Game") {
            computerPlay()
        }

    }


    private fun computerPlay() {
        viewModelScope.launch {
            delay(500)

            // With a 40% chance, make a random move to make a mistake
            if (kotlin.random.Random.nextDouble() < 0.4) {
                val availableIndices = _boardCells.mapIndexedNotNull { index, value ->
                    if (value == BoardCellValue.EMPTY) index else null
                }
                if (availableIndices.isNotEmpty()) {
                    val randomCell = availableIndices.random()
                    _boardCells[randomCell] = BoardCellValue.CIRCLE
                    updateGameState(BoardCellValue.CIRCLE)
                    return@launch
                }
            }

            // Otherwise, use the minimax algorithm to determine the best move.
            val bestMove = findBestMove(_boardCells)
            if (bestMove != -1) {  // Ensure that there's an available move
                _boardCells[bestMove] = BoardCellValue.CIRCLE
                updateGameState(BoardCellValue.CIRCLE)
            }
        }
    }



    private fun updateGameState(cellValue: BoardCellValue) {
        if (checkWinner(_boardCells, cellValue)) {

            state = state.copy(
                turnText = "${if (cellValue == BoardCellValue.CROSS) "You" else "Computer"} WON!",
                resultColor = if (cellValue == BoardCellValue.CROSS) Color.Blue else Color.Magenta,
                player1Count = if (cellValue == BoardCellValue.CROSS) state.player1Count + 1 else state.player1Count,
                player2Count = if (cellValue == BoardCellValue.CIRCLE) state.player2Count + 1 else state.player2Count,
                currentTurn = BoardCellValue.EMPTY,
                isWinner = true
            )

            // Set the event to PLAYER_WON
            event = GameEvent.PLAYER_WON

            viewModelScope.launch {
                delay(3000)
                state = state.copy(isWinner = false)

                // Reset the event after processing it
                event = null
            }
        } else if (isBoardFull()) {
            state = state.copy(
                turnText = "Draw Game",
                drawCount = state.drawCount + 1
            )
        } else {
            state = state.copy(
                turnText = if (cellValue == BoardCellValue.CROSS) "Computer's Turn" else "Your Turn",
                resultColor = if (cellValue == BoardCellValue.CROSS) Color.Magenta else Color.Blue,
                currentTurn = if (cellValue == BoardCellValue.CROSS) BoardCellValue.CIRCLE else BoardCellValue.CROSS
            )
        }
    }



    private fun checkWinner(board: List<BoardCellValue>, cellValue: BoardCellValue): Boolean {
        val winPositions = listOf(
            listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8),
            listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8),
            listOf(0, 4, 8), listOf(2, 4, 6)
        )
        return winPositions.any { it.all { pos -> board[pos] == cellValue } }
    }

    private fun isBoardFull(): Boolean = !boardCells.contains(BoardCellValue.EMPTY)
}

data class GameState(
    val player1Count: Int = 0,
    val player2Count: Int = 0,
    val drawCount: Int = 0,
    val turnText: String = "Your Turn",
    val currentTurn: BoardCellValue = BoardCellValue.CROSS,
    val isWinner: Boolean = false,
    val resultColor: Color = Color.Blue
)

enum class BoardCellValue {
    CROSS, CIRCLE, EMPTY
}

sealed class PlayerActions {
    object RePlayButtonClicked : PlayerActions()
    data class BoardState(val cellNo: Int) : PlayerActions()
}

enum class GameEvent {
    PLAYER_WON
}

private fun findBestMove(board: List<BoardCellValue>): Int {
    var bestVal = Int.MIN_VALUE
    var bestMove = -1

    for (i in board.indices) {
        if (board[i] == BoardCellValue.EMPTY) {
            val tempBoard = board.toMutableList()
            tempBoard[i] = BoardCellValue.CIRCLE
            val moveVal = minimax(tempBoard, 0, false)
            tempBoard[i] = BoardCellValue.EMPTY

            if (moveVal > bestVal) {
                bestMove = i
                bestVal = moveVal
            }
        }
    }
    return bestMove
}

private fun evaluateBoard(board: MutableList<BoardCellValue>): Int {
    val winPositions = listOf(
        listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8),
        listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8),
        listOf(0, 4, 8), listOf(2, 4, 6)
    )

    for (positions in winPositions) {
        if (board[positions[0]] == board[positions[1]] && board[positions[1]] == board[positions[2]]) {
            return when (board[positions[0]]) {
                BoardCellValue.CIRCLE -> 10
                BoardCellValue.CROSS -> -10
                else -> 0
            }
        }
    }
    return 0
}

private fun minimax(board: MutableList<BoardCellValue>, depth: Int, isMax: Boolean): Int {
    val score = evaluateBoard(board)

    if (score == 10) return score - depth
    if (score == -10) return score + depth
    if (!board.contains(BoardCellValue.EMPTY)) return 0

    if (isMax) {
        var best = Int.MIN_VALUE
        for (i in board.indices) {
            if (board[i] == BoardCellValue.EMPTY) {
                board[i] = BoardCellValue.CIRCLE
                best = kotlin.math.max(best, minimax(board, depth + 1, false)) // Switch to minimizing
                board[i] = BoardCellValue.EMPTY
            }
        }
        return best
    } else {
        var best = Int.MAX_VALUE
        for (i in board.indices) {
            if (board[i] == BoardCellValue.EMPTY) {
                board[i] = BoardCellValue.CROSS
                best = kotlin.math.min(best, minimax(board, depth + 1, true)) // Switch to maximizing
                board[i] = BoardCellValue.EMPTY
            }
        }
        return best
    }
}

