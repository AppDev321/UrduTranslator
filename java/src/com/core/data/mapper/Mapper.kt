package com.core.data.mapper

interface Mapper<F, T> {
    fun mapFrom(from: F): T
}