package ru.kolesnik.potok.core.network.repository

interface SearchRepository {
    suspend fun search(query: String): List<String>
}