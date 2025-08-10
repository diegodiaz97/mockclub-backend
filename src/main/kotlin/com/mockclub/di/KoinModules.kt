package org.example.com.mockclub.di

import org.example.com.mockclub.data.database.repository.UserRepositoryImpl
import org.example.com.mockclub.data.database.service.UserServiceImpl
import org.example.com.mockclub.domain.repository.UserRepository
import org.example.com.mockclub.domain.service.UserService
import org.koin.dsl.module

val userModule = module {
    single<UserRepository> { UserRepositoryImpl() }
    single<UserService> { UserServiceImpl(get()) }
}

val appModule = listOf(
    userModule
)
