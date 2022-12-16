package io.protopanda.model;

import lombok.Builder;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;
import java.time.Instant;

@Builder
@Table("person")
public class Person implements Serializable {

    @PrimaryKeyColumn(name = "id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private Integer id;

    @PrimaryKeyColumn(name = "first_name", ordinal = 2, type = PrimaryKeyType.CLUSTERED)
    private String firstName;

    @PrimaryKeyColumn(name = "last_name", ordinal = 3, type = PrimaryKeyType.CLUSTERED)
    private String lastName;

    @PrimaryKeyColumn(name = "dateTime", ordinal = 4, type = PrimaryKeyType.CLUSTERED)
    private Instant dateTime;

}
