package org.example.krpc.services

import org.example.krpc.models.requests.AuthBody
import org.example.krpc.models.responses.UserResponse
import kotlinx.rpc.RemoteService
import kotlinx.rpc.annotations.Rpc

@Rpc
interface AuthRpcService: RemoteService {

    /** регистрация пользователя
     * @param body - логин и пароль пользователя
     */
    suspend fun registerNewUser(body: AuthBody): UserResponse

    /** вход
     * @param body - логин и пароль пользователя
     */
    suspend fun login(body: AuthBody): UserResponse

    /** получить новую пару токенов
     * @param refreshToken - рефреш-токен
     * @return возвращает пару auth и refresh токенов
     */
    suspend fun refreshToken(refreshToken: String): UserResponse
}
