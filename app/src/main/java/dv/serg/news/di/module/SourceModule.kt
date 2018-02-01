package dv.serg.news.di.module

import dagger.Module
import dagger.Provides
import dv.serg.lib.dao.Dao
import dv.serg.news.di.PerActivity
import dv.serg.news.model.dao.room.database.AppDatabase
import dv.serg.news.model.dao.room.entity.Source
import dv.serg.news.ui.presenter.SourcePresenter
import io.reactivex.Flowable

@Module
class SourceModule {
    @PerActivity
    @Provides
    fun provideSources(): List<Source> {
        return listOf(
                Source(1, "Lenta.ru", "lenta", "Новости, статьи, фотографии, видео. Семь дней в неделю, 24 часа в сутки."),
                Source(2, "RBC", "rbc", "Главные новости политики, экономики и бизнеса, комментарии аналитиков, " +
                        "финансовые данные с российских и мировых биржевых систем на сайте rbc.ru."),
                Source(3, "BBC Sport", "bbc-sport", "The home of BBC Sport online. Includes " +
                        "live sports coverage, breaking news, results, video, audio and analysis on Football, F1, Cricket, Rugby Union, Rugby League, " +
                        "Golf, Tennis and all the main world sports, plus major events such as the Olympic Games."),
                Source(4, "Marca", "marca", "La mejor información deportiva en castellano actualizada minuto " +
                        "a minuto en noticias, vídeos, fotos, retransmisiones y resultados en directo."),
                Source(5, "InfoMoney", "info-money", "No InfoMoney você encontra tudo o que precisa sobre " +
                        "dinheiro. Ações, investimentos, bolsas de valores e muito mais. Aqui você encontra informação que vale dinheiro!"),
                Source(6, "Metro", "metro", "News, Sport, Showbiz, Celebrities from Metro - a free British newspaper."),
                Source(7, "National Geographic", "national-geographic", "Reporting our world daily: original nature and science news from National Geographic."),
                Source(8, "News24", "news24", "South Africa's premier news source, " +
                        "provides breaking news on national, world, Africa, sport, entertainment, technology and more."),
                Source(9, "Polygon", "polygon", "Polygon is a gaming website in partnership with Vox Media. Our culture focused site covers games, their creators, the fans, trending stories and entertainment news.")
        )
    }

    @PerActivity
    @Provides
    fun provideSourceDao(appDatabase: AppDatabase): Dao<Source> {
        val roomDao = appDatabase.getSourceDao()
        return object : Dao<Source> {
            override fun getAll(): Flowable<List<Source>> {
                return roomDao.getAll()
            }

            override fun deleteAll() {
                roomDao.deleteAll()
            }

            override fun insert(value: Source) {
                roomDao.insert(value)
            }

            override fun insertAll(values: List<Source>) {
                roomDao.insertAll(values)
            }
        }
    }


    @PerActivity
    @Provides
    fun providePresenter(sources: List<Source>, dao: Dao<Source>): SourcePresenter {
        return SourcePresenter(sources, dao)
    }
}