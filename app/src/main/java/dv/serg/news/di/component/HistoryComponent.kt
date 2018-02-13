package dv.serg.news.di.component

import dagger.Component
import dv.serg.news.di.PerActivity
import dv.serg.news.di.module.HistoryModule
import dv.serg.news.ui.activity.RecordActivity

@PerActivity
@Component(dependencies = [AppComponent::class], modules = [HistoryModule::class])
interface HistoryComponent {
    fun inject(recordActivity: RecordActivity)
}