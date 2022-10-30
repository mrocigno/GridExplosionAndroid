package br.com.mrocigno.gridexplosion

import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(window) {
            WindowCompat.setDecorFitsSystemWindows(this, false)
        }

        findViewById<ExplosionLayout>(R.id.explosion).apply {
            setOnTouchListener { _, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        startAnimation(event)
                    }
                    MotionEvent.ACTION_MOVE -> {
                        startAnimation(event)
                    }
                    MotionEvent.ACTION_UP -> {
                        performClick()
                    }
                }
                true
            }
        }
    }
}