package com.hmhco.testcontainers.consul;

import java.util.HashMap;

/**
 * An options class for building consul containers.
 * 
 * @author iyerk
 *
 */
public class ConsulContainerOptions extends HashMap<ConsulContainerOptions.ConsulContainerOption, String> {

    /**
	 * generated serialVersionUID
	 */
	private static final long serialVersionUID = -6592755469152473887L;
	
	/**
	 * config param
	 */
	public static final String LOCAL_CONFIG_PARAM_NAME = "CONSUL_LOCAL_CONFIG";

	/**
	 * Container options for consul
	 * @author iyerk
	 *
	 */
    public enum ConsulContainerOption {
    	
    	/**
    	 * the bind interface for the network
    	 */
        BIND_INTERFACE("CONSUL_BIND_INTERFACE", "eth0"),
        /**
         * theh bind address for consul
         */
        BIND_ADDRESS("CONSUL_BIND_ADDRESS", ""),
        /**
         * the consul client interface
         */
        CLIENT_INTERFACE("CONSUL_CLIENT_INTERFACE", ""),
        /**
         * the consul client address
         */
        CLIENT_ADDRESS("CONSUL_CLIENT_ADDRESS", "");

        private String optionName;
        private String defaultValue;

        ConsulContainerOption(String optionName, String defaultValue) {
            this.optionName = optionName;
            this.defaultValue = defaultValue;
        }

        /**
         * get option name 
         * @return optionName
         */
        public String getOptionName() {
            return optionName;
        }

        /**
         * get defaultValue
         * @return defaultValue
         */
        public String getDefaultValue() {
            return defaultValue;
        }
    }
}
