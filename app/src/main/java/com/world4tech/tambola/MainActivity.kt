package com.world4tech.tambola

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.world4tech.tambola.databinding.ActivityMainBinding
import com.world4tech.tambola.util.playBtnClickMusic
import com.world4tech.tambola.util.playFailureMusic
import com.world4tech.tambola.util.playWinnerMusic

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    var startCount = 1;
    var allComplete =false
    val userTable = Array(5) { IntArray(5) }
    val tambolaTable = Array(5) { IntArray(5) }
    //Current score of players
    var usrscore = 0;
    var computerScore=0

    var userTurn = 1
     var coveredNo:MutableList<Int> =  mutableListOf()
    //Array for tracking computer and user
    val compArr = arrayListOf<Int>()
    val userTrackArr= arrayListOf<Int>()

    var computerOption:Int = 0;
    var userOption:Int = 0

    //For tracking row
    var rowMemo = mutableListOf<Int>()
    //For tracking clm
    val clmMemo = mutableListOf<Int>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        var arrTemp = arrayListOf<Int>(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25)

        //2d array for computer

        for(i in arrTemp.indices){
            val no = arrTemp.random()
            arrTemp.remove(no)
            compArr.add(no)
        }
        var k=0
        for(i in 0..tambolaTable.size-1){
            val colArray = IntArray(5)
            for(j in 0..colArray.size-1){
                colArray[j] = compArr[k]
                k++
            }
            tambolaTable[i] = colArray
        }
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

    private fun updateData(txt: TextView) {
        playBtnClickMusic(this@MainActivity)
//        checkWinner()
        if(userTurn==1){
            if(txt.text=="X"){
                binding.currNumber.text = ""
                txt.text = startCount.toString()
                startCount++
                if(startCount==26){
                    binding.turnDecider.text = "Choose Number"
                    addUserTable()
                }

            }else if(startCount>=25 && !txt.paint.isStrikeThruText){
                binding.turnDecider.text = "Your Turn"
                val number = txt.text.toString().toInt()
                binding.alreadyDone.text = coveredNo.toString()
                if(txt.paint.isStrikeThruText) {
                    Toast.makeText(
                        this,
                        "This number has already been choosen for reference check Announced List Below!!",
                        Toast.LENGTH_SHORT
                    ).show()
                }else if(userTrackArr.contains(computerOption) && number!=computerOption){
                     Toast.makeText(this,"Kindly cross $computerOption from your table", Toast.LENGTH_SHORT).show()
                 }else if(number==computerOption){
                     //Set something else instead of putting cross
                     txt.paintFlags = txt.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                     txt.setBackgroundResource(R.drawable.table_block_back_red)

                     //REmoving number from computer and usertrack array
                     compArr.remove(number)
                     userTrackArr.remove(number)
                     for(i in 0..4){
                         for(j in 0..4){
                             if(userTable[i][j] == number){
                                 userTable[i][j] = 0
                             }
                             checkScore(userTable,i,j)
                         }
                     }
                 }else{
                     txt.setBackgroundResource(R.drawable.table_block_back_red)
                     coveredNo.add(number)
                     //Remove number from computer and userTrack array
                     compArr.remove(number)
                     userTrackArr.remove(number)

                     binding.prevNo.text = binding.currNumber.text
                     binding.currNumber.text = number.toString()
                    var xUcorr =0
                    var yUcorr =0
                     for(i in 0..4){
                         for(j in 0..4){
                             if(userTable[i][j] == number){
                                 userTable[i][j] = 0
                                 xUcorr = i
                                 yUcorr = j
                             }

                         }
                     }
                    checkScore(userTable,xUcorr,yUcorr)
                    var xcorr:Int = 0
                    var ycorr:Int = 0
                    for(i in 0..4){
                        for(j in 0..4){
                            if(tambolaTable[i][j]==number){
                                tambolaTable[i][j] = 0
                                xcorr = i
                                ycorr = j
                            }
                        }
                    }
                    checkCompScore(tambolaTable,xcorr,ycorr)

//                     if(number%3==0){
//                         checkScore(userTable)
//                     }
                     //Set something else instead of putting cross
                     txt.paintFlags = txt.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                     userTurn = 0
//                     updateData(txt)
                 }
            }
            if(userTurn==0){
//                binding.turnDecider.text = "Computer Turn"
//                binding.alreadyDone.text = ""
                performComputerTurn()
            }
        }
    }

    private fun checkWinner() {
        if(usrscore>=5 && computerScore<usrscore){
//            userTurn = 1000
            playWinnerMusic(this@MainActivity)
            createDialogueUser(1)
        }else if(computerScore>=5 &&  usrscore<5){
//            userTurn=1000
//            val i =Intent(this,WinningActivity::class.java)
//            startActivity(i)
//            finish()
            playFailureMusic(this@MainActivity)
            createDialogueUser(2)
        }
    }

    private fun createDialogueUser(no:Int) {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialogue_layout, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .show()

        val replay = mDialogView.findViewById<Button>(R.id.replay)
        val img:ImageView = mDialogView.findViewById(R.id.winnerImg)
        val txtView:TextView = mDialogView.findViewById(R.id.winnerText)
        val canBtn = mDialogView.findViewById<Button>(R.id.cancel)
        val uScore:TextView = mDialogView.findViewById(R.id.uScore)
        val cScore:TextView = mDialogView.findViewById(R.id.compScore)
        val uTable:TextView = mDialogView.findViewById(R.id.userTable)
        val cTable:TextView = mDialogView.findViewById(R.id.compTable)
        if(no==2){
            img.setImageResource(R.drawable.sab_boy)
            txtView.text = "Unfortunately you loose"
        }
        uScore.text = usrscore.toString()
        cScore.text = computerScore.toString()
        uTable.text = userTrackArr.toString()
        cTable.text = compArr.toString()
        canBtn.setOnClickListener {
            mBuilder.dismiss()
            val i = Intent(this,AskActivity::class.java)
            startActivity(i)
            finish()
        }
        replay.setOnClickListener {
            this.recreate()
        }
    }

    private fun performComputerTurn(){
        binding.turnDecider.text = "Computer Turn"
        binding.currNumber.text = ""
        Handler(Looper.getMainLooper()).postDelayed({
            val i = (0..4).random()
            val j = (0..4).random()
            if(compArr.isNotEmpty() && tambolaTable[i][j] !=0 && compArr.contains(tambolaTable[i][j])){
                binding.turnDecider.text ="Computer Turn"
                binding.alreadyDone.text = coveredNo.toString()
                computerOption = tambolaTable[i][j]
                binding.currNumber.text = tambolaTable[i][j].toString()
                coveredNo.add(tambolaTable[i][j])
                compArr.remove(tambolaTable[i][j])
                tambolaTable[i][j] = 0
                if(tambolaTable[i][j]%3==0) {
                    checkCompScore(tambolaTable,i,j)
                }
                userTurn = 1
            }else{
                performComputerTurn()
            }
        },700)
    }

    //--------------------------For calculation score section-----------------
    private fun checkScore(table: Array<IntArray>,row:Int,clm:Int) {
//        print2DArray(table,1)
        //For horizontal Checking
        val checkHor:Boolean = checkScoreHorizontal(table,row,clm)
        if(checkHor){
            rowMemo.add(row)
            usrscore++
        }
        //For vertical Checking
        val checkVert :Boolean = checkScoreVertical(table,row,clm)
        if(checkVert){
            clmMemo.add(clm)
            usrscore++
        }

        //For Diagonal Traversing Left to Right
        if(row==clm){
            val checkLtoRDiagonal:Boolean = checkScoreDiagonalLtoR(table)
            if(checkLtoRDiagonal){
                usrscore++
            }
        }

        //For Diagonal Traversing Right to Left
        if((5-row-1)==clm){

            val checkRtoLDiagonal:Boolean = checkScoreDiagonal(table)
            if(checkRtoLDiagonal){
                usrscore++
            }
        }
        binding.score.text = usrscore.toString()
        if(usrscore>=5){
            checkWinner()
        }
    }
    private fun checkCompScore(table: Array<IntArray>,row:Int,clm:Int) {
//        print2DArray(table,0)
        //For horizontal Checking
        val checkHor:Boolean = checkScoreHorizontal(table,row,clm)
        if(checkHor){
            computerScore++
        }
        //For vertical Checking
        val checkVert:Boolean = checkScoreVertical(table,row,clm)
        if(checkVert){
            computerScore++
        }

        //For Diagonal Traversing Left to Right
        if(row==clm){
            val checkLtoRDia:Boolean = checkScoreDiagonalLtoR(table)
            if(checkLtoRDia){
                computerScore++
            }
        }

        //For Diagonal Traversing Right to Left
        if(row==clm){

            val checkRtoLDia:Boolean = checkScoreDiagonal(table)
            if(checkRtoLDia){
                computerScore++
            }
        }
        if(computerScore>=5){
            checkWinner()
        }

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
    private fun checkScoreDiagonalLtoR(table: Array<IntArray>):Boolean {
        val list = mutableListOf<Int>()
        for (i in 0 until table.size){
            for(j in 0 until table[i].size){
                if(i==j){
                    list.add(table[i][j])
                    if(table[i][j]!=0){
                        return false
                    }
                }
            }
        }
        print("Values in L to R is: $list")
        return true
    }
    private fun checkScoreDiagonal(table: Array<IntArray>):Boolean {
        val list = mutableListOf<Int>()
        for(i in 0 until table.size){
            for (j in 0..table[i].size-1){
                if(j==Math.abs(table[i].size - i-1)){
                    list.add(table[i][j])
                    if(table[i][i]!=0){
                        return false
                    }
                }
            }

        }
        print("Values in R to L is: $list")
        return true
    }
    private fun checkScoreVertical(table: Array<IntArray>,row:Int,clm:Int) :Boolean{
        val list = mutableListOf<Int>()
        if(clmMemo.contains(clm)){
            return false
        }
        for(i in 0..4){
            list.add(table[i][clm])
            if(table[i][clm]!=0){
                return false
            }
        }
        print("Values in row= $row and clm =$clm is: $list")
        return true
    }
    private fun checkScoreHorizontal(table: Array<IntArray>,row:Int,clm:Int):Boolean {
        val list = mutableListOf<Int>()
        if(rowMemo.contains(row)){
            return false
        }
        for(j in 0..table[row].size-1){
            list.add(table[row][j])
            if(table[row][j]!=0){
                return false
            }
        }
        print("Values in row= $row and clm =$clm is: $list")
        return true
    }
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
    }
}