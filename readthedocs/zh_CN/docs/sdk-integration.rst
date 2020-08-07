.. role:: raw-html-m2r(raw)
   :format: html

.. _sdk-integration:

在 Java 应用中集成 WeIdentity Java SDK
=============================================================

整体介绍
--------


集成步骤
--------

1. 在自己的 Java 应用中引入 weid-java-sdk
'''''''''''''''''''''''''''''''''''''''''''''

编辑 ``build.gradle`` 文件，添加：

::

    compile("com.webank:weid-java-sdk:x.x.x")

.. note::
   x.x.x为您使用的weid-java-sdk版本号，建议指定使用版本号，避免自动引入最新版本导致前后不兼容。 版本号可以查看 \ `版本历史 <https://mvnrepository.com/artifact/com.webank/weid-java-sdk>`__



2. 配置您的 Java 应用
''''''''''''''''''''''''

通过“WeIdentity 部署工具”完成部署和配置后，会在其``resources``目录生成相应的配置文件，将这些配置文件拷贝至您的 Java 应用的``resources``目录：

您可以将 ``weid-build-tools/resources`` 目录下的所有文件拷贝至您的Java应用的 ``resources`` 目录下，WeIdentity Java SDK 会自动加载相应的资源文件。



3. 集成完成
''''''''''''''''

现在您可以使用 WeIdentity 开发您的区块链身份应用。WeIdentity Java SDK 相关接口请见：\ `WeIdentity JAVA SDK文档 <./projects/javasdk/weidentity-java-sdk-doc.html>`__ 。


.. note::
     注意：如果您使用了针对oracle jdk1.8.0.231及以上版本，不论用哪种方式部署SDK，在使用时都需要配置jvm参数 -Djdk.tls.namedGroups="secp256k1" 。详细原因，请见Oracle JDK 8u231的Release Notes： https://www.oracle.com/technetwork/java/javase/8u231-relnotes-5592812.html
