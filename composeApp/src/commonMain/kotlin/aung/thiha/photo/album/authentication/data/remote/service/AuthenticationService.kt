package aung.thiha.photo.album.authentication.data.remote.service

import aung.thiha.photo.album.authentication.data.remote.request.AuthenticationRequest
import aung.thiha.photo.album.authentication.data.remote.response.AuthenticationResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class AuthenticationService(
    private val httpClient: HttpClient
) {

    suspend fun signin(
        authenticationRequest: AuthenticationRequest
    ): AuthenticationResponse {
        return httpClient.post("api/auth/signin") {
            contentType(ContentType.Application.Json)
            setBody(authenticationRequest)
        }.body()
    }

    suspend fun signup(
        authenticationRequest: AuthenticationRequest
    ): AuthenticationResponse {
        return httpClient.post("api/auth/signup") {
            contentType(ContentType.Application.Json)
            setBody(authenticationRequest)
        }.body()
    }

}