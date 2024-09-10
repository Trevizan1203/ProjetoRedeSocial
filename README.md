# Spring Security Project

Este projeto é uma implementação de um sistema de autenticação e controle de permissões usando Spring Security. Ele oferece um exemplo prático de como implementar autenticação JWT, gerenciamento de usuários, roles e permissões administrativas.

## Funcionalidades

- Autenticação de usuários com JWT (JSON Web Token)
- Configuração de usuários administrativos
- Controle de permissões baseado em roles
- CRUD para entidades de usuários e posts
- Segurança configurada para endpoints RESTful

## Estrutura do Projeto

- `config/`: Configurações de segurança, incluindo classes para configurar o acesso e a autenticação.
  - **AdminUserConfig.java**: Configuração de usuário administrador.
  - **SecurityConfig.java**: Configuração da segurança, incluindo JWT.
  
- `controller/`: Controladores REST para gerenciamento de usuários, tokens e posts.
  - **PostController.java**: Controlador para operações de CRUD relacionadas a posts.
  - **TokenController.java**: Controlador para operações relacionadas a autenticação e emissão de tokens.
  - **UserController.java**: Controlador para operações de CRUD relacionadas a usuários.

- `DTO/`: Objetos de transferência de dados usados para comunicação entre cliente e servidor.
  - **CreatePost.java, CreateUser.java**: DTOs para criação de posts e usuários.
  - **LoginRequest.java, LoginResponse.java**: DTOs para a requisição e resposta do login.

- `entities/`: Definições de entidades JPA.
  - **Post.java**: Entidade de post.
  - **User.java**: Entidade de usuário.
  - **Role.java**: Entidade de role (permissão).

- `repository/`: Interfaces para comunicação com o banco de dados.
  - **PostRepository.java, RoleRepository.java, UserRepository.java**: Repositórios para acessar as entidades do banco de dados.

## Configuração

- **application.properties**: Contém as configurações de conexão com o banco de dados e outras propriedades da aplicação.
  
- **private.pem & public.pem**: Chaves privadas e públicas usadas para a assinatura e verificação de JWT.

## Pré-requisitos

- Java 17+
- Maven
- Spring Boot
- Banco de dados PostgreSQL

## Melhorias Futuras

- Implementar mais testes unitários e de integração.
- Adicionar documentação com Swagger.
- Suporte para OAuth2.
