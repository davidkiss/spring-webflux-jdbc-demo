package com.kaviddiss.webflux;

import org.davidmoten.rx.jdbc.Database;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@SpringBootApplication
public class WebfluxJdbcDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebfluxJdbcDemoApplication.class, args);
	}

    @Bean
    public CommandLineRunner initDB(Database db) {
        return args -> {
            db.update("CREATE TABLE IF NOT EXISTS world (\n" +
                  "   id INT NOT NULL,\n" +
                  "   randomNumber INT NOT NULL\n" +
                  ")");

            db.update("CREATE TABLE IF NOT EXISTS fortune (\n" +
                  "   id INT NOT NULL,\n" +
                  "   message VARCHAR(255) NOT NULL\n" +
                  ")");

            // insert 10,000 worlds, if table is empty:
            db.select("SELECT * FROM world")
                .count().filter(count -> count == 0)
                .subscribe(consumer -> Flux.range(0, 10000)
                    .flatMap(i -> Mono.from(db.update("INSERT INTO world VALUES (?, ?)")
                                    .parameters(i, i)
                                    .counts()))
                    .subscribe())
                ;

            // insert 100 fortunes, if table is empty:
            db.select("SELECT * FROM fortune")
                .count().filter(count -> count == 0)
                .subscribe(consumer -> Flux.range(0, 100)
                    .flatMap(i -> Mono.from(db.update("INSERT INTO fortune VALUES (?, ?)")
                                    .parameters(i, UUID.randomUUID().toString())
                                    .counts()))
                    .subscribe())
                ;
    };
  }
}
