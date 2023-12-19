
.. _weidentity-rest-deploy:

WeIdentity RestService 部署文档
----------------------------------------

1. Server 部署说明
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

1.1 环境要求
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Server 的环境要求与 WeIdentity-Java-SDK 的 `环境要求 <./one-stop-experience.html>`_ 类似，但它不需要 fisco-solc 编译环境：


.. list-table::
   :header-rows: 1
   :widths: 30 30 60

   * - 物料
     - 版本
     - 说明
   * - CentOS/Ubuntu
     - 7.2 / 16.04，64位
     - 部署 RestServer 用
   * - JDK
     - 1.8+
     - 推荐使用 1.8u141 及以上
   * - FISCO-BCOS 节点
     - 2.9以上
     - 确保它可以和部署 Server 机器互相连通，可 telnet 其 channelPort 端口
   * - Gradle
     - 4.6+
     - 同时支持 4.x 和 5.x 版本的 Gradle
   * - MySQL
     - 5 +
     - 需要MySQL存储必要的链上数据进行缓存


1.2 生成安装包
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

您可以从 \ `GitHub <https://github.com/WeBankBlockchain/WeIdentity-Rest-Service>`_\ 下载 RestService 的源代码，并进行编译以生成安装包（默认置于 ``/dist`` 目录下）：

.. code-block:: bash

   $ git clone https://github.com/WeBankBlockchain/WeIdentity-Rest-Service.git
   $ cd WeIdentity-Rest-Service
   $ gradle build -x test
   $ cd dist

默认为develop分支。您可以生成如下结构的安装包：

.. code-block:: text

   └─ dist
     ├─ app: 启动jar包
     ├─ lib: 依赖库
     ├─ conf: 配置文件
     ├─ keys/priv: 托管私钥
     ├─ server_status.sh：监控系统运行状态
     ├─ start.sh：启动RestServer
     └─ stop.sh：停止RestServer

1.3 修改配置文件
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

* 首先，确认 WeIdentity 合约已部署完毕，同时您所部署的 FISCO-BCOS 节点可以正常连通。目前支持 2.x 和3.x的 FISCO-BCOS 节点。
* 拷贝节点证书。您需要将节点的各个证书拷贝到 ``dist/conf`` 目录下。
* 修改合约地址。如果您使用部署工具部署了 WeIdentity 合约，那么只需将部署工具生成的 ``fisco.properties`` 及 ``weidentity.properties`` 拷贝到 ``dist/conf`` 目录下即可。然后更新 ``dist/conf/fisco.properties`` 下列属性中sdk.cert-path、amop.pub-path和amop.pri-path属性的值，把前缀 ``resources/`` 去掉。

* 拷贝您 WeIdentity 合约部署者的私钥 ``private_key`` 覆盖到 ``dist`` 目录下同名文件。如果您使用部署工具部署了 WeIdentity 合约，这个文件在 ``output/admin/`` 目录。

**如果使用无区块链（仅使用数据库）的WeIdentity，请跳过上面步骤，只需将根目录下的 ``fisco.properties`` 及 ``weidentity.properties`` 拷贝到 ``dist/conf`` 目录下即可，然后更新 ``weidentity.properties`` 中的datasource1.jdbc.url、datasource1.jdbc.username、datasource1.jdbc.password为你所要连接的数据库地址、用户和密码。此外，crypto.type配置项可以选择国密或者非国密。**

* 修改 ``dist/conf/application.properties`` ，填入需要打开的监听端口地址（用于 RestServer 监听外来的 HTTP/HTTPS RESTful 请求，默认为 6001，不可被其他程序占用）。

.. code-block:: bash

    # 默认的HTTP/HTTPS请求端口
    server.port=6001
    # 当开启HTTPS时的HTTP请求端口，默认未实现重定向，可忽略
    server.http.port=6000

* 如果您需要启用HTTPS，``dist/conf/application.properties`` 中，请将SSL开启，同时配置服务端证书并将证书的详细信息填入以下配置项中。如果您暂时没有合适的证书，当前，Rest Service已经默认提供了一份自签（Self-Signed）证书，其keystore默认密码也已给出。

.. code-block:: bash

    # 是否启用HTTPS：默认为否，需要改成true
    server.ssl.enabled=true
    # 证书的keystore
    server.ssl.key-store=classpath:tomcat.keystore
    # keystore的访问密码，默认的自签证书密码为123456
    server.ssl.key-store-password=
    # keystore种类（如JKS，PKCS12）
    server.ssl.keyStoreType=JKS
    # key的假名
    server.ssl.keyAlias=tomcat

.. note::
    当前，Rest Service 不论是 HTTP/HTTPS 方式，其访问 IP 均为 6001。出于安全考量，我们暂时未实现在启用 HTTPS 方式时的 HTTP 访问重定向功能。

.. note::
    如果您使用了自签证书，且准备通过使用 Postman 作为客户端访问 HTTPS，您需要在 Postman 的设置 File -> Setting -> General 中，手动将 SSL certificate verification 关闭；如果您使用 CA 签名证书，则需要在 Postman 的设置菜单 File -> Setting -> Certificates 中，安装此证书（及其证书链）。

.. note::
    关于如何生成您自己的自签名证书，可以参考以下文档：https://hutter.io/2016/02/09/java-create-self-signed-ssl-certificates-for-tomcat/ 。本教程不涉生成 CA 证书的步骤。

* 同时，请在 ``dist/conf/application.properties`` 中确认用来调用默认合约部署者私钥的暗语；由于此暗语可直接调用 WeIdentity 合约部署者的私钥，权限较高（详见 \ `RestService API 说明文档 <./weidentity-rest-api.html>`_\ ），因此请您务必对其进行修改。

.. code-block:: bash

    # 合约部署者私钥暗语。改成ecdsa_key，您就可以使用此来调用合约部署者的私钥发交易了。
    default.passphrase=ecdsa_key

* 最后，如果您需要连接使用MySQL，则需要在``dist/conf/weidentity.properties``内修改关于datasource相关的MySQL配置。

1. Server 使用说明
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

2.1 Server 启动/停止
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

进入 dist 目录，执行以下命令以启动或停止 Rest Server：

.. code-block:: bash

    # 为脚本文件增加权限
    $ chmod +x start.sh server_status.sh stop.sh
    # 启动应用
    $ ./start.sh
    # 观察应用状态
    $ ./server_status.sh
    # 停止应用
    $ ./stop.sh

执行 ``./start.sh`` 之后会输出以下提示，表示 RestServer 已经顺利启动：

.. code-block:: text

    ========================================================
    Starting com.webank.weid.http.Application ... [SUCCESS]
    ========================================================

请您通过执行 ``./server_status.sh`` 确认 RestServer 已经成功启动：

.. code-block:: text

    ========================================================
    com.webank.weid.http.Application is running(PID=100891)
    ========================================================

如果需要停止服务，请执行 ``./stop.sh`` ，之后会输出以下提示，表示 RestServer 已经顺利停止：

.. code-block:: text

    ========================================================
    Stopping com.webank.weid.http.Application ... [SUCCESS]
    ========================================================

3. 使用 Postman 访问 RestServer 的 API
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

RestServer 支持任何使用标准 HTTP/HTTPS 协议的 RESTful API 客户端访问，详细接口说明可见 API 文档。我们提供了一套 Postman 的环境与请求集供快速集成。使用步骤如下：

* 点击Postman的Import按钮，导入环境文件 ``weidentity-restservice.postman_environment.json`` 和请求集 ``invoke.postman_collection.json`` 。这两个文件可以在 GitHub代码仓库的 \ `对应目录 <https://github.com/WeBankBlockchain/weid-http-service/tree/develop/PostmanConfig>`_\ 下找到
* 确认 ``weidentity-restservice`` 这个环境文件已导入成功，它包含两个环境变量 ``host`` 和 ``httpport``
    * 修改环境变量 ``host`` 属性的值为安装部署 ``RestServer`` 的服务器地址
    * 修改环境变量 ``httpport`` 属性的值配置文件中的 Server 监听端口地址
* 接下来确认 Invoke 这个命令集已导入成功。如果成功，可以从侧边栏中看到
* 现在，可以调用 Invoke 这个命令集中的各类API了。您可以从无参数请求 CreateWeId 开始，看看返回结果是不是和 API 文档中一致，成功创建了一个 WeIdentity DID。
