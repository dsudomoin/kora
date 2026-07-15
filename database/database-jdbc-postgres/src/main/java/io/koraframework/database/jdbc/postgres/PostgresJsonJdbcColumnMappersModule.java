package io.koraframework.database.jdbc.postgres;

import io.koraframework.database.jdbc.mapper.parameter.JdbcParameterColumnMapper;
import io.koraframework.database.jdbc.mapper.result.JdbcResultColumnMapper;
import io.koraframework.database.jdbc.mapper.result.JdbcRowMapper;
import io.koraframework.database.jdbc.postgres.mapper.parameter.PostgresJsonParameterColumnMapper;
import io.koraframework.database.jdbc.postgres.mapper.result.JsonResultColumnMapper;
import io.koraframework.json.common.JsonReader;
import io.koraframework.json.common.JsonWriter;
import io.koraframework.json.common.annotation.Json;

// @Json ↔ PostgreSQL json мост. json-common подключён как compileOnly, поэтому модуль НЕ подмешивается
// в PostgresJdbcDatabaseModule — его добавляют в @KoraApp явно те, кто использует @Json.
// Запись через PGobject(type=jsonb): по эмпирике setString в json/jsonb колонку падает (нужен PGobject); jsonb пишется в любую JSON-колонку и работает с jsonb-операторами.
public interface PostgresJsonJdbcColumnMappersModule {

    @Json
    default <T> JdbcResultColumnMapper<T> jsonColumnJdbcResultColumnMapper(JsonReader<T> jsonReader) {
        return new JsonResultColumnMapper<>(jsonReader);
    }

    @Json
    default <T> JdbcRowMapper<T> jsonColumnJdbcRowMapper(@Json JdbcResultColumnMapper<T> resultColumnMapper) {
        return row -> resultColumnMapper.apply(row, 1);
    }

    @Json
    default <T> JdbcParameterColumnMapper<T> jsonColumnJdbcParameterColumnMapper(JsonWriter<T> jsonWriter) {
        return new PostgresJsonParameterColumnMapper<>(jsonWriter);
    }
}
