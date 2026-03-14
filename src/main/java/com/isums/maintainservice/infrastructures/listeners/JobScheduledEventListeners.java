package com.isums.maintainservice.infrastructures.listeners;

import com.isums.maintainservice.domains.entities.MaintenanceJob;
import com.isums.maintainservice.domains.enums.JobStatus;
import com.isums.maintainservice.domains.events.JobEvent;
import com.isums.maintainservice.domains.events.JobNeedRescheduleEvent;
import com.isums.maintainservice.domains.events.JobRescheduledEvent;
import com.isums.maintainservice.domains.events.JobScheduledEvent;
import com.isums.maintainservice.infrastructures.abstracts.MaintenanceJobService;
import com.isums.maintainservice.infrastructures.repositories.MaintenanceJobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JobScheduledEventListeners {
    private final MaintenanceJobService maintenanceJobService;
    private final MaintenanceJobRepository maintenanceJobRepository;

    @KafkaListener(topics = "job.scheduled", groupId = "maintenance-group")
    public void handle(JobEvent event) {

        if (!event.getJobType().equals("MAINTENANCE") && !event.getJobType().equals("ISSUE")) {
            return;
        }

        maintenanceJobService.markScheduled(event);

    }

    @KafkaListener(topics = "job.rescheduled", groupId = "maintenance-group")
    public void handleJobRescheduled(JobEvent event){

        MaintenanceJob job = maintenanceJobRepository.findById(event.getJobId())
                        .orElseThrow();

        job.setStatus(JobStatus.SCHEDULED);
        job.setSlotId(event.getSlotId());

        maintenanceJobRepository.save(job);

    }

    @KafkaListener(topics = "job.need-reschedule", groupId = "maintenance-group")
    public void handleNeedReschedule(JobEvent event){

        MaintenanceJob job = maintenanceJobRepository.findById(event.getJobId())
                        .orElseThrow();

        job.setStatus(JobStatus.NEED_RESCHEDULE);

        maintenanceJobRepository.save(job);

    }

}
