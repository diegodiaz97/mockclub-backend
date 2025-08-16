package org.example.com.mockclub.di

import org.example.com.mockclub.data.database.repository.CommentRepositoryImpl
import org.example.com.mockclub.data.database.repository.FollowerRepositoryImpl
import org.example.com.mockclub.data.database.repository.PostRepositoryImpl
import org.example.com.mockclub.data.database.repository.UserRepositoryImpl
import org.example.com.mockclub.data.database.service.CommentServiceImpl
import org.example.com.mockclub.data.database.service.FollowerServiceImpl
import org.example.com.mockclub.data.database.service.PostServiceImpl
import org.example.com.mockclub.data.database.service.UserServiceImpl
import org.example.com.mockclub.domain.repository.CommentRepository
import org.example.com.mockclub.domain.repository.FollowerRepository
import org.example.com.mockclub.domain.repository.PostRepository
import org.example.com.mockclub.domain.repository.UserRepository
import org.example.com.mockclub.domain.service.CommentService
import org.example.com.mockclub.domain.service.FollowerService
import org.example.com.mockclub.domain.service.PostService
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

val postModule = module {
    single<PostRepository> { PostRepositoryImpl() }
    single<PostService> { PostServiceImpl(get()) }
}

val commentModule = module {
    single<CommentRepository> { CommentRepositoryImpl() }
    single<CommentService> { CommentServiceImpl(get()) }
}

val appModule = listOf(
    userModule,
    followerModule,
    postModule,
    commentModule
)
