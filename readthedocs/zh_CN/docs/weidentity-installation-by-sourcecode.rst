.. role:: raw-html-m2r(raw)
   :format: html

.. _weidentity-installation-by-sourcecode:

WeIdentity JAVA SDK安装部署文档（源码方式）
=================================================

.. note::
     注意：如果您使用了针对oracle jdk1.8.0.231及以上版本，不论用哪种方式部署SDK，在使用时都需要配置jvm参数 -Djdk.tls.namedGroups="secp256k1" 。详细原因，请见Oracle JDK 8u231的Release Notes： https://www.oracle.com/technetwork/java/javase/8u231-relnotes-5592812.html


1.下载源码
""""""""""

.. note::
     注意：如果您使用了openjdk13，那么您需要手动修改build.gradle，禁用spotbugs相关功能。具体地，请打开build.gradle，然后将所有spotbugs相关项注释掉即可。


* 下载最新的代码

.. code-block:: shell

  git clone https://github.com/WeBankBlockchain/WeIdentity.git

或者

.. code-block:: shell

  git clone https://gitee.com/WeBank/WeIdentity.git

WeIdentity Java SDK 工程见\ `WeIdentity JAVA SDK <https://github.com/WeBankBlockchain/WeIdentity.git>`_\ :raw-html-m2r:`<br>`

- WeIdentity 编译

.. code-block:: shell

  cd WeIdentity
  chmod u+x ./gradlew
  ./gradlew build -x test

.. note::
     如果出现 xx SpotBugs violations were found 这个提示，请忽略。

- 配置节点证书和秘钥文件

::

    cd src/main/resources/


若您使用FISCO BCOS 2.0, 请参考\ `2.0 web3sdk客户端配置 <https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/sdk/java_sdk.html>`__，
将证书文件 ``ca.crt``， ``node.crt`` 和 ``node.key`` 复制出来，拷贝至当前目录下。


- 配置基本信息

::

    cd ../../../build-tools/bin/
    vim run.config


主要的配置文件 ``run.config`` ，配置一些运行时需要的一些参数.

-  配置说明：

 | ``blockchain_address`` ： 区块链节点 IP 和channel端口， channel端口的配置可以参考\ `FISCO BCOS 2.0 配置项说明 <https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/manual/configuration.html#rpc>`__ 进行配置。
 | ``amop_id`` ：机构间的通信标识，\ 配置说明。`<./deploy-via-web.html#blockchain-configuration-amop-id>`_\
 | ``org_id`` ：机构名称，\ 配置说明。`<./deploy-via-web.html#blockchain-configuration-org-id>`_\
 | ``chain_id`` ：用于标识您接入的区块链网络, 默认填写：101，\ `配置说明。<./deploy-via-web.html#weid-deploy-chain-id>`_\
 | ``persistence_type`` ：数据存储类型, 默认填写：mysql。
 | ``mysql_address`` ：配置数据库的ip和port，例：0.0.0.0:3306
 | ``mysql_database`` ：配置数据库名称
 | ``mysql_username`` ：配置数据库用户名
 | ``mysql_password`` ：配置数据库用户对应的密码
 | ``cns_profile_active`` ：合约部署环境标识,可用于WeIdentity合约隔离。


配置样例：
::

    #节点的连接串，节点IP为10.10.10.10，和channel端口为20200。
    blockchain_address=10.10.10.10:20200

    #机构间的通信标识
    amop_id=organizationA

    #机构名称
    org_id=organizationA

    #链标识
    chain_id=101
    
    #数据存储类型
    persistence_type=mysql
    
    #数据库ip和port
    mysql_address=0.0.0.0:3306

    #数据库名称
    mysql_database=database

    #数据库用户名
    mysql_username=username

    #数据库密码
    mysql_password=password

    #合约部署环境标识
    cns_profile_active=prdA

.. note::
     注意：如果您使用了Gradle 6.0+，那么您需要手动修改build.gradle中spotbug的Gradle插件版本号为2.0.0+。具体地，打开WeIdentity/build.gradle，将“classpath "gradle.plugin.com.github.spotbugs:spotbugs-gradle-plugin:1.6.5"”中的1.6.5改成2.0.0或更高版本。

.. note::
     如果您使用FISCO-BCOS 2.x，且需要跨群组部署WeIdentity，请参考\ `跨群组部署WeIdentity <./how-to-deploy-w-groupid.html>`__\ 。

2.安装部署
""""""""""

运行下面的命令，自动完成代码编译，智能合约编译，智能合约部署和所有配置文件的配置：

::

    chmod +x run.sh
    ./run.sh

出现下列输出，则表示安装部署成功。

.. code-block:: shell

	contract deployment done.
	begin to modify sdk config...
	modify sdk config finished...
	begin to clean config...
	clean finished...

到这里，您已经完成了weid-java-sdk的安装和部署的全部步骤，您可以开始使用WeIdentity来构建您的分布式身份管理的Java应用了。

.. note::
    如果执行部署过程中出现 160016 - no premission for this cns. 异常，请修改(run.config)中的配置项  cns_profile_active 的值，修改成一个独有的值即可, 如：cns_profile_active=test456。

Have fun!!!

备注
----

查看WeIdentity JAVA SDK部署结果
""""""""""""""""""""""""""""""""

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
