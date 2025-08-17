package org.example.com.mockclub.di

import org.example.com.mockclub.data.database.repository.*
import org.example.com.mockclub.data.database.service.*
import org.example.com.mockclub.domain.repository.*
import org.example.com.mockclub.domain.service.*
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


val searchModule = module {
    single<SearchRepository> { SearchRepositoryImpl(get()) }
    single<SearchService> { SearchServiceImpl(get()) }
}
val appModule = listOf(
    userModule,
    followerModule,
    postModule,
    commentModule,
    searchModule
)
