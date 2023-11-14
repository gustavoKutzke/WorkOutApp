package com.gustavo.workoutapp

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.MediaStore.Audio.Media
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.gustavo.workoutapp.databinding.ActivityExerciseBinding
import com.gustavo.workoutapp.databinding.DialogCustomBackConfirmationBinding
import java.lang.Exception
import java.util.Locale

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private var binding:ActivityExerciseBinding? =null
    private var restTimer:CountDownTimer?=null
    private var restProgress = 0

    private var restTimerExercise:CountDownTimer?=null
    private var restProgressExercise = 0

    private var exerciseList :ArrayList<ExerciseModel>?=null
    private var currentExercisePosition = 0

    private var tts : TextToSpeech? =null

    private var player:MediaPlayer? =null

    private var exerciseAdapter :ExerciseStatusAdapter? =null

    private var restTimerDuration:Long = 1
    private var exerciseTimerDuration:Long = 1



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        setSupportActionBar(binding?.toolbarExercise)

        tts = TextToSpeech(this,this)


        if(supportActionBar !=null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        exerciseList = Constants.defaultExerciseList()

        binding?.toolbarExercise?.setNavigationOnClickListener {
            customDialogBackButton()
        }

        setupRestView()
        setupExerciseStatusReyclerView()
    }

    override fun onBackPressed() {
        customDialogBackButton()
    }

    private fun customDialogBackButton(){
        val customDialog = Dialog(this)
        val dialogBinding =DialogCustomBackConfirmationBinding.inflate(layoutInflater)
        customDialog.setContentView(dialogBinding.root)
        customDialog.setCanceledOnTouchOutside(false)
        dialogBinding.btnYes.setOnClickListener {
            this@ExerciseActivity.finish()
            customDialog.dismiss()
        }
        dialogBinding.btnNo.setOnClickListener {
            customDialog.dismiss()
        }
        customDialog.show()



    }

    private fun setupExerciseStatusReyclerView(){
        binding?.rvExerciseStatus?.layoutManager =LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!)
        binding?.rvExerciseStatus?.adapter = exerciseAdapter


    }


    private fun setupRestView(){

        try {
            val soundURI = Uri.parse("android.resource://com.gustavo.workoutapp/" + R.raw.press_start)
            player = MediaPlayer.create(applicationContext,soundURI)
            player?.isLooping = false
            player?.start()

        }catch (e:Exception){
            e.printStackTrace()
        }

        binding?.flRestView?.visibility = View.VISIBLE
        binding?.txtTitle?.visibility = View.VISIBLE
        binding?.txtExercise?.visibility = View.INVISIBLE
        binding?.flExerciseProgressbar?.visibility = View.INVISIBLE
        binding?.imgExercise?.visibility = View.INVISIBLE
        binding?.txtUpcoming?.visibility = View.VISIBLE
        binding?.txtUpcomingExerciseName?.visibility = View.VISIBLE


        if(restTimer !=null){
            restTimer?.cancel()
            restProgress = 0
        }
        binding?.txtUpcomingExerciseName?.text = exerciseList!![currentExercisePosition + 1].getName()
        setRestProgressBar()
    }

    private fun setupExerciseView(){
        binding?.flRestView?.visibility = View.INVISIBLE
        binding?.txtTitle?.visibility = View.INVISIBLE
        binding?.txtExercise?.visibility = View.VISIBLE
        binding?.flExerciseProgressbar?.visibility = View.VISIBLE
        binding?.imgExercise?.visibility = View.VISIBLE
        binding?.txtUpcoming?.visibility = View.INVISIBLE
        binding?.txtUpcomingExerciseName?.visibility = View.INVISIBLE

        if(restTimerExercise !=null){
            restTimerExercise?.cancel()
            restProgressExercise = 0
        }

        speakOut(exerciseList!![currentExercisePosition].getName())

        binding?.imgExercise?.setImageResource(exerciseList!![currentExercisePosition].getImage())
        binding?.txtExercise?.text = exerciseList!![currentExercisePosition].getName()

        setExerciseProgressBar()
    }

    private fun setRestProgressBar(){
        binding?.progressBar?.progress = restProgress

        restTimer = object :CountDownTimer(restTimerDuration*1000,1000){
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                binding?.progressBar?.progress = 10 - restProgress
                binding?.txtTimer?.text = (10- restProgress).toString()
            }

            override fun onFinish() {
                currentExercisePosition++

                exerciseList!![currentExercisePosition].setIsSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()


                setupExerciseView()

            }
        }.start()
    }

    private fun setExerciseProgressBar(){
        binding?.progressBarExercise?.progress = restProgressExercise

        restTimerExercise = object :CountDownTimer(exerciseTimerDuration *3000,1000){
            override fun onTick(millisUntilFinished: Long) {
                restProgressExercise++
                binding?.progressBarExercise?.progress = 30 - restProgressExercise
                binding?.txtTimerExercise?.text = (30- restProgressExercise).toString()
            }

            override fun onFinish() {

                if(currentExercisePosition<exerciseList?.size!! - 1){
                    exerciseList!![currentExercisePosition].setIsSelected(false)
                    exerciseList!![currentExercisePosition].setIsCompleted(true)
                    exerciseAdapter!!.notifyDataSetChanged()
                    setupRestView()
                }else{
                    finish()
                    val intent = Intent(this@ExerciseActivity,FinishActivity::class.java)
                    startActivity(intent)

                }
            }
        }.start()
    }


    override fun onDestroy() {
        super.onDestroy()
        if(restTimer !=null){
            restTimer?.cancel()
            restProgress = 0
        }
        if(restTimerExercise !=null){
            restTimerExercise?.cancel()
            restProgressExercise = 0
        }

        if(tts !=null){
            tts!!.stop()
            tts!!.shutdown()
        }

        if(player !=null){
            player!!.stop()
        }

        binding = null
    }

    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS){
            val result = tts?.setLanguage(Locale.US)

            if(result == TextToSpeech.LANG_MISSING_DATA || result ==TextToSpeech.LANG_NOT_SUPPORTED){
             Log.e("TTS","A linguagem não é suportada")
            }
        }else{
            Log.e("TTS","Initizalization Failed")
        }
    }

    private fun speakOut(text:String){
        tts!!.speak(text,TextToSpeech.QUEUE_FLUSH,null,"")
    }


}