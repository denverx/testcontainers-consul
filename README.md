## Consul for test-containers

This repository provides consul support for test-containers, based on the
official hashicorp image from dockerhub.


##### Features

- support for token
- consul configuration as an object


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
