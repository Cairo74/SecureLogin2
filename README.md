# 🔐 SecureLogin - Sistema de Autenticação, Cadastro e Recuperação de Senha

Aplicação web desenvolvida com **Spring Boot** e **Thymeleaf**, implementando controle de acesso e autenticação com **Spring Security**, cadastro de usuários com criptografia **BCrypt**, armazenamento de dados em **memória (List/ArrayList)** e recuperação de senha com envio de e-mails via **Spring Mail** (seguindo a referência do projeto SendEmail).

---

## 🚀 Tecnologias Utilizadas

- **Java 21+** (compatível com JDK 21 a 25)
- **Spring Boot 4.x** (WebMVC, Thymeleaf, Spring Security, Spring Mail)
- **Thymeleaf** (Template Engine para renderização dinâmica no servidor)
- **Spring Security** (Proteção de rotas, controle de sessão, CSRF e autenticação)
- **BCrypt** (Criptografia segura para senhas)
- **Armazenamento em Memória** (`List<User>` em `UserService`)
- **JavaMailSender** (Envio de e-mails para recuperação de senha)
- **HTML5 & CSS3 Moderno** (Identidade visual própria com tema escuro, glassmorphism e animações fluidas)

---

## 📁 Estrutura do Projeto

A organização dos arquivos segue a arquitetura solicitada para a atividade:

```
src/
└── main/
    ├── java/
    │   └── com/example/SecureLogin2/
    │       ├── SecureLogin2Application.java       # Classe principal que inicia o Spring Boot
    │       │
    │       ├── application/                       # Classes de negócio e dados
    │       │   ├── User.java                      # Classe modelo simples (atributos, getters/setters)
    │       │   ├── UserService.java               # Lista em memória (List<User>) e regras de cadastro/login
    │       │   └── EmailService.java              # Envio de e-mails (estrutura do SendEmail)
    │       │
    │       ├── controller/                        # Controllers com rotas web
    │       │   ├── AuthController.java            # Endpoints /login, /register e /recoverpassword
    │       │   └── HomeController.java            # Endpoint /home da área restrita
    │       │
    │       └── config/                            # Configurações do framework
    │           └── SecurityConfig.java            # Configuração do Spring Security e rotas
    │
    └── resources/
        ├── application.properties                 # Configurações do servidor e de envio de e-mail
        ├── static/
        │   ├── css/
        │   │   └── style.css                      # Folha de estilos personalizada
        │   ├── images/
        │   │   └── logo.svg                       # Logotipo da aplicação
        │   ├── js/
        │   │   └── main.js                        # Alternar visualização da senha
        │   └── videos/
        │       └── .gitkeep                       # Diretório reservado para vídeos opcionais
        │
        └── templates/
            ├── login.html                         # Tela de login
            ├── register.html                      # Tela de cadastro
            ├── recoverpassword.html               # Tela de recuperação de senha
            └── home.html                          # Tela pós-login (autenticado) com logout
```

---

## 🌐 Endpoints Disponíveis

| Método | Endpoint | Descrição | Acesso |
| :--- | :--- | :--- | :--- |
| **GET** | `/login` | Exibe a tela de login (com mensagens de erro/sucesso) | Público |
| **POST** | `/login` | Processa a autenticação (gerenciado pelo Spring Security) | Público |
| **GET** | `/register` | Exibe o formulário de cadastro de novos usuários | Público |
| **POST** | `/register` | Processa e valida os dados de cadastro | Público |
| **GET** | `/recoverpassword` | Exibe o formulário de recuperação de senha | Público |
| **POST** | `/recoverpassword` | Gera nova senha e envia por e-mail | Público |
| **GET** | `/home` ou `/` | Área restrita com perfil do usuário logado | **Autenticado** |
| **POST** | `/logout` | Encerra a sessão atual com segurança | **Autenticado** |

---

## 🔑 Funcionalidades e Regras de Negócio

### 1. 🎨 Tela de Login (`/login`)
- Permite entrar informando **nome de usuário** OU **endereço de e-mail**.
- Campo de senha com botão para alternar visibilidade (mostrar/ocultar senha).
- Exibição de alertas dinâmicos para credenciais incorretas ou sessão encerrada.
- Links rápidos para cadastro (`/register`) e recuperação de senha (`/recoverpassword`).
- **Conta de demonstração pré-configurada**:
  - Usuário: `admin` (ou `admin@pucminas.br`)
  - Senha: `admin123`

### 2. 👤 Tela de Cadastro (`/register`)
Validações simples e diretas:
- ❌ Bloqueio de campos obrigatórios vazios.
- ❌ Validação de formato de e-mail (`.contains("@") && .contains(". ")`).
- ❌ Exigência de senha com no mínimo 6 caracteres.
- ❌ Verificação de confirmação de senha idêntica.
- ❌ Bloqueio de cadastros duplicados (mesmo nome de usuário ou mesmo e-mail).
- 🔒 Salvamento do usuário com senha criptografada com **BCrypt**.

### 3. 📧 Recuperação de Senha (`/recoverpassword`)
- Estrutura baseada no projeto de referência **SendEmail** da disciplina.
- Recebe o e-mail cadastrado e verifica na lista de usuários.
- Gera uma senha numérica temporária de 6 dígitos.
- Atualiza a senha criptografada do usuário e envia uma mensagem por e-mail via `JavaMailSender`.

### 4. 🛡️ Área Protegida (`/home`)
- Usuários não autenticados são automaticamente redirecionados para a tela de login.
- Exibe o nome do usuário logado.
- Botão de encerramento de sessão (**Logout**) que limpa a sessão.

---

## ⚙️ Como Executar o Projeto

1. Abra o terminal na pasta do projeto e execute:

**No Windows:**
```powershell
.\mvnw.cmd spring-boot:run
```

**No Linux / macOS:**
```bash
./mvnw spring-boot:run
```

2. Acesse no navegador:
```
http://localhost:8080/login
```

3. Teste o login com:
   - **Usuário:** `admin`
   - **Senha:** `admin123`
