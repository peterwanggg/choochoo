package com.pwang.kings.tasks;

import com.google.common.util.concurrent.AbstractScheduledService;
import io.dropwizard.lifecycle.Managed;
import org.apache.log4j.Logger;

/**
 * @author pwang on 1/3/18.
 */
public class ManagedPeriodicTask implements Managed {

    private final Logger LOGGER = Logger.getLogger(ManagedPeriodicTask.class);
    private final AbstractScheduledService periodicTask;

    public ManagedPeriodicTask(AbstractScheduledService periodicTask) {
        this.periodicTask = periodicTask;
    }

    @Override
    public void start() throws Exception {
        periodicTask.startAsync().awaitRunning();
    }

    @Override
    public void stop() throws Exception {
        periodicTask.stopAsync().awaitTerminated();
    }
}