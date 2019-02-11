package com.github.spheniscine.kbinding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations

typealias BoxedLiveData<T> = LiveData<Box<T>?>
fun <T> LiveData<T>.toBoxed(): BoxedLiveData<T?> =
    Transformations.map(this) { Box(it) }.apply { kick() }

typealias BoxedMutableLiveData<T> = MutableLiveData<Box<T>?>

typealias BoxedMediatorLiveData<T> = MediatorLiveData<Box<T>?>