package com.example.guessthephrase

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    //declaration
    lateinit var clMain: ConstraintLayout
    lateinit var guessField: EditText
    lateinit var guessButton: Button
    lateinit var messages: ArrayList<String>
    lateinit var stPhrase: TextView
    lateinit var stLetters: TextView

    val answer = "This is the Secret Phrase"
    val answerDictionary = mutableMapOf<Int, Char>()
    var theAnswer = ""
    var guessLetter = ""
    var count = 0
    var guessPhrase = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        for(i in answer.indices){
            if(answer[i] == ' '){
                answerDictionary[i] == ' '
                theAnswer += ' '
            }else{
                answerDictionary[i] = '*'
                theAnswer += '*'
            }
        }
        //Initialization
        clMain = findViewById(R.id.clMain)
        guessField = findViewById(R.id.GuessField)
        guessButton = findViewById(R.id.GuessButton)

        stPhrase = findViewById(R.id.stPhrase)
        stLetters = findViewById(R.id.stLetters)

        messages = ArrayList()

        stMessages.adapter = MessageAdapter(this,messages)
        stMessages.layoutManager = LinearLayoutManager(this)

        //setOnClickListener
        guessButton.setOnClickListener { addMessage() }

        update()
    }

    //Function to add more message
    @SuppressLint("NotifyDataSetChanged")
    fun addMessage(){
        val msg = guessField.text.toString()

        if(guessPhrase){
            if(msg == answer){
                disableEntry()
                showAlertDialog("YOU ARE WIN!! \n play again?")
            }else{
                messages.add("WRONG Guess The message is $msg")
                //update the guessPhrase value
                guessPhrase = false
                update()
            }
        }else{
            //Make sure the user enter only one letter
            if(msg.isNotEmpty() && msg.length == 1){
                theAnswer = ""
                guessPhrase = true
                check(msg[0])
            }else{
                Snackbar.make(clMain, "PLEASE Enter One Letter Only!!!",
                    Snackbar.LENGTH_LONG).show()
            }
        }
        //Clear the filed and stMessage to new Entry
        guessField.text.clear()
        guessField.clearFocus()
        stMessages.adapter?.notifyDataSetChanged()
    }

    fun disableEntry(){
        guessButton.isEnabled = false
        guessButton.isClickable = false
        guessField.isEnabled = false
        guessField.isClickable = false
    }

    //Function to update the letter
    fun update(){
        stPhrase.text =("Phrase: " + theAnswer.toUpperCase())
        stLetters.text = "Guessed Letter is: " + guessLetter
        if(guessPhrase){//true
            guessField.hint = "Try to Guess the full Phrase"
        }else{
            guessField.hint = "Guess a Letter"
        }
    }

    //Function to check the letter
    fun check(guessLetters: Char){
        var isFound = 0
        for( i in answer.indices){
            if(answer[i] == guessLetters){
                answerDictionary[i] = guessLetters
                isFound++
            }
        }
        for(i in answerDictionary){
            theAnswer+=answerDictionary[i.key]
        }
        if(theAnswer == answer){
            disableEntry()
            showAlertDialog("YOU ARE WIN!! \n play again?")
        }
        if(guessLetter.isEmpty()){
            guessLetter+=guessLetter
        }
        else{
            guessLetter+=", "+guessLetter
        }
        if(isFound > 0 ){
            messages.add("Found $isFound ${guessLetters.toUpperCase()}(s)")
        }else{
            messages.add("NO ${guessLetters.toUpperCase()} s Found")
        }
        count++
        //decrease the number of chances (the guesses remain)
        val guessLeft = 10 - count
        if(count < 10 ){
            messages.add("$guessLeft guesses remaining!!")
        }
        update()
        stMessages.scrollToPosition(messages.size - 1 )
    }

    //showAlertDialog Function
    fun showAlertDialog(title: String) {
        // build alert dialog
        val dialogBuilder = AlertDialog.Builder(this)

        // set message of alert dialog
        dialogBuilder.setMessage(title)
            // if the dialog is cancelable
            .setCancelable(false)
            // positive button text and action
            .setPositiveButton("Yes", DialogInterface.OnClickListener {
                    dialog, id -> this.recreate()
            })
            // negative button text and action
            .setNegativeButton("No", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
            })

        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle("Game Over")
        // show alert dialog
        alert.show()
    }
}




