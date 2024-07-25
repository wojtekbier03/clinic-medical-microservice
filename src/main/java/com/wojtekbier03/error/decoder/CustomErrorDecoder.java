package com.wojtekbier03.error.decoder;

import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class CustomErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 503) {
            return new RetryableException(
                    response.status(),
                    "Service is unavailable",
                    response.request().httpMethod(),
                    new Date(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(1)),
                    response.request()
            );
        }
        return defaultErrorDecoder.decode(methodKey, response);
    }
}
