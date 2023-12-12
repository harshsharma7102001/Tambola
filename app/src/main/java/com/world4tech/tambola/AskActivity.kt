package com.world4tech.tambola

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.world4tech.tambola.database.playerinfo
import com.world4tech.tambola.databinding.ActivityAskBinding
import com.world4tech.tambola.util.audioState
import com.world4tech.tambola.util.playBtnClickMusic


class AskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAskBinding
    private lateinit var dbReference:DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dbReference = FirebaseDatabase.getInstance().getReference("game")
        binding.Computer.setOnClickListener {
            playBtnClickMusic(this@AskActivity)
            val i = Intent(this,MainActivity::class.java)
            startActivity(i)
        }
        binding.friends.setOnClickListener {
            playBtnClickMusic(this@AskActivity)
            binding.friends.visibility = View.GONE
            binding.options.visibility = View.VISIBLE
        }
        binding.createGame.setOnClickListener {
            playBtnClickMusic(this@AskActivity)
            createOnlineGame()
        }

        binding.join.setOnClickListener {
            playBtnClickMusic(this@AskActivity)
            joinOnlineGame() }
        binding.audio.setOnClickListener{
            if(audioState==0){
                audioState = 1
                binding.audio.setImageDrawable(resources.getDrawable(R.drawable.ic_audio_off))
            }else{
                audioState = 0
                binding.audio.setImageDrawable(resources.getDrawable(R.drawable.ic_audio))
            }
        }
    }

    private fun createOnlineGame() {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.create_game, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .show()
        val pName : TextView = mDialogView.findViewById(R.id.playerName)
        val gCode:TextView = mDialogView.findViewById(R.id.code)
        val createGame : Button = mDialogView.findViewById(R.id.createGame)
        createGame.setOnClickListener {
            if(pName.text.isNotEmpty() && gCode.text.isNotEmpty()){
                val name = pName.text.toString()
                val code = gCode.text.toString()
                val info = playerinfo(code,name,"2","0","0","0","0")
                dbReference.child(code).setValue(info).addOnSuccessListener {
                    val i =Intent(this,OnlineActivity::class.java)
                    i.putExtra("Name",name)
                    i.putExtra("code",code)
                    i.putExtra("create",1)
                    startActivity(i)
                }.addOnFailureListener {
//                    Toast.makeText(this,"Failed to create Class",Toast.LENGTH_SHORT).show()
                }
            }else{

            }
        }
    }

    private fun joinOnlineGame() {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.join_game, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .show()
        val pName : TextView = mDialogView.findViewById(R.id.playerName)
        val gCode:TextView = mDialogView.findViewById(R.id.code)
        val joinGame : Button = mDialogView.findViewById(R.id.joinGame)
        joinGame.setOnClickListener {
            binding.note.visibility = View.GONE
            if(pName.text.isNotEmpty() && gCode.text.isNotEmpty()){
                val name = pName.text.toString()
                val code = gCode.text.toString()
                dbReference.child(code).get().addOnSuccessListener {
                    if (it.exists()){
                        val i =Intent(this,JoinActivity::class.java)
                        i.putExtra("Name",name)
                        i.putExtra("code",code)
                        startActivity(i)

                    }else{
                        binding.note.visibility = View.VISIBLE
                    }
                }
            }
        }

    }



}