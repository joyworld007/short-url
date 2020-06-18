package com.exam.shorturl.interceptor;

import com.exam.shorturl.domain.shorturl.entity.ShortUrl;
import com.exam.shorturl.repository.shorturl.ShortUrlRedisRepository;
import java.util.Optional;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
@Component
@Slf4j
public class ShortUrlInterceptor implements HandlerInterceptor {

  private final ShortUrlRedisRepository shortUrlRedisRepository;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    String regex = "([^:\\/\\s]+)(:([^\\/]*))?\\/([a-zA-Z0-9]{8})";
    Pattern p = Pattern.compile(regex);
    log.info("request : " + request.getServerName() + ":" + request.getServerPort()
        + request.getRequestURI());
    String url = request.getServerName() + ":" + request.getServerPort() + request.getRequestURI();
    if (p.matcher(url).matches()) {
      Optional<ShortUrl> shortUrl = shortUrlRedisRepository
          .findById(request.getRequestURI().replace("/", ""));
      if (shortUrl.isPresent()) {
        log.info("send redirect : {}", shortUrl.get().getOrgUrl());
        shortUrl.get().setViewCount(shortUrl.get().getViewCount() + 1);
        shortUrlRedisRepository.save(shortUrl.get());
        response.sendRedirect(shortUrl.get().getOrgUrl());
        return false;
      }
    }
    return true;
  }
}