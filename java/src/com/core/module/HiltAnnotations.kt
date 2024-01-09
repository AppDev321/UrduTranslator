package com.core.module

import javax.inject.Qualifier


@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class DefaultDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class IODispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class MainDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class UnconfinedDispatcher


@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class CoroutineScopeWithIODispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class CoroutineScopeWithDefaultDispatcher
