package org.jetax.testcontainers.consul;

import com.google.gson.Gson;
import org.jetax.testcontainers.consul.ConsulContainerOptions.ConsulContainerOption;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.containers.wait.strategy.WaitStrategy;

import java.time.Duration;

import static org.jetax.testcontainers.consul.ConsulContainerOptions.LOCAL_CONFIG_PARAM_NAME;

public class ConsulContainer extends GenericContainer<ConsulContainer> {

    private static final String CONSUL_IMAGE = "consul";
    private static final String CONSUL_VERSION = "1.4.0-rc1";

    private static final Integer DEFAULT_HTTP_PORT = 8500;
    private static final Integer DEFAULT_HTTPS_PORT = 8501;
    private static final Integer DEFAULT_DNS_PORT = 8600;

    private static final String HEALTH_CHECK_PATH = "/v1/status/leader";

    private ConsulConfiguration consulConfiguration;
    private ConsulContainerOptions consulContainerOptions;
    private Integer waitTimeout;

    public ConsulContainer() {
        super(CONSUL_IMAGE + ":" + CONSUL_VERSION);
        this.consulContainerOptions = new ConsulContainerOptions();
    }

    public ConsulContainer(ConsulConfiguration consulConfiguration) {
        this();
        this.consulConfiguration = consulConfiguration;
    }

    public ConsulContainer(ConsulContainerOptions options) {
        this();
        this.consulContainerOptions = options;
    }

    public ConsulContainer(ConsulConfiguration consulConfiguration, ConsulContainerOptions consulContainerOptions,
                           String containerVersion, Integer waitTimeout) {
        super(String.format("%s:%s", CONSUL_IMAGE, containerVersion != null ? containerVersion : CONSUL_VERSION));
        this.consulConfiguration = consulConfiguration;
        this.consulContainerOptions = consulContainerOptions;
        this.waitTimeout = waitTimeout;
    }

    @Override
    protected void configure() {
        bindPorts();
        setEnv();

        WaitStrategy wait = Wait.forHttp(HEALTH_CHECK_PATH)
                .forStatusCode(200)
                .forPort(getHttpPort());

        if (this.waitTimeout != null) {
            wait = wait.withStartupTimeout(Duration.ofSeconds(waitTimeout));
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

    public Integer getHttpPort() {
        return this.consulConfiguration != null &&
                    this.consulConfiguration.getPorts() != null &&
                    this.consulConfiguration.getPorts().getHttpPort() != null ?
                this.consulConfiguration.getPorts().getHttpPort() :
                DEFAULT_HTTP_PORT;
    }

    public Integer getHttpsPort() {
        return this.consulConfiguration != null &&
                this.consulConfiguration.getPorts() != null &&
                this.consulConfiguration.getPorts().getHttpsPort() != null ?
                this.consulConfiguration.getPorts().getHttpsPort() :
                DEFAULT_HTTPS_PORT;
    }

    public Integer getDnsPort() {
        return this.consulConfiguration != null &&
                this.consulConfiguration.getPorts() != null &&
                this.consulConfiguration.getPorts().getDnsPort() != null ?
                this.consulConfiguration.getPorts().getDnsPort() :
                DEFAULT_DNS_PORT;
    }
}
