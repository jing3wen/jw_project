export default {
  baseURL: "http://localhost:8081",
  imBaseURL: "http://localhost:81",
  webURL: "http://localhost",

  //验证码类型
  registerUserCodeType : "register_user",
  forgetPasswordCodeType : "user_forget_password",
  updateUserBindCodeType : "update_user_bind",

  /*
   * 用户注册类型
   * 注意!!!该数据要和数据库的sys_role表的role_code对应
   */
  register_type:"BLOG_REGISTER_ROLE",

  live2d_path: "https://cdn.jsdelivr.net/gh/stevenjoezhang/live2d-widget@latest/",
  cdnPath: "https://cdn.jsdelivr.net/gh/fghrsh/live2d_api/",
  waifuPath: "/blogWeb/front/getWaifuJson",
  hitokoto: "https://v1.hitokoto.cn",
  shehui: "https://api.oick.cn/yulu/api.php",
  tocbot: "https://cdnjs.cloudflare.com/ajax/libs/tocbot/4.11.1/tocbot.min.js",
  jinrishici: "https://v1.jinrishici.com/all.json",

  //前后端定义的密钥，AES使用16位
  cryptojs_key: "areyouokareyouok",

  friendLetterTop: "https://cdn.cbd.int/hexo-butterfly-envelope/lib/before.png",
  friendLetterBottom: "https://cdn.cbd.int/hexo-butterfly-envelope/lib/after.png",
  friendLetterBiLi: "https://cdn.cbd.int/hexo-butterfly-envelope/lib/line.png",
  friendLetterMiddle: "https://cdn.cbd.int//hexo-butterfly-envelope/lib/violet.jpg",

  before_color_list: ["#ff4b2b", "#EF794F", "#67C23A", "orange", "rgb(131, 123, 199)", "#23d5ab"],

  tree_hole_color: ["#ee7752", "#e73c7e", "#23a6d5", "#23d5ab", "rgb(131, 123, 199)", "#23d5ab"],

  before_color_1: "black",
  after_color_1: "linear-gradient(45deg, #f43f3b, #ec008c)",

  before_color_2: "rgb(131, 123, 199)",
  after_color_2: "linear-gradient(45deg, #f43f3b, #ec008c)",

  pageColor: "#ee7752",
  commentPageColor: "#23d5ab",
  userId: 1,
  source: 0,

  //表情包
  emojiTypeList:[
    {
      label:'默认',
      name:'default',
      suffix:".gif",
      list: ['衰', '鄙视', '再见', '捂嘴', '摸鱼', '奋斗', '白眼', '可怜', '皱眉', '鼓掌',
        '烦恼', '吐舌', '挖鼻', '委屈', '滑稽', '啊这', '生气', '害羞', '晕', '好色',
        '流泪', '吐血', '微笑', '酷', '坏笑', '吓', '大兵', '哭笑', '困', '呲牙'],
    },
    {
      label:'皮皮虾',
      name:'ppx',
      suffix:".png",
      list: ['102@3x', '103@3x', '104@3x', '107@3x', '108@3x', '109@3x', '10@3x', '110@3x', '111@3x', '112@3x',
        '113@3x', '114@3x', '11@3x', '12@3x', '13@3x', '14@3x', '15@3x', '16@3x', '17@3x', '18@3x',
        '19@3x', '1@3x', '20@3x', '21@3x', '22@3x', '23@3x', '24@3x', '25@3x', '26@3x', '27@3x',
        '28@3x', '29@3x', '2@3x', '30@3x', '31@3x', '32@3x', '33@3x', '34@3x', '35@3x', '36@3x',
        '37@3x', '38@3x', '39@3x', '3@3x', '40@3x', '41@3x', '42@3x', '43@3x', '44@3x', '46@3x',
        '47@3x', '48@3x', '49@3x', '4@3x', '50@3x', '51@3x', '52@3x', '53@3x', '54@3x', '55@3x',
        '57@3x', '58@3x', '59@3x', '5@3x', '60@3x', '61@3x', '62@3x', '63@3x', '64@3x', '65@3x',
        '66@3x', '67@3x', '69@3x', '6@3x', '72@3x', '74@3x', '76@3x', '77@3x', '78@3x', '79@3x',
        '7@3x', '80@3x', '81@3x', '82@3x', '85@3x', '86@3x', '87@3x', '8@3x', '90@3x', '92@3x',
        '93@3x', '94@3x', '95@3x', '96@3x', '97@3x', '98@3x', '99@3x', '9@3x', 'all@3x', 'ba@3x',
        'bainian@3x', 'bianpao@3x', 'bingdu@3x', 'bird@3x', 'bulls@3x', 'caihong@3x', 'chanche@3x', 'chepiao@3x', 'chick@3x', 'chinamoney@3x',
        'drive@3x', 'drug@3x', 'eagle@3x', 'egg@3x', 'eye@3x', 'facai@3x', 'feiji@3x', 'fengzheng@3x', 'flower@3x', 'fu@3x',
        'gouliang@3x', 'gouwuche@3x', 'greenhat@3x', 'gu@3x', 'guazi@3x', 'guihua@3x', 'hongbao@3x', 'horse@3x', 'huadeng@3x', 'jiaozi@3x',
        'jiayou@3x', 'jiu@3x', 'kouzhao@3x', 'lingmeng@3x', 'longxia@3x', 'longzhou@3x', 'mantou@3x', 'mushroom@3x', 'needle@3x', 'nian@3x',
        'nianyu@3x', 'niulang@3x', 'noodle@3x', 'pipi@3x', 'qinglv@3x', 'qingshu@3x', 'qiqiu@3x', 'qiuye@3x', 'rua@3x', 'shengridangao@3x',
        'shu@3x', 'shuijiao@3x', 'star@3x', 'tangguo@3x', 'tianyuebing@3x', 'tianzong@3x', 'tiger@3x', 'turtle@3x', 'tuzi@3x', 'weishengzhi@3x',
        'wenhao@3x', 'wocao@3x', 'xiandan@3x', 'xianyuebing@3x', 'xianzong@3x', 'xiaochou@3x', 'xique@3x', 'yijianchuanxin@3x', 'yueliang@3x', 'zhaoxiangji@3x',
        'zhijing@3x', 'zhinv@3x', '三蹦子@3x', '三轮车@3x', '两@3x', '中@3x', '乐@3x', '乐乐@3x', '买@3x', '今@3x',
        '刷@3x', '友@3x', '周@3x', '团@3x', '国@3x', '圆@3x', '大闸蟹@3x', '好@3x', '封号@3x', '岁@3x',
        '年@3x', '庆@3x', '快@3x', '快快@3x', '打工人@3x', '抱一下@3x', '拼@3x', '新@3x', '有@3x', '架火@3x',
        '歪嘴@3x', '烟花@3x', '烦@3x', '生@3x', '皮@3x', '皮一下@3x', '皮了么@3x', '秋@3x', '穷@3x', '穿云箭@3x',
        '芭了么@3x', '运@3x', '这@3x', '钱@3x', '黑大人@3x']

    },
  ],
}
