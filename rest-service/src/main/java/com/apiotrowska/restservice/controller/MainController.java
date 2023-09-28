package com.apiotrowska.restservice.controller;

import com.apiotrowska.restservice.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("api/books")
@RequiredArgsConstructor
public class MainController {

    private final RestTemplate restTemplate;
    @Value("${databaseservice.url}")
    private String databaseServiceUrl;

    @GetMapping
    public ResponseEntity<RestPageImpl> getAllBooks(@RequestParam(required = false) Integer page,
                                                      @RequestParam(required = false) Integer size,
                                                      @ModelAttribute BookFilterDto bookFilterDto,
                                                      @RequestParam(defaultValue = "id,asc") String[] sort) {

        UriComponentsBuilder urlTemplate;
        Map<String, String> params = new HashMap<>();
        urlTemplate = UriComponentsBuilder.fromHttpUrl(databaseServiceUrl);
        if (page != null && size != null) {
            urlTemplate.queryParam("page", "{page}")
                    .queryParam("size", "{size}");
            params.put("page", page.toString());
            params.put("size", size.toString());
        }
        if (bookFilterDto != null && bookFilterDto.getBookFilters() != null) {
            for (int i = 0; i < bookFilterDto.getBookFilters().size(); i++) {
                BookFilter bookFilter = bookFilterDto.getBookFilters().get(i);
                urlTemplate.queryParam("bookFilters[" + i + "].filterKey", "{bookFilters[" + i + "].filterKey}");
                params.put("bookFilters[" + i + "].filterKey", bookFilter.getFilterKey());
                urlTemplate.queryParam("bookFilters[" + i + "].value", "{bookFilters[" + i + "].value}");
                params.put("bookFilters[" + i + "].value", String.valueOf(bookFilter.getValue()));
            }
        }
        if (sort[0].contains(",")) {
            for (int i = 1; i <= sort.length; i++) {
                String sortOrder = sort[i - 1];
                urlTemplate.queryParam("sort", "{sortOrder" + i + "}");
                params.put("sortOrder" + i, sortOrder);
            }
        } else {
            urlTemplate.queryParam("sort", "{sort}");
            params.put("sort", sort[0] + "," + sort[1]);
        }

        String url = urlTemplate.encode().toUriString();
        log.info(url);
        log.info(params.toString());
        ResponseEntity<RestPageImpl> listOfBooksResponse = restTemplate.getForEntity(url, RestPageImpl.class, params);
        return listOfBooksResponse;
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable Long id) {
        ResponseEntity<BookResponse> bookResponse = restTemplate.getForEntity(databaseServiceUrl + "/" + id, BookResponse.class);
        return bookResponse;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BookResponse> createBook(@Valid @RequestBody BookRequest bookRequest) {
        ResponseEntity<BookResponse> createBookResponse = restTemplate.postForEntity(databaseServiceUrl, bookRequest, BookResponse.class);
        return createBookResponse;
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> updateBook(@PathVariable Long id, @Valid @RequestBody BookRequest bookRequest) {
        HttpEntity httpEntity = new HttpEntity(bookRequest, new HttpHeaders());
        ResponseEntity<BookResponse> createBookResponse = restTemplate.exchange(databaseServiceUrl + "/" + id,
                HttpMethod.PUT, httpEntity, BookResponse.class);
        return createBookResponse;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteBook(@PathVariable Long id) {
        ResponseEntity<HttpStatus> deleteBookResponse = restTemplate.exchange(databaseServiceUrl + "/" + id,
                HttpMethod.DELETE, HttpEntity.EMPTY, HttpStatus.class);
        return deleteBookResponse;
    }
}
