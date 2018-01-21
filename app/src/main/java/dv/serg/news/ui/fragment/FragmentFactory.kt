package dv.serg.news.ui.fragment

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import dv.serg.news.R
import dv.serg.news.util.TAG

class FragmentFactory {
    companion object {
        inline fun <reified T : Fragment> startFragment(compatActivity: AppCompatActivity, fragment: T) {
            Log.d("FragmentFactory", "---startFragment---")
            val supportFragmentManager = compatActivity.supportFragmentManager

            Log.d("FragmentFactory", "fragment.TAG = ${fragment.TAG}")
            when (fragment) {
                is NewsListFragment -> {
                    Log.d("FragmentFactory", "NewsListFragment.TAG??? = ${fragment.TAG}")
                    val fragmentByTag: Fragment? = supportFragmentManager.findFragmentByTag(fragment.TAG)
                    if (fragmentByTag == null) {
                        supportFragmentManager.beginTransaction().replace(R.id.fragmentHolder, fragment, fragment.TAG).commit()
                    } else {
                        supportFragmentManager.beginTransaction().replace(R.id.fragmentHolder, fragmentByTag, fragmentByTag.TAG).commit()
                    }
                }
            }
        }
    }
}