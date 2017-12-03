package org.sherman.bloodalcoholcontent.Models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

/**
 * Created by Admin on 11/29/2017.
 */
open class Drink: RealmObject() {
    @PrimaryKey
    private var id = UUID.randomUUID().toString()
    var asu: Double = 0.0
    var time:Long = 0

    fun getID(): String {
        return id
    }

    override  fun toString() : String {
        return "Alcohol Standard Units: $asu"
    }

}

