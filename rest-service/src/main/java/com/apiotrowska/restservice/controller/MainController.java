package com.apiotrowska.restservice.controller;

import com.apiotrowska.restservice.dto.BookRequest;
import com.apiotrowska.restservice.dto.BookResponse;
import jakarta.servlet.ServletOutputStream;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@RestController
@RequestMapping("api/books")
@RequiredArgsConstructor
public class MainController {

    private final RestTemplate restTemplate;

    @GetMapping
    public ResponseEntity<BookResponse[]> getAllBooks(@RequestParam(required = false) Integer offset,
                                                      @RequestParam(required = false) Integer pageSize,
                                                      @RequestParam(required = false) String filter,
                                                      @RequestParam(defaultValue = "id,asc") String[] sort) {

        UriComponentsBuilder urlTemplate;
        Map<String, String> params = new HashMap<>();
            ResponseEntity<BookResponse[]> listOfBooksResponse;
            if (offset == null || pageSize == null) {
                urlTemplate = UriComponentsBuilder.fromHttpUrl("http://database-service/api/data/all");
                if (filter != null) {
                    urlTemplate.queryParam("filter", "{filter}");
                    params.put("filter", filter);
                }
                if (sort[0].contains(",")) {
                    for (int i = 1; i <= sort.length; i++) {
                        String sortOrder = sort[i-1];
                        urlTemplate.queryParam("sort", "{sortOrder" + i + "}");
                        params.put("sortOrder" + i, sortOrder);
                    }
                } else {
                    urlTemplate.queryParam("sort", sort[1] + "," + sort[0]);
                }
                String url = urlTemplate.encode().toUriString();
                listOfBooksResponse = restTemplate.getForEntity(url, BookResponse[].class, params);
            } else {
                urlTemplate = UriComponentsBuilder.fromHttpUrl("http://database-service/api/data")
                        .queryParam("offset", "{offset}")
                        .queryParam("pageSize", "{pageSize}");
                params.put("offset", offset.toString());
                params.put("pageSize", pageSize.toString());
                if (filter != null) {
                    urlTemplate.queryParam("filter", "{filter}");
                    params.put("filter", filter);
                }
                if (sort[0].contains(",")) {
                    for (int i = 1; i <= sort.length; i++) {
                        String sortOrder = sort[i-1];
                        urlTemplate.queryParam("sort", "{sortOrder" + i + "}");
                        params.put("sortOrder" + i, sortOrder);
                    }
                } else {
                    urlTemplate.queryParam("sort", "{sort}");
                    params.put("sort", sort[1] + "," + sort[0]);
                }
                String url = urlTemplate.encode().toUriString();
                System.out.println(params);
                System.out.println(url);
                listOfBooksResponse = restTemplate.getForEntity(url, BookResponse[].class, params);
            }
            return listOfBooksResponse;
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable Long id) {
        ResponseEntity<BookResponse> bookResponse = restTemplate.getForEntity("http://database-service/api/data/" + id, BookResponse.class);
        return bookResponse;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BookResponse> createBook(@Valid @RequestBody BookRequest bookRequest) {
        ResponseEntity<BookResponse> createBookResponse = restTemplate.postForEntity("http://database-service/api/data", bookRequest, BookResponse.class);
        return createBookResponse;
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> updateBook(@PathVariable Long id, @Valid @RequestBody BookRequest bookRequest) {
        HttpEntity httpEntity = new HttpEntity(bookRequest, new HttpHeaders());
        ResponseEntity<BookResponse> createBookResponse = restTemplate.exchange("http://database-service/api/data/" + id,
                HttpMethod.PUT, httpEntity, BookResponse.class);
        return createBookResponse;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteBook(@PathVariable Long id) {
        ResponseEntity<HttpStatus> deleteBookResponse = restTemplate.exchange("http://database-service/api/data/" + id,
                HttpMethod.DELETE, HttpEntity.EMPTY, HttpStatus.class);
        return deleteBookResponse;
    }
}
