package com.github.spheniscine.kbinding.adapters

import android.widget.SeekBar
import com.github.spheniscine.kbinding.KBindableVar

val SeekBar.progress_kb get() = KBindableVar.adapt(
    get = ::getProgress,
    set = ::setProgress,
    attachListener = { update ->
        setOnSeekBarChangeListener(
            object: SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) { update() }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}

            }
        )
    }
)