package com.core.domain.remote

object ApiErrorMessages {

    fun getErrorMessage(error: BaseError): String {
        return when (error.errorCode) {
            ResponseErrors.RESPONSE_ERROR -> error.errorMessage
            ResponseErrors.CONNECTIVITY_EXCEPTION -> "Internet connection not available"
            ResponseErrors.HTTP_NOT_FOUND,
            ResponseErrors.HTTP_UNAVAILABLE,
            ResponseErrors.HTTP_BAD_REQUEST
            -> "Can\\'t reach server at the moment"

            ResponseErrors.UNKNOWN_EXCEPTION -> "Something went wrong , please try again later"
            else -> "Something went wrong , please try again later"
        }
    }
    fun getErrorMessage(exception: Exception): String {
        return exception.localizedMessage?.let { localizedMessage ->
            return localizedMessage
        } ?: kotlin.run { "Something went wrong , please try again later" }
    }
}