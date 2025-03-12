package br.com.jkavdev.fullcycle.subscription.infrastructure.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * abstracao da camada de extracao de dados do spring jdpb, no qual necessitamos ate o momento
 * apenas do resultset
 */
public interface RowMap<T> {

    T mapRow(ResultSet rs) throws SQLException;

}
