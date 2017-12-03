package org.sherman.bloodalcoholcontent.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import org.sherman.bloodalcoholcontent.Data.DRINK_TITLE
import org.sherman.bloodalcoholcontent.R

class DrinkActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drink)
        getSupportActionBar()?.setTitle(DRINK_TITLE);
    }

    fun gotoMain(view: View){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun getDrinkSize(view: View){
        val size: Double = view.getTag().toString().toDouble()
        Toast.makeText(this, "The Drink is $size", Toast.LENGTH_LONG).show()

        var intent = Intent(this,DrinkAlcoholActivity::class.java )
        intent.putExtra("size", size)
        startActivity(intent)
    }
}
