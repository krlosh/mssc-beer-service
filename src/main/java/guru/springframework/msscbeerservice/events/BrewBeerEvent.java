package guru.springframework.msscbeerservice.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import guru.springframework.msscbeerservice.web.model.BeerDto;

public class BrewBeerEvent extends BeerEvent {
    @JsonCreator
    public BrewBeerEvent(BeerDto beerDto) {
        super(beerDto);
    }
}
