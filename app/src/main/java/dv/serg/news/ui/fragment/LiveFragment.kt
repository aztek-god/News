package dv.serg.news.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment

abstract class LiveFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }
}