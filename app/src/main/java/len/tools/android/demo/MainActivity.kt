package len.tools.android.demo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import len.tools.android.Log

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.enableLog(true)
        Log.e("onCreate")
    }
}
