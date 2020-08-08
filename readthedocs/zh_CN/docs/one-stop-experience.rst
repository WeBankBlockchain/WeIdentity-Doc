
.. _one-stop-experience:

WeIdentity 入门
======================

准备工作
--------

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


第1步：安装 WeIdentity 部署工具
------------------------------------------

您可以参照\ `“安装 WeIdentity 部署工具” <./weidentity-installation-by-web.html>`_\，安装 WeIdentity 部署工具，这是一个网页工具，可以通过工具可以完成WeIdentity的部署，同时也提供了一些其他功能。

第2步：使用 WeIdentity 部署工具完成部署
---------------------------------------------------

参照\ `“配置教程” <./deploy-via-web.html>`_\，完成配置和部署。


第3步：运行 Sample 代码，体验接口（可选）
-------------------------------------------------------------

.. note::
     如果不想体验 Sample，可以直接跳过这一步


您可以参考\ `“开发样例使用” <./weidentity-sample-tryit.html>`_\，体验 WeIdentity 的各种接口。


第4步：在自己的 Java Service 中集成 WeIdentity Java SDK
-----------------------------------------------------------------

参考：\ `“集成 WeIdentity Java SDK” <sdk-integration.html>`_\

.. note::
     如果您是要的是其他语言而非 Java，可以参照\ `RestService文档 <./weidentity-rest.html>`_\，通过 WeIdentity Rest Service 来调用相关的接口。


其他：WeIdentity Java SDK 接口文档
------------------------------------------------

参考\ `“Java SDK文档” <https://weidentity.readthedocs.io/projects/javasdk/zh_CN/latest/>`_\，深入了解 WeIdentity 的接口调用。

其他： WeIdentity 部署工具其他功能介绍
-----------------------------------------------------

参考\ `“WeIdentity 部署工具使用简介” <./weidentity-quick-tools-web.html>`_\。

其他： WeIdentity 命令行工具使用
-----------------------------------------------------

我们提供了一些快捷工具，可以帮您快速体验 weid-java-sdk，请参考\ `“WeIdentity JAVA SDK 便捷工具使用” <./weidentity-quick-tools.html>`__\。
