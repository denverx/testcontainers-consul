package org.jetax.testcontainers.consul;

import org.testcontainers.containers.Network;

public class ConsulContainerBuilder {

    private static final String DEFAULT_CONSUL_VERSION = "1.4.0-rc1";
    private static final Integer DEFAULT_WAIT_TIMEOUT_SEC = 30;
    private static final Integer DEFAULT_CLUSTER_SIZE = 1;

    protected ConsulContainerOptions options;
    protected ConsulConfiguration config;
    protected ConsulCommand command;
    protected Integer clusterSize;
    protected String version;
    protected Integer waitTimeout;

    public ConsulContainerBuilder() {
        this.config = new ConsulConfiguration();
        this.options = new ConsulContainerOptions();
        this.command = new ConsulCommand();
        this.version = DEFAULT_CONSUL_VERSION;
        this.clusterSize = DEFAULT_CLUSTER_SIZE;
        this.waitTimeout = DEFAULT_WAIT_TIMEOUT_SEC;
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

    public ConsulContainerBuilder withDatacenter(String datacenter) {
        this.config.setDatacenter(datacenter);
        return this;
    }

    // region ACLs

    /**
     * Adds ACL configuration effective from 1.4.0
     */
    public ConsulContainerBuilder withACL(ConsulConfiguration.ACL acl) {
        this.config.setAcl(acl);
        return this;
    }

    /**
     * Adds master token to Consul configuration effective from 1.4.0
     */
    public ConsulContainerBuilder withMasterToken(String masterToken) {
        initAcl();
        this.config.getAcl().getTokens().setMaster(masterToken);
        return this;
    }

    /**
     * Adds agent token to Consul configuration effective from 1.4.0
     */
    public ConsulContainerBuilder withAgentToken(String agentToken) {
        initAcl();
        this.config.getAcl().getTokens().setAgent(agentToken);
        return this;
    }

    /**
     * Adds replication token to Consul configuration effective from 1.4.0
     */
    public ConsulContainerBuilder withReplicationToken(String replicationToken) {
        initAcl();
        this.config.getAcl().getTokens().setReplication(replicationToken);
        return this;
    }

    /**
     * Adds default token to Consul configuration effective from 1.4.0
     */
    public ConsulContainerBuilder withDefaultToken(String defaultToken) {
        initAcl();
        this.config.getAcl().getTokens().setDefaultToken(defaultToken);
        return this;
    }

    /**
     * Enable ACL for Consul configuration effective from 1.4.0
     * @return
     */
    public ConsulContainerBuilder withACLEnabled() {
        initAcl();
        this.config.getAcl().setEnabled(true);
        return this;
    }

    /**
     * Sets default ACL policy for Consul configuration effective from 1.4.0
     */
    public ConsulContainerBuilder withACLDefaultPolicy(String defaultPolicy) {
        initAcl();
        this.config.getAcl().setDefaultPolicy(defaultPolicy);
        return this;
    }

    private void initAcl() {
        if (this.config.getAcl() == null) {
            this.config.setAcl(new ConsulConfiguration.ACL());
        }
    }

    // endregion

    public ConsulContainerBuilder dev() {
        this.command.setDev(true);
        return this;
    }

    public ConsulContainerBuilder ui() {
        this.command.setUi(true);
        return this;
    }

    protected ConsulContainerBuilder join(String joinTo, boolean retry) {
        this.command.setJoin(joinTo);
        this.command.setRetryJoin(retry);
        return this;
    }

    public ConsulContainerBuilder withWaitTimeout(Integer seconds) {
        if (seconds != null) {
            this.waitTimeout = seconds;
        }
        return this;
    }

    /**
     * Configuration for ports for Consul setups starting with 1.4.0
     */
    public ConsulContainerBuilder withPorts(ConsulConfiguration.Ports ports) {
        this.config.setPorts(ports);
        return this;
    }

    public ConsulContainer build() {
        return new ConsulContainer(this.config, this.options, this.command, this.version, this.waitTimeout);
    }

    public ConsulCluster cluster(Integer size, Network network) {
        if (size == null || size < 1) {
            throw new RuntimeException(String.format("Invalid cluster size: %s", size));
        }
        return new ConsulCluster(size, this::build, network);
    }

    public ConsulCluster cluster(Integer size) {
        return cluster(size, null);
    }

    public ConsulConfiguration buildConfig() {
        return this.config;
    }
}
