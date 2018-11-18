package org.jetax.testcontainers.consul;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.stream.Stream;

@Data
@NoArgsConstructor
public class ConsulConfiguration {
    private String datacenter;
    @SerializedName("node_name")
    private String nodeName;
    @SerializedName("server")
    private Boolean isServer;
    @SerializedName("key_file")
    private String keyFile;
    @SerializedName("cert_file")
    private String certFile;
    @SerializedName("ca_file")
    private String caFile;
    @SerializedName("log_level")
    private String logLevel;
    @SerializedName("enable_debug")
    private Boolean enableDebug;
    @SerializedName("rejoin_after_leave")
    private Boolean rejoinAfterLeave;
    @SerializedName("primary_datacenter")
    private String primaryDatacenter;

    @SerializedName("ports")
    private Ports ports;
    @SerializedName("acl")
    private ACL acl;
    @SerializedName("dns")
    private DNS dns;

    @Data
    @NoArgsConstructor
    public static class Ports {
        @SerializedName("http")
        private Integer httpPort;
        @SerializedName("https")
        private Integer httpsPort;
        @SerializedName("dns")
        private Integer dnsPort;

        public Integer[] getPortsToExpose() {
            return Stream.of(httpPort, httpsPort, dnsPort)
                    .filter(Objects::nonNull)
                    .toArray(Integer[]::new);
        }

    }

    @Data
    @NoArgsConstructor
    public static class ACL {
        private Boolean enabled;
        @SerializedName("enable_token_replication")
        private Boolean tokenReplication;
        @SerializedName("policy_ttl")
        private String policyTTL;
        @SerializedName("token_ttl")
        private String tokenTTL;
        @SerializedName("default_policy")
        private String defaultPolicy;
        @SerializedName("down_policy")
        private String downPolicy;
        @SerializedName("tokens")
        private Tokens tokens;
    }

    @Data
    @NoArgsConstructor
    public static class Tokens {
        private String agent;
        @SerializedName("agent_master")
        private String agentMaster;
        @SerializedName("default")
        private String defaultToken;
        private String master;
        private String replication;
    }

    @Data
    @NoArgsConstructor
    public static class DNS {
        @SerializedName("allow_stale")
        private Boolean allowStale;
        @SerializedName("disable_compression")
        private Boolean disableCompression;
        @SerializedName("enable_truncate")
        private Boolean enableTruncate;
        @SerializedName("max_stale")
        private String maxStale;
        @SerializedName("node_ttl")
        private String nodeTTL;
        @SerializedName("only_passing")
        private String onlyPassing;
    }
}
