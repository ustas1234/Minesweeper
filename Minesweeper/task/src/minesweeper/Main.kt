package minesweeper

import com.sun.org.apache.xpath.internal.functions.FuncFalse
import com.sun.xml.internal.fastinfoset.util.StringArray
import kotlin.random.Random


class MineFieldOperations {
    companion object {
        private lateinit var mineField: Array<CharArray>
        private lateinit var mineFieldToDraw: Array<CharArray>
        var gameState = "Starting"
        var totalMines= 0
        var minesUnmarked = 0
        var canAutoMark = false

        fun userMarks(uInputNumOfMines: Int) {
            print("Set/unset mines marks or claim a cell as free: > ")
            val (userXInput, userYInput, userAction) = readLine()!!.split(' ')

            if (MineFieldOperations.gameState == "Starting" && userAction == "free") {
                MineFieldOperations.create(9, 9, uInputNumOfMines,userXInput.toInt(),userYInput.toInt())
                gameState = "Going"
            }

            when (userAction) {
                "free" -> {
                    when (mineField[userYInput.toInt() - 1][userXInput.toInt() - 1]) {
                        '*', '.', in '0'.. '9' -> {
                            showHints (userXInput.toInt() - 1, userYInput.toInt() - 1)
                            drawField()
                        }
                        'X' -> {
                            println("You stepped on a mine and failed!")
                            gameState = "End"
                        }
                    }
                }
                "mine" -> {
                    when (mineFieldToDraw[userYInput.toInt() - 1][userXInput.toInt() - 1]) {
                        '.' -> {
                            mineFieldToDraw[userYInput.toInt() - 1][userXInput.toInt() - 1] = '*'
                            drawField()
                        }
                        '*' -> {
                            mineFieldToDraw[userYInput.toInt() - 1][userXInput.toInt() - 1] = '.'
                            drawField()
                        }

                    }
                }
                "show" -> drawFieldReal()
            }

            if (gameState == "Going") checkIfWin()




        }

        private fun checkIfWin (){
            var isAllMinesChecked = true
            var isNonMinesExplored = true
            for (i in 0..8) {
                for (j in 0..8) {
                    if (mineField[i][j] == 'X') {
                        if (mineFieldToDraw[i][j] != '*') isAllMinesChecked = false
                    } else {
                        if (mineFieldToDraw[i][j] == '*' || mineFieldToDraw[i][j] == '.') isNonMinesExplored = false
                    }
                }
            }
            if (isAllMinesChecked && isNonMinesExplored) {
                println("Congratulations! You found all the mines!")
                gameState = "End"
            }
        }

        private fun create(rowNumber: Int, columnNumber: Int, minesNumberinput: Int, firstCellX: Int, firstCellY: Int) {
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
                if (mineField[randomRow][randomColumn] == '.') {
                    if (randomColumn == firstCellX - 1 && randomRow == firstCellY - 1) continue
                    mineField[randomRow][randomColumn] = 'X'
                    currentNumOfMines++
                }
            }

            fillWithHints()

        }

        fun createToDraw (columnNumber: Int, rowNumber: Int) {
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
                    print(mineFieldToDraw[i][j])
                }
                print("|\n")


            }
            println("—│—————————│")
        }

        private fun drawFieldReal() {
            println(" │123456789│\n" +
                    "—│—————————│")
            for (i in mineField.indices) {
                print("${i + 1}|")
                for (j in mineField[i].indices) {
                    print(mineField[i][j])
                }
                print("|\n")


            }
            println("—│—————————│")
        }

        private fun isMine(rowNumber: Int, columnNumber: Int): Boolean {
            return mineField[rowNumber][columnNumber] == 'X'
        }

        private  fun showHints (X: Int, Y: Int){
            if (mineField[Y][X] !in '0'..'9') floodHints(X,Y, mineField[Y][X]) else {
                            for (i in -1..1) {
                for (j in -1..1){
                    if ((X + j) in 0..8 && (Y + i) in 0..8) {
                        when (mineField[Y + i][X + j]) {
                            '.' -> mineFieldToDraw[Y + i][X + j] = '/'
                            '*' -> mineFieldToDraw[Y + i][X + j] = '/'
                            'X' -> mineFieldToDraw[Y + i][X + j] = '.'
                            else -> {
                                mineFieldToDraw[Y + i][X + j] = mineField[Y + i][X + j]
                            }
                        }

/*                            if (!((i == 0) && (j == 0)) &&
                                    ((mineFieldToDraw[Y + i][X + j] !in '0'..'9') &&
                                            (mineField[Y + i][X + j] != 'X'))) {
                                println("Found to go: X${X+ j} Y${Y + i}")
                                //showHints(X + j, Y + i)
                            }*/

                    }
                }
            }
            }




        }

        private fun floodHints(X: Int, Y: Int, prevValue: Char, nextValue: Char = '/'){
            if ((X !in 0..8) || (Y !in 0..8)) return
       //   if (prevValue != mineField[Y][X] || mineFieldToDraw[Y][X] == '/') return
            if (prevValue != mineField[Y][X]) return
         //   if (mineField[Y][X] in '0'..'9') return

            mineField[Y][X] = nextValue
            mineFieldToDraw[Y][X] = nextValue

            for (i in -1..1) {
                for (j in -1..1){
                    if ((X + j) in 0..8 && (Y + i) in 0..8) {
                        when (mineField[Y + i][X + j]) {
                            '.' -> mineFieldToDraw[Y + i][X + j] = '/'
                            '*' -> mineFieldToDraw[Y + i][X + j] = '/'
                            'X' -> mineFieldToDraw[Y + i][X + j] = '.'
                            else -> {
                                mineFieldToDraw[Y + i][X + j] = mineField[Y + i][X + j]
                            }
                        }

                        /*if (!((i == 0) && (j == 0)) &&
                                ((mineFieldToDraw[Y + i][X + j] !in '0'..'9') &&
                                        (mineField[Y + i][X + j] != 'X'))) {
                            println("Found to go: X${X+ j} Y${Y + i}")
                            //showHints(X + j, Y + i)
                        }*/

                    }
                }
            }


            floodHints(X + 1, Y, prevValue, nextValue)
            floodHints(X - 1, Y, prevValue, nextValue)
            floodHints(X , Y + 1, prevValue, nextValue)
            floodHints(X , Y - 1, prevValue, nextValue)

        }



        private fun checkNeighbor(X: Int, Y: Int) {
            for (i in -1..1) {
                for (j in -1..1) {
                    if ((X + j) in 0..8 && (Y + i) in 0..8) {
                        if (mineField[Y + i][X + j] == '.') canAutoMark = true
                    }

                }
            }
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
    MineFieldOperations.totalMines = uInputNumOfMines

  //  MineFieldOperations.create(9,9, uInputNumOfMines)
    while (MineFieldOperations.gameState != "End"){
                MineFieldOperations.userMarks(uInputNumOfMines)

}

}


