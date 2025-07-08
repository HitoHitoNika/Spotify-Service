package de.hitohitonika.spotify.debtservice.config;

import de.hitohitonika.spotify.debtservice.control.PaymentinfoRepository;
import de.hitohitonika.spotify.debtservice.entity.PaymentInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class DataInitializer {


    @Bean
    public CommandLineRunner loadInitialData(PaymentinfoRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                log.info("Datenbank ist leer. Initialisiere mit Startwerten...");

                repository.save(new PaymentInfo("Hamed", 0));
                repository.save(new PaymentInfo("Lucas", -27));
                repository.save(new PaymentInfo("Steven", 9));

                log.info("Initialisierung abgeschlossen. Anzahl der Einträge: {}", repository.count());
            } else {
                log.info("Datenbank enthält bereits Daten. Keine Initialisierung notwendig.");
            }
        };
    }
}