package com.project.core.service;

import lombok.Getter;
import lombok.Setter;
import org.osgi.service.component.annotations.Component;

import java.util.List;

@Component(service = CountryServiceImpl.class, immediate = true)
public class CountryServiceImpl {

    @Getter
    @Setter
    public List<String> listOfCountry;

}
