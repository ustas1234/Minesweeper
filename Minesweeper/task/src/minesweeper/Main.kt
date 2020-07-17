package minesweeper

import kotlin.random.Random


class MineFieldOperations {
    companion object {
        private lateinit var mineField: Array<CharArray>
        var gameState = "Going"
        var minesUnmarked = 0

        fun userMarks () {
            print("Set/delete mines marks (x and y coordinates): ")
            val (userYInput, userXInput) = readLine()!!.split(' ')

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

        fun create(rowNumber: Int, columnNumber: Int, minesNumberinput: Int) {
            val minesNumber: Int
            var currentNumOfMines = 0
            var randomRow: Int
            var randomColumn: Int


            minesNumber = if (minesNumberinput > rowNumber * columnNumber) {
                rowNumber * columnNumber
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
                if (mineField[randomRow][randomColumn] == '.') {
                    mineField[randomRow][randomColumn] = 'X'
                    currentNumOfMines++
                }
            }

            fillWithHints()

        }

        fun drawField() {
            println(" │123456789│\n" +
                    "—│—————————│")
            for (i in mineField.indices) {
                print("${i + 1}|")
                for (j in mineField[i].indices) {
                    when (mineField[i][j]) {
                        'X' -> print('.')
                        'm','M' -> print('*')
                        else -> print(mineField[i][j])
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
    MineFieldOperations.create(9,9, uInputNumOfMines)
    MineFieldOperations.drawField()

    while (MineFieldOperations.gameState != "End"){
                MineFieldOperations.userMarks()

}


}


