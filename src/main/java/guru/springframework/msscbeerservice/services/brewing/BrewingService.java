package guru.springframework.msscbeerservice.services.brewing;

import guru.springframework.msscbeerservice.config.JmsConfiguration;
import guru.springframework.msscbeerservice.domain.Beer;
import guru.sfg.brewery.model.events.BrewBeerEvent;
import guru.springframework.msscbeerservice.repositories.BeerRepository;
import guru.springframework.msscbeerservice.services.inventory.BeerInventoryService;
import guru.springframework.msscbeerservice.web.mappers.BeerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class BrewingService {

    private final BeerRepository beerRepository;
    private final BeerInventoryService beerInventoryService;
    private final JmsTemplate jmsTemplate;
    private final BeerMapper beerMapper;

    @Scheduled(fixedRate = 5000)
    public void checkForLowInventory() {

        List<Beer> beers = this.beerRepository.findAll();
        beers.forEach(beer -> {
            Integer invQOH = this.beerInventoryService.getOnHandInventory(beer.getId());

            log.debug("Min on hand is: {} ", beer.getMinOnHand());
            log.debug("Inventory is: {}", invQOH);
            if (beer.getMinOnHand() >= invQOH) {
                jmsTemplate.convertAndSend(JmsConfiguration.BREWING_REQUEST_QUEUE, new BrewBeerEvent(this.beerMapper.beerToBeerDto(beer)));
            }
        });
    }
}
