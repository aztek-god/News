package dv.serg.news.di.module

import dagger.Module
import dagger.Provides
import dv.serg.lib.dao.Dao
import dv.serg.news.di.PerActivity
import dv.serg.news.model.dao.room.database.AppDatabase
import dv.serg.news.model.dao.room.entity.Source
import dv.serg.news.ui.presenter.SourcePresenter

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
                Source(9, "Polygon", "polygon", "Polygon is a gaming website in partnership with Vox Media. Our culture focused site covers games, their creators, the fans, trending stories and entertainment news."),

                Source(10, "Independent", "independent", "National morning quality (tabloid) includes free online access to news and supplements. Insight by Robert Fisk and various other columnists."),
                Source(11, "NHL News", "nhl-news", "The most up-to-date breaking hockey news from the official source including interviews, rumors, statistics and schedules."),
                Source(12, "Recode", "recode", "Get the latest independent tech news, reviews and analysis from Recode with the most informed and respected journalists in technology and media."),
                Source(13, "Reddit", "reddit", "Reddit is an entertainment, social news networking service, and news website. Reddit's registered community members can submit content, such as text posts or direct links."),
                Source(14, "Reuters", "reuters", "Reuters.com brings you the latest news from around the world, covering breaking news in business, politics, entertainment, technology, video and pictures."),
                Source(15, "RT", "rt", "Актуальная картина дня на RT: круглосуточное ежедневное обновление новостей политики, бизнеса, финансов, спорта, науки, культуры. Онлайн-репортажи с места событий. Комментарии экспертов, актуальные интервью, фото и видео репортажи."),
                Source(16, "Talksport", "talksport", "Tune in to the world's biggest sports radio station - Live Premier League football coverage, breaking sports news, transfer rumours &amp; exclusive interviews."),
                Source(17, "The Economist", "the-economist", "The Economist offers authoritative insight and opinion on international news, politics, business, finance, science, technology and the connections between them."),
                Source(18, "The Lad Bible", "the-lad-bible", "The LAD Bible is one of the largest community for guys aged 16-30 in the world. Send us your funniest pictures and videos!"),
                Source(19, "Time", "time", "Breaking news and analysis from TIME.com. Politics, world news, photos, video, tech reviews, health, science and entertainment news."),
                Source(20, "Wired", "wired", "Wired is a monthly American magazine, published in print and online editions, that focuses on how emerging technologies affect culture, the economy, and politics."),
                Source(21, "NBC News", "nbc-news", "Breaking news, videos, and the latest top stories in world news, b`usiness, politics, health and pop culture."),
                Source(22, "Al Jazeera English", "al-jazeera-english", "News, analysis from the Middle East and worldwide, multimedia and interactives, opinions, documentaries, podcasts, long reads and broadcast schedule."),
                Source(23, "Der Tagesspiegel", "der-tagesspiegel", "Nachrichten, News und neueste Meldungen aus dem Inland und dem Ausland - aktuell präsentiert von tagesspiegel.de."),
                Source(23, "Fortune", "fortune", "Polygon is a gaming website in partnership with Vox Media. Our culture focused site covers games, their creators, the fans, trending stories and entertainment news."),
                Source(23, "Fox News", "Fox News", "Breaking News, Latest News and Current News from FOXNews.com. Breaking news and video. Latest Current News: U.S., World, Entertainment, Health, Business, Technology, Politics, Sports."),
                Source(23, "Fox Sports", "fox-sports", "Find live scores, player and team news, videos, rumors, stats, standings, schedules and fantasy games on FOX Sports."),
                Source(24, "El Mundo", "el-mundo", "Noticias, actualidad, álbumes, debates, sociedad, servicios, entretenimiento y última hora en España y el mundo.")
        )
    }

    @PerActivity
    @Provides
    fun provideSourceDao(appDatabase: AppDatabase): Dao<Source> {
        val roomDao = appDatabase.getSourceDao()
        return object : Dao<Source> {
            override fun getAll(): List<Source> {
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