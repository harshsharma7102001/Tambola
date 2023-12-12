package com.world4tech.tambola

import android.app.AlertDialog
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.world4tech.tambola.database.playerinfo
import com.world4tech.tambola.databinding.ActivityJoinBinding
import com.world4tech.tambola.util.playBtnClickMusic
import com.world4tech.tambola.util.playFailureMusic
import com.world4tech.tambola.util.playWinnerMusic
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class JoinActivity : AppCompatActivity() {
    var startCount = 1;
    val userTable = Array(5) { IntArray(5) }
    //Current score of players
    var usrscore = 0;
    var player2Score=0
    var first = 1
    var userTurn:Int = 0
    var coveredNo:MutableList<Int> =  mutableListOf()
    //Array for tracking computer and user
    val userTrackArr= arrayListOf<Int>()
    var userOption:Int = 0

    //For tracking row
    var rowMemo = mutableListOf<Int>()
    //For tracking clm
    val clmMemo = mutableListOf<Int>()
    val checklr = mutableListOf<Int>()
    val checkrl =  mutableListOf<Int>()
    private lateinit var database: DatabaseReference
    var player2Number = 0
    var gameCode ="0";
    var player1Name ="Player 1"
    var player2Name = "Player 2"
    var checkWinningTrack:String = "0"
    var onlyFirst = 0
    var onlyOnce = 1
    private lateinit var binding: ActivityJoinBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        //Getting data from user
        gameCode = intent?.extras?.getString("code").toString()
        player1Name = intent?.extras?.getString("Name").toString()

        database = FirebaseDatabase.getInstance().getReference("game")
        startUpdatingBackGround()
        println("Fetched gameCode is: $gameCode")
        println("Fetched playerName is: $player2Name")
        println("Fetched score is: $usrscore")
        println("Fetched turn is: $userTurn")


        binding.txt1.setOnClickListener { updateData(binding.txt1) }
        binding.txt2.setOnClickListener { updateData(binding.txt2) }
        binding.txt3.setOnClickListener { updateData(binding.txt3) }
        binding.txt4.setOnClickListener { updateData(binding.txt4) }
        binding.txt5.setOnClickListener {  updateData(binding.txt5)}
        binding.txt6.setOnClickListener {  updateData(binding.txt6)}
        binding.txt7.setOnClickListener {  updateData(binding.txt7)}
        binding.txt8.setOnClickListener {  updateData(binding.txt8)}
        binding.txt9.setOnClickListener {  updateData(binding.txt9)}
        binding.txt10.setOnClickListener {  updateData(binding.txt10)}
        binding.txt11.setOnClickListener {  updateData(binding.txt11)}
        binding.txt12.setOnClickListener {  updateData(binding.txt12)}
        binding.txt13.setOnClickListener {  updateData(binding.txt13)}
        binding.txt14.setOnClickListener {  updateData(binding.txt14)}
        binding.txt15.setOnClickListener {  updateData(binding.txt15)}
        binding.txt16.setOnClickListener {  updateData(binding.txt16)}
        binding.txt17.setOnClickListener {  updateData(binding.txt17)}
        binding.txt18.setOnClickListener {  updateData(binding.txt18)}
        binding.txt19.setOnClickListener {  updateData(binding.txt19)}
        binding.txt20.setOnClickListener {  updateData(binding.txt20)}
        binding.txt21.setOnClickListener {  updateData(binding.txt21)}
        binding.txt22.setOnClickListener {  updateData(binding.txt22)}
        binding.txt23.setOnClickListener {  updateData(binding.txt23)}
        binding.txt24.setOnClickListener {  updateData(binding.txt24)}
        binding.txt25.setOnClickListener {  updateData(binding.txt25)}
    }

    /*

            Func to handle crossing, btn click updating user table everything

    */
    private fun updateData(txt: TextView) {
        playBtnClickMusic(this@JoinActivity)
        startUpdatingBackGround()
        if (txt.text == "X") {
                binding.currNumber.text = ""
                txt.text = startCount.toString()
                startCount++
                if (startCount == 26) {
                    binding.turnDecider.text = "Choose Number"
                    addUserTable()
                }
        }else if (userTurn % 2 != 0) {
            if (startCount >= 25 && !txt.paint.isStrikeThruText) {
                binding.turnDecider.text = "$player1Name Turn"
                first=0
                val number = txt.text.toString().toInt()
                binding.alreadyDone.text = coveredNo.toString()
                if (txt.paint.isStrikeThruText) {
                    Toast.makeText(
                        this,
                        "This number has already been choosen for reference check Announced List Below!!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (userTrackArr.contains(player2Number) && number != player2Number) {
                    Toast.makeText(
                        this,
                        "Kindly cross $player2Number from your table",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (number == player2Number) {
                    //Set something else instead of putting cross
                    var xUcorr = 0
                    var yUcorr = 0
                    for (i in 0..4) {
                        for (j in 0..4) {
                            if (userTable[i][j] == number) {
                                userTable[i][j] = 0
                                xUcorr =i
                                yUcorr = j
                            }

                        }
                    }
                    txt.paintFlags = txt.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    txt.setBackgroundResource(R.drawable.table_block_back_red)
                    coveredNo.add(player2Number)
                    userTrackArr.remove(player2Number)
                    checkScore(userTable, xUcorr, yUcorr)
                    print2DArray(userTable,1)


                } else {
                    txt.setBackgroundResource(R.drawable.table_block_back_red)
                    coveredNo.add(number)
                    //Remove number from computer and userTrack array
                    userTrackArr.remove(number)
                    binding.prevNo.text = binding.currNumber.text
                    binding.currNumber.text = number.toString()
                    var xUcorr = 0
                    var yUcorr = 0
                    for (i in 0..4) {
                        for (j in 0..4) {
                            if (userTable[i][j] == number) {
                                userTable[i][j] = 0
                                xUcorr = i
                                yUcorr = j
                            }

                        }
                    }
                    checkScore(userTable, xUcorr, yUcorr)
                    print2DArray(userTable,1)
                    //Set something else instead of putting cross
                    txt.paintFlags = txt.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    //----------Update in firebase
                    val updatedTurn = (userTurn + 1).toString()
                    val userNumber = number.toString()
                    val userScore = number.toString()
                    val info =
                        playerinfo(gameCode, player2Name, updatedTurn, userNumber,userScore,player2Score.toString())
                    database.child(gameCode).setValue(info).addOnSuccessListener {

                    }.addOnFailureListener {
                        Toast.makeText(
                            this,
                            "Kindly check your internet connection",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    //                     updateData(txt)
                }
            }
        } else {
            binding.currNumber.text = player2Number.toString()
            Toast.makeText(this, "Please wait for $player2Name  turn", Toast.LENGTH_SHORT)
                .show()
        }
    }

    /*

            Matching current score with the opponent score

    */

    private fun checkWinner() {
        if (usrscore >= 5 && player2Score < 5) {
            val info = playerinfo(gameCode,"","","",player2Score.toString(),usrscore.toString(),"1")
            database.child(gameCode).setValue(info).addOnSuccessListener {
//                Toast.makeText(this,"Data Sucessfully updated",Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(
                    this,
                    "Kindly check your internet connection",
                    Toast.LENGTH_SHORT
                ).show()
            }
            playWinnerMusic(this@JoinActivity)
            createDialogueUser(1)
        } else if (player2Score >= 5 && usrscore < 5) {
            val info = playerinfo(gameCode,"","","",player2Score.toString(),usrscore.toString(),"1")
            database.child(gameCode).setValue(info).addOnSuccessListener {
//                Toast.makeText(this,"Data Sucessfully updated",Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(
                    this,
                    "Kindly check your internet connection",
                    Toast.LENGTH_SHORT
                ).show()
            }
            playFailureMusic(this@JoinActivity)
            createDialogueUser(2)
        }
    }
    /*

            Function to create Aleart Dialogue

    */
    private fun createDialogueUser(winnerNo:Int) {

        val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialogue_online_layout, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .show()

        val ohk = mDialogView.findViewById<Button>(R.id.replay)
        val text:TextView = mDialogView.findViewById(R.id.winnerText)
        val playerScore:TextView = mDialogView.findViewById(R.id.uScore)
        val oppScore:TextView = mDialogView.findViewById(R.id.compScore)
        if(winnerNo==1){
            playWinnerMusic(this@JoinActivity)
            text.text = "Congrats You Won"
        }else{
            playFailureMusic(this@JoinActivity)
            text.text="Unfortunately You Loose"

        }
        playerScore.text = usrscore.toString()
        oppScore.text = player2Score.toString()
        ohk.setOnClickListener {
            mBuilder.dismiss()
//            val i = Intent(this, AskActivity::class.java)
//            startActivity(i)
            finish()
        }
    }
    /*

                Function to chekc and upadte function

     */
    private fun checkScore(table: Array<IntArray>,row:Int,clm:Int) {
//        print2DArray(table,1)
        //For horizontal Checking
        val tag = "SCORE"
        val checkHor:Boolean = checkCol(table,clm)
        if(checkHor){
            rowMemo.add(row)
            Log.d(tag,"succ in checkHor at row: $row,clm: $clm")
            usrscore++
        }
        //For vertical Checking
        val checkVert :Boolean = checkRow(table,row)
        if(checkVert){
            clmMemo.add(clm)
            Log.d(tag,"succ in checkVert at row: $row,clm: $clm")
            usrscore++
        }
        //For Diagonal Traversing Left to Right
        if(row==clm){
            val checkLtoRDiagonal:Boolean = checkLeftToRightDiagonal(table,row,clm)
            if(checkLtoRDiagonal){
                checklr.add(row)
                Log.d(tag,"succ in checkLtoR at row: $row,clm: $clm")
                usrscore++
                println("LtoR is: $usrscore")
            }
        }
        //For Diagonal Traversing Right to Left
        if((5-clm-1)==row){
            val checkRtoLDiagonal:Boolean = checkRightToLeftDiagonal(table,row,clm)
            if(checkRtoLDiagonal){
                checkrl.add(row)
                Log.d(tag,"succ in checkRtoL at row: $row,clm: $clm")
                usrscore++
                println("RtoL is: $usrscore")
            }
        }
        binding.score.text = usrscore.toString()
        val info = playerinfo(gameCode,player2Name,"$userTurn","$player2Number",player2Score.toString(),usrscore.toString(),)
        database.child(gameCode).setValue(info).addOnSuccessListener {
//                Toast.makeText(this,"Data Sucessfully updated",Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(
                this,
                "Kindly check your internet connection",
                Toast.LENGTH_SHORT
            ).show()
        }
        if(usrscore>=5){
            startUpdatingBackGround()
        }
    }
    fun checkRow(table: Array<IntArray>, row: Int): Boolean {
        val temp = mutableListOf<Int>()
        if(rowMemo.contains(row)){
            return false
        }
        for (col in table[row].indices) {
            temp.add(table[row][col])
            if (table[row][col] != 0) {
                return false
            }
        }
        printFunction(temp,"row")
        return true
    }
    private fun printFunction(temp: MutableList<Int>, s: String) {
        println("s : $temp")
    }
    fun checkCol(table: Array<IntArray>, col: Int): Boolean {
        val temp  = mutableListOf<Int>()
        if(clmMemo.contains(col)){
            return false
        }
        for (row in table.indices) {
            temp.add(table[row][col])
            if (table[row][col] != 0) {
                return false
            }
        }
        printFunction(temp,"col")
        return true
    }
    private fun checkLeftToRightDiagonal(table: Array<IntArray>,row:Int,clm:Int): Boolean {
        val temp = mutableListOf<Int>()
        if(checklr.contains(row)){
            return false
        }
        for (i in table.indices) {
            temp.add(table[i][i])
            if (table[i][i] != 0) {
                return false
            }
        }
        printFunction(temp,"Left to Right")
        return true
    }
    private fun checkRightToLeftDiagonal(table: Array<IntArray>,row:Int,clm:Int): Boolean {
        val temp = mutableListOf<Int>()
        val n = table.size
        if(checkrl.contains(row)){
            return false
        }
        for (i in table.indices) {
            temp.add(table[i][n-i-1])
            if (table[i][n - i - 1] != 0) {
                return false
            }
        }
        printFunction(temp,"Right to Left")
        return true
    }
    private fun print2DArray(table: Array<IntArray>,check:Int) {
        if(check==1){
            println("Printing user table")
        }else{
            println("Printing Computer table")
        }
        for(i in 0 until table.size){
            for( j in 0 until table[i].size){
                print(table[i][j])
                print(" ")
            }
            println()
        }
    }



    /*

            Creating table after used had made a table

    */

    private fun addUserTable() {
        val userArray = arrayListOf<String>(binding.txt1.text.toString(), binding.txt2.text.toString(),
            binding.txt3.text.toString(), binding.txt4.text.toString(), binding.txt5.text.toString(),
            binding.txt6.text.toString(), binding.txt7.text.toString(), binding.txt8.text.toString(),
            binding.txt9.text.toString(), binding.txt10.text.toString(), binding.txt11.text.toString(),
            binding.txt12.text.toString(), binding.txt13.text.toString(), binding.txt14.text.toString(),
            binding.txt15.text.toString(), binding.txt16.text.toString(), binding.txt17.text.toString(),
            binding.txt18.text.toString(), binding.txt19.text.toString(), binding.txt20.text.toString(),
            binding.txt21.text.toString(), binding.txt22.text.toString(), binding.txt23.text.toString(),
            binding.txt24.text.toString(), binding.txt25.text.toString())
        for(i in 0 until userArray.size){
            userTrackArr.add(userArray[i].toInt())
        }
        var k=0
        for(i in 0..4){
            for(j in 0..4){
                userTable[i][j]= userArray[k].toInt()
                k++;
            }
        }
        print2DArray(userTable,1)
    }

    /*

            Updating in background

    */
    private fun startUpdatingBackGround() {
        lifecycleScope.launch {
            while (isActive){
                database.child("$gameCode").get().addOnSuccessListener {
                    if (it.exists()){
                        player2Name = it.child("name").value.toString()
                        try{
                            userTurn = it.child("turn").value.toString().toInt()
                            player2Number = it.child("number").value.toString().toInt()
                            player2Score = it.child("creatorscore").value.toString().toInt()
                        }catch (e:Exception){
                            println("Message received is: ${e.message}")
                        }
                        checkWinningTrack = it.child("dialogueAppeared").value.toString()
//                        if(checkExit=="1"){
//                            val i =Intent(this@JoinActivity,MainActivity::class.java)
//                            startActivity(i)
//                            Toast.makeText(this@JoinActivity,"Player left the match",Toast.LENGTH_SHORT).show()
//                        }
                        if(userTurn%2==0 && first!=1){
                            binding.turnDecider.text = player2Name
                            binding.currNumber.text = player2Number.toString()
                        }else{
                            binding.currNumber.text = player2Number.toString()
                            binding.turnDecider.text = player1Name
                        }
                        if(checkWinningTrack=="1" && onlyOnce==1){
                            onlyOnce++
                            showFinalResult()
                        }

                        if((usrscore>=5 || player2Score>=5) && onlyFirst==1){
                            onlyFirst++
                            showFinalResult()
                        }

                    }else{
                        database.child(gameCode).removeValue()
                        println("Opponent Left the match")
                        finish()
                    }
                }
                delay(2000)
            }
        }
    }

    private fun showFinalResult() {
        if(usrscore>=5 && usrscore>player2Score){
            val info = playerinfo(gameCode,player2Name,"$userTurn","$player2Number",player2Score.toString(),usrscore.toString(),"1")
            database.child(gameCode).setValue(info).addOnSuccessListener {
//                Toast.makeText(this,"Data Sucessfully updated",Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(
                    this,
                    "Kindly check your internet connection",
                    Toast.LENGTH_SHORT
                ).show()
            }
            createDialogueUser(1)
        }else if(player2Score>=5 && player2Score>usrscore){
            val info = playerinfo(gameCode,player2Name,"$userTurn","$player2Number",player2Score.toString(),usrscore.toString(),"1")
            database.child(gameCode).setValue(info).addOnSuccessListener {
//                Toast.makeText(this,"Data Sucessfully updated",Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(
                    this,
                    "Kindly check your internet connection",
                    Toast.LENGTH_SHORT
                ).show()
            }
            createDialogueUser(2)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        startUpdatingBackGround()
        database.child(gameCode).removeValue()

    }

    override fun onStart() {
        super.onStart()
        startUpdatingBackGround()
    }
}