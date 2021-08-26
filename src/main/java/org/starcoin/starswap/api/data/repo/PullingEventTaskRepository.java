package org.starcoin.starswap.api.data.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.starcoin.starswap.api.data.model.PullingEventTask;

import java.util.List;

public interface PullingEventTaskRepository extends JpaRepository<PullingEventTask, String> {
    List<PullingEventTask> findByStatusEquals(String status);
}
