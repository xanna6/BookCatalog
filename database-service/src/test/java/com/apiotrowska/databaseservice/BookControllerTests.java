package com.apiotrowska.databaseservice;

import com.apiotrowska.databaseservice.dto.BookResponse;
import com.apiotrowska.databaseservice.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void shouldReturnBook() throws Exception {
        // given
        String request = "{\"title\":\"newTitle\",\"author\":\"newAuthor\",\"publicationYear\":2000,\"pages\":200}";

        // when
        String json = mockMvc.perform(post("/api/data").content(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn()
                .getResponse()
                .getContentAsString();

        // then
        BookResponse response = objectMapper.readValue(json, BookResponse.class);

        assertThat(response.getAuthor()).isEqualTo("newAuthor");
    }

    @Test
    public void shouldReturnNotFoundBook() {

    }

    @Test
    public void shouldReturnListOfBooks() {

    }

    @Test
    public void shouldCreateBook() {

    }

    @Test
    public void shouldUpdateBook() {

    }

    @Test
    public void shouldReturnNotFoundUpdateBook() {

    }

    @Test
    public void shouldDeleteBook() {

    }
}
