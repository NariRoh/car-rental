package fsd01.carrental.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.param.ChargeCreateParams;
import fsd01.carrental.entity.Booking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StripeService {

    @Value("${STRIPE_SECRET_KEY}")
    private String API_PUBLIC_KEY;

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public StripeService() {
    }

    public void createCharge(Booking booking) {
        Stripe.apiKey = API_PUBLIC_KEY;

        // change total (in cents) for stripe api
        Long amount = (Double.valueOf(booking.getTotal()).longValue()) * 100;

        try {
            ChargeCreateParams params =
                    ChargeCreateParams.builder()
                            .setAmount(amount)
                            .setCurrency("cad")
                            .setDescription("Charge from car rental web app")
                            .setSource(booking.getStripeToken())
                            .setReceiptEmail(booking.getRenterEmail())
                            .build();

            Charge charge = Charge.create(params);
            String id = charge.getId();

            log.info(">>>>>> Requested charge to Stripe : {}", charge);
            log.info(">>>>>> Stripe charge id: {}", id);

            booking.setStripeChargeId(id);
        } catch (StripeException se) {
            se.printStackTrace();
        }
    }


}
