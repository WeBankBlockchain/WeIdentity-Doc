
.. _weidentity-rest-api:

WeIdentity RestService API 说明文档
=====================================

基于私钥托管模式的RestService API
---------------------------------

1. 总体介绍
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

每个API的入参都是满足以下格式的json字符串：

.. code-block:: java

    {
        "functionArg": 随调用SDK方法而变的入参json字符串 {
            ...
        },
        "transactionArg": 交易参数json字符串 {
            "invokerWeId": 用于索引私钥的WeIdentity DID，服务器端会凭此找到所托管的私钥（非必须）
        }
        "functionName": 调用SDK方法名
        "v": API版本号
    }

参数说明：

* functionArg是随不同的SDK调用方法而变的，具体的参数可以查看SDK接口文档；后文会为每个所提及的接口给出对应的链接
* transactionArg仅包括一个变量invokerWeId，由传入方决定使用在服务器端托管的具体哪个WeIdentity DID所对应的私钥
    * 非必需，只有在那些需要使用不同身份进行写交易签名的方法（如CreateAuthorityIssuer等）才会需要；后文中详细说明
* functionName是调用的SDK方法名，用于决定具体调用WeIdentity Java SDK的什么功能
* v是调用的API方法版本

每个API的接口返回都是满足以下格式的json字符串：

.. code-block:: java

    {
        "respBody": 随调用SDK方法而变的输出值json字符串 {
        }
        "ErrorCode": 错误码
        "ErrorMessage": 错误信息，成功时为"success"
    }


其中具体的输出值result亦是随不同的SDK调用方法而变的。

在后文中，我们将会逐一说明目前所提供的功能及其使用方式。

2. 创建WeIdentity DID（无参创建方式）
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

调用接口：

.. list-table::
   :header-rows: 1
   :widths: 30 50

   * - 标题
     - 描述
   * - 接口名
     - weid/api/invoke
   * - Method
     - POST
   * - Content-Type
     - application/json

接口入参：

.. list-table::
   :header-rows: 1
   :widths: 30 60 20

   * - Key
     - Value
     - Required
   * - functionName
     - createWeId
     - Y
   * - functionArg
     - 
     - Y
   * - transactionArg
     - 
     - Y
   * - v
     - 版本号
     - Y

.. note::
  基于托管模式的RestService不允许通过传入公钥参数的方式创建WeID。这是因为，这样生成的WeID私钥不存在于RestService本地，因此无法在后续过程中被调用。如果您需要通过传入公钥方式创建WeID，请使用基于轻客户端方式的接口创建。

接口入参示例：

.. code-block:: java

    {
        "functionArg": {
        },
        "transactionArg": {
        },
        "functionName": "createWeId",
        "v": "1.0.0"
    }


接口返回: application/json


.. list-table::
   :header-rows: 1
   :widths: 30 50

   * - Key
     - Value
   * - ErrorCode
     - 错误码，0表示成功
   * - ErrorMessage
     - 错误信息
   * - respBody
     - WeIdentity DID

返回示例：

.. code-block:: java

    {
        "ErrorCode": 0,
        "ErrorMessage": "success",
        "respBody": "did:weid:0x12025448644151248e5c1115b23a3fe55f4158e4153"
    }

3. 获取WeIdentity DID Document
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

调用接口：

.. list-table::
   :header-rows: 1
   :widths: 30 50

   * - 标题
     - 描述
   * - 接口名
     - weid/api/invoke
   * - Method
     - POST
   * - Content-Type
     - application/json

接口入参：

.. list-table::
   :header-rows: 1
   :widths: 30 60 20

   * - Key
     - Value
     - Required
   * - functionName
     - getWeIdDocument
     - Y
   * - functionArg
     - 
     - Y
   * - functionArg.weId
     - WeIdentity DID，与 `SDK直接调用的方式入参 <https://weidentity.readthedocs.io/projects/javasdk/zh_CN/latest/docs/weidentity-java-sdk-doc.html#getweiddocment>`_ 一致，下同
     - Y
   * - transactionArg
     - 
     - N，传空
   * - v
     - 版本号
     - Y

接口入参示例：

.. code-block:: java

    {
        "functionArg": {
            "weId": "did:weid:0x12025448644151248e5c1115b23a3fe55f4158e4153"
        },
        "transactionArg": {
        },
        "functionName": "getWeIdDocument",
        "v": "1.0.0"
    }


接口返回: application/json

.. list-table::
   :header-rows: 1
   :widths: 30 50

   * - Key
     - Value
   * - ErrorCode
     - 错误码，0表示成功
   * - ErrorMessage
     - 错误信息
   * - respBody
     - WeIdentity DID Document

返回示例：

.. code-block:: java

    {
        "respBody": {
            "@context" : "https://w3id.org/did/v1",
            "id" : "did:weid:0x12025448644151248e5c1115b23a3fe55f4158e4153",
            "created" : 1553224394993,
            "updated" : 1553224394993,
            "publicKey" : [ ],
            "authentication" : [ ],
            "service" : [ ]
        },
        "ErrorCode": 0,
        "ErrorMessage": "success"
    }

4. 创建AuthorityIssuer
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

调用接口：

.. list-table::
   :header-rows: 1
   :widths: 30 50

   * - 标题
     - 描述
   * - 接口名
     - weid/api/invoke
   * - Method
     - POST
   * - Content-Type
     - application/json


接口入参：


.. list-table::
   :header-rows: 1
   :widths: 30 60 20

   * - Key
     - Value
     - Required
   * - functionName
     - registerAuthorityIssuer
     - Y
   * - functionArg
     - 
     - Y
   * - functionArg.weId
     - WeIdentity DID，与 `SDK直接调用的方式入参 <https://weidentity.readthedocs.io/projects/javasdk/zh_CN/latest/docs/weidentity-java-sdk-doc.html#registercpt>`_ 一致，下同
     - Y
   * - functionArg.name
     - 机构名
     - Y
   * - transactionArg
     - 
     - Y
   * - transactionArg.invokerWeId
     - 用于索引私钥的WeIdentity DID，服务器端会凭此找到所托管的私钥。注意：如果在这里填入了预先定义在application.properties里的暗语，则可确保有足够的权限。
     - Y
   * - v
     - 版本号
     - Y

接口调用示例：

.. code-block:: java

    {
        "functionArg": {
            "weid": "did:weid:0x1Ae5b88d37327830307ab8da0ec5D8E8692A35D3",
            "name": "Sample College"
        },
        "transactionArg": {
            "invokerWeId": "did:weid:0x12025448644151248e5c1115b23a3fe55f4158e4153"
        },
        "functionName": "registerAuthorityIssuer",
        "v": "1.0.0"
    }


接口返回: application/json


.. list-table::
   :header-rows: 1
   :widths: 30 50

   * - Key
     - Value
   * - ErrorCode
     - 错误码，0表示成功
   * - ErrorMessage
     - 错误信息
   * - respBody
     - True/False

返回示例：

.. code-block:: java

    {
        "ErrorCode": 0,
        "ErrorMessage": "success",
        "respBody": True
    }


5. 查询AuthorityIssuer
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

调用接口：

.. list-table::
   :header-rows: 1
   :widths: 30 50

   * - 标题
     - 描述
   * - 接口名
     - weid/api/invoke
   * - Method
     - POST
   * - Content-Type
     - application/json

接口入参：

.. list-table::
   :header-rows: 1
   :widths: 30 60 20

   * - Key
     - Value
     - Required
   * - functionName
     - queryAuthorityIssuer
     - Y
   * - functionArg
     - 
     - Y
   * - functionArg.weId
     - WeIdentity DID，与 `SDK直接调用的方式入参 <https://weidentity.readthedocs.io/projects/javasdk/zh_CN/latest/docs/weidentity-java-sdk-doc.html#queryauthorityissuer>`_ 一致，下同
     - Y
   * - transactionArg
     - 
     - N，传空
   * - v
     - 版本号
     - Y

接口入参示例：

.. code-block:: java

    {
        "functionArg": {
            "weId": "did:weid:0x1ae5b88d37327830307ab8da0ec5d8e8692a35d3"
        },
        "transactionArg": {
        },
        "functionName": "queryAuthorityIssuer",
        "v": "1.0.0"
    }

接口返回: application/json

.. list-table::
   :header-rows: 1
   :widths: 30 50

   * - Key
     - Value
   * - ErrorCode
     - 错误码，0表示成功
   * - ErrorMessage
     - 错误信息
   * - respBody
     - 完整的Authority Issuer信息


.. code-block:: java

    {
        "respBody": {
            "accValue": ,
            "created": 16845611984115,
            "name": "Sample College",
            "weid": "did:weid:0x1ae5b88d37327830307ab8da0ec5d8e8692a35d3"
        }
        "ErrorCode": 0
        "ErrorMessage": "success"
    }

6. 创建CPT
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^


调用接口：

.. list-table::
   :header-rows: 1
   :widths: 30 50

   * - 标题
     - 描述
   * - 接口名
     - weid/api/invoke
   * - Method
     - POST
   * - Content-Type
     - application/json


接口入参: 

.. list-table::
   :header-rows: 1
   :widths: 30 60 20

   * - Key
     - Value
     - Required
   * - functionName
     - registerCpt
     - Y
   * - functionArg
     - 
     - Y
   * - functionArg.cptJsonSchema
     - CPT Json Schema，与 `SDK直接调用的方式入参 <https://weidentity.readthedocs.io/projects/javasdk/zh_CN/latest/docs/weidentity-java-sdk-doc.html#registercpt>`_ 一致，下同
     - Y
   * - functionArg.weId
     - CPT创建者
     - Y
   * - transactionArg
     - 
     - Y
   * - transactionArg.invokerWeId
     - 用于索引私钥的WeIdentity DID，服务器端会凭此找到所托管的私钥
     - Y
   * - v
     - 版本号
     - Y

.. code-block:: text

    CPT Json Schema是什么？应该满足什么格式？

    答：Json Schema是一种用来定义Json字符串格式的Json字符串，它定义了CPT应包括的字段、属性及规则。
    WeIdentity可以接受 http://json-schema.org/draft-04/schema# 所定义第四版及之前版本作为入参。


接口入参示例：

.. code-block:: java

      {
        "functionArg": {
            "weId": "did:weid:0x1ae5b88d37327830307ab8da0ec5d8e8692a35d3",
            "cptJsonSchema":{
                "title": "cpt",
                "description": "this is cpt",
                "properties": {
                    "name": {
                        "type": "string",
                        "description": "the name of certificate owner"
                    },
                    "gender": {
                        "enum": [
                            "F",
                            "M"
                        ],
                        "type": "string",
                        "description": "the gender of certificate owner"
                    },
                    "age": {
                        "type": "number",
                        "description": "the age of certificate owner"
                    }
                },
                "required": [
                    "name",
                    "age"
                ]
            }
        },
        "transactionArg": {
            "invokerWeId": "did:weid:0x1ae5b88d37327830307ab8da0ec5d8e8692a35d3"
        }，
        "functionName": "registerCpt"，
        "v": "1.0.0"
      }


接口返回: application/json


.. list-table::
   :header-rows: 1
   :widths: 30 50

   * - Key
     - Value
   * - ErrorCode
     - 错误码，0表示成功
   * - ErrorMessage
     - 错误信息
   * - respBody
     - cptBaseInfo

返回示例：

.. code-block:: java

    {
        "respBody": {
            "cptId": 2000001,
            "cptVersion": 1
        },
        "ErrorCode": 0,
        "ErrorMessage": "success"
    }

7. 查询CPT
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

调用接口：

.. list-table::
   :header-rows: 1
   :widths: 30 50

   * - 标题
     - 描述
   * - 接口名
     - weid/api/invoke
   * - Method
     - POST
   * - Content-Type
     - application/json

接口入参：

.. list-table::
   :header-rows: 1
   :widths: 30 60 20

   * - Key
     - Value
     - Required
   * - functionName
     - queryCpt
     - Y
   * - functionArg
     - 
     - Y
   * - functionArg.cptId
     - CPT ID，与 `SDK直接调用的方式入参 <https://weidentity.readthedocs.io/projects/javasdk/zh_CN/latest/docs/weidentity-java-sdk-doc.html#querycpt>`_ 一致。
     - Y
   * - transactionArg
     - 
     - N，传空
   * - v
     - 版本号
     - Y

接口入参示例：

.. code-block:: java

    {
        "functionArg": {
            "cptId": 10,
        },
        "transactionArg": {
        },
        "functionName": "queryCpt",
        "v": "1.0.0"
    }

接口返回: application/json

.. list-table::
   :header-rows: 1
   :widths: 30 50

   * - Key
     - Value
   * - ErrorCode
     - 错误码，0表示成功
   * - ErrorMessage
     - 错误信息
   * - respBody
     - 完整的CPT信息

接口返回示例：

.. code-block:: java

    {
        "respBody": {
            "cptBaseInfo" : {
                "cptId" : 10,
                "cptVersion" : 1
            },
            "cptId" : 10,
            "cptJsonSchema" : {
                "$schema" : "http://json-schema.org/draft-04/schema#",
                "title" : "a CPT schema",
                "type" : "object"
            },
            "cptPublisher" : "did:weid:0x104a58c272e8ebde0c29083552ebe78581322908",
            "cptSignature" : "HJPbDmoi39xgZBGi/aj1zB6VQL5QLyt4qTV6GOvQwzfgUJEZTazKZXe1dRg5aCt8Q44GwNF2k+l1rfhpY1hc/ls=",
            "cptVersion" : 1,
            "created" : 1553503354555,
            "metaData" : {
                "cptPublisher" : "did:weid:0x104a58c272e8ebde0c29083552ebe78581322908",
                "cptSignature" : "HJPbDmoi39xgZBGi/aj1zB6VQL5QLyt4qTV6GOvQwzfgUJEZTazKZXe1dRg5aCt8Q44GwNF2k+l1rfhpY1hc/ls=",
                "created" : 1553503354555,
                "updated" : 0
            },
            "updated" : 0
        },
        "ErrorCode": 0,
        "ErrorMessage": "success"
    }

8. 创建Credential
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

调用接口：

.. list-table::
   :header-rows: 1
   :widths: 30 50

   * - 标题
     - 描述
   * - 接口名
     - weid/api/invoke
   * - Method
     - POST
   * - Content-Type
     - application/json

接口入参：

.. list-table::
   :header-rows: 1
   :widths: 30 60 20

   * - Key
     - Value
     - Required
   * - functionName
     - createCredential
     - Y
   * - functionArg
     - 
     - Y
   * - functionArg.claim
     - claim Json结构体，与 `SDK直接调用的方式入参 <https://weidentity.readthedocs.io/projects/javasdk/zh_CN/latest/docs/weidentity-java-sdk-doc.html#createcredential>`_ 一致，下同     
     - Y
   * - functionArg.cptId
     - CPT ID
     - Y
   * - functionArg.issuer
     - issuer WeIdentity DID
     - Y
   * - functionArg.expirationDate
     - 过期时间（使用UTC格式）
     - Y
   * - transactionArg
     - 
     - Y
   * - transactionArg.invokerWeId
     - 用于索引私钥的WeIdentity DID，服务器端会凭此找到所托管的私钥
     - Y
   * - v
     - 版本号
     - Y

接口入参：Json，以signature代替私钥

.. code-block:: java

    {
        "functionArg": {
            "cptId": 10,
            "issuer": "did:weid:0x12025448644151248e5c1115b23a3fe55f4158e4153",
            "expirationDate": "2019-04-18T21:12:33Z",
            "claim": {
                "name": "zhang san",
                "gender": "F",
                "age": 18
            },
        },
        "transactionArg": {
            "invokerWeId": "did:weid:0x12025448644151248e5c1115b23a3fe55f4158e4153"
        },
        "functionName": "createCredential",
        "v": "1.0.0"
    }

接口返回: application/json

.. list-table::
   :header-rows: 1
   :widths: 30 50

   * - Key
     - Value
   * - ErrorCode
     - 错误码，0表示成功
   * - ErrorMessage
     - 错误信息
   * - respBody
     - 完整的Credential信息


接口返回示例:

.. code-block:: java

    {
      "respBody": {
          "@context": "https://github.com/WeBankFinTech/WeIdentity/blob/master/context/v1",
          "claim": {
              "content": "b1016358-cf72-42be-9f4b-a18fca610fca",
              "receiver": "did:weid:101:0x7ed16eca3b0737227bc986dd0f2851f644cf4754",
              "weid": "did:weid:101:0xfd28ad212a2de77fee518b4914b8579a40c601fa"
          },
          "cptId": 2000156,
          "expirationDate": "2100-04-18T21:12:33Z",
          "id": "da6fbdbb-b5fa-4fbe-8b0c-8659da2d181b",
          "issuanceDate": "2020-02-06T22:24:00Z",
          "issuer": "did:weid:101:0xfd28ad212a2de77fee518b4914b8579a40c601fa",
          "proof": {
              "created": "1580999040000",
              "creator": "did:weid:101:0xfd28ad212a2de77fee518b4914b8579a40c601fa",
              "signature": "G0XzzLY+MqUAo3xXkS3lxVsgFLnTtvdXM24p+G5hSNNMSIa5vAXYXXKl+Y79CO2ho5DIGPPvSs2hvAixmfIJGbw=",
              "type": "Secp256k1"
          }
      },
      "errorCode": 0,
      "errorMessage": "success"
    }


9. 验证Credential
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

调用接口：

.. list-table::
   :header-rows: 1
   :widths: 30 50

   * - 标题
     - 描述
   * - 接口名
     - weid/api/invoke
   * - Method
     - POST
   * - Content-Type
     - application/json


接口入参：

.. list-table::
   :header-rows: 1
   :widths: 30 60 20

   * - Key
     - Value
     - Required
   * - functionName
     - verifyCredential
     - Y
   * - functionArg
     - 
     - Y
   * - functionArg.claim
     - claim Json 结构体，与 `SDK直接调用的方式入参 <https://weidentity.readthedocs.io/projects/javasdk/zh_CN/latest/docs/weidentity-java-sdk-doc.html#verify>`_ 一致，下同
     - Y
   * - functionArg.cptId
     - CPT ID
     - Y
   * - functionArg.context
     - context值
     - Y
   * - functionArg.uuid
     - Credential的UUID
     - Y
   * - functionArg.issuer
     - issuer WeIdentity DID
     - Y
   * - functionArg.issuranceDate
     - 颁发时间
     - Y
   * - functionArg.expirationDate
     - 过期时间
     - Y
   * - functionArg.proof
     - Credential签名结构体
     - Y
   * - transactionArg
     - 
     - N，传空
   * - v
     - 版本号
     - Y

接口入参：

.. code-block:: java

    {
        "functionArg": {
          "@context": "https://github.com/WeBankFinTech/WeIdentity/blob/master/context/v1",
          "claim": {
              "content": "b1016358-cf72-42be-9f4b-a18fca610fca",
              "receiver": "did:weid:101:0x7ed16eca3b0737227bc986dd0f2851f644cf4754",
              "weid": "did:weid:101:0xfd28ad212a2de77fee518b4914b8579a40c601fa"
          },
          "cptId": 2000156,
          "expirationDate": "2100-04-18T21:12:33Z",
          "id": "da6fbdbb-b5fa-4fbe-8b0c-8659da2d181b",
          "issuanceDate": "2020-02-06T22:24:00Z",
          "issuer": "did:weid:101:0xfd28ad212a2de77fee518b4914b8579a40c601fa",
          "proof": {
              "created": "1580999040000",
              "creator": "did:weid:101:0xfd28ad212a2de77fee518b4914b8579a40c601fa",
              "signature": "G0XzzLY+MqUAo3xXkS3lxVsgFLnTtvdXM24p+G5hSNNMSIa5vAXYXXKl+Y79CO2ho5DIGPPvSs2hvAixmfIJGbw=",
              "type": "Secp256k1"
          }
        },
        "transactionArg": {
        },
        "functionName": "verifyCredential"
        "v": "1.0.0"
    }


接口返回: application/json

.. list-table::
   :header-rows: 1
   :widths: 30 50

   * - Key
     - Value
   * - ErrorCode
     - 错误码，0表示成功
   * - ErrorMessage
     - 错误信息
   * - respBody
     - True/False


接口返回：

.. code-block:: java

    {
        "respBody": true,
        "ErrorCode": 0,
        "ErrorMessage": "success"
    }



10. 创建CredentialPojo
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

调用接口：

.. list-table::
   :header-rows: 1
   :widths: 30 50

   * - 标题
     - 描述
   * - 接口名
     - weid/api/invoke
   * - Method
     - POST
   * - Content-Type
     - application/json

接口入参：

.. list-table::
   :header-rows: 1
   :widths: 30 60 20

   * - Key
     - Value
     - Required
   * - functionName
     - createCredentialPojo
     - Y
   * - functionArg
     - 
     - Y
   * - functionArg.claim
     - claim Json结构体，与 `SDK直接调用的方式入参 <https://weidentity.readthedocs.io/projects/javasdk/zh_CN/latest/docs/weidentity-java-sdk-doc.html#createcredential>`_ 一致，下同     
     - Y
   * - functionArg.cptId
     - CPT ID
     - Y
   * - functionArg.issuer
     - issuer WeIdentity DID
     - Y
   * - functionArg.expirationDate
     - 过期时间（使用UTC格式）
     - Y
   * - transactionArg
     - 
     - Y
   * - transactionArg.invokerWeId
     - 用于索引私钥的WeIdentity DID，服务器端会凭此找到所托管的私钥
     - Y
   * - v
     - 版本号
     - Y

接口入参：Json，以signature代替私钥

.. code-block:: java

    {
        "functionArg": {
            "cptId": 10,
            "issuer": "did:weid:0x12025448644151248e5c1115b23a3fe55f4158e4153",
            "expirationDate": "2019-04-18T21:12:33Z",
            "claim": {
                "name": "zhang san",
                "gender": "F",
                "age": 18
            },
        },
        "transactionArg": {
            "invokerWeId": "did:weid:0x12025448644151248e5c1115b23a3fe55f4158e4153"
        },
        "functionName": "createCredentialPojo",
        "v": "1.0.0"
    }

接口返回: application/json

.. list-table::
   :header-rows: 1
   :widths: 30 50

   * - Key
     - Value
   * - ErrorCode
     - 错误码，0表示成功
   * - ErrorMessage
     - 错误信息
   * - respBody
     - 完整的CredentialPojo信息


接口返回示例:

.. code-block:: java

    {
      "respBody": {
          "cptId": 2000156,
          "issuanceDate": 1580996777,
          "context": "https://github.com/WeBankFinTech/WeIdentity/blob/master/context/v1",
          "claim": {
              "content": "b1016358-cf72-42be-9f4b-a18fca610fca",
              "receiver": "did:weid:101:0x7ed16eca3b0737227bc986dd0f2851f644cf4754",
              "weid": "did:weid:101:0xfd28ad212a2de77fee518b4914b8579a40c601fa"
          },
          "id": "21d10ab1-75fe-4733-9f1d-f0bad71b5922",
          "proof": {
              "created": 1580996777,
              "creator": "did:weid:101:0xfd28ad212a2de77fee518b4914b8579a40c601fa#keys-0",
              "salt": {
                  "content": "ncZ5F",
                  "receiver": "L0c40",
                  "weid": "I4aop"
              },
              "signatureValue": "HEugP13uDVBg2G0kmmwbTkQXobsrWNqtGQJW6BoHU2Q2VQpwVhK382dArRMFN6BDq7ogozYBRC15QR8ueX5G3t8=",
              "type": "Secp256k1"
          },
          "type": [
              "VerifiableCredential",
              "hashTree"
          ],
          "issuer": "did:weid:101:0xfd28ad212a2de77fee518b4914b8579a40c601fa",
          "expirationDate": 4111737153
      },
      "errorCode": 0,
      "errorMessage": "success"
    }


11. 验证CredentialPojo
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

调用接口：

.. list-table::
   :header-rows: 1
   :widths: 30 50

   * - 标题
     - 描述
   * - 接口名
     - weid/api/invoke
   * - Method
     - POST
   * - Content-Type
     - application/json


接口入参：

.. list-table::
   :header-rows: 1
   :widths: 30 60 20

   * - Key
     - Value
     - Required
   * - functionName
     - verifyCredentialPojo
     - Y
   * - functionArg
     - 
     - Y
   * - functionArg.claim
     - claim Json 结构体，与 `SDK直接调用的方式入参 <https://weidentity.readthedocs.io/projects/javasdk/zh_CN/latest/docs/weidentity-java-sdk-doc.html#verify>`_ 一致，下同
     - Y
   * - functionArg.cptId
     - CPT ID
     - Y
   * - functionArg.context
     - context值
     - Y
   * - functionArg.uuid
     - CredentialPojo的UUID
     - Y
   * - functionArg.issuer
     - issuer WeIdentity DID
     - Y
   * - functionArg.issuranceDate
     - 颁发时间
     - Y
   * - functionArg.expirationDate
     - 过期时间
     - Y
   * - functionArg.proof
     - Credential签名值
     - Y
   * - transactionArg
     - 
     - N，传空
   * - v
     - 版本号
     - Y

接口入参：

.. code-block:: java

    {
        "functionArg": {
          "cptId": 2000156,
          "issuanceDate": 1580996777,
          "context": "https://github.com/WeBankFinTech/WeIdentity/blob/master/context/v1",
          "claim": {
              "content": "b1016358-cf72-42be-9f4b-a18fca610fca",
              "receiver": "did:weid:101:0x7ed16eca3b0737227bc986dd0f2851f644cf4754",
              "weid": "did:weid:101:0xfd28ad212a2de77fee518b4914b8579a40c601fa"
          },
          "id": "21d10ab1-75fe-4733-9f1d-f0bad71b5922",
          "proof": {
              "created": 1580996777,
              "creator": "did:weid:101:0xfd28ad212a2de77fee518b4914b8579a40c601fa#keys-0",
              "salt": {
                  "content": "ncZ5F",
                  "receiver": "L0c40",
                  "weid": "I4aop"
              },
              "signatureValue": "HEugP13uDVBg2G0kmmwbTkQXobsrWNqtGQJW6BoHU2Q2VQpwVhK382dArRMFN6BDq7ogozYBRC15QR8ueX5G3t8=",
              "type": "Secp256k1"
          },
          "type": [
              "VerifiableCredential",
              "hashTree"
          ],
          "issuer": "did:weid:101:0xfd28ad212a2de77fee518b4914b8579a40c601fa",
          "expirationDate": 4111737153
        },
        "transactionArg": {
        },
        "functionName": "verifyCredentialPojo"
        "v": "1.0.0"
    }


接口返回: application/json

.. list-table::
   :header-rows: 1
   :widths: 30 50

   * - Key
     - Value
   * - ErrorCode
     - 错误码，0表示成功
   * - ErrorMessage
     - 错误信息
   * - respBody
     - True/False


接口返回：

.. code-block:: java

    {
        "respBody": true,
        "ErrorCode": 0,
        "ErrorMessage": "success"
    }

基于轻客户端模式的RestService API
------------------------------------

1. 总体介绍
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

基于轻客户端模式的API的入参形式与基于私钥托管模式的API相同，也是满足以下格式的json字符串。
最大的区别在于，流程包括两次交互，第一次是轻客户端提供接口参数发给RestService服务端，后者进行组装、编码区块链原始交易串并返回；第二次是轻客户端在本地使用自己的私钥，对原始交易串进行符合ECDSA的sha3签名，发给RestService服务端，后者打包并直接执行交易。

- 第一次交互

调用接口：

.. list-table::
   :header-rows: 1
   :widths: 30 50

   * - 标题
     - 描述
   * - 接口名
     - weid/api/encode
   * - Method
     - POST
   * - Content-Type
     - application/json

接口入参（Body）：

.. code-block:: java

    {
        "functionArg": 随调用SDK方法而变的入参json字符串 {
            ...
        },
        "transactionArg": 交易参数json字符串 {
            "nonce": 用于防止重放攻击的交易随机数
        }
        "functionName": 调用SDK方法名
        "v": API版本号
    }

参数说明：

* functionArg是随不同的SDK调用方法而变的，具体的参数可以查看SDK接口文档；后文会为每个所提及的接口给出对应的链接
* transactionArg仅包括一个变量nonce，用于防止重放攻击的交易随机数，您可以使用RestService jar的getNonce()，或其他类似方法生成此随机数。
    * 必需，且必须妥善保存，这个nonce在第二次交互中还会用到
* functionName是调用的SDK方法名，用于决定具体调用WeIdentity Java SDK的什么功能
* v是调用的API方法版本

第一次交互的接口返回是以下格式的json字符串：

.. code-block:: java

    {
        "respBody": {
            "encodedTransaction": 基于Base64编码的原始交易串信息
            "data": 交易特征值
        }
        "ErrorCode": 错误码
        "ErrorMessage": 错误信息，成功时为"success"
    }

返回结构体包含encodedTransaction和data两项。
调用者随后需要使用自己的私钥对encodeTransaction进行交易签名，然后使用Base64对其进行编码，和data、nonce一起待用，
进行第二次交互。

使用ECDSA私钥进行签名和Base64编码的范例代码见下：

.. code-block:: java

    // 依赖web3sdk 2.2.2和weid-java-sdk 1.5
    byte[] encodedTransaction = DataToolUtils
            .base64Decode("<encodedTransaction的值>".getBytes());
        SignatureData clientSignedData = Sign.getSignInterface().signMessage(encodedTransactionClient, ecKeyPair);
        String base64SignedMsg = new String(
            DataToolUtils.base64Encode(TransactionEncoderUtilV2.simpleSignatureSerialization(clientSignedData)));

- 第二次交互

调用接口：

.. list-table::
   :header-rows: 1
   :widths: 30 50

   * - 标题
     - 描述
   * - 接口名
     - weid/api/transact
   * - Method
     - POST
   * - Content-Type
     - application/json

接口入参：

.. code-block:: java

    {
        "functionArg": 空 {
        },
        "transactionArg": 交易参数json字符串 {
            "nonce": 用于防止重放攻击的交易随机数
            "data": 交易特征值，需和第一步中返回值一致
            "signedMessage": 基于Base64编码的、使用私钥签名之后的encodedTransaction签名值
        }
        "functionName": 调用SDK方法名
        "v": API版本号
    }

这一步的目的是将已签名交易的参数发给RestService，并由后者将交易打包上链。参数说明：

* functionArg此时为空
* transactionArg包括第一次交互起始阶段生成的nonce、第一次交互收到的data，以及最重要的，使用调用者本地私钥签名并通过Base64编码的encodedTransaction的签名值
* functionName是调用的SDK方法名，用于决定具体调用WeIdentity Java SDK的什么功能
* v是调用的API方法版本

第二次交互的接口返回是以下格式的json字符串：

.. code-block:: java

    {
        "respBody": 随调用SDK方法而变的输出值json字符串 {
        }
        "ErrorCode": 错误码
        "ErrorMessage": 错误信息，成功时为"success"
    }


其中具体的输出值result亦是随不同的SDK调用方法而变的。
可以看到，基于轻客户端的交易方式，本质上，是因为签名操作必须在本地完成，因此将原始交易串分成了两次交互完成。
基于轻客户端的每个API的入参，也仅仅在第一次交互中不同。因此，在下文的介绍中，我们会忽略第二次交互的入参，只提供第一次交互的入参和第二次交互的返回值。

2. 创建WeIdentity DID（有参创建方式）
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

第一次交互，POST /weid/api/encode 的接口入参：

.. list-table::
   :header-rows: 1
   :widths: 30 60 20

   * - Key
     - Value
     - Required
   * - functionName
     - createWeId
     - Y
   * - functionArg
     - 
     - Y
   * - functionArg.publicKey
     - ECDSA公钥，需要为10进制的整型数字，以字符串形式传入
     - Y
   * - transactionArg
     - 
     - Y
   * - transactionArg.nonce
     - 交易随机数
     - Y
   * - v
     - 版本号
     - Y

第一次交互，POST /weid/api/encode 接口入参示例：

.. code-block:: java

    {
        "functionArg": {
            "publicKey": "712679236821355231513532168231727831978932132185632517152735621683128"
        },
        "transactionArg": {
            "nonce": "1474800601011307365506121304576347479508653499989424346408343855615822146039"
        },
        "functionName": "createWeId",
        "v": "1.0.0"
    }

第二次交互，POST /weid/api/transact 接口入参示例：

.. code-block:: java

    {
        "functionArg": {},
        "transactionArg": {
            "nonce": "1474800601011307365506121304576347479508653499989424346408343855615822146039",
            "data": "809812638256c1235b1231000e000000001231287bacf213c",
            "signedMessage": "HEugP13uDVBg2G0kmmwbTkQXobsrWNqtGQJW6BoHU2Q2VQpwVhK382dArRMFN6BDq7ogozYBRC15QR8ueX5G3t8=" 
        },
        "functionName": "createWeId",
        "v": "1.0.0"
    }

第二次交互的接口返回：

.. list-table::
   :header-rows: 1
   :widths: 30 50

   * - Key
     - Value
   * - ErrorCode
     - 错误码，0表示成功
   * - ErrorMessage
     - 错误信息
   * - respBody
     - WeIdentity DID

返回示例：

.. code-block:: java

    {
        "ErrorCode": 0,
        "ErrorMessage": "success",
        "respBody": "did:weid:0x12025448644151248e5c1115b23a3fe55f4158e4153"
    }

3. 注册Authority Issuer
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

第一次交互，POST /weid/api/encode 的接口入参：

.. list-table::
   :header-rows: 1
   :widths: 30 60 20

   * - Key
     - Value
     - Required
   * - functionName
     - registerAuthorityIssuer
     - Y
   * - functionArg
     - 
     - Y
   * - functionArg.name
     - 机构名
     - Y
   * - functionArg.weId
     - WeIdentity DID，与 `SDK直接调用的方式入参 <https://weidentity.readthedocs.io/projects/javasdk/zh_CN/latest/docs/weidentity-java-sdk-doc.html#registercpt>`_ 一致，下同
     - Y
   * - transactionArg
     - 
     - Y
   * - transactionArg.nonce
     - 交易随机数
     - Y
   * - v
     - 版本号
     - Y

第一次交互，POST /weid/api/encode 接口入参示例：

.. code-block:: java

    {
        "functionArg": {
            "name": "BV-College",
            "weId": "did:weid:0x12025448644151248e5c1115b23a3fe55f4158e4153"
        },
        "transactionArg": {
            "nonce": "1474800601011307365506121304576347479508653499989424346408343855615822146039"
        },
        "functionName": "registerAuthorityIssuer",
        "v": "1.0.0"
    }

第二次交互，POST /weid/api/transact 接口入参示例：

.. code-block:: java

    {
        "functionArg": {},
        "transactionArg": {
            "nonce": "1474800601011307365506121304576347479508653499989424346408343855615822146039",
            "data": "809812638256c1235b1231000e000000001231287bacf213c",
            "signedMessage": "HEugP13uDVBg2G0kmmwbTkQXobsrWNqtGQJW6BoHU2Q2VQpwVhK382dArRMFN6BDq7ogozYBRC15QR8ueX5G3t8=" 
        },
        "functionName": "registerAuthorityIssuer",
        "v": "1.0.0"
    }

第二次交互的接口返回：

.. list-table::
   :header-rows: 1
   :widths: 30 50

   * - Key
     - Value
   * - ErrorCode
     - 错误码，0表示成功
   * - ErrorMessage
     - 错误信息
   * - respBody
     - Authority Isser信息

返回示例：

.. code-block:: java

    {
        "ErrorCode": 0,
        "ErrorMessage": "success",
        "respBody": {
            "accValue": ,
            "created": "1581420650",
            "name": "BV-College",
            "weId": "did:weid:0x12025448644151248e5c1115b23a3fe55f4158e4153"
        }
    }

4. 创建CPT
^^^^^^^^^^^^^

第一次交互，POST /weid/api/encode 的接口入参：

.. list-table::
   :header-rows: 1
   :widths: 30 60 20

   * - Key
     - Value
     - Required
   * - functionName
     - registerCpt
     - Y
   * - functionArg
     - 
     - Y
   * - functionArg.cptJsonSchema
     - CPT Json Schema，与 `SDK直接调用的方式入参 <https://weidentity.readthedocs.io/projects/javasdk/zh_CN/latest/docs/weidentity-java-sdk-doc.html#registercpt>`_ 一致，下同
     - Y
   * - functionArg.weId
     - CPT创建者
     - Y
   * - functionArg.cptSignature
     - 创建者使用自己的私钥对cptJsonSchema的签名（和私钥托管方式不同，本方式特有）
     - Y
   * - transactionArg
     - 
     - Y
   * - transactionArg.nonce
     - 交易随机数
     - Y
   * - v
     - 版本号
     - Y

第一次交互，POST /weid/api/encode 接口入参示例：

.. code-block:: java

    {
        "functionArg": {
            "weId": "did:weid:0x1ae5b88d37327830307ab8da0ec5d8e8692a35d3",
            "cptJsonSchema": {
                "title": "cpt",
                "description": "this is cpt",
                "properties": {
                    "name": {
                        "type": "string",
                        "description": "the name of certificate owner"
                    },
                    "gender": {
                        "enum": [
                            "F",
                            "M"
                        ],
                        "type": "string",
                        "description": "the gender of certificate owner"
                    },
                    "age": {
                        "type": "number",
                        "description": "the age of certificate owner"
                    }
                },
                "required": [
                    "name",
                    "age"
                ]
            }
            "cptSignature": "BaUeP13uDVBg2G0kmmwbTkQXobsrWNqtGQJW6BoHU2Q2VQpwVhK382dArRMFN6BDq7ogozYBRC15QR8ueX5G3t8=" 
        },
        "transactionArg": {
            "nonce": "1474800601011307365506121304576347479508653499989424346408343855615822146039"
        },
        "functionName": "registerAuthorityIssuer",
        "v": "1.0.0"
    }

第二次交互，POST /weid/api/transact 接口入参示例：

.. code-block:: java

    {
        "functionArg": {},
        "transactionArg": {
            "nonce": "1474800601011307365506121304576347479508653499989424346408343855615822146039",
            "data": "809812638256c1235b1231000e000000001231287bacf213c",
            "signedMessage": "HEugP13uDVBg2G0kmmwbTkQXobsrWNqtGQJW6BoHU2Q2VQpwVhK382dArRMFN6BDq7ogozYBRC15QR8ueX5G3t8=" 
        },
        "functionName": "registerAuthorityIssuer",
        "v": "1.0.0"
    }

第二次交互的接口返回：

.. list-table::
   :header-rows: 1
   :widths: 30 50

   * - Key
     - Value
   * - ErrorCode
     - 错误码，0表示成功
   * - ErrorMessage
     - 错误信息
   * - respBody
     - Authority Isser信息

返回示例：

.. code-block:: java

    {
        "ErrorCode": 0,
        "ErrorMessage": "success",
        "respBody": {
            "cptId": 2000001,
            "cptVersion": 1
        }
    }

5. 创建CredentialPojo
^^^^^^^^^^^^^^^^^^^^^^^

创建CredentialPojo不需要进行区块链交互，因此，只需要进行一次交互POST /weid/api/encode，然后对返回的结果进行签名即可：

POST /weid/api/encode 接口入参

.. list-table::
   :header-rows: 1
   :widths: 30 60 20

   * - Key
     - Value
     - Required
   * - functionName
     - createCredentialPojo
     - Y
   * - functionArg
     - 
     - Y
   * - functionArg.claim
     - claim Json结构体，与 `SDK直接调用的方式入参 <https://weidentity.readthedocs.io/projects/javasdk/zh_CN/latest/docs/weidentity-java-sdk-doc.html#createcredential>`_ 一致，下同     
     - Y
   * - functionArg.cptId
     - CPT ID
     - Y
   * - functionArg.issuer
     - issuer WeIdentity DID
     - Y
   * - functionArg.expirationDate
     - 过期时间（使用UTC格式）
     - Y
   * - transactionArg
     - 为空
     - Y
   * - v
     - 版本号
     - Y

接口入参：Json，以signature代替私钥

.. code-block:: java

    {
        "functionArg": {
            "cptId": 10,
            "issuer": "did:weid:0x12025448644151248e5c1115b23a3fe55f4158e4153",
            "expirationDate": "2019-04-18T21:12:33Z",
            "claim": {
                "name": "zhang san",
                "gender": "F",
                "age": 18
            },
        },
        "transactionArg": {
        },
        "functionName": "createCredentialPojo",
        "v": "1.0.0"
    }

接口返回: application/json

.. list-table::
   :header-rows: 1
   :widths: 30 50

   * - Key
     - Value
   * - ErrorCode
     - 错误码，0表示成功
   * - ErrorMessage
     - 错误信息
   * - respBody
     - 完整的CredentialPojo信息


接口返回示例:

.. code-block:: java

    {
      "respBody": {
          "cptId": 2000156,
          "issuanceDate": 1580996777,
          "context": "https://github.com/WeBankFinTech/WeIdentity/blob/master/context/v1",
          "claim": {
              "content": "b1016358-cf72-42be-9f4b-a18fca610fca",
              "receiver": "did:weid:101:0x7ed16eca3b0737227bc986dd0f2851f644cf4754",
              "weid": "did:weid:101:0xfd28ad212a2de77fee518b4914b8579a40c601fa"
          },
          "id": "21d10ab1-75fe-4733-9f1d-f0bad71b5922",
          "proof": {
              "created": 1580996777,
              "creator": "did:weid:101:0xfd28ad212a2de77fee518b4914b8579a40c601fa#keys-0",
              "salt": {
                  "content": "ncZ5F",
                  "receiver": "L0c40",
                  "weid": "I4aop"
              },
              "signatureValue": "{"claim":"{\"acc\":\"0x7773420d753e69eab620f2ce5b198bddb61f420c1e53495db6b7f47048e34b7f\",\"name\":\"0x8ba4c2d57e830a9810824716f94d3d734e5c17e77835570a2d9e184248d855cf\"}","context":"https://github.com/WeBankFinTech/WeIdentity/blob/master/context/v1","cptId":10,"expirationDate":2218367553,"id":"90a65d0a-eb3b-4ba4-882e-e6d48bb1256e","issuanceDate":1581422279,"issuer":"did:weid:101:0xb81d7948beedda55b46ce0da827bc21b0919736e","proof":null,"type":["VerifiableCredential","hashTree"]}",
              "type": "Secp256k1"
          },
          "type": [
              "VerifiableCredential",
              "hashTree"
          ],
          "issuer": "did:weid:101:0xfd28ad212a2de77fee518b4914b8579a40c601fa",
          "expirationDate": 4111737153
      },
      "errorCode": 0,
      "errorMessage": "success"
    }

接下来，您只需要将这个CredentialPojo里面的签名值（proof中的signatureValue项）使用Issuer的私钥进行签名并Base64编码，就能得到一个正确的CredentialPojo了。

使用ECDSA私钥进行签名和Base64编码的范例代码见下：

.. code-block:: java

    String signature = DataToolUtils.sign(signatureValue, privateKey);

WeIdentity Endpoint Service API
------------------------------------

1. 获取所有已注册的Endpoint信息
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

调用接口：

.. list-table::
   :header-rows: 1
   :widths: 30 50

   * - 标题
     - 描述
   * - 接口名
     - weid/api/endpoint
   * - Method
     - GET
   * - Content-Type
     - application/json

接口入参：无

接口返回：

.. code-block:: java

    {
        "ErrorCode": 0,
        "ErrorMessage": "success",
        "respBody": [
            {
                "requestName": "create-passphrase",
                "inAddr": [
                    "127.0.0.1:6010",
                    "127.0.0.1:6011"
                ],
                "description": "Create a valid random passphrase"
            },
            {
                "requestName": "verify-passphrase",
                "inAddr": [
                    "127.0.0.1:6012",
                    "127.0.0.1:6013"
                ],
                "description": "Verify a passphrase"
            }
        ]
    }


2. 进行Endpoint调用
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

调用接口：

.. list-table::
   :header-rows: 1
   :widths: 30 50

   * - 标题
     - 描述
   * - 接口名
     - weid/api/endpoint/{endpoint}
   * - Method
     - POST
   * - Content-Type
     - application/json


接口入参：

.. list-table::
   :header-rows: 1
   :widths: 30 60 20

   * - Key
     - Value
     - Required
   * - /{endpoint}
     - 在API路径中标明的API名，String
     - Y
   * - body
     - 以```分隔的多个传入服务端用于执行API的参数
     - Y

接口入参：

.. code-block:: java

    {
        "body": "did:weid:0x12025448644151248e5c1115b23a3fe55f4158e4153```25"
    }


接口返回: application/json

.. list-table::
   :header-rows: 1
   :widths: 30 50

   * - Key
     - Value
   * - ErrorCode
     - 错误码，0表示成功
   * - ErrorMessage
     - 错误信息
   * - respBody
     - SDK侧的返回值，String

接口返回：

.. code-block:: java

    {
        "ErrorCode": 0,
        "ErrorMessage": "success",
        "respBody": "did:weid:0x1Ae5b88d37327830307ab8da0ec5D8E8692A35D3",
    }
