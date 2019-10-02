package com.elyeproj.simplearchcomp

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val viewModel: MyViewModel by lazy {
        ViewModelProviders.of(this).get(MyViewModel::class.java)
    }

    private val changeObserver =
            Observer<Int> { value -> value?.let { incrementCount(value) } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel.restoreState(savedInstanceState)
        viewModel.changeRegister.observe(this, changeObserver)
        my_container.setOnClickListener { viewModel.increment() }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.saveState(outState)
    }

    private fun incrementCount(value: Int) {
        my_text.text = (value).toString()
    }
}

class MyViewModel : ViewModel() {
    companion object {
        const val COUNT_KEY = "CountKey"
    }

    private val changeNotifier = MutableLiveData<Int>()
    val changeRegister: LiveData<Int>
        get() = changeNotifier

    private var count: Int
        get() = changeNotifier.value ?: 0
        set(value) { changeNotifier.value = value}

    fun increment() {
        Handler().postDelayed({
            changeNotifier.value = count + 1
        }, 3000)
    }

    fun saveState(outState: Bundle) {
        outState.putInt(COUNT_KEY, count)
    }

    fun restoreState(inState: Bundle?) {
        inState?.let {
            count = inState.getInt(COUNT_KEY)
        }
    }
}
