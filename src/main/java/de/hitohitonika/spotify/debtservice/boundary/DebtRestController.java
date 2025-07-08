package de.hitohitonika.spotify.debtservice.boundary;

import de.hitohitonika.spotify.debtservice.control.PaymentInfoService;
import de.hitohitonika.spotify.debtservice.entity.PaymentInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/paymentinfo")
public class DebtRestController {
    private PaymentInfoService paymentInfoService;

    public DebtRestController(PaymentInfoService paymentInfoService) {
        this.paymentInfoService = paymentInfoService;
    }

    @GetMapping()
    public ResponseEntity<List<PaymentInfo>> getPaymentInfos() {
        return ResponseEntity.ok(paymentInfoService.getAllPaymentInfos());
    }

    @GetMapping("/user")
    public ResponseEntity<PaymentInfo> getPaymentInfoByName(@RequestParam String name) {
        var list = paymentInfoService.getAllPaymentInfos();
        var specificInfo = list.stream()
                .filter(info -> info.getName().equals(name))
                .findFirst();
        return specificInfo.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/pay")
    public ResponseEntity<PaymentInfo> payDebt(@RequestParam String name, @RequestParam double amount) {
        if(name == null || amount == 0) {
            return ResponseEntity.badRequest().build();
        }
        var info = this.paymentInfoService.payDebt(name, amount);
        if (info == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(info);
    }
}
