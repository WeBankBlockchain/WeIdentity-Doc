
.. _weidentity-endpoint:

WeIdentity Endpoint Service
=================================

在开发过程中，您可能会有将自己的某些Java方法暴露给外部调用方调用的需求。在过去，您可能需要自己开发一套基于HTTP/HTTPS的请求，及基于RPC的Endpoint服务端点的创建、维护及处理服务，这可能会花费大量的开发人力和工时。

现在，WeIdentity提供了一套标准化的Endpoint服务的参考实现样例。只要您已经集成了WeIdentity及RestService，便可以直接使用这一功能。您只需要在自己的Java集成环境里调用WeIdentity Java SDK，进行Endpoint的注册，之后您的方法就会自动出现在RestService对外暴露的API列表里，并可供外部调用了。

部署Endpoint Service
-----------------------

您可以参照 \ `部署指引 <./weidentity-endpoint-deploy.html>`_\ 文档，进行Endpoint Service的部署及初始化。

.. toctree::
   :hidden:
   :maxdepth: 1
   
   weidentity-endpoint-deploy.rst

使用Endpoint Service
-----------------------------

您可以参照 \ `RestService API <./weidentity-rest-api.html>`_\ 文档中的Endpoint Service API部分，了解如何查询和调用您注册的Endpoint信息。

基于Endpoint Service的数据授权
------------------------------------

WeIdentity提供依托于Endpoint Service而存在的数据授权。授权的详细信息会以可信可验证的凭证（Credential）方式展现，并由Endpoint Service服务端进行验证。

一个标准的数据授权凭证其CPT ID为101。它的Claim中包括以下内容：

- 授权发起者（fromWeId）：表示这个凭证是由哪个WeID所发起的
    - 必须和凭证的Issuer一致，且在链上存在
- 授权接收者（toWeId）：表示这个凭证是由哪个WeID所接收的
- 授权时间（duration）：表示此授权的持续时间，单位为毫秒
- 服务地址（serviceUrl）：表示此授权的目的服务地址
    - 只支持HTTP/HTTPS协议
    - 必须是以标准URL形式存储，必须包括主机名、端口及路径
- 资源ID（resourceId）：表示此授权所请求的资源ID，以UUID形式表示

简而言之，调用者必须传入一个CPT101授权凭证，此凭证中包含数据授权的主机地址、资源ID，由Endpoint Service的服务端进行查验，确认凭证合法且服务地址（serviceUrl）已在本服务端后台注册过。查验通过后，Endpoint Service会将请求以RPC请求的形式转向在本服务端注册的各端点后台，并由各后台根据传入的资源ID，返回资源信息。具体的调用方式可以查阅API文档。

技术细节
--------------

- Endpoint Service的RPC库基于 \ `smart-socket 1.4.2 <https://gitee.com/smartboot/smart-socket>`_\ 这一Java AIO框架。
    - 目前Endpoint Service在此RPC库基础上自行实现了更加简单易用的按需断线重连机制。
    - 目前Endpoint Service以异步的方式实现远程调用，并通过指定时长内的本地轮询查询执行结果。如果您的方法可能耗时过长，可以调节轮询的最大等待时长。
- Endpoint Service支持1:N的多集成端访问和多活机制。如果一个Endpoint可以由多台集成端访问，那么当其中某一台集成端访问出错时，访问其的请求会被自动导向其他注册了相同Endpoint的机器上。
