package minesweeper

import kotlin.random.Random


class MineFieldOperations {
    companion object {
        private lateinit var mineField: Array<CharArray>
        private lateinit var mineFieldToDraw: Array<CharArray>
        var gameState = "Starting"
        var minesUnmarked = 0

        fun userMarks(uInputNumOfMines: Int) {
            print("Set/unset mines marks or claim a cell as free: > ")
            val (userYInput, userXInput, userAction) = readLine()!!.split(' ')

            if (MineFieldOperations.gameState == "Starting" && userAction == "free") {
                MineFieldOperations.create(9, 9, uInputNumOfMines,userXInput.toInt(),userYInput.toInt())
                gameState = "Going"
            }

            when (userAction) {
                "free" -> {

                }
                "mine" -> {

                }
            }

            when (mineField[userXInput.toInt() - 1][userYInput.toInt() - 1]) {
                in '0'..'9' -> println("There is a number here!")
                'X' -> {
                    mineField[userXInput.toInt() - 1][userYInput.toInt() - 1] = 'M'
                    drawField()
                    minesUnmarked--
                }
                '.' -> {
                    mineField[userXInput.toInt() - 1][userYInput.toInt() - 1] = 'm'
                    drawField()
                    minesUnmarked++
                }
                'M' -> {
                    mineField[userXInput.toInt() - 1][userYInput.toInt() - 1] = 'X'
                    drawField()
                    minesUnmarked++
                }
                'm' -> {
                    mineField[userXInput.toInt() - 1][userYInput.toInt() - 1] = '.'
                    drawField()
                    minesUnmarked--
                }
            }

            if (minesUnmarked == 0) {
                gameState = "End"
                println("Congratulations! You found all the mines!")
            }
        }

        fun create(rowNumber: Int, columnNumber: Int, minesNumberinput: Int, firstCellX: Int,firstCellY: Int) {
            val minesNumber: Int
            var currentNumOfMines = 0
            var randomRow: Int
            var randomColumn: Int


            minesNumber = if (minesNumberinput >= rowNumber * columnNumber) {
                rowNumber * columnNumber - 1
            } else minesNumberinput

            minesUnmarked = minesNumber

            for (i in 0..rowNumber) {
                for (j in 0..columnNumber) {
                    mineField = Array(i) { CharArray(j) { '.' } }
                }
            }

            while (currentNumOfMines != minesNumber) {
                randomRow = Random.nextInt(0, rowNumber)
                randomColumn = Random.nextInt(0, columnNumber)
                if (mineField[randomRow][randomColumn] == '.'&& (randomColumn != firstCellX && randomRow != firstCellY)) {
                    mineField[randomRow][randomColumn] = 'X'
                    currentNumOfMines++
                }
            }

            fillWithHints()

        }

        fun createToDraw (rowNumber: Int, columnNumber: Int) {
            for (i in 0..rowNumber) {
                for (j in 0..columnNumber) {
                    mineFieldToDraw = Array(i) { CharArray(j) { '.' } }
                }
            }
        }

        fun drawField() {
            println(" │123456789│\n" +
                    "—│—————————│")
            for (i in mineFieldToDraw.indices) {
                print("${i + 1}|")
                for (j in mineFieldToDraw[i].indices) {
                    when (mineFieldToDraw[i][j]) {
                        'X' -> print('.')
                        'm','M' -> print('*')
                        else -> print(mineFieldToDraw[i][j])
                    }
                }
                print("|\n")


            }
            println("—│—————————│")
        }

        private fun isMine(rowNumber: Int, columnNumber: Int): Boolean {
            return mineField[rowNumber][columnNumber] == 'X'
        }

        private fun fillWithHints() {
            var currentHint: Int
            for (i in mineField.indices) {
                for (j in mineField[i].indices) {
                    currentHint = 0

                    if (mineField[i][j] != 'X') {
                        when (i){
                            0 -> {
                                when (j) {
                                    0 -> {
                                        if (isMine(i,j + 1)) currentHint++
                                        if (isMine(i + 1 , j)) currentHint++
                                        if (isMine(i + 1, j + 1)) currentHint++
                                        if (currentHint != 0) mineField[i][j] = (currentHint + 48).toChar()
                                    }

                                    mineField[i].lastIndex -> {
                                        if (isMine(i,j - 1)) currentHint++
                                        if (isMine(i + 1 , j)) currentHint++
                                        if (isMine(i + 1, j - 1)) currentHint++
                                        if (currentHint != 0) mineField[i][j] = (currentHint + 48).toChar()
                                    }
                                    else -> {
                                        if (isMine(i,j - 1)) currentHint++
                                        if (isMine(i,j + 1)) currentHint++
                                        if (isMine(i + 1 , j)) currentHint++
                                        if (isMine(i + 1, j - 1)) currentHint++
                                        if (isMine(i + 1,j + 1)) currentHint++
                                        if (currentHint != 0) mineField[i][j] = (currentHint + 48).toChar()
                                    }
                                }
                            }
                            mineField.lastIndex -> {
                                when (j) {
                                    0 -> {
                                        if (isMine(i,j + 1)) currentHint++
                                        if (isMine(i - 1 , j)) currentHint++
                                        if (isMine(i - 1, j + 1)) currentHint++
                                        if (currentHint != 0) mineField[i][j] = (currentHint + 48).toChar()
                                    }

                                    mineField[i].lastIndex -> {
                                        if (isMine(i,j - 1)) currentHint++
                                        if (isMine(i - 1 , j)) currentHint++
                                        if (isMine(i - 1, j - 1)) currentHint++
                                        if (currentHint != 0) mineField[i][j] = (currentHint + 48).toChar()
                                    }
                                    else -> {
                                        if (isMine(i,j - 1)) currentHint++
                                        if (isMine(i,j + 1)) currentHint++
                                        if (isMine(i - 1 , j)) currentHint++
                                        if (isMine(i - 1, j - 1)) currentHint++
                                        if (isMine(i - 1,j + 1)) currentHint++
                                        if (currentHint != 0) mineField[i][j] = (currentHint + 48).toChar()
                                    }
                                }
                            }

                            else -> {
                                when (j) {
                                    0 -> {
                                        if (isMine(i,j + 1)) currentHint++
                                        if (isMine(i + 1 , j)) currentHint++
                                        if (isMine(i + 1, j + 1)) currentHint++
                                        if (isMine(i - 1 , j)) currentHint++
                                        if (isMine(i - 1, j + 1)) currentHint++
                                        if (currentHint != 0) mineField[i][j] = (currentHint + 48).toChar()
                                    }

                                    mineField[i].lastIndex -> {
                                        if (isMine(i,j - 1)) currentHint++
                                        if (isMine(i + 1 , j)) currentHint++
                                        if (isMine(i - 1, j - 1)) currentHint++
                                        if (isMine(i - 1 , j)) currentHint++
                                        if (isMine(i + 1, j - 1)) currentHint++
                                        if (currentHint != 0) mineField[i][j] = (currentHint + 48).toChar()
                                    }
                                    else -> {
                                        if (isMine(i,j - 1)) currentHint++
                                        if (isMine(i,j + 1)) currentHint++
                                        if (isMine(i + 1 , j)) currentHint++
                                        if (isMine(i + 1, j - 1)) currentHint++
                                        if (isMine(i + 1,j + 1)) currentHint++
                                        if (isMine(i - 1 , j)) currentHint++
                                        if (isMine(i - 1, j - 1)) currentHint++
                                        if (isMine(i - 1,j + 1)) currentHint++
                                        if (currentHint != 0) mineField[i][j] = (currentHint + 48).toChar()
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
    }
}


fun main() {

    print("How many mines do you want on the field? ")
    val uInputNumOfMines = readLine()!!.toInt()
    MineFieldOperations.createToDraw(9,9)
    MineFieldOperations.drawField()




  //  MineFieldOperations.create(9,9, uInputNumOfMines)
    while (MineFieldOperations.gameState != "End"){
                MineFieldOperations.userMarks(uInputNumOfMines)

}

}


