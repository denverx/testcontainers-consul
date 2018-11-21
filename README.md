## Consul for test-containers

This repository provides consul support for test-containers, based on the
official hashicorp image from dockerhub.


##### Features

- support for ACLs
- consul configuration as an object
- support for both new (>= 1.4.0) and legacy (< 1.4.0) ACL config
- TLS support


##### Examples
Simply running container:
```java
ConsulContainer cc = new ConsulContainer();
cc.start();
```

Adding custom configuration:
```java
// set master token
ConsulConfiguration.Tokens tokens = new ConsulConfiguration.Tokens();
tokens.setMaster(UUID.randomUUID().toString());

// enable ACLs
ConsulConfiguration.ACL acl = new ConsulConfiguration.ACL();
acl.setEnabled(true);
acl.setDefaultPolicy("deny");
acl.setDownPolicy("deny");
acl.setTokens(tokens);

// build config
ConsulConfiguration ccf = new ConsulConfiguration();
ccf.setDatacenter("dc");
ccf.setPrimaryDatacenter("dc");
ccf.setAcl(acl);

ConsulContainer cc = new ConsulContainer(ccf); // apply config
cc.start();
```
TLS support:
```java
// resources are taken from classpath
ConsulConfiguration.TLSConfig tlsConfig = new ConsulConfiguration.TLSConfig();
tlsConfig.setCaFile("tls/ca.cert");
tlsConfig.setKeyFile("tls/consul.key");
tlsConfig.setCertFile("tls/consul.cert");

ConsulConfiguration config = new ConsulConfiguration();
config.setDatacenter(DEFAULT_DC);
config.setTlsConfig(tlsConfig);
config.setVerifyIncoming(true);
config.setVerifyOutgoing(true);

Ports ports = new Ports();
ports.setHttpsPort(8501);
config.setPorts(ports);

ConsulContainer cc = new ConsulContainer(ccf);
cc.start();
```
