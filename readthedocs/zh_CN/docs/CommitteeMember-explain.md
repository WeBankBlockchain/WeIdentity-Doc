# CommitteeMember合约解析

委员会管理合约分为2个，分别是CommitteeMemberController和CommitteeMemberData，根据依赖关系，可以看出CommitteeMemberData是CommitteeMemberController的数据部分，两个文件也都引用了RoleController合约。

## CommitteeMemberData合约

先来看看CommitteeMemberData文件，它的核心操作是通过角色权限管理来维护一个委员会成员数组。合约开始部分，先定义了3个返回码用于不同状态下的返回值描述，分别对应成功、已经存在和不存在。

```js
		uint constant private RETURN_CODE_SUCCESS = 0;
    uint constant private RETURN_CODE_FAILURE_ALREADY_EXISTS = 500251;
    uint constant private RETURN_CODE_FAILURE_NOT_EXIST = 500252;
```

### 构造函数

接下来是数据结构定义和构造函数，该合约负责委员会成员管理，因此它使用了一个address类型的动态数组（committeeMemberArray）来维护委员会成员。roleController则是RoleController的对象（注意区分大小写），构造函数需要传入已经部署的RoleController地址，并将其赋值给roleController，后面就可以通过roleController对象来使用角色权限的管理功能了。

```js
		address[] private committeeMemberArray;
    RoleController private roleController;

    function CommitteeMemberData(address addr) public {
        roleController = RoleController(addr);
    }
```

除此之外，CommitteeMemberData合约还提供了5个函数，功能如下：

- isCommitteeMember  检查某地址是否为委员会成员
- addCommitteeMemberFromAddress  添加地址到委员会
- deleteCommitteeMemberFromAddress  删除委员会中的地址
- getDatasetLength  获取委员会的人数
- getCommitteeMemberAddressFromIndex  通过索引获取成员地址

接下来，再具体分析一下各个函数。

### isCommitteeMember

***isCommitteeMember***用来检查某个地址是否为委员会成员，由于所有的委员会成员都在一个动态数组中，因此只需要判断该地址是否和数组中的某个地址相等就可以。代码如下。

```js
    function isCommitteeMember(
        address addr
    ) 
        public 
        constant 
        returns (bool) 
    {
        // Use LOCAL ARRAY INDEX here, not the RoleController data.
        // The latter one might lose track in the fresh-deploy or upgrade case.
        for (uint index = 0; index < committeeMemberArray.length; index++) {
            if (committeeMemberArray[index] == addr) {
                return true;
            }
        }
        return false;
    }
```

### addCommitteeMemberFromAddress

***addCommitteeMemberFromAddress***负责向数组内添加成员，在添加前它做了2个检测，第1个是该地址是否已经是成员，如果已经是了则返回已经存在，第2个是检查操作者的权限，根据RoleController合约的介绍，该操作者应该具备MODIFY_COMMITTEE权限（在RoleController合约内定义了该权限对应的判断方式），如果不具备，那么将返回没有权限。如果权限检测通过了，则通过roleController调用addRole函数来添加委员会成员，同时也会在将该地址追加到委员会数组地址中。

```js
    function addCommitteeMemberFromAddress(
        address addr
    ) 
        public
        returns (uint)
    {
        if (isCommitteeMember(addr)) {
            return RETURN_CODE_FAILURE_ALREADY_EXISTS;
        }
        if (!roleController.checkPermission(tx.origin, roleController.MODIFY_COMMITTEE())) {
            return roleController.RETURN_CODE_FAILURE_NO_PERMISSION();
        }
        roleController.addRole(addr, roleController.ROLE_COMMITTEE());
        committeeMemberArray.push(addr);
        return RETURN_CODE_SUCCESS;
    }
```

### deleteCommitteeMemberFromAddress

***deleteCommitteeMemberFromAddress***负责删除委员会成员，它的操作逻辑与添加类似，先要判断该地址是否为委员会成员，之后再检查执行者是否具备MODIFY_COMMITTEE权限。具体执行删除时同样是做2个动作，先通过roleController对象来删除委员会成员，再把该地址在动态数组中删掉。为了避免数组内出现空的情况，删除操作的时候通常要把目标地址置换到数组的末尾，然后使用delete语句来删除数组最后一个元素，所以下面的代码用循环语句完成了该操作。0.4版本的编译器还需要自行维护数组的个数，新版编译器已经不需要此操作。

```js
    function deleteCommitteeMemberFromAddress(
        address addr
    ) 
        public
        returns (uint)
    {
        if (!isCommitteeMember(addr)) {
            return RETURN_CODE_FAILURE_NOT_EXIST;
        }
        if (!roleController.checkPermission(tx.origin, roleController.MODIFY_COMMITTEE())) {
            return roleController.RETURN_CODE_FAILURE_NO_PERMISSION();
        }
        roleController.removeRole(addr, roleController.ROLE_COMMITTEE());
        uint datasetLength = committeeMemberArray.length;
        for (uint index = 0; index < datasetLength; index++) {
            if (committeeMemberArray[index] == addr) {break;}
        }
        if (index != datasetLength-1) {
            committeeMemberArray[index] = committeeMemberArray[datasetLength-1];
        }
        delete committeeMemberArray[datasetLength-1];
        committeeMemberArray.length--;
        return RETURN_CODE_SUCCESS;
    }
```

### getDatasetLength

***getDatasetLength***用于获取委员会数组的长度，直接返回动态数组的length就可以了。

```js
    function getDatasetLength() 
        public 
        constant 
        returns (uint) 
    {
        return committeeMemberArray.length;
    }
```

### getCommitteeMemberAddressFromIndex

***getCommitteeMemberAddressFromIndex***用于返回某个index对应的委员会成员，直接通过数组下标返回元素就可以了。这种带index参数用以返回数组成员的函数通常会作为外部调用合约的数据查询接口。

```js
    function getCommitteeMemberAddressFromIndex(
        uint index
    ) 
        public 
        constant 
        returns (address) 
    {
        return committeeMemberArray[index];
    }
```

## CommitteeMemberController合约

再来看看CommitteeMemberController合约。它直接引用了CommitteeMemberData和RoleController，这也意味着它要通过上述2个合约完成相应的功能。

首先是数据定义部分，CommitteeMemberController合约内部定义了committeeMemberData和roleController，在动作类型常量方面定义了OPERATION_ADD和OPERATION_REMOVE，另外还有一个CommitteeRetLog事件用来记录委员会的操作日志。

```js
    CommitteeMemberData private committeeMemberData;
    RoleController private roleController;

    // Event structure to store tx records
    uint constant private OPERATION_ADD = 0;
    uint constant private OPERATION_REMOVE = 1;
    
    event CommitteeRetLog(uint operation, uint retCode, address addr);
```

### 构造函数

构造函数需要2个参数，分别是委员会成员数据合约的地址和角色管理合约的地址，也就是引用的2个合约，在构造函数内部通过这2个地址对之前定义的committeeMemberData和roleController两个对象进行初始化。

```js
    // Constructor.
    function CommitteeMemberController(
        address committeeMemberDataAddress,
        address roleControllerAddress
    )
        public 
    {
        committeeMemberData = CommitteeMemberData(committeeMemberDataAddress);
        roleController = RoleController(roleControllerAddress);
    }
```

构造函数之外，还有4个函数，分布是：

- addCommitteeMember  添加委员会成员
- removeCommitteeMember  删除委员会成员
- getAllCommitteeMemberAddress  获取全部委员会成员
- isCommitteeMember  是否是为委员会成员

***addCommitteeMember***用来添加委员会成员，它的内部会先检测执行者是否具备委员会的操作权限，不具备则响应CommitteeRetLog无权限的事件，通过检查后，则是通过committeeMemberData对象调用其内部的添加成员函数addCommitteeMemberFromAddress来完成添加，操作完成后同样会触发委员会添加的事件。

```js
    function addCommitteeMember(
        address addr
    ) 
        public 
    {
        if (!roleController.checkPermission(tx.origin, roleController.MODIFY_COMMITTEE())) {
            CommitteeRetLog(OPERATION_ADD, roleController.RETURN_CODE_FAILURE_NO_PERMISSION(), addr);
            return;
        }
        uint result = committeeMemberData.addCommitteeMemberFromAddress(addr);
        CommitteeRetLog(OPERATION_ADD, result, addr);
    }
```

### removeCommitteeMember

***removeCommitteeMember***用来移除委员会的成员，它的逻辑和添加是类似的，同样先检查权限，然后是调用committeeMemberData的deleteCommitteeMemberFromAddress函数来删除委员会成员，不管成功还是失败都会有事件响应。

```js
    function removeCommitteeMember(
        address addr
    ) 
        public 
    {
        if (!roleController.checkPermission(tx.origin, roleController.MODIFY_COMMITTEE())) {
            CommitteeRetLog(OPERATION_REMOVE, roleController.RETURN_CODE_FAILURE_NO_PERMISSION(), addr);
            return;
        }
        uint result = committeeMemberData.deleteCommitteeMemberFromAddress(addr);
        CommitteeRetLog(OPERATION_REMOVE, result, addr);
    }
```

### getAllCommitteeMemberAddress

***getAllCommitteeMemberAddress***作用是返回全部委员会的成员，它先从committeeMemberData合约内拿到动态数组的长度，然后再将committeeMemberData合约内的数组元素添加到一个局部数组中，最终返回这个局部数组。在这里，我们可以看到getCommitteeMemberAddressFromIndex函数的使用方式，也就是前文提到的作为外部合约调用的接口以获取数据。

```js
    function getAllCommitteeMemberAddress() 
        public 
        constant 
        returns (address[]) 
    {
        // Per-index access
        uint datasetLength = committeeMemberData.getDatasetLength();
        address[] memory memberArray = new address[](datasetLength);
        for (uint index = 0; index < datasetLength; index++) {
            memberArray[index] = committeeMemberData.getCommitteeMemberAddressFromIndex(index);
        }
        return memberArray;
    }
```

### isCommitteeMember

***isCommitteeMember***用来判断一个地址是否为委员会成员，它直接调用committeeMemberData的isCommitteeMember函数来判断即可。

```js
    function isCommitteeMember(
        address addr
    ) 
        public 
        constant 
        returns (bool) 
    {
        return committeeMemberData.isCommitteeMember(addr);
    }
```



