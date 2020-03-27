.. role:: raw-html-m2r(raw)
   :format: html

.. _weidentity-build-without-deploy:

WeIdentity JAVA SDK 安装部署工具（不部署智能合约）
============================================================

整体介绍
--------

一条区块链里，有多家机构，只需要一家机构部署 WeIdentity 智能合约，部署完成后，将智能合约地址给到其他机构即可。

* 不部署 WeIdentity 智能合约的机构，参考本文档完成安装部署和集成。
* 部署 WeIdentity 智能合约的机构，可以参考\ `WeIdentity Java SDK 安装部署工具（部署智能合约 <./weidentity-build-with-deploy.html>`__\ 。


部署步骤
--------


1. 部署 WeIdentity 智能合约
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

1.1 下载安装部署工具
''''''''''''''''''''''''''''''
::

    git clone https://github.com/WeBankFinTech/weid-build-tools.git
 


1.2  配置基本信息
''''''''''''''''''''''''''''''''''''
最新版本的build-tool工具仅支持 FISCO BCOS 2.0 版本。


1.2.1  基本配置
>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

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

1.2.2 配置节点证书和秘钥文件
>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

::

    cd resources/

FISCO BCOS 2.0请参考\ `2.0 web3sdk客户端配置 <https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/sdk/java_sdk.html>`__\ 将证书文件 ``ca.crt``， ``node.crt`` 和 ``node.key`` 复制出来，拷贝至当前目录下。


1.3 部署智能合约并自动生成配置文件
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

2 weid-java-sdk 的集成
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

2.1 在自己的Java应用中引入 weid-java-sdk
'''''''''''''''''''''''''''''''''''''''''''''

在自己的 Java 应用通过 ``build.gradle`` 引入，目前 weid-java-sdk 暂时只支持 gradle 方式引入。

::

    compile("com.webank:weid-java-sdk:x.x.x")

.. note::
   x.x.x为您使用的weid-java-sdk版本，建议使用最新版本。
   
2.2 配置您的Java应用
'''''''''''''''''''''''''''''''''
将 weid-build-tools 里配置好的配置文件拷贝至您的Java应用中：
::

    cd resources/
    ls


您可以将 ``resources`` 目录下的所有文件拷贝至您的 Java 应用的 ``resources`` 目录下，weid-java-sdk 会自动加载相应的资源文件。

现在您可以使用 WeIdentity 开发您的区块链身份应用。weid-java-sdk 相关接口请见：\ `WeIdentity JAVA SDK文档 <https://weidentity.readthedocs.io/projects/javasdk/zh_CN/latest/docs/weidentity-java-sdk-doc.html>`__ 。

我们提供了一个基于WeID的 \ `开发样例 <https://github.com/WeBankFinTech/weid-sample>`__\， 您可以参考。


3. WeIdentity JAVA SDK 便捷工具使用
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

我们提供了一些快捷工具，可以帮您快速体验 weid-java-sdk，请参考\ `WeIdentity JAVA SDK 便捷工具使用 <./weidentity-quick-tools.html>`__\。
