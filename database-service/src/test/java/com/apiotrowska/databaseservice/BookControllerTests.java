package com.apiotrowska.databaseservice;

import com.apiotrowska.databaseservice.dto.BookResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerTests {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldCreateBook() throws Exception {
        // given
        String request = "{\"title\":\"newTitle\",\"author\":\"newAuthor\",\"publicationYear\":2000,\"pages\":200}";

        // when
        String json = mockMvc.perform(post("/api/data").content(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // then
        BookResponse response = objectMapper.readValue(json, BookResponse.class);

        assertThat(response.getTitle()).isEqualTo("newTitle");
        assertThat(response.getAuthor()).isEqualTo("newAuthor");
        assertThat(response.getPublicationYear()).isEqualTo(2000);
        assertThat(response.getPages()).isEqualTo(200);
    }

    @Test
    public void shouldReturnBook() throws Exception {
        // given
        String request = "{\"title\":\"Title#2\",\"author\":\"Author#2\",\"publicationYear\":2002,\"pages\":202}";

        String json = mockMvc.perform(post("/api/data").content(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn()
                .getResponse()
                .getContentAsString();

        BookResponse response = objectMapper.readValue(json, BookResponse.class);
        Long id = response.getId();

        // when
        String getBookJson = mockMvc.perform(get("/api/data/" + id)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn()
                .getResponse()
                .getContentAsString();

        //then
        BookResponse getBookResponse = objectMapper.readValue(getBookJson, BookResponse.class);

        assertThat(getBookResponse.getTitle()).isEqualTo("Title#2");
        assertThat(getBookResponse.getAuthor()).isEqualTo("Author#2");
        assertThat(getBookResponse.getPublicationYear()).isEqualTo(2002);
        assertThat(getBookResponse.getPages()).isEqualTo(202);

    }

    @Test
    public void shouldReturnNotFoundBook() throws Exception {
        // given
        String request = "{\"title\":\"Title#1\",\"author\":\"Author#1\",\"publicationYear\":2001,\"pages\":201}";

        String json = mockMvc.perform(post("/api/data").content(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn()
                .getResponse()
                .getContentAsString();

        BookResponse response = objectMapper.readValue(json, BookResponse.class);
        Long id = response.getId() + 1;

        // when
        MockHttpServletResponse putBookResponse = mockMvc.perform(get("/api/data/" + id)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn()
                .getResponse();

        //then
        assertThat(putBookResponse.getStatus()).isEqualTo(MockHttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    public void shouldReturnListOfBooks() throws Exception {
        // given
        List<String> requestList = new ArrayList<>();
        String request1 = "{\"title\":\"Title#1\",\"author\":\"Author#1\",\"publicationYear\":2001,\"pages\":201}";
        String request2 = "{\"title\":\"Title#2\",\"author\":\"Author#2\",\"publicationYear\":2002,\"pages\":202}";
        String request3 = "{\"title\":\"Title#3\",\"author\":\"Author#3\",\"publicationYear\":2003,\"pages\":203}";
        requestList.add(request1);
        requestList.add(request2);
        requestList.add(request3);

        List<BookResponse> response = new ArrayList<>();

        requestList.forEach(request -> {
            String json = null;
            try {
                json = mockMvc.perform(post("/api/data").content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andReturn()
                        .getResponse()
                        .getContentAsString();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            try {
                response.add(objectMapper.readValue(json, BookResponse.class));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });

        // when
        String getBooksJson = mockMvc.perform(get("/api/data")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn()
                .getResponse()
                .getContentAsString();

        //then
        List<BookResponse> getBooksResponse = Arrays.asList(objectMapper.readValue(getBooksJson, BookResponse[].class));

        assertThat(getBooksResponse).containsAll(response);
    }

    @Test
    public void shouldReturnPageOfBooks() throws Exception {
        // given
        List<String> requestList = new ArrayList<>();
        String request1 = "{\"title\":\"Title#1\",\"author\":\"Author#1\",\"publicationYear\":2001,\"pages\":201}";
        String request2 = "{\"title\":\"Title#2\",\"author\":\"Author#2\",\"publicationYear\":2002,\"pages\":202}";
        String request3 = "{\"title\":\"Title#3\",\"author\":\"Author#3\",\"publicationYear\":2003,\"pages\":203}";
        String request4 = "{\"title\":\"Title#4\",\"author\":\"Author#4\",\"publicationYear\":2004,\"pages\":204}";
        requestList.add(request1);
        requestList.add(request2);
        requestList.add(request3);
        requestList.add(request4);

        List<BookResponse> response = new ArrayList<>();

        requestList.forEach(request -> {
            String json = null;
            try {
                json = mockMvc.perform(post("/api/data").content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andReturn()
                        .getResponse()
                        .getContentAsString();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            try {
                response.add(objectMapper.readValue(json, BookResponse.class));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });

        int offset = 1;
        int pageSize = 2;

        // when
        String getBooksJson = mockMvc.perform(get("/api/data")
                .param("offset", String.valueOf(offset))
                .param("pageSize", String.valueOf(pageSize))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn()
                .getResponse()
                .getContentAsString();

        //then
        List<BookResponse> getBooksResponse = Arrays.asList(objectMapper.readValue(getBooksJson, BookResponse[].class));

        assertThat(getBooksResponse.size()).isEqualTo(pageSize);
    }

    @Test
    public void shouldReturnFilteredBooks() throws Exception {
        // given
        List<String> requestList = new ArrayList<>();
        String request1 = "{\"title\":\"Title#1\",\"author\":\"Author#1\",\"publicationYear\":2001,\"pages\":201}";
        String request2 = "{\"title\":\"Title#2\",\"author\":\"Author#2\",\"publicationYear\":2002,\"pages\":202}";
        String request3 = "{\"title\":\"Title#3\",\"author\":\"Author#3\",\"publicationYear\":2003,\"pages\":203}";
        String request4 = "{\"title\":\"Title#4\",\"author\":\"Author#4\",\"publicationYear\":2004,\"pages\":204}";
        requestList.add(request1);
        requestList.add(request2);
        requestList.add(request3);
        requestList.add(request4);

        List<BookResponse> response = new ArrayList<>();

        requestList.forEach(request -> {
            String json = null;
            try {
                json = mockMvc.perform(post("/api/data").content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andReturn()
                        .getResponse()
                        .getContentAsString();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            try {
                response.add(objectMapper.readValue(json, BookResponse.class));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });

        String filter = "3";

        // when
        String getBooksJson = mockMvc.perform(get("/api/data")
                .param("filter", filter)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn()
                .getResponse()
                .getContentAsString();

        //then
        List<BookResponse> getBooksResponse = Arrays.asList(objectMapper.readValue(getBooksJson, BookResponse[].class));

        assertThat(getBooksResponse).contains(response.get(2));
        Assertions.assertFalse(getBooksResponse.contains(response.get(0)));
        Assertions.assertFalse(getBooksResponse.contains(response.get(1)));
        Assertions.assertFalse(getBooksResponse.contains(response.get(3)));
    }

    @Test
    public void shouldUpdateBook() throws Exception {
        // given
        String postRequest = "{\"title\":\"Title#1\",\"author\":\"Author#1\",\"publicationYear\":2001,\"pages\":201}";

        String json = mockMvc.perform(post("/api/data").content(postRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn()
                .getResponse()
                .getContentAsString();

        BookResponse response = objectMapper.readValue(json, BookResponse.class);
        Long id = response.getId();
        String putRequest = "{\"title\":\"Title#2\",\"author\":\"Author#2\",\"publicationYear\":2002,\"pages\":202}";

        // when
        String putBookJson = mockMvc.perform(put("/api/data/" + id).content(putRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn()
                .getResponse()
                .getContentAsString();

        //then
        BookResponse putBookResponse = objectMapper.readValue(putBookJson, BookResponse.class);

        assertThat(putBookResponse.getTitle()).isEqualTo("Title#2");
        assertThat(putBookResponse.getAuthor()).isEqualTo("Author#2");
        assertThat(putBookResponse.getPublicationYear()).isEqualTo(2002);
        assertThat(putBookResponse.getPages()).isEqualTo(202);
    }

    @Test
    public void shouldReturnNotFoundUpdateBook() throws Exception {
        // given
        String request = "{\"title\":\"Title#1\",\"author\":\"Author#1\",\"publicationYear\":2001,\"pages\":201}";

        String json = mockMvc.perform(post("/api/data").content(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn()
                .getResponse()
                .getContentAsString();

        BookResponse response = objectMapper.readValue(json, BookResponse.class);
        Long id = response.getId() + 1;
        String putRequest = "{\"title\":\"Title#2\",\"author\":\"Author#2\",\"publicationYear\":2002,\"pages\":202}";

        // when
        MockHttpServletResponse putBookResponse = mockMvc.perform(put("/api/data/" + id).content(putRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn()
                .getResponse();

        //then
        assertThat(putBookResponse.getStatus()).isEqualTo(MockHttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    public void shouldDeleteBook() throws Exception {
        // given
        String postRequest = "{\"title\":\"Title#1\",\"author\":\"Author#1\",\"publicationYear\":2001,\"pages\":201}";

        String json = mockMvc.perform(post("/api/data").content(postRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn()
                .getResponse()
                .getContentAsString();

        BookResponse response = objectMapper.readValue(json, BookResponse.class);
        Long id = response.getId();

        // when
        MockHttpServletResponse deleteBookResponse = mockMvc.perform(delete("/api/data/" + id)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn()
                .getResponse();

        //then
        assertThat(deleteBookResponse.getStatus()).isEqualTo(MockHttpServletResponse.SC_NO_CONTENT);

        MockHttpServletResponse getBookResponse = mockMvc.perform(get("/api/data/" + id)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn()
                .getResponse();

        assertThat(getBookResponse.getStatus()).isEqualTo(MockHttpServletResponse.SC_NOT_FOUND);
    }
}
