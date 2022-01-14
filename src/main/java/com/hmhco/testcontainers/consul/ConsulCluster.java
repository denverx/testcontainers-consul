package com.hmhco.testcontainers.consul;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.testcontainers.containers.Network;

import com.github.dockerjava.api.model.ContainerNetwork;

/**
 * A consul cluster representation
 * 
 * @author iyerk
 *
 */
public class ConsulCluster {

    private int size;
    private Supplier<ConsulContainer> containerCreator;
    private ConsulContainer[] containers;
    private Network network;

    /**
     * construct consul cluster for the given size, container and network.
     * @param size - cluster size
     * @param containerCreator - container creator
     * @param network - network for the container
     */
    public ConsulCluster(int size, Supplier<ConsulContainer> containerCreator, Network network) {
        this.size = size;
        this.containers = new ConsulContainer[size];
        this.containerCreator = containerCreator;
        this.network = network;
    }

    /**
     * start cluster
     */
    public void start() {
        String firstAddr = bootstrapFirst();

        for (int i = 1; i < size; i++) {
            containers[i] = bootstrapNext(firstAddr);
        }
    }

    /**
     * stop cluster
     */
    public void stop() {
        for (ConsulContainer consulContainer : containers) {
            if (consulContainer != null) {
                consulContainer.stop();
            }
        }
    }


    /**
     * get containers in the cluster
     * @return - containers in the cluster
     */
    public List<ConsulContainer> getContainers() {
        return Arrays.asList(containers);
    }

    /**
     * bootstrap initial container
     * @return container address
     */
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

    /**
     * bootstrap cluster members
     * @param address - container address
     * @return container - cluster member
     */
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

