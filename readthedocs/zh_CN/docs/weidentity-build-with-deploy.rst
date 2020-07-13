.. role:: raw-html-m2r(raw)
   :format: html

.. _weidentity-build-with-deploy:

使用 WeIdentity 部署工具完成部署（联盟链管理员）
=============================================================

整体介绍
--------

一条区块链里，有多家机构，只需要一家机构部署 WeIdentity 智能合约，部署完成后，将智能合约地址给到其他机构即可。

* 部署 WeIdentity 智能合约的机构，参考本文档完成安装部署和集成。
* 不部署 WeIdentity 智能合约的机构，可以参考\ `WeIdentity Java SDK 安装部署工具（不部署智能合约 <./weidentity-build-without-deploy.html>`__\ 。

1.  配置基本信息
'''''''''''''''''''''''''''''

::

    cd weid-build-tools   
    vim run.config   

- 配置区块链节点信息，填入区块链节点 IP 和 Channel端口，示例如下：

.. note::
     区块链节点的Channel端口说明见\ `FISCO BCOS 2.0配置文件说明 <https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/manual/configuration.html#rpc>`__\ 。

.. code:: shell

    blockchain_address=10.10.10.10:20200

- 如果需要配置多个区块链节点，用逗号分隔，示例如下：

.. code:: shell

    blockchain_address=10.10.10.10:20200,10.10.10.11:20200

- 配置机构名称，该名称也被用作后续机构间的 \ `AMOP <https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/manual/amop_protocol.html>`__ 通信标识。假设您的机构名为 test，您可以配置为：

.. code:: shell

    org_id=test

- 配置 chain-id，该配置项用于路由到不同的网络，假设您的 chain-id 定义为1，则您可以配置为：

.. code:: shell

    chain_id=1

- 配置数据库相关，该配置用于SDK存储相关数据使用：

.. code:: shell

    mysql_address=0.0.0.0:3306
    mysql_database=database
    mysql_username=username
    mysql_password=password

保存退出，即完成基本配置。

.. note::
     如果您使用FISCO-BCOS 2.x，且需要跨群组部署WeIdentity，请参考\ `跨群组部署WeIdentity <./how-to-deploy-w-groupid.html>`__\ 。


2. 配置节点证书和秘钥文件
>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

::

    cd resources/

FISCO BCOS 2.0请参考\ `2.0 web3sdk客户端配置 <https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/sdk/java_sdk.html#sdk>`__\，将证书文件 ``ca.crt``， ``node.crt`` 和 ``node.key`` 复制出来，拷贝至当前目录下。

3. 部署智能合约并自动生成配置文件
'''''''''''''''''''''''''''''''''''

.. raw:: html

   </div>


- 如果您是第一次使用本工具，您需要先进行编译：

.. note::
  | 如果您重新修改了 ``run.config`` 里的配置项，您也需要重新编译。

::

    cd ..
    chmod +x compile.sh   
    ./compile.sh

如果执行过程没报错，大约半分钟左右可以编译完成。


- 执行脚本 deploy.sh 进行 WeIdentity 智能合约的发布。

::

    chmod +x deploy.sh   
    ./deploy.sh


运行成功后，会打印以下信息：

::

    contract is deployed with success.
    ===========================================.
    weid contract address is 0x4ba81103afbd5fc203db14322c3a48cd1abb7770
    cpt contract address is 0xb1f3f13f772f3fc04b27ad8c377def5bc0c94200
    authority issuer contract address is 0xabb97b3042d0f50b87eef3c49ffc8447560faf76
    evidence contract address is 0x8cc0de880394cbde18ca17f6ce2cf7af5c51891e
    specificIssuer contract address is 0xca5fe4a67da7e25a24d76d24efbf955c475ab9ca
    ===========================================.


.. note::
  | 发布 WeIdentity 智能合约的机构将会自动注册为委员会机构成员（ Committee Member ）。
  | 发布 WeIdentity 智能合约会同时会在 ``weid-build-tools/output/admin`` 目录下动态生成私钥文件 ``ecdsa_key``，以及对应的公钥文件 ``ecdsa_key.pub``，此私钥后续用于注册权威机构，您可以将其保存到您的其他存储库里。
  | 在根目录下会生成一个hash文件，此文件用于给其他不部署合约的机构使用。

至此，您已经完成 weid-java-sdk 的安装部署，您可以开始您的 Java 应用集成以及便捷工具体验。

.. note::
     一条区块链里，有一家机构负责部署 WeIdentity 智能合约，部署成功后，会将上述智能合约地址给到其他机构。



