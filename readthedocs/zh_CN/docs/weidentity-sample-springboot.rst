spring-boot服务方式使用
-----------------------

整体介绍
~~~~~~~~

::

    使用 spring-boot 方式，weid-sample 程序将作为一个后台进程运行，您可以使用swagger可视化地体验交互流程。

1. 下载源码
~~~~~~~~~~~~~~~~~~~~~~~~~~

.. code:: shell

    git clone https://github.com/WeBankFinTech/weid-sample.git

2. 配置与部署
~~~~~~~~~~~~~~~~~~~~~~~~~~

2.1 部署 weid-java-sdk 与配置基本信息
''''''''''''''''''''''''''''''''''''''

若您在体验WeIdentity Sample springboot之前已经完成WeIdentity Build Tool的部署和配置，无需您再次进行配置。

若您想单独体验WeIdentity Sample, 您可以参考\ `部署weid-java-sdk与配置基本信息 <./weidentity-sample-deploy.html>`__\进行配置。



2.2 基本流程的演示
''''''''''''''''''''''''
2.2.1 编译和运行
>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

- 编译 weid-sample

.. code:: shell

    cd weid-sample
    chmod +x *.sh
    ./build.sh

- 启动 weid-sample 服务:


.. code:: shell

    ./start.sh

若启动成功，则会打印以下信息：

::

    [main] INFO  AnnotationMBeanExporter() - Registering beans for JMX exposure on startup
    [main] INFO  Http11NioProtocol() - Initializing ProtocolHandler ["https-jsse-nio-6101"]
    [main] INFO  Http11NioProtocol() - Starting ProtocolHandler ["https-jsse-nio-6100"]
    [main] INFO  NioSelectorPool() - Using a shared selector for servlet write/read
    [main] INFO  Http11NioProtocol() - Initializing ProtocolHandler ["http-nio-6101"]
    [main] INFO  NioSelectorPool() - Using a shared selector for servlet write/read
    [main] INFO  Http11NioProtocol() - Starting ProtocolHandler ["http-nio-6101"]
    [main] INFO  TomcatEmbeddedServletContainer() - Tomcat started on port(s): 6100 (https) 6101 (http)
    [main] INFO  SampleApp() - Started SampleApp in 3.588 seconds (JVM running for 4.294)

2.2.2 流程演示
>>>>>>>>>>>>>>>>>>>>>>>>

以下将为您演示
假设您的服务部署在本地，地址是 ``127.0.0.1``，服务端口是 ``6101``。您可以在 ``resources/`` 里修改端口信息。
您可以使用浏览器打开http://127.0.0.1:6101/swagger-ui.html，通过可视化的方式体验WeIdentity的核心功能。

- 创建 WeID

单击``/step1/issuer/createWeId``，创建WeID，并返回结果。

若调用成功，则会显示以下信息：

.. image:: images/weid-sample-springboot-1.png

表明创建的 WeID 是 did:weid:1:0xbb96163789a4e16790f3d213319bd4cf2b517582。

- 注册 Cpt

单击``/step2/registCpt``，参数里的 publisher 传入step1刚刚注册的WeID

运行成功，则会打印以下信息：

.. image:: images/weid-sample-springboot-2.png

表明注册 CPT 成功，CPT ID 为 2000000。

- 创建 Credential

单击``/step3/createCredential``，修改参数``claimData``为具体值，参数issuer为step1的WeID，参数cptId为step2返回的Cpt ID


运行成功，则会打印以下信息：

.. image:: images/weid-sample-springboot-3.png

表明创建 Credential 成功，Credential 的具体信息为图中的 credential 字段对应的内容。

- 验证 Credential

单击``/step1/verifyCredential``，修改参数为上步所得到的``credential``。

若运行成功，则会打印以下信息：

.. image:: images/weid-sample-springboot-4.png

表明 Credential 验证成功。

至此，您已经体验了 weid-sample 实现的各个角色的运行流程，实现的入口类在weid-sample工程的 ``com.webank.weid.demo.server.SampleApp``，您可以参考进行您的 Java 应用开发。
