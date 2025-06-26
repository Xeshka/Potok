package ru.kolesnik.potok.core.data.util

interface Mapper<FROM, TO> {
    fun map(from: FROM): TO
}