package org.example.krpc.di.network

import io.ktor.client.HttpClient
import org.example.krpc.data.api.rest.RestApi
import org.example.krpc.data.api.rest.RestApiImpl
import org.example.krpc.data.api.rpc.api.RpcApi
import org.example.krpc.data.api.rpc.api.RpcApiImpl
import org.example.krpc.data.network.ktorClient
import org.koin.dsl.module

val ktorClientModule
    get() = module {
        single<HttpClient> { ktorClient() }
        single<RpcApi> { RpcApiImpl(get()) }
        single<RestApi> { RestApiImpl(get()) }
    }