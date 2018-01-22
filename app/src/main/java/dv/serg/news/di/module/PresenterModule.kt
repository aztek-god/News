package dv.serg.news.di.module

import dagger.Module
import dagger.Provides
import dv.serg.news.di.PerFragment
import dv.serg.news.model.rest.abstr.NewsDao
import dv.serg.news.model.rest.pojo.Article
import dv.serg.news.ui.fragment.NewsFragment
import dv.serg.news.ui.presenter.NewsPresenter

@Module
class PresenterModule(private val view: NewsFragment) {
    @PerFragment
    @Provides
    fun provideNewsPresenter(remoteDao: NewsDao<String, Article>): NewsPresenter {
        return NewsPresenter(view, remoteDao)
    }
}