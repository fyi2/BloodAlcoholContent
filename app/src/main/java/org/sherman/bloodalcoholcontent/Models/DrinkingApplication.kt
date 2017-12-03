package org.sherman.bloodalcoholcontent.Models

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

/**
 * Created by Admin on 11/29/2017.
 */
class DrinkingApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        val config = RealmConfiguration.Builder()
                .build()
        Realm.setDefaultConfiguration(config)
    }
}