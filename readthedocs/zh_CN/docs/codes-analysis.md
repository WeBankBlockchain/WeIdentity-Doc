# WeIdentity智能合约

# 介绍

本文结合WeIdentity智能合约文档对其源码进行阅读分析。当前，WeIdentity合约层面的工作目标主要包括两部分：

- **WeIdentity DID智能合约**，负责链上ID体系建立，具体包括生成DID（Distributed IDentity）、生成DID Document、DID在链上的读取与更新。
- **WeIdentity Authority智能合约**，负责进行联盟链权限管理，具体包括链上DID角色的定义、操作与权限的定义与控制。

## WeIdentity DID

### 概述

从业务视角来看，DID智能合约只需要做一件事，就是如何定义DID Document的存储结构和读写方式。DID Document的结构并不复杂（见规范文档），但在实际的业务中，存在一些挑战：

- 伴随着接入用户（人与物）的快速增长，DID的总量将会增长迅速，规模庞大。因此，设计一个大而全的映射表是不现实的，这会带来巨大的寻址开销，即使采用传统分库、分表、跨链的思路也难以应付。
- DID存在更新的需求。因此，每次都存储完整的Document域在更新情况下会产生大量的历史数据。

因此，WeIdentity使用Linked Event：基于事件链的存储方法来解决以上问题。

### 存储结构

Linked Event的核心实现思路是借助Solidity的事件（Event）机制，采用类似链表的思路对DID Document的更新进行存储和读取。在Solidity里，每个区块都有对应的Event存储区，用于对区块相关的事件进行存储，并最终存入Event log。因此，存储层面上，在不同时间点DID的更新可以存入更新时当前块的Event里，同时将当前块高作为索引记录每次更新事件。读取层面上，如果要读取完整DID Document，只需按索引反向遍历对应的块的Event里即可。基于这一思路，进行以下设计：

- 设计一个映射记录，使用DID的地址作为索引，用来存储每个DID最近的一次更新事件所对应的块高；
- 设计一个更新事件，用来记录每次DID更新的相关属性及前一个块高；
- 设计一个查询函数，用来读取映射记录找到某个DID的最近的块高，以便反向解析具体的更新事件。

以上数据和逻辑会被合并到一个整体合约里。具体流程为：

- 每当触发一次DID Document的属性更新，就记入一次更新事件，同时记录更新事件所对应的当前块高，存入整体合约的记录映射部分；
- 记录映射部分存入整体合约的存储区，更新事件最终会存入区块链的Event；
- 当读取DID Document时，只需通过记录映射读取块高，反向遍历对应的块的Event，解析并找到Document更新相关的事件内容，然后合并即可。

这一流程图可见于：

![linked-events.png](https://weidentity.readthedocs.io/zh_CN/latest/_images/linked-events.png)





### 性能评估

使用Linked Event进行存储的优势有以下几点：

- 非常适合更新的场景。由于Solidity Event的特性，本方案的写性能和存储开销会远远优于完整存储DID Document内容进入合约的解决方案。
- 更方便的记录历史版本。通过记录每个事件的块高，可以快速的定位到每个事件，在溯源场景下有着广泛的应用；同时，又不需对那些未更新的属性项进行存储。
- 读性能对更新事件是O(N)的时间增长。因此，在Document更新不频繁的场景下，读性能非常好。由于WeIdentity的DID本身更多地用来存储公钥等信息，更新频率大部分情况下并不高，因此非常适合WeIdentity的使用场景。



## WeIdentity Authority

**WeIdentity Authority智能合约**，负责进行联盟链权限管理，具体包括链上DID角色的定义、操作与权限的定义与控制。

- 不同的DID实体拥有不同的权限。

例如，存在Authority Issuer这一角色用来描述现实世界中的「权威凭证发行者」，它们能够发行低段位授权CPT，权限高于一般的DID；更进一步地，在Authority Issuer之上存在着委员会（Committee），它们的权限更高，包括了对Authority Issuer的治理等内容。因此，WeIdentity需要设计合理的「角色—操作」二元权限控制。

- 权限管理的业务逻辑会随着业务迭代而不断更新。

在真实业务场景中，随着业务变化，权限管理逻辑也可能随之改变；同时，不同的业务方可能会有定制化权限管理的需求。因此，WeIdentity需要进行合理的分层设计，将数据和行为逻辑分离，在升级的情况下就只需对行为逻辑部分进行升级，数据存储保持不变，尽可能地降低更新成本。

当前，业内已经有了一些对权限进行操作和维护的开源解决方案，如ds-auth和OpenZepplin的Role智能合约；但它们的权限管理逻辑可扩展性较差且不支持合约分层更新。下文将介绍WeIdentity的Authority智能合约实现。

### 架构

#### 角色与权限

当前的WeIdentity角色设计了四种角色：

- 一般DID。一般的实体（人或物），由WeIdentity的分布式多中心的ID注册机制生成，没有特定权限。
- Authority Issuer。授权机构，具有发行低段位授权CPT的权限。
- Committee Member。机构委员会成员。具有管理Authority Issuer成员资格的权限。
- Administrator。系统管理员。具有管理Committee Member及Authority Issuer成员资格的权限，未来还包括修改合约地址的权限。

每个角色具体的权限表如下：

| 操作                   | 一般DID | Authority Issuer | Committee Member | Administrator |
| ---------------------- | ------- | ---------------- | ---------------- | ------------- |
| 增删改Administrator    | N       | N                | N                | Y             |
| 增删改Committee Member | N       | N                | N                | Y             |
| 增删改Authority Issuer | N       | N                | Y                | Y             |
| 发行授权CPT            | N       | Y                | Y                | Y             |

#### 合约分层

WeIdentity采用分层设计模式，即将合约分为逻辑合约、数据合约、及权限合约。

- 逻辑合约：它专注于数据的逻辑处理和对外提供接口，通过访问数据合约获得数据，对数据做逻辑处理，写回数据合约。一般情况下，控制器合约不需要存储任何数据，它完全依赖外部的输入来决定对数据合约的访问。
- 数据合约：它专注于数据结构的定义、数据内容的存储和数据读写的直接接口。
- 权限合约：它专注于判断访问者的角色，并基于判断结果确定不同操作的权限。

上述架构图如下：

![authority-contract-arch.png](https://weidentity.readthedocs.io/zh_CN/latest/_images/authority-contract-arch.png)

#### 权限与安全管理

当前的WeIdentity权限管理的挑战是：

- 合约在链上部署之后，攻击者可能会绕过SDK直接以DApp的形式访问合约。因此合约层面必须要有自完善的权限处理逻辑，不能依赖SDK。
- 数据合约是公开的，因此数据合约的操作也需要进行权限管理。

WeIdentity的权限管理依赖于一个独立的RoleManager权限管理器合约，它承担了合约所有的权限检查逻辑。WeIdentity的权限粒度是基于角色和操作的二元组，这也是当前大多数智能合约权限控制的通用做法。它的设计要点包括：

- 将角色和操作权限分别存储。
- 设计一个权限检查函数checkPermission()供外部调用，输入参数为「地址，操作」的二元组。
- 对角色和权限分别设计增删改函数供外部调用。
- 所有WeIdentity的数据合约里需要进行权限检查的操作，都通过外部合约函数调用的方式，调用checkPermission()。
- 所有WeIdentity依赖权限管理器的合约，需要有更新权限管理器地址的能力。

WeIdentity的权限管理有以下特性：

- 优秀的可扩展性。WeIdentity的权限控制合约使用外部调用而非继承（如ds-auth和OpenZepplin的Role智能合约实现角色管理方式）方式实现。在权限控制合约升级的场景中，外部调用方案只需简单地将权限管理器合约地址更新即可，极大地提升了灵活度。
- 使用tx.origin而非msg.sender进行调用源追踪。这是因为用户的权限和自己的DID地址唯一绑定。因此所有权限的验证必须要以最原始用户地址作为判断标准，不能单纯地依赖msg.sender。此外，WeIdentity的权限控制合约需要支持更大的可扩展性，以支持更多公众联盟链的参与成员自行实现不同的Controller。因此，需要通过tx.origin追踪到调用者的WeIdentity DID，并根据DID确定权限。



## WeIdentity CPT智能合约

WeIdentity的CPT（Claim Protocol Type）合约，用于在链上存储凭证的Claim模板。CPT合约使用标准的数据-逻辑分离架构。一个数据CPT合约里，最重要的是其jsonSchema部分，它存储了以jsonSchema格式记载的Claim格式内容。区分不同CPT是通过其ID来进行的。

根据CPT使用目的、内容的不同，ID可以被划分成以下三个范围：1~1000（系统CPT），1000~2000000（授权CPT），2000000以上（普通CPT）。

### 系统CPT表

系统CPT的ID落在1~1000里，它们是在WeIdentity智能合约部署之初就创建好的内置CPT，用来完成所有WeIdentity实例的统一功能，它们在部署WeIdentity智能合约时，在初始化过程中部署在链上。系统CPT不支持任何角色创建。

当前，系统CPT表包括以下内容：

| ID   | 标题             | 内容                                           |
| ---- | ---------------- | ---------------------------------------------- |
| 101  | 授权凭证         | 某个WeID授权另一个WeID使用数据                 |
| 102  | 挑战凭证         | 某个WeID对另一个WeID身份证明的挑战             |
| 103  | 身份验证凭证     | 某个WeID针对CPT102的挑战的回复                 |
| 104  | Claim Policy     | 某个选择性披露的Claim Policy定义               |
| 105  | API Endpoint     | Endpoint端点服务的端点定义                     |
| 106  | 嵌套凭证         | 嵌套的Credential，用来进行多签                 |
| 107  | 嵌套凭证         | 嵌套的CredentialPojo，用来进行多签             |
| 108  | 整合可信时间戳   | 为某个嵌套凭证生成的可信时间戳，包含凭证原文   |
| 109  | 可分离可信时间戳 | 为某个嵌套凭证生成的可信时间戳，不包含凭证原文 |



关于每个系统CPT的详细字段要求，可以查阅代码中的 [对应文件](https://github.com/WeBankBlockchain/WeIdentity/tree/master/src/main/java/com/webank/weid/protocol/cpt)，此处不再详细展开。

### 授权CPT

授权CPT的ID落在1000~2000000里，如Authority合约中所述，授权CPT仅支持由Authority Issuer创建，一般是和具体的联盟链业务相关。

### 一般CPT

一般CPT的ID从2000000开始自增。任何WeID均可以创建此类CPT。

## WeIdentity智能合约依赖关系

![contractdep.jpg](https://weidentity.readthedocs.io/zh_CN/latest/_images/contractdep.jpg)



Evidence相关合约使用Hash实现了存证功能，可视为一个较独立部分，这里先不加以考虑。



# RoleController

## 背景

### 权限

每个角色具体的权限表如下：

| 操作                   | 一般DID | Authority Issuer | Committee Member | Administrator |
| ---------------------- | ------- | ---------------- | ---------------- | ------------- |
| 增删改Administrator    | N       | **N**            | N                | Y             |
| 增删改Committee Member | N       | **N**            | N                | Y             |
| 增删改Authority Issuer | N       | **N**            | Y                | Y             |
| 发行授权CPT            | N       | **Y**            | Y                | Y             |

通过这里我们可知Authority Issuer的权限允许其发行授权CPT

## 代码分析

### 功能概述

核心权限判断控制合约

### 整体结构

```solidity
	//首先定义了通用的错误提示码
    uint constant public RETURN_CODE_FAILURE_NO_PERMISSION = 500000;

     //定义角色相关代号
    uint constant public ROLE_AUTHORITY_ISSUER = 100;
    uint constant public ROLE_COMMITTEE = 101;
    uint constant public ROLE_ADMIN = 102;

    
     //定义操作相关常数
    uint constant public MODIFY_AUTHORITY_ISSUER = 200;
    uint constant public MODIFY_COMMITTEE = 201;
    uint constant public MODIFY_ADMIN = 202;
    uint constant public MODIFY_KEY_CPT = 203;

    //建立角色映射
    mapping (address => bool) private authorityIssuerRoleBearer;
    mapping (address => bool) private committeeMemberRoleBearer;
    mapping (address => bool) private adminRoleBearer;
```

```solidity
//构造器并赋予合约部署者相关权限
    function RoleController() public {
        authorityIssuerRoleBearer[msg.sender] = true;
        adminRoleBearer[msg.sender] = true;
        committeeMemberRoleBearer[msg.sender] = true;
    }
```

## 具体函数说明

```solidity
// 查询某地址是否有某操作的权限
    function checkPermission(address addr,uint operation) public constant returns (bool) {
        if (operation == MODIFY_AUTHORITY_ISSUER) {
            if (adminRoleBearer[addr] || committeeMemberRoleBearer[addr]) {
                return true;
            }
        }
        if (operation == MODIFY_COMMITTEE) {
            if (adminRoleBearer[addr]) {
                return true;
            }
        }
        if (operation == MODIFY_ADMIN) {
            if (adminRoleBearer[addr]) {
                return true;
            }
        }
        if (operation == MODIFY_KEY_CPT) {
            if (authorityIssuerRoleBearer[addr]) {
                return true;
            }
        }
        return false;
    }
```

```solidity
// 给某地址添加权限
function addRole(address addr,uint role) public {
        if (role == ROLE_AUTHORITY_ISSUER) {
        	//检查合约调用者是否有相应修改的权限
            if (checkPermission(tx.origin, MODIFY_AUTHORITY_ISSUER)) {authorityIssuerRoleBearer[addr] = true;}
        }
        if (role == ROLE_COMMITTEE) {
            if (checkPermission(tx.origin, MODIFY_COMMITTEE)) {committeeMemberRoleBearer[addr] = true;}
        }
        if (role == ROLE_ADMIN) {
            if (checkPermission(tx.origin, MODIFY_ADMIN)) {
                adminRoleBearer[addr] = true;
            }
        }
    }
```

```solidity
// 删除某地址权限
function removeRole(
        address addr,
        uint role
    ) 
        public 
    {
        if (role == ROLE_AUTHORITY_ISSUER) {
            if (checkPermission(tx.origin, MODIFY_AUTHORITY_ISSUER)) {
                authorityIssuerRoleBearer[addr] = false;
            }
        }
        if (role == ROLE_COMMITTEE) {
            if (checkPermission(tx.origin, MODIFY_COMMITTEE)) {
                committeeMemberRoleBearer[addr] = false;
            }
        }
        if (role == ROLE_ADMIN) {
            if (checkPermission(tx.origin, MODIFY_ADMIN)) {
                adminRoleBearer[addr] = false;
            }
        }
    }

```

```solidity
// 检查某地址是否属于某类别
function checkRole(
        address addr,
        uint role
    ) 
        public 
        constant 
        returns (bool) 
    {
        if (role == ROLE_AUTHORITY_ISSUER) {
            return authorityIssuerRoleBearer[addr];
        }
        if (role == ROLE_COMMITTEE) {
            return committeeMemberRoleBearer[addr];
        }
        if (role == ROLE_ADMIN) {
            return adminRoleBearer[addr];
        }
    }
```



# WeIdContract

## 背景

### 角色与权限

当前的WeIdentity角色设计了四种角色：

- 一般DID。一般的实体（人或物），由WeIdentity的分布式多中心的ID注册机制生成，没有特定权限。
- Authority Issuer。授权机构，具有发行低段位授权CPT的权限。
- Committee Member。机构委员会成员。具有管理Authority Issuer成员资格的权限。
- Administrator。系统管理员。具有管理Committee Member及Authority Issuer成员资格的权限，未来还包括修改合约地址的权限。



每个角色具体的权限表如下：

| 操作                   | 一般DID | Authority Issuer | Committee Member | Administrator |
| ---------------------- | ------- | ---------------- | ---------------- | ------------- |
| 增删改Administrator    | N       | N                | N                | Y             |
| 增删改Committee Member | N       | N                | N                | Y             |
| 增删改Authority Issuer | N       | N                | Y                | Y             |
| 发行授权CPT            | N       | Y                | Y                | Y             |

### Event事件

当被调用时，会触发参数存储到交易的日志中（区块链上的特殊数据结构）。这些日志与合约的地址关联，并合并到区块链中。日志和事件在合约内不可直接被访问，即使是创建日志的合约。

可设置indexed，来设置是否被索引。设置为索引后，可以允许通过这个参数来查找日志，甚至可以按特定的值过滤。

```solidity
event WeIdAttributeChanged(
        address indexed identity,
        bytes32 key,
        bytes value,
        uint previousBlock,
        int updated
    );

    event WeIdHistoryEvent(
        address indexed identity,
        uint previousBlock,
        int created
    );
```

该合约中定义了两个事件

在createWeId函数中创建WeId时会发送两条Event事件，用来记录创建时间、auth（身份信息，包括公钥和address）、上次更新WeId对应的区块高度。保存当前区块高度到changed数组，key为address，value为当前区块高度。
setAttribute发送WeIdAttributeChanged事件用来保存DID Document信息，保存当前区块高度到changed数组。

### modifier

```solidity
//上述代码使用了该modifier，用于限制输入的identity必须是合约调用者本身的地址
modifier onlyOwner(address identity, address actor) {
        require (actor == identity);
        _;
    }
```

测试传参过程中如果报错请查看：

https://blog.csdn.net/m0_52739647/article/details/126649846

## 代码分析

### import

```
import "./RoleController.sol";
```



### 功能概述

创建或者修改DID的相关属性并用Event进行记录，可大体分为两类：创建或修改自身的属性以及有权限的机构创造或修改ID属性



### 整体结构

参数设置：

```solidity
	// 使用import中的RoleController类
	RoleController private roleController;
	
	//changed[identity]：存储该id最近一次发生改变的区块
    mapping(address => uint) changed;
    
	//记录合约建立时的区块
    uint firstBlockNum;

	//最新交易的区块
    uint lastBlockNum;
    
    // 计数器：记录DID的总数
    uint weIdCount = 0;

	// blockAfterLink[BlockNum]：与该区块相关联的下一个区块数
    mapping(uint => uint) blockAfterLink;
    
    bytes32 constant private WEID_KEY_CREATED = "created";
    
    bytes32 constant private WEID_KEY_AUTHENTICATION = "/weId/auth";
```



修改器（详细说明见上）：

```solidity
 modifier onlyOwner(address identity, address actor) {
        require (actor == identity);
        _;
    }
```



构造器：

```solidity
//需要roleControllerAddress的合约地址
function WeIdContract(
        address roleControllerAddress
    )
        public
    {
    	//将RoleController按照指定地址构造，这样方便后续RoleController合约更新升级
        roleController = RoleController(roleControllerAddress);
        //初始化firstBlockNum与lastBlockNum
        firstBlockNum = block.number;
        lastBlockNum = firstBlockNum;
    }
```



构造Event事件：

```solidity
event WeIdAttributeChanged(
        address indexed identity,
        bytes32 key,
        bytes value,
        uint previousBlock,
        int updated
    );

    event WeIdHistoryEvent(
        address indexed identity,
        uint previousBlock,
        int created
    );
```



get函数：

* `getLatestRelatedBlock(address identity)`：查询与该ID相关的最新区块
* `getFirstBlockNum()`:查询首区块数
* `getNextBlockNumByBlockNum(uint currentBlockNum)`：查询与该区块相关联的下一个区块
* `getLatestBlockNum() `:查询最新区块数
* `getWeIdCount()`：查询ID总数
* `isIdentityExist(address identity) `:ID是否存在



### 具体函数说明

```solidity
//创建该地址自己的ID	
	function createWeId(address identity,bytes auth,bytes created,int updated) public
		//使用modifier，用于限制输入的identity必须是合约调用者本身的地址
        onlyOwner(identity, msg.sender)
    {
        //ID属性变动事件WeIdAttributeChanged，created与updated是一些身份信息（包括公钥和address）
        WeIdAttributeChanged(identity, WEID_KEY_CREATED, created, changed[identity], updated);
        //基本同上
        WeIdAttributeChanged(identity, WEID_KEY_AUTHENTICATION, auth, changed[identity], updated);
        //更新 changed[identity]、blockAfterLink[lastBlockNum]、lastBlockNum
        changed[identity] = block.number;
        if (block.number > lastBlockNum) {
            blockAfterLink[lastBlockNum] = block.number;
        }
        WeIdHistoryEvent(identity, lastBlockNum, updated);
        if (block.number > lastBlockNum) {
            lastBlockNum = block.number;
        }
        //计数器+1
        weIdCount++;
    }

```

```solidity
//有权限的机构创造ID
function delegateCreateWeId(address identity,bytes auth,bytes created,int updated)public{
        // 检查权限等级，合约交互者是否是授权机构，没有要求identity必须是合约交互者地址
        // 即在判断其权限之后，可以创造其他地址的ID
        if (roleController.checkPermission(msg.sender, roleController.MODIFY_AUTHORITY_ISSUER())) {
            WeIdAttributeChanged(identity, WEID_KEY_CREATED, created, changed[identity], updated);
            WeIdAttributeChanged(identity, WEID_KEY_AUTHENTICATION, auth, changed[identity], updated);
            changed[identity] = block.number;
            if (block.number > lastBlockNum) {
                blockAfterLink[lastBlockNum] = block.number;
            }
            WeIdHistoryEvent(identity, lastBlockNum, updated);
            if (block.number > lastBlockNum) {
                lastBlockNum = block.number;
            }
            weIdCount++;
        }
    }

```

```solidity
//更改自身的ID属性
function setAttribute(address identity, bytes32 key, bytes value, int updated) public 
        //限制更改自身id的属性
        onlyOwner(identity, msg.sender)
    {
        WeIdAttributeChanged(identity, key, value, changed[identity], updated);
        changed[identity] = block.number;
    }
```

```solidity
//有权限的机构更改ID属性
function delegateSetAttribute(address identity,bytes32 key,bytes value,int updated)public
    {
        
        if (roleController.checkPermission(msg.sender, roleController.MODIFY_AUTHORITY_ISSUER())) {
            WeIdAttributeChanged(identity, key, value, changed[identity], updated);
            changed[identity] = block.number;
        }
    }

```



# AuthorityIssuerData 

## 背景

### 权限

每个角色具体的权限表如下：

| 操作                   | 一般DID | Authority Issuer | Committee Member | Administrator |
| ---------------------- | ------- | ---------------- | ---------------- | ------------- |
| 增删改Administrator    | N       | **N**            | N                | Y             |
| 增删改Committee Member | N       | **N**            | N                | Y             |
| 增删改Authority Issuer | N       | **N**            | Y                | Y             |
| 发行授权CPT            | N       | **Y**            | Y                | Y             |

通过这里我们可知Authority Issuer的权限允许其发行授权CPT

### 合约分层

其实从项目整体结构分析就不难发现，WeIdentity采用分层设计模式，即将合约分为逻辑合约、数据合约、及权限合约。

- 逻辑合约：它专注于数据的逻辑处理和对外提供接口，通过访问数据合约获得数据，对数据做逻辑处理，写回数据合约。一般情况下，控制器合约不需要存储任何数据，它完全依赖外部的输入来决定对数据合约的访问。
- 数据合约：它专注于数据结构的定义、数据内容的存储和数据读写的直接接口。
- 权限合约：它专注于判断访问者的角色，并基于判断结果确定不同操作的权限。

![authority-contract-arch.png](https://weidentity.readthedocs.io/zh_CN/latest/_images/authority-contract-arch.png)

所以该部分是涉及合约的数据分层部分`SpecificIssuerData ` 、`CommitteeMemberData `、`AuthorityIssuerData `、` CptData`与之同理

此类合约专注于数据结构的定义、数据内容的存储和数据读写的直接接口。

## 代码分析

### import

```
//导入权限管理合约
import "./RoleController.sol";
```

### 功能概述

实现授权机构的创建删除以及认证

### 整体结构

```solidity
    // Error codes
    uint constant private RETURN_CODE_SUCCESS = 0;
    uint constant private RETURN_CODE_FAILURE_ALREADY_EXISTS = 500201;
    uint constant private RETURN_CODE_FAILURE_NOT_EXIST = 500202;
    uint constant private RETURN_CODE_NAME_ALREADY_EXISTS = 500203;
    uint constant private RETURN_CODE_UNRECOGNIZED = 500204;

    struct AuthorityIssuer {
        
        //AuthorityIssuer的一些属性
        // [0]: name, [1]: desc, [2-11]: extra string
        bytes32[16] attribBytes32;
        // [0]: create date, [1]: update date, [2-11]: extra int
        // [15]: flag for recognition status (0: unrecognized, 1: recognized)
        int[16] attribInt;
        bytes accValue;
    }
    
	//authorityIssuerMap[addr]：相应地址对应的AuthorityIss结构
    mapping (address => AuthorityIssuer) private authorityIssuerMap;
    
    // 授权机构的地址队列
    address[] private authorityIssuerArray;
    
    //uniqueNameMap[name]：相应名字映射的地址
    mapping (bytes32 => address) private uniqueNameMap;
    
    //计数器：已认证的机构数
    uint recognizedIssuerCount = 0;

    RoleController private roleController;
```

构造器：

```solidity
//引入RoleController合约功能同上 
    function AuthorityIssuerData(address addr) public {
        roleController = RoleController(addr);
    }
```



Get函数：

* `getRecognizedIssuerCount()`：返回已认证的授权机构数
* `getAddressFromName(bytes32 name)`：根据名字返回地址
* `isNameDuplicate(bytes32 name)`：判断名字是否重复
* `isAuthorityIssuer(address addr) `：是否为授权机构
* `getAuthorityIssuerInfoAccValue(address addr)`：根据地址获取其AuthorityIssuer的AccValue属性
* `getAuthorityIssuerInfoNonAccValue(address addr)`：根据地址获取其AuthorityIssuer部分属性
* `getAuthorityIssuerFromIndex(uint index)`：根据序号获得其授权机构地址
* `getDatasetLength()`：获得授权机构总数

### 具体函数说明

```solidity
//判断是否有此AuthorityIssuer，即其地址是否在authorityIssuerMap中存在
function isAuthorityIssuer(address addr) public constant returns (bool) 
    {
    	//结合权限合约进行AuthorityIssuer权限判断
        if (!roleController.checkRole(addr, roleController.ROLE_AUTHORITY_ISSUER())) {
            return false;
        }
        //检查该地址映射的AuthorityIssuer数据结构的name是否为空，为空则返回false
        if (authorityIssuerMap[addr].attribBytes32[0] == bytes32(0)) {
            return false;
        }
        return true;
    }
```

```solidity
//添加新的授权机构
function addAuthorityIssuerFromAddress(
        address addr,
        bytes32[16] attribBytes32,
        int[16] attribInt,
        bytes accValue
    )
        public
        returns (uint)
    {
    	//addr对应的AuthorityIssuer结构中attribBytes32属性的首位不为0，则说明其authorityIssuer已经存在
    	//从中也可以发现，该结构中的Name属性十分重要
        if (authorityIssuerMap[addr].attribBytes32[0] != bytes32(0)) {
            return RETURN_CODE_FAILURE_ALREADY_EXISTS;
        }
        
        //判断使用的名字有没有重复
        if (isNameDuplicate(attribBytes32[0])) {
            return RETURN_CODE_NAME_ALREADY_EXISTS;
        }

        // Actual Role must be granted by calling recognizeAuthorityIssuer()
        // roleController.addRole(addr, roleController.ROLE_AUTHORITY_ISSUER());

		//生成新的authorityIssuer并加入队列，改变相应的属性
        AuthorityIssuer memory authorityIssuer = AuthorityIssuer(attribBytes32, attribInt, accValue);
        authorityIssuerMap[addr] = authorityIssuer;
        authorityIssuerArray.push(addr);
        uniqueNameMap[attribBytes32[0]] = addr;
        return RETURN_CODE_SUCCESS;
    }
    
```

```solidity
// 给授权机构添加相应权限（即认证）
   function recognizeAuthorityIssuer(address addr) public returns (uint) {
        if (!roleController.checkPermission(tx.origin, roleController.MODIFY_AUTHORITY_ISSUER())) {
            return roleController.RETURN_CODE_FAILURE_NO_PERMISSION();
        }
        if (authorityIssuerMap[addr].attribBytes32[0] == bytes32(0)) {
            return RETURN_CODE_FAILURE_NOT_EXIST;
        }
        // Set role and flag
        roleController.addRole(addr, roleController.ROLE_AUTHORITY_ISSUER());
        recognizedIssuerCount = recognizedIssuerCount + 1;
        authorityIssuerMap[addr].attribInt[15] = int(1);
        return RETURN_CODE_SUCCESS;
    }
```

```solidity
//删除授权机构的相应权限（即去认证）  
    function deRecognizeAuthorityIssuer(address addr) public returns (uint) {
        if (!roleController.checkPermission(tx.origin, roleController.MODIFY_AUTHORITY_ISSUER())) {
            return roleController.RETURN_CODE_FAILURE_NO_PERMISSION();
        }
        // Remove role and flag
        roleController.removeRole(addr, roleController.ROLE_AUTHORITY_ISSUER());
        recognizedIssuerCount = recognizedIssuerCount - 1;
        authorityIssuerMap[addr].attribInt[15] = int(0);
        return RETURN_CODE_SUCCESS;
    }
```

```solidity
//删除授权机构
  function deleteAuthorityIssuerFromAddress(
        address addr
    ) 
        public 
        returns (uint)
    {
        if (authorityIssuerMap[addr].attribBytes32[0] == bytes32(0)) {
            return RETURN_CODE_FAILURE_NOT_EXIST;
        }
        if (!roleController.checkPermission(tx.origin, roleController.MODIFY_AUTHORITY_ISSUER())) {
            return roleController.RETURN_CODE_FAILURE_NO_PERMISSION();
        }
        roleController.removeRole(addr, roleController.ROLE_AUTHORITY_ISSUER());
        
        if (authorityIssuerMap[addr].attribInt[15] == int(1)) {
            recognizedIssuerCount = recognizedIssuerCount - 1;
        }
        
        uniqueNameMap[authorityIssuerMap[addr].attribBytes32[0]] = address(0x0);
        delete authorityIssuerMap[addr];
        uint datasetLength = authorityIssuerArray.length;
        for (uint index = 0; index < datasetLength; index++) {
            if (authorityIssuerArray[index] == addr) { 
                break; 
            }
        } 
        if (index != datasetLength-1) {
            authorityIssuerArray[index] = authorityIssuerArray[datasetLength-1];
        }
        delete authorityIssuerArray[datasetLength-1];
        authorityIssuerArray.length--;
        return RETURN_CODE_SUCCESS;
    }
```



# CptData

## 背景

### Authority Issuer

通过上述权限设置可知，Authority Issuer的权限允许其发行授权CPT

### Claim Protocol Type（CPT）注册机制

Claim Protocol Type：凭证的声明类型

不同的Issuer按业务场景需要，各自定义不同类型数据结构的Claim，所有的Claim结构都需要到CPT合约注册，以保证全网唯一。所有的CPT定义文件（JSON-LD格式）可以从CPT合约下载。

![image-20221006110030563](https://pic-1306483575.cos.ap-nanjing.myqcloud.com/images/image-20221006110030563.png)

```
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
```

其中CPT为模板类，定义了Claim包含的数据字段及各字段属性要求。Claim为CPT的实例。Issuer将Claim进行签名，即可生成Credential。

还有一些例子可参考：https://weidentity.readthedocs.io/zh_CN/latest/docs/cpt-templates.html#cpt-templates

## 代码分析

### 功能概述

该合约实现了CPT的构建

### import

```
import "./AuthorityIssuerData.sol";
```

### 整体结构

```solidity
    // CPT ID has been categorized into 3 zones: 0 - 999 are reserved for system CPTs,
    //  1000-2000000 for Authority Issuer's CPTs, and the rest for common WeIdentiy DIDs.
    uint constant public AUTHORITY_ISSUER_START_ID = 1000;
    uint constant public NONE_AUTHORITY_ISSUER_START_ID = 2000000;
    uint private authority_issuer_current_id = 1000;
    uint private none_authority_issuer_current_id = 2000000;

    AuthorityIssuerData private authorityIssuerData;
    
    //签名
     struct Signature {
        uint8 v; 
        bytes32 r; 
        bytes32 s;
    }
    
    
	//CPT结构属性
    struct Cpt {
        //store the weid address of cpt publisher
        address publisher;
        // [0]: cpt version, [1]: created, [2]: updated, [3]: the CPT ID
        int[8] intArray;
        // [0]: desc
        bytes32[8] bytes32Array;
        //store json schema
        bytes32[128] jsonSchemaArray;
        //store signature
        Signature signature;
    }

    mapping (uint => Cpt) private cptMap;
    //用于存储CPT ID的List，由于不同的CPT ID根据不同的分类创建并不连续，这里的List起汇总所有ID的作用
    uint[] private cptIdList;

```

构造器：

```solidity
    function CptData(
        address authorityIssuerDataAddress
    ) 
        public
    {
        authorityIssuerData = AuthorityIssuerData(authorityIssuerDataAddress);
    }
```

 

一些Get函数：

* 根据CptId获取相应cpt以及相应属性值（与cpt结构一一对应）
  * `getCpt(uint cptId)`：根据cptId获取相应的Cpt
  * `getCptPublisher(uint cptId)`
  * `getCptIntArray(uint cptId)`
  * `getCptJsonSchemaArray(uint cptId)`
  * `getCptBytes32Array(uint cptId)`
  * `getCptSignature(uint cptId)`

* `isCptExist(uint cptId) `：判断是否存在此CptId

* `getDatasetLength()`：当前Cpt的总数
* `getCptIdFromIndex(uint index)`：通过索引值获取CptId
* `getCptId(address publisher)`



### 具体函数说明

```solidity
//输入所需属性构建相应的CPT
function putCpt(
        uint cptId, 
        address cptPublisher, 
        int[8] cptIntArray, 
        bytes32[8] cptBytes32Array,
        bytes32[128] cptJsonSchemaArray, 
        uint8 cptV, 
        bytes32 cptR, 
        bytes32 cptS
    ) 
        public 
        returns (bool) 
    {
    	//构建签名
        Signature memory cptSignature = Signature({v: cptV, r: cptR, s: cptS});
        //构建cpt
        cptMap[cptId] = Cpt({publisher: cptPublisher, intArray: cptIntArray, bytes32Array: cptBytes32Array, jsonSchemaArray:cptJsonSchemaArray, signature: cptSignature});
        cptIdList.push(cptId);
        return true;
    }
```

```solidity
// 分配cptid索引
function getCptId(address publisher)  public  constant returns (uint cptId)
    {
    	// 判断是否存是授权机构
        if (authorityIssuerData.isAuthorityIssuer(publisher)) {
            while (isCptExist(authority_issuer_current_id)) {
                authority_issuer_current_id++;
            }
            cptId = authority_issuer_current_id++;
            // 获得可用的cptid索引
            if (cptId >= NONE_AUTHORITY_ISSUER_START_ID) {
                cptId = 0;
            }
        } else {
        	// 不是授权机构则直接从2000000开始分配id
            while (isCptExist(none_authority_issuer_current_id)) {
                none_authority_issuer_current_id++;
            }
            cptId = none_authority_issuer_current_id++;
        }
    }

```





# CptController

## 功能概述

实现 cpt 的注册、查找与更新并进行记录

## import

```
import "./CptData.sol";
import "./WeIdContract.sol";
import "./RoleController.sol";
```

## 整体功能

```solidity
// Error codes
    uint constant private CPT_NOT_EXIST = 500301;
    uint constant private AUTHORITY_ISSUER_CPT_ID_EXCEED_MAX = 500302;
    uint constant private CPT_PUBLISHER_NOT_EXIST = 500303;
    uint constant private CPT_ALREADY_EXIST = 500304;
    uint constant private NO_PERMISSION = 500305;

    // 默认的 CPT 版本
    int constant private CPT_DEFAULT_VERSION = 1;

    WeIdContract private weIdContract;
    RoleController private roleController;

    // 为合约持有者预留
    address private internalRoleControllerAddress;
    address private owner;

    // CPT和Policy数据的存储地址
    address private cptDataStorageAddress;
    address private policyDataStorageAddress;
```

构造器：

```solidity
function CptController(
        address cptDataAddress,
        address weIdContractAddress
    ) 
        public
    {
        owner = msg.sender;
        weIdContract = WeIdContract(weIdContractAddress);
        cptDataStorageAddress = cptDataAddress;
    }
```

事件：

```solidity
event RegisterCptRetLog(
        uint retCode, 
        uint cptId, 
        int cptVersion
    );

    event UpdateCptRetLog(
        uint retCode, 
        uint cptId, 
        int cptVersion
    );
```



一些Get函数：

* getCptDynamicIntArray
* getCptDynamicBytes32Array
* getCptDynamicJsonSchemaArray
* getPolicyIdList
* getCptIdList
* getTotalCptId()
* getTotalPolicyId()

## 具体函数说明

```solidity
//设置policy数据合约地址
function setPolicyData(
        address policyDataAddress
    )
        public
    {
    	//如果合约调用者地址不为合约构建者或者其policyData为空，则直接返回
        if (msg.sender != owner || policyDataAddress == 0x0) {
            return;
        }
        否则的话将policyDataStorageAddress赋值为policyDataAddress
        policyDataStorageAddress = policyDataAddress;
    }
```

```solidity
//设置权限控制合约地址
function setRoleController(
        address roleControllerAddress
    )
        public
    {
        if (msg.sender != owner || roleControllerAddress == 0x0) {
            return;
        }
        roleController = RoleController(roleControllerAddress);
        if (roleController.ROLE_ADMIN() <= 0) {
            return;
        }
        internalRoleControllerAddress = roleControllerAddress;
    }
```

```solidity
//指定CPTID构建CPT并进行登记
function registerCptInner(
        uint cptId,
        address publisher, 
        int[8] intArray, 
        bytes32[8] bytes32Array,
        bytes32[128] jsonSchemaArray, 
        uint8 v, 
        bytes32 r, 
        bytes32 s,
        address dataStorageAddress
    )
        private
        returns (bool)
    {
    	//判断是否存在该DID
        if (!weIdContract.isIdentityExist(publisher)) {
        	//如果不存在就日志中记录“不存在”
            RegisterCptRetLog(CPT_PUBLISHER_NOT_EXIST, 0, 0);
            return false;
        }
        //从相应地址获取数据
        CptData cptData = CptData(dataStorageAddress);
        if (cptData.isCptExist(cptId)) {
        	//根据id判断是否存在该CPT，并打印日志
            RegisterCptRetLog(CPT_ALREADY_EXIST, cptId, 0);
            return false;
        }
		// 权限检查，我们在这里使用tx.origin进行相应操作。
		// 对于SDK调用，publisher和tx.origin通常是相同的，对于DApp调用，tx.origin应该规定
        uint lowId = cptData.AUTHORITY_ISSUER_START_ID();
        uint highId = cptData.NONE_AUTHORITY_ISSUER_START_ID();
        if (cptId < lowId) {
            // 委员会成员创建
            // 首先检查初始化
            if (internalRoleControllerAddress == 0x0) {
                RegisterCptRetLog(NO_PERMISSION, cptId, 0);
                return false;
            }
            // 检查权限
            if (!roleController.checkPermission(tx.origin, roleController.MODIFY_AUTHORITY_ISSUER())) {
                RegisterCptRetLog(NO_PERMISSION, cptId, 0);
                return false;
            }
        } else if (cptId < highId) {
            // 授权机构创建
            if (internalRoleControllerAddress == 0x0) {
                RegisterCptRetLog(NO_PERMISSION, cptId, 0);
                return false;
            }
            // 检查权限
            if (!roleController.checkPermission(tx.origin, roleController.MODIFY_KEY_CPT())) {
                RegisterCptRetLog(NO_PERMISSION, cptId, 0);
                return false;
            }
        }

        intArray[0] = CPT_DEFAULT_VERSION;
        
        //构建Cpt
        cptData.putCpt(cptId, publisher, intArray, bytes32Array, jsonSchemaArray, v, r, s);
        
        //日志记录
        RegisterCptRetLog(0, cptId, CPT_DEFAULT_VERSION);
        return true;
    }
```

```solidity
//cpt ID通过系统获取  
  function registerCptInner(
        address publisher, 
        int[8] intArray, 
        bytes32[8] bytes32Array,
        bytes32[128] jsonSchemaArray, 
        uint8 v, 
        bytes32 r, 
        bytes32 s,
        address dataStorageAddress
    ) 
        private 
        returns (bool) 
    {
        if (!weIdContract.isIdentityExist(publisher)) {
            RegisterCptRetLog(CPT_PUBLISHER_NOT_EXIST, 0, 0);
            return false;
        }
        CptData cptData = CptData(dataStorageAddress);
		//使用getCptId获得当前将符合条件的下一个id索引
        uint cptId = cptData.getCptId(publisher); 
        if (cptId == 0) {
            RegisterCptRetLog(AUTHORITY_ISSUER_CPT_ID_EXCEED_MAX, 0, 0);
            return false;
        }
        int cptVersion = CPT_DEFAULT_VERSION;
        intArray[0] = cptVersion;
        cptData.putCpt(cptId, publisher, intArray, bytes32Array, jsonSchemaArray, v, r, s);

        RegisterCptRetLog(0, cptId, cptVersion);
        return true;
    }

```

```solidity
//更新cpt
function updateCptInner(
        uint cptId, 
        address publisher, 
        int[8] intArray, 
        bytes32[8] bytes32Array,
        bytes32[128] jsonSchemaArray, 
        uint8 v, 
        bytes32 r, 
        bytes32 s,
        address dataStorageAddress
    ) 
        private 
        returns (bool) 
    {
    	// 判断是否存在该DID
        if (!weIdContract.isIdentityExist(publisher)) {
            UpdateCptRetLog(CPT_PUBLISHER_NOT_EXIST, 0, 0);
            return false;
        }
        // 获取数据
        CptData cptData = CptData(dataStorageAddress);
        // 检查权限
        if (!roleController.checkPermission(tx.origin, roleController.MODIFY_AUTHORITY_ISSUER())
            && publisher != cptData.getCptPublisher(cptId)) {
            UpdateCptRetLog(NO_PERMISSION, 0, 0);
            return false;
        }
        // 检查该CPT是否存在
        if (cptData.isCptExist(cptId)) {
            int[8] memory cptIntArray = cptData.getCptIntArray(cptId);
            // cpt版本更新
            int cptVersion = cptIntArray[0] + 1;
            intArray[0] = cptVersion;
            int created = cptIntArray[1];
            intArray[1] = created;
            // cpt更新并记录
            cptData.putCpt(cptId, publisher, intArray, bytes32Array, jsonSchemaArray, v, r, s);
            UpdateCptRetLog(0, cptId, cptVersion);
            return true;
        } else {
            UpdateCptRetLog(CPT_NOT_EXIST, 0, 0);
            return false;
        }
    }

```

```solidity
//查找功能
function queryCptInner(
        uint cptId,
        address dataStorageAddress
    ) 
        private 
        constant 
        returns (
        address publisher, 
        int[] intArray, 
        bytes32[] bytes32Array,
        bytes32[] jsonSchemaArray, 
        uint8 v, 
        bytes32 r, 
        bytes32 s)
    {
        CptData cptData = CptData(dataStorageAddress);
        publisher = cptData.getCptPublisher(cptId);
        intArray = getCptDynamicIntArray(cptId, dataStorageAddress);
        bytes32Array = getCptDynamicBytes32Array(cptId, dataStorageAddress);
        jsonSchemaArray = getCptDynamicJsonSchemaArray(cptId, dataStorageAddress);
        (v, r, s) = cptData.getCptSignature(cptId);
    }
```

凭证模板存储的相关函数

```solidity
    //credentialTemplateStored[cptId]：cptID创建时的区块数
    mapping (uint => uint) credentialTemplateStored;
    // 定义事件
    event CredentialTemplate(
        uint cptId,
        bytes credentialPublicKey,
        bytes credentialProof
    );
	// 添加模板
    function putCredentialTemplate(
        uint cptId,
        bytes credentialPublicKey,
        bytes credentialProof
    )
        public
    {
        CredentialTemplate(cptId, credentialPublicKey, credentialProof);
        credentialTemplateStored[cptId] = block.number;
    }
//根据id 获得模板对应区块数
    function getCredentialTemplateBlock(
        uint cptId
    )
        public
        constant
        returns(uint)
    {
        return credentialTemplateStored[cptId];
    }

    // --------------------------------------------------------
    // Claim Policy storage belonging to v.s. Presentation, Publisher WeID, and CPT
    
    //claimPoliciesFromPresentation[presentationClaimMapId]：其对应的Claim Policy ID list
    mapping (uint => uint[]) private claimPoliciesFromPresentation;
    mapping (uint => address) private claimPoliciesWeIdFromPresentation;
    //claimPoliciesFromCPT[cptId]：其对应的Claim Policy ID List
    mapping (uint => uint[]) private claimPoliciesFromCPT;

    uint private presentationClaimMapId = 1;

    function putClaimPoliciesIntoPresentationMap(uint[] uintArray) public {
        claimPoliciesFromPresentation[presentationClaimMapId] = uintArray;
        claimPoliciesWeIdFromPresentation[presentationClaimMapId] = msg.sender;
        RegisterCptRetLog(0, presentationClaimMapId, CPT_DEFAULT_VERSION);
        presentationClaimMapId ++;
    }
	// 根据presentationId返回相应映射
    function getClaimPoliciesFromPresentationMap(uint presentationId) public constant returns (uint[], address) {
        return (claimPoliciesFromPresentation[presentationId], 		              claimPoliciesWeIdFromPresentation[presentationId]);  
    }
    
    function putClaimPoliciesIntoCptMap(uint cptId, uint[] uintArray) public {
        claimPoliciesFromCPT[cptId] = uintArray;
        RegisterCptRetLog(0, cptId, CPT_DEFAULT_VERSION);
    }
    // 根据cptID获得映射
    function getClaimPoliciesFromCptMap(uint cptId) public constant returns (uint[]) {
        return claimPoliciesFromCPT[cptId];
    }
```





# Evidence相关合约

WeIdentity不仅提供了基于DID的公钥存储 + 数字签名用来防止凭证被篡改，同时也提供了Evidence存证功能，基于区块链不可篡改的特性，为创建出的凭证增信。简单来说，任何使用者，都可以将凭证的内容摘要上传到链上，以便在未来使用时可以根据链上内容比对，以防篡改。内容摘要使用Hash算法，抗逆向反推。其具体涉及以下三个合约，这里就不再展开：

* Evidence 
* EvidenceContract
* EvidenceFactory



# 其他类似合约

由于合约采用分层设计模式，即将合约分为逻辑合约、数据合约、及权限合约。其他合约与之类似这里简要提及。

## CommitteeMember

* CommitteeMemberController：与上述 AuthorityIssuerController 十分类似，实现CommitteeMember相关操作
* CommitteeMemberData：与上述 AuthorityIssuerData 十分类似，实现CommitteeMember数据的添加、删除操作

## SpecificIssuer（Issuer链上类型声明）

WeIdentity支持为每位Authority Issuer在链上声明所属类型，即Specific Issuer。您可以指定某位Authority Issuer的具体类型属性，如学校、政府机构、医院等。当前，此属性与其对应的权限没有直接关系，仅作记录之目的。

* SpecificIssuerController
* SpecificIssuerData
