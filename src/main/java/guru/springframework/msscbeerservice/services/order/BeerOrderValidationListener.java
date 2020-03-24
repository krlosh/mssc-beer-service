package guru.springframework.msscbeerservice.services.order;

import guru.sfg.brewery.model.events.ValidateOrderRequest;
import guru.sfg.brewery.model.events.ValidateOrderResult;
import guru.springframework.msscbeerservice.config.JmsConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BeerOrderValidationListener {

    private final BeerOrderValidator validator;
    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfiguration.VALIDATE_ORDER_QUEUE)
    public void listen(ValidateOrderRequest validateOrderRequest) {
        Boolean isValid = this.validator.validateOrder(validateOrderRequest.getBeerOrder());

        jmsTemplate.convertAndSend(JmsConfiguration.VALIDATE_ORDER_RESPONSE_QUEUE,
                ValidateOrderResult.builder()
                        .id(validateOrderRequest.getBeerOrder().getId())
                        .isValid(isValid)
                .build()
        );
    }
}
