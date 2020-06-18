package com.example.shorturl.controller;

import com.example.shorturl.domain.common.CommonResponseDto;
import com.example.shorturl.domain.common.CommonResponseEntity;
import com.example.shorturl.domain.common.Result;
import com.example.shorturl.domain.shorturl.entity.ShortUrl;
import com.example.shorturl.service.ShortUrlService;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/short-urls")
public class ShortUrlApiController {

  final private ShortUrlService shortUrlService;

  @PostMapping
  public ResponseEntity createShoutUrl(@RequestBody ShortUrl shoutUrl, HttpServletRequest request)
      throws Exception {
    log.info("shoutUrl: " + shoutUrl.getOrgUrl());
    CommonResponseDto commonResponseDto = shortUrlService.createShortUrl(shoutUrl, request);

    switch (commonResponseDto.getCode()) {
      case "FAIL":
        return CommonResponseEntity
            .fail(commonResponseDto.getCode(), commonResponseDto.getMessage());
      default:
        return CommonResponseEntity.ok(Result.builder().entry(shoutUrl).build());
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity selectShortUrl(@PathVariable(value = "id") String id) {
    Optional<ShortUrl> shortUrl = shortUrlService.selectShortUrl(id);
    if (!shortUrl.isPresent()) {
      return CommonResponseEntity.notFound();
    }
    return CommonResponseEntity.ok(Result.builder().entry(shortUrl).build());
  }

}

