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

    // region ACLs

    public ConsulContainerBuilder withACL(ConsulConfiguration.ACL acl) {
        this.config.setAcl(acl);
        return this;
    }

    /**
     * Adds master token to Consul configuration effective from 1.4.0
     */
    public ConsulContainerBuilder withMasterToken(String masterToken) {
        this.config.getAcl().getTokens().setMaster(masterToken);
        return this;
    }

    /**
     * Adds agent token to Consul configuration effective from 1.4.0
     */
    public ConsulContainerBuilder withAgentToken(String agentToken) {
        this.config.getAcl().getTokens().setAgent(agentToken);
        return this;
    }

    /**
     * Adds replication token to Consul configuration effective from 1.4.0
     */
    public ConsulContainerBuilder withReplicationToken(String replicationToken) {
        this.config.getAcl().getTokens().setReplication(replicationToken);
        return this;
    }

    /**
     * Adds default token to Consul configuration effective from 1.4.0
     */
    public ConsulContainerBuilder withDefaultToken(String defaultToken) {
        this.config.getAcl().getTokens().setDefaultToken(defaultToken);
        return this;
    }

    /**
     * Enable ACL for Consul configuration effective from 1.4.0
     * @return
     */
    public ConsulContainerBuilder withACLEnabled() {
        this.config.getAcl().setEnabled(true);
        return this;
    }

    /**
     * Sets default ACL policy for Consul configuration effective from 1.4.0
     */
    public ConsulContainerBuilder withACLDefaultPolicy(String defaultPolicy) {
        this.config.getAcl().setDefaultPolicy(defaultPolicy);
        return this;
    }

    // endregion

    public ConsulContainerBuilder withWaitTimeout(Integer seconds) {
        this.waitTimeout = seconds;
        return this;
    }

    public ConsulContainer build() {
        return new ConsulContainer(this.config, this.options, this.version, this.waitTimeout);
    }

    public ConsulConfiguration buildConfig() {
        return this.config;
    }

}
