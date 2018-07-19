package com.papashkin.sudoku

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.main_activity.*
import java.io.FileNotFoundException

const val fileName = "sudoku.txt"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        try{
            val file = openFileInput(fileName)
            file.close()
            loadBtn.visibility = View.VISIBLE
            deleteBtn.visibility = View.VISIBLE

        }catch(e: FileNotFoundException) {
            loadBtn.visibility = View.INVISIBLE
            deleteBtn.visibility = View.INVISIBLE
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
            R.id.rateBtn ->{
                var rateView = Intent(Intent.ACTION_VIEW)
                rateView.data = Uri.parse("market://details?id=com.papashkin.sudoku")
                startActivity(rateView)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun newGame(view: View){
        val levels= arrayOf(getString(R.string.easy_level),
                getString(R.string.medium_level), getString(R.string.hard_level))
        val levelMsg = AlertDialog.Builder(this)
        val dialogView = this.layoutInflater.inflate(R.layout.custom_dialog, null)
        levelMsg.setView(dialogView)
        levelMsg.setTitle(R.string.dif_level)   //("Уровень сложности")
        levelMsg.setMessage(R.string.level_question) //("Выберите уровень сложности:")
        levelMsg.setNeutralButton(levels[0]) {_, _ -> setSecondActivity(0) }    // "Легкий"
        levelMsg.setNegativeButton(levels[1]) {_, _ -> setSecondActivity(1) }   // "Средний" // instead to: dialog, whichButton -> setSecondActivity(1)
        levelMsg.setPositiveButton(levels[2]) { _, _ -> setSecondActivity(2) }  // "Сложный"
        levelMsg.setCancelable(true)
        levelMsg.create().show()
    }

    fun loadGame(view: View){
        val toSecondScr = Intent(this, SecondActivity::class.java)
        toSecondScr.putExtra("gameType", "LOAD")
        finish()
        startActivity(toSecondScr)
    }

    fun deleteGame(view: View){
        deleteFile(fileName)
        loadBtn.visibility = View.INVISIBLE
        deleteBtn.visibility - View.INVISIBLE
        deleteBtn.visibility = View.GONE
    }

    private fun setSecondActivity(value: Int ){
        val levels = arrayOf(getString(R.string.easy_level),
                getString(R.string.medium_level), getString(R.string.hard_level))
        val msg = getString(R.string.level_msg, levels[value].toUpperCase())
        (Toast.makeText(this, msg, Toast.LENGTH_SHORT)).show()
        val toSecondScr = Intent(this, SecondActivity::class.java)
        toSecondScr.putExtra("gameType", "NEW")
        toSecondScr.putExtra("levelCount", value)
        finish()
        startActivity(toSecondScr)
    }
}