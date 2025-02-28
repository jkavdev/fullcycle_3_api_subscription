<center>
  <p align="center">
    <img src="https://user-images.githubusercontent.com/20674439/158480514-a529b310-bc19-46a5-ac95-fddcfa4776ee.png" width="150"/>&nbsp;
    <img src="https://icon-library.com/images/java-icon-png/java-icon-png-15.jpg"  width="150" />
  </p>  
  <h1 align="center">🚀 Microserviço: Catálogo de Vídeos com Java</h1>
  <p align="center">
    Microserviço referente ao backend do Catálogo de Vídeos<br />
    Utilizando Clean Architecture, TDD e as boas práticas atuais de mercado
  </p>
</center>
<br />

## Ferramentas necessárias

- JDK 17
- IDE de sua preferência
- Docker

## Rodando no windows 11
tem que habilitar o SUDO no windows para que o run.sh funcione

sera dara erro de permissao

````shell
$ ./run.sh
Error response from daemon: network with name elastic already exists
es01
Sudo is disabled on this machine. To enable it, go to the Developer Settings page in the Settings app
mkdir: cannot change permissions of '.docker': Permission denied
mkdir: cannot change permissions of '.docker/es01': Permission denied
validating C:\Users\jkavdev\Repo\github\jkavdev\fullcycle_3_api_catalogo_videos\sandbox\elk\docker-compose.yml: volumes.es01 Additional property elastic is not allowed

````

## Kafka Connect

### Debezium

Creating the debezium mysql user:
```sql
CREATE USER IF NOT EXISTS 'debezium'@'%' IDENTIFIED WITH mysql_native_password BY 'debezium';
GRANT SELECT, RELOAD, SHOW DATABASES, REPLICATION SLAVE, REPLICATION CLIENT ON *.* TO 'debezium'@'%';
FLUSH PRIVILEGES;
```

Creating the kafka connect connector for the Debezium:
```shell
curl --location --request PUT 'http://localhost:8083/connectors/admin-mysql-cdc/config' \
--header 'Content-Type: application/json' \
--data '{
    "topic.creation.default.replication.factor": 1,
    "topic.creation.default.partitions": 1,
    "connector.class": "io.debezium.connector.mysql.MySqlConnector",
    "tasks.max": "1",
    "key.converter": "org.apache.kafka.connect.json.JsonConverter",
    "key.converter.schemas.enable": "true",
    "value.converter": "org.apache.kafka.connect.json.JsonConverter",
    "value.converter.schemas.enable": "true",
    "database.hostname": "mysql",
    "database.port": "3306",
    "database.user": "debezium",
    "database.password": "debezium",
    "database.server.id": "10000",
    "database.server.name": "adm_videos_mysql",
    "database.allowPublicKeyRetrieval": "true",
    "database.include.list": "adm_videos",
    "table.include.list": "adm_videos.categories,adm_videos.cast_members,adm_videos.genres,adm_videos.videos",
    "database.history.kafka.bootstrap.servers": "kafka:9092",
    "database.history.kafka.topic": "adm_videos.dbhistory",
    "include.schema.changes": "false",
    "schema.enable": "false",
    "topic.creation.adm_videos.include": "adm_videos_mysql\\.adm_videos\\.*",
    "topic.creation.adm_videos.partitions": 1
}'
```

## Observacoes
devido as erros do elasticsearch nao serem tao amigaveis, quando acontecer o seguinte erro
a causa pode ser o tipo

````java
[es/search] failed: [search_phase_execution_exception] all shards failed
co.elastic.clients.elasticsearch._types.ElasticsearchException: [es/search] failed: [search_phase_execution_exception] all shards failed
	at app//co.elastic.clients.transport.rest_client.RestClientTransport.getHighLevelResponse(RestClientTransport.java:334)
	at app//co.elastic.clients.transport.rest_client.RestClientTransport.performRequest(RestClientTransport.java:154)
	at app//co.elastic.clients.elasticsearch.ElasticsearchClient.search(ElasticsearchClient.java:1882)
	at app//org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate.lambda$doSearch$14(ElasticsearchTemplate.java:341)
	at app//org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate.execute(ElasticsearchTemplate.java:623)
	at app//org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate.doSearch(ElasticsearchTemplate.java:341)
	at app//org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate.search(ElasticsearchTemplate.java:334)
	at app//org.springframework.data.elasticsearch.core.AbstractElasticsearchTemplate.search(AbstractElasticsearchTemplate.java:492)
	at app//br.com.jkavdev.fullcycle.catalogo.infrastructure.category.CategoryElasticsearchGateway.findAll(CategoryElasticsearchGateway.java:75)
	at app//br.com.jkavdev.fullcycle.catalogo.infrastructure.category.CategoryElasticsearchGatewayTest.givenValidTerm_whenCallsFindAll_shouldReturnElementsFiltered(CategoryElasticsearchGatewayTest.java:158)
	at java.base@17.0.13/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
````

# Possiveis problemas ao rodar a aplicacao

* caso esteja com problemas em rodar a aplicacao e aplicacao nao consegue se conectar no kafka local
eh porque tem que mapear o `localhost` para esse `kafka` no `C:\Windows\System32\drivers\etc\hosts` do windows

```manifest
127.0.0.1 kafka
```

TODOs: fazer o mesmo que foi feito para genero e video, no qual temos um input e output 
no with, recebendo apenas o valor raw, e no construtor transformando para o objeto final

* quando tiver dando problema de erros de permissao negada no github actions, em alguma parte que seja de executar algum comando

pode ser que a gradlew nao tenha sido gerado, para isso rodar o comando abaixo

```shell
gradle wrapper
```

# TODOs

* implementar outros testes unitarios mencionados na aula 0704