package com.exam.shorturl.service;

import com.exam.shorturl.domain.common.CommonResponseDto;
import com.exam.shorturl.domain.shorturl.entity.ShortUrl;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public interface ShortUrlService {

  CommonResponseDto<ShortUrl> createShortUrl(ShortUrl shortUrl, HttpServletRequest request);

  Optional<ShortUrl> selectShortUrl(String id);
}

