
//导入别人的api接口
const live2d_path = "https://api.itggg.cn/live2dnew/left/";

function loadExternalResource(url, type) {
  return new Promise((resolve, reject) => {
    let tag;
    if (type === "css") {
      tag = document.createElement("link");
      tag.rel = "stylesheet";
      tag.href = url
    } else if (type === "js") {
      tag = document.createElement("script");
      tag.src = url
    }
    if (tag) {
      tag.onload = () => resolve(url);
      tag.onerror = () => reject(url);
      document.head.appendChild(tag)
    }
  })
}

export function openKanBan(){
  //看板娘加载指定模型
  localStorage.setItem('modelId', '2');
  localStorage.setItem('modelTexturesId', '1');

  if (screen.width >= 768) {
    Promise.all([loadExternalResource(live2d_path + "waifu.min.css", "css"),
      loadExternalResource(live2d_path + "live2d.min.js", "js"),
      loadExternalResource(live2d_path + "waifu-tips.min.js", "js")]).then(() => {
      initWidget({waifuPath: live2d_path + "waifu-tips.json", apiPath: "https://api.itggg.cn/live2d_api/",})
    })
  }
}



