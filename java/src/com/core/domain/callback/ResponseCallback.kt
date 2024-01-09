package com.core.domain.callback

import com.core.domain.remote.BaseError

interface OptimizedResponseCallBack<T>{
    fun onApiSuccess(response: T)
    fun onApiError(error: BaseError)
}