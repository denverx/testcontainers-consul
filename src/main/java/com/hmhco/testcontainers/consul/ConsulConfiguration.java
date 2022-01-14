package com.hmhco.testcontainers.consul;

import java.util.Objects;
import java.util.stream.Stream;

import org.testcontainers.shaded.org.apache.commons.lang.StringUtils;

import com.google.gson.annotations.SerializedName;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

/**
 * Consul container configuration class.
 * 
 * @author iyerk
 *
 */
@Data
public class ConsulConfiguration {
	
	/**
	 * Default no arg constructor
	 */
    public ConsulConfiguration() {
    }	
   
	/**
	 * datacenter configuration
	 * @param datacenter - the  datacenter
	 * @return datacenter - the datacenter
	 */
    private String datacenter;
    
    @SerializedName("node_name")

    /**
     * the consul node name
     * @param nodeName - the node name
     * @return nodeName - the node name
     */
    private String nodeName;

    @SerializedName("server")
    /**
     * isServer flag
     * 
     * @param isServer - boolean
     * @return isServer - boolean
     */
    private Boolean isServer;
    
    @Setter(AccessLevel.PROTECTED) @SerializedName("key_file")
    /**
     * the keyFile for TLS
     * @param keyFile - the keyFile
     * @return String - the keyFile
     */
    private String keyFile;
    
    /**
     * the certFile for TLS
     * @param certFile - the certFile
     * @return String - the certFile
     */
    @Setter(AccessLevel.PROTECTED) @SerializedName("cert_file")
    private String certFile;
    
    /**
     * the caFile for TLS
     * @param caFile - the caFile
     * @return String - the caFile
     */
    @Setter(AccessLevel.PROTECTED) @SerializedName("ca_file")
    private String caFile;
    
    /**
     * the logLevel for this container
     * @param logLevel the logLevel
     * @return String - the logLevel
     */
    @SerializedName("log_level")
    private String logLevel;
    
    @SerializedName("enable_debug")
    /**
     * enable debug for the container
     * @param enableDebug -  enable debug flag
     * @return Boolean - enableDebug
     */
    private Boolean enableDebug;
    
    @SerializedName("rejoin_after_leave")
    /**
     * Equivalent to the -rejoin command-line flag.
     * @param rejoinAfterLeave - rejoin
     * @return Boolean - rejoin
     */
    private Boolean rejoinAfterLeave;
    
    @SerializedName("primary_datacenter")
    /**
     * This designates the datacenter which is authoritative for ACL information
     * @param primaryDatacenter - the primary data center
     * @return String - the primary data center 
     */
    private String primaryDatacenter;
    
    @SerializedName("verify_incoming")
    /**
     * If set to true, Consul requires that all incoming connections 
     * make use of TLS
     * @param verifyIncoming - verifies incoming TLS
     * @return Boolean - verifyIncoming
     */
    private Boolean verifyIncoming;
    
    @SerializedName("verify_outgoing")
    /**
     * If set to true, Consul requires that all outgoing connections from this agent 
     * make use of TLS
     * @param verifyOutgoing - verifies outgoing TLS
     * @return Boolean - verifyOutgoing
     */
    private Boolean verifyOutgoing;

    @SerializedName("ports")
    /**
     * Ports for consul
     * @param ports - ports to use for this configuration 
     * @return Ports - ports to use for this configuration 
     */
    private Ports ports;
    
    @SerializedName("acl")
    /**
     * acl for this configuration
     * @param acl - acl for this configuration
     * @return ACL - acl for this configuration
     */
    private ACL acl;
    
    @SerializedName("dns")
    /**
     * DNS to use for this configuration
     * @param dns - dns resolution configuration
     * @return DNS - dns resolution configuration  
     */
    private DNS dns;

    /**
     * @param tlsConfig -  TLS Config
     * @return TLSConfig - TLS Config
     */
    private transient TLSConfig tlsConfig;

    // region deprecated
    /** Below field are deprecated since 1.4.0
     * {@link ACL} should be used since 1.4.0
     */
    @Deprecated
    @SerializedName("acl_agent_master_token")
    /**
     * @param aclAgentMasterToken - agent master token
     * @return String
     */
    private String aclAgentMasterToken;

    @Deprecated
    @SerializedName("acl_agent_token")
    /**
     * @param aclAgentToken - agent token
     * @return String
     */
    private String aclAgentToken;

    @Deprecated
    @SerializedName("acl_datacenter")
    /**
     * This designates the datacenter which is authoritative for ACL information
     * @param aclDatacenter - acl datacenter
     * @return String
     */    
    private String aclDatacenter;

    @Deprecated
    @SerializedName("acl_default_policy")
    /**
     * @param aclDefaultPolicy - acl default policy
     * @return String
     */
    private String aclDefaultPolicy;

    @Deprecated
    @SerializedName("acl_down_policy")
    /**
     * @param aclDownPolicy - acl down policy
     * @return String
     */    
    private String aclDownPolicy;

    @Deprecated
    @SerializedName("acl_master_token")
    /**
     * @param aclMasterToken - acl master token
     * @return String
     */        
    private String aclMasterToken;

    @Deprecated
    @SerializedName("acl_replication_token")
    /**
     * @param aclReplicationToken - acl replication token
     * @return String
     */            
    private String aclReplicationToken;

    /**
     * Ports configuration for Consul
     * @author iyerk
     *
     */
    @Data
    public static class Ports {
    	
    	/**
    	 * Default no arg constructor
    	 */
    	public Ports() {
    		
    	}
        @SerializedName("http")
        /**
         * http Port for consul
         * @param httpPort - the http port
         * @return httpPort - the http port
         */
        private Integer httpPort;
        
        @SerializedName("https")
        /**
         * https Port for consul
         * @param httpsPort - the https port
         * @return httpsPort - the https port
         */
        private Integer httpsPort;

        @SerializedName("dns")
        /**
         * Dns Port for consul
         * @param dnsPort - the dns port
         * @return dnsPort - the dns port
         */
        private Integer dnsPort;

        /**
         * an array of configured ports to expose 
         * @return Integer[] - ports to expose
         */
        public Integer[] getPortsToExpose() {
            return Stream.of(httpPort, httpsPort, dnsPort)
                    .filter(Objects::nonNull)
                    .toArray(Integer[]::new);
        }
    }

    /**
     * An ACL representation for consul
     * @author iyerk
     *
     */
    @Data
    public static class ACL {
    	
    	/**
    	 * acl enabled flag
    	 * @param enabled - acl enabled
    	 * @return Boolean
    	 */
        private Boolean enabled;
        
        @SerializedName("enable_token_replication")
        /**
         * tokenReplication enabled
         * @param tokenReplication - tokenReplication enabled
         * @return Boolean
         */
        private Boolean tokenReplication;
        
        @SerializedName("policy_ttl")
        /**
         * ttl for policy
         * @param policyTTL - ttl for policy
         * @return String
         */
        private String policyTTL;
        
        @SerializedName("token_ttl")
        /**
         * ttl for token
         * @param tokenTTL - ttl for token
         * @return String
         */
        private String tokenTTL;
        
        @SerializedName("default_policy")
        /**
         * default acl policy
         * @param defaultPolicy - default acl policy
         * @return String - default acl policy
         */
        private String defaultPolicy;
        
        @SerializedName("down_policy")
        /**
         * Default down policy for usecases where policy cannot be read.
         * @param downPolicy - down policy
         * @return String - down policy
         */
        private String downPolicy;
        
        @SerializedName("tokens")
        /**
         * ACL tokens
         * @param tokens - acl tokens
         * @return Tokens
         */
        private Tokens tokens;

        /**
         * Default constructor
         */
        public ACL() {
            this.tokens = new Tokens();
        }
    }

    /**
     * A consul tokens representation
     * @author iyerk
     *
     */
    @Data
    public static class Tokens {

    	/**
    	 * Default no args constructor
    	 */
    	public Tokens() {
    		
    	}
    	/**
    	 * agent token
    	 * @param agent - agent token
    	 * @return String - agent token
    	 */
    	private String agent;

    	@SerializedName("agent_master")
    	/**
    	 * agent master token
    	 * @param agentMaster - the agent master token
    	 * @return String - the agent master token
    	 */
    	private String agentMaster;

    	@SerializedName("default")
    	/**
    	 * default Token
    	 * @param defaultToken - the default token
    	 * @return String - the default token 
    	 */
        private String defaultToken;

    	/**
    	 * master token
    	 * @param master - the master token
    	 * @return String - the master token
    	 */
    	private String master;
    	
    	/**
    	 * replication token
    	 * @param replication - the replication token
    	 * @return String - the replication token
    	 */
        private String replication;
    }

    /**
     * A DNS configuration for consul that determines how DNS queries are serviced.
     * @author iyerk
     *
     */
    @Data
    public static class DNS {
    	/**
    	 * Default no arg constructor
    	 */
    	public DNS() {
        }
    	
        @SerializedName("allow_stale")
        /**
         *  Enables a stale query for DNS information
         *  @param allowStale - allow stale dns resolution
         *  @return Boolean
         */
        private Boolean allowStale;
        
        @SerializedName("disable_compression")
        /**
         * If set to true, DNS responses will not be compressed
         * @param disableCompression - compression flag
         * @return Boolean
         */
        private Boolean disableCompression;
        
        @SerializedName("enable_truncate")
        /**
         * If set to true, a UDP DNS query that would return more than 3 records,
         * or more than would fit into a valid UDP response, will set the truncated flag,
         * indicating to clients that they should re-query using TCP to get the full set of records
         * @param enableTruncate - enable truncate for DNS
         * @return Boolean
         */
        private Boolean enableTruncate;
        
        @SerializedName("max_stale")
        /**
         *  This lets Consul continue serving requests in long outage scenarios 
         *  where no leader can be elected.
         *  @param maxStale - max stale results
         *  @return String
         */
        private String maxStale;
        
        @SerializedName("node_ttl")
        /**
         * By default, this is "0s", so all node lookups are served with a 0 TTL value. 
         * DNS caching for node lookups can be enabled by setting this value. 
         * This should be specified with the "s" suffix for second or "m" for minute.
         * @param nodeTTL - node TTLS value
         * @return String
         */
        private String nodeTTL;
        
        @SerializedName("only_passing")
        /**
         *  If set to true, any nodes whose health checks are warning or critical 
         *  will be excluded from DNS results
         *  @param onlyPassing - exclude unhealthy nodes
         *  @return String
         */
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
    public static class TLSConfig {

    	/**
    	 * Default no arg constructor
    	 */
    	public TLSConfig() {
    		
    	}
        /**
         * caFile for this TLSConfig
         * 
         * @param caFile - the caFile for TLSConfig
         * @return String - the caFile for TLSConfig
         */
    	private String caFile;
    	
        /**
         * certFile for this TLSConfig
         * 
         * @param certFile - the certFile for TLSConfig
         * @return String - the certFile for TLSConfig
         */
        private String certFile;

        /**
         * keyFile for this TLSConfig
         * 
         * @param keyFile - the keyFile for TLSConfig
         * @return String - the keyFile for TLSConfig
         */
        private String keyFile;

        /**
         * tlsEnabled flag
         * @return boolean - tlsEnabled
         */
        public boolean tlsEnabled() {
            return StringUtils.isNotEmpty(keyFile) && StringUtils.isNotEmpty(certFile) && StringUtils.isNotEmpty(caFile);
        }
    }
}
