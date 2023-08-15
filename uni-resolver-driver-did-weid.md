# uni-resolver-driver-did-weid
A Universal Resolver driver for did:weid identifiers.
## 简述
本项目基于 weid-java-sdk 实现了 [DIF](identity.foundation) 的 Universal Resolver Driver for did:weid, 对于在多种 DID Method 兼容的场景下，可以使用 使用 Universal Resolver 加载本 Driver 快速接入 did:weid的解析服务。
考虑到 WeIdentity 主要依托联盟链的形态存在，因此本 Driver 可能适用于联盟链自身的域内工作，若想要贡献给 DIF 社区，还需要进一步的工作。例如编写符合 W3C 规范的DID Method 文档，注册[DID Specification Registry](https://w3c.github.io/did-spec-registries/)等，相关工作尚需fiscobcos社区及各方人士共同努力。

## 项目仓库

[https://github.com/h3ar7dump/uni-resolver-driver-did-weid](https://github.com/h3ar7dump/uni-resolver-driver-did-weid)

## 项目详情

### Specifications

- [Decentralized Identifiers](https://w3c.github.io/did-core/)
- [DID Method Specification](https://weidentity.readthedocs.io/zh_CN/latest/docs/weidentity-spec.html)

### Example DIDs

```text
did:weid:101:0x0086eb1f712ebc6f1c276e12ec21
did:weid:iot:0x772b4982bebc911b66cf0793de3efe463e442af8
```

### Project Structure

```text
.
├── LICENSE
├── README.md
├── build.gradle
├── gradle
├── gradlew
├── gradlew.bat
├── settings.gradle
└── src
    ├── main
    │   ├── java
    │   │   └── uniresolver
    │   │       └── driver
    │   │           └── did
    │   │               └── weid
    │   │                   └── DIDWeidDriver.java
    │   └── resources
    │       ├── conf
    │       │   ├── amop
    │       │   │   ├── consumer_private_key.p12
    │       │   │   └── consumer_public_key.pem
    │       │   └── gm
    │       │       ├── sm_ca.crt
    │       │       ├── sm_ensdk.crt
    │       │       ├── sm_ensdk.key
    │       │       ├── sm_sdk.crt
    │       │       └── sm_sdk.key
    │       ├── fisco.properties
    │       └── weidentity.properties
    └── test
        ├── java
        │   └── uniresolver
        │       └── examples
        │           └── TestLocalUniResolver.java
        └── resources
```

### Build and Run (Docker)

```bash
docker build -f ./docker/Dockerfile . -t universalresolver/driver-did-weid
docker run -p 8080:8080 universalresolver/driver-did-weid

# use weid in you scope to test
curl -X GET http://localhost:8080/1.0/identifiers/did:weid:iot:0x772b4982bebc911b66cf0793de3efe463e442af8
```

### Resolution Resulte Example

```json
{
  "@context": "https://w3id.org/did-resolution/v1",
  "didDocument": {
    "@context": "https://github.com/WeBankFinTech/WeIdentity/blob/master/context/v1",
    "id": "did:weid:iot:0x772b4982bebc911b66cf0793de3efe463e442af8",
    "authentication": [
      {
        "type": "SM2VerificationKey",
        "id": "did:weid:iot:0x772b4982bebc911b66cf0793de3efe463e442af8#keys-2a8ee6ba",
        "controller": "did:weid:iot:0x772b4982bebc911b66cf0793de3efe463e442af8",
        "publicKeyMultibase": "z5rfyeaTovNZjGnJsixwHXgWTKPLhTwFJWYYAMGGpLAmDKaz7N6N5NUZouLzsP1Nx55tsTgEYtwuUsmr7FWnarpGvsCHhKarmWD9J1J8oLJDgcvDqpN5dqmt3CeF5wAcwtCt8dR5oGjEpxgjth6rrdr5hqw6Gm4ajk4gydje3vwBndo3XrFGEpQgxtGg2tE8HtHUDMAbu1X75LS4M8f3u1",
        "publicKey": "1309054680743461058038787313867871763266074183195852350126078860483253384655447103358465251710948485441705155847139019374532947750661575547013497376941900"
      }
    ],
    "service": [
      {
        "type": "WeIdentity",
        "id": "did:weid:iot:0x772b4982bebc911b66cf0793de3efe463e442af8#c2b7ef07",
        "serviceEndpoint": "https://github.com/WeBankBlockchain/WeIdentity"
      }
    ]
  },
  "didResolutionMetadata": {
    "contentType": "application/did+ld+json",
    "error": null,
    "transactionInfo": null
  },
  "didDocumentMetadata": {
    "created": null,
    "updated": null,
    "deactivated": false,
    "versionId": 1
  }
}
```

### Driver Variables

Following the
document [https://weidentity.readthedocs.io/zh_CN/release-3.1.1/](https://weidentity.readthedocs.io/zh_CN/release-3.1.1/)
to modify `resources/conf/fisco.properties` and `weidentity.properties` to connect to your specific fiscobcos blockchain
network and Weidentity service.
