package org.starcoin.starswap.api.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.starcoin.starswap.api.bean.Token;

import java.util.List;

public interface TokenRepository extends JpaRepository<Token, String> {


    List<Token> findByDeactivedIsFalse();

//    Page<Token> findByNetworkAndDeletedAtIsNull(String network, Pageable page);
//
//    Token findByTitleOrTitleEnAndDeletedAtIsNull(String title, String titleEn);
}
