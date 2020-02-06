# WeIdentity Java SDK 与各版本JDK的兼容性

备注：本页面所有测试由rockyxia完成。

## 1.CentOS

测试操作系统： CentOS Linux release 7.2.1511 (Core) 

### Open JDK 各版本的兼容性 (推荐使用)

| open JDK版本 | 测试结果       | 
| ---------- | ------------ |
| 1.8.0      |  验证成功✅  | 
| 9.0.1      |  验证成功✅ | 
| 10         |  验证成功✅  |
| 11         |  验证成功✅  |
| 12         |  验证成功✅  |
| 13         |  验证成功✅  |


### 通过 yum 命令下载安装的Open JDK

不兼容。

| yum open JDK | 测试结果 |
| ------------ | ---- |
| 1.8.0_232    | 验证失败❌ |
| 11.0.5       | 验证失败 ❌|

不兼容的原因：由于CentOS的yum仓库的OpenJDK缺少JCE(Java Cryptography Extension)


### Oracle JDK 各版本的兼容性 

| Oracle JDK版本 | 测试结果  |
| ------------ | ---- |
| 1.8.141      | 验证成功  &#x2611;  ✅ | 
| 1.8.181      | 验证成功  ✅| 
| 1.8.181      | 验证成功  ✅| 
| 1.8.231      | 验证成功  ✅| 
| 11.0.5       | 验证成功 ✅| 
| 13.0.1       | 验证成功 ✅| 

## 2.Ubuntu

    测试操作系统：Ubuntu 16.04.1 LTS

### Open JDK 各版本的兼容性 (推荐使用)

| open JDK版本 | 测试结果          |
| ---------- | ---------- |
| 1.8.0      | 安装成功✅ |
| 9.0.1      | 安装成功✅|
| 10         | 安装成功 ✅|
| 11         | 安装成功✅|
| 12         | 安装成功 ✅|
| 13         | 安装成功 ✅|

### Oracle JDK 各版本的兼容性 

| Oracle JDK版本 | 测试结果        | 
| ------------ | ---------- | 
| 1.8.141      | 安装成功    ✅   | 
| 1.8.181      | 安装成功      ✅ | 
| 1.8.181      | 安装成功     ✅  |
| 1.8.231      | 安装成功 ✅      | 
| 11.0.5       | 安装成功    ✅   | 
| 13.0.1       | 安装成功,告警信息可忽略 ✅| 

告警信息如下：

WARNING: An illegal reflective access operation has occurred
WARNING: Illegal reflective access by io.netty.util.internal.ReflectionUtil (file:/home/rocky/weid-build-tools/dist/lib/netty-all-4.1.15.Final.jar) to constructor java.nio.DirectByteBuffer(long,int)
WARNING: Please consider reporting this to the maintainers of io.netty.util.internal.ReflectionUtil
WARNING: Use --illegal-access=warn to enable warnings of further illegal reflective access operations
WARNING: All illegal access operations will be denied in a future release

### apt-get Open JDK 各版本的兼容性 (推荐使用)

| apt-get Open JDK版本 | 测试结果          |
| ---------- | ---------- |
| 1.8.0_242  | 安装成功✅ |
| 9.0.4      | 安装成功✅|
| 10.0.2     | 安装成功 ✅|
| 11.0.5     | 安装成功✅|
| 12.0.2     | 安装成功 ✅|
| 13.0.1     | 安装成功 ✅|

部署过程中有告警，告警信息如下：

WARNING: An illegal reflective access operation has occurred
WARNING: Illegal reflective access by io.netty.util.internal.ReflectionUtil (file:/home/rocky/weid-build-tools/dist/lib/netty-all-4.1.15.Final.jar) to constructor java.nio.DirectByteBuffer(long,int)
WARNING: Please consider reporting this to the maintainers of io.netty.util.internal.ReflectionUtil
WARNING: Use --illegal-access=warn to enable warnings of further illegal reflective access operations
WARNING: All illegal access operations will be denied in a future release
