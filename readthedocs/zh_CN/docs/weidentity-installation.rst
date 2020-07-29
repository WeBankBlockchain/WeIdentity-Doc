.. role:: raw-html-m2r(raw)
   :format: html

.. _weidentity-installation:

安装 WeIdentity 部署工具
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
     - CentOS （7.2.* 64位）或Ubuntu（16.04 64位）。
   * - FISCO-BCOS区块链环境
     - 您需要有一套可以运行的FISCO-BCOS区块链环境，如果没有，可以参考\ `「FISCO-BCOS 2.0节点安装方法」 <https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/installation.html>`_\ 来搭建一套区块链环境。
   * - JDK
     - 要求\ `JDK1.8+ <https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html>`_\ ，推荐使用jdk8u141。JDK 跟 WeID 直接的兼容性，可见\ `兼容性文档 <https://weidentity.readthedocs.io/zh_CN/latest/docs/weid-compatibility-test.html>`_\
   * - 网络连通
     - 检查部署 WeIdentity JAVA SDK 的服务器是否能 telnet 通 FISCO BCOS 节点的 channel 端口（\ `channel端口是什么，详见 <https://mp.weixin.qq.com/s/XZ0pXEELaj8kXHo32UFprg>`_\），若telnet不通，需要检查网络连通性和安全策略。


安装 WeIdentity 部署工具
------------------------

我们提供两种方式安装 WeIdentity 部署工具（包括两步：首先将WeIdentity智能合约部署到区块链上，再集成SDK到您的Java工程中）：

* `通过脚本进行安装 <./weidentity-installation-by-web.html>`_ （推荐方式）
* `下载源码的方式进行安装 <./weidentity-installation-by-sourcecode.html>`_  （代码贡献者按这种方式搭建）

.. note::
     注意：如果您使用了针对oracle jdk1.8.0.231及以上版本，不论用哪种方式部署SDK，在使用时都需要配置jvm参数 -Djdk.tls.namedGroups="secp256k1" 。详细原因，请见Oracle JDK 8u231的Release Notes： https://www.oracle.com/technetwork/java/javase/8u231-relnotes-5592812.html


.. toctree::
   :hidden:
   :maxdepth: 1

   weidentity-build-with-deploy.rst
   weidentity-installation-by-sourcecode.rst


便捷工具
----------

当您部署SDK完成之后，可以通过一些便捷工具快速上手体验SDK，请参考 \ `WeIdentity 便捷工具使用 <./weidentity-quick-tools.html>`__\。


.. toctree::
   :hidden:
   :maxdepth: 1

   weidentity-quick-tools.rst
