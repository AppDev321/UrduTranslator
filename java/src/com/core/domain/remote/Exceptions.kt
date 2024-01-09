package com.core.domain.remote

import java.io.IOException

class HTTPBadRequest constructor(message: String) : Throwable(message)

class HTTPNotFoundException constructor(message: String) : IOException(message)

class ServerNotAvailableException(message: String) : IOException(message)

class NetworkException(throwable: Throwable) : IOException(throwable.message, throwable)

class SessionExpiredException constructor(message: String) : IOException(message)



