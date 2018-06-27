package com.example.papas.myfirstapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TableRow
import android.widget.TextView
import kotlinx.android.synthetic.main.second_activity.*

class SecondActivity : AppCompatActivity() {

    private var sudoku: Sudoku = Sudoku(9,9)
    companion object {
        const val LEVEL_COUNT = "level_count"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val solvedSudoku = Sudoku(9,9)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.second_activity)
        val level = intent.getIntExtra(LEVEL_COUNT, 0)
        title = getString(R.string.game, levelsList[level])
        sudoku.generate(level)
        for ( i in 0 until sudoku.rowLength()){
            for (j in 0 until sudoku.columnLength()){
                solvedSudoku.setValue(i,j,sudoku.getValue(i,j))
            }
        }
        for (i: Int in 0 until sudoku.rowLength()) {
            val aRow  = TableRow(this)
            aRow.id = i
            aRow.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT)
            aRow.setBackgroundColor(Color.BLACK)
            aRow.textAlignment = View.TEXT_ALIGNMENT_CENTER

            for (j: Int in 0 until sudoku.columnLength()) {
                val row = i+1
                val aColumn = View.inflate(this, R.layout.cell, null) as TextView
                if (getCellColor(i,j)==0) {
                    aColumn.setBackgroundResource(R.drawable.sudoku_cell_white)
                } else {
                    aColumn.setBackgroundResource(R.drawable.sudoku_cell_blue)
                }
                aColumn.id = "$row$j".toInt()
                aColumn.text = when (sudoku.getValue(i,j)){
                        0 -> " "
                    else -> sudoku.getValue(i,j).toString()
                }
                aColumn.textSize = 32f
                aColumn.setTextColor(Color.BLACK)
                aColumn.textAlignment = View.TEXT_ALIGNMENT_CENTER
                aRow.addView(aColumn, j)
            }
            tableLayout.addView(aRow, i)
        }
    }

    private fun getCellColor(row: Int, col: Int):Int{
        return when {
            (row ==0 && col ==3)||(row ==0 && col ==4)||(row ==0 && col ==5)->0
            (row ==1 && col ==3)||(row ==1 && col ==4)||(row ==1 && col ==5)->0
            (row ==2 && col ==3)||(row ==2 && col ==4)||(row ==2 && col ==5)->0

            (row ==3 && col ==0)||(row ==3 && col ==1)||(row ==3 && col ==2)->0
            (row ==4 && col ==0)||(row ==4 && col ==1)||(row ==4 && col ==2)->0
            (row ==5 && col ==0)||(row ==5 && col ==1)||(row ==5 && col ==2)->0

            (row ==3 && col ==6)||(row ==3 && col ==7)||(row ==3 && col ==8)->0
            (row ==4 && col ==6)||(row ==4 && col ==7)||(row ==4 && col ==8)->0
            (row ==5 && col ==6)||(row ==5 && col ==7)||(row ==5 && col ==8)->0

            (row ==6 && col ==3)||(row ==6 && col ==4)||(row ==6 && col ==5)->0
            (row ==7 && col ==3)||(row ==7 && col ==4)||(row ==7 && col ==5)->0
            (row ==8 && col ==3)||(row ==8 && col ==4)||(row ==8 && col ==5)->0
            else -> 1
            }
    }

    private fun checkCellBackground(id: Int):Int{
        val row = id/10
        val col = id%10
        return getCellColor(row-1,col)
    }

    fun clickCell(view: View){
        if (!view.isActivated){
            view.setBackgroundResource(R.drawable.selected_cell)
            view.isActivated = true
        }
        else {
            if (checkCellBackground(view.id)==0) view.setBackgroundResource(R.drawable.sudoku_cell_white)
            else view.setBackgroundResource(R.drawable.sudoku_cell_blue)
            view.isActivated = false
        }
    }

    fun returnClick(view: View){
        val toMainScr = Intent(this, MainActivity::class.java)
        startActivity(toMainScr)
    }
}