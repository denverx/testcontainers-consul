package org.jetax.testcontainers.consul;

import java.util.HashMap;

public class ConsulContainerOptions extends HashMap<ConsulContainerOptions.ConsulContrainerOption, String> {

    public static final String LOCAL_CONFIG_PARAM_NAME = "CONSUL_LOCAL_CONFIG";

    public enum ConsulContrainerOption {
        BIND_INTERFACE("CONSUL_BIND_INTERFACE", "eth0"),
        BIND_ADDRESS("CONSUL_BIND_ADDRESS", ""),
        CLIENT_INTERFACE("CONSUL_CLIENT_INTERFACE", ""),
        CLIENT_ADDRESS("CONSUL_CLIENT_ADDRESS", "");

        private String optionName;
        private String defaultValue;

        ConsulContrainerOption(String optionName, String defaultValue) {
            this.optionName = optionName;
            this.defaultValue = defaultValue;
        }

        public String getOptionName() {
            return optionName;
        }

        public String getDefaultValue() {
            return defaultValue;
        }
    }
}
