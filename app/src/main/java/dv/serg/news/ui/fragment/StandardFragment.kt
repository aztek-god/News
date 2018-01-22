package dv.serg.news.ui.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import dv.serg.news.util.InjectablePresenter
import javax.inject.Inject

open class StandardFragment<T : InjectablePresenter> : Fragment(), InjectablePresenter {
    override fun attach() {
        // todo
    }

    override fun detach() {
        // todo
        presenter.detach()
    }

    protected var parentContext: Context? = null

    @Inject
    protected lateinit var presenter: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject(this)
    }

    override fun onAttach(context: Context?) {
        // todo make coordinator layout: https://stackoverflow.com/questions/30739806/coordinator-layout-with-toolbar-in-fragments-or-activity
        super.onAttach(context)
        parentContext = context
    }

    //
    override fun onDetach() {
        super.onDetach()
        presenter.detach()
        parentContext = null
    }
}