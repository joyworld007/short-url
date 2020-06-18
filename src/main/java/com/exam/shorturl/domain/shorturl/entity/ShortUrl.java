package com.exam.shorturl.domain.shorturl.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("shortUrl")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ShortUrl {

  @Id
  private String id; //hash
  private String url; //short url
  private String orgUrl; //origin url
  private Long viewCount; // view count

}
