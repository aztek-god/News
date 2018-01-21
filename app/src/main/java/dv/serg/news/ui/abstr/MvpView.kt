package dv.serg.news.ui.abstr

interface MvpView<T> {
    fun onStartLoading()
    fun show(data: List<T>)
    fun showError(message: Throwable)
    fun onFinishLoading()
}