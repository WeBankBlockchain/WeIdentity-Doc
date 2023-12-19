
.. _weidentity-spec:

WeIdentity 规范
=====================

.. list-table::
   :header-rows: 1

   * - 版本
     - 说明
     - 作者
   * - V0.1.0
     - 完成整体协议框架
     - 张俊麒
   * - V0.2.0
     - 完善流程，ER等
     - 张俊麒
   * - V0.3.0
     - 添加选择性披露，Notification机制
     - 张俊麒
   * - V0.3.1
     - 补充WeIdentity Document，增加CPT字段定义
     - 陈浩
   * - V0.3.2
     - 描述修改
     - 张俊麒
   * - V0.3.3
     - 补充图解及撤销部分实现
     - 张俊麒，胡朝新


1. 设计目标
------------

.. raw:: html

    <embed>
      <table border='1' style="width:100%;border-collapse:collapse">
         <tr>
            <th width="100">目标</th>
            <th>说明</th>
         </tr>
         <tr>
            <td>多中心</td>
            <td>分布式多中心的ID注册机制，摆脱对传统模式下单一中心ID注册的依赖</td>
         </tr>
         <tr>
            <td>开源开放</td>
            <td>技术方案完全开源，面向政府、企业、开发者服务</td>
         </tr>
         <tr>
            <td>隐私保护</td>
            <td>实体的现实身份和可验证数字凭证的内容进行链下存储。支持实体将信息最小化或者选择性披露给其他机构，同时防止任何第三方反向推测出实体在现实世界或其他场景语义中的身份</td>
         </tr>
         <tr>
            <td>可移植性</td>
            <td>基于WeIdentity规范，数据可移植至遵循同样规范的其他平台，兼容业务主流区块链底层平台</td>
         </tr>
         <tr>
            <td>互操作性</td>
            <td>提供标准化接口，支持跨链、跨平台互操作</td>
         </tr>
         <tr>
            <td>可扩展性</td>
            <td>保证操作性，可移植性或简单性的情况下，数据模型可以通过多种不同方式进行扩展</td>
         </tr>
      </table>
      <br />
    </embed>


2. 相关术语
------------

详见：\ :ref:`术语表 <terminologies>`

3. WeIdentity DID
------------------

WeIdentity DID与WeIdentity Credential的关系
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^


.. image:: images/weidentity-er.png
   :alt: weidentity-er.png

从图中可见，WeIdentity DID与WeIdentity Credential的关系并非单纯的一对多：从设计目标上看，WeIdentity DID用来描述实体（人或物），WeIdentity Credential用来描述实体的身份、属性和实体间关系。因此，一个WeIdentity DID可以持有多个WeIdentity Credential；而一个WeIdentity Credential则会包含至少一个所描述的WeIdentity DID，可能会有多个。最后，每个WeIdentity DID都有一个WeIdentity Document，用来存储此DID的认证方式（如公钥、私钥套件）等信息，与WeIdentity Credential无关。

总体流程
^^^^^^^^


.. image:: images/overall-flow@2x.png
   :alt: overall-flow.png

一般来说，WeIdentity解决方案的基本流程如下：

   #. 用户根据业务需求，选择是否需要进行KYC认证。
   #. 用户生成WeIdentity DID。
   #. 用户向相关业务方申请Credential。
   #. 相关业务方扮演Issuer的角色，发行Credential交给用户。
   #. 用户成为了Credential的Holder。
   #. 用户出示Credential，以完成业务需求。
   #. 相关业务方扮演Verifier的角色，验证Credential有效性。

如何生成WeIdentity DID
^^^^^^^^^^^^^^^^^^^^^^

WeIdentity DID = did:weid:chain-id:bs-specific-string


.. image:: images/weidentity-did-format1.png
   :alt: weidentity-did-format1.png


.. list-table::
   :header-rows: 1

   * - 字段
     - 说明
   * - did
     - 遵循DID规范，使用固定字符“did”
   * - weid
     - WeIdentity DID规范的method name字段，固定为“weid”
   * - chain-id
     - 链 ID，用于路由到不同的链网络（如果需要跟其他链打通，需要找 WeIdentity 开源项目的 owner 微众银行注册路由信息），例如同时使用 WeIdentity 的可能有多条区块链，可以使用这个字段作为标识信息，路由到特定区块链
   * - bs-specific-string
     - 基于底层区块链平台生成，代表Entity在链上的地址，保证全网唯一


备注：bsSpecificString根据区块链底层平台和业务具体情况来确定生成规则，例如可以是随机字符串，或者区块链上的地址。

示例(这个例子中，chain-id是``101``: ``"did:weid:101:0x0086eb1f712ebc6f1c276e12ec21"``

WeIdentity Document格式
^^^^^^^^^^^^^^^^^^^^^^^

.. list-table::
   :header-rows: 1

   * - 字段
     - 说明
   * - @context
     - 用于描述WeIdentity Document结构等信息
   * - id
     - WeIdentity DID，表示当前Document描述的Entity，用于自描述
   * - created
     - Document的创建时间,标识创建操作的时间戳(总毫秒数)
   * - updated
     - Document的更新时间,标识上次更新操作的时间戳(总毫秒数)
   * - deactivated
     - Document是否停用标识
   * - versionId
     - Document 版本id,上一次更新操作的版本
   * - authentication
     - 用于Entity证明其与当前Document的关联性
   * - authentication.id
     - 验证方法id 需要符合 DID URL ABNF 语法规则：did-url = did path-abempty [ "?" query ] [ "#" fragment ]
   * - authentication.type
     - 用于指定signature suite 非国密：Ed25519VerificationKey2020，国密：SM2VerificationKey
   * - authentication.controller
     - 验证方法控制器 需要符合DID ABNF 语法规则 "did:" method-name ":" method-specific-id
   * - authentication.publicKeyMultibase
     - 验证方法材料  符合\ `「MULTIBASE」 <https://datatracker.ietf.org/doc/html/draft-multiformats-multibase-03>`_\ 编码的公钥
   * - service
     - service描述数组，用于描述跟当前DID相关的服务，一般以开放标准规范发布,格式如下
   * - service.id
     - service endpoint的ID 需要符合\ `「RFC3986」 <https://www.rfc-editor.org/rfc/rfc3986>`_\ uri规则
   * - service.type
     - service endpoint的协议
   * - service.serviceEndpoint
     - serviceEndpoint列表，可以是URI或者一个JSON-LD对象,需要符合\ `「RFC3986」 <https://www.rfc-editor.org/rfc/rfc3986>`_\ uri规则
   * - service.其他
     - 待定
   * - recovery
     - WeIdentity DID私钥丢失后，可由本字段指定的WeIdentity进行公私钥重置。是否需要抽象一层合约层来实现待定


* WeIdentity DID的Authorization机制及Recovery机制由补充规范实现，不在本文中定义。

示例：

.. code-block:: javascript

   {
     "@context": "https://weidentity.webank.com/did/v1",
     "id": "did:weid:1:123456789abcdefghi",
     "created": "1560419409898",
     "updated": "1560419409898",
     "authentication": [{
       "id": "did:weid:1:123456789abcdefghi",
       // this key can be used to authenticate as DID ...9938
       "type": "Ed25519VerificationKey2020",
       "publicKeyMultibase": "did:weid:1:123456789abcdefghi#keys-1"
       }
     ],
     "service": [{
       "id": "did:weid:1:123456789abcdefghi",
       "type": "drivingCardService",
       "serviceEndpoint": "https://weidentity.webank.com/endpoint/8377464"
       }, {
       "id": "did:weid:1:123456789abcdefghi",
       "type": "padiCertificateService",
       "serviceEndpoint": "https://weidentity.webank.com/endpoint/8377465"
     }],
     "recovery": ["did:weid:1:2323e3e3dweweewew2","did:weid:1:2323e3e3dweweewew3"],
   }

WeIdentity DID支持操作
^^^^^^^^^^^^^^^^^^^^^^

创建
~~~~

创建一个WeIdentity DID（以及对应的WeIdentity Document）

读取/验证
~~~~~~~~~

通过WeIdentity DID读取到WeIdentity Document，并验证

更新
~~~~

更新WeIdentity Document的相关属性

4. WeIdentity Credential
--------------------------

角色和关系
^^^^^^^^^^


.. image:: images/roles-relation.png
   :alt: roles-relation.png

如图所示在WeIdentity生态中，存在着以下角色：

   .. list-table::
      :header-rows: 1

      * - 角色
        - 说明
      * - User (Entity)
        - 用户（实体）。会注册属于自己的WeIdentity DID，申请Credential，并通过出示给相关业务方来使用之。
      * - Issuer
        - Credential的发行者。会首先验证实体对WeIdentity DID的所有权，其次发行Credential交给实体。
      * - Verifier
        - Credential的使用者。会首先验证实体对WeIdentity DID的所有权，其次验证Credential的有效性。
      * - User Agent / Credential Repository
        - 用户（实体）在此生成WeIdentity DID。为了便于使用，实体也可将自己的私钥、持有的Credential托管于此。

场景
^^^^


.. image:: images/scenario.png
   :alt: scenario.png

上图展示了五个WeIdentity生态下Credential在不同角色间流转的场景：

#. 身份证明机构作为Issuer向用户发行「实名认证Credential」，政府机构作为Verifier在办理公共事务时对其进行验证。
#. 学校作为Issuer向用户发行「学历证明Credential」，公司作为Verifier在对候选人进行背景调查时对其进行验证。
#. 出入境机构作为Issuer向用户发行「签证Credential」，海关作为Verifier在出入境时对其进行验证。
#. 公司作为Issuer向用户发行「收入证明Credential」，银行作为Verifier在发放贷款时对其进行验证。
#. 商户作为Issuer向用户发行「优惠券Credential」，商户自己作为Verifier在对优惠券核销时对其进行验证。

更多使用场景，可见： \ :ref:`使用场景文档 <use-cases>`。

Credential结构
^^^^^^^^^^^^^^

.. list-table::
   :header-rows: 1

   * - 属性
     - 说明
   * - @context
     - 用于描述Credential的字段信息等
   * - id
     - 本Credential的ID，按UUID生成
   * - issuer
     - Issuer的DID，遵从WeIdentity规范
   * - issued
     - issue日期
   * - claim
     - Claim的具体细节，数据结构由CPT定义，详见CPT介绍
   * - claim.primeNumberIdx
     - 素数在素数表中的index
   * - claim.type
     - Claim Protocol Type的ID，申请按序递增，例如中国内地驾照设置为CPT100
   * - revocation
     - 撤销相关实现，待补充
   * - proof
     - Issuer的签名列表，是一个数组，可由holder和issuer分别打上签名
   * - proof.type
     - 签名类型
   * - proof.created
     - 签名的创建时间
   * - proof.creator
     - 签名机构的WeIdentity DID
   * - proof.salt
     - 盐值，选择性披露时用于保证不被类似彩虹表的方式反向破解 hash 算法
   * - proof.signatureValue
     - 签名的具体value，对整个Credential结构中除去signature字段的其他字段做签名


区块链上的Credential结构
^^^^^^^^^^^^^^^^^^^^^^^^

.. list-table::
   :header-rows: 1

   * - 属性
     - 说明
   * - id
     - 同上
   * - type
     - 同上
   * - issued
     - 同上
   * - claimHash
     - Claim结构内容的hash
   * - revocation
     - 同上
   * - signature
     - 同上


.. code-block:: javascript

   {
     "claim": {
        "age": 1,
        "gender": "F",
        "id": "did:weid:101:0xd6f4d1215c52ee7e7975ac946a0e094040aa5eeb",
        "name": "1"
     },
     "context": "https://github.com/WeBankBlockchain/WeIdentity/blob/master/context/v1",
     "cptId": 2000006,
     "expirationDate": "2020-09-24T14:34:44Z",
     "id": "f894d33d-88c3-4122-afb6-87e21d2ae656",
     "issuanceDate": "2020-09-23T14:34:44Z",
     "issuer": "did:weid:101:0xd6f4d1215c52ee7e7975ac946a0e094040aa5eeb",
     "proof": {
        "created": "2020-09-23T14:34:44Z",
        "creator": "did:weid:101:0xd6f4d1215c52ee7e7975ac946a0e094040aa5eeb#keys-0",
        "salt": {
           "age": "IObiF",
           "gender": "GdzQs",
           "id": "dDIIt",
           "name": "PecuG"
        },
        "signatureValue": "/T21Vb5aw2dyB5kgh5LQmMHCQcx7MLJvFLf0g+EPkJVwoFH7+tPwokNBBHjQWn3BWLORDHJcbsUo+6OrRgTp8wA=",
        "type": "Secp256k1"
      },
     "type": [
        "VerifiableCredential",
        "original"
     ],
     "$from": "toJson"
    }

Claim Protocol Type（CPT）注册机制
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

不同的Issuer按业务场景需要，各自定义不同类型数据结构的Claim，所有的Claim结构都需要到CPT合约注册，以保证全网唯一。所有的CPT定义文件（JSON-LD格式）可以从CPT合约下载。


* CPT格式

.. list-table::
   :header-rows: 1

   * - 属性
     - 说明
   * - @context
     - 用于描述CPT等信息
   * - id
     - CPT的编号，要保证唯一
   * - cptType
     - Credential 的类型，目前有三种：lite1，original，zkp
   * - title
     - CPT的名字，例如“FISCO BCOS 高级工程师证书”，或者“驾照”
   * - description
     - CPT的描述信息
   * - properties
     - Claim 结构的说明
   * - created
     - 创建时间
   * - updated
     - 更新时间
   * - version
     - CPT的版本号
   * - publisher
     - CPT的发布者的WeIdentity DID
   * - proof
     - CPT的发布者的签名




* CPT例子

.. code-block:: javascript

   {
      "$context" : "http://json-schema.org/draft-04/schema#",
      "cptType" : "original",
      "description" : "学历证书说明",
      "title" : "学历证书",
      "type" : "object",
      "properties" : {
        "id" : {
          "description" : "学生证id",
          "type" : "string"
        },
        "name" : {
          "description" : "名字",
          "type" : "string"
        }
      }
    }


.. image:: images/cpt-er.png
   :alt: cpt-er.png

其中CPT为模板类，定义了Claim包含的数据字段及各字段属性要求。Claim为CPT的实例。Issuer将Claim进行签名，即可生成Credential。

Claim示例参考：
^^^^^^^^^^^^^^^

:ref:`Claim实例 <cpt-templates>`

Credential操作
^^^^^^^^^^^^^^

创建Credential
~~~~~~~~~~~~~~

任何实体都可以issue一个Credential。

验证Credential
~~~~~~~~~~~~~~

通过这个接口，一个Entity可以对一个Credential进行验证。

存储/提取Credential
~~~~~~~~~~~~~~~~~~~

Credential的holder可以转移这个Credential，或者提取完整的Credential私下存储。

撤销 Credential
~~~~~~~~~~~~~~~

Credential的Issuer可以撤销这个Credential。

选择性披露
^^^^^^^^^^

如何为Credential生成签名
~~~~~~~~~~~~~~~~~~~~~~~~

Issuer生成Credential签名的过程：


#. Claim中的每个字段计算生成一个对应的hash值。
#. 将Claim中的每个字段的hash值以某种形式拼接起来形成一个字符串Claim_Hash，然后跟Credential原有的其他字段组成一个新的用于计算hash的Credential结构。
#. 对这个包含Claim_Hash的Credential结构计算hash，得到Credential Hash。
#. 使用Private Key对这个Credential Hash进行签名，得到签名的值Signature。


.. image:: images/sign-credential.png
   :alt: sign-credential.png


如何验证选择性披露的Credential
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

用户选择需要披露的字段集合（可以是一个或者几个字段，这个例子中是Field_1），需要披露的字段提供原文，其他字段提供hash值。将包含这个Claim结构的Credential披露给Verifier。下面是Verifier验证的过程：


#. Verifier从Credential提取用户披露的Claim字段。
#. Verifier对用户披露的字段分别计算hash（这个例子中是Field_1,计算出Field_1_hash）,然后得到一个包含所有字段hash值的Claim结构。
#. 对这个Claim结构中的每个字段的hash值以某种形式拼接起来形成一个字符串Claim_Hash，然后跟Credential原有的其他字段组成一个新的用于计算hash的Credential结构。
#. 对这个包含Claim_Hash的Credential结构计算hash，得到Credential Hash。
#. 使用Credential的Signature和Issuer的public key进行decrypt，得到一个签名的计算值。
#. 比较Credential的Signature与签名的计算值，看是否相等，确认这个Credential的合法性。


.. image:: images/verify-credential.png
   :alt: verify-credential.png


Credential撤销
^^^^^^^^^^^^^^

撤销如何工作
~~~~~~~~~~~~

Credential撤销机制利用了下面两点：

#. 任意大于1的整数 a，如果 a 不是素数，则 a 可以表示为一系列素数的乘积，且这个表示是唯一的（不考虑顺序）。参见：\ `算数基本定理 <https://en.wikipedia.org/wiki/Fundamental_theorem_of_arithmetic>`_\
#. 目前没有一个有效率的算法，对两个足够大的素数乘积得到的半素数（\ `semiprime <https://en.wikipedia.org/wiki/Semiprime>`_\ ）进行整数分解。参见：\ `整数分解 <https://en.wikipedia.org/wiki/Integer_factorization>`_\

WeIdentity 会公开一个大素数的文件，每个素数会有一个 index，供所有 Issuer 使用。Issuer 发行一个 Credential 的时候，就随机从这个大素数文件中选择一个素数，这个素数的 index 会作为这个 Credential 的属性之一。并把以往所有发行的未撤销的 Credential 的素数相乘，得到一个大数 Accumulator（每个 Issuer 会维护自己的 Accumulator），并将这个 Accumulator 公开供所有接入方查询。

Issuer 如何撤销一个 Credential
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

用这个 Credential 对应的大素数去除 Issuer 自己的 Accumulator，将结果更新为新的 Accumulator。

Verifier 如何验证一个 Credential 有效（未被撤销）
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

用这个 Credential 对应的大素数去除 Credential 的 Issuer 公开的 Accumulator，如果能整除，则表明是 Credential 是有效的（未被撤销）。


.. image:: images/before-revocation.png
   :alt: before-revocation.png


.. image:: images/after-revocation.png
   :alt: after-revocation.png


5. Authority Issuer注册
------------------------

链上每个WeIdentity DID持有者（包括人，机构，或者物）都可以issue一个Claim，并打上自己的签名，但只有联盟链里面的权威机构才可以在公共合约注册成为Authority Issuer。每个注册为Authority Issuer的新成员需要联盟链内其他机构投票通过。

.. list-table::
   :header-rows: 1

   * - 字段
     - 说明
   * - version
     - version描述
   * - id
     - Authority Issuer的WeIdentity DID，符合WeIdentity规范的ID
   * - name
     - Authority Issuer机构的名字
   * - created
     - 创建日期
   * - updated
     - 更新日期
   * - publicKey
     - Authority Issuer公开的公钥列表
   * - validCrenRef
     - Authority Issuer公布的自己的当前所有未撤销的credential的primeNumber字段的累加乘积


6. Notification
----------------

通用字段如下，根据不同的type，notification的payload结构不同。

.. list-table::
   :header-rows: 1

   * - 字段
     - 说明
   * - type
     - 通知的类型：register, update
   * - weid
     - notification描述的实体的WeIdentity DID
   * - payload
     - Notification机制里，用以描述业务相关信息的字段


WeIdentity DID注册事件的通知
^^^^^^^^^^^^^^^^^^^^^^^^^^^^



当创建WeIdentity DID时，注册了该notification的机构会收到这个通知，表明有新的WeIdentity DID被注册。

.. list-table::
   :header-rows: 1

   * - 字段
     - 值
   * - type
     - register



* payload结构：

暂定为NULL。

WeIdentity DID属性修改的通知
^^^^^^^^^^^^^^^^^^^^^^^^^^^^


当WeIdentity DID的属性有更新时，注册了该notification的机构会收到这个通知，例如public key改变或者其他meta data值改变。

.. list-table::
   :header-rows: 1

   * - 字段
     - 值
   * - type
     - document_mod，表示WeIdentity Document有修改



* payload结构（此payload是一个数组，以支持多个属性变更的通知。）：

.. list-table::
   :header-rows: 1

   * - 字段
     - 说明
   * - operation
     - add, update 或者 remove
   * - field
     - 变动的字段的key
   * - original
     - 原值
   * - new
     - 新值


Notification携带业务数据
^^^^^^^^^^^^^^^^^^^^^^^^^


用于传输小段数据，例如机构间传递Credential的明文可以通过这类通知，payload由机构双方自行约定结构。

.. list-table::
   :header-rows: 1

   * - 字段
     - 值
   * - type
     - transportation


Notification订阅
^^^^^^^^^^^^^^^^^^^

支持对感兴趣的通知进行注册。依赖于实现。

7. 引用
-------


* `W3C DID Spec <https://www.w3.org/TR/did-core/>`_
* `W3C Verifiable Credentials <https://github.com/w3c/vc-data-model>`_
* `Linked Data Signatures 1.0 Draft <https://github.com/w3c-ccg/ld-signatures/>`_
* `RSA Signature Suite 2018 <https://github.com/w3c-ccg/lds-rsa2018/>`_
