//导入别人的js文件
const live2d_path = "https://fastly.jsdelivr.net/gh/stevenjoezhang/live2d-widget@latest/autoload.js"

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
  localStorage.setItem('modelId', '4');
  localStorage.setItem('modelTexturesId', '1');

  if (screen.width >= 768) {
    //采用第三方库动态导入看板娘文件
    loadExternalResource(live2d_path, "js").then(r => {

    })
  }
}
