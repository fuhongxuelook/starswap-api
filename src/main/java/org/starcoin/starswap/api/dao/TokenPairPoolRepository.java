package org.starcoin.starswap.api.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.starcoin.starswap.api.bean.TokenPairPool;
import org.starcoin.starswap.api.bean.TokenPairPoolId;

import java.util.List;

public interface TokenPairPoolRepository extends JpaRepository<TokenPairPool, TokenPairPoolId> {

    List<TokenPairPool> findByDeactivedIsFalse();

//
//    Page<Token> findByNetworkAndDeletedAtIsNull(String network, Pageable page);
//
//    Token findByTitleOrTitleEnAndDeletedAtIsNull(String title, String titleEn);
}
