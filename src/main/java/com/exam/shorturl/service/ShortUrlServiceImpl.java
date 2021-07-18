package com.exam.shorturl.service;

import com.exam.shorturl.domain.common.CommonResponseDto;
import com.exam.shorturl.domain.common.Result;
import com.exam.shorturl.domain.common.ApiResponse;
import com.exam.shorturl.domain.shorturl.entity.ShortUrl;
import com.exam.shorturl.repository.shorturl.ShortUrlRedisRepository;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class ShortUrlServiceImpl implements ShortUrlService {

  private final RestTemplate restTemplate;
  private final ShortUrlRedisRepository shortUrlRedisRepository;
  @Value("${naver-open-api.short-url.url}")
  private String url;
  @Value("${naver-open-api.short-url.client-id}")
  private String clientId;
  @Value("${naver-open-api.short-url.secret}")
  private String secret;

  @Override
  public CommonResponseDto createShortUrl(ShortUrl shortUrl, HttpServletRequest request) {
    String regex = "^(https?):\\/\\/([^:\\/\\s]+)(:([^\\/]*))?((\\/[^\\s/\\/]+)*)?\\/?([^#\\s\\?]*)(\\?([^#\\s]*))?(#(\\w*))?$";
    Pattern p = Pattern.compile(regex);
    if (!p.matcher(shortUrl.getOrgUrl()).matches()) {
      return CommonResponseDto.builder()
          .result(null)
          .code("FAIL").message("INVALID_URL").build();
    }
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    headers.add("X-Naver-Client-Id", clientId);
    headers.add("X-Naver-Client-Secret", secret);
    MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
    map.add("url", shortUrl.getOrgUrl());
    HttpEntity<MultiValueMap<String, String>> naverApiRequest = new HttpEntity(map, headers);

    ResponseEntity<ApiResponse> response = restTemplate
        .postForEntity(url, naverApiRequest, ApiResponse.class);
    log.info("response : {}", response);
    Optional.ofNullable(response.getBody()).ifPresent(
        t -> {
          if ("200".equals(t.getCode())) {
            shortUrl.setId(t.getResult().getHash());
            shortUrl.setUrl(
                request.getServerName() + ":" + request.getServerPort() + "/" + t.getResult()
                    .getHash());
            shortUrl.setViewCount(0L);
          }
          shortUrlRedisRepository.save(shortUrl);
        }
    );
    return CommonResponseDto.builder()
        .result(Result.builder().entry(shortUrl).build())
        .code("SUCCESS").message("OK").build();
  }

  @Override
  public Optional<ShortUrl> selectShortUrl(String id) {
    return shortUrlRedisRepository.findById(id);
  }
}
