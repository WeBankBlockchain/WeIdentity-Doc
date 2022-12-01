# WeIdentity 版本说明

标签：``WeId版本说明`` ``兼容性`` ``Release Note`` ``组件版本``


.. important::
    FISCO-BCOS 2.0与3.0对比、JDK版本、WeBASE及其他子系统的兼容版本说明！`请查看 <https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/compatibility.html>`_

当前WeIdentity 1.3.0及以上版本已支持FISCO BCOS 2.x 和 FISCO BCOS 3.x

## 项目版本说明


版本号对应代码可切换到`release/v1.x.x`的分支，如WeIdentity最新代码位于`develop`默认分支，如需查看v1.8.6-rc1的代码，则切换到`release/1.8.6`分支。

| 项目   |  功能简介      | 最新版本          |   文档    | github  | gitee | 备注 |
| :----    | :----          | :----           | :----    |  :----    |  :---- |:---- |
| WeIdentity (weid-java-sdk) |  分布式身份解决方案 |     v1.8.6-rc1    | [文档](https://weidentity.readthedocs.io/zh_CN/latest/)、[接口文档](https://weidentity.readthedocs.io/projects/javasdk/zh_CN/latest/docs/weidentity-java-sdk-doc.html)、 [设计规范](https://weidentity.readthedocs.io/zh_CN/latest/docs/weidentity-spec.html)  | [github](https://github.com/WeBankBlockchain/WeIdentity)  | [gitee](https://gitee.com/WeBank/WeIdentity)  | 符合W3C DID最新规范  |
| WeIdentity-Build-Tools |  WeIdentity部署工具 |     v1.3.1-rc1    | [文档](https://weidentity.readthedocs.io/zh_CN/latest/docs/weidentity-installation-by-web.html)  | [github](https://github.com/WeBankBlockchain/WeIdentity-Build-Tools)  | [gitee](https://gitee.com/WeBank/WeIdentity-Build-Tools)  | 通过安装部署工具可以快速的在您的应用项目中集成weid-java-sdk  |
| WeIdentity-Contract (weid-contract-java) |  WeIdentity 合约代码 |     v1.3.1    | [文档](https://weidentity.readthedocs.io/zh_CN/latest/docs/weidentity-contract-design.html)  | [github](https://github.com/WeBankBlockchain/WeIdentity-Contract)  | [gitee](https://gitee.com/WeBank/WeIdentity-Contract)  | 符合W3C DID最新规范，使用新的合约存储。旧版本参考v1.2.30  |
| WeIdentity-Sample |  WeIdentity sample代码 |     v1.0.11    | [文档](https://weidentity.readthedocs.io/zh_CN/latest/docs/weidentity-sample-tryit.html)  | [github](https://github.com/WeBankBlockchain/WeIdentity-Sample)  | [gitee](https://gitee.com/WeBank/WeIdentity-Sample)  |   |
| WeIdentity-Rest-Service |  WeIdentity restful服务 |     v1.5.8    | [文档](https://weidentity.readthedocs.io/zh_CN/latest/docs/weidentity-rest-design.html)  | [github](https://github.com/WeBankBlockchain/WeIdentity-Rest-Service)  | [gitee](https://gitee.com/WeBank/WeIdentity-Rest-Service)  |   |


根据WeIdentity版本，在下文的**版本说明表格**中找到**对应的组件版本**后进行部署使用。如，WeIdentity v1.8.4版本，对应WeIdentity-Build-Tools的版本为v1.0.28。


版本说明
--------

| WeIdentity |  WeIdentity-Build-Tools  |  WeIdentity-Sample  | WeIdentity-Rest-Service | WeIdentit-Contract | 备注 |
| :----    | :----     | :---- | :----|  :----|  :----|
|  v1.8.6-rc1 |  v1.3.1-rc1 |     feature/1.0.12     | v1.6.0    | v1.3.1  | 跟进W3C DID最新规范，更新合约存储方式，[WeIdentity v1.8.6文档](https://weidentity.readthedocs.io/zh_CN/release-1.8.6/) |
|  v1.8.5-rc1  |  v1.3.0-rc1   |    feature/1.0.12    |    | v1.2.30  | 替换web3sdk、支持国密和FISCO BCOS 3.0，[WeIdentity v1.8.5文档](https://weidentity.readthedocs.io/zh_CN/release-1.8.5/) |
|  v1.8.4  |  v1.0.28  |  v1.0.11    | v1.5.8     | v1.2.30 | **stable版**，[WeIdentity v1.8.4文档](https://weidentity.readthedocs.io/zh_CN/release-1.8.4/) |
|  v1.8.3  |  v1.0.28  |      |      | v1.2.30 | 升级log4j |
|  v1.8.2  |  v1.0.27  |      |      | v1.2.30 | - |
|  v1.8.1  |  v1.0.26  |      |      | v1.2.30 | - |
|  v1.8.0  |  v1.0.25  |      |      | v1.2.29 | - |
|  v1.8.0  |  v1.0.24  |      |      | v1.2.29 | 1.0.23的重构(前端重构)版本 |
|  v1.8.0  |  v1.0.23  |      |      | v1.2.29 | - |
|  v1.7.1  |  v1.0.22  |      |      | v1.2.28 | - |
|  v1.7.0  |  v1.0.21  |      |      | v1.2.27 | - |
|  v1.6.7  |  v1.0.20  |      |      | v1.2.26 | - |
|  v1.6.6  |  v1.0.19  |      |      | v1.2.24 | - |
|  v1.6.5  |  v1.0.18  |      |      | v1.2.23 | - |
|  v1.6.4  |  v1.0.12  |      |      | v1.2.21 | - |



- v1.x.x版本：代码位于对应的tag或release/1.x.x分支中，或直接使用maven仓库中对应版本号的jar包。如`WeIdentity`的`v1.8.6-rc1`版本则位于 WeIdentity 代码仓库的`release/1.8.6`发布分支中，也位于`v1.8.6-rc1`的tag中（在Releases页面查看），同时也可以拉取maven中的`weid-java-sdk`的`v1.8.6-rc1`版本的jar包；
- feature版本：代码位于该项目代码仓库的`feature/x.x.x`的开发分支，正在开发中。如`WeIdentity-Sample`的`feature/1.0.12`版本则位于 WeIdentity-Sample 的 feature/1.0.12 分支；
- 默认的develop分支则是开发中最新代码，同理，feature分支则是某个功能特性的最新代码。**如需要使用稳定版，则建议拉取release分支或拉取tag**，release分支是该版本的最新代码，tag则是该8版本的里程碑tag标签。如1.8.6的`v1.8.6-rc1`是`release/1.8.6`分支的一个里程碑，后续可能基于该分支继续开发`v1.8.6-rc2`版本，以修复一些已知的bug。

例如：
其中WeIdentity最新的v1.8.6的版本包含：
1. 稳定版：weid-java-sdk-v1.8.4（github的release/1.8.4分支或tag v1.8.4），对应weid-build-tools-1.0.28（github的release/1.0.28分支）
2. 稳定rc版：weid-java-sdk-1.8.6-rc1（github的release/1.8.6分支），对应weid-build-tools-1.3.1-rc1（github的release/1.3.1分支）
3. 开发中：WeIdentity的weid-java-sdk-1.8.6-rc.3-SNAPSHOT（github的feature/store-internal分支），对应weid-build-tools-1.3.1-rc2-SNAPSHOT（github的develop分支）





