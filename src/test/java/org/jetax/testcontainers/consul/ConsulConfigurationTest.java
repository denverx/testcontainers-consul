package org.jetax.testcontainers.consul;

import com.google.gson.Gson;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

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

}