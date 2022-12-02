package com.intern.gagyebu.dialog

//필터링, 정렬 옵션을 전달하기 위한 CustomClickListener
interface OptionDialogListener {
    fun option(filter:String, order:String)
}