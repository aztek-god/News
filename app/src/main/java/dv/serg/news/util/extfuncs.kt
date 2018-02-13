package dv.serg.news.util

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import dv.serg.lib.adapter.StandardAdapter
import dv.serg.news.AppContext
import dv.serg.news.AppContext.Companion.TimePattern.Companion.PRETTY
import dv.serg.news.AppContext.Companion.context
import dv.serg.news.R
import dv.serg.news.model.dao.room.entity.Bookmark
import dv.serg.news.model.dao.room.entity.History
import dv.serg.news.model.dao.room.entity.Source
import dv.serg.news.model.rest.pojo.Article
import dv.serg.news.ui.viewholder.SourceHolder
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import java.text.SimpleDateFormat
import java.util.*
import kotlin.reflect.KFunction0


const val DEFAULT_DATETIME_PATTERN = "yyyy'-'MM'-'dd'T'HH':'mm':'ss"
const val TAG = "extfuncs"


fun <T : Fragment> AppCompatActivity.startFragment(fragment: Class<T>, placeholder: Int, isAddToBackstack: Boolean = false) {
    val supportFragmentManager = supportFragmentManager
    val newInstance: T = fragment.newInstance()
    val transaction = supportFragmentManager.beginTransaction()
            .replace(placeholder, newInstance, newInstance.TAG)

    if (isAddToBackstack) {
        transaction.addToBackStack(newInstance.TAG)
    }

    transaction.commit()
}

fun <T : Fragment> AppCompatActivity.startFragment(fragment: KFunction0<T>, placeholder: Int, isAddToBackstack: Boolean = false) {
    val supportFragmentManager = supportFragmentManager
    val newInstance: T = fragment()
    val transaction = supportFragmentManager.beginTransaction()
            .replace(placeholder, newInstance, newInstance.TAG)

    if (isAddToBackstack) {
        transaction.addToBackStack(newInstance.TAG)
    }

    transaction.commit()
}


fun <T> Iterable<T>.asObservable(): io.reactivex.Observable<T> = Observable.fromIterable(this)


fun convertDatetimeFromString(datetimeString: String, pattern: String = DEFAULT_DATETIME_PATTERN): Long {
    val sdf = SimpleDateFormat(pattern, Locale.ENGLISH)
    val date: Date = sdf.parse(datetimeString)
    return date.time
}

fun convertDatetimeToHandyString(datetime: Long): String {
    val sdf = SimpleDateFormat("HH:mm, d __ yyyy")
    val date = Date(datetime)
    val monthNumber = date.month
    return sdf.format(date).replace("__", DateMonthName.getName(monthNumber))
}

object DateMonthName {
    fun getName(index: Int): String {
        return AppContext.context.resources.getStringArray(R.array.months)[index]
//        return AppContext.context.getString(index)
    }
}

fun getCurrentDatetime(pattern: String = PRETTY): String {
    return SimpleDateFormat(pattern, Locale.ENGLISH).format(Calendar.getInstance().time)
}

//val StandardFragment<*>.appContext: AppContext =

fun getCurrentTimestamp(): Long {
    return Calendar.getInstance().time.time
}

fun convertTimestamp(timestamp: Long, pattern: String = PRETTY): String {
    val date = Date(timestamp)
    val sdf = SimpleDateFormat(pattern, Locale.ENGLISH)
    return sdf.format(date)
}

fun convertToHistory(article: Article): History {
    val history = History(0, article.source?.name
            ?: throw ArticleException("Cannot retrieve resource"), article.title
            ?: throw ArticleException("Cannot retrieve title"),
            article.description
                    ?: throw ArticleException("Cannot retrieve description"), article.url
            ?: throw ArticleException("Cannot retrieve article url"), convertDatetimeFromString(article.publishedAt
            ?: throw ArticleException("Cannot retrieve publishedAt")), article.url.hashCode().toLong())
    return history
}

fun convertToBookmark(article: Article): Bookmark {
    val bookmark = Bookmark(0, article.source?.name
            ?: throw ArticleException("Cannot retrieve resource"), article.title
            ?: throw ArticleException("Cannot retrieve title"),
            article.description
                    ?: throw ArticleException("Cannot retrieve description"), article.url
            ?: throw ArticleException("Cannot retrieve article url"), convertDatetimeFromString(article.publishedAt
            ?: throw ArticleException("Cannot retrieve publishedAt")), article.url.hashCode().toLong())
    return bookmark
}

class ArticleException(message: String) : Exception(message)

//interface InjectablePresenter

val Any.TAG: String
    get() {
        return javaClass.simpleName
    }

fun Log.dv(message: String) {
    Log.d("sergdv", message)
}

val CompositeDisposable.isEmpty: Boolean get() = this.size() == 0


fun Activity.startBrowser(url: String) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(url)

    val title = resources.getString(R.string.browser_chooser)
    val chooser = Intent.createChooser(intent, title)

    if (intent.resolveActivity(packageManager) != null) {
        startActivity(chooser)
    }
}

fun Activity.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun StandardAdapter<Source, SourceHolder>.clearSelected() {
    forEach { it.isSubscribed = false }
    notifyDataSetChanged()
}

val isInternetActive: Boolean
    get() {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork = cm.activeNetworkInfo
        val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting

        return isConnected
    }

fun showSimpleDialog(context: Context, title: String, message: String) {
    val alertDialog = AlertDialog.Builder(context).create()
    alertDialog.setTitle(title)
    alertDialog.setMessage(message)
    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
            DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
    alertDialog.show()
}