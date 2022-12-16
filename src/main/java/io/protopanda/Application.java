package io.protopanda;

import io.protopanda.model.Person;
import io.protopanda.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.cassandra.core.CassandraOperations;

import java.time.Instant;
import java.util.List;
import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@SpringBootApplication
public class Application implements ApplicationRunner {

    public static void main(String[] args) {

        SpringApplication.exit(SpringApplication.run(Application.class, args));
    }

    private final PersonRepository personRepository;

    private final CassandraOperations cassandraTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        List<Person> personList = cassandraTemplate.select("SELECT * FROM home-lab.person", Person.class);
        log.info("..... Cassandra Template Response .....");

        for (Person person : personList) {
            log.info("Template Item: " + person);
        }
        Person person = Person.builder()
                .id(new Random().nextInt())
                .firstName("John")
                .lastName("Doe")
                .dateTime(Instant.now())
                .build();

        personRepository.save(person);

        Iterable<Person> personIterable = personRepository.findAll();
        log.info("..... Crud Repository Response .....");

        for (Person temp : personIterable) {
            log.info("Repository Item: " + temp);
        }
    }
}
