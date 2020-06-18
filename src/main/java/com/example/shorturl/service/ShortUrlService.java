package com.example.shorturl.service;

import com.example.shorturl.domain.common.CommonResponseDto;
import com.example.shorturl.domain.shorturl.entity.ShortUrl;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public interface ShortUrlService {

  CommonResponseDto<ShortUrl> createShortUrl(ShortUrl shortUrl, HttpServletRequest request);

  Optional<ShortUrl> selectShortUrl(String id);
}

