.. role:: raw-html-m2r(raw)
   :format: html

.. _deploy-via-web:

使用 WeIdentity 部署工具完成部署（可视化部署方式）
=====================================================================

0. 打开 WeIdentity 部署工具的 Web 页面
""""""""""""""""""""""""""""""""""""""""""""""""""""""

通过安装 “ WeIdentity 部署工具” 的服务器的公网 IP 访问 Web 页面 :code:`http://ip:6102/guide.html` 以进行 WeIdentity JAVA SDK 的部署。

.. note::
     1. 在使用之前, 请确保已安装 WeIdentity 部署工具, 详见文档：\ `安装 WeIdentity 部署工具 <./weidentity-installation.html>`_\
     2. 若无法使用 Web 页面, 请使用命令行的方式完成部署, 详见文档：\ `部署文档(命令行部署方式) <./deploy-via-commandline.html>`_\。
     3. 在部署之前, 请确保已安装 FISCO-BCOS 区块链, 详见文档：`「FISCO-BCOS 2.x 节点安装方法」 <https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/installation.html>`_\

1. 选择角色
"""""""""""""""""""""""""""

此步骤可选择部署时所用的角色, 包括 “联盟链委员会管理员” 和 “非联盟链委员会管理员”, 如下图所示。

   .. image:: images/deploy-via-web-guide-choose-role.png
      :alt: deploy-via-web-guide-choose-role.png

.. note::
     什么是“联盟链委员会管理员”?
       一条联盟链中, 选取一家机构来作为联盟链管理员, 一般为创世节点所在机构, 此机构将作为联盟链委员会管理员来管理此链中使用的WeID相关合约。

2. 配置区块链节点
"""""""""""""""""""""""""""

此步骤将配置需连接的区块链节点(:download:`点击下载配置示例 <./samples/run.config.sample>`), 如下图所示。

   .. image:: images/deploy-via-web-guide-setup-blockchain.png
      :alt: deploy-via-web-guide-setup-blockchain.png

   - 配置机构名称
      * 配置说明：机构名称用于标识机构唯一性, 可作为链上存储时的唯一标识, 亦可用作机构间的 AMOP 通信标识, 还可作为联盟链成员的IssuerName。
      * 配置要求：建议使用机构的英文名称或简称, 并确保机构名称在联盟链成员中唯一。

   - 配置 AMOP 通讯 ID
      * 配置说明：此 ID 将作为节点间 AMOP 通讯所需要的Topic来进行监听。AMOP 通讯可在不同机构的节点间通讯, 亦可在同一机构内的不同节点间通讯。
      * 配置要求：建议使用英文, 并确保 ID 机构唯一或 VPC 唯一。

   - 配置部署环境
      * 配置说明：目前支持三种部署环境, 生产环境, 测试环境和开发环境。不同环境可使用同一条区块链, 亦可各自使用独立的链。
      * 配置要求：请根据实际需要选择, 并确保联盟链成员的环境一致。

   - 配置区块链节点 IP 和 Channel 端口
      * 配置说明：区块链节点 IP 为 WeIdentity 所需使用的区块链节点的内网或公网 IP。Channel 端口用于 WeIdentity 部署工具, WeIdentity SDK 与区块链节点间的通信。
      * 配置要求：格式为区块链节点 IP:Channel 端口。如需使用多个区块链节点, 请用半角逗号","分隔。默认端口号为20200。
      * 配置示例：10.10.4.1:20200,10.10.4.2:20200,127.0.0.1:20200

   - 配置 sdk 证书
      * 配置说明：通过web3sdk连接机构节点时需要使用 sdk 证书。
      * 配置要求：登录区块链节点服务器, 生成/拷贝证书文件, 并下载到本机后上传。sdk 证书文件包括 ca.crt, node.crt 和 node.key。

.. note::
     1. \ `「什么是 AMOP ?」 <https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/manual/amop_protocol.html?highlight=amop>`_\
     2. \ `「什么是 Channel 端口?」 <https://mp.weixin.qq.com/s/XZ0pXEELaj8kXHo32UFprg>`_\
     3. \ `「如何获得 sdk 证书?」 <https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/enterprise_tools/operation.html#get-sdk-file>`_\
     4. \ `「区块链中的各种证书」 <https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/manual/certificates.html>`_\

3. 选择主群组 ID
"""""""""""""""""""""""""""

此步骤可选择所需使用的主群组, 如下图所示。

   .. image:: images/deploy-via-web-guide-choose-group-id.png
      :alt: deploy-via-web-guide-choose-group-id.png

   - 选择主群组 ID
      * 配置说明：主群组编号为联盟链成员节点所共有的群组编号, 此群组编号为区块链搭建的时候联盟链成员协调出来的群组编号。
      * 配置要求：默认主群组 ID 为 1。请确保联盟链成员选择使用一致的主群组。

.. note::
   \ `「如何查看群组?」 <https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/manual/console.html#getgrouplist>`_\

4. 配置数据库(可选)
"""""""""""""""""""""""""""

此步骤将配置所需连接的数据库环境, 请提前安装数据库并创建数据库实例及用户, 如下图所示。

   - 配置数据库
      * 配置说明：目前支持MySql 5.7+ 数据库。当使用 Transportation, Envidence 异步存证, Persistence 数据存储等相关功能组件时, 必需使用数据库。
      * 配置示例: :download:`点击下载配置示例 <./samples/run.config.sample>`

   .. image:: images/deploy-via-web-guide-setup-database.png
      :alt: deploy-via-web-guide-setup-database.png

5. 创建管理员 WeID (仅限联盟链委员会管理员)
""""""""""""""""""""""""""""""""""""""""""

此步骤将配置您在 weid-build-tools 里面的 Admin 账户, 后续的部署等操作将使用该账户（请妥善保管私钥, 谨防丢失）。

   .. image:: images/deploy-via-web-guide-create-admin-weid.png
      :alt: deploy-via-web-guide-create-admin-weid.png

6. 部署 WeIdentity 智能合约(仅限联盟链委员会管理员)
"""""""""""""""""""""""""""""""""""""""""""""""""

此步骤将部署 WeIdentity 智能合约 到指定的区块链上, 如图所示。

   .. image:: images/deploy-via-web-guide-deploy-weid-contract.png
      :alt: deploy-via-web-guide-deploy-weid-contract.png

   - 选择链 ID (Chain Id)
         * 配置说明：待连接节点所属的链ID, 安装区块链时自动生成, 一般情况下无需更改。
         * 配置要求: 默认 Chain Id 为 1。

.. note::
   1. \ `「什么是链 ID (Chain Id) ?」 <https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/sdk/go_sdk/env_conf.html?highlight=%E9%93%BEid#id6>`_\
   2. WeIdentity 智能合约部署完成后将会在 `./output/admin/` 目录下生成 Admin 密钥文件, 用于后续注册权威机构等操作, 请妥善保管。
   3. WeIdentity 智能合约部署完成后将会在 `./resources/` 目录下生成相关文件, 以便于您的应用集成 WeIdentity JAVA SDK。

至此, WeIdentity JAVA SDK 的部署已完成。您可以继续通过 WeIdentity 部署工具体验使用 WeIdentity JAVA SDK 的功能, 详见文档。
