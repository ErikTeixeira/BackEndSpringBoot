# Projeto API de Carros (CarroProjeto)

API RESTful desenvolvida em Spring Boot para gerenciamento de carros e suas respectivas marcas. O projeto demonstra funcionalidades CRUD, validações, relacionamentos entre entidades e testes unitários.

## Funcionalidades

*   **Gerenciamento de Carros:**
    *   Cadastrar novos carros (associando a uma marca existente ou criando uma nova marca em cascata).
    *   Listar todos os carros cadastrados.
    *   Buscar um carro específico pelo seu ID.
    *   Buscar carros pelo nome (busca case-insensitive e parcial).
    *   Atualizar dados de um carro existente.
    *   Remover um carro pelo seu ID.
*   **Validações:**
    *   Campos obrigatórios para nome e modelo do carro.
    *   Tamanho mínimo e máximo para modelo do carro e nome da marca.
    *   Nome e modelo do carro não podem ser iguais.
    *   Campo obrigatório para nome da marca.
*   **Relacionamento:**
    *   Um `Carro` pertence a uma `Marca` (`ManyToOne`).
    *   Operações em `Carro` podem afetar `Marca` devido ao `CascadeType.ALL`.
*   **Testes:**
    *   Testes unitários para `CarroService` e `CarroController` utilizando JUnit 5 e Mockito.
    *   Cobertura de código com Jacoco.
*   **Documentação da API:**
    *   Disponível via Swagger UI.

## Tecnologias Utilizadas

*   Java 21
*   Spring Boot 3.4.5
    *   Spring Web
    *   Spring Data JPA
    *   Spring Validation
*   MySQL (Banco de Dados relacional)
*   H2 (Banco de Dados em memória para testes)
*   Lombok
*   Maven (Gerenciador de dependências e build)
*   JUnit 5 & Mockito (Testes)
*   Jacoco (Cobertura de testes)
*   Docker & Docker Compose (Containerização)
*   Springdoc OpenAPI (Swagger UI para documentação da API)

## Pré-requisitos

*   JDK 21 ou superior
*   Maven 3.8.x ou superior
*   Docker e Docker Compose
*   Postman (ou similar) para testar os endpoints da API
*   IDE de sua preferência (IntelliJ IDEA, VSCode com extensões Java, Eclipse, etc.)

## Como Rodar o Projeto

Existem duas formas principais de rodar a aplicação:

### 1. Usando Docker Compose (Recomendado)

Esta é a forma mais simples de subir a aplicação junto com o banco de dados MySQL, sem necessidade de configurar o MySQL localmente.

1.  **Clone o repositório:**
    ```bash
    git clone <url-do-seu-repositorio>
    cd CarroProjeto
    ```

2.  **Construa e suba os containers:**
    No diretório raiz do projeto (onde está o `docker-compose.yml`), execute:
    ```bash
    docker-compose up --build -d
    ```
    O `-d` executa os containers em background.
    O `--build` força a reconstrução da imagem da aplicação caso haja alterações no `Dockerfile` ou no código.

3.  **Acesse a aplicação:**
    *   A API estará disponível em: `http://localhost:8080/api`
    *   A documentação Swagger UI estará disponível em: `http://localhost:8080/swagger-ui.html`

4.  **Para parar os containers:**
    ```bash
    docker-compose down
    ```

### 2. Rodando Localmente (Sem Docker para a Aplicação)

Se preferir rodar a aplicação Spring Boot diretamente na sua máquina (fora de um container Docker), mas ainda usando o MySQL do Docker Compose:

1.  **Clone o repositório (se ainda não o fez).**

2.  **Suba apenas o container do banco de dados MySQL:**
    No diretório raiz do projeto, execute:
    ```bash
    docker-compose up -d mysql-db
    ```
    Isso iniciará apenas o serviço `mysql-db` definido no `docker-compose.yml`.
    O banco de dados `carros` estará acessível em `localhost:3307` (conforme mapeado no `docker-compose.yml`), com usuário `root` e senha `root`.

3.  **Configure a aplicação Spring Boot:**
    Seu `docker-compose.yml` já define as variáveis de ambiente para a conexão com o banco de dados quando a aplicação roda dentro do container `meu-app-spring`. Se você for rodar a aplicação localmente (fora do container), precisa garantir que essas configurações sejam aplicadas.
    Você pode configurar isso no arquivo `src/main/resources/application.properties` (crie-o se não existir):
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3307/carros
    spring.datasource.username=root
    spring.datasource.password=root
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true

    # Configuração para Swagger/OpenAPI (opcional, mas bom ter)
    springdoc.api-docs.path=/v3/api-docs
    springdoc.swagger-ui.path=/swagger-ui.html
    ```

4.  **Construa e execute a aplicação com Maven:**
    No diretório raiz do projeto, execute:
    ```bash
    mvn clean install
    mvn spring-boot:run
    ```
    Ou execute a classe principal `CarroProjetoApplication.java` a partir da sua IDE.

5.  **Acesse a aplicação:**
    *   A API estará disponível em: `http://localhost:8080/api`
    *   A documentação Swagger UI estará disponível em: `http://localhost:8080/swagger-ui.html`

## Testando a API com Postman

A base URL para os endpoints é `http://localhost:8080/api/carro`.

### 1. Cadastrar um Carro (`POST /save`)

*   **Método:** `POST`
*   **URL:** `http://localhost:8080/api/carro/save`
*   **Headers:** `Content-Type: application/json`
*   **Body (raw - JSON):**

    ```json
    {
        "nome": "Fusca",
        "modelo": "Itamar",
        "marca": {
            "nome": "Volkswagen",
            "descricao": "Fabricante Alemã de Automóveis"
        }
    }
    ```
    *Observação: Como `Carro.marca` tem `CascadeType.ALL`, ao salvar um carro com uma nova marca (sem ID na marca), a marca também será criada. Se você fornecer um `id` para a marca no JSON acima, ele tentará associar a uma marca existente com aquele ID.*

    **Exemplo com marca existente (supondo que uma marca com ID 1 já exista):**
    ```json
    {
        "nome": "Gol",
        "modelo": "CLI",
        "marca": {
            "id": 1
        }
    }
    ```

*   **Resposta Esperada (Status 200 OK):**
    ```
    Carro salvo com sucesso
    ```

### 2. Listar Todos os Carros (`GET /findAll`)

*   **Método:** `GET`
*   **URL:** `http://localhost:8080/api/carro/findAll`
*   **Resposta Esperada (Status 200 OK):**
    Uma lista de carros em formato JSON.
    ```json
    [
        {
            "id": 1,
            "nome": "Fusca",
            "modelo": "Itamar",
            "marca": {
                "id": 1,
                "nome": "Volkswagen",
                "descricao": "Fabricante Alemã de Automóveis"
            }
        },
        {
            "id": 2,
            "nome": "Civic",
            "modelo": "EXS",
            "marca": {
                "id": 2,
                "nome": "Honda",
                "descricao": "Fabricante Japonesa"
            }
        }
    ]
    ```

### 3. Buscar Carro por ID (`GET /findById/{id}`)

*   **Método:** `GET`
*   **URL:** `http://localhost:8080/api/carro/findById/1` (substitua `1` pelo ID desejado)
*   **Resposta Esperada (Status 200 OK):**
    O carro com o ID especificado.
    ```json
    {
        "id": 1,
        "nome": "Fusca",
        "modelo": "Itamar",
        "marca": {
            "id": 1,
            "nome": "Volkswagen",
            "descricao": "Fabricante Alemã de Automóveis"
        }
    }
    ```
    Se o carro não for encontrado, o corpo da resposta será `null` com status `200 OK` (conforme implementação atual do controller).

### 4. Buscar Carros por Nome (`GET /findByNome`)

*   **Método:** `GET`
*   **URL:** `http://localhost:8080/api/carro/findByNome?nome=Fus` (substitua `Fus` pelo termo de busca)
*   **Resposta Esperada (Status 200 OK):**
    Uma lista de carros cujo nome contém o termo de busca (case-insensitive).
    ```json
    [
        {
            "id": 1,
            "nome": "Fusca",
            "modelo": "Itamar",
            "marca": {
                "id": 1,
                "nome": "Volkswagen",
                "descricao": "Fabricante Alemã de Automóveis"
            }
        }
    ]
    ```

### 5. Atualizar um Carro (`PUT /update/{id}`)

*   **Método:** `PUT`
*   **URL:** `http://localhost:8080/api/carro/update/1` (substitua `1` pelo ID do carro a ser atualizado)
*   **Headers:** `Content-Type: application/json`
*   **Body (raw - JSON):**
  * Mantenha o ID da marca se for a mesma, ou altere se necessário
      ```json
      {
          "nome": "Fusca",
          "modelo": "GL",
          "marca": {
              "id": 1, 
              "nome": "Volkswagen",
              "descricao": "Das Auto"
          }
      }
      ```
*   **Resposta Esperada (Status 200 OK):**
    ```
    Carro atualizado com sucesso
    ```

### 6. Deletar um Carro (`DELETE /deleteById/{id}`)

*   **Método:** `DELETE`
*   **URL:** `http://localhost:8080/api/carro/deleteById/1` (substitua `1` pelo ID do carro a ser deletado)
*   **Resposta Esperada (Status 200 OK):**
    ```
    Carro deletado com sucesso
    ```

## Rodando os Testes

Para executar os testes unitários e gerar o relatório de cobertura:

```bash
mvn test