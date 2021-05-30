package com.ctrlbytes.statelayoutdemo

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ctrlbytes.statelayout.State
import com.ctrlbytes.statelayoutdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.stateLayout.onButtonClick {
            Toast.makeText(this, "Button Clicked", Toast.LENGTH_SHORT).show()
        }

        binding.stateLayout.setOnStateChangeListener { state ->
            when (state) {
                State.CONTENT -> Toast.makeText(this, "CONTENT", Toast.LENGTH_SHORT).show()
                State.EMPTY -> Toast.makeText(this, "EMPTY", Toast.LENGTH_SHORT).show()
                State.LOADING -> Toast.makeText(this, "LOADING", Toast.LENGTH_SHORT).show()
                State.ERROR -> Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_content -> binding.stateLayout.showContent()
            R.id.action_empty -> binding.stateLayout.showEmpty("Inbox is Empty")
            R.id.action_error -> binding.stateLayout.showError("Server Down at this moment")
            R.id.action_loading -> binding.stateLayout.showLoading()
        }
        return true
    }
}