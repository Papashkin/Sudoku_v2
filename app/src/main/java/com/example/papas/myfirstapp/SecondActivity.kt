package com.example.papas.myfirstapp

import android.app.ProgressDialog.show
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.second_activity.*

class SecondActivity : AppCompatActivity() {

    private var sudoku: Sudoku = Sudoku(9,9)
    companion object {
        const val LEVEL_COUNT = "level_count"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        var solvedSudoku: Sudoku = Sudoku(9,9)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.second_activity)
        val level = textLevel()
        sudoku.generate(level)
        for ( i in 0 until sudoku.rowLength()){
            for (j in 0 until sudoku.columnLength()){
                solvedSudoku.setValue(i,j,sudoku.getValue(i,j))
            }
        }
        for (i: Int in 0 until sudoku.rowLength()) {
            var aRow  = TableRow(this)
            aRow.id = i
            aRow.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT)
            aRow.setBackgroundColor(Color.WHITE)
            aRow.textAlignment = View.TEXT_ALIGNMENT_CENTER

            for (j: Int in 0 until sudoku.columnLength()) {
                var aColumn = TextView(this)
                aColumn.layoutParams = TableRow.LayoutParams(110,121)
                aColumn.id = j
                aColumn.text = when (sudoku.getValue(i,j)){
                        0 -> " "
                    else -> sudoku.getValue(i,j).toString()
                }
                aColumn.textSize = 32f
                getCellColor(i,j,aColumn)
                aColumn.setTextColor(Color.BLACK)
                aColumn.textAlignment = View.TEXT_ALIGNMENT_CENTER
                aRow.addView(aColumn, j)
            }
            tableLayout.addView(aRow, i)
        }
    }

    private fun textLevel(): Int{
        val level = intent.getIntExtra(LEVEL_COUNT, 0)
        level_id.text = getString(R.string.level_id, levelsList[level])
        return level
    }

    private fun getCellColor(row: Int, col: Int, cell:TextView){
        var count = when {
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
        when (count){
            0 -> cell.setBackgroundResource(R.drawable.sudoku_cell_white)
            1 -> cell.setBackgroundResource(R.drawable.sudoku_cell_beige)
        }
    }

    fun checkCell(view: View){
        when (view.id){
            1-> (Toast.makeText(this, ("Выбрана первая ячейка"), Toast.LENGTH_SHORT)).show()
            8 -> (Toast.makeText(this, ("Выбрана восьмая ячейка"), Toast.LENGTH_SHORT)).show()
            9 -> (Toast.makeText(this, ("Выбрана девятая ячейка"), Toast.LENGTH_SHORT)).show()
            else -> (Toast.makeText(this, ("Выбран другая ячейка"), Toast.LENGTH_SHORT)).show()
        }
    }

    fun returnClick(view: View){
        val toMainScr = Intent(this, MainActivity::class.java)
        startActivity(toMainScr)
    }
}