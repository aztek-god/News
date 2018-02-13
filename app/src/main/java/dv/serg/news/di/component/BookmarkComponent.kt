package dv.serg.news.di.component

import dagger.Component
import dv.serg.news.di.PerActivity
import dv.serg.news.di.module.BookmarkModule
import dv.serg.news.ui.activity.RecordActivity

@PerActivity
@Component(dependencies = [AppComponent::class], modules = [BookmarkModule::class])
interface BookmarkComponent {
    fun inject(recordActivity: RecordActivity)
}