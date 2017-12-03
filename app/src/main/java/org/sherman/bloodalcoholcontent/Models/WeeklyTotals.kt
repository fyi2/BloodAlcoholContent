package org.sherman.bloodalcoholcontent.Models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*


open class WeeklyTotals: RealmObject() {
    @PrimaryKey
    private var id = UUID.randomUUID().toString()
    var weekNumber: Int = 0
    var totalASU:Double = 0.0

    fun getID(): String {
        return id
    }

    override  fun toString() : String {
        return "Week Number: $weekNumber"
    }

}

