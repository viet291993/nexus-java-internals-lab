package com.nexus.phase1;

/**
 * Interface chung cho các Plugin.
 * Cả Main App và Plugin đều phải biết Interface này.
 */
public interface Plugin {
    void execute();
    String getVersion();
}
