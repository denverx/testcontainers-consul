package com.hmhco.testcontainers.consul;

import static com.hmhco.testcontainers.consul.ConsulContainerOptions.LOCAL_CONFIG_PARAM_NAME;

import java.time.Duration;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.containers.wait.strategy.WaitStrategy;
import org.testcontainers.utility.MountableFile;

import com.google.gson.Gson;
import com.hmhco.testcontainers.consul.ConsulContainerOptions.ConsulContainerOption;

/**
 * A testcontainer for Hashicorp Consul.
 * 
 * @author iyerk
 *
 */
public class ConsulContainer extends GenericContainer<ConsulContainer> {

    private static final String CONSUL_IMAGE = "consul";
    private static final String CONSUL_VERSION = "1.4.0-rc1";

    private static final Integer DEFAULT_HTTP_PORT = 8500;
    private static final Integer DEFAULT_HTTPS_PORT = 8501;
    private static final Integer DEFAULT_DNS_PORT = 8600;

    private static final String HEALTH_CHECK_PATH = "/v1/status/leader";

    private static final String CA_FILE_NAME = "/consul/config/ca";
    private static final String CERT_FILE_NAME = "/consul/config/cert";
    private static final String KEY_FILE_NAME = "/consul/config/key";

    private ConsulConfiguration consulConfiguration;
    private ConsulContainerOptions consulContainerOptions;
    private ConsulCommand consulCommand;
    private Integer waitTimeout;

    /**
     * Default constructor
     */
    public ConsulContainer() {
        super(CONSUL_IMAGE + ":" + CONSUL_VERSION);
        this.consulContainerOptions = new ConsulContainerOptions();
    }

    /**
     * Build Container with configuration
     * 
     * @param consulConfiguration - the consul configuration
     */
    public ConsulContainer(ConsulConfiguration consulConfiguration) {
        this();
        this.consulConfiguration = consulConfiguration;
    }

    /**
     * Build container
     * 
     * @param consulConfiguration - the consul configuration
     * @param consulContainerOptions - teh consul container options
     * @param consulCommand - the comsul command
     * @param containerVersion - the container version
     * @param waitTimeout - the container wait timeout
     */
    public ConsulContainer(ConsulConfiguration consulConfiguration, ConsulContainerOptions consulContainerOptions,
                           ConsulCommand consulCommand,
                           String containerVersion, Integer waitTimeout) {
        super(String.format("%s:%s", CONSUL_IMAGE, containerVersion != null ? containerVersion : CONSUL_VERSION));
        this.consulConfiguration = consulConfiguration;
        this.consulContainerOptions = consulContainerOptions;
        this.consulCommand = consulCommand;
        this.waitTimeout = waitTimeout;
    }

    @Override
    protected void configure() {
        copyFiles();
        bindPorts();
        setEnv();

        WaitStrategy wait = Wait.forHttp(HEALTH_CHECK_PATH)
                .forStatusCode(200)
                .forPort(getHttpPort());

        if (this.waitTimeout != null) {
            wait = wait.withStartupTimeout(Duration.ofSeconds(waitTimeout));
        }

        if (consulCommand != null) {
            withCommand(consulCommand.toCommand());
        }

        waitingFor(wait);
    }


    private void bindPorts() {
        if (this.consulConfiguration != null && this.consulConfiguration.getPorts() != null) {
            withExposedPorts(this.consulConfiguration.getPorts().getPortsToExpose());
        } else {
            // default ports
            withExposedPorts(DEFAULT_HTTP_PORT, DEFAULT_HTTPS_PORT, DEFAULT_DNS_PORT);
        }
    }

    private void setEnv() {
        for (ConsulContainerOption opt : ConsulContainerOption.values()) {
            withEnv(opt.getOptionName(),
                    this.consulContainerOptions.getOrDefault(opt.getOptionName(), opt.getDefaultValue()));
        }
        if (this.consulConfiguration != null) {
            withEnv(LOCAL_CONFIG_PARAM_NAME, new Gson().toJson(this.consulConfiguration));
        }
    }

    /**
     * copy tls config to the container
     */
    private void copyFiles() {
        if (this.consulConfiguration != null &&
                this.consulConfiguration.getTlsConfig() != null &&
                this.consulConfiguration.getTlsConfig().tlsEnabled()) {
            withCopyFileToContainer(MountableFile.forClasspathResource(this.consulConfiguration.getTlsConfig().getCaFile()),
                    CA_FILE_NAME);
            withCopyFileToContainer(MountableFile.forClasspathResource(this.consulConfiguration.getTlsConfig().getCertFile()),
                    CERT_FILE_NAME);
            withCopyFileToContainer(MountableFile.forClasspathResource(this.consulConfiguration.getTlsConfig().getKeyFile()),
                    KEY_FILE_NAME);

            this.consulConfiguration.setCaFile(CA_FILE_NAME);
            this.consulConfiguration.setCertFile(CERT_FILE_NAME);
            this.consulConfiguration.setKeyFile(KEY_FILE_NAME);
        }
    }

    /**
     * get the http port
     * @return Integer - the http port
     */    
    public Integer getHttpPort() {
        return this.consulConfiguration != null &&
                    this.consulConfiguration.getPorts() != null &&
                    this.consulConfiguration.getPorts().getHttpPort() != null ?
                this.consulConfiguration.getPorts().getHttpPort() :
                DEFAULT_HTTP_PORT;
    }


    /**
     * get the https port
     * @return Integer - the https port
     */
    public Integer getHttpsPort() {
        return this.consulConfiguration != null &&
                this.consulConfiguration.getPorts() != null &&
                this.consulConfiguration.getPorts().getHttpsPort() != null ?
                this.consulConfiguration.getPorts().getHttpsPort() :
                DEFAULT_HTTPS_PORT;
    }

    /**
     * get the DnsPort
     * @return Integer - the DnsPort
     */
    public Integer getDnsPort() {
        return this.consulConfiguration != null &&
                this.consulConfiguration.getPorts() != null &&
                this.consulConfiguration.getPorts().getDnsPort() != null ?
                this.consulConfiguration.getPorts().getDnsPort() :
                DEFAULT_DNS_PORT;
    }

    /**
     * sets the consul command for this container
     * @param consulCommand - the consul command
     */
    protected void setConsulCommand(ConsulCommand consulCommand) {
        this.consulCommand = consulCommand;
    }

    /**
     * get consul command for this container
     * @return ConsulCommand
     */
    protected ConsulCommand getConsulCommand() {
        return consulCommand;
    }
}

