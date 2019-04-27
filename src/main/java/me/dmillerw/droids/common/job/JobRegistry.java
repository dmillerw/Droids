package me.dmillerw.droids.common.job;

import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.Map;

public class JobRegistry {

    public static final JobRegistry INSTANCE = new JobRegistry();

    private Map<String, JobDefinition> registeredJobTypes = Maps.newHashMap();

    public void registerJobType(JobDefinition definition) {
        registeredJobTypes.put(definition.key, definition);
    }

    public JobDefinition getJob(String jobKey) {
        return registeredJobTypes.get(jobKey);
    }

    public Collection<JobDefinition> getJobs() {
        return registeredJobTypes.values();
    }
}
