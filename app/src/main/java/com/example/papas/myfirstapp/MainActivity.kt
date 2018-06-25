package com.example.papas.myfirstapp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.Toast


val levelsList = arrayOf("Легкий", "Средний", "Сложный")

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val startBtn:Button = findViewById(R.id.startBtn)
        val loadBtn:Button = findViewById(R.id.loadBtn)

        startBtn.setOnClickListener{
            newGame()
        }
        loadBtn.setOnClickListener{
            (Toast.makeText(this, "А нечего загружать :(", Toast.LENGTH_SHORT)).show()
        }
    }

    private fun newGame() {
        val levelMsg = AlertDialog.Builder(this)
        val dialogView = this.layoutInflater.inflate(R.layout.custom_dialog, null)

        levelMsg.setView(dialogView)
        levelMsg.setTitle("Уровень сложности")
        levelMsg.setMessage("Выберите уровень сложности:")
        levelMsg.setNeutralButton(levelsList[0], { // "Легкий"
            _, _ -> setSecondActivity(0)
        })
        levelMsg.setNegativeButton(levelsList[1], { // "Средний"
            _, _ -> setSecondActivity(1)    // instead to: dialog, whichButton -> setSecondActivity(1)
        })
        levelMsg.setPositiveButton(levelsList[2], { // "Сложный"
            _, _ -> setSecondActivity(2)
        })
        levelMsg.setCancelable(true)
        levelMsg.create().show()
    }

    private fun setSecondActivity(value: Int ){
        (Toast.makeText(this, ("Выбран "+levelsList[value].toUpperCase()+" уровень"), Toast.LENGTH_SHORT)).show()
        val toSecondScr = Intent(this, SecondActivity::class.java)
        toSecondScr.putExtra(SecondActivity.LEVEL_COUNT, value)
        startActivity(toSecondScr)
    }
}