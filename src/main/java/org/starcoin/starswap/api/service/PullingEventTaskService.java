package org.starcoin.starswap.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.starcoin.starswap.api.data.model.PullingEventTask;
import org.starcoin.starswap.api.data.repo.PullingEventTaskRepository;
import org.starcoin.utils.BeanUtils2;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PullingEventTaskService {

    @Autowired
    private PullingEventTaskRepository pullingEventTaskRepository;

    @Transactional
    public void createPullingEventTask(PullingEventTask pullingEventTask) {
        PullingEventTask targetEventTask = new PullingEventTask();
        Set<String> props = Arrays.stream(new String[]{"fromBlockNumber", "toBlockNumber"}).collect(Collectors.toSet());
        BeanUtils2.copySpecificProperties(pullingEventTask, targetEventTask, props);
        targetEventTask.setCreatedAt(System.currentTimeMillis());
        targetEventTask.setCreatedBy("ADMIN");
        targetEventTask.setUpdatedAt(targetEventTask.getCreatedAt());
        targetEventTask.setUpdatedBy(targetEventTask.getCreatedBy());
        pullingEventTaskRepository.save(targetEventTask);
    }

}
