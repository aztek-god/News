package dv.serg.news.di.component

import dagger.Component
import dv.serg.news.di.PerFragment
import dv.serg.news.di.module.NewsApiModule
import dv.serg.news.di.module.PresenterModule
import dv.serg.news.ui.fragment.NewsFragment

@PerFragment
@Component(dependencies = [AppComponent::class], modules = [(NewsApiModule::class), (PresenterModule::class)])
interface NewsListComponent {
    fun inject(newsFragment: NewsFragment)
}