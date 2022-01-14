package com.hmhco.testcontainers.consul;

import org.testcontainers.containers.Network;

/**
 * A consul container builder.
 * 
 * @author iyerk
 *
 */
public class ConsulContainerBuilder {

    private static final String DEFAULT_CONSUL_VERSION = "1.4.0-rc1";
    private static final Integer DEFAULT_WAIT_TIMEOUT_SEC = 30;
    private static final Integer DEFAULT_CLUSTER_SIZE = 1;

    /**
     * ConsulContainerOptions
     */
    protected ConsulContainerOptions options;

    /**
     * ConsulConfiguration
     */
    protected ConsulConfiguration config;
    
    /**
     * ConsulCommand
     */
    protected ConsulCommand command;
    
    /**
     * clusterSize
     */
    protected Integer clusterSize;
    
    /**
     * version
     */
    protected String version;
    
    /**
     * waitTimeout
     */
    protected Integer waitTimeout;

    /** 
     * default constructor
     */
    public ConsulContainerBuilder() {
        this.config = new ConsulConfiguration();
        this.options = new ConsulContainerOptions();
        this.command = new ConsulCommand();
        this.version = DEFAULT_CONSUL_VERSION;
        this.clusterSize = DEFAULT_CLUSTER_SIZE;
        this.waitTimeout = DEFAULT_WAIT_TIMEOUT_SEC;
    }

    /**
     * build container with options
     * 
     * @param consulContainerOptions - the options
     * @return ConsulContainerBuilder - with options
     */
    public ConsulContainerBuilder withOptions(ConsulContainerOptions consulContainerOptions) {
        this.options = consulContainerOptions;
        return this;
    }

    /**
     * add option to container option.
     * 
     * @param option - ConsulContainerOption
     * @param value - the option to add
     * @return ConsulContainerBuilder
     */
    public ConsulContainerBuilder withOption(ConsulContainerOptions.ConsulContainerOption option, String value) {
        this.options.put(option, value);
        return this;
    }

    /**
     * build container with configuration
     * @param consulConfiguration - the configuration
     * @return ConsulContainerBuilder
     */
    public ConsulContainerBuilder withConfig(ConsulConfiguration consulConfiguration) {
        this.config = consulConfiguration;
        return this;
    }

    /**
     * set container version
     * 
     * @param containerVersion - the container version
     * @return ConsulContainerBuilder
     */
    public ConsulContainerBuilder withContainerVersion(String containerVersion) {
        this.version = containerVersion;
        return this;
    }

    /**
     * set the datacenter
     * 
     * @param datacenter - the datacenter to use
     * @return ConsulContainerBuilder
     */
    public ConsulContainerBuilder withDatacenter(String datacenter) {
        this.config.setDatacenter(datacenter);
        return this;
    }

    /**
     * Adds ACL configuration effective from 1.4.0
     * @param acl - the acl for this container
     * @return  ConsulContainerBuilder
     */
    public ConsulContainerBuilder withACL(ConsulConfiguration.ACL acl) {
        this.config.setAcl(acl);
        return this;
    }

    /**
     * Adds master token to Consul configuration effective from 1.4.0
     * @param masterToken -  master token for the container
     * @return ConsulContainerBuilder
     */
    public ConsulContainerBuilder withMasterToken(String masterToken) {
        initAcl();
        this.config.getAcl().getTokens().setMaster(masterToken);
        return this;
    }

    /**
     * Adds agent token to Consul configuration effective from 1.4.0
     * @param agentToken - the agent token
     * @return ConsulContainerBuilder
     */
    public ConsulContainerBuilder withAgentToken(String agentToken) {
        initAcl();
        this.config.getAcl().getTokens().setAgent(agentToken);
        return this;
    }

    /**
     * Adds replication token to Consul configuration effective from 1.4.0
     * @param replicationToken - the replication token
     * @return ConsulContainerBuilder
     */
    public ConsulContainerBuilder withReplicationToken(String replicationToken) {
        initAcl();
        this.config.getAcl().getTokens().setReplication(replicationToken);
        return this;
    }

    /**
     * Adds default token to Consul configuration effective from 1.4.0
     * @param defaultToken -  the default Token
     * @return ConsulContainerBuilder
     */
    public ConsulContainerBuilder withDefaultToken(String defaultToken) {
        initAcl();
        this.config.getAcl().getTokens().setDefaultToken(defaultToken);
        return this;
    }

    /**
     * Enable ACL for Consul configuration effective from 1.4.0
     * @return ConsulContainerBuilder
     */
    public ConsulContainerBuilder withACLEnabled() {
        initAcl();
        this.config.getAcl().setEnabled(true);
        return this;
    }

    /**
     * Sets default ACL policy for Consul configuration effective from 1.4.0
     * @param defaultPolicy -  the default ACL policy
     * @return ConsulContainerBuilder
     */
    public ConsulContainerBuilder withACLDefaultPolicy(String defaultPolicy) {
        initAcl();
        this.config.getAcl().setDefaultPolicy(defaultPolicy);
        return this;
    }

    /**
     * init ACL
     */
    private void initAcl() {
        if (this.config.getAcl() == null) {
            this.config.setAcl(new ConsulConfiguration.ACL());
        }
    }

    /**
     * build container with dev flag
     * @return ConsulContainerBuilder
     */
    public ConsulContainerBuilder dev() {
        this.command.setDev(true);
        return this;
    }

    /**
     * Enable UI for teh container
     * @return ConsulContainerBuilder
     */
    public ConsulContainerBuilder ui() {
        this.command.setUi(true);
        return this;
    }

    /**
     * Build container with join address and retry
     * @param joinTo - address
     * @param retry - flag
     * @return ConsulContainerBuilder
     */
    protected ConsulContainerBuilder join(String joinTo, boolean retry) {
        this.command.setJoin(joinTo);
        this.command.setRetryJoin(retry);
        return this;
    }
    
    /**
     * add wait timeout in seconds for the container
     * 
     * @param seconds - wait timeout
     * @return ConsulContainerBuilder
     */
    public ConsulContainerBuilder withWaitTimeout(Integer seconds) {
        if (seconds != null) {
            this.waitTimeout = seconds;
        }
        return this;
    }

    /**
     * Configuration for ports for Consul setups starting with 1.4.0
     * @param ports - the port configuration for this container
     * @return ConsulContainerBuilder
     */
    public ConsulContainerBuilder withPorts(ConsulConfiguration.Ports ports) {
        this.config.setPorts(ports);
        return this;
    }

    /**
     * build the container
     * @return ConsulContainer
     */
    public ConsulContainer build() {
        return new ConsulContainer(this.config, this.options, this.command, this.version, this.waitTimeout);
    }

    /**
     * Build cluster with member size and network
     * 
     * @param size - cluster size
     * @param network - network for the container
     * @return ConsulCluster
     */
    public ConsulCluster cluster(Integer size, Network network) {
        if (size == null || size < 1) {
            throw new RuntimeException(String.format("Invalid cluster size: %s", size));
        }
        return new ConsulCluster(size, this::build, network);
    }

    /**
     * Build cluster with member size
     * @param size -  cluster size
     * @return ConsulCluster
     */
    public ConsulCluster cluster(Integer size) {
        return cluster(size, null);
    }

    /**
     * build container configuration.
     * @return ConsulConfiguration
     */
    public ConsulConfiguration buildConfig() {
        return this.config;
    }
}

