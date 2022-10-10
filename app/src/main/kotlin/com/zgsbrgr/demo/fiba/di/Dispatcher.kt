package com.zgsbrgr.demo.fiba.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val zgFibaDispatcher: ZGFibaDispatchers)

enum class ZGFibaDispatchers {
    IO
}
