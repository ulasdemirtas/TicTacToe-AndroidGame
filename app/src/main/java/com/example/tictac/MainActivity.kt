package com.example.tictac

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tictac.ui.theme.Purple40

import com.example.tictac.ui.theme.TicTacTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TicTacTheme {
                val viewModel: StateView by viewModels()

                GameScreen(
                    viewModel = viewModel
                )

            }
        }
    }


    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    fun GameScreen(viewModel: StateView) {
        val state = viewModel.state
        val configuration = LocalConfiguration.current
        val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

        PlaySound(state.isWinner)

        if (isLandscape) {
            // Landscape Mode Design
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(25.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Player scores on the left side
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Top,

                ) {
                    Image(
                        painter = painterResource(id = R.drawable.tic_tac_toe_02),
                        contentDescription = null,
                        modifier = Modifier
                            .size(200.dp, 150.dp)
                                               )
                    Text(text = "You : ${state.player1Count}", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Draw : ${state.drawCount}", fontSize = 18.sp)
                    Text( text="Computer: ${state.player2Count}", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }

                // Game Board (with fixed size in the center)
                Box(
                    modifier = Modifier
                        .size(300.dp),

                    contentAlignment = Alignment.Center
                ) {

                    BoardBase()

                    LazyVerticalGrid(
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .aspectRatio(1f),
                        columns = GridCells.Fixed(3)
                    ) {
                        viewModel.boardCells.indices.forEach { index ->
                            val boardCellValue = viewModel.boardCells[index]
                            item {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(1f)
                                        .clickable {
                                            // Adjusting the index to start from 1
                                            viewModel.onAction(PlayerActions.BoardState(index + 1))
                                        },
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    AnimatedVisibility(
                                        visible = boardCellValue != BoardCellValue.EMPTY,
                                        enter = scaleIn(tween(800))
                                    ) {
                                        if (boardCellValue == BoardCellValue.CIRCLE) {
                                            Circle()
                                        } else if (boardCellValue == BoardCellValue.CROSS) {
                                            Cross()
                                        }
                                    }
                                }
                            }
                        }

                    }
                }

                // Turn on the right side and Restart Button
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .weight(1f),
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {


                    Text(text = state.turnText, fontSize = 20.sp, fontWeight = FontWeight.Bold,color = state.resultColor)

                    Button(
                        modifier = Modifier.padding(20.dp),
                        onClick = {
                            viewModel.onAction(
                                PlayerActions.RePlayButtonClicked
                            )
                        },
                        shape = RoundedCornerShape(15.dp),
                        elevation = ButtonDefaults.buttonElevation(10.dp),
                        colors = ButtonDefaults.buttonColors(contentColor = Color.White)

                    ) {
                        Text(text = "Restart", fontSize = 16.sp)
                    }
                }
            }

        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 25.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Row {
                    Image(
                        painter = painterResource(id = R.drawable.tic_tac_toe_02),
                        contentDescription = null,
                        modifier = Modifier
                            .size(400.dp, 200.dp)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "You: ${state.player1Count}", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Draw : ${state.drawCount}", fontSize = 18.sp)
                    Text( text="Computer : ${state.player2Count}", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    contentAlignment = Alignment.Center
                ) {
                    BoardBase()
                    LazyVerticalGrid(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .aspectRatio(1f),
                        columns = GridCells.Fixed(3)
                    ) {
                        viewModel.boardCells.forEachIndexed { index, boardCellValue ->
                            item {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(1f)
                                        .clickable {
                                            viewModel.onAction(
                                                PlayerActions.BoardState(index + 1)
                                            )
                                        },
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    AnimatedVisibility(
                                        visible = boardCellValue != BoardCellValue.EMPTY,
                                        enter = scaleIn(tween(800))
                                    ) {
                                        if (boardCellValue == BoardCellValue.CIRCLE) {
                                            Circle()
                                        } else if (boardCellValue == BoardCellValue.CROSS) {
                                            Cross()
                                        }
                                    }
                                }
                            }
                        }
                    }

                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = state.turnText,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = state.resultColor,

                    )
                    Button(
                        onClick = {
                            viewModel.onAction(
                                PlayerActions.RePlayButtonClicked
                            )
                        },
                        shape = RoundedCornerShape(15.dp),
                        elevation = ButtonDefaults.buttonElevation(6.dp),
                        colors = ButtonDefaults.buttonColors(contentColor = Color.White, containerColor = Purple40)
                    )
                    {
                        Text(
                            text = "Restart",
                            fontSize = 16.sp
                        )
                    }

                }
            }

        }
    }
}




