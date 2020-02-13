package guru.sfg.common.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import guru.springframework.msscbeerservice.web.model.BeerDto;

public class NewInventoryEvent extends BeerEvent {

    @JsonCreator
    public NewInventoryEvent(BeerDto beerDto) {
        super(beerDto);
    }
}
