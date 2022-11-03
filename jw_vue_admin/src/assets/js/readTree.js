
/**
 * 广度优先遍历森林，并选出符合type类型的值
 * (后端把菜单列表构建成菜单树，前端把菜单树还原成菜单列表，属于是脱裤子放屁了 ε=ε=ε=(~￣▽￣)~)
 * @param forest
 * @param type
 * @returns {*[]}
 */
function readForestBFS(forest=[], type1='', type2=''){
    let returnList = [];
    forest.forEach(tree =>{
        let queue = [tree];
        while(queue.length > 0){
            let top_node = queue.shift()
            if(top_node.menuType === type1 || top_node.menuType === type2) returnList.push(top_node)
            top_node.children.forEach(child =>{
                queue.push(child)
            })
        }
    })
    return returnList
}
//列表转化成数
function listToTree(list) {
    // * 先生成parent建立父子关系
    const obj = {};
    list.forEach((item) => {
        obj[item.id] = item
    });
    // * obj -> {1001: {id: 1001, parentId: 0, name: 'AA'}, 1002: {...}}
    // console.log(obj, "obj")
    const parentList = [];
    list.forEach((item) => {
        const parent = obj[item.parentId];
        if (parent) {
            // * 当前项有父节点
            parent.children = parent.children || [];
            parent.children.push(item);
        } else {
            // * 当前项没有父节点 -> 顶层
            parentList.push(item);
        }
    });
    return parentList;
}

//将菜单树转变成菜单select选择框
function menuListToMenuOptions(menuTree){
    // console.log(menuTree)
    let menuList = readForestBFS(menuTree,'directory','menu')
    //将menuList格式化成select选项格式
    let optionList=[]
    let rootOption = {id: 0, label:'JW后台管理系统', parentId:'',}
    optionList.push(rootOption)
    menuList.forEach((item) => {
        let option = {
            id:'',
            label:'',
            parentId:'',
        }
        option.id = item.id
        option.label = item.menuName
        option.parentId = item.parentId
        optionList.push(option)
    })
    return listToTree(optionList)
}

export {readForestBFS, menuListToMenuOptions}
