package com.will.tooljars.dropwizard.health;

import com.codahale.metrics.health.HealthCheck;

public class WillHealthCheck extends HealthCheck {

    protected Result check() throws Exception {
        return Result.healthy();
    }

}
