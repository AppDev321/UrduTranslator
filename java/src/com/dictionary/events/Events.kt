package com.dictionary.events

import com.core.database.entity.HistoryEntity
import com.dictionary.model.LanguageModel

class FavouriteUpdate(val item: HistoryEntity)
class LanguageChangeEvent(val isLeftSide: Boolean, val langModel: LanguageModel)