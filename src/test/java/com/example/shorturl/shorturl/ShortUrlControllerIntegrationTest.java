package com.example.shorturl.shorturl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.shorturl.controller.ShortUrlApiController;
import com.example.shorturl.domain.common.CommonResponseDto;
import com.example.shorturl.domain.common.Result;
import com.example.shorturl.domain.shorturl.entity.ShortUrl;
import com.example.shorturl.repository.shorturl.ShortUrlRedisRepository;
import com.example.shorturl.service.ShortUrlService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@WebMvcTest(ShortUrlApiController.class)
@SuppressWarnings("unchecked")
public class ShortUrlControllerIntegrationTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  ShortUrlService shortUrlService;

  @MockBean
  ShortUrlRedisRepository shortUrlRedisRepository;

  @DisplayName("create controller test")
  @Test
  void createShoutUrlTest() throws Exception {
    ShortUrl shortUrl = ShortUrl.builder().orgUrl("http://www.musinsa.com").build();
    CommonResponseDto commonResponseDto = CommonResponseDto.builder().result(
        Result.builder().entry(shortUrl).build()
    ).code("SUCCESS").build();
    MockHttpServletRequest request = new MockHttpServletRequest();
    given(shortUrlService.createShortUrl(any(), any())).willReturn(commonResponseDto);

    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
    ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
    String requestJson = ow.writeValueAsString(shortUrl);

    mockMvc.perform(post("/v1/short-urls")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestJson))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("SUCCESS"));

  }

  @DisplayName("create controller fail test")
  @Test
  void createShoutUrlFailTest() throws Exception {
    ShortUrl shortUrl = ShortUrl.builder().orgUrl("http://www.musinsa.com").build();
    CommonResponseDto commonResponseDto = CommonResponseDto.builder().result(
        Result.builder().entry(shortUrl).build()
    ).code("FAIL").build();
    MockHttpServletRequest request = new MockHttpServletRequest();
    given(shortUrlService.createShortUrl(any(), any())).willReturn(commonResponseDto);

    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
    ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
    String requestJson = ow.writeValueAsString(shortUrl);

    mockMvc.perform(post("/v1/short-urls")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestJson))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("FAIL"));
  }

  @DisplayName("Short Url Search Test Success")
  @Test
  public void shortUrlSearchSuccessTest() throws Exception {
    Optional<ShortUrl> shortUrl = Optional
        .of(ShortUrl.builder().orgUrl("http://www.musinsa.com").build());
    given(shortUrlService.selectShortUrl(any())).willReturn(shortUrl);
    mockMvc.perform(get("/v1/short-urls/1"))
        .andExpect(status().isOk());
  }

  @DisplayName("Short Url Search Fail Test")
  @Test
  public void shortUrlSearchFailTest() throws Exception {
    Optional<ShortUrl> shortUrl = Optional.empty();
    given(shortUrlService.selectShortUrl(any())).willReturn(shortUrl);
    mockMvc.perform(get("/v1/short-urls/1"))
        .andExpect(status().isNotFound());
  }

}
