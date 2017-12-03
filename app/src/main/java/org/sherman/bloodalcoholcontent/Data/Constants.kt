package org.sherman.bloodalcoholcontent.Data

/**
 * Created by Admin on 11/29/2017.
 */
val DEBUG = "DEBUG ===>"
val DRINK_TITLE = "Select Drink Type"
val DRINK_STRENGTH = "Select Drink Strength"
val RETURN_DRINK_ACTIVITY = 1 // Activity return Code

enum class DRAW(val title:String){
    TITLE("Set Up"),
    FIRST("Alcohol Budget"),
    SECOND("Profile"),
    THIRD("Help")
}