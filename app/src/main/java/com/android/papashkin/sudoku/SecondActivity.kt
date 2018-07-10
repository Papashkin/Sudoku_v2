package com.android.papashkin.sudoku

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.TableRow
import android.widget.TextView
import kotlinx.android.synthetic.main.second_activity.*
import java.io.*
import java.util.*

class SecondActivity : AppCompatActivity() {

    private var aSudoku: Sudoku = Sudoku(9,9)
    private var posSudoku = Sudoku(9,9)
    private var tabCellId = 0
    private var aValue = 0
    private var isHelp = false
    private var level = 0
    private val fileName = "sudoku.txt"
//    private val levelsList = arrayOf(getString(R.string.easy_level),
//            getString(R.string.medium_level), getString(R.string.hard_level)) // "Легкий", "Средний", "Сложный"
    private var base= IntArray(81)
    private var game = IntArray(81)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.second_activity)
        val gameType = intent.getStringExtra("gameType")
        checkGameType(gameType)
        val levels = arrayOf(getString(R.string.easy_level),
                getString(R.string.medium_level), getString(R.string.hard_level)) // "Легкий", "Средний", "Сложный"
        title = getString(R.string.game, levels[level])
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
        inflater.inflate(R.menu.menu_second, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem):Boolean {
        return when (item.itemId) {
            R.id.returnBtn -> {
                returnClick()
                true
            }
            R.id.helper_checkbox -> {
                item.isChecked = !item.isChecked
                isHelp = item.isChecked
                helpMode()
                true
            }
            R.id.start_again -> {
                startAgain()
                true
            }
            R.id.saveGame ->{
                saveGame()
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
            else->1
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
            var vec:IntArray
            val cell = checkID(id)
            for (i: Int in 1..aSudoku.rowLength()) {
                vec = intArrayOf(cell[0],cell[1],i)
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

        if (posSudoku.getValue(i,j) != 0){
            view.setTextColor(Color.BLACK)
        } else if ((posSudoku.getValue(i,j) == 0)&&(aSudoku.getValue(i,j) != 0)){
            view.setTextColor(Color.parseColor("#0000FF"))
        }
        view.textSize = 32f
        view.textAlignment = View.TEXT_ALIGNMENT_CENTER
    }

    private fun returnClick(){
        val toMainScr = Intent(this, MainActivity::class.java)
        finish()
        startActivity(toMainScr)
    }

    private fun helpMode(){
        if (!isHelp) repaint()
        if (tabCellId !=0) possibleValues(tabCellId)
    }

    private fun checkCancel(cell: IntArray){
        val clearBtn = findViewById<ImageButton>(R.id.CleanBtn)
        if (tabCellId != 0 && aSudoku.getValue(cell[0],cell[1]) != 0) clearBtn.visibility = View.VISIBLE
        else clearBtn.visibility = View.INVISIBLE
    }

    private fun checkFullSudoku(step: Int):Boolean{
        var count = 0
        for (row in 0 until aSudoku.rowLength()){
            for (col in 0 until aSudoku.columnLength()){
                when (step){
                    1 -> if (0 == aSudoku.getValue(row,col)) count++    // sudoku is completely filled
                    2 -> if (!aSudoku.isUniqueSolve(intArrayOf(row,col,aSudoku.getValue(row,col)))) count++ // right solve
                }
            }
        }
        return (count==0)
    }

    private fun startAgain(){
        for ( i in 0 until posSudoku.rowLength()){
            for (j in 0 until posSudoku.columnLength()){
                if (0 == posSudoku.getValue(i,j)) {
                    aSudoku.setValue(i,j,posSudoku.getValue(i,j))
                    val id = findViewById<TextView>("$i$j".toInt())
                    id.text = ""
                }
            }
        }
        repaint()
    }

    private fun saveGame(){
        try {
            var str = ""
            for ( i in 0 until posSudoku.rowLength()){
                for (j in 0 until posSudoku.columnLength()) str += posSudoku.getValue(i,j).toString()
            }
            str += " "
            for ( i in 0 until aSudoku.rowLength()){
                for (j in 0 until aSudoku.columnLength()) str += aSudoku.getValue(i,j).toString()
            }
            str += " "
            str += level.toString()
            str += " "
            val bw = BufferedWriter(OutputStreamWriter(openFileOutput(fileName, MODE_PRIVATE)))
            bw.write(str)
            bw.close()
        } catch (e: FileNotFoundException) { e.printStackTrace()
        } catch (e: IOException) { e.printStackTrace() }
    }

    private fun loadGame(){
        try {
            val br = BufferedReader(InputStreamReader(openFileInput(fileName)))
            val bbb = Scanner(br)
            val table = bbb.next()
            val sudoku = bbb.next()
            level = bbb.next().toInt()
            br.close()
            for (i in 0 until posSudoku.getSize()){
                base[i] = Character.getNumericValue(table[i])
                game[i] = sudoku[i] - '0'
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun checkGameType(type: String){
        when (type){
            "LOAD" -> {
                var count = 0
                loadGame()
                for ( i in 0 until aSudoku.rowLength()){
                    for (j in 0 until aSudoku.columnLength()){
                        posSudoku.setValue(i,j,base[count])
                        aSudoku.setValue(i,j,game[count])
                        count++
                    }
                }
            }
            "NEW" ->{
                level = intent.getIntExtra("levelCount",0)
                aSudoku.generate(level)
                for ( i in 0 until aSudoku.rowLength()){
                    for (j in 0 until aSudoku.columnLength()){
                        posSudoku.setValue(i,j,aSudoku.getValue(i,j))
                    }
                }
            }
        }
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
        val levels = arrayOf(getString(R.string.easy_level),
                getString(R.string.medium_level), getString(R.string.hard_level)) // "Легкий", "Средний", "Сложный"
        val aCell = view as TextView
        val aID = checkID(tabCellId)
        if (aCell.isActivated){
            aValue =aCell.text.toString().toInt()
            aSudoku.setValue(aID[0],aID[1],aValue)
            val tabCell = findViewById<TextView>(tabCellId)
            tabCell.text = aCell.text.toString()
            tabCell.setTextColor(Color.parseColor("#0000FF"))
            repaint()
        }
        checkCancel(aID)
        if (checkFullSudoku(1)){
            if (checkFullSudoku(2)){
                val levelMsg = AlertDialog.Builder(this)
                val dialogView = this.layoutInflater.inflate(R.layout.congrats_dialog, null)
                levelMsg.setView(dialogView)
                levelMsg.setTitle(R.string.congrats)
                levelMsg.setMessage(getString(R.string.congrats_msg,levels[level].toLowerCase()))
                levelMsg.setNegativeButton(R.string.ok) { // "OK"
                    _, _ -> returnClick()
                }
                levelMsg.create().show()
            }
        }
    }

    fun cleanCell(view: View) {
        val aCell = findViewById<TextView>(tabCellId)
        val cellCoord = checkID(tabCellId)
        aCell.text = ""
        aSudoku.setValue(cellCoord[0],cellCoord[1],0)
        repaint()
        possibleValues(tabCellId)
        checkCancel(cellCoord)
    }
}