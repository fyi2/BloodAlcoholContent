package org.sherman.bloodalcoholcontent.Data

/**
 * Created by Admin on 11/29/2017.
 */
val DEBUG = "DEBUG ===>"
val DRINK_TITLE = "Select Drink Type"
val DRINK_STRENGTH = "Select Drink Strength"
val GRAMS_OF_ALCOHOL = 14
val GRAMS_PER_POUND = 454
val ELAPSED_TIME = 24
val ABSORPTION_RATE = 0.015
val RETURN_DRINK_ACTIVITY = 1 // Activity return Code

val DAYINMILLI: Long  = 24*60*60*1000

enum class DRAW(val title:String){
    TITLE("Set Up"),
    FIRST("Alcohol Budget"),
    SECOND("Profile"),
    THIRD("Help")
}