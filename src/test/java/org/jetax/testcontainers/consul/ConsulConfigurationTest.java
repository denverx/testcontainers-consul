package org.jetax.testcontainers.consul;

import com.google.gson.Gson;
import org.junit.Test;

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

}