package com.example.shorturl.repository.shorturl;

import com.example.shorturl.domain.shorturl.entity.ShortUrl;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShortUrlRedisRepository extends CrudRepository<ShortUrl, String> {

}
