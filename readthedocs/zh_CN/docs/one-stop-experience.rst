
.. _one-stop-experience:

WeIdentity 一站式体验
======================

本页化繁为简地聊了聊WeIdentity，您可以在此快速了解WeIdentity的参考场景、体验Demo、快速部署并体验WeIdentity的核心功能。
如果您是开发人员，还可以进一步了解WeIdentity的参考实现，以及深入了解SDK的使用方式。

WeIdentity参考场景
-------------------

.. image:: images/roles-relation.png
   :alt: roles-relation.png

在WeIdentity生态中，存在着上图所示的几类角色，不同角色的权责和角色之间的关系如下表所示：

.. list-table::
   :header-rows: 1

   * - 角色
     - 说明
   * - User (Entity)
     - 用户（实体）。会在链上注册属于自己的WeIdentity DID，从Issuer处申请Credential，并授权转发或直接出示给Verifier来使用之。
   * - Issuer
     - Credential的发行者。会验证实体对WeIdentity DID的所有权，其次发行实体相关的Credential。
   * - Verifier
     - Credential的使用者。会验证实体对WeIdentity DID的所有权，其次在链上验证Credential的真实性，以便处理相关业务。
   * - User Agent / Credential Repository
     - 用户（实体）在此生成WeIdentity DID。为了便于使用，实体也可将自己的私钥、持有的Credential托管于此。

在实际业务里，WeIdentity可以被广泛运用在「实体身份标识」及「可信数据交换」场景中。首先，通过User Agent为不同的实体生成独立唯一的DID；其次，Issuer验证实体身份及DID所有权，为实体发行各种各样的电子化Credential。当实体需要办理业务的时候，可以直接将Credential出示给Verifier，也可以通过在链上进行主动授权 + 授权存证上链的方式，由之前授权的凭证存储机构转发给Verifier。

以上流程，保证了数据以实体用户为中心，同时实体身份、确权、授权等操作在链上完成，可追溯，可验证，不可篡改。

Demo体验
---------- 

下面提供了几个不同场景的WeIdentity Demo：

.. list-table::
   :header-rows: 1

   * - 使用场景
     - 访问入口
     - 设计说明
   * - 学历信息电子化
     - \ `开始体验 <https://sandbox.webank.com/weid>`_\
     - 基于WeID，将用户身份同电子身份ID对应的学历信息电子化，Hash上链，保证身份和学历信息高效验证，不可篡改

1. 安装 WeIdentity 部署工具
------------------------------

您可以参照\ `安装 WeIdentity 部署工具 <./weidentity-installation.html>`_\，安装 WeIdentity 部署工具。

2. 使用 WeIdentity 部署工具完成部署
---------------------------------------

参照\ `部署文档 <./deploy-via-web.html>`_\，完成部署和配置。


3. 运行 Sample 代码，体验接口（可选）
-------------------------------------------------

.. note::
     如果不想体验 Sample，可以直接跳过这一步


您可以参考\ `开发样例使用 <./weidentity-sample-commandline.html>`_\，体验 WeIdentity 的各种接口。


4. 在自己的 Java Service 中集成 WeIdentity Java SDK 
-----------------------------------------------------

参考：\ `集成 WeIdentity Java SDK <sdk-integration.html>`_\

.. note::
     如果您是要的是其他语言而非 Java，可以参照\ `RestService文档 <./weidentity-rest.html>`_\，通过 WeIdentity Rest Service 来调用相关的接口。


其他：SDK文档
------------------

参考\ `Java SDK文档 <https://weidentity.readthedocs.io/projects/javasdk/zh_CN/latest/>`_\，深入了解 WeIdentity 的接口调用。


其他： WeIdentity JAVA SDK 便捷工具使用
-----------------------------------------

我们提供了一些快捷工具，可以帮您快速体验 weid-java-sdk，请参考\ `WeIdentity JAVA SDK 便捷工具使用 <./weidentity-quick-tools.html>`__\。

