package com.example.papas.myfirstapp

class Sudoku(rows: Int, cols: Int) {
    private var mBoard: Array<IntArray>? = null
    private val boxSize: Int
    private val boardSize: Int

    init {
        mBoard = Array(rows, { IntArray(cols) })
        boardSize = mBoard!!.size
        boxSize = Math.sqrt(boardSize.toDouble()).toInt()
    }

    fun erase() {
        for (i in mBoard!!.indices) {
            for (j in 0 .. mBoard!![0].size) {
                mBoard!![i][j] = 0
            }
        }
    }

    fun rowLength(): Int {
        return mBoard!!.size
    }

    fun columnLength(): Int {
        return mBoard!!.size
    }

    fun generate(lvl:Int){ // Sudoku generator
        val columns: Int = boardSize
        val rows: Int = boardSize
        var aRow =  setFirstRow()
        for (j in 0 until columns){
            this.setValue(0,j,aRow[j])
        }
        for (i in 1 until rows){
            if (i == 3 || i == 6){
                aRow = shiftLeft(aRow, 1)
            }
            aRow = shiftLeft(aRow, 3)
            for (j in 0..(columns-1)){
                setValue(i,j,aRow[j])
            }
        }
        transpose(Math.random())
        overturn(Math.random())
        swapRowsArea(40)
        swapColsArea(40)
        swapRows(80)
        swapCols(80)
        cleanCells(lvl)
    }

    fun setValue(row: Int, col: Int, value: Any) {
        mBoard!![row][col] = value as Int
    }

    fun getValue(row: Int, col: Int): Any {
        return mBoard!![row][col]
    }

    // Method allows to transpose the sudoku matrix.
    private fun transpose(random: Double) {
        if (random > 0.5) {
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
    }

    // Method allows to overturn the sudoku matrix.
    private fun overturn(random: Double) {
        if (random > 0.5) {
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
    }

    // Method allows to swap areas in horizontal
    private fun swapRowsArea(count: Int) {
        var area1: Int
        var area2: Int
        val outSudoku = mBoard!!.clone()
        for (step in 0 until count) {
            area1 = (3 * Math.random()).toInt()
            do
                area2 = (3 * Math.random()).toInt()
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
        }
        mBoard = outSudoku.clone()
    }

    // Method allows to swap areas in vertical
    private fun swapColsArea(count: Int) {
        var area1: Int
        var area2: Int
        val outSudoku = mBoard!!.clone()
        for (step in 0 until count) {
            area1 = (3 * Math.random()).toInt()
            do
                area2 = (3 * Math.random()).toInt()
            while (area2 == area1)
            area1 *= boxSize
            area2 *= boxSize
            for (col in 0 until boxSize) {
                for (row in mBoard!!.indices) {
                    val `val` = mBoard!![row][area1 + col]
                    outSudoku[row][area1 + col] = mBoard!![row][area2 + col]
                    outSudoku[row][area2 + col] = `val`
                }
            }
        }
        mBoard = outSudoku.clone()
    }

    // Method allows to swap columns in one area
    private fun swapCols(count: Int) {
        var area: Int
        var col1: Int
        var col2: Int
        val outSudoku = mBoard!!.clone()
        for (step in 0 until count) {
            area = (3 * Math.random()).toInt()
            area *= boxSize
            col1 = (3 * Math.random()).toInt() + area
            do {
                col2 = (3 * Math.random()).toInt() + area
            } while (col1 == col2)
            for (row in mBoard!!.indices) {
                val value = mBoard!![row][col1]
                outSudoku[row][col1] = mBoard!![row][col2]
                outSudoku[row][col2] = value
            }
        }
        mBoard = outSudoku.clone()
    }

    // Method allows to swap rows in one area
    private fun swapRows(count: Int) {
        var area: Int
        var row1: Int
        var row2: Int
        val outSudoku = mBoard!!.clone()
        for (step in 0 until count) {
            area = ((3 * Math.random()).toInt()) * boxSize
            row1 = (3 * Math.random()).toInt() + area
            do {
                row2 = (3 * Math.random()).toInt() + area
            } while (row1 == row2)
            for (col in mBoard!!.indices) {
                val value = mBoard!![row1][col]
                outSudoku[row1][col] = mBoard!![row2][col]
                outSudoku[row2][col] = value
            }
        }
        mBoard = outSudoku.clone()
    }

    // create well-played game field from the matrix (via random method)
    private fun cleanCells(lvl: Int) {
        var cellValue: IntArray
        var i = Math.pow(boardSize.toDouble(), 2.0).toInt()
        var filledCells = when (lvl){
            0 -> (4 * Math.random()).toInt() + 30
            1 -> (4 * Math.random()).toInt() + 25
            else -> (4 * Math.random()).toInt() + 21
        }
        do {
            cellValue = getCellValue()
            mBoard!![cellValue[0]][cellValue[1]] = 0
            if (isUniqueSolve(cellValue)) {
                i--
            } else {
                mBoard!![cellValue[0]][cellValue[1]] = cellValue[2]
            }
        } while (i > filledCells)
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
        var isUniq = true
        var i = 0
        val aVector = IntArray(9)
        do{
            rndNum = ((Math.random()*9)+1).toInt()    // random value from 1 to 9 including
            for (i in aVector.indices) {
//                var num: Int = i
                if (rndNum == aVector[i]){
                    isUniq = false
                    break
                }
            }
            if (isUniq){
                do{
                    position = ((Math.random()*9)).toInt()  // random position in the row
                }
                while (aVector[position] != 0)
                aVector[position] = rndNum
                i++
            } else isUniq = true
        }
        while (i < aVector.size)
        return aVector
    }

    // Get value from the random not-null cell
    private fun getCellValue(): IntArray {
        val cell = IntArray(3)
        var value: Int
        var row: Int
        var col: Int
        do {
            row = (9 * Math.random()).toInt()
            col = (9 * Math.random()).toInt()
            value = mBoard!![row][col]
        } while (value == 0)
        cell[0] = row
        cell[1] = col
        cell[2] = value
        return cell
    }

    // check the solution of the sudoku (must be one solution)
    private fun isUniqueSolve(cell: IntArray): Boolean {
        var aVal: Int
        val row = cell[0]
        val col = cell[1]
        val value = cell[2]
        val area: IntArray
        for (i in 0 until boardSize) {    // is unique in row
            aVal = mBoard!![row][i]
            if (aVal == value) {
                return false
            }
        }
        for (i in 0 until boardSize) {    // is unique in column
            aVal = mBoard!![i][col]
            if (aVal == value) {
                return false
            }
        }
        area = getAreaIndex(row, col)          // is unique in box 3x3 area
        for (i in area.indices) {
            area[i] = area[i] * 3
        }
        for (i in 0 .. 2) {
            for (j in 0 .. 2) {
                aVal = mBoard!![area[0] + i][area[1] + j]
                if (aVal == value) {
                    return false
                }
            }
        }
        return true
    }

    private fun getAreaIndex(row: Int, col: Int): IntArray {
        val area = IntArray(2)
        val areaIndex = intArrayOf(0, 0, 0, 1, 1, 1, 2, 2, 2)
        area[0] = areaIndex[row]
        area[1] = areaIndex[col]
        return area
    }
}