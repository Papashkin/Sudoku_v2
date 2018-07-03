package com.example.papas.myfirstapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.TableRow
import android.widget.TextView
import kotlinx.android.synthetic.main.second_activity.*

class SecondActivity : AppCompatActivity() {

    private var aSudoku: Sudoku = Sudoku(9,9)
    private var posSudoku = Sudoku(9,9)
    private var tabCellId = 0
    private var aValue = 0
    private var isHelp:Boolean = false
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
                setCellParams(aColumn,i,j)
                repaintCell(aColumn)
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
            R.id.helper_checkbox ->{
                item.isChecked = !item.isChecked
                isHelp = item.isChecked
                helpMode()
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
        repaint()
        if (isHelp) {
            val vec = IntArray(3)
            val cell = checkID(id)
            vec[0] = cell[0]
            vec[1] = cell[1]
            for (i: Int in 1..aSudoku.rowLength()) {
                vec[2] = i
                if (aSudoku.isUniqueSolve(vec)) {
                    val aView = solvingRow.getChildAt(i - 1)
                    aView.setBackgroundColor(Color.CYAN)
                    aView.isActivated = true
                    aView.isClickable = true
                }
            }
        } else {
            for (i: Int in 0 until aSudoku.rowLength()) {
                val aView = solvingRow.getChildAt(i)
                aView.isActivated = true
                aView.isClickable = true
            }
        }
    }

    private fun repaint(){
        for (i:Int in 0 until solvingRow.childCount){
            val cell = solvingRow.getChildAt(i)
            cell.setBackgroundColor(Color.WHITE)
            cell.isActivated=false
            cell.isClickable=true
        }
    }

    private fun setCellParams(view:TextView, i: Int, j: Int) {
        view.id = "$i$j".toInt()
        view.tag = "$i$j".toInt()
        view.text = when (aSudoku.getValue(i,j)){
            0 -> " "
            else -> aSudoku.getValue(i,j).toString()
        }
        view.textSize = 32f
        view.setTextColor(Color.BLACK)
        view.textAlignment = View.TEXT_ALIGNMENT_CENTER
    }

    private fun returnClick(){
        val toMainScr = Intent(this, MainActivity::class.java)
        finish()
        startActivity(toMainScr)
    }

    private fun helpMode(){
        if (!isHelp){
            repaint()
            if (tabCellId !=0) possibleValues(tabCellId)
        }else{
            if (tabCellId !=0) possibleValues(tabCellId)
        }
    }

    private fun checkCancel(cell: IntArray){
        val clearBtn = findViewById<ImageButton>(R.id.CleanBtn)
        if (tabCellId != 0 && aSudoku.getValue(cell[0],cell[1]) != 0) clearBtn.visibility = View.VISIBLE
        else clearBtn.visibility = View.INVISIBLE
    }

    fun clickCell(view: View){
        val cell = checkID(view.id)
        if (posSudoku.getValue(cell[0],cell[1])==0) {
            if (!view.isActivated){
                tabCellId = view.id
                clearSelect()
                view.setBackgroundResource(R.drawable.selected_cell)
                possibleValues(tabCellId)
            } else {
                tabCellId=0
                repaintCell(view)
                repaint()
            }
            view.isActivated = !view.isActivated
        }
        checkCancel(cell)
    }

    fun fillCell(view: View){
        val aCell = view as TextView
        val aID = checkID(tabCellId)
        if (aCell.isActivated){
            aValue =aCell.text.toString().toInt()
            aSudoku.setValue(aID[0],aID[1],aValue)
            val tabCell = findViewById<TextView>(tabCellId)
            tabCell.text = aCell.text.toString()
            tabCell.setTextColor(Color.parseColor("#0000FF"))
            repaint()
//            possibleValues(tabCellId)
        }
        checkCancel(aID)
    }

    fun cleanCell(view: View) {
        val aCell = findViewById<TextView>(tabCellId)
        val cellCoord = checkID(tabCellId)
        aCell.text = ""
        aSudoku.setValue(cellCoord[0],cellCoord[1],0)
        repaint()
        possibleValues(tabCellId)
        view.visibility = View.INVISIBLE
    }
}