var clone = function clone(obj) {
    var o, i, j, k;
    if (typeof(obj) != "object" || obj === null) return obj;
    if (obj instanceof (Array)) {
        o = [];
        i = 0;
        j = obj.length;
        for (; i < j; i++) {
            if (typeof(obj[i]) == "object" && obj[i] != null) {
                o[i] = arguments.callee(obj[i]);
            }
            else {
                o[i] = obj[i];
            }
        }
    }
    else {
        o = {};
        for (i in obj) {
            if (typeof(obj[i]) == "object" && obj[i] != null) {
                o[i] = arguments.callee(obj[i]);
            }
            else {
                o[i] = obj[i];
            }
        }
    }

    return o;
};
var getNowFormatDate = function (time) {
    var date = new Date(time);
    var seperator1 = "-";
    var seperator2 = ":";
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate + " " + date.getHours() + seperator2 + date.getMinutes() + seperator2 + date.getSeconds();
    return currentdate;
}

var formatCreateDate = function (row, column) {
    if (row.createTime != null) {
        return this.formatDate(row.createTime);
    } else {
        return '';
    }
}

var formatSex = function (row, column) {
    if (row.sex != null) {
        return row.sex == 1 ? '男' : '女';
    }
}