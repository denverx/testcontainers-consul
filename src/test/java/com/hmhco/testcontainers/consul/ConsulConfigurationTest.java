package com.hmhco.testcontainers.consul;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.UUID;

import org.junit.Test;

import com.google.gson.Gson;

public class ConsulConfigurationTest {

    private final Gson gson = new Gson();

    @Test
    public void testNullsAreNotSerializedWithDefaultGsonConfiguration() {
        // given
        ConsulConfiguration consulConfiguration = new ConsulConfiguration();
        consulConfiguration.setNodeName("my-node");

        // when
        String json = gson.toJson(consulConfiguration);

        // then
        assertEquals("{\"node_name\":\"my-node\"}", json);
    }

    @Test
    public void testLegacyAclIsSerialized() {
        // given
        ConsulConfiguration consulConfiguration = new ConsulConfiguration();
        consulConfiguration.setAclDatacenter("dc");
        consulConfiguration.setAclMasterToken("token");
        consulConfiguration.setAclDefaultPolicy("deny");

        // when
        String json = gson.toJson(consulConfiguration);

        // then
        assertEquals("{" +
                            "\"acl_datacenter\":\"dc\"," +
                            "\"acl_default_policy\":\"deny\"," +
                            "\"acl_master_token\":\"token\"" +
                            "}",
                json);
    }

    @Test
    public void testConfigurationShortcuts() {
        // given
        String masterToken = UUID.randomUUID().toString();
        String agentToken = UUID.randomUUID().toString();
        String replicationToken = UUID.randomUUID().toString();
        String aclDefaultPolicy = "deny";

        ConsulContainerBuilder containerBuilder = new ConsulContainerBuilder();
        containerBuilder
                .withACLEnabled()
                .withMasterToken(masterToken)
                .withAgentToken(agentToken)
                .withReplicationToken(replicationToken)
                .withACLDefaultPolicy(aclDefaultPolicy);


        // when
        ConsulConfiguration config = containerBuilder.buildConfig();

        // then
        assertNotNull(config.getAcl());
        assertTrue(config.getAcl().getEnabled());
        assertEquals(aclDefaultPolicy, config.getAcl().getDefaultPolicy());
        assertNotNull(config.getAcl().getTokens());
        assertEquals(masterToken, config.getAcl().getTokens().getMaster());
        assertEquals(agentToken, config.getAcl().getTokens().getAgent());
        assertEquals(replicationToken, config.getAcl().getTokens().getReplication());
    }

}