

// 网站名称非空验证
export function validateUrlName(rule, value, callback){
    if (!value) {
        return callback(new Error('请填写此字段~'))
    } else {
        callback()
    }
}

// // 网址正则
const urlGrep = new RegExp('^((https|http|)?://)' +
    "+(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" + '(([0-9]{1,3}\\.){3}[0-9]{1,3}' + '|' +
    "([0-9a-z_!~*'()-]+\\.)*" + '([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\.' + '[a-z]{2,6})' +
    '(:[0-9]{1,5})?' + '((/?)|' + "(/[0-9a-zA-Z_!~*'().;?:@&=+$,%#-]+)+/?)$");
// 网站地址非空验证
export function validateUrlAddress(rule, value, callback){
    if (!value) {
        return callback(new Error('请填写此字段~'))
    } else {
        if (!urlGrep.test(value)) {
            callback(new Error('请按照URL标准格式填写，必须包含http/https'));
        } else {
            callback()
        }
    }
}


// 网站简介非空验证
export function validateUrlReferral(rule, value, callback){
    if (!value) {
        return callback(new Error('请填写此字段~'))
    } else {
        callback()
    }
}


// 网站类型非空验证
export function validateUrlType(rule, value, callback){
    if (!value) {
        return callback(new Error('请填写此字段~'))
    } else {
        callback()
    }
}

// 图片是否上传
export function validateUrlLitimg(rule, value, callback){
    if (!this.urlLitimg || !this.imageUrl) {
        return callback(new Error('网站缩略图未上传成功，上传后再添加哟~'))
    } else {
        callback()
    }
}


// 邮箱正则表达式判断
const emailGrep = /^\w+@[\da-z\.-]+\.([a-z]{2,6}|[\u2E80-\u9FFF]{2,3})$/;
// 站长邮箱验证
export function validateWebmasterEmail(rule, value, callback){
    if (!value) {
        return callback(new Error('请填写此字段~'))
    } else {
        if (!emailGrep.test(value)) {
            callback(new Error('请按照正确的邮箱格式输入'));
        } else {
            callback()
        }
    }
}

