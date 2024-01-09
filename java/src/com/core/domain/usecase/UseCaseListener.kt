package com.core.domain.usecase

interface UseCaseListener {
    fun onPreExecute()
    fun onPostExecute()
}