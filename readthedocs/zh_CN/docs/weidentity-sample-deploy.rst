部署 weid-java-sdk 与配置基本信息
---------------

配置 Committee Member 私钥
~~~~~~~~

.. note::
  此项配置并非必要。由于注册 Authority Issuer 需要委员会机构成员（ Committee Member ）权限，若您不是发布智能合约的机构，您无需关注此配置项。
  若您是智能合约发布的机构，您可以参考以下进行配置：


将您在\ `部署WeIdentity智能合约阶段 <./weidentity-build-with-deploy.html#id7>`__\ 生成的私钥文件拷贝至
``weid-sample/keys/priv/`` 目录中，此私钥后续将用于注册 Authority Issuer，weid-sample 会自动加载。


配置weidentity.properties
~~~~~~~~

.. code:: shell

   cd weid-sample
   vim resources/weidentity.properties

关键配置如下：

 | ``blockchain.orgid`` ：机构名称。样例以 organizationA 为例，请修改为 organizationA。
 | ``nodes`` ：区块链节点信息。你可以修改为您区块链网络中的任一节点即可。

配置样例：

.. code:: properties

  blockchain.orgid=organizationA
  nodes=10.10.10.10:20200

配置节点证书和密钥文件
~~~~~~~~~
::

    cd resources/

FISCO BCOS 2.0请参考\ `2.0 web3sdk客户端配置 <https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/sdk/java_sdk.html#sdk>`__\，将证书文件 ``ca.crt``， ``node.crt`` 和 ``node.key`` 复制出来，拷贝至当前目录下。


