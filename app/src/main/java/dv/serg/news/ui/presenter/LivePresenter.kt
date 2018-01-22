package dv.serg.news.ui.presenter

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelStore

class LivePresenter<T>(protected val mData: List<T>? = null) {

    private val viewModelStore: ViewModelStore = ViewModelStore()

//    init {
//        ViewModelStores.of()
////        viewModelStore.
//    }
//
//    fun get

    private class LiveViewModel<T>(mData: List<T>? = null) : ViewModel() {
        var persistData: List<T>? = mData
    }


    interface Cancellable {
        fun cancel()
    }

}