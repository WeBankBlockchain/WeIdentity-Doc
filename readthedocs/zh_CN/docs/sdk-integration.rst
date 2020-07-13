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
   x.x.x为您使用的weid-java-sdk版本，建议使用最新版本。 查看 \ `版本历史 <https://mvnrepository.com/artifact/com.webank/weid-java-sdk>`__  



2. 配置您的 Java 应用
''''''''''''''''''''''''

将 weid-build-tools 里配置好的配置文件拷贝至您的 Java 应用中：
::

    cd resources/
    ls


您可以将 ``resources`` 目录下的所有文件拷贝至您的Java应用的 ``resources`` 目录下，weid-java-sdk 会自动加载相应的资源文件。



3. 集成完成
''''''''''''''''

现在您可以使用 WeIdentity 开发您的区块链身份应用。weid-java-sdk 相关接口请见：\ `WeIdentity JAVA SDK文档 <./projects/javasdk/weidentity-java-sdk-doc.html>`__ 。



