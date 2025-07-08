package de.hitohitonika.spotify.debtservice.control;

import de.hitohitonika.spotify.debtservice.entity.PaymentInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentinfoRepository extends JpaRepository<PaymentInfo, Long> {
    Optional<PaymentInfo> findByName(String name);
}
