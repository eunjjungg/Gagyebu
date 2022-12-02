package com.intern.gagyebu.produce

enum class Category(val script: String) {
    income("수입"),
    foodCost("식료품"),
    housingCost("주거비"),
    eduCost("교육비"),
    healthCost("의료비"),
    transCost("교통비"),
    commuCost("통신비"),
    ect("기타");

    override fun toString() : String {
        return script
    }

}