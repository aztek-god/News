package dv.serg.lib.collection

class ObservableArrayList<T> : ArrayList<T>(), ObservableList<T> {

    private val changeObservers: MutableList<ObservableList.ListObserver<T>> = ArrayList()

    private inline fun process(process: () -> Unit) {
        val oldData = ArrayList<T>(this)
        process()
        val newData = this
        changeObservers.forEach { it: ObservableList.ListObserver<T> -> it.observeChanges(oldData, newData) }
    }

    override fun addObserver(observer: ObservableList.ListObserver<T>) {
        changeObservers.add(observer)
    }

    override fun clearObservers() {
        changeObservers.clear()
    }

    override fun removeObserver(observer: ObservableList.ListObserver<T>) {
        changeObservers.remove(observer)
    }

    override fun add(element: T): Boolean {
        var flag = false
        process {
            flag = super.add(element)
        }

        return flag
    }

    override fun add(index: Int, element: T) {
        process {
            super.add(index, element)
        }
    }

    override fun addAll(index: Int, elements: Collection<T>): Boolean {
        var flag = false
        process {
            flag = super.addAll(index, elements)
        }

        return flag
    }

    override fun addAll(elements: Collection<T>): Boolean {
        var flag = false
        process {
            flag = super.addAll(elements)
        }

        return flag
    }

    override fun clear() {
        process { super.clear() }
    }

    override fun remove(element: T): Boolean {
        var flag = false
        process { flag = super.remove(element) }
        return flag
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        var flag = false
        process { flag = super.removeAll(elements) }
        return flag
    }

    override fun removeAt(index: Int): T {
        var elem: T? = null
        process { elem = super.removeAt(index) }
        return elem ?: throw Exception("The value at index: $index is missing.")
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        var flag = false
        process { flag = super.retainAll(elements) }
        return flag
    }

    override fun set(index: Int, element: T): T {
        var elem: T? = null
        process { elem = super.set(index, element) }
        return elem ?: throw Exception("The value at index was not be inserted.")
    }

}
