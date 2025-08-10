package org.example.com.mockclub.di

import org.example.com.mockclub.data.database.repository.FollowerRepositoryImpl
import org.example.com.mockclub.data.database.repository.UserRepositoryImpl
import org.example.com.mockclub.data.database.service.FollowerServiceImpl
import org.example.com.mockclub.data.database.service.UserServiceImpl
import org.example.com.mockclub.domain.repository.FollowerRepository
import org.example.com.mockclub.domain.repository.UserRepository
import org.example.com.mockclub.domain.service.FollowerService
import org.example.com.mockclub.domain.service.UserService
import org.koin.dsl.module

val userModule = module {
    single<UserRepository> { UserRepositoryImpl() }
    single<UserService> { UserServiceImpl(get()) }
}

val followerModule = module {
    single<FollowerRepository> { FollowerRepositoryImpl() }
    single<FollowerService> { FollowerServiceImpl(get()) }
}

val appModule = listOf(
    userModule,
    followerModule,
)
