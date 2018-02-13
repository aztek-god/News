package dv.serg.lib.collection

interface ObservableList<T> : MutableList<T> {

    interface ListObserver<T> {
        fun observeChanges(oldData: List<T>, newData: List<T>)
    }

    fun addObserver(observer: ListObserver<T>)

    fun clearObservers()

    fun removeObserver(observer: ListObserver<T>)
}