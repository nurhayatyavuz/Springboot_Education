package com.exampla.surucu.service;


import com.exampla.surucu.CarResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class SurucuService {
    public SurucuService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private final RestTemplate restTemplate;



    public CarResponse getCar(@PathVariable Integer id) {
        CarResponse carResponse = restTemplate.getForObject("http://localhost:8081/api/car/v1/{id}"
                , CarResponse.class, id);
        return carResponse;
    }

    //service sınıfında pathvariable olmaz parantez içine id eklendi o nedenle
    public ResponseEntity<?> getCarDetail(@PathVariable Integer id) {
        try {
//            ResponseEntity<CarResponse> carResponseEntity = restTemplate.getForEntity("http://localhost:8081/api/car/v1/{id}"
//                    , CarResponse.class, id);
//            return carResponseEntity;
            URI uri = UriComponentsBuilder
                    .fromUriString("http://localhost:8081/api/car/v1")
                    .buildAndExpand(id)
                    .toUri();
            ResponseEntity<CarResponse> responseEntity = restTemplate.getForEntity(uri, CarResponse.class);
            return responseEntity;

        } catch (HttpClientErrorException e) {
            //404 not found olarak
            String responseBody = e.getResponseBodyAsString();
            HttpStatusCode statusCode = e.getStatusCode();
            return ResponseEntity.status(statusCode).body(responseBody);
        } catch (HttpServerErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body("Sunucu Hatası: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("bilinmeyen hata: " + e.getMessage());
        }
    }

    public ResponseEntity<?> getCarByModel(String model) {
        try {
//            ResponseEntity<CarResponse> responseEntity = restTemplate.getForEntity("http://localhost:8081/api/car/v1/carmodel?model={model}"
//                    , CarResponse.class, model);//requestparam bu şekilde yazıldığı için bölye ekledik
//            return responseEntity;

            URI uri=UriComponentsBuilder
                    .fromUriString("http://localhost:8081/api/car/v1/carmodel")
                    .queryParam("model",model)
                    .build()
                    .toUri();

            ResponseEntity<CarResponse> responseEntity = restTemplate.getForEntity(uri, CarResponse.class);
            return responseEntity;

        } catch (HttpClientErrorException e) {
            String responseBody = e.getResponseBodyAsString();
            HttpStatusCode statusCode = e.getStatusCode();
            return ResponseEntity.status(statusCode).body(responseBody);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("bilinmeyen hata: " + e.getMessage());
        }
    }

    //path variable exmp
    public int getTop(int sayi1,int sayi2){
        //with map
        Map<String,Integer> map= new HashMap<>();
        map.put("sayi1",sayi1);
        map.put("sayi2",sayi2);
        //path variable
        Integer sonuc = restTemplate.getForObject(
                "http://localhost:8081/api/car/v1/get-sum/{sayi1}/{sayi2}",
                Integer.class,
                //sayi1, sayi2
                map);

        return sonuc;
    }

    // RequestParam
    public int getToplam2(int sayia, int sayib, int sayic) {
        Integer sonuc = restTemplate.getForObject(
                "http://localhost:8081/get-toplam-deger?sayiA={sayia}&sayiB={sayib}&sayiC={sayic}",
                Integer.class,
                sayia, sayib, sayic
        );
        return sonuc;
    }

    //path variable-with URİComponentBuilder
    public int getToplamURI(int sayi1,int sayi2){
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:8081/api/car/v1/get-sum/{sayi1}/{sayi2}")
                .buildAndExpand(sayi1, sayi2)
                .toUri();
        Integer sonuc = restTemplate.getForObject(uri, Integer.class);
        return sonuc;
    }

    //request param with UriComponentBuilder
    public int getToplam3(int sayia,int sayib,int sayic){
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:8081/api/car/v1/get-toplam-deger")
                .queryParam("sayiA", sayia)
                .queryParam("sayiB", sayib)
                .queryParam("sayiC", sayic)
                .build()
                .toUri();

        Integer sonuc = restTemplate.getForObject(uri, Integer.class);
        return sonuc;
    }

//    public static List<CarResponse> getCarAll() {
//        List<CarResponse> forObject = restTemplate.getForObject("http://localhost:8081/api/car/v1",
//                List.class);
//    }
}
