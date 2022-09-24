#!/usr/bin/env python
# -*- coding: utf-8 -*-
# @Time    : 2022/4/20 9:41
# @Author  : jingwen
# @File    : ApiResponse.py
# @Desc:   : socket返回类

"""


"""


class ApiResponse(object):
    def __init__(self, code, message, data={}):
        super(ApiResponse, self).__init__()
        self.code = code
        self.message = message
        self.data = data

    def succeed(self, message, data):
        self.code = 200
        self.message = message
        self.data = data

    # 将ApiResopnse类转换成dict，方便封装成json数据
    def apiResopnseToDict(stf):
        return {
            'code': stf.code,
            'message': stf.message,
            'data': stf.data
        }
