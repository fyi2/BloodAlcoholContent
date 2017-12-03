package org.sherman.bloodalcoholcontent.Activities

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_drink_alcohol.*
import org.sherman.bloodalcoholcontent.Data.DRINK_STRENGTH
import org.sherman.bloodalcoholcontent.Data.DRINK_TITLE
import org.sherman.bloodalcoholcontent.Models.Drink
import org.sherman.bloodalcoholcontent.R
import org.sherman.bloodalcoholcontent.R.id.rgSpirit
import java.util.*

class DrinkAlcoholActivity : AppCompatActivity() {
    var mSize: Double = 0.0
    var mRg: RadioGroup? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drink_alcohol)
        getSupportActionBar()?.setTitle(DRINK_STRENGTH);

        mSize = intent.extras.getDouble("size")

        // Disable unused columns

        when(mSize){
            1.5,3.0 -> { // We have Spirits
                rgBeer.visibility = View.INVISIBLE
                rgWine.visibility = View.INVISIBLE
                spiritRadioButton2.isChecked = true
                mRg = findViewById<RadioGroup>(R.id.rgSpirit)
            }
            5.0,8.0 -> { // We have Wine
                rgBeer.visibility = View.INVISIBLE
                rgSpirit.visibility = View.INVISIBLE
                wineRadioButton6.isChecked = true
                mRg = findViewById<RadioGroup>(R.id.rgWine)
            }
            else -> { // Else Beer
                rgSpirit.visibility = View.INVISIBLE
                rgWine.visibility = View.INVISIBLE
                beerRadioButton4.isChecked = true
                mRg = findViewById<RadioGroup>(R.id.rgBeer)
            }
        }
    }

    fun gotoMain(view: View){
        val radioButtonID = mRg?.checkedRadioButtonId
        val rb = findViewById<RadioButton>(radioButtonID!!)
        val checkedValue = rb.text.toString().replace("%","").toDouble()

        // Now we have the ABV and we know the size of the glass, so we work out the ASU
        var asu = mSize*checkedValue/60

        // Round to the nearest half drink
        asu = (((asu*2).toInt()).toDouble()/2)
        logDrink(asu)

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("asu", asu)

        startActivityForResult(intent, Activity.RESULT_OK)
    }

    fun logDrink(asu:Double){
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            val drink = Drink()
            drink.time = Calendar.getInstance().timeInMillis
            drink.asu = asu
            realm.copyToRealm(drink)
        }
    }
}
