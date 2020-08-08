.. role:: raw-html-m2r(raw)
   :format: html

.. _weidentity-quick-tools-web:

WeIdentity 部署工具使用简介
============================================================

.. note::
   如果还未完成配置，详见文档: \ `使用 WeIdentity 部署工具完成部署（可视化部署方式） <./deploy-via-web.html>`__\。


WeIdentity 部署工具的 Web 页面, 主要提供以下功能:

  - 配置管理
    - 配置区块链节点
    - 配置主群组
    - 配置数据库
    - 配置 WeID 账户

  - 功能管理
    - 部署 WeIdentity 智能合约
    - 部署 Evidence 智能合约
    - 管理 WeID
    - 管理权威凭证发行者(Authority Issuer)
    - 管理 WeID 白名单
    - 管理凭证类型(CPT)

  - 异步上链管理
    - 管理 Evidence 异步上链

   .. image:: images/weidentity-quick-tools-web.png
      :alt: weidentity-quick-tools-web.png


WeIdentity 部署工具功能介绍
--------------------------------

1. 部署 WeIdentity 智能合约(仅联盟链委员会管理员可用)
"""""""""""""""""""""""""""""""""""""""""""""""

   - 部署 WeIdentity 智能合约
      在菜单栏点击功能管理 -> 部署 WeIdentity 智能合约 -> 主群组部署 WeIdentity 智能合约。
      创建成功后页面显示 WeIdentity 智能合约相关信息, 如下图所示。

      .. image:: images/ weidentity-quick-tools-web-deploy-weid-contract.png
         :alt: weidentity-quick-tools-web-deploy-weid-contract.png

   - 启用 WeIdentity 智能合约
      在菜单栏点击功能管理 -> 部署 WeIdentity 智能合约, 选择合约, 点击`启用`按钮。

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

      .. image:: images/ weidentity-quick-tools-web-deploy-evidence-contract2.png
         :alt: weidentity-quick-tools-web-deploy-evidence-contract2.png

   .. note::
      Evidence 智能合约仅可在多群组的场景下, 根据实际业务需要创建。

3. WeID 管理
"""""""""""""""""""""""""""

   - 创建 WeID

      在菜单栏点击功能管理 -> WeID管理 -> 创建WeID 。
      可选择以下任意一种方式创建新的 WeID, 如下图所示。
         * 默认方式创建 WeID (系统自动创建公私钥)
         * 自定义私钥创建 WeID (自行上传私钥)
         * 代理模式创建 WeID (自行上传公钥)

      .. image:: images/weidentity-quick-tools-web-create-weid.png
         :alt: weidentity-quick-tools-web-create-weid.png

      .. _key_generation:

      创建密钥示例代码
      ::

         import org.fisco.bcos.web3j.crypto.ECKeyPair;
         import org.fisco.bcos.web3j.crypto.Keys;
         import java.security.InvalidAlgorithmParameterException;
         import java.security.NoSuchAlgorithmException;
         import java.security.NoSuchProviderException;

         public class ECKeySampleApp {

             public static void main (String[] args) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
                 ECKeyPair keyPair = Keys.createEcKeyPair();
                 System.out.println("public key " + keyPair.getPublicKey());
                 System.out.println("private key " + keyPair.getPrivateKey());
                 // public key
                 // 2826353706326430136059766899918547268257144433345028935544246672544715811531698763009967557019653807523504447872634462259780101707992526761608737256788009
                 // private key
                 // 51801066929398358250268966823436564939107125383375289829603669124463475610644
             }
         }

   - 将 WeID 注册为权威凭证发行者

      在菜单栏点击功能管理 ->  WeID管理, 选择已创建好的WeID, 点击`注册为权威凭证发行者`按钮进行注册, 如下图所示。
      更多具体操作详见 管理权威凭证发行者_。

      .. image:: images/weidentity-quick-tools-web-create-weid-to-authority-issuer.png
         :alt: weidentity-quick-tools-web-create-weid-to-authority-issuer.png

   - 将 WeID 添加到白名单

      在菜单栏点击功能管理 ->  WeID管理, 选择已创建好的WeID, 点击`添加到白名单`按钮, 如下图所示。
      更多具体操作详见 管理WeID白名单_ 。

      .. image:: images/weidentity-quick-tools-web-create-weid-to-whitelist.png
         :alt: weidentity-quick-tools-web-create-weid-to-whitelist.png

   .. note::
     使用 Weidentity 部署工具部署后, 系统默认为 Admin 账户创建 WeID 。

.. _管理权威凭证发行者:

4. 权威凭证发行者(Authority Issuer)管理
"""""""""""""""""""""""""""""""""""""""

   - 注册权威凭证发行者

      在菜单栏点击功能管理 -> 权威凭证发行者 -> 注册权威凭证发行者。
      填入所要注册的 WeID (必须是已生成的 WeID ), 并自定义权威机构名称, 如下图所示。

      .. image:: images/weidentity-quick-tools-web-register-authority-issuer.png
         :alt: weidentity-quick-tools-web-register-authority-issuer.png


   - 认证权威凭证发行者

      在菜单栏点击功能管理 -> 权威凭证发行者, 选择权威凭证发行者, 点击`认证`按钮, 如下图所示。

      .. image:: images/weidentity-quick-tools-web-register-authority-issuer-auth.png
         :alt: weidentity-quick-tools-web-register-authority-issuer-auth.png

   - 撤销权威凭证发行者

      在菜单栏点击功能管理 -> 权威凭证发行者, 选择权威凭证发行者, 点击`撤销认证`按钮, 如下图所示。

      .. image:: images/weidentity-quick-tools-web-register-authority-issuer-revoke.png
         :alt: weidentity-quick-tools-web-register-authority-issuer-revoke.png

   .. note::
      1. 仅委员会成员(Committee Member)可以进行本节操作，若您不是委员会成员，请将您的 WeIdentity DID 和机构名称发给委员会成员，让其帮您注册成为 Authority Issuer。
      2. 每个 WeIdentity DID 只能注册一次, 若需更换权威机构名称, 请点击`撤销认证`按钮再重新注册。

.. _管理WeID白名单:

5. WeID 白名单管理
""""""""""""""""""""""""""""""""""""""""""""""""""""""

   - 新增 WeID 白名单

      在菜单栏点击功能管理 -> 白名单功能管理 -> 新增白名单。
      自定义白名单名称,点击`新增`按钮, 如下图所示。

      .. image:: images/weidentity-quick-tools-web-add-whitelist.png
         :alt: weidentity-quick-tools-web-add-whitelist.png

   - 将 WeID 添加到白名单

      在菜单栏点击功能管理 -> 白名单功能管理, 选择某个已创建的白名单, 点击`添加WeID到这个白名单`按钮, 填入所需添加的 WeID, 如下图所示。

      .. image:: images/weidentity-quick-tools-web-add-weid-to-whitelist.png
         :alt: weidentity-quick-tools-web-add-weid-to-whitelist.png

   - 将 WeID 从白名单移除

      在菜单栏点击功能管理 -> 白名单功能管理, 选选择某个已创建的白名单, 展开内容, 选择要移除的 WeID, 点击`删除`按钮。

   .. note::
      1. 只有委员会成员(Committee Member)可以进行本节操作，若您不是委员会成员，您可以将您的 WeIdentity DID 和机构名称发给委员会成员，让其帮您添加到白名单。
      2. 目前暂不支持通过页面删除白名单。

6. 凭证类型(CPT)管理
"""""""""""""""""""""""""""

   - 注册凭证类型

      在菜单栏点击功能管理 -> 凭证类型(CPT)管理 -> 注册新的凭证类型(CPT)。
      通过以下任意一种方式提供 CPT 内容, 并点击`注册`按钮, 如下图所示。
         * 上传 CPT JSON 文件
         * 选择预置 CPT 模版
         * 在窗口内直接编辑 CPT 内容

      .. image:: images/weidentity-quick-tools-web-register-cpt.png
         :alt: weidentity-quick-tools-web-register-cpt.png

      .. _cpt_sample:

      CPT 样例文件 id_card.json

      .. literalinclude:: ./samples/id_card.json


   - 下载凭证类型

      在菜单栏点击功能管理 -> 凭证类型(CPT)管理, 选择已注册的凭证类型, 点击`下载CPT`按钮。

   .. note::
      1. 注册凭证类型时, 若需自定义 CPT ID, 可在\ `WeIdentity CPT智能合约 <./weidentity-contract-design.html>`__\ 中参考 CPT ID 设计。
      2. 若在窗口内直接编辑CPT内容, 请确保正确使用空格与 Tab 键。

7. 异步上链管理
""""""""""""""""""""""""""""""""""""""""""""""""""""""

   - Evidence 异步上链管理

      在菜单栏点击功能管理 -> 异步上链管理 -> Evidence异步上链管理, 选择所有查看的日期及处理类型, 点击`查询`按钮, 如下图所示。

      .. image:: images/weidentity-quick-tools-web-check-evidence-on-chain.png
         :alt: weidentity-quick-tools-web-check-evidence-on-chain.png
