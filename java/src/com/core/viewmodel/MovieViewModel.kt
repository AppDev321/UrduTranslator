package com.core.viewmodel

import android.view.View
import androidx.lifecycle.viewModelScope
import com.android.inputmethod.latin.R
import com.core.base.BaseViewModel
import com.core.data.model.MovieResponse
import com.core.data.usecase.MovieUseCase
import com.core.domain.callback.OptimizedCallbackWrapper
import com.core.domain.remote.ApiErrorMessages
import com.core.domain.remote.BaseError
import com.core.extensions.TAG
import com.core.utils.AppLogger

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
/*


@HiltViewModel
class MovieViewModel @Inject constructor(
    private val movieUseCase: MovieUseCase,
    private val movieMapper: MovieMapper
) : BaseViewModel<MovieNavigator>() {

     var itemList = arrayListOf<MovieResponse.Results>()

    init {
        AppLogger.e(TAG, "Model init")
    }

    fun fetchMovies() {
        getNavigator()?.setProgressVisibility(View.VISIBLE)
        movieUseCase.execute(
            MovieSubscriber(),
            null,
            coroutineScope = viewModelScope,
            dispatcher = ioDispatcher
        )
    }


    inner class MovieSubscriber :
        OptimizedCallbackWrapper<MovieResponse>() {
        override fun onApiSuccess(response: MovieResponse) {
            getNavigator()?.setProgressVisibility(View.GONE)
            val list = movieMapper.mapFrom(response)
            list?.let {
                getNavigator()?.setAdapter(it)
                itemList = list
            }

        }

        override fun onApiError(error: BaseError) {
            getNavigator()?.setProgressVisibility(View.GONE)
            getNavigator()?.prepareAlert(
                title = R.string.error,
                message = ApiErrorMessages.getErrorMessage(error)
            )
        }
    }


}*/
