package org.starcoin.starswap.api.data.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.starcoin.starswap.api.data.model.TokenPair;
import org.starcoin.starswap.api.data.model.TokenPairId;

import java.util.List;

public interface TokenPairRepository extends JpaRepository<TokenPair, TokenPairId> {

    List<TokenPair> findByDeactivedIsFalse();

//
//    Page<Token> findByNetworkAndDeletedAtIsNull(String network, Pageable page);
//
//    Token findByTitleOrTitleEnAndDeletedAtIsNull(String title, String titleEn);
}
