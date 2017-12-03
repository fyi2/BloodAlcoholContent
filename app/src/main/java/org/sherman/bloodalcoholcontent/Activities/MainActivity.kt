package org.sherman.bloodalcoholcontent.Activities

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*
import org.sherman.bloodalcoholcontent.Data.*
import org.sherman.bloodalcoholcontent.Models.Drink
import org.sherman.bloodalcoholcontent.Models.Profile
import org.sherman.bloodalcoholcontent.Models.Status
import org.sherman.bloodalcoholcontent.R
import org.sherman.bloodalcoholcontent.R.id.time
import java.time.*
import java.time.temporal.TemporalAdjuster
import java.time.temporal.TemporalAdjusters
import java.util.*

class MainActivity : AppCompatActivity() {
    private var mDrawerList: ListView? = null
    private var mAdapter: ArrayAdapter<String>? = null
    private var mDrawerToggle: ActionBarDrawerToggle? = null
    private var mDrawerLayout: DrawerLayout? = null
    private var mActivityTitle: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // TODO Update the screen with accurate figures

        var inflater: LayoutInflater = layoutInflater
        var listViewHeader: View = inflater.inflate(R.layout.draw_header, null, false)

        mDrawerLayout = findViewById(R.id.drawer_layout)
        mActivityTitle = getTitle().toString();
        mDrawerList = findViewById(R.id.navList)
        mDrawerList?.addHeaderView(listViewHeader)
        addDrawItems()
        setupDrawer();

        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setHomeButtonEnabled(true)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null){
            setIntent(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        //intent = intent
        calcBAC()
        if (intent!!.extras != null){
            val asu:Double  = intent!!.extras.get("asu").toString().toDouble()
            val realm = Realm.getDefaultInstance()
            val profile = realm.where(Profile::class.java).findFirst()
            val startDay = profile!!.startOfWeek
            val budget = profile!!.budget
            val dailyBudget = round(budget/7)
            // Read current Status Record
            var status = realm.where(Status::class.java).findFirst()
            if(status == null){
                realm.executeTransaction {
                    var firstStatus = Status()
                    firstStatus!!.weeklyBudget = budget
                    firstStatus!!.dailyBudget = dailyBudget
                    firstStatus!!.totalDrinks = 0.0
                    firstStatus!!.dateStamp = Calendar.getInstance().timeInMillis
                    realm.insertOrUpdate(firstStatus)
                }
                return
            }
            var dateTime:LocalDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(status!!.dateStamp), ZoneId.systemDefault())
            var rolloverDay = dateTime
            // Read start Day

            // Find Next Start Day
            when (startDay){
                5 -> rolloverDay = dateTime.with(TemporalAdjusters.next(DayOfWeek.FRIDAY))
                6 -> rolloverDay = dateTime.with(TemporalAdjusters.next(DayOfWeek.SATURDAY))
                0 -> rolloverDay = dateTime.with(TemporalAdjusters.next(DayOfWeek.SUNDAY))
                else -> rolloverDay = dateTime.with(TemporalAdjusters.next(DayOfWeek.MONDAY))
            }
            // If rolloverDay < Now then clean slate Status
            val rolloverDayMS:Long = rolloverDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
            if (rolloverDayMS < Calendar.getInstance().timeInMillis){
                realm.executeTransaction {
                    status.weeklyBudget = budget
                    bacWeeklyBudgetTextView8.setText(status.weeklyBudget.toString())
                    status.dailyBudget = dailyBudget
                    bacDailyBudgetTextView6Id.setText(status.dailyBudget.toString())
                    status.totalDrinks = 0.0
                    bacDailyDrinksTextView4Id.setText(status.totalDrinks.toString())
                    status.dateStamp = Calendar.getInstance().timeInMillis
                    realm.insertOrUpdate(status)
                }
            } else {
                // Else Update Status
                realm.executeTransaction {
                    status.dateStamp = Calendar.getInstance().timeInMillis
                    bacDailyDrinksTextView4Id.setText(status.totalDrinks.toString())
                    status.totalDrinks = round(bacDailyDrinksTextView4Id.text.toString().toDouble()+asu)
                    bacDailyBudgetTextView6Id.setText(status.dailyBudget.toString())
                    status.dailyBudget = round(bacDailyBudgetTextView6Id.text.toString().toDouble()-asu)
                    bacWeeklyBudgetTextView8.setText(status.weeklyBudget.toString())
                    status.weeklyBudget = round(bacWeeklyBudgetTextView8.text.toString().toDouble()-asu)
                    realm.insertOrUpdate(status)
                }
            }
            //Toast.makeText(this,"update Status with $asu", Toast.LENGTH_SHORT).show()
        } else {
            //Load up the Status Record
            // TODO: This needs to have the rollover logic at some point
            // TODO Change logic from screen based calcs to database
            var realm = Realm.getDefaultInstance()
            realm.executeTransaction {
                val status = realm.where(Status::class.java).findFirst()
                if(status != null) {
                    bacDailyDrinksTextView4Id.setText(status.totalDrinks.toString())
                    bacDailyBudgetTextView6Id.setText(status.dailyBudget.toString())
                    bacWeeklyBudgetTextView8.setText(status.weeklyBudget.toString())
                }
            }
        }

    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        mDrawerToggle?.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        mDrawerToggle?.onConfigurationChanged(newConfig)
    }

    fun addDrawItems() {
        val actionArray = mutableListOf<String>(DRAW.FIRST.title, DRAW.SECOND.title, DRAW.THIRD.title)
        mAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, actionArray )
        mDrawerList!!.adapter = mAdapter

        mDrawerList!!.setOnItemClickListener(AdapterView.OnItemClickListener { parent: AdapterView<*>, view:View, position:Int, id:Long ->
            when (position) {
                1 -> loadProfile()
                2 -> loadProfile()
                3 -> loadProfile()
                else -> Toast.makeText(this, "Illegal Click", Toast.LENGTH_LONG).show()
            }
        })
    }

    fun setupDrawer() {
        mDrawerToggle = object : ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state.  */
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar()?.setTitle(DRAW.TITLE.title);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state.  */
            override fun onDrawerClosed(view: View) {
                super.onDrawerClosed(view);
                getSupportActionBar()?.setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        }
        mDrawerToggle!!.setDrawerIndicatorEnabled(true)
        mDrawerLayout?.addDrawerListener(mDrawerToggle!!)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        // Activate the navigation drawer toggle
        if (mDrawerToggle!!.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item)
    }

    fun loadProfile() {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }

    fun gotoDrink(view: View){
        val intent = Intent(this, DrinkActivity::class.java)
        startActivity(intent)
    }

    fun updateDailyDrinks(asu:Double){
        var dailyDrinks = 0.0
        val dailyDrinksString = bacDailyDrinksTextView4Id.text.toString()
        Toast.makeText(this,"update daily drinks with same drink as last time - $dailyDrinksString", Toast.LENGTH_SHORT).show()
        if (dailyDrinksString != ""){
            dailyDrinks = dailyDrinksString.toDouble()
        }
        dailyDrinks = round(dailyDrinks+asu)
        bacDailyDrinksTextView4Id.setText(dailyDrinks.toString())

    }


    fun round(number:Double):Double{ // Rounds to the nearest 0.5 drinks
        return (number*2).toInt().toDouble()/2.0
    }

    fun plusOne(view: View){
        // Adds another drink to the ledger
        // Set up the intent first
        val intent = Intent(this,MainActivity::class.java)
        // Get the last asu
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction {
            val drink = realm.where(Drink::class.java).findAllSorted("time").last()
            if (drink != null){
                intent.putExtra("asu", drink.asu)
            } else {
                intent.putExtra("asu", 1.0)
            }
        }
        onResume()
    }
    fun calcBAC() {
        var cal: Long = Calendar.getInstance().timeInMillis
        val yesterday:Long = cal - DAYINMILLI
        var alcoholConsumption = 0.0
        val realm = Realm.getDefaultInstance()
        val query = realm.where(Profile::class.java).findFirst()
        var GENDER_CONST = 0.68
        if (query!!.male != 0){
            GENDER_CONST = 0.55
        }
        val query2 = realm.where(Drink::class.java).greaterThan("time", yesterday).findAll()
        for (q in query2){
            alcoholConsumption += q.asu
        }
        alcoholConsumption = alcoholConsumption* GRAMS_OF_ALCOHOL
        val adjustedWeight = query.weight* GRAMS_PER_POUND*GENDER_CONST
        var bac = (((alcoholConsumption/adjustedWeight)*100) -(ELAPSED_TIME* ABSORPTION_RATE))
        bac = (bac*1000).toInt().toDouble()/1000  // Round to 3 decimal places

        // Update XML
        bacTextView2Id.setText(bac.toString())

    }
}

