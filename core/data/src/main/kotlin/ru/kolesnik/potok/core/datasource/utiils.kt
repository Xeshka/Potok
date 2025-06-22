package ru.kolesnik.potok.core.datasource

interface Mapper<FROM, TO> {
    fun map(from: FROM): TO
}