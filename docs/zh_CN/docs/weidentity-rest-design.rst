
.. _weidentity-rest-design:

WeIdentity RestService 设计文档
----------------------------------------

1. 设计理念
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

RestService的设计秉持以下原则：

- 私钥在网络上进行传输是有可能遭遇窃听或中间人攻击的。因此，如果牵扯到远程调用，应该尽可能避免把私钥作为输入参数。私钥本身，要么由客户端自行存储，要么托管在RestService服务端。对于前者，WeIdentity支持“轻客户端模式”，允许用户直接在客户端对交易进行私钥签名并直接经由RestService向区块链发送交易；对于后者，RestService支持“私钥托管模式”，允许用户在信任RestService的前提下将私钥托管到服务端本地。
- 在某些场景下，机构或用户可能不支持使用Java环境部署调用SDK，但是任何机构或用户都可以发送HTTP请求。因此，使用RestService的另一个好处便是降低了多语言版本的SDK的开发成本。用户只要有发送HTTP请求的能力即可以调用WeIdentity的相关功能。
- RestService服务端所有API均为无状态的。

2. RestService整体技术架构
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

2.1 架构图
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

.. image:: images/arch.jpg

RestService架构包括以下模块：

* 用户应用：用户的业务app应用，发送HTTP请求
* rest-server：Server服务器端
* weid-java-sdk：WeIdentity的SDK jar包

2.2 交易模型
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

过去的做法是：

- 用户传入接口参数（包括私钥），直接调用RESTful接口，RestService调用sdk然后发送交易；

改造后：

（轻客户端模式：两次交互）
- 轻客户端传入接口参数（不包括私钥），然后POST /weid/api/encode，发送请求给RestService
- RestService接受请求，根据接口参数组装、编码区块链原始交易串，返回给轻客户端
- 轻客户端在本地使用自己的私钥，对原始交易串进行符合ECDSA的sha3签名，然后POST /weid/api/transact，发送请求给RestService
- RestService接受请求，打包签名完成的交易串，直接发送交易给区块链节点

（托管模式：一次交互）
- 用户应用调传入自己的私钥索引以指明自己使用哪个私钥，然后POST /weid/api/invoke，发送请求给RestService
- RestService接受请求，依据索引载入所托管的私钥，调用weid-java-sdk的对应方法，发送交易给区块链节点

3. 调用时序说明
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. image:: images/fig2.jpg

步骤说明：

（托管模式）
- 用户组装方法名、业务输入参数、API版本号、私钥索引，然后POST /weid/api/invoke，要求调用函数。
- Server访问SDK进行入参检测，若通过，索引其托管在Server端的私钥，使用私钥进行签名，随后调用SDK相关合约方法并返回结果。
