package org.jetax.testcontainers.consul;

import org.junit.Test;

import java.util.UUID;

import static org.jetax.testcontainers.consul.ConsulConfiguration.*;
import static org.junit.Assert.*;

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
    public void testACLWorks() {
        // client
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