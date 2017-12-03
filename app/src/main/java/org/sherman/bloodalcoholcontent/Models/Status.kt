package org.sherman.bloodalcoholcontent.Models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class Status: RealmObject() {
    @PrimaryKey
    private var id = UUID.randomUUID().toString()
    var totalDrinks: Double = 0.0
    var dailyBudget: Double = 2.0
    var weeklyBudget: Double = 13.0
    var bac:Double = 0.0
    var dateStamp:Long = 0

    fun getID(): String {
        return id
    }

    override  fun toString() : String {
        return "BAC = $bac"
    }

}
