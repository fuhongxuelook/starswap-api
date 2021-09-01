package org.starcoin.starswap.api.data.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.starcoin.starswap.api.data.model.FarmAccount;
import org.starcoin.starswap.api.data.model.FarmAccountId;

import java.util.List;

public interface FarmAccountRepository extends JpaRepository<FarmAccount, FarmAccountId> {

    List<FarmAccount> findByDeactivedIsFalse();

    List<FarmAccount> findByFarmAccountIdAccountAddress(String accountAddress);

}
