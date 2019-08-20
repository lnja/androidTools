package len.tools.android.demo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.orhanobut.logger.*
import len.tools.android.JsonUtils
import len.tools.android.Log
import len.tools.android.StorageUtils
import len.tools.android.extend.LnjaCsvFormatStrategy

class MainActivity : AppCompatActivity() {

    val rawJson:String = "{\"value\":\"{\\\"country\\\":\\\"中国\\\",\\\"country_id\\\":\\\"CN\\\",\\\"area\\\":\\\"华东\\\",\\\"area_id\\\":\\\"300000\\\",\\\"region\\\":\\\"上海市\\\",\\\"region_id\\\":\\\"310000\\\",\\\"city\\\":\\\"上海市\\\",\\\"city_id\\\":\\\"310100\\\",\\\"county\\\":\\\"\\\",\\\"county_id\\\":\\\"-1\\\",\\\"isp\\\":\\\"腾讯网络\\\",\\\"isp_id\\\":\\\"1000153\\\",\\\"ip\\\":\\\"115.159.152.210\\\"}\"}"
    val rawJson2:String = "{\"country\":\"中国\",\"country_id\":\"CN\",\"area\":\"华东\",\"area_id\":\"300000\",\"region\":\"上海市\",\"region_id\":\"310000\",\"city\":\"上海市\",\"city_id\":\"310100\",\"county\":\"\",\"county_id\":\"-1\",\"isp\":\"腾讯网络\",\"isp_id\":\"1000153\",\"ip\":\"115.159.152.210\"}"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        logDemo()
        loggerDemo()
    }

    fun logDemo(){
        Log.init("AndroidTools",android.util.Log.VERBOSE)
        Log.enableLog(true)
        Log.e("onCreate")
        Log.d("onCreate")
//        Log.d("onCreate",Throwable())
    }

    fun loggerDemo(){
        Log.e("loggerDemo{")
        val formatStrategy = PrettyFormatStrategy.newBuilder()
            .tag("AndroidTools")
            .build()
        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })
        val csvFormatStrategy = LnjaCsvFormatStrategy.newBuilder()
            .tag("AndroidTools").logPath(StorageUtils.getExtendDir(this,"logs").absolutePath)
            .build()
        Logger.addLogAdapter(object : DiskLogAdapter(csvFormatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })
        Logger.d("logger demo......")

        Log.e("}loggerDemo")

        Logger.e(JsonUtils.toJsonViewStr(rawJson))

        Logger.e(JsonUtils.toJsonViewStr(rawJson2))
    }
}
