# RoleController合约解析

在WeIdentity合约工程中，RoleController合约文件是被引用次数最多，这也说明它是整个工程的基础。它的总体思想是实现「角色—操作」这样的二元权限控制，也理解成不同的角色具备不同的操作权限。

如果有读者喜欢通过视频的方式学习代码讲解，也可以点击下方的链接。

配套视频讲解：[WeID合约代码讲解](https://www.bilibili.com/video/BV1R8411x7DM/?spm_id_from=333.337.search-card.all.click&vd_source=6874e63006b329860f78832e8a773416)

## 常量定义

理解了RoleController的设计思路，就不难理解在合约中定义的2组常量。ROLE_AUTHORITY_ISSUER、ROLE_COMMITTEE、ROLE_ADMIN代表了3类角色，分布是权威发布者、委员会成员、管理员，MODIFY_AUTHORITY_ISSUER、MODIFY_COMMITTEE、MODIFY_ADMIN、MODIFY_KEY_CPT代表了4类操作权限，在业务上可以设置不同的角色具备不同的权限。

```javascript
		/**
     * Role related Constants.
     */
    uint constant public ROLE_AUTHORITY_ISSUER = 100;
    uint constant public ROLE_COMMITTEE = 101;
    uint constant public ROLE_ADMIN = 102;

    /**
     * Operation related Constants.
     */
    uint constant public MODIFY_AUTHORITY_ISSUER = 200;
    uint constant public MODIFY_COMMITTEE = 201;
    uint constant public MODIFY_ADMIN = 202;
    uint constant public MODIFY_KEY_CPT = 203;
```

## 构造函数

在0.4.4版编译器的合约中，构造函数与合约同名，而不是使用constructor。RoleController合约的构造函数主要是针对三类角色地址进行初始化。3个mapping结构中authorityIssuerRoleBearer对应发布者的角色，committeeMemberRoleBearer对应委员会成员的角色，adminRoleBearer对应管理员的角色，在构造函数初始化时，合约发布者自动成为上述3种角色。

```js
    mapping (address => bool) private authorityIssuerRoleBearer;
    mapping (address => bool) private committeeMemberRoleBearer;
    mapping (address => bool) private adminRoleBearer;

    function RoleController() public {
        authorityIssuerRoleBearer[msg.sender] = true;
        adminRoleBearer[msg.sender] = true;
        committeeMemberRoleBearer[msg.sender] = true;
    }
```

除了构造函数，RoleController合约一共只有4个函数，分布对应的功能是：

- checkPermission 检查角色地址与权限是否对应
- addRole  添加角色
- removeRole  删除角色
- checkRole 检查角色地址与角色是否对应

## checkPermission

***checkPermission***函数的作用是用来检查权限，它的作用是检查某个地址是否具备某个权限。MODIFY_AUTHORITY_ISSUER操作需要的权限是管理员或委员会成员，所以它需要检测给定的地址是否在adminRoleBearer或committeeMemberRoleBearer中。MODIFY_COMMITTEE代表修改委员会成员的动作，这个动作要求操作者必须是管理员，MODIFY_ADMIN代表修改管理员，这个动作同样要求操作者必须是管理员，所以都用adminRoleBearer来检测。MODIFY_KEY_CPT动作是修改CPT，这个权限要求执行者只要是发布者就可以了。

```js
		/**
     * Public common checkPermission logic.
     */
    function checkPermission(
        address addr,
        uint operation
    ) 
        public 
        constant 
        returns (bool) 
    {
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

## addRole

***addRole***函数负责添加角色，需要指定一个地址和角色类型，在添加的时候需要检查执行者的角色权限是否符合条件，这刚好可以通过checkPermission来完成。当添加的角色是发行者时，MODIFY_AUTHORITY_ISSUER就是要检查的权限；当添加的角色是委员会成员时，需要检查的权限是MODIFY_COMMITTEE；当添加的角色是管理员时，需要检查的权限是MODIFY_ADMIN。特别注意的是，在做权限检查时，调用checkPermission函数时传递的参数是tx.origin，这代表了最原始的调用方。

```js
    /**
     * Add Role.
     */
    function addRole(
        address addr,
        uint role
    ) 
        public 
    {
        if (role == ROLE_AUTHORITY_ISSUER) {
            if (checkPermission(tx.origin, MODIFY_AUTHORITY_ISSUER)) {
                authorityIssuerRoleBearer[addr] = true;
            }
        }
        if (role == ROLE_COMMITTEE) {
            if (checkPermission(tx.origin, MODIFY_COMMITTEE)) {
                committeeMemberRoleBearer[addr] = true;
            }
        }
        if (role == ROLE_ADMIN) {
            if (checkPermission(tx.origin, MODIFY_ADMIN)) {
                adminRoleBearer[addr] = true;
            }
        }
    }
```

## removeRole

***removeRole***函数负责删除角色，删除时同样要确定地址以及要删除的角色类型。它的实现思路和前面的addRole函数没有太大区别，只是权限检测通过后，设置mapping里的值为false。

```js
    /**
     * Remove Role.
     */
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

## checkRole

***checkRole***函数的作用是检查某个地址是否对应某个角色，它的操作就是在对应角色权限的mapping中判断地址是否存在就可以了。这个函数与checkPermission函数有一定的功能重合，但二者还是有区别的。checkPermission检查的是操作权限，例如管理员和委员会成员都可以增加和删除发发布者，checkRole函数则只是检查一个地址是不是管理员、委员会成员或发发布者。

```js
    /**
     * Check Role.
     */
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




