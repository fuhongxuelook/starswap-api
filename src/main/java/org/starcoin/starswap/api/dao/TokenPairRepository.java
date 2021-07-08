package org.starcoin.starswap.api.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.starcoin.starswap.api.bean.Token;
import org.starcoin.starswap.api.bean.TokenPair;
import org.starcoin.starswap.api.bean.TokenPairId;

import java.util.List;

public interface TokenPairRepository extends JpaRepository<TokenPair, TokenPairId> {

    List<TokenPair> findByDeactivedIsFalse();

//
//    Page<Token> findByNetworkAndDeletedAtIsNull(String network, Pageable page);
//
//    Token findByTitleOrTitleEnAndDeletedAtIsNull(String title, String titleEn);
}
