Claro! Aqui está uma versão mais resumida do README:

---

# Projeto Spring Security com JWT

## Descrição

Aplicação Spring Boot com autenticação JWT e controle de acesso baseado em roles. Inclui gerenciamento de usuários e posts.

## Funcionalidades

- **Autenticação JWT**: Gera e valida tokens JWT.
- **Administração de Usuários**: Criação e gerenciamento de usuários com roles.
- **Controle de Posts**: CRUD básico para posts.

## Entidades

- **User**: Representa um usuário com `id`, `username`, `password` e `roles`.
- **Role**: Representa uma role com `roleId` e `roleName`.
- **Post**: Representa um post com `postId`, `user`, `conteudo` e `creationTimestamp`.

## Repositórios

- **UserRepository**: Operações CRUD para `User`.
- **RoleRepository**: Operações CRUD para `Role`.
- **PostRepository**: Operações CRUD para `Post`.

## Endpoints

- **`/login`** (POST): Autentica e retorna um token JWT.
- **`/users`** (POST, GET): Cria e obtém usuários.
- **`/posts`** (POST, DELETE): Cria e deleta posts.
