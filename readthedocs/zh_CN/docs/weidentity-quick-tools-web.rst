.. role:: raw-html-m2r(raw)
   :format: html

.. _weidentity-quick-tools-web:

WeIdentity 部署工具使用简介
============================================================

通过 WeIdentity 部署工具的 Web 页面, 您可以快速的部署和使用 WeIdentity JAVA SDK。
WeIdentity 部署工具主要提供以下功能:

   - 配置管理

   - 配置区块链节点
   - 配置主群组
   - 配置数据库
   - 配置WeID账户

   - 功能管理

   - 部署 WeIdentity 智能合约
   - 部署 Evidence 智能合约
   - 管理 WeID
   - 管理权威凭证发行者(Authority Issuer)
   - 管理特定类型的发行者(Specific Issuer)
   - 管理凭证类型(CPT)
   - 管理披露策略(Presentation Policy)

   - 异步上链管理

   - 管理 Evidence 异步上链

   .. image:: images/weidentity-quick-tools-web.png
      :alt: weidentity-quick-tools-web.png

安装部署 WeIdentity JAVA SDK
--------------------------------

   安装部署 WeIdentity JAVA SDK, 详见文档: \ `使用 WeIdentity 部署工具完成部署（可视化部署方式） <./deploy-via-web.html>`__\。


使用 WeIdentity JAVA SDK
--------------------------------

准备: 打开 WeIdentity 部署工具的 Web 页面
""""""""""""""""""""""""""""""""""""""""""""""""""""""

   通过安装 “ WeIdentity 部署工具” 的服务器的公网 IP 访问 Web 页面 :code:`http://ip:6102/index.html` 以便捷的使用 WeIdentity JAVA SDK 的功能。

   .. note::
      在使用之前, 请确保已安装部署 WeIdentity JAVA SDK。

1. 部署 WeIdentity 智能合约(仅联盟链委员会管理员)
"""""""""""""""""""""""""""""""""""""""""""""""

   - 部署 WeIdentity 智能合约
      在菜单栏点击功能管理 -> 部署 WeIdentity 智能合约 -> 主群组部署 WeIdentity 智能合约。
      创建成功后页面显示 WeIdentity 智能合约相关信息, 如下图所示。

      .. image:: images/ weidentity-quick-tools-web-deploy-weid-contract.png
         :alt: weidentity-quick-tools-web-deploy-weid-contract.png

   - 启用 WeIdentity 智能合约
      在菜单栏点击功能管理 -> 部署 WeIdentity 智能合约, 选择合约, 点击`启用`按钮。
      首次部署的 WeIdentity 智能合约, 将自动被启用。

   - 删除 WeIdentity 智能合约
      在菜单栏点击功能管理 -> 部署 WeIdentity 智能合约, 选择合约, 点击`删除`按钮。

   .. note::
      1. 非联盟链委员会管理员仅可以查看已部署的 WeIdentity 智能合约, 无法进行部署, 启用, 删除等操作。
      2. 联盟链委员会管理员可多次部署 WeIdentity 智能合约, 若启用新合约, 则旧合约将自动更改为未启用状态。
      因此, 多次部署合约一般用于测试等特殊场景。

2. 部署 Evidence 智能合约
"""""""""""""""""""""""""""

   - 部署 Evidence 智能合约

      在菜单栏点击功能管理 -> 部署 Evidence 智能合约 -> 部署 Evidence 智能合约。
      创建成功后页面显示 Evidence 智能合约相关信息。

      .. image:: images/ weidentity-quick-tools-web-deploy-evidence-contract.png
         :alt: weidentity-quick-tools-web-deploy-evidence-contract.png

   .. note::
      Evidence 智能合约仅可在多群组的场景下, 根据实际业务需要创建。

3. WeID 管理
"""""""""""""""""""""""""""

   - 创建 WeID

      在菜单栏点击功能管理 -> WeID管理 -> 创建WeID。
      可选择以下任意一种方式创建新的 WeID, 如下图所示。
         * 默认方式创建 WeID (系统自动创建公私钥)
         * \ `自定义私钥创建 WeID (自行上传私钥) <./weidentity-quick-tools-web.html>`__\
         * \ `代理模式创建 WeID (自行上传公钥) <./weidentity-quick-tools-web.html>`__\

      .. image:: images/weidentity-quick-tools-web-create-weid.png
         :alt: weidentity-quick-tools-web-create-weid.png

   - 将 WeID 注册为权威凭证发行者

      在菜单栏点击功能管理 ->  WeID管理, 选择已创建好的WeID, 点击`注册为权威凭证发行者`按钮进行注册。
      具体操作详见 管理权威凭证发行者_。

   - 将 WeID 注册为特定类型的发行者

      在菜单栏点击功能管理 ->  WeID管理, 选择已创建好的WeID, 点击`注册为特定类型的发行者`按钮进行注册。
      具体操作详见 管理特定类型的发行者_ 。

   .. note::
     使用 Weidentity 部署工具部署后, 系统默认为 Admin 账户创建 WeID。

.. _管理权威凭证发行者:

4. 权威凭证发行者(Authority Issuer)管理
"""""""""""""""""""""""""""""""""""""""

   - 注册权威凭证发行者

      在菜单栏点击功能管理 -> 权威凭证发行者 -> 注册权威凭证发行者。
      填入所要注册的 WeID (必须是已生成的 WeID ), 并自定义权威机构名称, 如下图所示。

      .. image:: images/weidentity-quick-tools-web-register-authority-issuer.png
         :alt: weidentity-quick-tools-web-register-authority-issuer.png

   - 删除权威凭证发行者

      在菜单栏点击功能管理 -> 权威凭证发行者, 选择权威凭证发行者, 点击`删除`按钮。

   .. note::
      1. 仅委员会成员(Committee Member)可以进行本节操作，若您不是委员会成员，请将您的 WeIdentity DID 和机构名称发给委员会成员，让其帮您注册成为 Authority Issuer。
      2. 每个 WeIdentity DID 只能注册一次, 若需更换权威机构名称, 请点击`删除`按钮再重新注册。

.. _管理特定类型的发行者:

5. 特定类型的发行者(Specific Issuer)管理
""""""""""""""""""""""""""""""""""""""""""""""""""""""

   - 注册特定类型的发行者

      在菜单栏点击功能管理 -> 特定类型的发行者 -> 注册特定类型的发行者。
      自定义类型名称,点击`注册`按钮, 如下图所示。

      .. image:: images/weidentity-quick-tools-web-register-specific-issuer.png
         :alt: weidentity-quick-tools-web-register-specific-issuer.png

   - 将 WeID 注册为特定类型的发行者

      在菜单栏点击功能管理 -> 特定类型的发行者, 选择某个已注册的特定类型的发行者, 点击`将WeID注册为这个特定类型的发行者`按钮, 填入所需注册的 WeID, 如下图所示。

      .. image:: images/weidentity-quick-tools-web-register-weid-as-specific-issuer.png
         :alt: weidentity-quick-tools-web-register-weid-as-specific-issuer.png

   - 将 WeID 从特定类型的发行者中移除

      在菜单栏点击功能管理 -> 特定类型的发行者, 选择某个已注册的特定类型的发行者, 展开内容, 选择要移除的 WeID, 点击`删除`按钮。

   .. note::
      1. 只有委员会成员(Committee Member)可以进行本节操作，若您不是委员会成员，您可以将您的 WeIdentity DID 和机构名称发给委员会成员，让其帮您注册成 Specific Issuer。
      2. 目前暂不支持通过页面删除特定类型的发行者。

6. 凭证类型(CPT)管理
"""""""""""""""""""""""""""

   - 注册凭证类型

      在菜单栏点击功能管理 -> 凭证类型(CPT)管理 -> 注册新的凭证类型(CPT)。
      通过以下任意一种方式提供 CPT 内容, 并点击`注册`按钮, 如图所示。
         * 上传 CPT JSON 文件(:download:`点击下载 CPT 样例 <./samples/id_card.json>`)
         * 选择预置 CPT 模版
         * 在窗口内直接编辑 CPT 内容

      .. image:: images/weidentity-quick-tools-web-register-cpt.png
         :alt: weidentity-quick-tools-web-register-cpt.png

   - 下载凭证类型

      在菜单栏点击功能管理 -> 凭证类型(CPT)管理, 选择已注册的凭证类型, 点击`下载CPT`按钮。

   - 将凭证类型转为Jar包

      在菜单栏点击功能管理 -> 凭证类型(CPT)管理, 选择已注册的凭证类型, 点击`将选中凭证类型(CPT)转成Jar包`按钮, 如图所示。

      .. image:: images/weidentity-quick-tools-web-convert-cpt-jar.png
         :alt: weidentity-quick-tools-web-convert-cpt-jar.png

   .. note::
      1. 注册凭证类型时, 若需自定义 CPT ID, 可在\ `WeIdentity CPT智能合约 <./weidentity-contract-design.html>`__\ 中参考 CPT ID 设计。
      2. 若在窗口内直接编辑CPT内容, 请确保正确使用空格与 Tab 键。

7. 披露策略(Presentation Policy)管理
""""""""""""""""""""""""""""""""""""""""""""""""""""""

   - 将披露策略转为Jar包

      在菜单栏点击功能管理 -> CPT转JAVA Jar包 -> 披露策略转Jar包。
      上传披露策略文件(:download:`点击下载披露策略样例 <./samples/presentation_policy.json>`)或者在窗口内直接编辑内容, 点击`转换`按钮, 如下图所示。

      .. image:: images/weidentity-quick-tools-web-convert-presentation-policy-to-jar.png
         :alt: weidentity-quick-tools-web-convert-presentation-policy-to-jar.png

   - 下载披露策略

      在菜单栏点击功能管理 -> CPT转JAVA Jar包, 选择需下载的披露策略, 点击`下载披露策略文件`按钮, 如下图所示。

      .. image:: images/weidentity-quick-tools-web-download-presentation-policy.png
         :alt: weidentity-quick-tools-web-download-presentation-policy.png

   - 下载披露策略Jar包

      在菜单栏点击功能管理 -> CPT转JAVA Jar包, 选择需下载的披露策略, 点击`下载Jar包`按钮。

8. 异步上链管理
""""""""""""""""""""""""""""""""""""""""""""""""""""""

   - Evidence 异步上链管理
