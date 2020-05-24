# 跨群组分离部署WeIdentity的DID相关合约与存证合约

在实际应用场景中，经常会出现以下一类问题：不同的机构间构成了联盟链，将WeID、Authority Issuer、CPT等信息注册在联盟链上。
然而，对于存证合约，由于可以通过它的log项来记录额外信息，且它间接体现了具体上链的存证业务量的多少，因此，不同机构间可能不一定有意愿在同一条链上使用存证。
为了满足这一需求，对主合约和存证合约进行跨群组的分离部署是至关重要的。

从WeIdentity 1.6.3版本开始，我们内置支持了将存证合约和WeIdentity主合约集（WeID合约、Authority/Specific Issuer合约、CPT合约）分开部署在不同的群组上。
只需要指定存证合约的群组ID号，就可以达到将主合约和存证合约分离的目的。

## 前提条件

跨群组分离部署需要以下条件：
- 使用FISCO BCOS 2.0.1+及以上版本。推荐使用2.3.0+。
- 所连接的节点必须处于需要接入的群组中。如果您使用部署工具，则节点在`./run.config`里定义；如果使用源码部署，则在`./build-tools/bin/run.config`里定义。
- 如果您为了多活的目的，在`run.config`里填写了为WeIdentity连接的多个节点，请无论如何确保：每一个节点都能够连接到需要接入的群组中。
    - 您可以对您需要连接的多个节点生成SDK证书保证连通性，详细请参见：[FISCO-BCOS企业部署工具生成SDK证书说明](https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/enterprise_tools/operation.html#generate-sdk-certificate) 。


## 使用可视化工具进行分离部署

在weid-build-tools 1.0.11+的可视化部署方式页面上，您可以看到一个新的选项：“存证部署”。点击“根据群组部署存证合约”，即可填入群组ID，单独部署分离的存证合约。

## 使用命令行部署进行分离部署

## 使用源码方式进行分离部署

如果您需要使用源码方式单独分离部署存证合约，则需要在（[源码方式安装部署文档](./weidentity-installation-by-sourcecode.html)）执行完成后，再单独执行：

```
    run.sh --deploy_evidence_on_group_id <groupId>
```

执行这条命令，即可以直接达成部署存证合约的目的，它和可视化工具是等效的。

## 如何在集成SDK时使用分离部署的群组

在构造Evidence服务时，您需要使用有参构造方法构造，并指定群组ID作为传入参数。默认范例：

```
    private EvidenceService evidenceService = new EvidenceServiceImpl(ProcessingMode.IMMEDIATE, evidenceGroupId);
```

注意：如果您仍然使用无参方法构造，则后果是可能会将存证合约写入主群组，就无法达到分离部署的目的。

## 相关组件支持

### 部署工具weid-build-tools

weid-build-tools需要使用1.0.11及以上版本，便可通过可视化工具和命令行两种方式进行分离部署，具体需求详见上述流程。

### HTTP/S接入工具weid-http-service

weid-http-service需要使用1.5.2及以上版本。

如果您想要在weid-http-service里使用跨群组分离功能，需要执行以下步骤：
- 在`dist/conf/application.properties`里，在``evidence.group.id``这一项中填入存证合约的群组ID号。
- 在`dist/conf/weidentity.properties`里，填入已经写入存证合约部署信息的数据库。
    - 如果您使用部署工具（不论命令行/可视化工具均可），只需将生成完成的`src/main/weidentity.properites`拷贝到weid-http-service的`dist/conf`目录下。
    - 如果您使用源码方式，只需将生成完成的`dist/conf/weidentity.properites`拷贝到weid-http-service的`dist/conf`目录下。

## 工作原理

跨群组分离部署实际在执行时，执行了以下操作：
- 通过web3sdk，检查节点和不同群组间的通信是否正常。
- 将所有合约部署在主群组里（默认在`fisco.properties.tpl`中定义），部署完成后，将生成的CNS hash，写入`fisco.properties`的``cns.contract.follow``里。
- 将Evidence存证合约部署在指定的群组ID里，部署完成后，将生成的CNS hash（合约命名服务标记），写入数据库中。
- 在调用有参构造方法构造Evidence服务时，如果传入的群组ID和`fisco.properties`中所定义的不一致，会自动访问数据库，获取CNS hash，并访问正确的存证合约在分离群组上的地址。

默认`fisco.properties.tpl`中定义的主群组ID为1。您可以修改此ID，只需注意，在使用分离部署功能时，存证合约群组和主群组ID需要不一致，即可。
