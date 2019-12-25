.. role:: raw-html-m2r(raw)
   :format: html

.. _weidentity-installation:

WeIdentity Java SDK 安装部署
================================


整体介绍
--------

  通过安装部署脚本，可以帮助您快速完成源码的编译打包以及智能合约的部署，您只需要进行一些简单的配置，即可快速的生成一套可执行并配置好的WeIdentity运行环境.

准备工作
--------

.. list-table::
   :header-rows: 1

   * - 配置
     - 说明
   * - 操作系统
     - CentOS （7.2 64位）或Ubuntu（16.04 64位）。
   * - FISCO-BCOS区块链环境
     - 您需要有一套可以运行的FISCO-BCOS区块链环境，如果没有，可以参考\ `「FISCO-BCOS 2.0节点安装方法」 <https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/installation.html>`_\ 或\ `「FISCO-BCOS 1.3节点安装方法」 <https://fisco-bcos-documentation.readthedocs.io/zh_CN/release-1.3/docs/tools/index.html>`_\ 来搭建一套区块链环境。
   * - JDK
     - 要求\ `JDK1.8+ <https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html>`_\ ，推荐使用jdk8u141。
   * - gradle
     - WeIdentity JAVA SDK使用\ `gradle <https://gradle.org/>`_\ 进行构建，您需要提前安装好gradle，版本要求不低于4.3。
   * - 网络连通
     - 检查WeIdentity JAVA SDK部署环境是否能telnet通FISCO BCOS节点的channelPort端口，若telnet不通，需要检查网络连通性和安全策略。


安装部署
--------

我们提供两种方式安装部署SDK（包括两步：首先将WeIdentity智能合约部署到区块链上，再集成SDK到您的Java工程中）：

* `安装部署工具方式 <./weidentity-build-with-deploy.html>`_ （推荐方式）   
* `源码方式 <./weidentity-installation-by-sourcecode.html>`_ 

.. note::
     注意：如果您使用了针对oracle jdk1.8.0.231及以上版本，不论用哪种方式部署SDK，在使用时都需要配置jvm参数 -Djdk.tls.namedGroups="secp256k1" 。详细原因，请见Oracle JDK 8u231的Release Notes： https://www.oracle.com/technetwork/java/javase/8u231-relnotes-5592812.html


便捷工具
----------

当您部署SDK完成之后，可以通过一些便捷工具快速上手体验SDK，请参考 \ `WeIdentity JAVA SDK 便捷工具使用 <./weidentity-quick-tools.html>`__\。

.. toctree::
   :hidden:
   :maxdepth: 1
   
   weidentity-build-with-deploy.rst
   weidentity-installation-by-sourcecode.rst
   weidentity-quick-tools.rst