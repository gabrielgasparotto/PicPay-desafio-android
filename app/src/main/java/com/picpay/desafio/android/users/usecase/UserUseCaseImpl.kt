package com.picpay.desafio.android.users.usecase

import com.picpay.desafio.android.users.model.User
import com.picpay.desafio.android.users.repository.UserRepository
import com.picpay.desafio.android.users.repository.remote.exception.UserServiceException
import com.picpay.desafio.android.users.usecase.exception.UserEmptyException

class UserUseCaseImpl(private val repository: UserRepository) : UserUseCase {

    override suspend fun getUsers(): Result<List<User>> {
        return try {
            getUserResultNotEmptyOrError(repository.getUsers())
        } catch (exception: UserServiceException) {
            getUserResultNotEmptyOrError(repository.getUsersCache())
        }
    }

    private fun getUserResultNotEmptyOrError(result: Result<List<User>>): Result<List<User>> {
        return if (result.isSuccess && result.getOrNull().isNullOrEmpty()) {
            Result.failure(UserEmptyException())
        } else {
            result
        }
    }

}