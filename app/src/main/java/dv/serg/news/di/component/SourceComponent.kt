package dv.serg.news.di.component

import dagger.Component
import dv.serg.news.di.PerActivity
import dv.serg.news.di.module.SourceModule
import dv.serg.news.ui.activity.SourceActivity

@PerActivity
@Component(dependencies = [AppComponent::class], modules = [SourceModule::class])
interface SourceComponent {
    fun inject(sourceActivity: SourceActivity)
}