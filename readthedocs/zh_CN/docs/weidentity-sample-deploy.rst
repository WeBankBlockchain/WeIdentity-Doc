部署 weid-java-sdk 与配置基本信息
-------------------------------------------

配置管理员私钥
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

.. note::
  此项配置并非必要。若您不是发布智能合约的管理员，您无需关注此配置项。若您是智能合约发布的管理员，您可以参考以下进行配置：


将您在\ `部署WeIdentity智能合约阶段 <./weidentity-build-with-deploy.html#id7>`__\ 所使用的私钥文件private_key拷贝至
``weid-sample/keys/priv/`` 目录中，此私钥后续将用于注册 Authority Issuer，weid-sample 会自动加载。如果使用weid-build-tools
可视化部署WeIdentity智能合约，该私钥文件位于weid-build-tools/output/admin目录


其他配置文件
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
1. 从已经使用weid-build-tools的机器上，在该工程的resources目录下，将weidentity.properties 和 fisco.properties
复制到weid-sample的resources目录下
2. 从已经使用weid-build-tools的机器上，在该工程的resources/conf目录下，将所有FISCO BCOS节点证书文件复制到weid-sample的resources/conf目录下（为了方便，可以直接将整个conf目录拷贝过来），
或者从区块链节点获取对应的证书文件。



