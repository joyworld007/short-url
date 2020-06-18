package com.exam.shorturl.shorturl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.exam.shorturl.domain.common.ApiResponse;
import com.exam.shorturl.domain.common.ApiResult;
import com.exam.shorturl.domain.common.CommonResponseDto;
import com.exam.shorturl.domain.shorturl.entity.ShortUrl;
import com.exam.shorturl.repository.shorturl.ShortUrlRedisRepository;
import com.exam.shorturl.service.ShortUrlServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
@EnableAutoConfiguration
@ActiveProfiles("local")
@SuppressWarnings("unchecked")
public class ShortUrlServiceTest {

  Logger log = LoggerFactory.getLogger(this.getClass());

  @Value("${naver-open-api.short-url.url}")
  private String url;
  @Value("${naver-open-api.short-url.client-id}")
  private String clientId;
  @Value("${naver-open-api.short-url.secret}")
  private String secret;

  @Mock
  private ShortUrlRedisRepository shortUrlRedisRepository;

  @Mock
  private RestTemplate restTemplate;

  @InjectMocks
  private ShortUrlServiceImpl shortUrlService;

  private ShortUrl shortUrl;
  private ApiResponse apiResponse;
  private ApiResult apiResult;
  private ResponseEntity<ApiResponse> responseEntity;
  private MockHttpServletRequest request;
  private HttpEntity<MultiValueMap<String, String>> naverApiRequest;

  @BeforeEach
  public void setup(TestInfo testInfo) {
    rawSetup(testInfo);
  }

  private void rawSetup(TestInfo testInfo) {
    switch (testInfo.getDisplayName()) {
      case "Short Url Invalid Test":
        shortUrl = ShortUrl.builder().orgUrl("12345").build();
        break;

      case "Short Url Generate Test":
        shortUrl = ShortUrl.builder().orgUrl("http://www.mushinsa.com").build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("X-Naver-Client-Id", clientId);
        headers.add("X-Naver-Client-Secret", secret);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("url", shortUrl.getOrgUrl());
        naverApiRequest = new HttpEntity(map, headers);
        apiResult = ApiResult.builder()
            .hash("abcdbefh")
            .orgurl("http://www.musinsa.com")
            .url("localhost:8080/abcdbefh")
            .build();
        apiResponse = ApiResponse.builder()
            .result(apiResult)
            .code("SUCCESS")
            .message("OK")
            .build();
        responseEntity = ResponseEntity.ok(apiResponse);
        break;
    }
  }

  @DisplayName("Short Url Invalid Test")
  @Test
  public void shortUrlInvalidTest() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    CommonResponseDto commonResponseDto = shortUrlService.createShortUrl(shortUrl, request);
    assertThat(commonResponseDto.getCode()).isEqualTo("FAIL");
  }

  @DisplayName("Short Url Generate Test")
  @Test
  public void shortUrlGenerateTest() {
    try {
      MockHttpServletRequest request = new MockHttpServletRequest();
      when(restTemplate.postForEntity(url, naverApiRequest, ApiResponse.class))
          .thenReturn(responseEntity);
      when(shortUrlRedisRepository.save(any())).thenReturn(null);
      CommonResponseDto commonResponseDto = shortUrlService.createShortUrl(shortUrl, request);
      assertThat(commonResponseDto.getCode()).isEqualTo("SUCCESS");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
