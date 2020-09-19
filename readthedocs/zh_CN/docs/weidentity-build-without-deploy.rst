.. role:: raw-html-m2r(raw)
   :format: html

.. _weidentity-build-without-deploy:

使用 WeIdentity 部署工具完成部署（非联盟链管理员）
============================================================

整体介绍
--------

一条区块链里，有多家机构，只需要一家机构部署 WeIdentity 智能合约，部署完成后，将智能合约地址给到其他机构即可。

* 不部署 WeIdentity 智能合约的机构，参考本文档完成安装部署和集成。
* 部署 WeIdentity 智能合约的机构，可以参考\ `WeIdentity Java SDK 安装部署工具（部署智能合约 <./weidentity-build-with-deploy.html>`__\ 。



1.  配置基本信息
'''''''''''''''''''''''''''''

::

    cd weid-build-tools
    vim run.config   

- 配置区块链节点信息，填入区块链节点 IP 和 Channel端口，示例如下：

.. note::
   区块链节点Channel端口说明见\ `FISCO BCOS 2.0配置文件说明 <https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/manual/configuration.html#rpc>`__\。

.. code:: shell

    blockchain_address=10.10.10.10:20200

- 如果需要配置多个区块链节点，用逗号分隔，示例如下：

.. code:: shell

    blockchain_address=10.10.10.10:20200,10.10.10.11:20200


- 配置机构名称，该名称也被用作后续机构间的 \ `AMOP <https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/manual/amop_protocol.html>`__ 通信标识。

假设您的机构名为 test，您可以配置为：

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

- 配置CNS地址，配置你需要使用哪家机构的CNS地址：

.. code:: shell

    cns_contract_follow=0x....

.. note::
     一条联盟链里，当一家机构部署 WeIdentity 智能合约成功后，需要将CNS地址发给其他机构，或者使用可视化工具选择CNS进行启用。

保存退出，即完成基本配置。

2. 配置节点证书和秘钥文件
>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

::

    cd resources/

FISCO BCOS 2.0请参考\ `2.0 web3sdk客户端配置 <https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/sdk/java_sdk.html#sdk>`__\ 将证书文件 ``ca.crt``， ``node.crt`` 和 ``node.key`` 复制出来，拷贝至当前目录下。


3. 部署智能合约并自动生成配置文件
''''''''''''''''''''''''''''''''''''

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

至此，您已经完成 weid-java-sdk 的安装部署，您可以开始您的 Java 应用集成以及便捷工具体验。


