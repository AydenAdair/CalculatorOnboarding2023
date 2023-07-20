package org.aydenadair.calculator;

import io.confluent.rest.RestConfig;
import org.apache.kafka.common.config.ConfigDef;

import java.util.Map;

public class CalculatorConfig extends RestConfig{
    private static final ConfigDef config;

    static {
        config = baseConfigDef();
    }

    public CalculatorConfig(Map<String, String> props) {
        super(config, props);
    }

}
