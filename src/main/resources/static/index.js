var vm = new Vue({
    el: '#vm',
    data: {
        tableData: [],
        add: {
            userId: '',
            userCode: '',
            department: '',
            age: '',
            mail: '',
            phone: ''
        },
        update: {
            userId: '',
            userCode: '',
            department: '',
            age: '',
            mail: '',
            phone: ''
        },
        user: {},
        filter: {
            phone: '',
            age: '',
            mail: '',
            userCode: '',
            start: 1,
            pageSize: 5,
            total: 0
        },
        deleteIds: {},
        operation: null,
        rules: {
            userCode: [
                {required: true, message: '请输入编码', trigger: 'blur'},
                {min: 3, max: 15, message: '长度在 3 到 15 个字符'}
            ],
            mail: [
                {required: true, type: 'email', message: '邮箱格式不正确', trigger: 'blur'}
            ],
            phone: [
                {required: true, message: '电话不能为空'},
                {pattern: /^1[34578]\d{9}$/, message: '请输入中国国内的手机号码', trigger: 'blur'}
            ],
            age: [
                {required: true, message: '请输入年龄'},
                {
                    validator: (rule, value, callback) = > {
                    if(!value)
{
    return callback(new Error('年龄不能为空'));
}
setTimeout(() = > {
    if(
!Number.isInteger(value)
)
{
    callback(new Error('请输入数字值'));
}
else
{
    if (value < 18) {
        callback(new Error('必须年满18岁'));
    } else if (value > 100) {
        callback(new Error('必须小于100岁'));
    } else {
        callback();
    }
}
},
1000
)
;
},
trigger: 'blur'
}
],
department: [
    {required: true, message: '请选择部门', trigger: 'change'}
]
},
dialogCreateVisible: false,
    dialogEditVisible
:
false,
    dialogInfoVisible
:
false,
    createLoading
:
false,
    editLoading
:
false,
    infoLoading
:
false,
},
mounted: function () {
    this.getUsers();
}
,
methods: {
    selectionChange: function (val) {
        this.deletedIds = val;
    }
,
    changeSize: function (val) {
        this.filter.pageSize = val;
        this.getUsers();
    }
,
    changeCurrentPage: function (val) {
        this.filter.start = val;
        this.getUsers();
    }
,
    getUsers: function () {
        var user = {};

        this.operation = 'getList';
        this.$http.get("/user/" + this.operation, {params: params_}).then(function (response) {
            var data = response.body;
            this.filter.total = data.total;
            this.tableData = [];
            for (var key in data.data) {
                this.$set(this.tableData, key, data.data[key]);
            }
        }).catch(function (response) {
            console.error(response);
        });
    }
,
    formatDate: getNowFormatDate
        , formatCreateDate
:
    formatCreateDate
        , formatSex
:
    formatSex
        , addUser
:

    function () {
        this.operation = 'add';
        this.dialogCreateVisible = true;
    }

,
    editUser: function (user) {
        this.operation = 'update';
        for (var key in user) {
            this.update[key] = user[key];
        }
        this.dialogEditVisible = true;
    }
,
    deleteUser: function (user) {
        this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
        }).then(() = > {
            this.$http.post("/user/delete", {userId: user.userId}, {emulateJSON: true}).then((response) = > {
            this.$message({
            type: 'success',
            message: '删除成功!'
        });
        this.getUsers();
    }).
        catch((response) = > {
            this.$message({
            type: 'info',
            message: '操作失败'
        });
    })
        ;
    }).
        catch(() = > {
            this.$message({
            type: 'info',
            message: '已取消删除'
        });
    })
        ;
    }
,
    deleteUsers: function () {
        if (this.deletedIds.length < 1) {
            this.$message.error("请至少选择一条");
            return;
        }
        this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
        }).then(() = > {
            var ids = [];
        for (var id in this.deletedIds) {
            ids.push(this.deletedIds[id].userId);
        }
        this.$http.post("/user/deleteUsers", {userIds: ids.join(',')}, {emulateJSON: true}).then((response) = > {
            this.$message.success("删除成功");
        this.getUsers();
    }).
        catch((response) = > {
            this.$message.error("删除失败");
    })
        ;
    }).
        catch(() = > {
            this.$message({
            type: 'info',
            message: '已取消删除'
        });
    })
        ;
    }
,
    saveUser: function () {
        this.$refs[this.operation].validate((valid) = > {
            if(valid) {
                if (this.operation == 'add')
                    this.createLoading = true;
                else if (this.operation == 'update')
                    this.editLoading = true;
                this.$http.post("/user/" + this.operation, JSON.stringify(this.$data[this.operation])
                ).then((response) = > {
                    var data = response.body;
                if (this.operation == 'add') {
                    if (data.status == false) {
                        this.$message.error(data.message);
                        this.dialogCreateReset()
                        return;
                    }
                    this.$message.success('新增用户资料成功！');
                    this.dialogCreateReset()
                }
                else if (this.operation == 'update') {
                    if (data.status == false) {
                        this.$message.error(data.message);
                        this.dialogUpdateReset()
                        return;
                    }
                    this.$message.success('修改用户资料成功！');
                    this.dialogUpdateReset();
                }
                this.getUsers();
            }).
                catch((response) = > {
                    var data = response.data;
                if (data instanceof Array) {
                    this.$message.error(data[0]["message"]);
                }
                else if (data instanceof Object) {
                    this.$message.error(data["message"]);
                }
                if (this.operation == 'create')
                    this.saveLoading = false;
                if (this.operation == 'update')
                    this.editLoading = false;
            })
                ;
            } else {
                return false;
    }
    })
        ;
    }
,
    info: function (user) {
        this.dialogInfoVisible = true;
        this.user = user;
    }
,
    reset: function (ref) {
        this.$refs[ref].resetFields();
    }
,
    dialogCreateReset: function () {
        this.dialogCreateVisible = false;
        this.createLoading = false;
        this.reset('add');
    }
,
    dialogUpdateReset: function () {
        this.dialogEditVisible = false;
        this.editLoading = false;
        this.reset('update');
    }
}
})
;