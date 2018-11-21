package org.jetax.testcontainers.consul;

import com.google.gson.annotations.SerializedName;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.testcontainers.shaded.org.apache.commons.lang.StringUtils;

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
    @Setter(AccessLevel.PROTECTED) @SerializedName("key_file")
    private String keyFile;
    @Setter(AccessLevel.PROTECTED) @SerializedName("cert_file")
    private String certFile;
    @Setter(AccessLevel.PROTECTED) @SerializedName("ca_file")
    private String caFile;
    @SerializedName("log_level")
    private String logLevel;
    @SerializedName("enable_debug")
    private Boolean enableDebug;
    @SerializedName("rejoin_after_leave")
    private Boolean rejoinAfterLeave;
    @SerializedName("primary_datacenter")
    private String primaryDatacenter;
    @SerializedName("verify_incoming")
    private Boolean verifyIncoming;
    @SerializedName("verify_outgoing")
    private Boolean verifyOutgoing;

    @SerializedName("ports")
    private Ports ports;
    @SerializedName("acl")
    private ACL acl;
    @SerializedName("dns")
    private DNS dns;

    private transient TLSConfig tlsConfig;

    // region deprecated
    /** Below field are deprecated since 1.4.0
     * {@link ACL} should be used since 1.4.0
     */
    @Deprecated
    @SerializedName("acl_agent_master_token")
    private String aclAgenMasterToken;

    @Deprecated
    @SerializedName("acl_agent_token")
    private String aclAgenToken;

    @Deprecated
    @SerializedName("acl_datacenter")
    private String aclDatacenter;

    @Deprecated
    @SerializedName("acl_default_policy")
    private String aclDefaultPolicy;

    @Deprecated
    @SerializedName("acl_down_policy")
    private String aclDownPolicy;

    @Deprecated
    @SerializedName("acl_master_token")
    private String aclMasterToken;

    @Deprecated
    @SerializedName("acl_replication_token")
    private String aclReplicationToken;

    // endregion

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

    /**
     * We can't use {@link ConsulConfiguration#caFile}, {@link ConsulConfiguration#certFile} and
     * {@link ConsulConfiguration#keyFile} as it is serialized to consul config, but we have to preserve
     * mapping from classpath resource to file in container.
     * Filenames from {@link TLSConfig} are copied to container and then automatically set up in
     * {@link ConsulConfiguration}.
     */
    @Data
    @NoArgsConstructor
    public static class TLSConfig {
        private String caFile;
        private String certFile;
        private String keyFile;

        public boolean tlsEnabled() {
            return StringUtils.isNotEmpty(keyFile) && StringUtils.isNotEmpty(certFile) && StringUtils.isNotEmpty(caFile);
        }
    }
}
