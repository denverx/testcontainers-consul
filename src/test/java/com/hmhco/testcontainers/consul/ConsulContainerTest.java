package com.hmhco.testcontainers.consul;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.UUID;

import org.junit.Test;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.OperationException;
import com.ecwid.consul.v1.kv.model.PutParams;
import com.hmhco.testcontainers.consul.ConsulConfiguration.ACL;
import com.hmhco.testcontainers.consul.ConsulConfiguration.Ports;
import com.hmhco.testcontainers.consul.ConsulConfiguration.Tokens;

public class ConsulContainerTest {

    private static final String DEFAULT_DC = "dc";

    @Test
    public void testContainerIsRunAndWaitSucceeds() {
        ConsulContainer cc = new ConsulContainer();
        cc.start();
    }

    @Test
    public void testContainerWithTokenIsRunAndWaitSucceeds() {
        ConsulConfiguration config = new ConsulConfiguration();
        config.setAcl(getAclWithTokens());
        config.setDatacenter(DEFAULT_DC);
        config.setPrimaryDatacenter(DEFAULT_DC);

        ConsulContainer cc = new ConsulContainer(config);
        cc.start();
    }

    @Test
    public void testContainerWithCustomPortsIsRunAndWaitSucceeds() {
        Ports ports = new Ports();
        ports.setHttpPort(8080);

        ConsulConfiguration config = new ConsulConfiguration();
        config.setPorts(ports);

        ConsulContainer cc = new ConsulContainer(config);
        cc.start();

        assertTrue(cc.getExposedPorts().contains(8080));
    }

    @Test
    public void testContainerWithLegacyACLIsRunAndWaitSucceeds() {
        ConsulConfiguration config = new ConsulConfiguration();
        config.setDatacenter(DEFAULT_DC);
        config.setAclAgentMasterToken(UUID.randomUUID().toString());
        config.setAclDatacenter(DEFAULT_DC);
        config.setAclDefaultPolicy("deny");

        ConsulContainer cc = new ConsulContainerBuilder()
                .withConfig(config)
                .withContainerVersion("1.3.0")
                .build();

        cc.start();
    }

    @Test
    public void testACLWorks() {
        // given
        ConsulConfiguration config = new ConsulConfiguration();
        config.setAcl(getAclWithTokens());
        config.setDatacenter(DEFAULT_DC);
        config.setPrimaryDatacenter(DEFAULT_DC);

        ConsulContainer cc = new ConsulContainer(config);
        cc.start();

        ConsulClient client = new ConsulClient(cc.getContainerIpAddress(), cc.getMappedPort(cc.getHttpPort()));

        // when
        Exception actualException = null;
        try {
            client.setKVValue("1", "1").getValue();
        } catch (Exception e) {
            actualException = e;
        }

        Boolean savedWithToken = client.setKVValue("1", "1", config.getAcl().getTokens().getMaster(),
                new PutParams()).getValue();

        assertTrue(savedWithToken);
        assertTrue(actualException instanceof OperationException);
        assertEquals(403, ((OperationException) actualException).getStatusCode());
    }

    @Test
    public void testLegacyACLWorks() {
        // given
        ConsulConfiguration config = new ConsulConfiguration();
        config.setDatacenter(DEFAULT_DC);
        config.setAclMasterToken(UUID.randomUUID().toString());
        config.setAclDatacenter(DEFAULT_DC);
        config.setAclDefaultPolicy("deny");

        ConsulContainer cc = new ConsulContainerBuilder()
                .withConfig(config)
                .withContainerVersion("1.3.0")
                .build();

        cc.start();

        ConsulClient client = new ConsulClient(cc.getContainerIpAddress(), cc.getMappedPort(cc.getHttpPort()));

        // when
        Exception actualException = null;
        try {
            client.setKVValue("1", "1").getValue();
        } catch (Exception e) {
            actualException = e;
        }

        Boolean savedWithToken = client.setKVValue("1", "1", config.getAclMasterToken(),
                new PutParams()).getValue();

        assertTrue(savedWithToken);
        assertTrue(actualException instanceof OperationException);
        assertEquals(403, ((OperationException) actualException).getStatusCode());
    }

    private ACL getAclWithTokens() {
        Tokens tokens = new Tokens();
        tokens.setMaster(UUID.randomUUID().toString());
        tokens.setDefaultToken(UUID.randomUUID().toString());
        tokens.setAgent(UUID.randomUUID().toString());
        tokens.setReplication(UUID.randomUUID().toString());

        ACL acl = new ACL();
        acl.setEnabled(true);
        acl.setDefaultPolicy("deny");
        acl.setTokens(tokens);

        return acl;
    }




}