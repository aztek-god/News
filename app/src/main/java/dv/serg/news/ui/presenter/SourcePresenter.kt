package dv.serg.news.ui.presenter

import dv.serg.lib.dao.Dao
import dv.serg.news.model.dao.room.entity.Source

class SourcePresenter(val sources: List<Source>, val dao: Dao<Source>)