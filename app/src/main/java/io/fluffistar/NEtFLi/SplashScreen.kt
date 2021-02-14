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
import java.io.IOException
import java.lang.Exception


class SplashScreen : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.splashlayout)

        findViewById<TextView>(R.id.appVersion).text = "Version ${ packageManager.getPackageInfo(this.packageName, 0).versionName}";

        Thread(Runnable {

            Handler(Looper.getMainLooper()).postDelayed(Runnable {
                // Update UI here
                Thread {
                    Verwaltung.Setup(this)
                }.start()
                while (!Verwaltung.laoded){}
                val sharedPref = this.getSharedPreferences(  "data",Context.MODE_PRIVATE)
                val email_txt = sharedPref.getString( "email", "")
                val password_txt = sharedPref.getString( "password", "")
                val url =  ( Verwaltung.main +  "/login")
                Log.d("RESZULTz","password: $password_txt and email: $email_txt")
                val asynclogin = Fuel.post(
                    url,
                    listOf("email" to "$email_txt", "password" to "$password_txt")
                ).responseString { request, response, result  ->
                    when(result){
                        is  Result.Failure -> {
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                            finish()

                        }
                        is Result.Success -> {
                            try {
                                Log.d("RESZULTD",result.get())
                                if(result.get() != "")
                                    throw Exception()
                                val session = response.headers.get("Set-Cookie").asIterable().toList()[1].split(";")[0]

                                Log.d("Login_Html" , result.get())

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