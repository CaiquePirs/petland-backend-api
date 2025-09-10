# 🐾 Petland Backend API

Bem-vindo ao **Petland**, uma API backend robusta e escalável desenvolvida para gerenciar as operações de um pet shop moderno. Construída com Java e Spring Boot, esta aplicação cuida de tudo: desde o gerenciamento de clientes e pets até vendas de produtos, agendamento de serviços e controle de vacinas. É modular, segura e pronta para produção.

---

## 🚀 Visão Geral do Projeto

Petland é uma solução backend voltada para negócios de pet shop. Ela centraliza operações como:

- 📦 Controle de estoque (produtos e vacinas)
- 👥 Gerenciamento de clientes e funcionários
- 🐾 Cadastro e rastreamento de pets
- 🛁 Agendamento e gerenciamento de serviços pet (tosa, consultas, exames)
- 💰 Vendas de produtos e faturamento
- 💉 Controle de vacinação
- 📊 Relatórios de receita e serviços
- 🗓️ Geração de PDFs para agendamentos e relatórios financeiros

---

## 🧩 Stack Tecnológica

| Camada             | Tecnologia                            |
|--------------------|----------------------------------------|
| Linguagem          | Java                                   |
| Framework          | Spring Boot                            |
| Segurança          | Spring Security + JWT                  |
| Arquitetura        | Camadas (Domain-Driven Design)         |
| Conteinerização    | Docker + Docker Compose                |
| Documentação       | Swagger                                |
| Testes             | JUnit + Testes Unitários e de Integração |
| Banco de Dados     | Relacional (ex: PostgreSQL)            |

---

## 📚 Funcionalidades

### 🐶 Gestão de Pets
- Cadastro de múltiplos pets por cliente
- Rastreamento de dados: nome, raça, idade, espécie, peso
- Associação de pets a serviços e compras

### 👤 Gestão de Clientes
- Validação de e-mail e login seguro
- Visualização de histórico de serviços, compras e agendamentos

### 🧼 Serviços & Agendamentos
- Agendamento de serviços como banho, tosa, consultas médicas
- Regras de tempo (mínimo 1h antes, máximo 30 dias à frente)
- Cancelamento ou reagendamento
- Geração de PDFs de agendamentos

### 💼 Gestão de Funcionários
- Atribuição de funcionários a departamentos (MÉDICO, CUIDADOS, etc.)
- Auditoria de ações e atualizações

### 🛍️ Produtos & Vendas
- Gerenciamento de estoque, validade e preços
- Vendas com múltiplos itens e métodos de pagamento (DINHEIRO, CARTÃO)
- Atualização de estoque após compra

### 💉 Controle de Vacinação
- Cadastro de vacinas e eventos de vacinação
- Associação de vacinas a pets e clientes

### 📊 Relatórios & Dashboards
- Relatórios de vendas por produto, vacina ou período
- Relatórios de vendas de serviços e vaccinações
- Exportação de resumos financeiros em PDF

### 🔐 Segurança
- Acesso baseado em papéis (ADMIN, CLIENTE)
- Autenticação com JWT e expiração de token
- Soft delete para integridade e rastreabilidade dos dados

---

## 📘 Visão Geral de alguns Endpoints da API

### Cliente
- Criar, Buscar por ID, Atualizar, Excluir
- Listar todos os clientes
- Buscar pets, vendas, serviços, consultas e agendamentos por cliente

### Pet
- Criar, Buscar por ID, Atualizar, Excluir
- Listar todos os pets

### Funcionário
- Criar, Buscar por ID, Atualizar, Excluir
- Listar funcionários com filtros

### Produto
- Criar, Buscar por ID, Atualizar, Excluir
- Atualizar estoque
- Listar todos os produtos

### Venda & ItemVenda
- Criar, Buscar por ID, Excluir
- Listar todas as vendas
- Gerenciar itens da venda

### Vacinação & Vacina
- Criar, Buscar por ID, Atualizar, Excluir
- Listar todas as vacinações e vacinas

### Serviços PetCare
- Criar, Buscar por ID ou Cliente, Excluir
- Listagem paginada de serviços

### Consultas
- Criar, Buscar por ID
- Listar por ID do cliente ou com filtros paginados

### Agendamento
- Agendar, Reagendar, Cancelar
- Alternar status
- Buscar por ID
- Listar por filtros ou cliente

---

## 📂 Regras de Negócio

- **Soft Delete**: Entidades marcadas como `DELETED` para preservar histórico
- **Auditoria**: Rastreia quem modificou o quê e quando
- **Controle de Status**: Entidades usam `EntityStatus` para gerenciar ciclo de vida
- **Tratamento de Erros**: Respostas estruturadas via `ErrorMessageDTO` e `ErrorResponseDTO`

---

## 🛠️ Atividades de Desenvolvimento

- ✅ Definição de regras de negócio e requisitos do sistema
- ✅ Escolha de arquitetura backend em camadas
- ✅ Desenvolvimento e refatoração do código backend
- ✅ Implementação de testes unitários e de integração
- ✅ Documentação da API com Swagger
- ✅ Conteinerização da aplicação e publicação no Docker Hub
- ✅ Modelagem do sistema com diagramas UML

---

## 🎯 Desafios Enfrentados

- 🔐 Implementação do Spring Security com JWT
- 🧱 Construção de arquitetura em camadas com modularização por domínio
- 🧠 Aplicação de princípios SOLID e padrões de projeto

---

## 📦 Infraestrutura

- Arquitetura modular baseada em domínios
- Serviços conteinerizados com Docker Compose
- Swagger UI para documentação interativa da API
- Diagramas UML para modelagem do sistema

---

## 📄 Como Começar

```bash
# Clonar o repositório
https://github.com/CaiquePirs/petland-backend-api.git

# Adicionar suas variáveis de ambiente
- criar arquivo .env

# Navegar até o diretório do projeto
cd petland-backend

# Compilar o projeto
mvn clean install

# Executar com Docker
docker-compose up
```

Access Swagger UI at: http://localhost:8080/swagger-ui.html

## 📌 Melhorias Futuras
- Adicionar pipeline de CI/CD
- Implementar dashboards por tipo de usuário
- Adicionar sistema de notificações para agendamentos

## 🐾 Autor
BookStore API foi desenvolvido por mim **Caique Pires**. Contribuições são bem vindas!
