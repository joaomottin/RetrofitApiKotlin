# Atividade 3 - App de Usuários e Tarefas

## Descrição
Este é um aplicativo Android feito em **Kotlin** com **Jetpack Compose**, que exibe usuários e suas tarefas obtidas de uma API externa. O app permite navegar entre as telas de usuários, detalhes do usuário e tarefas, com botões de navegação para avançar e voltar. Todas as telas estão implementadas no arquivo `MainActivity.kt`.

## Tecnologias
- Kotlin
- Jetpack Compose
- Retrofit + Gson
- Navigation Compose
- Java 1.9

## Funcionalidades

### Tela Principal (Usuários)
- Lista todos os usuários via `GET /users`.
- Exibe **username, nome e e-mail** de cada usuário.
- Mostra o total de usuários.
- Clique em um usuário para navegar para a tela de detalhes.

### Tela de Detalhes
- Exibe **ID, username, nome e e-mail** do usuário selecionado.
- Botão para abrir as tarefas do usuário.
- Botão de **voltar** fixado na parte inferior da tela.

### Tela de Tarefas
- Lista todas as tarefas do usuário via `GET /users/{id}/todos`.
- Exibe **ID da tarefa, título e status de conclusão**.
- Mostra o total de tarefas e quantas estão concluídas.
- Botão de **voltar** fixado na parte inferior da tela.

## Estrutura do Projeto
- `MainActivity.kt`: Contém todas as telas:
  - `TelaUsuarios`: Lista usuários e exibe contador.
  - `TelaDetalhes`: Mostra informações do usuário e botão para tarefas.
  - `TelaTarefas`: Lista tarefas, mostra contador de concluídas e botão de voltar.
- `ApiService.kt`: Interface Retrofit com os endpoints da API.
- `RetrofitInstance.kt`: Singleton que configura Retrofit com Gson e base URL `https://jsonplaceholder.typicode.com/`.
- Modelos:
  - `Usuario(id: Int, name: String, username: String, email: String)`
  - `Tarefa(id: Int, title: String, completed: Boolean)`

## Observações
- O app não utiliza ViewModel nem banco de dados; o estado é mantido em memória.
- Mensagens de erro são exibidas de forma simples.
- Botões de voltar nas telas de detalhes e tarefas estão travados na parte inferior usando `Spacer(Modifier.weight(1f))`.
