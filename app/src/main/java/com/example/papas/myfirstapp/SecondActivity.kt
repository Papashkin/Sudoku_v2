package com.example.papas.myfirstapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import kotlinx.android.synthetic.main.second_activity.*

class SecondActivity : AppCompatActivity() {

    private var aSudoku: Sudoku = Sudoku(9,9)
    private var posSudoku = Sudoku(9,9)
    companion object {
        const val LEVEL_COUNT = "level_count"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.second_activity)
        val level = intent.getIntExtra(LEVEL_COUNT, 0)
        title = getString(R.string.game, levelsList[level])
        aSudoku.generate(level)
        for ( i in 0 until aSudoku.rowLength()){
            for (j in 0 until aSudoku.columnLength()){
                posSudoku.setValue(i,j,aSudoku.getValue(i,j))
            }
        }
        for (i: Int in 0 until aSudoku.rowLength()) {
            val aRow  = TableRow(this)
            aRow.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT)
            for (j: Int in 0 until aSudoku.columnLength()) {
                val aColumn = View.inflate(this, R.layout.cell, null) as TextView
                aColumn.id = "$i$j".toInt()
                aColumn.tag = "$i$j".toInt()
                if (checkCellBackground(aColumn.id)==0) aColumn.setBackgroundResource(R.drawable.sudoku_cell_white)
                else aColumn.setBackgroundResource(R.drawable.sudoku_cell_blue)
                aColumn.text = when (aSudoku.getValue(i,j)){
                        0 -> " "
                    else -> aSudoku.getValue(i,j).toString()
                }
                aColumn.textSize = 32f
                aColumn.setTextColor(Color.BLACK)
                aColumn.textAlignment = View.TEXT_ALIGNMENT_CENTER
                aRow.addView(aColumn, j)
            }
            tableLayout.addView(aRow, i)
            val aSolve = View.inflate(this, R.layout.solving_values, null) as TextView
            aSolve.id = (i+1)*100
            aSolve.text = (i+1).toString()
            aSolve.textSize = 30f
            aSolve.textAlignment = View.TEXT_ALIGNMENT_CENTER
            solvingRow.addView(aSolve, i)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = MenuInflater(this)
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem):Boolean {
        return when (item.itemId) {
            R.id.returnBtn -> {
                returnClick()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun cellRender(area: IntArray):Int{
        return when (setOf(area[0],area[1])) {
            setOf(0,2)->0
            setOf(1,1)->0
            setOf(0,0)->0
            setOf(2,2)->0
            else -> 1
        }
    }

    private fun checkID(id: Int):IntArray{
        val cell = IntArray(2)
        cell[0] = (id/10)
        cell[1] = (id%10)
        return cell
    }

    private fun checkCellBackground(id: Int):Int{
        val cell = checkID(id)
        val area = aSudoku.getAreaIndex(cell[0],cell[1])
        return cellRender(area)
    }

    fun clickCell(view: View){
        val cell = checkID(view.id)
        if (posSudoku.getValue(cell[0],cell[1])==0) {
            if (!view.isActivated){
                clearSelect()
                repaint()
                view.setBackgroundResource(R.drawable.selected_cell)
                possibleValues(view.id)
            } else {
                repaintCell(view)
                repaint()
            }
//            possibleValues(view.id)
            view.isActivated = !view.isActivated
        }
    }

    private fun clearSelect() {
        for (i:Int in 0 until aSudoku.rowLength()){
            val aRow = tableLayout.getChildAt(i)
            for (j:Int in 0 until aSudoku.columnLength()){
                val id = "$i$j".toInt()
                val aCell = aRow.findViewWithTag<TextView>(id)
                if (aCell.isActivated) {
                    repaintCell(aCell)
                    aCell.isActivated = false
                }
            }
        }
    }

    private fun repaintCell(view:View){
        if (checkCellBackground(view.id)==0) view.setBackgroundResource(R.drawable.sudoku_cell_white)
        else view.setBackgroundResource(R.drawable.sudoku_cell_blue)
    }

    private fun possibleValues(id:Int){
        val vec = IntArray(3)
        val cell = checkID(id)
        vec[0]=cell[0]
        vec[1]=cell[1]
        for (i:Int in 1 .. aSudoku.rowLength()){
            vec[2]=i
            if (aSudoku.isUniqueSolve(vec)) {
                val aView = solvingRow.getChildAt(i-1)
                aView.setBackgroundColor(Color.CYAN)
            }
        }
    }

    private fun repaint(){
        for (i:Int in 0 until solvingRow.childCount){
                val cell = solvingRow.getChildAt(i)
                cell.setBackgroundColor(Color.WHITE)
            }
        }

    private fun returnClick(){
        val toMainScr = Intent(this, MainActivity::class.java)
        finish()
        startActivity(toMainScr)
    }

    fun fillCell(view: View){

    }

    fun cleanCell(view: View){

    }
}
