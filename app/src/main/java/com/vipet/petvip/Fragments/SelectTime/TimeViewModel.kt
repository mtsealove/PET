package com.vipet.petvip.Fragments.SelectTime

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TimeViewModel : ViewModel() {
    private val Start = MutableLiveData<String>()
    private val End = MutableLiveData<String>()

    fun getStart(): LiveData<String> {
        return Start
    }

    fun setStart(start: String) {
        this.Start.value = start
    }

    fun getEnd(): LiveData<String> {
        return End
    }

    fun setEnd(end: String) {
        this.End.value = end
    }
}