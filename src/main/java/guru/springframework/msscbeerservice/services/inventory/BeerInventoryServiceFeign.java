package guru.springframework.msscbeerservice.services.inventory;

import guru.springframework.msscbeerservice.services.inventory.model.BeerInventoryDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Profile("local-discovery")
@Slf4j
@RequiredArgsConstructor
@Service
public class BeerInventoryServiceFeign  implements BeerInventoryService{

    private final InventoryServiceFeignClient inventoryServiceFeignClient;

    @Override
    public Integer getOnHandInventory(UUID beerId) {

        ResponseEntity<List<BeerInventoryDto>> responseEntity = inventoryServiceFeignClient.getOnhandInventory(beerId);
        Integer onHand = Objects.requireNonNull(responseEntity.getBody())
                .stream()
                .mapToInt(BeerInventoryDto::getQuantityOnHand)
                .sum();
        log.debug("BeerId: {} On hand is:{}", beerId, onHand);
        return onHand;
    }
}


