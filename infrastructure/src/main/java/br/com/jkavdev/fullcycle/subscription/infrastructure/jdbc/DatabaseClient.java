package br.com.jkavdev.fullcycle.subscription.infrastructure.jdbc;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * para remover o acoplamento do jdbc das repository, podemos criar uma abstracao que provera
 * os metodos comuns ate o momento de acesso ao banco de dados
 */
public interface DatabaseClient {

    <T> Optional<T> queryOne(String sql, Map<String, Object> params, RowMap<T> mapper);

    <T> List<T> query(String sql, RowMap<T> mapper);

    <T> List<T> query(String sql, Map<String, Object> params, RowMap<T> mapper);

    int update(String sql, Map<String, Object> params);

}
