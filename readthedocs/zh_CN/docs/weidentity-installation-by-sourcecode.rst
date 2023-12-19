.. role:: raw-html-m2r(raw)
   :format: html

.. _weidentity-installation-by-sourcecode:

WeIdentity JAVA SDK安装部署文档（源码方式）
=================================================


.. list-table::
   :header-rows: 1

   * - 配置
     - 说明
   * - 操作系统
     - CentOS （7.2.* 64位）或Ubuntu（16.04 64位）。
   * - FISCO-BCOS区块链环境
     - 您需要有一套可以运行的FISCO-BCOS区块链环境，如果没有，可以参考\ `「FISCO-BCOS 2.0节点安装方法」 <https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/installation.html>`_\ 来搭建一套区块链环境。
   * - JDK
     - 要求\ `JDK1.8+ <https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html>`_\ ，推荐使用jdk8u141。JDK 跟 WeID 直接的兼容性，可见\ `兼容性文档 <https://weidentity.readthedocs.io/zh_CN/latest/docs/weid-compatibility-test.html>`_\
   * - 网络连通
     - 检查部署 WeIdentity JAVA SDK 的服务器是否能 telnet 通 FISCO BCOS 节点的 channel 端口（\ `channel端口是什么，详见 <https://mp.weixin.qq.com/s/XZ0pXEELaj8kXHo32UFprg>`_\），若telnet不通，需要检查网络连通性和安全策略。



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

    mkdir -p resources/conf/amop && cp src/main/resources/consumer_p* resources/conf/amop/


若您使用FISCO BCOS 2.x或者3.x的非国密链： 将证书文件 ``ca.crt``， ``sdk.crt`` 和 ``sdk.key`` 复制出来，拷贝至resources/conf目录下。
若您使用FISCO BCOS 2.x的国密链，在resources/conf目录下再创建gm目录，将证书文件 ``gmca.crt``， ``gmsdk.crt``， ``gmsdk.key``， ``gmensdk.crt``和 ``gmensdk.key``拷贝至resources/conf/gm目录下。
若您使用FISCO BCOS 3.x的国密链，在resources/conf目录下再创建gm目录，将证书文件 ``sm_ca.crt``， ``sm_sdk.crt``， ``sm_sdk.key``， ``sm_ensdk.crt``和 ``sm_ensdk.key``拷贝至resources/conf/gm目录下。

- 配置基本信息

::

    cd ../../../build-tools/bin/
    vim run.config


主要的配置文件 ``run.config`` ，配置一些运行时需要的一些参数.

-  配置说明：

 | ``blockchain_address`` ： 区块链节点 IP 和channel端口， channel端口的配置可以参考\ `FISCO BCOS 2.0 配置项说明 <https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/manual/configuration.html#rpc>`__ 进行配置。
 | ``amop_id`` ：机构间的通信标识，\ `见配置说明 <./deploy-via-web.html#blockchain-configuration-amop-id>`__
 | ``org_id`` ：机构名称， \ `见配置说明 <./deploy-via-web.html#blockchain-configuration-org-id>`__
 | ``chain_id`` ：用于标识您接入的区块链网络, 默认填写：101， \ `见配置说明 <./deploy-via-web.html#weid-deploy-chain-id>`__
 | ``group_id`` ：群组标识，用于链接FISCO BCOS中特定的群组，注意FISCO BCOS的2.x和3.x版本的群组标识类型不同
 | ``blockchain_fiscobcos_version`` ：使用哪一个版本的FISCO BCOS，2表示2.x，3表示3.x
 | ``sdk_sm_crypto`` ：密码学套件类别，0表示非国密，1表示国密
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

    #FISCO BCOS版本。
    blockchain_fiscobcos_version=2

    #机构间的通信标识
    amop_id=organizationA

    #机构名称
    org_id=organizationA

    #链标识
    chain_id=101

    #群组标识 3.x的链则支持string 默认为group0
    group_id=1

    #密码学套件标识
    sdk_sm_crypto=0
    
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

根目录下生成的文件 ``private_key`` 为weid-java-sdk部署合约动态生成的秘钥文件，您的Java应用集成weid-java-sdk的时候可能需要使用此文件，请妥善保管。
