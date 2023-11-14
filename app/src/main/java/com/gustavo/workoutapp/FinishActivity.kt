package com.gustavo.workoutapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.gustavo.workoutapp.databinding.ActivityFinishBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FinishActivity : AppCompatActivity() {


    private var binding:ActivityFinishBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinishBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarFinishActivity)
        if(supportActionBar!=null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding?.toolbarFinishActivity?.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding?.btnFinish?.setOnClickListener {
            finish()
        }

        val historyDao = (application as WorkOutApp).db.historydao()
        addDateToDatabase(historyDao)


    }

    private fun addDateToDatabase(historyDao:HistoryDao){

        val c = Calendar.getInstance()
        val dateTime = c.time
        val sdf = SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault())
        val date =sdf.format(dateTime)

        lifecycleScope.launch{
            historyDao.insert(HistoryEntity(date))
        }


    }

}