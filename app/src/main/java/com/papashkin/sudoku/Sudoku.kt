package com.papashkin.sudoku

class Sudoku(rows: Int, cols: Int) {
    private var mBoard: Array<IntArray>? = null
    private val boxSize: Int
    private val boardSize: Int

    init {
        mBoard = Array(rows) { IntArray(cols) }
        boardSize = mBoard!!.size
        boxSize = Math.sqrt(boardSize.toDouble()).toInt()
    }

    fun rowLength(): Int {
        return mBoard!!.size
    }

    fun columnLength(): Int {
        return mBoard!!.size
    }
    fun getSize():Int{
        return boardSize*boardSize
    }

    fun setValue(row: Int, col: Int, value: Int) {
        mBoard!![row][col] = value
    }

    fun getValue(row: Int, col: Int): Int {
        return mBoard!![row][col]
    }

    // Sudoku generator
    fun generate(lvl:Int){
        val columns: Int = boardSize
        val rows: Int = boardSize
        var aRow =  setFirstRow()
        for (j in 0 until columns){
            this.setValue(0,j,aRow[j])
        }
        for (i in 1 until rows){
            if (i == 3 || i == 6){
                aRow = this.shiftLeft(aRow, 1)
            }
            aRow = this.shiftLeft(aRow, 3)
            for (j in 0..(columns-1)){
                this.setValue(i,j,aRow[j])
            }
        }
        var lastIndex = 0
        for (i in 0 .. 200) {
            var index: Int
            do
                index = getRandomNumber(1,8)
            while (index == lastIndex)
            when (index) {
                1 -> this.overturn()
                2 -> this.swapRows()
                3 -> this.swapCols()
                4 -> this.swapRowsArea()
                5 -> this.swapColsArea()
                6 -> this.transpose()
                7 -> this.changeNumbers()
            }
            lastIndex = index
        }
        this.cleanCells(lvl)
    }

    // Method allows to transpose the sudoku matrix.
    private fun transpose() {
        val row = mBoard!!.size
        val col = mBoard!!.size
        val oSudoku = Array(row) { IntArray(col) }
        for (i in 0 until row) {
            for (j in 0 until col) {
                oSudoku[j][i] = mBoard!![i][j]
            }
        }
        mBoard = oSudoku.clone()
    }

    // Method allows to overturn the sudoku matrix.
    private fun overturn() {
        val row = mBoard!!.size
        val col = mBoard!!.size
        val oSudoku = Array(row) { IntArray(col) }
        for (i in 0 until row) {
            for (j in 0 until col) {
                oSudoku[col - i - 1][j] = mBoard!![i][j]
            }
        }
        mBoard = oSudoku.clone()
    }

    // Method allows to swap areas in horizontal
    private fun swapRowsArea() {
        var area1 = getRandomNumber(0,3)    // var area1 = (3 * Math.random()).toInt()
        var area2: Int
        val outSudoku = mBoard!!.clone()
        do
            area2 = getRandomNumber(0,3)
        while (area2 == area1)
        area1 *= 3
        area2 *= 3
        for (row in 0..2) {
            for (col in mBoard!!.indices) {
                val value = mBoard!![area1 + row][col]
                outSudoku[area1 + row][col] = mBoard!![area2 + row][col]
                outSudoku[area2 + row][col] = value
            }
        }
        mBoard = outSudoku.clone()
    }

    // Method allows to swap areas in vertical
    private fun swapColsArea(){
        this.transpose()
        this.swapRowsArea()
        this.transpose()
    }

    // Method allows to swap rows in one area
    private fun swapRows() {
        val area = getRandomNumber(0,3)*boxSize
        val row1 = area + getRandomNumber(0,3)
        var row2: Int
        val outSudoku = mBoard!!.clone()
        do {
            row2 = getRandomNumber(0,3) + area
        } while (row1 == row2)
        for (col in mBoard!!.indices) {
            val value = mBoard!![row1][col]
            outSudoku[row1][col] = mBoard!![row2][col]
            outSudoku[row2][col] = value
        }
        mBoard = outSudoku.clone()
    }

    // Method allows to swap columns in one area
    private fun swapCols(){
        this.transpose()
        this.swapRows()
        this.transpose()
    }

    // create well-played game field from the matrix (via random method)
    private fun cleanCells(lvl: Int) {
        var cellValue: IntArray
        var i = Math.pow(boardSize.toDouble(), 2.0).toInt()
        val filledCells = when (lvl){
            0 -> getRandomNumber(0,3) + 28   //(2 * Math.random()).toInt() + 28
            1 -> getRandomNumber(0,3) + 24   //(2 * Math.random()).toInt() + 24
            else -> getRandomNumber (0,3) + 21  //2 * Math.random()).toInt() + 21
        }
        do {
            cellValue = this.getCellValue()
            mBoard!![cellValue[0]][cellValue[1]] = 0
            if (isUniqueSolve(cellValue)) {
                i--
            } else {
                mBoard!![cellValue[0]][cellValue[1]] = cellValue[2]
            }
        } while (i > filledCells)
    }

    private fun changeNumbers(){
        val x = getRandomNumber(1,9)
        var y: Int
        do y = getRandomNumber(1,9)
            while (x == y)
        for (i in 0 until this.rowLength()) {
            for (j in 0 until this.columnLength()) {
                val num = this.getValue(i,j)
                when (num){
                    x -> this.setValue(i,j,y)
                    y -> this.setValue(i,j,x)
                }
            }
        }
    }

    private fun getRandomNumber(min:Int, max:Int):Int {
        return (Math.random() * (max - min) + min).toInt()
    }

    //shift the number vector to the left by shiftCount value
    private fun shiftLeft (inVector:IntArray, shiftCount:Int):IntArray{
        val outVector = IntArray(inVector.size)
        for (j in 0 until shiftCount) outVector[j+(inVector.size)-shiftCount] = inVector[j]
        for (j in 0 until (inVector.size)-shiftCount) outVector[j] = inVector[j+shiftCount]
        return outVector
    }

    // build first row for matrix via random method
    private fun setFirstRow(): IntArray{
        var rndNum: Int
        var position: Int
        var isUnique = true
        var i = 0
        val aVector = IntArray(9)
        do{
            rndNum = getRandomNumber(1,10)   // random value from 1 to 9 including
            for (i in aVector.indices) {
                if (rndNum == aVector[i]){
                    isUnique = false
                    break
                }
            }
            if (isUnique){
                do{
                    position = getRandomNumber(0,9)  // random position in the row
                }
                while (aVector[position] != 0)
                aVector[position] = rndNum
                i++
            } else isUnique = true
        }
        while (i < aVector.size)
        return aVector
    }

    // Get value from the random not-null cell
    private fun getCellValue(): IntArray {
        val cell: IntArray
        var value: Int
        var row: Int
        var col: Int
        do {
            row = getRandomNumber(0,9)
            col = getRandomNumber(0,9)
            value = mBoard!![row][col]
        } while (value == 0)
        cell = intArrayOf(row,col,value)
        return cell
    }

    // check the solution of the sudoku (must be one solution)
    fun isUniqueSolve(cell: IntArray): Boolean {
        var isCorrect = true
        var aVal: Int
        val row = cell[0]
        val col = cell[1]
        val value = cell[2]
        val area: IntArray
        for (i in 0 until boardSize) {    // is unique in row
            aVal = mBoard!![row][i]
            if (aVal == value && i != col) isCorrect = false
        }
        for (i in 0 until boardSize) {    // is unique in column
            aVal = mBoard!![i][col]
            if (aVal == value && i != row) isCorrect = false
        }
        area = getAreaIndex(row, col)          // is unique in box 3x3 area
        for (i in area.indices) {area[i] *= 3}
        for (i in 0 .. 2) {
            for (j in 0 .. 2) {
                aVal = mBoard!![area[0] + i][area[1] + j]
                if (aVal == value && (area[0] + i) != row && (area[1] + j) != col) isCorrect = false
            }
        }
        return isCorrect
    }

    fun getAreaIndex(row: Int, col: Int): IntArray {
        val areaIndex = intArrayOf(0, 0, 0, 1, 1, 1, 2, 2, 2)
        return intArrayOf(areaIndex[row],areaIndex[col])
    }
}