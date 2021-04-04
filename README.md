## Manga Progress API
API REST de gerenciamento de mangas

### Observação
O projeto foi refatorado, para armazenar dados de mangas. Projeto
original realizava o gerenciamento de estoque de cerveja.


### Execução

* Para executar o projeto no terminal, digite o seguinte comando:
```shell script
mvn spring-boot:run 
```

* Para executar a suíte de testes:

```shell script
mvn clean test
```

### Exemplo usando o CURL
#### GET
*http://localhost:8080/api/v1/mangas/*
```
curl http://localhost:8080/api/v1/mangas
```
*http://localhost:8080/api/v1/mangas/{name}*
```
curl http://localhost:8080/api/v1/mangas/Bleach
```

#### POST
*http://localhost:8080/api/v1/mangas/*
```
curl -d '{"name":"Bleach", "author":"Cubo", "chapters": 500, "genre": "OTHER"}' -H "Content-Type: application/json" -X POST http://localhost:8080/api/v1/mangas/
```

#### PATCH
*http://localhost:8080/api/v1/mangas/{id}*
```
curl -d '{"name":"Bleach", "author":"Tite Cubo", "chapters": 600, "genre": "SHONEN"}' -H "Content-Type: application/json" -X PATCH http://localhost:8080/api/v1/mangas/1
```

#### DELETE
*http://localhost:8080/api/v1/mangas/{id}*
```
curl -X DELETE http://localhost:8080/api/v1/mangas/1 
```

### Ferramentas: 
* Curl 7.71
* Java 8.
* Maven 3.6.3 ou versões superiores.
* Controle de versão GIT.
