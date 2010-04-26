package com.advertiseserver.service;

import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;

import com.advertiseserver.domain.Ad;

public interface AdService {
	@Get
    public Ad retrieve();

    @Put
    public void store(Ad ad);

    @Delete
    public void remove();
}
