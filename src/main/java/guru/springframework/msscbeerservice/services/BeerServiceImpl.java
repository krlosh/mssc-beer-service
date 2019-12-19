package guru.springframework.msscbeerservice.services;

import guru.springframework.msscbeerservice.domain.Beer;
import guru.springframework.msscbeerservice.repositories.BeerRepository;
import guru.springframework.msscbeerservice.web.controller.NotFoundException;
import guru.springframework.msscbeerservice.web.mappers.BeerMapper;
import guru.springframework.msscbeerservice.web.model.BeerDto;
import guru.springframework.msscbeerservice.web.model.BeerPageList;
import guru.springframework.msscbeerservice.web.model.BeerStyleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BeerServiceImpl implements BeerService {

    private final BeerRepository repository;
    private final BeerMapper mapper;

    @Override
    public BeerDto getById(UUID beerId, Boolean showInventoryOnHand) {
        if(showInventoryOnHand){
            return this.mapper.beerToBeerDtoWithInventory(
                    this.repository.findById(beerId).orElseThrow(NotFoundException::new)
            );
        }
        else {
            return this.mapper.beerToBeerDto(
                    this.repository.findById(beerId).orElseThrow(NotFoundException::new)
            );
        }
    }

    @Override
    public BeerDto saveNewBeer(BeerDto beerDto) {
        return this.mapper.beerToBeerDto(
                this.repository.save(this.mapper.beerDtoToBeer(beerDto))
        );
    }

    @Override
    public BeerDto updateBeer(UUID beerId, BeerDto beerDto) {
        Beer beer = this.repository.findById(beerId).orElseThrow(NotFoundException::new);

        beer.setBeerName(beerDto.getBeerName());
        beer.setBeerStyle(beerDto.getBeerStyle().name());
        beer.setPrice(beerDto.getPrice());
        beer.setUpc(beerDto.getUpc());

        return this.mapper.beerToBeerDto(beer);
    }

    @Override
    public BeerPageList listBeers(String beerName, BeerStyleEnum beerStyle, PageRequest pageRequest, Boolean showInventoryOnHand) {
        Page<Beer> page;
        if (!StringUtils.isEmpty(beerName) && !StringUtils.isEmpty(beerStyle)) {
            page = this.repository.findAllByBeerNameAndBeerStyle(beerName, beerStyle, pageRequest);
        }
        else if (!StringUtils.isEmpty(beerName) && StringUtils.isEmpty(beerStyle)) {
            page = this.repository.findAllByBeerName(beerName, pageRequest);
        }
        else if(StringUtils.isEmpty(beerName) && !StringUtils.isEmpty(beerStyle)) {
            page = this.repository.findAllByBeerStyle(beerStyle, pageRequest);
        }
        else {
            page = this.repository.findAll(pageRequest);
        }
        if(showInventoryOnHand){
            return new BeerPageList(page
                    .getContent()
                    .stream()
                    .map(this.mapper::beerToBeerDtoWithInventory)
                    .collect(Collectors.toList()),
                    PageRequest.of(page.getPageable().getPageNumber(),
                            page.getPageable().getPageSize()),
                    page.getTotalElements()
            );
        }
        else {
            return new BeerPageList(page
                    .getContent()
                    .stream()
                    .map(this.mapper::beerToBeerDto)
                    .collect(Collectors.toList()),
                    PageRequest.of(page.getPageable().getPageNumber(),
                            page.getPageable().getPageSize()),
                    page.getTotalElements()
            );
        }
    }
}
