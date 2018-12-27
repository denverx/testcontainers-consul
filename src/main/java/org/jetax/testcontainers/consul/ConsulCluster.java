package org.jetax.testcontainers.consul;

import com.github.dockerjava.api.model.ContainerNetwork;
import org.testcontainers.containers.Network;
import org.testcontainers.shaded.com.google.common.collect.ImmutableList;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ConsulCluster {

    private int size;
    private Supplier<ConsulContainer> containerCreator;
    private ConsulContainer[] containers;
    private Network network;

    public ConsulCluster(int size, Supplier<ConsulContainer> containerCreator, Network network) {
        this.size = size;
        this.containers = new ConsulContainer[size];
        this.containerCreator = containerCreator;
        this.network = network;
    }

    public void start() {
        String firstAddr = bootstrapFirst();

        for (int i = 1; i < size; i++) {
            containers[i] = bootstrapNext(firstAddr);
        }
    }

    public void stop() {
        for (ConsulContainer consulContainer : containers) {
            if (consulContainer != null) {
                consulContainer.stop();
            }
        }
    }


    public List<ConsulContainer> getContainers() {
        return Arrays.asList(containers);
    }

    private String bootstrapFirst() {
        ConsulContainer first = containerCreator.get();
        if (this.network != null) first.withNetwork(network);
        first.start();
        containers[0] = first;
        Map<String, ContainerNetwork> nets = first.getContainerInfo().getNetworkSettings().getNetworks();
        if (network != null) {
            if (nets.containsKey(network.getId())) {
                return nets.get(network.getId()).getIpAddress();
            }
        }
        // if no networks specified explicitly, we do not know, which one to choose
        return nets.values().stream().findFirst().get().getIpAddress();
    }

    private ConsulContainer bootstrapNext(String address) {
        ConsulContainer container = containerCreator.get();
        if (this.network != null) container.withNetwork(network);
        ConsulCommand command = container.getConsulCommand();
        command.setJoin(address);
        command.setRetryJoin(true);
        container.start();
        return container;
    }


}
