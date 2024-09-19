package estudos.SpringSecurity.config.check;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

@Component
public class HealthCheck implements HealthIndicator {
    //metrica personalizada
    //pode ser feito qualquer teste que seja necessario para garantir o funcionamento correto da aplicacao
    @Override
    public Health health() {
        try {
            InetAddress adress = InetAddress.getByName("localhost");
            if(adress.isReachable(10000))
                return Health.up().build();
        } catch (Exception e) {
            return Health.down().withDetail("Motivo", e.getMessage()).build();
        }
        return Health.down().withDetail("Motivo", "motivoDesconhecido").build();
    }

}
