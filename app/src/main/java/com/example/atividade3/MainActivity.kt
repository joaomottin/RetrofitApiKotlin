package com.example.atividade3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController, startDestination = "principal") {

                composable("principal") { TelaUsuarios(navController) }

                composable(
                    "detalhes/{id}/{username}/{name}/{email}",
                    arguments = listOf(
                        navArgument("id") { type = NavType.IntType },
                        navArgument("username") { type = NavType.StringType },
                        navArgument("name") { type = NavType.StringType },
                        navArgument("email") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val id = backStackEntry.arguments?.getInt("id") ?: 0
                    val username = backStackEntry.arguments?.getString("username") ?: ""
                    val name = backStackEntry.arguments?.getString("name") ?: ""
                    val email = backStackEntry.arguments?.getString("email") ?: ""
                    TelaDetalhes(navController, id, username, name, email)
                }

                composable(
                    "tarefas/{userId}/{username}",
                    arguments = listOf(
                        navArgument("userId") { type = NavType.IntType },
                        navArgument("username") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val userId = backStackEntry.arguments?.getInt("userId") ?: 0
                    val username = backStackEntry.arguments?.getString("username") ?: ""
                    TelaTarefas(navController, userId, username)
                }
            }
        }
    }
}

@Composable
fun TelaUsuarios(navController: androidx.navigation.NavController) {
    var listaUsuarios by remember { mutableStateOf<List<Usuario>>(emptyList()) }
    var erro by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        RetrofitInstance.apiService.getUsuarios().enqueue(object : Callback<List<Usuario>> {
            override fun onResponse(call: Call<List<Usuario>>, response: Response<List<Usuario>>) {
                if (response.isSuccessful) listaUsuarios = response.body() ?: emptyList()
                else erro = "Erro ao carregar usuários"
            }
            override fun onFailure(call: Call<List<Usuario>>, t: Throwable) {
                erro = "Falha ao carregar usuários"
            }
        })
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            "Usuários",
            fontSize = 28.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))
        Text("Total de usuários: ${listaUsuarios.size}", fontSize = 16.sp)
        Spacer(Modifier.height(16.dp))

        if (erro != null) Text(erro!!)
        else LazyColumn(Modifier.weight(1f)) {
            items(listaUsuarios) { usuario ->
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { navController.navigate("detalhes/${usuario.id}/${usuario.username}/${usuario.name}/${usuario.email}") }
                ) {
                    Text("${usuario.username} (ID ${usuario.id})", fontSize = 20.sp)
                    Text("Nome: ${usuario.name}")
                    Text("E-mail: ${usuario.email}")
                    Spacer(Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun TelaDetalhes(navController: androidx.navigation.NavController, id: Int, username: String, name: String, email: String) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            "Detalhes do usuário",
            fontSize = 28.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(16.dp))
        Text("ID: $id", fontSize = 18.sp)
        Text("Username: $username", fontSize = 18.sp)
        Text("Nome: $name", fontSize = 18.sp)
        Text("E-mail: $email", fontSize = 18.sp)
        Spacer(Modifier.height(24.dp))

        Button(onClick = { navController.navigate("tarefas/$id/$username") }, modifier = Modifier.fillMaxWidth()) {
            Text("Ver Tarefas do Usuário")
        }

        Spacer(Modifier.weight(1f))

        Button(onClick = { navController.popBackStack() }, modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
            Text("Voltar")
        }
    }
}

@Composable
fun TelaTarefas(navController: androidx.navigation.NavController, userId: Int, username: String) {
    var listaTarefas by remember { mutableStateOf<List<Tarefa>>(emptyList()) }
    var erro by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(userId) {
        RetrofitInstance.apiService.getTarefasDoUsuario(userId).enqueue(object : Callback<List<Tarefa>> {
            override fun onResponse(call: Call<List<Tarefa>>, response: Response<List<Tarefa>>) {
                if (response.isSuccessful) listaTarefas = response.body() ?: emptyList()
                else erro = "Erro ao carregar tarefas"
            }
            override fun onFailure(call: Call<List<Tarefa>>, t: Throwable) {
                erro = "Falha ao carregar tarefas"
            }
        })
    }

    val concluidas = listaTarefas.count { it.completed }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            "Tarefas de $username",
            fontSize = 28.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))
        Text("Total de tarefas: ${listaTarefas.size} ($concluidas concluídas)", fontSize = 16.sp)
        Spacer(Modifier.height(16.dp))

        if (erro != null) Text(erro!!)
        else LazyColumn(Modifier.weight(1f)) {
            items(listaTarefas) { tarefa ->
                Column(Modifier.padding(8.dp)) {
                    Text("(ID #${tarefa.id}) ${tarefa.title}", fontSize = 20.sp)
                    Text("Concluída?: ${if (tarefa.completed) "Sim" else "Não"}")
                    Spacer(Modifier.height(12.dp))
                }
            }
        }

        Button(onClick = { navController.popBackStack() }, modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
            Text("Voltar")
        }
    }
}
