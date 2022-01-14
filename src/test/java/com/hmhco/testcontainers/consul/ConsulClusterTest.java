package com.hmhco.testcontainers.consul;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.Test;
import org.testcontainers.containers.ContainerState;
import org.testcontainers.containers.Network;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.OperationException;
import com.ecwid.consul.v1.catalog.CatalogServiceRequest;
import com.ecwid.consul.v1.catalog.model.CatalogService;

public class ConsulClusterTest {

    private static final String DEFAULT_DC = "dc";

    @Test
    public void testClusterCanBeRun() throws Exception {
        // given
        Integer clusterSize = 3;

        ConsulConfiguration config = new ConsulConfiguration();
        config.setDatacenter(DEFAULT_DC);

        ConsulCluster cluster = new ConsulContainerBuilder()
                .withConfig(config)
                .withContainerVersion("1.3.0")
                .cluster(clusterSize);

        // when
        cluster.start();

        // then
        List<ConsulContainer> containers = cluster.getContainers();
        assertEquals(clusterSize, Integer.valueOf(containers.size()));
        assertTrue(containers.stream().allMatch(ContainerState::isRunning));

        ConsulContainer anchor = containers.get(0);
        ConsulClient consulClient = new ConsulClient(anchor.getContainerIpAddress(), anchor.getMappedPort(anchor.getHttpPort()));

        List<CatalogService> services = consulClient.getCatalogService("consul",
                    CatalogServiceRequest.newBuilder().build().newBuilder().setDatacenter(DEFAULT_DC).build())
                .getValue();
        assertEquals(3, services.size());

        // finally
        cluster.stop();
    }

    @Test
    public void testClusterCanBeRunWithCustomNetwork() throws Exception {
        // given
        Integer clusterSize = 3;

        Network consulClusterNetwork = Network.newNetwork();

        ConsulConfiguration config = new ConsulConfiguration();
        config.setDatacenter(DEFAULT_DC);

        ConsulCluster cluster = new ConsulContainerBuilder()
                .withConfig(config)
                .withContainerVersion("1.3.0")
                .cluster(clusterSize, consulClusterNetwork);

        // when
        cluster.start();

        // then
        List<ConsulContainer> containers = cluster.getContainers();
        assertEquals(clusterSize, Integer.valueOf(containers.size()));
        assertTrue(containers.stream().allMatch(ContainerState::isRunning));

        ConsulContainer anchor = containers.get(0);
        ConsulClient consulClient = new ConsulClient(anchor.getContainerIpAddress(), anchor.getMappedPort(anchor.getHttpPort()));

        List<CatalogService> services = consulClient.getCatalogService("consul",
                CatalogServiceRequest.newBuilder().build().newBuilder().setDatacenter(DEFAULT_DC).build())
                .getValue();
        assertEquals(3, services.size());

        // finally
        cluster.stop();
    }

    @Test
    public void testClusterPropagatesConfigurationToAllNodes() throws Exception {
        // given
        Integer clusterSize = 3;
        String masterToken = UUID.randomUUID().toString();

        ConsulCluster cluster = new ConsulContainerBuilder()
                .withDatacenter(DEFAULT_DC)
                .withACLEnabled()
                .withACLDefaultPolicy("deny")
                .withMasterToken(masterToken)
                .withContainerVersion("1.4.0")
                .cluster(clusterSize);

        // when
        cluster.start();

        // then
        List<ConsulContainer> containers = cluster.getContainers();
        assertEquals(clusterSize, Integer.valueOf(containers.size()));
        assertTrue(containers.stream().allMatch(ContainerState::isRunning));

        List<ConsulClient> consulClients = containers.stream()
            .map(c -> new ConsulClient(c.getContainerIpAddress(), c.getMappedPort(c.getHttpPort())))
            .collect(Collectors.toList());

        for (ConsulClient client: consulClients) {
            List<CatalogService> services = client.getCatalogService("consul",
                    CatalogServiceRequest.newBuilder().build().newBuilder().setDatacenter(DEFAULT_DC).build())
                    .getValue();
            assertEquals(0, services.size());

            OperationException ex = null;
            try {
                client.setKVValue("consul", String.valueOf(client.hashCode()));
            } catch (OperationException e) {
                ex = e;
            }
            assertNotNull(ex);
            assertEquals(403, ex.getStatusCode());
        }

        // finally
        cluster.stop();
    }
}
