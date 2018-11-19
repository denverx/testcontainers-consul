package org.jetax.testcontainers.consul;

public class ConsulContainerBuilder {

    private static final String DEFAULT_CONSUL_VERSION = "1.4.0-rc1";

    private ConsulContainerOptions options;
    private ConsulConfiguration config;
    private String version;
    private Integer waitTimeout;

    public ConsulContainerBuilder() {
        this.config = new ConsulConfiguration();
        this.options = new ConsulContainerOptions();
        this.version = DEFAULT_CONSUL_VERSION;
    }

    public ConsulContainerBuilder withOptions(ConsulContainerOptions consulContainerOptions) {
        this.options = consulContainerOptions;
        return this;
    }

    public ConsulContainerBuilder withOption(ConsulContainerOptions.ConsulContainerOption option, String value) {
        this.options.put(option, value);
        return this;
    }

    public ConsulContainerBuilder withConfig(ConsulConfiguration consulConfiguration) {
        this.config = consulConfiguration;
        return this;
    }

    public ConsulContainerBuilder withContainerVersion(String containerVersion) {
        this.version = containerVersion;
        return this;
    }

    public ConsulContainerBuilder withACL(ConsulConfiguration.ACL acl) {
        this.config.setAcl(acl);
        return this;
    }

    public ConsulContainerBuilder withWaitTimeout(Integer seconds) {
        this.waitTimeout = seconds;
        return this;
    }

    public ConsulContainer build() {
        return new ConsulContainer(this.config, this.options, this.version, this.waitTimeout);
    }

}
