package org.sherman.bloodalcoholcontent.Models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

/**
 * Created by Admin on 11/30/2017.
 */
open class Profile: RealmObject() {
    @PrimaryKey
    private var id = UUID.randomUUID().toString()
    var male: Int = 0
    var weight: Double = 60.0
    var startOfWeek: Int = 0
    var budget:Double = 14.0

    fun getID(): String {
        return id
    }

    override  fun toString() : String {
        return "Start of Week: $startOfWeek"
    }

}