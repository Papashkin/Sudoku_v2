package com.example.papas.myfirstapp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.second_activity.*

class SecondActivity : AppCompatActivity() {

    var sudoku: Sudoku = Sudoku(9,9)

    companion object {
        const val LEVEL_COUNT = "level_count"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.second_activity)
        val level = textLevel()
//        sudoku.cleanCells(level)

    }

    fun returnClick(view: View){
        val toMainScr = Intent(this, MainActivity::class.java)
        startActivity(toMainScr)
    }

    private fun textLevel(): Int{
        val level = intent.getIntExtra(LEVEL_COUNT, 0)
        level_id.text = getString(R.string.level_id, level)
        return level
    }
}