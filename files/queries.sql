show tables;
-- comando que o debezium executa no mysql, se por caso der erro, quer dizer que a versao do mysql nao suporta
-- estamos usando a versao 8.0 do mysql
show master status;
desc categories;

select
*
from categories c
;

insert into categories values(
	"qualquercoisa", 'qualquercoisa', null, 1, now(), now(), null
)

-- alterando as configuracoes para podermos acessar o root pela senha, sem problemas
-- sera acessado pelo endpoint do debezium
alter user 'root'@'%' identified with mysql_native_password by '123456';
-- alter user 'root'@'%' identified with caching_sha2_password by '123456';