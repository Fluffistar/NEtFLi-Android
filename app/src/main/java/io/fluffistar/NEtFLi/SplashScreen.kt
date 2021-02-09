package io.fluffistar.NEtFLi

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.Result
import io.fluffistar.NEtFLi.Backend.Verwaltung
import io.fluffistar.NEtFLi.ui.LoginPage.LoginActivity
import org.json.JSONObject
import java.lang.Exception


class SplashScreen : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        Thread {
            Verwaltung.Setup(this)
        }.start()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.splashlayout)

        findViewById<TextView>(R.id.appVersion).text = "Version ${ packageManager.getPackageInfo(this.packageName, 0).versionName}";

        Thread(Runnable {

            Handler(Looper.getMainLooper()).postDelayed(Runnable {
                // Update UI here

                val sharedPref = this.getSharedPreferences(  "data",Context.MODE_PRIVATE)
                val email_txt = sharedPref.getString( "email", "")
                val password_txt = sharedPref.getString( "password", "")
                val url = Verwaltung.getKey( Verwaltung.main +  "/api/v1/account/login")
                Log.d("RESZULTz","password: $password_txt and email: $email_txt")
                val asynclogin = Fuel.post(
                    url,
                    listOf("email" to "$email_txt", "password" to "$password_txt")
                ).responseString { _, _, result ->
                    when(result){
                        is  Result.Failure -> {
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                            finish()

                        }
                        is Result.Success -> {
                            try {
                                Log.d("RESZULTD",result.get())
                                val session = JSONObject(result.get()).getString("session")


                                with(sharedPref!!.edit()) {
                                    putString("SessionID", session)

                                    apply()

                                }
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }catch (ex :Exception){
                                Log.d("ERRORD",ex.message.toString())
                                val intent = Intent(this, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }
                    }
                }


                    asynclogin.join()


            }, 100) //it will wait 10 sec before upate ui
        }).start()


    }
    val r = Runnable {



    }

}