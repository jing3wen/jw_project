import request from "@/utils/request";

export function PageList(data){
    return request.post('/api/sysUser/getPageList',data)
}
