.. role:: raw-html-m2r(raw)
   :format: html

.. _weidentity-installation-by-sourcecode:

WeIdentity JAVA SDK安装部署文档（源码方式）
=================================================

1.下载源码
""""""""""

.. note::
     注意：如果您使用了openjdk13，那么您需要手动修改build.gradle，禁用spotbugs相关功能。具体地，请打开build.gradle，然后将所有spotbugs相关项注释掉即可。


* `WeIdentity JAVA SDK <https://github.com/WeBankFinTech/WeIdentity.git>`_\ :raw-html-m2r:`<br>`
  建议下载最新版本的release
  
.. code-block:: shell

  git clone https://github.com/WeBankFinTech/WeIdentity.git



- 配置节点证书和秘钥文件

::

    cd WeIdentity/src/main/resources/


若您使用FISCO BCOS 2.0, 请参考\ `2.0 web3sdk客户端配置 <https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/sdk/java_sdk.html>`__，
将证书文件 ``ca.crt``， ``node.crt`` 和 ``node.key`` 复制出来，拷贝至当前目录下。

若您使用FISCO BCOS 1.3, 请参考\ `1.3 web3sdk客户端配置 <https://fisco-bcos-documentation.readthedocs.io/zh_CN/release-1.3/docs/tools/web3sdk.html>`__，
将证书文件 ``ca.crt`` 和 ``client.keystore`` 复制出来，拷贝至当前目录下 。


- 配置基本信息

::

    cd ../../../build-tools/bin/
    vim run.config


主要的配置文件 ``run.config`` ，配置一些运行时需要的一些参数.

-  配置说明：

 | ``blockchain_address`` ： 区块链节点 IP 和channel端口， channel端口的配置可以参考\ `FISCO BCOS 2.0 配置项说明 <https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/manual/configuration.html#rpc>`__ 进行配置，FISCO BCOS 1.3可以参考\ `FISCO BCOS 1.3 配置项说明 <https://fisco-bcos-documentation.readthedocs.io/zh_CN/release-1.3/docs/web3sdk/config_web3sdk.html#java>`__ 进行配置。
 | ``blockchain_fiscobcos_version`` ：对接的FISCO BCOS版本。
 | ``org_id`` ：机构名称，该名称也被用作后续机构间的通信标识。
 | ``chain_id`` ：用于标识您接入的区块链网络。

配置样例：
::

    #节点的连接串，节点IP为10.10.10.10，和channel端口为20200。
    blockchain_address=10.10.10.10:20200

    # 2表示FISCO BCOS的版本为2.0, 1则表示FISCO BCOS 1.3
    blockchain_fiscobcos_version=2

    #机构名称
    org_id=organizationA

    #链标识
    chain_id=1 
 
.. note::
     注意：如果您使用了Gradle 6.0+，那么您需要手动修改build.gradle中spotbug的Gradle插件版本号为2.0.0+。具体地，打开WeIdentity/build.gradle，将“classpath "gradle.plugin.com.github.spotbugs:spotbugs-gradle-plugin:1.6.5"”中的1.6.5改成2.0.0或更高版本。

.. note::
     如果您使用FISCO-BCOS 2.x，且需要跨群组部署WeIdentity，请参考\ `跨群组部署WeIdentity <./how-to-deploy-w-groupid.html>`__\ 。

2.安装部署
""""""""""

运行下面的命令，自动完成代码编译，智能合约编译，智能合约部署和所有配置文件的配置：

::

    chmod +x *.sh
    ./run.sh

出现下列输出，则表示安装部署成功。

.. code-block:: shell

	contract deployment done.
	begin to modify sdk config...
	modify sdk config finished...
	begin to clean config...
	clean finished...

到这里，您已经完成了weid-java-sdk的安装和部署的全部步骤，您可以开始使用WeIdentity来构建您的分布式身份管理的Java应用了。

Have fun!!!

备注
----

查看WeIdentity JAVA SDK部署结果
""""""""""""""""""""

* 进入dist目录

.. code-block:: shell

   cd ../../dist/
   ls

dist目录包含以下目录： ``app``， ``conf`` 和 ``lib``

.. list-table::
   :header-rows: 1

   * - 目录名
     - 说明
   * - app
     - 打包好的weid-java-sdk jar包。
   * - conf
     - weid-java-sdk运行时的一些配置，Java应用集成weid-java-sdk的时候，需要将此目录下的文件放到您自己的Java应用的classpath下。
   * - lib
     - 依赖的jar包。

* 进入源码根目录

.. code-block:: shell

   cd ../
   ls

根目录下生成的文件 ``ecdsa_key`` 为weid-java-sdk部署合约动态生成的秘钥文件，您的Java应用集成weid-java-sdk的时候可能需要使用此文件，请妥善保管。