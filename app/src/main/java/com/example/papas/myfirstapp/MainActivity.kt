package com.example.papas.myfirstapp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import kotlinx.android.synthetic.main.main_activity.*
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.nio.file.Files

val levelsList = arrayOf("Легкий", "Средний", "Сложный")
const val fileName = "sudoku.txt"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        val startBtn:Button = findViewById(R.id.startBtn)
        val loadBtn:Button = findViewById(R.id.loadBtn)
//        val exitBtn:Button = findViewById(R.id.exitBtn)
        val deleteBtn: Button = findViewById(R.id.deleteBtn)

        try{
            val file = openFileInput(fileName)
            file.close()
            loadBtn.visibility = View.VISIBLE
            deleteBtn.visibility = View.VISIBLE

        }catch(e: FileNotFoundException) {
            loadBtn.visibility = View.INVISIBLE
            deleteBtn.visibility = View.INVISIBLE
        }

        startBtn.setOnClickListener{
            newGame()
        }
        loadBtn.setOnClickListener{
            loadGame()
        }
//        exitBtn.setOnClickListener {
//            exitGame()
//        }
        deleteBtn.setOnClickListener {
            deleteGame()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = MenuInflater(this)
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem):Boolean {
        return when (item.itemId) {
            R.id.exitBtn -> {
                finish()
                System.exit(0)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun newGame() {
        val levelMsg = AlertDialog.Builder(this)
        val dialogView = this.layoutInflater.inflate(R.layout.custom_dialog, null)

        levelMsg.setView(dialogView)
        levelMsg.setTitle("Уровень сложности")
        levelMsg.setMessage("Выберите уровень сложности:")
        levelMsg.setNeutralButton(levelsList[0]) { // "Легкий"
            _, _ -> setSecondActivity(0)
        }
        levelMsg.setNegativeButton(levelsList[1]) { // "Средний"
            _, _ -> setSecondActivity(1)    // instead to: dialog, whichButton -> setSecondActivity(1)
        }
        levelMsg.setPositiveButton(levelsList[2]) { // "Сложный"
            _, _ -> setSecondActivity(2)
        }
        levelMsg.setCancelable(true)
        levelMsg.create().show()
    }

    private fun setSecondActivity(value: Int ){
        val msg = getString(R.string.level_msg, levelsList[value].toUpperCase())
        (Toast.makeText(this, msg, Toast.LENGTH_SHORT)).show()
        val toSecondScr = Intent(this, SecondActivity::class.java)
        toSecondScr.putExtra("gameType", "NEW")
        toSecondScr.putExtra("levelCount", value)
        finish()
        startActivity(toSecondScr)
    }

    private fun loadGame(){
//        (Toast.makeText(this, "А нечего загружать :(", Toast.LENGTH_SHORT)).show()
        val toSecondScr = Intent(this, SecondActivity::class.java)
        toSecondScr.putExtra("gameType", "LOAD")
        finish()
        startActivity(toSecondScr)
    }

    private fun deleteGame(){
        deleteFile(fileName)
        loadBtn.visibility = View.INVISIBLE
        deleteBtn.visibility - View.INVISIBLE
        deleteBtn.visibility = View.GONE
    }
}