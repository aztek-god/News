package dv.serg.news.di.component

import dagger.Component
import dv.serg.news.di.PerActivity
import dv.serg.news.di.module.BookmarkModule
import dv.serg.news.di.module.NewsModule
import dv.serg.news.di.module.SourceModule
import dv.serg.news.ui.activity.NewsActivity

@PerActivity
@Component(dependencies = [AppComponent::class], modules = [NewsModule::class, SourceModule::class, BookmarkModule::class])
interface NewsComponent {
    fun inject(newsActivity: NewsActivity)
}