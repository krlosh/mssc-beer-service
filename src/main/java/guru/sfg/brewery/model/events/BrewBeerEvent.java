package guru.sfg.brewery.model.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import guru.sfg.brewery.model.BeerDto;

public class BrewBeerEvent extends BeerEvent {
    @JsonCreator
    public BrewBeerEvent(BeerDto beerDto) {
        super(beerDto);
    }
}
