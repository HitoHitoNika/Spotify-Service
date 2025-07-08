package de.hitohitonika.spotify.debtservice.control;

import de.hitohitonika.spotify.debtservice.entity.PaymentInfo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
public class PaymentInfoService {

    private final PaymentinfoRepository paymentinfoRepository;
    private final double costPerUser = 4.5;

    private final List<PaymentInfo> paymentInfoList = List.of(
            new PaymentInfo()
    );

    public PaymentInfoService(PaymentinfoRepository paymentinfoRepository) {
        this.paymentinfoRepository = paymentinfoRepository;
    }

    @Scheduled(cron = "0 0 1 1 * ?")
    @Transactional
    public void incrementDebt(){
        List<PaymentInfo> allUsers = paymentinfoRepository.findAll();
        allUsers.forEach(paymentInfo -> {
            paymentInfo.setDebt(paymentInfo.getDebt() + costPerUser);
            paymentinfoRepository.save(paymentInfo);
        });
    }

    @Transactional
    public PaymentInfo payDebt(String name, double amount) {
        Optional<PaymentInfo> paymentInfo = paymentinfoRepository.findByName(name);

        if (paymentInfo.isPresent()) {
            PaymentInfo info = paymentInfo.get();
            info.setDebt(info.getDebt() - amount);
            return paymentinfoRepository.save(info);
        }

        return null;
    }

    public List<PaymentInfo> getAllPaymentInfos() {
        return paymentinfoRepository.findAll();
    }

}
