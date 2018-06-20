package com.example.papas.myfirstapp

class Sudoku(rows: Int, cols: Int) {
    private var mBoard: Array<IntArray>? = null
    private val boxSize: Int
    private val boardSize: Int

    init {
        mBoard = Array(rows) { IntArray(cols) }
        boardSize = mBoard!!.size
        boxSize = Math.sqrt(boardSize.toDouble()).toInt()
    }

    fun erase() {
        for (i in mBoard!!.indices) {
            for (j in 0 until mBoard!![0].size) {
                mBoard!![i][j] = 0
            }
        }
    }

    fun setValue(row: Int, col: Int, value: Any) {
        mBoard!![row][col] = value as Int
    }

    fun getValue(row: Int, col: Int): Any {
        return mBoard!![row][col]
    }

    fun rowLength(): Int {
        return mBoard!!.size
    }

    fun columnLength(): Int {
        return mBoard!!.size
    }

    fun transpose(random: Double) {   // Method allows to transpose the sudoku matrix.
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

    fun overturn(random: Double) {   // Method allows to overturn the sudoku matrix.
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

    fun swapRowsArea(count: Int) {   // Method allows to swap areas in horizontal
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
                    val `val` = mBoard!![area1 + row][col]
                    outSudoku[area1 + row][col] = mBoard!![area2 + row][col]
                    outSudoku[area2 + row][col] = `val`
                }
            }
        }
        mBoard = outSudoku.clone()
    }

    fun swapColsArea(count: Int) {   // Method allows to swap areas in vertical
        var area1: Int
        var area2: Int
        val outSudoku = mBoard!!.clone()
        for (step in 0 .. count) {
            area1 = (3 * Math.random()).toInt()
            do
                area2 = (3 * Math.random()).toInt()
            while (area2 == area1)
            area1 *= boxSize
            area2 *= boxSize
            for (col in 0 .. boxSize) {
                for (row in mBoard!!.indices) {
                    val `val` = mBoard!![row][area1 + col]
                    outSudoku[row][area1 + col] = mBoard!![row][area2 + col]
                    outSudoku[row][area2 + col] = `val`
                }
            }
        }
        mBoard = outSudoku.clone()
    }

    fun swapCols(count: Int) {   // Method allows to swap columns in one area
        var area: Int
        var col1: Int
        var col2: Int
        val outSudoku = mBoard!!.clone()
        for (step in 0 .. count) {
            area = (3 * Math.random()).toInt()
            area = area * boxSize
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

    fun swapRows(count: Int) {   // Method allows to swap rows in one area
        var area: Int
        var row1: Int
        var row2: Int
        val outSudoku = mBoard!!.clone()
        for (step in 0 .. count) {
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

    fun cleanCells(lvl: Int) {        // create well-played game field from the matrix (via random method)
        val filledCells: Int
        var cellValue: IntArray
        var i = Math.pow(boardSize.toDouble(), 2.0).toInt()
        if (lvl == 0) {
            filledCells = (4 * Math.random()).toInt() + 31
        } else if (lvl == 1) {
            filledCells = (4 * Math.random()).toInt() + 26
        } else
            filledCells = (4 * Math.random()).toInt() + 21
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

    private fun getCellValue(): IntArray {  // Get value from the random not-null cell
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

    private fun isUniqueSolve(cell: IntArray): Boolean { // check the solution of the sudoku (must be one solution)
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
        for (i in 0..2) {
            for (j in 0..2) {
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