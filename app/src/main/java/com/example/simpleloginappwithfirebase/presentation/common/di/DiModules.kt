package com.example.simpleloginappwithfirebase.presentation.common.di

import androidx.room.Room
import com.example.simpleloginappwithfirebase.data.common.StringProviderImpl
import com.example.simpleloginappwithfirebase.data.repository.AppDataBase
import com.example.simpleloginappwithfirebase.data.repository.ItemRepositoryImpl
import com.example.simpleloginappwithfirebase.data.repository.UserRepositoryImpl
import com.example.simpleloginappwithfirebase.domain.common.StringProvider
import com.example.simpleloginappwithfirebase.domain.repostiory.ItemRepository
import com.example.simpleloginappwithfirebase.domain.repostiory.UserRepository
import com.example.simpleloginappwithfirebase.presentation.listdata.ItemsViewModel
import com.example.simpleloginappwithfirebase.presentation.login.LoginViewModel
import com.example.simpleloginappwithfirebase.presentation.profile.ProfileViewModel
import com.example.simpleloginappwithfirebase.presentation.signup.SignUpViewModel
import com.example.simpleloginappwithfirebase.presentation.welcomescreen.WelcomeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val viewModelsModule: Module = module {
    viewModel {
        LoginViewModel(get(), get())
    }
    viewModel {
        SignUpViewModel(get(), get())
    }
    viewModel {
        WelcomeViewModel()
    }
    viewModel {
        ItemsViewModel(get(), get())
    }
    viewModel {
        ProfileViewModel(get())
    }
}

val repositoriesModule: Module = module {
    single<UserRepository> { UserRepositoryImpl(get()) }
    single<ItemRepository> { ItemRepositoryImpl(get()) }
}

val commonModule: Module = module {
    single<StringProvider> { StringProviderImpl(androidContext()) }
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDataBase::class.java,
            AppDataBase.DATABASE_NAME
        ).build()
    }
}