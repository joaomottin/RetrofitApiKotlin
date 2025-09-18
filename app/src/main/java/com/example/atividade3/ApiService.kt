package com.example.atividade3

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("users")
    fun getUsuarios(): Call<List<Usuario>>

    @GET("users/{id}/todos")
    fun getTarefasDoUsuario(@Path("id") id: Int): Call<List<Tarefa>>
}