package org.sherman.bloodalcoholcontent.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.view.*
import org.sherman.bloodalcoholcontent.Models.Profile
import org.sherman.bloodalcoholcontent.R


class ProfileActivity : AppCompatActivity() {

    var mMale: Int = 1
    var mWeight: Double = 66.0
    var mStartOfWeek: Int = 0
    var mBudget:Double = 14.0
    var mUUID:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val realm = Realm.getDefaultInstance()
        val query = realm.where(Profile::class.java).findFirst()

        // If exists
        if(query != null) {
            //Read defaults and set them on the screen
            getUserDefaults(realm)
        } else {
            // Else
            // Write defaults
            createUserDefaults(realm)
            // Set defaults on screen
            getUserDefaults(realm)
        }



    }

    // Set defaults to values from the database
    fun getUserDefaults(realm:Realm) {
        val values = realm.where(Profile::class.java).findFirst()
        // Set Weight
        bacWeightId.setText(values?.weight.toString())
        bacBudgetId.setText(values?.budget.toString())
        // Set Gender
        when(values?.male){
            0 -> genderRadioButton.isChecked = true
            1 -> genderRadioButton1.isChecked = true
            2 -> genderRadioButton2.isChecked = true
            else -> genderRadioButton3.isChecked = true
        }
        // Set Start of Week
        when(values?.startOfWeek){
            0 -> startWeekRadioButton.isChecked = true
            1 -> startWeekRadioButton1.isChecked = true
            5 -> startWeekRadioButton5.isChecked = true
            else -> startWeekRadioButton6.isChecked = true
        }
    }


    // CREATE an object with the user defaults in it. One occurrence of the object is all that we need
    fun createUserDefaults(realm: Realm) {
        val mRg: RadioGroup? = findViewById<RadioGroup>(R.id.genderRadioGrpId)
        val genderId = mRg!!.checkedRadioButtonId
        val mRGWeek: RadioGroup? = findViewById<RadioGroup>(R.id.startWeekRadioGrpId)
        val startOfWeek = mRGWeek!!.checkedRadioButtonId

        when(findViewById<RadioButton>(genderId).text){
            genderRadioButton.text -> mMale = 0
            genderRadioButton1.text -> mMale = 1
            genderRadioButton2.text -> mMale = 2
            else -> mMale = 3
        }
        when(findViewById<RadioButton>(startOfWeek).text){
            startWeekRadioButton5.text -> mStartOfWeek = 5
            startWeekRadioButton6.text -> mStartOfWeek = 6
            startWeekRadioButton.text -> mStartOfWeek = 0
            else -> mStartOfWeek = 1
        }
        mWeight = bacWeightId.text.toString().toDouble()
        mBudget = bacBudgetId.text.toString().toDouble()

        realm.executeTransaction {
            var profile = realm.where(Profile::class.java).findFirst()
            if (profile == null){
                profile = Profile()
            }
            profile!!.male = mMale
            profile.weight = mWeight
            profile.startOfWeek = mStartOfWeek
            profile.budget = mBudget
            realm.insertOrUpdate(profile)
        }
    }

    fun gotoMain(view: View) {

        // Read & Write Defaults
        val realm = Realm.getDefaultInstance()
        createUserDefaults(realm)

        // Go Home
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
