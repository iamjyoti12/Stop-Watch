package com.my.testing

import android.annotation.SuppressLint
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Chronometer
import android.widget.NumberPicker
import com.my.testing.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    var isRunning = false
    private var countdownTimer: CountDownTimer? = null
    private var selectedMinutes = 0

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val lapsList = ArrayList<String>()
        val arrayAdapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_list_item_1,
            lapsList
        )
        binding.listView.adapter = arrayAdapter

        binding.lap.setOnClickListener {
            if (isRunning) {
                lapsList.add(binding.chronometer.text.toString())
                arrayAdapter.notifyDataSetChanged()
            }
        }

        binding.clock.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.dilog)
            val numberPicker = dialog.findViewById<NumberPicker>(R.id.numberPicker)
            numberPicker.minValue = 0
            numberPicker.maxValue = 5
            dialog.findViewById<Button>(R.id.set_time).setOnClickListener {
                selectedMinutes = numberPicker.value
                binding.watchTime.text = "$selectedMinutes mins"
                dialog.dismiss()
            }
            dialog.show()
        }

        binding.run.setOnClickListener {
            if (!isRunning) {
                isRunning = true
                startTimer(selectedMinutes * 60 * 1000L)
                binding.run.text = "Stop"
            } else {
                isRunning = false
                countdownTimer?.cancel()
                binding.run.text = "Run"
            }
        }
    }

    private fun startTimer(millisInFuture: Long) {
        countdownTimer = object : CountDownTimer(millisInFuture, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                binding.chronometer.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                isRunning = false
                binding.run.text = "Run"
                binding.chronometer.text = "00:00"
            }
        }.start()
    }
}
