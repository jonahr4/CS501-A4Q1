package com.example.q1

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class LifecycleLogger(
    private val log: (String) -> Unit
) : DefaultLifecycleObserver {

    override fun onStart(owner: LifecycleOwner)    { log("onStart") }
    override fun onResume(owner: LifecycleOwner)   { log("onResume") }
    override fun onPause(owner: LifecycleOwner)    { log("onPause") }
    override fun onStop(owner: LifecycleOwner)     { log("onStop") }
    override fun onDestroy(owner: LifecycleOwner)  { log("onDestroy") }
}